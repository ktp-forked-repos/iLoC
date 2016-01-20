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

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Global;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.RealExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import it.cnr.istc.smt.api.IModel;
import it.cnr.istc.smt.api.ISMTBool;
import it.cnr.istc.smt.api.ISMTInt;
import it.cnr.istc.smt.api.ISMTNetwork;
import it.cnr.istc.smt.api.ISMTReal;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class SMTNetwork implements ISMTNetwork {

    private static final Logger LOG = Logger.getLogger(SMTNetwork.class.getName());
    private final Properties properties;
    private int n_vars = 0;
    private final Context ctx;
    private final Solver s;
    private IModel model;

    public SMTNetwork(Properties properties) {
        this.properties = properties;
        Global.setParameter("pp.decimal", "true");
        Global.setParameter("pp.max_depth", "4294967295");
        HashMap<String, String> cfg = new HashMap<>();
        cfg.put("model", "true");
        this.ctx = new Context(cfg);
        this.s = ctx.mkSolver();
    }

    @Override
    public ISMTBool newBool() {
        return new BoolVar(ctx.mkBoolConst("x" + n_vars++));
    }

    @Override
    public ISMTBool newBool(String constant) {
        return new BoolVar(ctx.mkBool(Boolean.parseBoolean(constant)));
    }

    @Override
    public ISMTBool not(ISMTBool var) {
        return new BoolVar(ctx.mkNot(((BoolVar) var).expr));
    }

    @Override
    public ISMTBool eq(ISMTBool var0, ISMTBool var1) {
        return new BoolVar(ctx.mkEq(((BoolVar) var0).expr, ((BoolVar) var1).expr));
    }

    @Override
    public ISMTBool and(ISMTBool... vars) {
        return new BoolVar(ctx.mkAnd(Stream.of(vars).map(v -> ((BoolVar) v).expr).toArray(BoolExpr[]::new)));
    }

    @Override
    public ISMTBool or(ISMTBool... vars) {
        return new BoolVar(ctx.mkOr(Stream.of(vars).map(v -> ((BoolVar) v).expr).toArray(BoolExpr[]::new)));
    }

    @Override
    public ISMTBool xor(ISMTBool var0, ISMTBool var1) {
        return new BoolVar(ctx.mkXor(((BoolVar) var0).expr, ((BoolVar) var1).expr));
    }

    @Override
    public ISMTInt newInt() {
        return new IntVar(ctx.mkIntConst("x" + n_vars++));
    }

    @Override
    public ISMTInt newInt(String constant) {
        return new IntVar(ctx.mkInt(constant));
    }

    @Override
    public ISMTInt add(ISMTInt... vars) {
        return new IntVar(ctx.mkAdd(Stream.of(vars).map(v -> ((IntVar) v).expr).toArray(ArithExpr[]::new)));
    }

    @Override
    public ISMTInt divide(ISMTInt var0, ISMTInt var1) {
        return new IntVar(ctx.mkDiv(((IntVar) var0).expr, ((IntVar) var1).expr));
    }

    @Override
    public ISMTInt multiply(ISMTInt... vars) {
        return new IntVar(ctx.mkMul(Stream.of(vars).map(v -> ((IntVar) v).expr).toArray(ArithExpr[]::new)));
    }

    @Override
    public ISMTInt subtract(ISMTInt var0, ISMTInt var1) {
        return new IntVar(ctx.mkSub(((IntVar) var0).expr, ((IntVar) var1).expr));
    }

    @Override
    public ISMTInt negate(ISMTInt var) {
        return new IntVar(ctx.mkUnaryMinus(((IntVar) var).expr));
    }

    @Override
    public ISMTBool leq(ISMTInt var0, ISMTInt var1) {
        return new BoolVar(ctx.mkLe(((IntVar) var0).expr, ((IntVar) var1).expr));
    }

    @Override
    public ISMTBool geq(ISMTInt var0, ISMTInt var1) {
        return new BoolVar(ctx.mkGe(((IntVar) var0).expr, ((IntVar) var1).expr));
    }

    @Override
    public ISMTBool lt(ISMTInt var0, ISMTInt var1) {
        return new BoolVar(ctx.mkLt(((IntVar) var0).expr, ((IntVar) var1).expr));
    }

    @Override
    public ISMTBool gt(ISMTInt var0, ISMTInt var1) {
        return new BoolVar(ctx.mkGt(((IntVar) var0).expr, ((IntVar) var1).expr));
    }

    @Override
    public ISMTBool eq(ISMTInt var0, ISMTInt var1) {
        return new BoolVar(ctx.mkEq(((IntVar) var0).expr, ((IntVar) var1).expr));
    }

    @Override
    public ISMTReal newReal() {
        return new RealVar(ctx.mkRealConst("x" + n_vars++));
    }

    @Override
    public ISMTReal newReal(String constant) {
        return new RealVar(ctx.mkReal(constant));
    }

    @Override
    public ISMTReal add(ISMTReal... vars) {
        return new RealVar(ctx.mkAdd(Stream.of(vars).map(v -> ((RealVar) v).expr).toArray(ArithExpr[]::new)));
    }

    @Override
    public ISMTReal divide(ISMTReal var0, ISMTReal var1) {
        return new RealVar(ctx.mkDiv(((RealVar) var0).expr, ((RealVar) var1).expr));
    }

    @Override
    public ISMTReal multiply(ISMTReal... vars) {
        return new RealVar(ctx.mkMul(Stream.of(vars).map(v -> ((RealVar) v).expr).toArray(ArithExpr[]::new)));
    }

    @Override
    public ISMTReal subtract(ISMTReal var0, ISMTReal var1) {
        return new RealVar(ctx.mkSub(((RealVar) var0).expr, ((RealVar) var1).expr));
    }

    @Override
    public ISMTReal negate(ISMTReal var) {
        return new RealVar(ctx.mkUnaryMinus(((RealVar) var).expr));
    }

    @Override
    public ISMTBool leq(ISMTReal var0, ISMTReal var1) {
        return new BoolVar(ctx.mkLe(((RealVar) var0).expr, ((RealVar) var1).expr));
    }

    @Override
    public ISMTBool geq(ISMTReal var0, ISMTReal var1) {
        return new BoolVar(ctx.mkLe(((RealVar) var0).expr, ((RealVar) var1).expr));
    }

    @Override
    public ISMTBool lt(ISMTReal var0, ISMTReal var1) {
        return new BoolVar(ctx.mkLe(((RealVar) var0).expr, ((RealVar) var1).expr));
    }

    @Override
    public ISMTBool gt(ISMTReal var0, ISMTReal var1) {
        return new BoolVar(ctx.mkLe(((RealVar) var0).expr, ((RealVar) var1).expr));
    }

    @Override
    public ISMTBool eq(ISMTReal var0, ISMTReal var1) {
        return new BoolVar(ctx.mkLe(((RealVar) var0).expr, ((RealVar) var1).expr));
    }

    @Override
    public ISMTReal toReal(ISMTInt val) {
        return new RealVar(ctx.mkInt2Real((IntExpr) ((IntVar) val).expr));
    }

    @Override
    public ISMTInt toInt(ISMTReal val) {
        return new IntVar(ctx.mkReal2Int((RealExpr) ((RealVar) val).expr));
    }

    @Override
    public void assertFacts(ISMTBool... facts) {
        s.add(Stream.of(facts).map(v -> ((BoolVar) v).expr).toArray(BoolExpr[]::new));
    }

    @Override
    public boolean checkFacts(ISMTBool... facts) {
        Status check = s.check(Stream.of(facts).map(v -> ((BoolVar) v).expr).toArray(BoolExpr[]::new));
        switch (check) {
            case UNSATISFIABLE:
                return false;
            case UNKNOWN:
                return false;
            case SATISFIABLE:
                return true;
            default:
                throw new AssertionError(check.name());
        }
    }

    @Override
    public boolean propagate() {
        Status check = s.check();
        switch (check) {
            case UNSATISFIABLE:
                model = null;
                return false;
            case UNKNOWN:
                model = null;
                return false;
            case SATISFIABLE:
                model = new SMTModel(s.getModel());
                return true;
            default:
                throw new AssertionError(check.name());
        }
    }

    @Override
    public IModel getModel() {
        return model;
    }

    @Override
    public void push() {
        s.push();
    }

    @Override
    public void pop() {
        s.pop();
    }

    @Override
    public void reset() {
        s.reset();
    }

    @Override
    public String toString() {
        return s.toString();
    }
}
