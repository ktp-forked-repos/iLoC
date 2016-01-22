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
package it.cnr.istc.ac;

import it.cnr.istc.ac.api.IACBool;
import it.cnr.istc.ac.api.IACEnum;
import it.cnr.istc.ac.api.IACInt;
import it.cnr.istc.ac.api.IACNetwork;
import it.cnr.istc.ac.api.IACReal;
import it.cnr.istc.ac.api.IConstraint;
import it.cnr.istc.ac.api.IVar;
import it.cnr.istc.ac.api.Int;
import it.cnr.istc.ac.api.Real;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ACNetwork implements IACNetwork {

    private final Properties properties;
    /**
     * The propagation queue. There are the variables whose domain has changed.
     */
    private final Set<IVar> prop_q = new LinkedHashSet<>();
    private final BoolVar true_constant = new BoolVar(this, "true", Arrays.asList(Boolean.TRUE));
    private final BoolVar false_constant = new BoolVar(this, "false", Arrays.asList(Boolean.FALSE));
    protected Deque<Layer> stack = new ArrayDeque<>();

    public ACNetwork(Properties properties) {
        this.properties = properties;
        this.stack.addFirst(new Layer());
    }

    @Override
    public int getNbVars() {
        return stack.peekFirst().vars.size();
    }

    @Override
    public int getNbConstraints() {
        return stack.peekFirst().constraints.size();
    }

    @Override
    public IACBool newBool() {
        BoolVar bv = new BoolVar(this, "x" + stack.peekFirst().vars.size());
        stack.peekFirst().vars.add(bv);
        return bv;
    }

    @Override
    public IACBool newBool(String constant) {
        switch (constant) {
            case "true":
                return true_constant;
            case "false":
                return false_constant;
            default:
                throw new AssertionError(constant);
        }
    }

    @Override
    public IACBool not(IACBool var) {
        BNot not = new BNot(this, var);
        watchConstraint(not);
        return not;
    }

    @Override
    public IACBool eq(IACBool var0, IACBool var1) {
        return new BEqB(this, var0, var1);
    }

    @Override
    public IACBool and(IACBool... vars) {
        return new And(this, Arrays.asList(vars));
    }

    @Override
    public IACBool or(IACBool... vars) {
        return new Or(this, Arrays.asList(vars));
    }

    @Override
    public IACBool xor(IACBool var0, IACBool var1) {
        return new XOr(this, var0, var1);
    }

    @Override
    public IACInt newInt() {
        IntVar i = new IntVar(this, "x" + stack.peekFirst().vars.size(), Int.NEGATIVE_INFINITY, Int.POSITIVE_INFINITY);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IACInt newInt(String constant) {
        Int c = new Int(constant);
        IntVar i = new IntVar(this, "x" + stack.peekFirst().vars.size(), c, c);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IACInt add(IACInt... vars) {
        ISum sum = new ISum(this, Arrays.asList(vars));
        watchConstraint(sum);
        return sum;
    }

    @Override
    public IACInt divide(IACInt var0, IACInt var1) {
        IDiv div = new IDiv(this, var0, var1);
        watchConstraint(div);
        return div;
    }

    @Override
    public IACInt multiply(IACInt... vars) {
        IMul mul = new IMul(this, Arrays.asList(vars));
        watchConstraint(mul);
        return mul;
    }

    @Override
    public IACInt subtract(IACInt var0, IACInt var1) {
        ISum sub = new ISum(this, Arrays.asList(var0, negate(var1)));
        watchConstraint(sub);
        return sub;
    }

    @Override
    public IACInt negate(IACInt var) {
        INot not = new INot(this, var);
        watchConstraint(not);
        return not;
    }

    @Override
    public IACBool leq(IACInt var0, IACInt var1) {
        IACInt slack = new IntVar(this, "$(<= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.NEGATIVE_INFINITY, Int.ZERO);
        IEqI leq = new IEqI(this, add(var0, negate(var1)), slack);
        return leq;
    }

    @Override
    public IACBool geq(IACInt var0, IACInt var1) {
        IACInt slack = new IntVar(this, "$(>= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.ZERO, Int.POSITIVE_INFINITY);
        IEqI geq = new IEqI(this, add(var0, negate(var1)), slack);
        return geq;
    }

    @Override
    public IACBool lt(IACInt var0, IACInt var1) {
        IACInt slack = new IntVar(this, "$(< " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.NEGATIVE_INFINITY, Int.negate(Int.ONE));
        IEqI lt = new IEqI(this, add(var0, negate(var1)), slack);
        return lt;
    }

    @Override
    public IACBool gt(IACInt var0, IACInt var1) {
        IACInt slack = new IntVar(this, "$(> " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.ONE, Int.POSITIVE_INFINITY);
        IEqI gt = new IEqI(this, add(var0, negate(var1)), slack);
        return gt;
    }

    @Override
    public IACBool eq(IACInt var0, IACInt var1) {
        return new IEqI(this, var0, var1);
    }

    @Override
    public IACReal newReal() {
        RealVar i = new RealVar(this, "x" + stack.peekFirst().vars.size(), Real.NEGATIVE_INFINITY, Real.POSITIVE_INFINITY);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IACReal newReal(String constant) {
        Real c = new Real(constant);
        RealVar i = new RealVar(this, "x" + stack.peekFirst().vars.size(), c, c);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IACReal add(IACReal... vars) {
        RSum sum = new RSum(this, Arrays.asList(vars));
        watchConstraint(sum);
        return sum;
    }

    @Override
    public IACReal divide(IACReal var0, IACReal var1) {
        RDiv div = new RDiv(this, var0, var1);
        watchConstraint(div);
        return div;
    }

    @Override
    public IACReal multiply(IACReal... vars) {
        RMul mul = new RMul(this, Arrays.asList(vars));
        watchConstraint(mul);
        return mul;
    }

    @Override
    public IACReal subtract(IACReal var0, IACReal var1) {
        RSum sub = new RSum(this, Arrays.asList(var0, negate(var1)));
        watchConstraint(sub);
        return sub;
    }

    @Override
    public IACReal negate(IACReal var) {
        RNot not = new RNot(this, var);
        watchConstraint(not);
        return not;
    }

    @Override
    public IACBool leq(IACReal var0, IACReal var1) {
        IACReal slack = new RealVar(this, "$(<= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.NEGATIVE_INFINITY, Real.ZERO);
        REqR leq = new REqR(this, add(var0, negate(var1)), slack);
        return leq;
    }

    @Override
    public IACBool geq(IACReal var0, IACReal var1) {
        IACReal slack = new RealVar(this, "$(>= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.ZERO, Real.POSITIVE_INFINITY);
        REqR geq = new REqR(this, add(var0, negate(var1)), slack);
        return geq;
    }

    @Override
    public IACBool lt(IACReal var0, IACReal var1) {
        IACReal slack = new RealVar(this, "$(< " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.NEGATIVE_INFINITY, Real.ZERO);
        REqR lt = new REqR(this, add(var0, negate(var1)), slack);
        return lt;
    }

    @Override
    public IACBool gt(IACReal var0, IACReal var1) {
        IACReal slack = new RealVar(this, "$(> " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.ZERO, Real.POSITIVE_INFINITY);
        REqR gt = new REqR(this, add(var0, negate(var1)), slack);
        return gt;
    }

    @Override
    public IACBool eq(IACReal var0, IACReal var1) {
        return new REqR(this, var0, var1);
    }

    @Override
    public IACReal toReal(IACInt var) {
        ToReal to_real = new ToReal(this, var);
        watchConstraint(to_real);
        return to_real;
    }

    @Override
    public IACInt toInt(IACReal var) {
        ToInt to_int = new ToInt(this, var);
        watchConstraint(to_int);
        return to_int;
    }

    @Override
    public <T> IACEnum<T> newEnum(Collection<T> vals) {
        EnumVar<T> i = new EnumVar<>(this, "x" + stack.peekFirst().vars.size(), vals);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public <T> IACBool eq(IACEnum<T> var0, IACEnum<T> var1) {
        return new EEqE<>(this, var0, var1);
    }

    @Override
    public <T0, T1> IACBool bijection(IACEnum<T0> var0, IACEnum<T1> var1) {
        return new EBiE<>(this, var0, var1);
    }

    @Override
    public void enqueue(IVar var) {
        if (stack.peekFirst().watched_constraints.containsKey(var)) {
            // we add the variable to the propagation queue only if it watches constraints..
            prop_q.add(var);
        }
    }

    @Override
    public void assertFacts(IACBool... facts) {
        for (IACBool fact : facts) {
            if (fact instanceof IConstraint && !(fact instanceof BNot)) {
                watchConstraint((IConstraint) fact);
            }
            if (!fact.remove(Boolean.FALSE)) {
                throw new UnsupportedOperationException("The network is not consistent..");
            }
        }
    }

    private void watchConstraint(IConstraint constraint) {
        stack.peekFirst().constraints.add(constraint);
        constraint.getArguments().stream().filter(arg -> !arg.isSingleton()).forEach(arg -> {
            if (!stack.peekFirst().watched_constraints.containsKey(arg)) {
                stack.peekFirst().watched_constraints.put(arg, new LinkedList<>(Arrays.asList(constraint)));
            } else {
                assert !stack.peekFirst().watched_constraints.get(arg).contains(constraint);
                stack.peekFirst().watched_constraints.get(arg).add(constraint);
            }
            if (arg instanceof IConstraint && !stack.peekFirst().constraints.contains(arg)) {
                watchConstraint((IConstraint) arg);
            }
        });
    }

    @Override
    public boolean checkFacts(IACBool... facts) {
        push();
        assertFacts(facts);
        boolean propagate = propagate();
        pop();
        return propagate;
    }

    @Override
    public boolean propagate() {
        while (!prop_q.isEmpty()) {
            IVar next = prop_q.iterator().next();
            assert !next.isEmpty() : "Domain " + next + " is empty.. a constraint among " + stack.peekFirst().watched_constraints.get(next) + " should have not propagated.";
            prop_q.remove(next);

            // if next is singleton we can make some memory cleanings..
            Collection<IConstraint> watched = next.isSingleton() ? stack.peekFirst().watched_constraints.remove(next) : stack.peekFirst().watched_constraints.get(next);
            if (watched.stream().anyMatch(c -> !c.propagate(next))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void push() {
        assert prop_q.isEmpty();
        stack.addFirst(new Layer(stack.peekFirst()));
    }

    @Override
    public void pop() {
        assert !stack.isEmpty();
        Layer layer = stack.peekFirst();
        layer.domains.keySet().forEach(var -> var.restoreDomain());
        stack.removeFirst();
    }
}
