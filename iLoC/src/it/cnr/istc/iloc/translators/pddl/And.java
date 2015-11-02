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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class And implements Env {

    private final Env enclosingGD;
    private final List<Env> envs = new ArrayList<>();
    private boolean consistent = true;

    And(Env enclosingGD) {
        this.enclosingGD = enclosingGD;
    }

    @Override
    public Env getEnclosingEnv() {
        return enclosingGD;
    }

    @Override
    public void addEnv(Env gd) {
        envs.add(gd);
    }

    public List<Env> getEnvs() {
        return Collections.unmodifiableList(envs);
    }

    @Override
    public boolean isConsistent() {
        return consistent;
    }

    @Override
    public void setConsistent(boolean consistent) {
        this.consistent = consistent;
    }

    @Override
    public void simplify() {
        if (envs.stream().allMatch(env -> env.isConsistent())) {
            envs.stream().forEach(env -> env.simplify());
            this.consistent = envs.stream().allMatch(env -> env.isConsistent());
        } else {
            this.consistent = false;
        }
    }
}
