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

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.ISolver;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Formula extends SimpleObject implements IFormula {

    private final IFormula cause;
    private FormulaState state = FormulaState.Inactive;

    Formula(ISolver solver, IFormula cause, IPredicate predicate, Map<String, IObject> assignments) {
        super(solver, assignments.containsKey(Constants.SCOPE) ? assignments.get(Constants.SCOPE) : solver, predicate);
        this.cause = cause;

        // We assign values to fields
        assert assignments.keySet().stream().noneMatch(field_name -> (!predicate.getFields().containsKey(field_name))) : assignments.keySet() + " not in " + predicate.getFields().keySet();
        assignments.entrySet().forEach(entry -> {
            set(entry.getKey(), entry.getValue());
        });

        // We instantiate unassigned fields
        predicate.getFields().values().stream().filter(field -> !field.isSynthetic() && !assignments.containsKey(field.getName())).forEach(field -> {
            if (!field.getType().isPrimitive()) {
                set(field.getName(), field.getType().newExistential());
            } else {
                set(field.getName(), field.getType().newInstance(this));
            }
        });
    }

    @Override
    public IFormula getCause() {
        return cause;
    }

    @Override
    public IPredicate getType() {
        return (IPredicate) super.getType();
    }

    @Override
    public FormulaState getFormulaState() {
        return state;
    }

    @Override
    public void setActiveState() {
        assert state == FormulaState.Inactive;
        state = FormulaState.Active;
        getType().getEnclosingScope().formulaActivated(this);
        getSolver().getDynamicCausalGraph().formulaActivated(this);
    }

    @Override
    public void setUnifiedState(List<IFormula> formulas, List<IBool> constraints) {
        assert state == FormulaState.Inactive;
        state = FormulaState.Unified;
        getType().getEnclosingScope().formulaUnified(this, formulas, constraints);
        getSolver().getDynamicCausalGraph().formulaUnified(this, formulas, constraints);
    }

    @Override
    public void setInactiveState() {
        assert state == FormulaState.Active || state == FormulaState.Unified;
        state = FormulaState.Inactive;
        getType().getEnclosingScope().formulaInactivated(this);
        getSolver().getDynamicCausalGraph().formulaInactivated(this);
    }

    @Override
    public String toString() {
        return getType().getName() + "(" + getType().getFields().values().stream().filter(field -> !field.isSynthetic()).map(f -> f.getType().getName() + " " + f.getName() + " " + get(f.getName())).collect(Collectors.joining(", ")) + ")";
    }
}
