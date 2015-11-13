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
package it.cnr.istc.iloc.translators.pddl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StateVariableValue {

    private final StateVariable stateVariable;
    private final String name;
    private final Collection<Action> actions = new ArrayList<>();
    private final Collection<DurativeAction> atStartDurativeActions = new ArrayList<>();
    private final Collection<DurativeAction> atEndDurativeActions = new ArrayList<>();
    private Double lb;

    StateVariableValue(StateVariable state_variable, String name) {
        this.stateVariable = state_variable;
        this.name = name;
    }

    public StateVariable getStateVariable() {
        return stateVariable;
    }

    public String getName() {
        return name;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actions);
    }

    public void addAtStartDurativeAction(DurativeAction action) {
        atStartDurativeActions.add(action);
    }

    public void removeAtStartDurativeAction(DurativeAction action) {
        atStartDurativeActions.remove(action);
    }

    public Collection<DurativeAction> getAtStartDurativeActions() {
        return Collections.unmodifiableCollection(atStartDurativeActions);
    }

    public void addAtEndDurativeAction(DurativeAction action) {
        atEndDurativeActions.add(action);
    }

    public void removeAtEndDurativeAction(DurativeAction action) {
        atEndDurativeActions.remove(action);
    }

    public Collection<DurativeAction> getAtEndDurativeActions() {
        return Collections.unmodifiableCollection(atEndDurativeActions);
    }

    public int getSize() {
        return actions.size() + atStartDurativeActions.size() + atEndDurativeActions.size();
    }

    public boolean isLeaf() {
        return getSize() == 0;
    }

    public Double getLb() {
        return lb;
    }

    public void setLb(Double lb) {
        this.lb = lb;
    }

    @Override
    public String toString() {
        return stateVariable.getName() + "." + name + "()";
    }
}
