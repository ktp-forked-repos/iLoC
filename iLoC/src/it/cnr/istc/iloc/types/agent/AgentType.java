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
package it.cnr.istc.iloc.types.agent;

import it.cnr.istc.iloc.Field;
import it.cnr.istc.iloc.Type;
import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.ISolver;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class AgentType extends Type {

    public static final String TYPE_NAME = "Agent";
    private final Properties properties;

    public AgentType(ISolver solver, Properties properties) {
        super(solver, solver, TYPE_NAME);
        this.properties = properties;
    }

    @Override
    public void predicateDefined(IPredicate predicate) {
        try {
            predicate.defineField(new Field(Constants.START, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.END, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.DURATION, enclosingScope.getType(Constants.NUMBER)));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AgentType.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Let's add temporal constraints to the formula whenever the formula is
     * activated:
     * <p>
     * duration = end - start;
     * <p>
     * start >= origin;
     * <p>
     * end {@literal <=} horizon;
     * <p>
     * duration {@literal >=} 0;
     *
     * @param formula the formula that has been created.
     */
    @Override
    public void formulaActivated(IFormula formula) {
        IConstraintNetwork network = solver.getConstraintNetwork();
        final INumber start = formula.get(Constants.START);
        final INumber end = formula.get(Constants.END);
        final INumber duration = formula.get(Constants.DURATION);
        final INumber origin = solver.get(Constants.ORIGIN);
        final INumber horizon = solver.get(Constants.HORIZON);
        network.assertFacts(
                network.eq(duration, network.subtract(end, start)),
                network.geq(start, origin),
                network.leq(end, horizon),
                network.geq(duration, network.newReal("0"))
        );
    }
}
