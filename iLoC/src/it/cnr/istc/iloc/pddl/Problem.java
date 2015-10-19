/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package it.cnr.istc.iloc.pddl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Problem {

    private final Domain domain;
    private final String name;
    private final Map<String, Constant> objects = new LinkedHashMap<>();
    private final List<Term> init_els = new ArrayList<>();
    private Term goal;

    Problem(Domain domain, String name) {
        this.domain = domain;
        this.name = name;
    }

    public Domain getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public Constant getObject(String name) {
        return objects.get(name);
    }

    public Map<String, Constant> getObjects() {
        return Collections.unmodifiableMap(objects);
    }

    public void addObject(Constant object) {
        objects.put(object.getName(), object);
    }

    public List<Term> getInitEls() {
        return Collections.unmodifiableList(init_els);
    }

    public void addInitEl(Term init_el) {
        init_els.add(init_el);
    }

    public Term getGoal() {
        return goal;
    }

    public void setGoal(Term goal) {
        this.goal = goal;
    }
}
