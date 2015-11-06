// Generated from MRCPSP.g4 by ANTLR 4.4
package it.cnr.istc.iloc.translators.mrcpsp;

import java.util.List;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MRCPSPParser extends Parser {

    static {
        RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION);
    }
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache
            = new PredictionContextCache();
    public static final int LBRACKET = 1, RBRACKET = 2, MINUS = 3, NumericLiteral = 4, WS = 5;
    public static final String[] tokenNames = {
        "<INVALID>", "'['", "']'", "'-'", "NumericLiteral", "WS"
    };
    public static final int RULE_compilation_unit = 0, RULE_header = 1, RULE_activities = 2, RULE_activity = 3,
            RULE_direct_successors = 4, RULE_weights = 5, RULE_resource_usages = 6,
            RULE_resource_usage = 7, RULE_modes = 8, RULE_activity_mode = 9, RULE_renewable_resources_uses = 10,
            RULE_nonrenewable_resources_uses = 11, RULE_doubly_constrained_resources_uses = 12,
            RULE_capacities = 13, RULE_renewable_resources_capacities = 14, RULE_nonrenewable_resources_capacities = 15,
            RULE_doubly_constrained_resources_capacities = 16, RULE_positive_number = 17,
            RULE_number = 18;
    public static final String[] ruleNames = {
        "compilation_unit", "header", "activities", "activity", "direct_successors",
        "weights", "resource_usages", "resource_usage", "modes", "activity_mode",
        "renewable_resources_uses", "nonrenewable_resources_uses", "doubly_constrained_resources_uses",
        "capacities", "renewable_resources_capacities", "nonrenewable_resources_capacities",
        "doubly_constrained_resources_capacities", "positive_number", "number"
    };

    @Override
    public String getGrammarFileName() {
        return "MRCPSP.g4";
    }

    @Override
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }
    int n_real_activities;
    int n_renewable_resources;
    int n_nonrenewable_resources;
    int n_doubly_constrained_resources;
    int[] n_modes;
    int[] n_direct_successors;

    public MRCPSPParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class Compilation_unitContext extends ParserRuleContext {

        public ActivitiesContext activities() {
            return getRuleContext(ActivitiesContext.class, 0);
        }

        public HeaderContext header() {
            return getRuleContext(HeaderContext.class, 0);
        }

        public Resource_usagesContext resource_usages() {
            return getRuleContext(Resource_usagesContext.class, 0);
        }

        public CapacitiesContext capacities() {
            return getRuleContext(CapacitiesContext.class, 0);
        }

        public Compilation_unitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_compilation_unit;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterCompilation_unit(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitCompilation_unit(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitCompilation_unit(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Compilation_unitContext compilation_unit() throws RecognitionException {
        Compilation_unitContext _localctx = new Compilation_unitContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_compilation_unit);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(38);
                header();
                setState(39);
                activities();
                setState(40);
                resource_usages();
                setState(41);
                capacities();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class HeaderContext extends ParserRuleContext {

        public Positive_numberContext n_real_activities;
        public Positive_numberContext n_renewable_resources;
        public Positive_numberContext n_nonrenewable_resources;
        public Positive_numberContext n_doubly_constrained_resources;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public HeaderContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_header;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterHeader(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitHeader(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitHeader(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final HeaderContext header() throws RecognitionException {
        HeaderContext _localctx = new HeaderContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_header);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(43);
                ((HeaderContext) _localctx).n_real_activities = positive_number();
                n_real_activities = ((HeaderContext) _localctx).n_real_activities.v;
                setState(45);
                ((HeaderContext) _localctx).n_renewable_resources = positive_number();
                n_renewable_resources = ((HeaderContext) _localctx).n_renewable_resources.v;
                setState(47);
                ((HeaderContext) _localctx).n_nonrenewable_resources = positive_number();
                n_nonrenewable_resources = ((HeaderContext) _localctx).n_nonrenewable_resources.v;
                setState(49);
                ((HeaderContext) _localctx).n_doubly_constrained_resources = positive_number();
                n_doubly_constrained_resources = ((HeaderContext) _localctx).n_doubly_constrained_resources.v;
            }

            n_modes = new int[n_real_activities + 2];
            n_direct_successors = new int[n_real_activities + 2];

        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ActivitiesContext extends ParserRuleContext {

        public int c_activity;

        public List<ActivityContext> activity() {
            return getRuleContexts(ActivityContext.class);
        }

        public ActivityContext activity(int i) {
            return getRuleContext(ActivityContext.class, i);
        }

        public ActivitiesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_activities;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterActivities(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitActivities(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitActivities(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ActivitiesContext activities() throws RecognitionException {
        ActivitiesContext _localctx = new ActivitiesContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_activities);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(57);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(52);
                                if (!(_localctx.c_activity < n_real_activities + 2)) {
                                    throw new FailedPredicateException(this, "$c_activity < n_real_activities + 2");
                                }
                                _localctx.c_activity++;
                                setState(54);
                                activity();
                            }
                        }
                    }
                    setState(59);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ActivityContext extends ParserRuleContext {

        public Positive_numberContext id;
        public Positive_numberContext n_modes;
        public Positive_numberContext n_direct_successors;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public Direct_successorsContext direct_successors() {
            return getRuleContext(Direct_successorsContext.class, 0);
        }

        public WeightsContext weights(int i) {
            return getRuleContext(WeightsContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public List<WeightsContext> weights() {
            return getRuleContexts(WeightsContext.class);
        }

        public ActivityContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_activity;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterActivity(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitActivity(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitActivity(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ActivityContext activity() throws RecognitionException {
        ActivityContext _localctx = new ActivityContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_activity);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(60);
                ((ActivityContext) _localctx).id = positive_number();
                setState(61);
                ((ActivityContext) _localctx).n_modes = positive_number();
                setState(62);
                ((ActivityContext) _localctx).n_direct_successors = positive_number();
                setState(63);
                direct_successors(((ActivityContext) _localctx).n_direct_successors.v);
                setState(67);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(64);
                                weights();
                            }
                        }
                    }
                    setState(69);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                }
            }

            n_modes[((ActivityContext) _localctx).id.v] = ((ActivityContext) _localctx).n_modes.v;
            n_direct_successors[((ActivityContext) _localctx).id.v] = ((ActivityContext) _localctx).n_direct_successors.v;

        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Direct_successorsContext extends ParserRuleContext {

        public int n_successors;
        public int c_successor;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Direct_successorsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public Direct_successorsContext(ParserRuleContext parent, int invokingState, int n_successors) {
            super(parent, invokingState);
            this.n_successors = n_successors;
        }

        @Override
        public int getRuleIndex() {
            return RULE_direct_successors;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterDirect_successors(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitDirect_successors(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitDirect_successors(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Direct_successorsContext direct_successors(int n_successors) throws RecognitionException {
        Direct_successorsContext _localctx = new Direct_successorsContext(_ctx, getState(), n_successors);
        enterRule(_localctx, 8, RULE_direct_successors);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(75);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(70);
                                if (!(_localctx.c_successor < _localctx.n_successors)) {
                                    throw new FailedPredicateException(this, "$c_successor < $n_successors");
                                }
                                _localctx.c_successor++;
                                setState(72);
                                positive_number();
                            }
                        }
                    }
                    setState(77);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class WeightsContext extends ParserRuleContext {

        public List<NumberContext> number() {
            return getRuleContexts(NumberContext.class);
        }

        public NumberContext number(int i) {
            return getRuleContext(NumberContext.class, i);
        }

        public WeightsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_weights;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterWeights(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitWeights(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitWeights(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final WeightsContext weights() throws RecognitionException {
        WeightsContext _localctx = new WeightsContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_weights);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(78);
                match(LBRACKET);
                setState(80);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(79);
                            number();
                        }
                    }
                    setState(82);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == MINUS || _la == NumericLiteral);
                setState(84);
                match(RBRACKET);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Resource_usagesContext extends ParserRuleContext {

        public int c_activity;

        public Resource_usageContext resource_usage(int i) {
            return getRuleContext(Resource_usageContext.class, i);
        }

        public List<Resource_usageContext> resource_usage() {
            return getRuleContexts(Resource_usageContext.class);
        }

        public Resource_usagesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_resource_usages;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterResource_usages(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitResource_usages(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitResource_usages(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Resource_usagesContext resource_usages() throws RecognitionException {
        Resource_usagesContext _localctx = new Resource_usagesContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_resource_usages);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(91);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(86);
                                if (!(_localctx.c_activity < n_real_activities + 2)) {
                                    throw new FailedPredicateException(this, "$c_activity < n_real_activities + 2");
                                }
                                _localctx.c_activity++;
                                setState(88);
                                resource_usage();
                            }
                        }
                    }
                    setState(93);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Resource_usageContext extends ParserRuleContext {

        public Positive_numberContext activity_id;

        public ModesContext modes() {
            return getRuleContext(ModesContext.class, 0);
        }

        public Positive_numberContext positive_number() {
            return getRuleContext(Positive_numberContext.class, 0);
        }

        public Resource_usageContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_resource_usage;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterResource_usage(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitResource_usage(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitResource_usage(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Resource_usageContext resource_usage() throws RecognitionException {
        Resource_usageContext _localctx = new Resource_usageContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_resource_usage);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(94);
                ((Resource_usageContext) _localctx).activity_id = positive_number();
                setState(95);
                modes(((Resource_usageContext) _localctx).activity_id.v);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ModesContext extends ParserRuleContext {

        public int activity_id;
        public int c_mode;

        public List<Activity_modeContext> activity_mode() {
            return getRuleContexts(Activity_modeContext.class);
        }

        public Activity_modeContext activity_mode(int i) {
            return getRuleContext(Activity_modeContext.class, i);
        }

        public ModesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public ModesContext(ParserRuleContext parent, int invokingState, int activity_id) {
            super(parent, invokingState);
            this.activity_id = activity_id;
        }

        @Override
        public int getRuleIndex() {
            return RULE_modes;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterModes(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitModes(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitModes(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ModesContext modes(int activity_id) throws RecognitionException {
        ModesContext _localctx = new ModesContext(_ctx, getState(), activity_id);
        enterRule(_localctx, 16, RULE_modes);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(102);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(97);
                                if (!(_localctx.c_mode < n_modes[_localctx.activity_id])) {
                                    throw new FailedPredicateException(this, "$c_mode < n_modes[$activity_id]");
                                }
                                _localctx.c_mode++;
                                setState(99);
                                activity_mode();
                            }
                        }
                    }
                    setState(104);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Activity_modeContext extends ParserRuleContext {

        public Positive_numberContext mode_id;
        public Positive_numberContext activity_duration;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public Doubly_constrained_resources_usesContext doubly_constrained_resources_uses() {
            return getRuleContext(Doubly_constrained_resources_usesContext.class, 0);
        }

        public Renewable_resources_usesContext renewable_resources_uses() {
            return getRuleContext(Renewable_resources_usesContext.class, 0);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Nonrenewable_resources_usesContext nonrenewable_resources_uses() {
            return getRuleContext(Nonrenewable_resources_usesContext.class, 0);
        }

        public Activity_modeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_activity_mode;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterActivity_mode(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitActivity_mode(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitActivity_mode(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Activity_modeContext activity_mode() throws RecognitionException {
        Activity_modeContext _localctx = new Activity_modeContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_activity_mode);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(105);
                ((Activity_modeContext) _localctx).mode_id = positive_number();
                setState(106);
                ((Activity_modeContext) _localctx).activity_duration = positive_number();
                setState(107);
                renewable_resources_uses();
                setState(108);
                nonrenewable_resources_uses();
                setState(109);
                doubly_constrained_resources_uses();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Renewable_resources_usesContext extends ParserRuleContext {

        public int c_res;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Renewable_resources_usesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_renewable_resources_uses;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterRenewable_resources_uses(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitRenewable_resources_uses(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitRenewable_resources_uses(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Renewable_resources_usesContext renewable_resources_uses() throws RecognitionException {
        Renewable_resources_usesContext _localctx = new Renewable_resources_usesContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_renewable_resources_uses);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(116);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(111);
                                if (!(_localctx.c_res < n_renewable_resources)) {
                                    throw new FailedPredicateException(this, "$c_res < n_renewable_resources");
                                }
                                _localctx.c_res++;
                                setState(113);
                                positive_number();
                            }
                        }
                    }
                    setState(118);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Nonrenewable_resources_usesContext extends ParserRuleContext {

        public int c_res;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Nonrenewable_resources_usesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_nonrenewable_resources_uses;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterNonrenewable_resources_uses(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitNonrenewable_resources_uses(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitNonrenewable_resources_uses(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Nonrenewable_resources_usesContext nonrenewable_resources_uses() throws RecognitionException {
        Nonrenewable_resources_usesContext _localctx = new Nonrenewable_resources_usesContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_nonrenewable_resources_uses);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(124);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(119);
                                if (!(_localctx.c_res < n_nonrenewable_resources)) {
                                    throw new FailedPredicateException(this, "$c_res < n_nonrenewable_resources");
                                }
                                _localctx.c_res++;
                                setState(121);
                                positive_number();
                            }
                        }
                    }
                    setState(126);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Doubly_constrained_resources_usesContext extends ParserRuleContext {

        public int c_res;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Doubly_constrained_resources_usesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_doubly_constrained_resources_uses;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterDoubly_constrained_resources_uses(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitDoubly_constrained_resources_uses(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitDoubly_constrained_resources_uses(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Doubly_constrained_resources_usesContext doubly_constrained_resources_uses() throws RecognitionException {
        Doubly_constrained_resources_usesContext _localctx = new Doubly_constrained_resources_usesContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_doubly_constrained_resources_uses);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(132);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 8, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(127);
                                if (!(_localctx.c_res < n_doubly_constrained_resources * 2)) {
                                    throw new FailedPredicateException(this, "$c_res < n_doubly_constrained_resources * 2");
                                }
                                _localctx.c_res++;
                                setState(129);
                                positive_number();
                            }
                        }
                    }
                    setState(134);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 8, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CapacitiesContext extends ParserRuleContext {

        public Renewable_resources_capacitiesContext renewable_resources_capacities() {
            return getRuleContext(Renewable_resources_capacitiesContext.class, 0);
        }

        public Nonrenewable_resources_capacitiesContext nonrenewable_resources_capacities() {
            return getRuleContext(Nonrenewable_resources_capacitiesContext.class, 0);
        }

        public Doubly_constrained_resources_capacitiesContext doubly_constrained_resources_capacities() {
            return getRuleContext(Doubly_constrained_resources_capacitiesContext.class, 0);
        }

        public CapacitiesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_capacities;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterCapacities(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitCapacities(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitCapacities(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final CapacitiesContext capacities() throws RecognitionException {
        CapacitiesContext _localctx = new CapacitiesContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_capacities);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(135);
                renewable_resources_capacities();
                setState(136);
                nonrenewable_resources_capacities();
                setState(137);
                doubly_constrained_resources_capacities();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Renewable_resources_capacitiesContext extends ParserRuleContext {

        public int c_res;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Renewable_resources_capacitiesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_renewable_resources_capacities;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterRenewable_resources_capacities(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitRenewable_resources_capacities(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitRenewable_resources_capacities(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Renewable_resources_capacitiesContext renewable_resources_capacities() throws RecognitionException {
        Renewable_resources_capacitiesContext _localctx = new Renewable_resources_capacitiesContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_renewable_resources_capacities);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(144);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(139);
                                if (!(_localctx.c_res < n_renewable_resources)) {
                                    throw new FailedPredicateException(this, "$c_res < n_renewable_resources");
                                }
                                _localctx.c_res++;
                                setState(141);
                                positive_number();
                            }
                        }
                    }
                    setState(146);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Nonrenewable_resources_capacitiesContext extends ParserRuleContext {

        public int c_res;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Nonrenewable_resources_capacitiesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_nonrenewable_resources_capacities;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterNonrenewable_resources_capacities(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitNonrenewable_resources_capacities(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitNonrenewable_resources_capacities(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Nonrenewable_resources_capacitiesContext nonrenewable_resources_capacities() throws RecognitionException {
        Nonrenewable_resources_capacitiesContext _localctx = new Nonrenewable_resources_capacitiesContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_nonrenewable_resources_capacities);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(152);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(147);
                                if (!(_localctx.c_res < n_nonrenewable_resources)) {
                                    throw new FailedPredicateException(this, "$c_res < n_nonrenewable_resources");
                                }
                                _localctx.c_res++;
                                setState(149);
                                positive_number();
                            }
                        }
                    }
                    setState(154);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Doubly_constrained_resources_capacitiesContext extends ParserRuleContext {

        public int c_res;

        public Positive_numberContext positive_number(int i) {
            return getRuleContext(Positive_numberContext.class, i);
        }

        public List<Positive_numberContext> positive_number() {
            return getRuleContexts(Positive_numberContext.class);
        }

        public Doubly_constrained_resources_capacitiesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_doubly_constrained_resources_capacities;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterDoubly_constrained_resources_capacities(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitDoubly_constrained_resources_capacities(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitDoubly_constrained_resources_capacities(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Doubly_constrained_resources_capacitiesContext doubly_constrained_resources_capacities() throws RecognitionException {
        Doubly_constrained_resources_capacitiesContext _localctx = new Doubly_constrained_resources_capacitiesContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_doubly_constrained_resources_capacities);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(160);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(155);
                                if (!(_localctx.c_res < n_doubly_constrained_resources * 2)) {
                                    throw new FailedPredicateException(this, "$c_res < n_doubly_constrained_resources * 2");
                                }
                                _localctx.c_res++;
                                setState(157);
                                positive_number();
                            }
                        }
                    }
                    setState(162);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Positive_numberContext extends ParserRuleContext {

        public int v;
        public Token n;

        public TerminalNode NumericLiteral() {
            return getToken(MRCPSPParser.NumericLiteral, 0);
        }

        public Positive_numberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_positive_number;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterPositive_number(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitPositive_number(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitPositive_number(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Positive_numberContext positive_number() throws RecognitionException {
        Positive_numberContext _localctx = new Positive_numberContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_positive_number);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(163);
                ((Positive_numberContext) _localctx).n = match(NumericLiteral);
                ((Positive_numberContext) _localctx).v = Integer.valueOf(((Positive_numberContext) _localctx).n.getText());
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class NumberContext extends ParserRuleContext {

        public int v;
        public Token negative;
        public Token n;

        public TerminalNode NumericLiteral() {
            return getToken(MRCPSPParser.NumericLiteral, 0);
        }

        public NumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_number;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).enterNumber(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MRCPSPListener) {
                ((MRCPSPListener) listener).exitNumber(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MRCPSPVisitor) {
                return ((MRCPSPVisitor<? extends T>) visitor).visitNumber(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final NumberContext number() throws RecognitionException {
        NumberContext _localctx = new NumberContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_number);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(167);
                _la = _input.LA(1);
                if (_la == MINUS) {
                    {
                        setState(166);
                        ((NumberContext) _localctx).negative = match(MINUS);
                    }
                }

                setState(169);
                ((NumberContext) _localctx).n = match(NumericLiteral);
                ((NumberContext) _localctx).v = ((NumberContext) _localctx).negative != null ? -Integer.valueOf(((NumberContext) _localctx).n.getText()) : Integer.valueOf(((NumberContext) _localctx).n.getText());
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @Override
    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 2:
                return activities_sempred((ActivitiesContext) _localctx, predIndex);
            case 4:
                return direct_successors_sempred((Direct_successorsContext) _localctx, predIndex);
            case 6:
                return resource_usages_sempred((Resource_usagesContext) _localctx, predIndex);
            case 8:
                return modes_sempred((ModesContext) _localctx, predIndex);
            case 10:
                return renewable_resources_uses_sempred((Renewable_resources_usesContext) _localctx, predIndex);
            case 11:
                return nonrenewable_resources_uses_sempred((Nonrenewable_resources_usesContext) _localctx, predIndex);
            case 12:
                return doubly_constrained_resources_uses_sempred((Doubly_constrained_resources_usesContext) _localctx, predIndex);
            case 14:
                return renewable_resources_capacities_sempred((Renewable_resources_capacitiesContext) _localctx, predIndex);
            case 15:
                return nonrenewable_resources_capacities_sempred((Nonrenewable_resources_capacitiesContext) _localctx, predIndex);
            case 16:
                return doubly_constrained_resources_capacities_sempred((Doubly_constrained_resources_capacitiesContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean direct_successors_sempred(Direct_successorsContext _localctx, int predIndex) {
        switch (predIndex) {
            case 1:
                return _localctx.c_successor < _localctx.n_successors;
        }
        return true;
    }

    private boolean nonrenewable_resources_capacities_sempred(Nonrenewable_resources_capacitiesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 8:
                return _localctx.c_res < n_nonrenewable_resources;
        }
        return true;
    }

    private boolean doubly_constrained_resources_uses_sempred(Doubly_constrained_resources_usesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 6:
                return _localctx.c_res < n_doubly_constrained_resources * 2;
        }
        return true;
    }

    private boolean modes_sempred(ModesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 3:
                return _localctx.c_mode < n_modes[_localctx.activity_id];
        }
        return true;
    }

    private boolean activities_sempred(ActivitiesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return _localctx.c_activity < n_real_activities + 2;
        }
        return true;
    }

    private boolean renewable_resources_capacities_sempred(Renewable_resources_capacitiesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 7:
                return _localctx.c_res < n_renewable_resources;
        }
        return true;
    }

    private boolean resource_usages_sempred(Resource_usagesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 2:
                return _localctx.c_activity < n_real_activities + 2;
        }
        return true;
    }

    private boolean renewable_resources_uses_sempred(Renewable_resources_usesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 4:
                return _localctx.c_res < n_renewable_resources;
        }
        return true;
    }

    private boolean doubly_constrained_resources_capacities_sempred(Doubly_constrained_resources_capacitiesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 9:
                return _localctx.c_res < n_doubly_constrained_resources * 2;
        }
        return true;
    }

    private boolean nonrenewable_resources_uses_sempred(Nonrenewable_resources_usesContext _localctx, int predIndex) {
        switch (predIndex) {
            case 5:
                return _localctx.c_res < n_nonrenewable_resources;
        }
        return true;
    }
    public static final String _serializedATN
            = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\7\u00af\4\2\t\2\4"
            + "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"
            + "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
            + "\4\23\t\23\4\24\t\24\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"
            + "\3\3\3\3\4\3\4\3\4\7\4:\n\4\f\4\16\4=\13\4\3\5\3\5\3\5\3\5\3\5\7\5D\n"
            + "\5\f\5\16\5G\13\5\3\6\3\6\3\6\7\6L\n\6\f\6\16\6O\13\6\3\7\3\7\6\7S\n\7"
            + "\r\7\16\7T\3\7\3\7\3\b\3\b\3\b\7\b\\\n\b\f\b\16\b_\13\b\3\t\3\t\3\t\3"
            + "\n\3\n\3\n\7\ng\n\n\f\n\16\nj\13\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3"
            + "\f\3\f\7\fu\n\f\f\f\16\fx\13\f\3\r\3\r\3\r\7\r}\n\r\f\r\16\r\u0080\13"
            + "\r\3\16\3\16\3\16\7\16\u0085\n\16\f\16\16\16\u0088\13\16\3\17\3\17\3\17"
            + "\3\17\3\20\3\20\3\20\7\20\u0091\n\20\f\20\16\20\u0094\13\20\3\21\3\21"
            + "\3\21\7\21\u0099\n\21\f\21\16\21\u009c\13\21\3\22\3\22\3\22\7\22\u00a1"
            + "\n\22\f\22\16\22\u00a4\13\22\3\23\3\23\3\23\3\24\5\24\u00aa\n\24\3\24"
            + "\3\24\3\24\3\24\2\2\25\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&\2\2"
            + "\u00a8\2(\3\2\2\2\4-\3\2\2\2\6;\3\2\2\2\b>\3\2\2\2\nM\3\2\2\2\fP\3\2\2"
            + "\2\16]\3\2\2\2\20`\3\2\2\2\22h\3\2\2\2\24k\3\2\2\2\26v\3\2\2\2\30~\3\2"
            + "\2\2\32\u0086\3\2\2\2\34\u0089\3\2\2\2\36\u0092\3\2\2\2 \u009a\3\2\2\2"
            + "\"\u00a2\3\2\2\2$\u00a5\3\2\2\2&\u00a9\3\2\2\2()\5\4\3\2)*\5\6\4\2*+\5"
            + "\16\b\2+,\5\34\17\2,\3\3\2\2\2-.\5$\23\2./\b\3\1\2/\60\5$\23\2\60\61\b"
            + "\3\1\2\61\62\5$\23\2\62\63\b\3\1\2\63\64\5$\23\2\64\65\b\3\1\2\65\5\3"
            + "\2\2\2\66\67\6\4\2\3\678\b\4\1\28:\5\b\5\29\66\3\2\2\2:=\3\2\2\2;9\3\2"
            + "\2\2;<\3\2\2\2<\7\3\2\2\2=;\3\2\2\2>?\5$\23\2?@\5$\23\2@A\5$\23\2AE\5"
            + "\n\6\2BD\5\f\7\2CB\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2F\t\3\2\2\2GE"
            + "\3\2\2\2HI\6\6\3\3IJ\b\6\1\2JL\5$\23\2KH\3\2\2\2LO\3\2\2\2MK\3\2\2\2M"
            + "N\3\2\2\2N\13\3\2\2\2OM\3\2\2\2PR\7\3\2\2QS\5&\24\2RQ\3\2\2\2ST\3\2\2"
            + "\2TR\3\2\2\2TU\3\2\2\2UV\3\2\2\2VW\7\4\2\2W\r\3\2\2\2XY\6\b\4\3YZ\b\b"
            + "\1\2Z\\\5\20\t\2[X\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\17\3\2\2\2"
            + "_]\3\2\2\2`a\5$\23\2ab\5\22\n\2b\21\3\2\2\2cd\6\n\5\3de\b\n\1\2eg\5\24"
            + "\13\2fc\3\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2i\23\3\2\2\2jh\3\2\2\2kl"
            + "\5$\23\2lm\5$\23\2mn\5\26\f\2no\5\30\r\2op\5\32\16\2p\25\3\2\2\2qr\6\f"
            + "\6\3rs\b\f\1\2su\5$\23\2tq\3\2\2\2ux\3\2\2\2vt\3\2\2\2vw\3\2\2\2w\27\3"
            + "\2\2\2xv\3\2\2\2yz\6\r\7\3z{\b\r\1\2{}\5$\23\2|y\3\2\2\2}\u0080\3\2\2"
            + "\2~|\3\2\2\2~\177\3\2\2\2\177\31\3\2\2\2\u0080~\3\2\2\2\u0081\u0082\6"
            + "\16\b\3\u0082\u0083\b\16\1\2\u0083\u0085\5$\23\2\u0084\u0081\3\2\2\2\u0085"
            + "\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\33\3\2\2"
            + "\2\u0088\u0086\3\2\2\2\u0089\u008a\5\36\20\2\u008a\u008b\5 \21\2\u008b"
            + "\u008c\5\"\22\2\u008c\35\3\2\2\2\u008d\u008e\6\20\t\3\u008e\u008f\b\20"
            + "\1\2\u008f\u0091\5$\23\2\u0090\u008d\3\2\2\2\u0091\u0094\3\2\2\2\u0092"
            + "\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\37\3\2\2\2\u0094\u0092\3\2\2"
            + "\2\u0095\u0096\6\21\n\3\u0096\u0097\b\21\1\2\u0097\u0099\5$\23\2\u0098"
            + "\u0095\3\2\2\2\u0099\u009c\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2"
            + "\2\2\u009b!\3\2\2\2\u009c\u009a\3\2\2\2\u009d\u009e\6\22\13\3\u009e\u009f"
            + "\b\22\1\2\u009f\u00a1\5$\23\2\u00a0\u009d\3\2\2\2\u00a1\u00a4\3\2\2\2"
            + "\u00a2\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3#\3\2\2\2\u00a4\u00a2\3"
            + "\2\2\2\u00a5\u00a6\7\6\2\2\u00a6\u00a7\b\23\1\2\u00a7%\3\2\2\2\u00a8\u00aa"
            + "\7\5\2\2\u00a9\u00a8\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab"
            + "\u00ac\7\6\2\2\u00ac\u00ad\b\24\1\2\u00ad\'\3\2\2\2\17;EMT]hv~\u0086\u0092"
            + "\u009a\u00a2\u00a9";
    public static final ATN _ATN
            = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
