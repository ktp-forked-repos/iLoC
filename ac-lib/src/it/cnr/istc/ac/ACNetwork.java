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

import it.cnr.istc.ac.api.IBoolVar;
import it.cnr.istc.ac.api.IConstraint;
import it.cnr.istc.ac.api.IConstraintNetwork;
import it.cnr.istc.ac.api.IEnumVar;
import it.cnr.istc.ac.api.IIntVar;
import it.cnr.istc.ac.api.IRealVar;
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
public class ACNetwork implements IConstraintNetwork {

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
    public IBoolVar newBool() {
        BoolVar bv = new BoolVar(this, "x" + stack.peekFirst().vars.size());
        stack.peekFirst().vars.add(bv);
        return bv;
    }

    @Override
    public IBoolVar newBool(String constant) {
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
    public IBoolVar not(IBoolVar var) {
        BNot not = new BNot(this, var);
        watchConstraint(not);
        return not;
    }

    @Override
    public IBoolVar eq(IBoolVar var0, IBoolVar var1) {
        return new BEqB(this, var0, var1);
    }

    @Override
    public IBoolVar and(IBoolVar... vars) {
        return new And(this, Arrays.asList(vars));
    }

    @Override
    public IBoolVar or(IBoolVar... vars) {
        return new Or(this, Arrays.asList(vars));
    }

    @Override
    public IBoolVar xor(IBoolVar var0, IBoolVar var1) {
        return new XOr(this, var0, var1);
    }

    @Override
    public IIntVar newInt() {
        IntVar i = new IntVar(this, "x" + stack.peekFirst().vars.size(), Int.NEGATIVE_INFINITY, Int.POSITIVE_INFINITY);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IIntVar newInt(String constant) {
        Int c = new Int(constant);
        IntVar i = new IntVar(this, "x" + stack.peekFirst().vars.size(), c, c);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IIntVar add(IIntVar... vars) {
        ISum sum = new ISum(this, Arrays.asList(vars));
        watchConstraint(sum);
        return sum;
    }

    @Override
    public IIntVar divide(IIntVar var0, IIntVar var1) {
        IDiv div = new IDiv(this, var0, var1);
        watchConstraint(div);
        return div;
    }

    @Override
    public IIntVar multiply(IIntVar... vars) {
        IMul mul = new IMul(this, Arrays.asList(vars));
        watchConstraint(mul);
        return mul;
    }

    @Override
    public IIntVar subtract(IIntVar var0, IIntVar var1) {
        ISum sub = new ISum(this, Arrays.asList(var0, negate(var1)));
        watchConstraint(sub);
        return sub;
    }

    @Override
    public IIntVar negate(IIntVar var) {
        INot not = new INot(this, var);
        watchConstraint(not);
        return not;
    }

    @Override
    public IBoolVar leq(IIntVar var0, IIntVar var1) {
        IIntVar slack = new IntVar(this, "$(<= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.NEGATIVE_INFINITY, Int.ZERO);
        IEqI leq = new IEqI(this, add(var0, negate(var1)), slack);
        return leq;
    }

    @Override
    public IBoolVar geq(IIntVar var0, IIntVar var1) {
        IIntVar slack = new IntVar(this, "$(>= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.ZERO, Int.POSITIVE_INFINITY);
        IEqI geq = new IEqI(this, add(var0, negate(var1)), slack);
        return geq;
    }

    @Override
    public IBoolVar lt(IIntVar var0, IIntVar var1) {
        IIntVar slack = new IntVar(this, "$(< " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.NEGATIVE_INFINITY, Int.negate(Int.ONE));
        IEqI lt = new IEqI(this, add(var0, negate(var1)), slack);
        return lt;
    }

    @Override
    public IBoolVar gt(IIntVar var0, IIntVar var1) {
        IIntVar slack = new IntVar(this, "$(> " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Int.ONE, Int.POSITIVE_INFINITY);
        IEqI gt = new IEqI(this, add(var0, negate(var1)), slack);
        return gt;
    }

    @Override
    public IBoolVar eq(IIntVar var0, IIntVar var1) {
        return new IEqI(this, var0, var1);
    }

    @Override
    public IRealVar newReal() {
        RealVar i = new RealVar(this, "x" + stack.peekFirst().vars.size(), Real.NEGATIVE_INFINITY, Real.POSITIVE_INFINITY);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IRealVar newReal(String constant) {
        Real c = new Real(constant);
        RealVar i = new RealVar(this, "x" + stack.peekFirst().vars.size(), c, c);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public IRealVar add(IRealVar... vars) {
        RSum sum = new RSum(this, Arrays.asList(vars));
        watchConstraint(sum);
        return sum;
    }

    @Override
    public IRealVar divide(IRealVar var0, IRealVar var1) {
        RDiv div = new RDiv(this, var0, var1);
        watchConstraint(div);
        return div;
    }

    @Override
    public IRealVar multiply(IRealVar... vars) {
        RMul mul = new RMul(this, Arrays.asList(vars));
        watchConstraint(mul);
        return mul;
    }

    @Override
    public IRealVar subtract(IRealVar var0, IRealVar var1) {
        RSum sub = new RSum(this, Arrays.asList(var0, negate(var1)));
        watchConstraint(sub);
        return sub;
    }

    @Override
    public IRealVar negate(IRealVar var) {
        RNot not = new RNot(this, var);
        watchConstraint(not);
        return not;
    }

    @Override
    public IBoolVar leq(IRealVar var0, IRealVar var1) {
        IRealVar slack = new RealVar(this, "$(<= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.NEGATIVE_INFINITY, Real.ZERO);
        REqR leq = new REqR(this, add(var0, negate(var1)), slack);
        return leq;
    }

    @Override
    public IBoolVar geq(IRealVar var0, IRealVar var1) {
        IRealVar slack = new RealVar(this, "$(>= " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.ZERO, Real.POSITIVE_INFINITY);
        REqR geq = new REqR(this, add(var0, negate(var1)), slack);
        return geq;
    }

    @Override
    public IBoolVar lt(IRealVar var0, IRealVar var1) {
        IRealVar slack = new RealVar(this, "$(< " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.NEGATIVE_INFINITY, Real.ZERO);
        REqR lt = new REqR(this, add(var0, negate(var1)), slack);
        return lt;
    }

    @Override
    public IBoolVar gt(IRealVar var0, IRealVar var1) {
        IRealVar slack = new RealVar(this, "$(> " + (var0.isSingleton() ? var0.toString() : var0.getName()) + " " + (var1.isSingleton() ? var1.toString() : var1.getName()) + ")", Real.ZERO, Real.POSITIVE_INFINITY);
        REqR gt = new REqR(this, add(var0, negate(var1)), slack);
        return gt;
    }

    @Override
    public IBoolVar eq(IRealVar var0, IRealVar var1) {
        return new REqR(this, var0, var1);
    }

    @Override
    public IRealVar toReal(IIntVar var) {
        ToReal to_real = new ToReal(this, var);
        watchConstraint(to_real);
        return to_real;
    }

    @Override
    public IIntVar toInt(IRealVar var) {
        ToInt to_int = new ToInt(this, var);
        watchConstraint(to_int);
        return to_int;
    }

    @Override
    public <T> IEnumVar<T> newEnum(Collection<T> vals) {
        EnumVar<T> i = new EnumVar<>(this, "x" + stack.peekFirst().vars.size(), vals);
        stack.peekFirst().vars.add(i);
        return i;
    }

    @Override
    public <T> IBoolVar eq(IEnumVar<T> var0, IEnumVar<T> var1) {
        return new EEqE<>(this, var0, var1);
    }

    @Override
    public void enqueue(IVar var) {
        if (stack.peekFirst().watched_constraints.containsKey(var)) {
            // we add the variable to the propagation queue only if it watches constraints..
            prop_q.add(var);
        }
    }

    @Override
    public void assertFacts(IBoolVar... facts) {
        for (IBoolVar fact : facts) {
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
    public boolean checkFacts(IBoolVar... facts) {
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
