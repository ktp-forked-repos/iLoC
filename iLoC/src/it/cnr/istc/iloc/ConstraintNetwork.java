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
package it.cnr.istc.iloc;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Global;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.Model;
import com.microsoft.z3.RatNum;
import com.microsoft.z3.RealExpr;
import com.microsoft.z3.Status;
import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IEnum;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IInt;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IReal;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IString;
import it.cnr.istc.iloc.api.IType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ConstraintNetwork implements IConstraintNetwork {

    private static final Logger LOG = Logger.getLogger(ConstraintNetwork.class.getName());
    private final ISolver solver;
    private final Properties properties;
    private IType boolType;
    private IType intType;
    private IType realType;
    private IType stringType;
    private int n_vars = 0;
    private final Context ctx;
    private final com.microsoft.z3.Solver s;
    private IModel model;
    private final int scale;

    ConstraintNetwork(ISolver solver, Properties properties) {
        this.solver = solver;
        this.properties = properties;
        try {
            this.boolType = solver.getType(Constants.BOOL);
            this.intType = solver.getType(Constants.INT);
            this.realType = solver.getType(Constants.REAL);
            this.stringType = solver.getType(Constants.STRING);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConstraintNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.loadLibrary("libz3");
        Global.setParameter("pp.decimal", "true");
        Global.setParameter("pp.max_depth", "4294967295");
        HashMap<String, String> cfg = new HashMap<>();
        cfg.put("model", "true");
        this.ctx = new Context(cfg);
        this.s = ctx.mkSolver();
        this.scale = Integer.parseInt(properties.getProperty("Scale", "5"));
//        LOG.log(Level.INFO, "Z3 version: {0}", Version.getString());
    }

    @Override
    public IBool newBool() {
        return new Z3Bool(ctx.mkBoolConst("x" + n_vars++));
    }

    @Override
    public IBool newBool(String constant) {
        return new Z3Bool(ctx.mkBool(Boolean.parseBoolean(constant)));
    }

    @Override
    public IInt newInt() {
        return new Z3Int(ctx.mkIntConst("x" + n_vars++));
    }

    @Override
    public IInt newInt(String constant) {
        return new Z3Int(ctx.mkInt(constant));
    }

    @Override
    public IReal newReal() {
        return new Z3Real(ctx.mkRealConst("x" + n_vars++));
    }

    @Override
    public IReal newReal(String constant) {
        return new Z3Real(ctx.mkReal(constant));
    }

    @Override
    public IReal toReal(IInt val) {
        return new Z3Real(ctx.mkInt2Real((IntExpr) ((Z3Number) val).expr));
    }

    @Override
    public IInt toInt(IReal val) {
        return new Z3Int(ctx.mkReal2Int((RealExpr) ((Z3Number) val).expr));
    }

    @Override
    public IBool isInt(IReal val) {
        return new Z3Bool(ctx.mkIsInteger((RealExpr) ((Z3Number) val).expr));
    }

    @Override
    public IString newString() {
        return new Z3String();
    }

    @Override
    public IString newString(String constant) {
        return new Z3String(constant);
    }

    @Override
    public IEnum newEnum(IType type, List<? extends IObject> values) {
        assert !values.isEmpty();
        assert values.stream().anyMatch(value -> type.isAssignableFrom(value.getType()));
        IInt sel_var = null;
        if (values.size() == 1) {
            sel_var = newInt("0");
        } else {
            sel_var = newInt();
        }
        IEnum c_enum = new Z3Enum(solver, solver, type, sel_var, values.toArray(new IObject[values.size()]));
        if (values.size() > 1) {
            solver.getCurrentNode().addResolver(new IResolver() {
                private boolean resolved = false;

                @Override
                public double getKnownCost() {
                    return 0;
                }

                @Override
                public void resolve() {
                    assert !resolved;
                    s.add(
                            ctx.mkGe(((Z3Number) c_enum.getVar()).expr, ctx.mkInt("0")),
                            ctx.mkLe(((Z3Number) c_enum.getVar()).expr, ctx.mkInt(Integer.toString(values.size() - 1)))
                    );
                    resolved = true;
                }

                @Override
                public boolean isResolved() {
                    return resolved;
                }

                @Override
                public void retract() {
                    assert resolved;
                    resolved = false;
                }
            });
        }
        return c_enum;
    }

    @Override
    public IEnum newEnum(IType type, IInt var, List<? extends IObject> values) {
        assert values.stream().anyMatch(value -> type.isAssignableFrom(value.getType()));
        return new Z3Enum(solver, solver, type, var, values.toArray(new IObject[values.size()]));
    }

    @Override
    public IBool not(IBool val) {
        return new Z3Bool(ctx.mkNot(((Z3Bool) val).expr));
    }

    @Override
    public IBool eq(IBool val0, IBool val1) {
        return new Z3Bool(ctx.mkEq(((Z3Bool) val0).expr, ((Z3Bool) val1).expr));
    }

    @Override
    public IBool and(IBool... val) {
        return new Z3Bool(ctx.mkAnd(Stream.of(val).map(v -> ((Z3Bool) v).expr).toArray(BoolExpr[]::new)));
    }

    @Override
    public IBool or(IBool... val) {
        return new Z3Bool(ctx.mkOr(Stream.of(val).map(v -> ((Z3Bool) v).expr).toArray(BoolExpr[]::new)));
    }

    @Override
    public IBool xor(IBool... val) {
        BoolExpr expr = ((Z3Bool) val[0]).expr;
        for (int i = 1; i < val.length; i++) {
            expr = ctx.mkXor(expr, ((Z3Bool) val[i]).expr);
        }
        return new Z3Bool(expr);
    }

    @Override
    public INumber add(INumber... val) {
        if (Stream.of(val).allMatch(v -> v.getType() == intType)) {
            return new Z3Int(ctx.mkAdd(Stream.of(val).map(v -> ((Z3Number) v).expr).toArray(ArithExpr[]::new)));
        } else {
            return new Z3Real(ctx.mkAdd(Stream.of(val).map(v -> ((Z3Number) v).expr).toArray(ArithExpr[]::new)));
        }
    }

    @Override
    public INumber divide(INumber val0, INumber val1) {
        if (val0.getType() == intType && val1.getType() == intType) {
            return new Z3Int(ctx.mkDiv(((Z3Number) val0).expr, ((Z3Number) val1).expr));
        } else {
            return new Z3Real(ctx.mkDiv(((Z3Number) val0).expr, ((Z3Number) val1).expr));
        }
    }

    @Override
    public INumber multiply(INumber... val) {
        if (Stream.of(val).allMatch(v -> v.getType() == intType)) {
            return new Z3Int(ctx.mkMul(Stream.of(val).map(v -> ((Z3Number) v).expr).toArray(ArithExpr[]::new)));
        } else {
            return new Z3Real(ctx.mkMul(Stream.of(val).map(v -> ((Z3Number) v).expr).toArray(ArithExpr[]::new)));
        }
    }

    @Override
    public INumber subtract(INumber val0, INumber val1) {
        if (val0.getType() == intType && val1.getType() == intType) {
            return new Z3Int(ctx.mkSub(((Z3Number) val0).expr, ((Z3Number) val1).expr));
        } else {
            return new Z3Real(ctx.mkSub(((Z3Number) val0).expr, ((Z3Number) val1).expr));
        }
    }

    @Override
    public INumber negate(INumber val) {
        if (val.getType() == intType) {
            return new Z3Int(ctx.mkUnaryMinus(((Z3Number) val).expr));
        } else {
            return new Z3Real(ctx.mkUnaryMinus(((Z3Number) val).expr));
        }
    }

    @Override
    public IBool leq(INumber val0, INumber val1) {
        return new Z3Bool(ctx.mkLe(((Z3Number) val0).expr, ((Z3Number) val1).expr));
    }

    @Override
    public IBool geq(INumber val0, INumber val1) {
        return new Z3Bool(ctx.mkGe(((Z3Number) val0).expr, ((Z3Number) val1).expr));
    }

    @Override
    public IBool lt(INumber val0, INumber val1) {
        return new Z3Bool(ctx.mkLt(((Z3Number) val0).expr, ((Z3Number) val1).expr));
    }

    @Override
    public IBool gt(INumber val0, INumber val1) {
        return new Z3Bool(ctx.mkGt(((Z3Number) val0).expr, ((Z3Number) val1).expr));
    }

    @Override
    public IBool eq(INumber val0, INumber val1) {
        return new Z3Bool(ctx.mkEq(((Z3Number) val0).expr, ((Z3Number) val1).expr));
    }

    @Override
    public void assertFacts(IBool... facts) {
        s.add(Stream.of(facts).map(v -> ((Z3Bool) v).expr).toArray(BoolExpr[]::new));
    }

    @Override
    public boolean checkFacts(IBool... facts) {
        s.push();
        s.add(Stream.of(facts).map(v -> ((Z3Bool) v).expr).toArray(BoolExpr[]::new));
        Status check = s.check();
        s.pop();
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
                model = new Z3Model(s.getModel());
                return true;
            default:
                throw new AssertionError(check.name());
        }
    }

    @Override
    public void minimize(INumber objective_function) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void maximize(INumber objective_function) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    private final class Z3Bool extends SimpleObject implements IBool {

        private final BoolExpr expr;

        Z3Bool(BoolExpr expr) {
            super(ConstraintNetwork.this.solver, ConstraintNetwork.this.solver, boolType);
            this.expr = expr;
        }

        @Override
        public IBool eq(IObject expression) {
            return IBool.super.eq(expression);
        }

        @Override
        public String toString() {
            return expr.getSExpr();
        }
    }

    private class Z3Number extends SimpleObject implements INumber {

        protected final ArithExpr expr;

        Z3Number(IType type, ArithExpr expr) {
            super(ConstraintNetwork.this.solver, ConstraintNetwork.this.solver, type);
            this.expr = expr;
        }

        @Override
        public IBool eq(IObject expression) {
            return INumber.super.eq(expression);
        }

        @Override
        public String toString() {
            return expr.getSExpr();
        }
    }

    private final class Z3Int extends Z3Number implements IInt {

        Z3Int(ArithExpr expr) {
            super(intType, expr);
        }
    }

    private final class Z3Real extends Z3Number implements IReal {

        Z3Real(ArithExpr expr) {
            super(realType, expr);
        }
    }

    private final class Z3String extends SimpleObject implements IString {

        private String value;

        Z3String() {
            super(ConstraintNetwork.this.solver, ConstraintNetwork.this.solver, stringType);
        }

        Z3String(String value) {
            super(ConstraintNetwork.this.solver, ConstraintNetwork.this.solver, stringType);
            this.value = value;
        }

        String getValue() {
            return value;
        }

        @Override
        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final class Z3Enum extends SimpleObject implements IEnum {

        private final IInt sel_var;
        private final IObject[] enums;

        Z3Enum(ISolver solver, IEnvironment enclosing_environment, IType type, IInt sel_var, IObject[] enums) {
            super(solver, enclosing_environment, type);
            assert Stream.of(enums).noneMatch(Objects::isNull);
            assert Stream.of(enums).allMatch(object -> type.isAssignableFrom(object.getType()));
            this.sel_var = sel_var;
            this.enums = enums;
        }

        @Override
        public IInt getVar() {
            return sel_var;
        }

        @Override
        public IObject[] getEnums() {
            return enums;
        }

        @Override
        public <T extends IObject> T get(String name) {
            if (!super.getObjects().containsKey(name) && getType().getFields().containsKey(name)) {
                try {
                    // Notice that different enums can address the same object..
                    // This will result in a new enum variable whose enums might contain duplicates..
                    super.set(name, new Z3Enum(solver, solver, getType().getField(name).getType(), sel_var, Stream.of(enums).map(v -> v.get(name)).toArray(IObject[]::new)));
                } catch (NoSuchFieldException ex) {
                    Logger.getLogger(ConstraintNetwork.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return super.get(name);
        }

        @Override
        public void set(String name, IObject object) {
            throw new AssertionError("Could not invoke set on enums..");
        }

        @Override
        public String toString() {
            return "E" + ((Z3Number) sel_var).expr.getSExpr();
        }
    }

    private final class Z3Model implements IModel {

        private final Model model;

        Z3Model(Model model) {
            this.model = model;
        }

        @Override
        public Boolean evaluate(IBool var) {
            BoolExpr evaluate = (BoolExpr) model.evaluate(((Z3Bool) var).expr, false);
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
        public BigDecimal evaluate(INumber var) {
            return (var instanceof IInt) ? evaluate((IInt) var) : evaluate((IReal) var);
        }

        @Override
        public BigDecimal evaluate(IInt var) {
            Expr evaluate = model.evaluate(((Z3Number) var).expr, false);
            IntNum c_int = (IntNum) evaluate;
            return new BigDecimal(c_int.toString());
        }

        @Override
        public BigDecimal evaluate(IReal var) {
            Expr evaluate = model.evaluate(((Z3Number) var).expr, false);
            RatNum c_real = (RatNum) evaluate;
            return new BigDecimal(c_real.toDecimalString(scale).replace("?", ""));
        }

        @Override
        public String evaluate(IString var) {
            return ((Z3String) var).getValue();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends IObject> T evaluate(IEnum var) {
            if (var.getEnums().length == 1) {
                return (T) var.getEnums()[0];
            } else {
                int sel_var = evaluate(var.getVar()).intValue();
                return (T) var.getEnums()[sel_var];
            }
        }

        @Override
        public String toString() {
            return model.toString();
        }
    }
}
