/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.iloc.translators.mrcpsp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class MRCPSPMaxTranslator {

    public static String translate(File file) throws IOException {
        final List<Resource> renewable_resources = new ArrayList<>();
        final List<Resource> nonrenewable_resources = new ArrayList<>();
        final List<Resource> doubly_constrained_resources = new ArrayList<>();
        final Map<Integer, Activity> activities = new LinkedHashMap<>();
        final List<Constraint> constraints = new ArrayList<>();

        MRCPSPParser parser = new MRCPSPParser(new CommonTokenStream(new MRCPSPLexer(new ANTLRFileStream(file.getPath()))));
        MRCPSPParser.Compilation_unitContext context = parser.compilation_unit();

        // Creating resources
        for (int i = 0; i < parser.n_renewable_resources; i++) {
            renewable_resources.add(new Resource("rr" + i, context.capacities().renewable_resources_capacities().positive_number(i).v));
        }
        for (int i = 0; i < parser.n_nonrenewable_resources; i++) {
            nonrenewable_resources.add(new Resource("cr" + i, context.capacities().nonrenewable_resources_capacities().positive_number(i).v));
        }
        if (parser.n_doubly_constrained_resources > 0) {
            throw new UnsupportedOperationException("Doubly-constrained resources are not supported yet..");
        }

        // Creating all activities (also initial and final one!)
        for (int i = 0; i <= parser.n_real_activities + 1; i++) {
            Activity activity = new Activity(i, renewable_resources, nonrenewable_resources, doubly_constrained_resources);
            for (int j = 0; j < context.activities().activity(i).n_modes.v; j++) {
                List<String> renewable_resource_usages = new ArrayList<>(parser.n_renewable_resources);
                List<String> nonrenewable_resource_usages = new ArrayList<>(parser.n_nonrenewable_resources);
                for (int k = 0; k < parser.n_renewable_resources; k++) {
                    renewable_resource_usages.add(Integer.toString(context.resource_usages().resource_usage(i).modes().activity_mode(j).renewable_resources_uses().positive_number(k).v));
                }
                for (int k = 0; k < parser.n_nonrenewable_resources; k++) {
                    nonrenewable_resource_usages.add(Integer.toString(context.resource_usages().resource_usage(i).modes().activity_mode(j).nonrenewable_resources_uses().positive_number(k).v));
                }
                Mode mode = new Mode(j + 1, context.resource_usages().resource_usage(i).modes().activity_mode(j).activity_duration.v, renewable_resource_usages, nonrenewable_resource_usages);
                activity.modes.add(mode);
            }
            activities.put(activity.id, activity);
        }

        // Creating constraints
        for (int i = 0; i <= parser.n_real_activities + 1; i++) {
            Activity activity = activities.get(i);
            for (int j = 0; j < context.activities().activity(i).n_direct_successors.v; j++) {
                List<String> weights = new ArrayList<>(context.activities().activity(i).weights(j).number().size());
                for (MRCPSPParser.NumberContext numberContext : context.activities().activity(i).weights(j).number()) {
                    weights.add(Integer.toString(numberContext.v));
                }
                activity.addSuccessor(new Successor(activities.get(context.activities().activity(i).direct_successors().positive_number(j).v), weights));
            }
            for (Successor successor : activity.successors) {
                int j = 0;
                for (Mode mode0 : activity.modes) {
                    for (Mode mode1 : successor.activity.modes) {
                        constraints.add(new Constraint(activity, mode0, successor.activity, mode1, Integer.parseInt(successor.weights.get(j++)), activities));
                    }
                }
            }
        }

        ST translation = new STGroupFile(MRCPSPMaxTranslator.class.getResource("MRCPSPMaxTemplate.stg").getPath()).getInstanceOf("MRCPSPMax");
        translation.add("class_name", "RCPSP_max");
        translation.add("instance_name", "rcpsp_max");

        if (renewable_resources.isEmpty()) {
            translation.add("renewable_resources", new ST.AttributeList());
        } else {
            renewable_resources.stream().forEach((resource) -> {
                translation.add("renewable_resources", resource);
            });
        }
        if (nonrenewable_resources.isEmpty()) {
            translation.add("nonrenewable_resources", new ST.AttributeList());
        } else {
            nonrenewable_resources.stream().forEach((resource) -> {
                translation.add("nonrenewable_resources", resource);
            });
        }
        if (doubly_constrained_resources.isEmpty()) {
            translation.add("doubly_constrained_resources", new ST.AttributeList());
        } else {
            doubly_constrained_resources.stream().forEach((resource) -> {
                translation.add("doubly_constrained_resources", resource);
            });
        }

        activities.values().stream().forEach((activity) -> {
            translation.add("activities", activity);
        });

        constraints.stream().forEach((constraint) -> {
            translation.add("constraints", constraint);
        });

        return translation.render();
    }

    private static class Resource {

        private final String name;
        private final int capacity;

        Resource(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }

        public String getName() {
            return name;
        }

        public int getCapacity() {
            return capacity;
        }
    }

    private static class Activity {

        private final int id;
        private final List<Resource> renewable_resources;
        private final List<Resource> nonrenewable_resources;
        private final List<Resource> doubly_constrained_resources;
        private final List<Mode> modes = new ArrayList<>();
        private final List<Successor> successors = new ArrayList<>();

        Activity(int id, List<Resource> renewable_resources, List<Resource> nonrenewable_resources, List<Resource> doubly_constrained_resources) {
            this.id = id;
            this.renewable_resources = renewable_resources;
            this.nonrenewable_resources = nonrenewable_resources;
            this.doubly_constrained_resources = doubly_constrained_resources;
        }

        public int getId() {
            return id;
        }

        public List<Mode> getModes() {
            return Collections.unmodifiableList(modes);
        }

        public void addSuccessor(Successor successor) {
            successors.add(successor);
        }

        public List<Successor> getSuccessors() {
            return Collections.unmodifiableList(successors);
        }

        @Override
        public String toString() {
            ST translation = new STGroupFile(MRCPSPMaxTranslator.class.getResource("MRCPSPMaxTemplate.stg").getPath()).getInstanceOf("Activity");

            if (renewable_resources.isEmpty()) {
                translation.add("renewable_resources", new ST.AttributeList());
            } else {
                renewable_resources.stream().forEach((resource) -> {
                    translation.add("renewable_resources", resource);
                });
            }
            if (nonrenewable_resources.isEmpty()) {
                translation.add("nonrenewable_resources", new ST.AttributeList());
            } else {
                nonrenewable_resources.stream().forEach((resource) -> {
                    translation.add("nonrenewable_resources", resource);
                });
            }
            if (doubly_constrained_resources.isEmpty()) {
                translation.add("doubly_constrained_resources", new ST.AttributeList());
            } else {
                doubly_constrained_resources.stream().forEach((resource) -> {
                    translation.add("doubly_constrained_resources", resource);
                });
            }

            translation.add("activity", this);
            return translation.render();
        }
    }

    private static class Mode {

        private final int id;
        private final int duration;
        private final List<String> renewable_resource_usages;
        private final List<String> nonrenewable_resource_usages;

        Mode(int id, int duration, List<String> renewable_resource_usages, List<String> nonrenewable_resource_usages) {
            this.id = id;
            this.duration = duration;
            this.renewable_resource_usages = renewable_resource_usages;
            this.nonrenewable_resource_usages = nonrenewable_resource_usages;
        }

        public int getId() {
            return id;
        }

        public int getDuration() {
            return duration;
        }

        public List<String> getRenewable_resource_usages() {
            return Collections.unmodifiableList(renewable_resource_usages);
        }

        public List<String> getNonrenewable_resource_usages() {
            return Collections.unmodifiableList(nonrenewable_resource_usages);
        }
    }

    private static class Successor {

        private final Activity activity;
        private final List<String> weights;

        Successor(Activity activity, List<String> weights) {
            this.activity = activity;
            this.weights = weights;
        }

        public Activity getActivity() {
            return activity;
        }

        public List<String> getWeights() {
            return Collections.unmodifiableList(weights);
        }
    }

    private static class Constraint {

        private final Activity first_activity;
        private final Mode first_mode;
        private final Activity second_activity;
        private final Mode second_mode;
        private final int weight;
        private final Map<Integer, Activity> activities;

        Constraint(Activity first_activity, Mode first_mode, Activity second_activity, Mode second_mode, int weight, Map<Integer, Activity> activities) {
            this.first_activity = first_activity;
            this.first_mode = first_mode;
            this.second_activity = second_activity;
            this.second_mode = second_mode;
            this.weight = weight;
            this.activities = activities;
        }

        public Activity getFirst_activity() {
            return first_activity;
        }

        public Mode getFirst_mode() {
            return first_mode;
        }

        public Activity getSecond_activity() {
            return second_activity;
        }

        public Mode getSecond_mode() {
            return second_mode;
        }

        public String getWeight() {
            return Integer.toString(weight >= 0 ? weight : -weight);
        }

        public boolean isMinimum() {
            return weight >= 0;
        }

        @Override
        public String toString() {
            ST translation = null;

            if (activities.get(0) == first_activity) {
                translation = new STGroupFile(MRCPSPMaxTranslator.class.getResource("MRCPSPMaxTemplate.stg").getPath()).getInstanceOf("FromFirstActivityConstraint");
            } else if (activities.get(activities.size() - 1) == second_activity) {
                translation = new STGroupFile(MRCPSPMaxTranslator.class.getResource("MRCPSPMaxTemplate.stg").getPath()).getInstanceOf("ToLastActivityConstraint");
            } else {
                translation = new STGroupFile(MRCPSPMaxTranslator.class.getResource("MRCPSPMaxTemplate.stg").getPath()).getInstanceOf("Constraint");
            }

            translation.add("constraint", this);
            return translation.render();
        }
    }

    private MRCPSPMaxTranslator() {
    }
}
