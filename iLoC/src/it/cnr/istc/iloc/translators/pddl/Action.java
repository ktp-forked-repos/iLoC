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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Action {

    private final String name;
    private final Env precondition = new And(null);
    private final Env effect = new And(null);

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Env getPrecondition() {
        return precondition;
    }

    public Env getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(action ").append(name).append("\n");
        sb.append("(:precondition ").append(precondition.toString()).append(")\n");
        sb.append("(:effect ").append(effect.toString()).append(")\n");
        sb.append(")");
        return sb.toString();
    }
}
