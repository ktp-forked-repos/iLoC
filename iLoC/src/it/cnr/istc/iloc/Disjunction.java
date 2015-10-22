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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IDisjunction;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Disjunction extends Scope implements IDisjunction {

    private final Collection<IDisjunct> disjuncts = new ArrayList<>();

    public Disjunction(ISolver solver, IScope enclosingScope) {
        super(solver, enclosingScope);
    }

    @Override
    public void addDisjunct(IDisjunct disjunct) {
        this.disjuncts.add(disjunct);
    }

    @Override
    public Collection<IDisjunct> getDisjuncts() {
        return Collections.unmodifiableCollection(disjuncts);
    }
}
