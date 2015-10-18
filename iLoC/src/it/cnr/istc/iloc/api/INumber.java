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
package it.cnr.istc.iloc.api;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface INumber extends IObject {

    @Override
    public default IBool eq(IObject expression) {
        if (this == expression) {
            // Objects are exactly the same..
            return getSolver().getConstraintNetwork().newBool("true");
        }
        if (expression instanceof INumber) {
            return getSolver().getConstraintNetwork().eq(this, (INumber) expression);
        } else if (expression instanceof IEnum) {
            IConstraintNetwork network = getSolver().getConstraintNetwork();
            IObject[] expression_enums = ((IEnum) expression).getEnums();
            List<IBool> c_terms = new ArrayList<>(expression_enums.length);
            for (int i = 0; i < expression_enums.length; i++) {
                c_terms.add(network.and(
                        network.eq(((IEnum) expression).getVar(), network.newInt(Integer.toString(i))),
                        eq(expression_enums[i])
                ));
            }
            if (c_terms.isEmpty()) {
                return network.newBool("false");
            } else if (c_terms.size() == 1) {
                return c_terms.get(0);
            } else {
                return network.or(c_terms.toArray(new IBool[c_terms.size()]));
            }
        } else {
            return getSolver().getConstraintNetwork().newBool("false");
        }
    }
}
