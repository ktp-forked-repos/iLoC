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
package it.cnr.istc.iloc.ddl;

import it.cnr.istc.iloc.Type;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DDLEnumType extends Type {

    private final List<IString> enums = new ArrayList<>();

    DDLEnumType(ISolver solver, IScope enclosingScope, String name) {
        super(solver, enclosingScope, name);
    }

    void addEnum(IString enum_constant) {
        enums.add(enum_constant);
    }

    void addEnums(List<IString> enums) {
        enums.addAll(enums);
    }

    List<IString> getEnums() {
        return Collections.unmodifiableList(enums);
    }

    @Override
    public IObject newInstance(IEnvironment enclosing_environment) {
        IConstraintNetwork network = solver.getConstraintNetwork();
        return network.newEnum(this, enums);
    }
}
