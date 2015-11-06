// Generated from MRCPSP.g4 by ANTLR 4.4
package it.cnr.istc.iloc.translators.mrcpsp;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MRCPSPParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MRCPSPVisitor<T> extends ParseTreeVisitor<T> {

    /**
     * Visit a parse tree produced by
     * {@link MRCPSPParser#nonrenewable_resources_capacities}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNonrenewable_resources_capacities(@NotNull MRCPSPParser.Nonrenewable_resources_capacitiesContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link MRCPSPParser#doubly_constrained_resources_uses}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDoubly_constrained_resources_uses(@NotNull MRCPSPParser.Doubly_constrained_resources_usesContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#modes}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitModes(@NotNull MRCPSPParser.ModesContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#positive_number}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPositive_number(@NotNull MRCPSPParser.Positive_numberContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#activity}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitActivity(@NotNull MRCPSPParser.ActivityContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#activity_mode}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitActivity_mode(@NotNull MRCPSPParser.Activity_modeContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link MRCPSPParser#doubly_constrained_resources_capacities}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDoubly_constrained_resources_capacities(@NotNull MRCPSPParser.Doubly_constrained_resources_capacitiesContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#weights}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitWeights(@NotNull MRCPSPParser.WeightsContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#direct_successors}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDirect_successors(@NotNull MRCPSPParser.Direct_successorsContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#compilation_unit}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompilation_unit(@NotNull MRCPSPParser.Compilation_unitContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#number}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNumber(@NotNull MRCPSPParser.NumberContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#resource_usage}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitResource_usage(@NotNull MRCPSPParser.Resource_usageContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#activities}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitActivities(@NotNull MRCPSPParser.ActivitiesContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link MRCPSPParser#renewable_resources_capacities}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRenewable_resources_capacities(@NotNull MRCPSPParser.Renewable_resources_capacitiesContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#header}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitHeader(@NotNull MRCPSPParser.HeaderContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#resource_usages}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitResource_usages(@NotNull MRCPSPParser.Resource_usagesContext ctx);

    /**
     * Visit a parse tree produced by {@link MRCPSPParser#capacities}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCapacities(@NotNull MRCPSPParser.CapacitiesContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link MRCPSPParser#renewable_resources_uses}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRenewable_resources_uses(@NotNull MRCPSPParser.Renewable_resources_usesContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link MRCPSPParser#nonrenewable_resources_uses}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNonrenewable_resources_uses(@NotNull MRCPSPParser.Nonrenewable_resources_usesContext ctx);
}
