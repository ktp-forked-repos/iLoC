/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.smt;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.Model;
import com.microsoft.z3.RatNum;
import it.cnr.istc.smt.api.IModel;
import it.cnr.istc.smt.api.ISMTBool;
import it.cnr.istc.smt.api.ISMTInt;
import it.cnr.istc.smt.api.ISMTReal;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class SMTModel implements IModel {

    private final Model model;

    SMTModel(Model model) {
        this.model = model;
    }

    @Override
    public Boolean evaluateBoolean(ISMTBool var) {
        BoolExpr evaluate = (BoolExpr) model.evaluate(((BoolVar) var).expr, false);
        switch (evaluate.getBoolValue()) {
            case Z3_L_TRUE:
                return true;
            case Z3_L_UNDEF:
                return null;
            case Z3_L_FALSE:
                return false;
            default:
                throw new AssertionError(evaluate.getBoolValue().name());
        }
    }

    @Override
    public BigInteger evaluateInt(ISMTInt var) {
        Expr evaluate = model.evaluate(((IntVar) var).expr, false);
        IntNum c_int = (IntNum) evaluate;
        return new BigInteger(c_int.toString());
    }

    @Override
    public BigDecimal evaluateReal(ISMTReal var) {
        Expr evaluate = model.evaluate(((RealVar) var).expr, false);
        RatNum c_real = (RatNum) evaluate;
        return new BigDecimal(c_real.toString());
    }

    @Override
    public String toString() {
        return model.toString();
    }
}
