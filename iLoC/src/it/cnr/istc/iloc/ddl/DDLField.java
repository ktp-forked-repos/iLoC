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

import it.cnr.istc.iloc.Field;
import it.cnr.istc.iloc.api.IType;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DDLField extends Field {

    final DDLParser.ExprContext context;

    DDLField(DDLParser.ExprContext context, String name, IType type) {
        super(name, type);
        this.context = context;
    }
}
