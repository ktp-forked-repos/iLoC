// Generated from MRCPSP.g4 by ANTLR 4.4
package it.cnr.istc.iloc.translators.mrcpsp;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MRCPSPParser}.
 */
public interface MRCPSPListener extends ParseTreeListener {

    /**
     * Enter a parse tree produced by
     * {@link MRCPSPParser#nonrenewable_resources_capacities}.
     *
     * @param ctx the parse tree
     */
    void enterNonrenewable_resources_capacities(@NotNull MRCPSPParser.Nonrenewable_resources_capacitiesContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link MRCPSPParser#nonrenewable_resources_capacities}.
     *
     * @param ctx the parse tree
     */
    void exitNonrenewable_resources_capacities(@NotNull MRCPSPParser.Nonrenewable_resources_capacitiesContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link MRCPSPParser#doubly_constrained_resources_uses}.
     *
     * @param ctx the parse tree
     */
    void enterDoubly_constrained_resources_uses(@NotNull MRCPSPParser.Doubly_constrained_resources_usesContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link MRCPSPParser#doubly_constrained_resources_uses}.
     *
     * @param ctx the parse tree
     */
    void exitDoubly_constrained_resources_uses(@NotNull MRCPSPParser.Doubly_constrained_resources_usesContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#modes}.
     *
     * @param ctx the parse tree
     */
    void enterModes(@NotNull MRCPSPParser.ModesContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#modes}.
     *
     * @param ctx the parse tree
     */
    void exitModes(@NotNull MRCPSPParser.ModesContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#positive_number}.
     *
     * @param ctx the parse tree
     */
    void enterPositive_number(@NotNull MRCPSPParser.Positive_numberContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#positive_number}.
     *
     * @param ctx the parse tree
     */
    void exitPositive_number(@NotNull MRCPSPParser.Positive_numberContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#activity}.
     *
     * @param ctx the parse tree
     */
    void enterActivity(@NotNull MRCPSPParser.ActivityContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#activity}.
     *
     * @param ctx the parse tree
     */
    void exitActivity(@NotNull MRCPSPParser.ActivityContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#activity_mode}.
     *
     * @param ctx the parse tree
     */
    void enterActivity_mode(@NotNull MRCPSPParser.Activity_modeContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#activity_mode}.
     *
     * @param ctx the parse tree
     */
    void exitActivity_mode(@NotNull MRCPSPParser.Activity_modeContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link MRCPSPParser#doubly_constrained_resources_capacities}.
     *
     * @param ctx the parse tree
     */
    void enterDoubly_constrained_resources_capacities(@NotNull MRCPSPParser.Doubly_constrained_resources_capacitiesContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link MRCPSPParser#doubly_constrained_resources_capacities}.
     *
     * @param ctx the parse tree
     */
    void exitDoubly_constrained_resources_capacities(@NotNull MRCPSPParser.Doubly_constrained_resources_capacitiesContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#weights}.
     *
     * @param ctx the parse tree
     */
    void enterWeights(@NotNull MRCPSPParser.WeightsContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#weights}.
     *
     * @param ctx the parse tree
     */
    void exitWeights(@NotNull MRCPSPParser.WeightsContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#direct_successors}.
     *
     * @param ctx the parse tree
     */
    void enterDirect_successors(@NotNull MRCPSPParser.Direct_successorsContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#direct_successors}.
     *
     * @param ctx the parse tree
     */
    void exitDirect_successors(@NotNull MRCPSPParser.Direct_successorsContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#compilation_unit}.
     *
     * @param ctx the parse tree
     */
    void enterCompilation_unit(@NotNull MRCPSPParser.Compilation_unitContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#compilation_unit}.
     *
     * @param ctx the parse tree
     */
    void exitCompilation_unit(@NotNull MRCPSPParser.Compilation_unitContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#number}.
     *
     * @param ctx the parse tree
     */
    void enterNumber(@NotNull MRCPSPParser.NumberContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#number}.
     *
     * @param ctx the parse tree
     */
    void exitNumber(@NotNull MRCPSPParser.NumberContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#resource_usage}.
     *
     * @param ctx the parse tree
     */
    void enterResource_usage(@NotNull MRCPSPParser.Resource_usageContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#resource_usage}.
     *
     * @param ctx the parse tree
     */
    void exitResource_usage(@NotNull MRCPSPParser.Resource_usageContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#activities}.
     *
     * @param ctx the parse tree
     */
    void enterActivities(@NotNull MRCPSPParser.ActivitiesContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#activities}.
     *
     * @param ctx the parse tree
     */
    void exitActivities(@NotNull MRCPSPParser.ActivitiesContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link MRCPSPParser#renewable_resources_capacities}.
     *
     * @param ctx the parse tree
     */
    void enterRenewable_resources_capacities(@NotNull MRCPSPParser.Renewable_resources_capacitiesContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link MRCPSPParser#renewable_resources_capacities}.
     *
     * @param ctx the parse tree
     */
    void exitRenewable_resources_capacities(@NotNull MRCPSPParser.Renewable_resources_capacitiesContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#header}.
     *
     * @param ctx the parse tree
     */
    void enterHeader(@NotNull MRCPSPParser.HeaderContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#header}.
     *
     * @param ctx the parse tree
     */
    void exitHeader(@NotNull MRCPSPParser.HeaderContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#resource_usages}.
     *
     * @param ctx the parse tree
     */
    void enterResource_usages(@NotNull MRCPSPParser.Resource_usagesContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#resource_usages}.
     *
     * @param ctx the parse tree
     */
    void exitResource_usages(@NotNull MRCPSPParser.Resource_usagesContext ctx);

    /**
     * Enter a parse tree produced by {@link MRCPSPParser#capacities}.
     *
     * @param ctx the parse tree
     */
    void enterCapacities(@NotNull MRCPSPParser.CapacitiesContext ctx);

    /**
     * Exit a parse tree produced by {@link MRCPSPParser#capacities}.
     *
     * @param ctx the parse tree
     */
    void exitCapacities(@NotNull MRCPSPParser.CapacitiesContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link MRCPSPParser#renewable_resources_uses}.
     *
     * @param ctx the parse tree
     */
    void enterRenewable_resources_uses(@NotNull MRCPSPParser.Renewable_resources_usesContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link MRCPSPParser#renewable_resources_uses}.
     *
     * @param ctx the parse tree
     */
    void exitRenewable_resources_uses(@NotNull MRCPSPParser.Renewable_resources_usesContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link MRCPSPParser#nonrenewable_resources_uses}.
     *
     * @param ctx the parse tree
     */
    void enterNonrenewable_resources_uses(@NotNull MRCPSPParser.Nonrenewable_resources_usesContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link MRCPSPParser#nonrenewable_resources_uses}.
     *
     * @param ctx the parse tree
     */
    void exitNonrenewable_resources_uses(@NotNull MRCPSPParser.Nonrenewable_resources_usesContext ctx);
}
