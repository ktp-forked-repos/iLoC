// Generated from C:\Users\pst\Documents\NetBeansProjects\lab\iLoC\src\it\cnr\istc\iloc\ddl\DDL.g4 by ANTLR 4.5.1
package it.cnr.istc.iloc.ddl;

import java.util.List;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
class DDLParser extends Parser {

    static {
        RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION);
    }
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache
            = new PredictionContextCache();
    public static final int TYPE_DEF = 1, INT = 2, REAL = 3, BOOL = 4, STRING = 5, ENUM = 6, CLASS = 7, GOAL = 8,
            FACT = 9, EXTENDS = 10, PREDICATE = 11, NEW = 12, OR = 13, THIS = 14, SUPER = 15, VOID = 16,
            TRUE = 17, FALSE = 18, RETURN = 19, DOT = 20, COMMA = 21, COLON = 22, SEMICOLON = 23,
            LPAREN = 24, RPAREN = 25, LBRACKET = 26, RBRACKET = 27, LBRACE = 28, RBRACE = 29,
            PLUS = 30, MINUS = 31, STAR = 32, SLASH = 33, BAR = 34, EQUAL = 35, GT = 36, LT = 37,
            BANG = 38, QUESTIONMARK = 39, EQEQ = 40, LTEQ = 41, GTEQ = 42, BANGEQ = 43, IMPLICATION = 44,
            AMPAMP = 45, BARBAR = 46, CARET = 47, ID = 48, NumericLiteral = 49, StringLiteral = 50,
            LINE_COMMENT = 51, COMMENT = 52, WS = 53;
    public static final int RULE_compilation_unit = 0, RULE_type_declaration = 1, RULE_typedef_declaration = 2,
            RULE_enum_declaration = 3, RULE_enum_constants = 4, RULE_class_declaration = 5,
            RULE_member = 6, RULE_field_declaration = 7, RULE_variable_dec = 8, RULE_method_declaration = 9,
            RULE_constructor_declaration = 10, RULE_explicit_constructor_invocation = 11,
            RULE_predicate_declaration = 12, RULE_statement = 13, RULE_block = 14,
            RULE_assignment_statement = 15, RULE_local_variable_statement = 16, RULE_expression_statement = 17,
            RULE_preference_statement = 18, RULE_disjunction_statement = 19, RULE_disjunct = 20,
            RULE_formula_statement = 21, RULE_return_statement = 22, RULE_assignment_list = 23,
            RULE_assignment = 24, RULE_expr = 25, RULE_expr_list = 26, RULE_literal = 27,
            RULE_qualified_id = 28, RULE_type = 29, RULE_class_type = 30, RULE_primitive_type = 31,
            RULE_typed_list = 32;
    public static final String[] ruleNames = {
        "compilation_unit", "type_declaration", "typedef_declaration", "enum_declaration",
        "enum_constants", "class_declaration", "member", "field_declaration",
        "variable_dec", "method_declaration", "constructor_declaration", "explicit_constructor_invocation",
        "predicate_declaration", "statement", "block", "assignment_statement",
        "local_variable_statement", "expression_statement", "preference_statement",
        "disjunction_statement", "disjunct", "formula_statement", "return_statement",
        "assignment_list", "assignment", "expr", "expr_list", "literal", "qualified_id",
        "type", "class_type", "primitive_type", "typed_list"
    };
    private static final String[] _LITERAL_NAMES = {
        null, "'typedef'", "'int'", "'real'", "'bool'", "'string'", "'enum'",
        "'class'", "'goal'", "'fact'", "'extends'", "'predicate'", "'new'", "'or'",
        "'this'", "'super'", "'void'", "'true'", "'false'", "'return'", "'.'",
        "','", "':'", "';'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'+'",
        "'-'", "'*'", "'/'", "'|'", "'='", "'>'", "'<'", "'!'", "'?'", "'=='",
        "'<='", "'>='", "'!='", "'->'", "'&&'", "'||'", "'^'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
        null, "TYPE_DEF", "INT", "REAL", "BOOL", "STRING", "ENUM", "CLASS", "GOAL",
        "FACT", "EXTENDS", "PREDICATE", "NEW", "OR", "THIS", "SUPER", "VOID",
        "TRUE", "FALSE", "RETURN", "DOT", "COMMA", "COLON", "SEMICOLON", "LPAREN",
        "RPAREN", "LBRACKET", "RBRACKET", "LBRACE", "RBRACE", "PLUS", "MINUS",
        "STAR", "SLASH", "BAR", "EQUAL", "GT", "LT", "BANG", "QUESTIONMARK", "EQEQ",
        "LTEQ", "GTEQ", "BANGEQ", "IMPLICATION", "AMPAMP", "BARBAR", "CARET",
        "ID", "NumericLiteral", "StringLiteral", "LINE_COMMENT", "COMMENT", "WS"
    };
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "DDL.g4";
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

    DDLParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class Compilation_unitContext extends ParserRuleContext {

        public TerminalNode EOF() {
            return getToken(DDLParser.EOF, 0);
        }

        public List<Type_declarationContext> type_declaration() {
            return getRuleContexts(Type_declarationContext.class);
        }

        public Type_declarationContext type_declaration(int i) {
            return getRuleContext(Type_declarationContext.class, i);
        }

        public List<Method_declarationContext> method_declaration() {
            return getRuleContexts(Method_declarationContext.class);
        }

        public Method_declarationContext method_declaration(int i) {
            return getRuleContext(Method_declarationContext.class, i);
        }

        public List<Predicate_declarationContext> predicate_declaration() {
            return getRuleContexts(Predicate_declarationContext.class);
        }

        public Predicate_declarationContext predicate_declaration(int i) {
            return getRuleContext(Predicate_declarationContext.class, i);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
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
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterCompilation_unit(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitCompilation_unit(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitCompilation_unit(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Compilation_unitContext compilation_unit() throws RecognitionException {
        Compilation_unitContext _localctx = new Compilation_unitContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_compilation_unit);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(72);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TYPE_DEF) | (1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ENUM) | (1L << CLASS) | (1L << GOAL) | (1L << FACT) | (1L << PREDICATE) | (1L << NEW) | (1L << THIS) | (1L << SUPER) | (1L << VOID) | (1L << TRUE) | (1L << FALSE) | (1L << RETURN) | (1L << LPAREN) | (1L << LBRACKET) | (1L << LBRACE) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                    {
                        setState(70);
                        switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                            case 1: {
                                setState(66);
                                type_declaration();
                            }
                            break;
                            case 2: {
                                setState(67);
                                method_declaration();
                            }
                            break;
                            case 3: {
                                setState(68);
                                predicate_declaration();
                            }
                            break;
                            case 4: {
                                setState(69);
                                statement();
                            }
                            break;
                        }
                    }
                    setState(74);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(75);
                match(EOF);
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

    public static class Type_declarationContext extends ParserRuleContext {

        public Typedef_declarationContext typedef_declaration() {
            return getRuleContext(Typedef_declarationContext.class, 0);
        }

        public Enum_declarationContext enum_declaration() {
            return getRuleContext(Enum_declarationContext.class, 0);
        }

        public Class_declarationContext class_declaration() {
            return getRuleContext(Class_declarationContext.class, 0);
        }

        public Type_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterType_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitType_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitType_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Type_declarationContext type_declaration() throws RecognitionException {
        Type_declarationContext _localctx = new Type_declarationContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_type_declaration);
        try {
            setState(80);
            switch (_input.LA(1)) {
                case TYPE_DEF:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(77);
                        typedef_declaration();
                    }
                    break;
                case ENUM:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(78);
                        enum_declaration();
                    }
                    break;
                case CLASS:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(79);
                        class_declaration();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
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

    public static class Typedef_declarationContext extends ParserRuleContext {

        public Token name;

        public Primitive_typeContext primitive_type() {
            return getRuleContext(Primitive_typeContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public Typedef_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_typedef_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterTypedef_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitTypedef_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitTypedef_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Typedef_declarationContext typedef_declaration() throws RecognitionException {
        Typedef_declarationContext _localctx = new Typedef_declarationContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_typedef_declaration);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(82);
                match(TYPE_DEF);
                setState(83);
                primitive_type();
                setState(84);
                expr(0);
                setState(85);
                ((Typedef_declarationContext) _localctx).name = match(ID);
                setState(86);
                match(SEMICOLON);
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

    public static class Enum_declarationContext extends ParserRuleContext {

        public Token name;

        public List<Enum_constantsContext> enum_constants() {
            return getRuleContexts(Enum_constantsContext.class);
        }

        public Enum_constantsContext enum_constants(int i) {
            return getRuleContext(Enum_constantsContext.class, i);
        }

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public Enum_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_enum_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterEnum_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitEnum_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitEnum_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Enum_declarationContext enum_declaration() throws RecognitionException {
        Enum_declarationContext _localctx = new Enum_declarationContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_enum_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(88);
                match(ENUM);
                setState(89);
                ((Enum_declarationContext) _localctx).name = match(ID);
                setState(90);
                enum_constants();
                setState(95);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == BAR) {
                    {
                        {
                            setState(91);
                            match(BAR);
                            setState(92);
                            enum_constants();
                        }
                    }
                    setState(97);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(98);
                match(SEMICOLON);
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

    public static class Enum_constantsContext extends ParserRuleContext {

        public List<TerminalNode> StringLiteral() {
            return getTokens(DDLParser.StringLiteral);
        }

        public TerminalNode StringLiteral(int i) {
            return getToken(DDLParser.StringLiteral, i);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public Enum_constantsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_enum_constants;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterEnum_constants(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitEnum_constants(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitEnum_constants(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Enum_constantsContext enum_constants() throws RecognitionException {
        Enum_constantsContext _localctx = new Enum_constantsContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_enum_constants);
        int _la;
        try {
            setState(111);
            switch (_input.LA(1)) {
                case LBRACE:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(100);
                        match(LBRACE);
                        setState(101);
                        match(StringLiteral);
                        setState(106);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == COMMA) {
                            {
                                {
                                    setState(102);
                                    match(COMMA);
                                    setState(103);
                                    match(StringLiteral);
                                }
                            }
                            setState(108);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(109);
                        match(RBRACE);
                    }
                    break;
                case INT:
                case REAL:
                case BOOL:
                case STRING:
                case ID:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(110);
                        type();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
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

    public static class Class_declarationContext extends ParserRuleContext {

        public Token name;

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public List<MemberContext> member() {
            return getRuleContexts(MemberContext.class);
        }

        public MemberContext member(int i) {
            return getRuleContext(MemberContext.class, i);
        }

        public Class_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_class_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterClass_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitClass_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitClass_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Class_declarationContext class_declaration() throws RecognitionException {
        Class_declarationContext _localctx = new Class_declarationContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_class_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(113);
                match(CLASS);
                setState(114);
                ((Class_declarationContext) _localctx).name = match(ID);
                setState(117);
                _la = _input.LA(1);
                if (_la == EXTENDS) {
                    {
                        setState(115);
                        match(EXTENDS);
                        setState(116);
                        type();
                    }
                }

                setState(119);
                match(LBRACE);
                setState(123);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TYPE_DEF) | (1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ENUM) | (1L << CLASS) | (1L << PREDICATE) | (1L << VOID) | (1L << ID))) != 0)) {
                    {
                        {
                            setState(120);
                            member();
                        }
                    }
                    setState(125);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(126);
                match(RBRACE);
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

    public static class MemberContext extends ParserRuleContext {

        public Field_declarationContext field_declaration() {
            return getRuleContext(Field_declarationContext.class, 0);
        }

        public Method_declarationContext method_declaration() {
            return getRuleContext(Method_declarationContext.class, 0);
        }

        public Constructor_declarationContext constructor_declaration() {
            return getRuleContext(Constructor_declarationContext.class, 0);
        }

        public Predicate_declarationContext predicate_declaration() {
            return getRuleContext(Predicate_declarationContext.class, 0);
        }

        public Type_declarationContext type_declaration() {
            return getRuleContext(Type_declarationContext.class, 0);
        }

        public MemberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_member;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterMember(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitMember(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitMember(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final MemberContext member() throws RecognitionException {
        MemberContext _localctx = new MemberContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_member);
        try {
            setState(133);
            switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(128);
                        field_declaration();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(129);
                        method_declaration();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(130);
                        constructor_declaration();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                     {
                        setState(131);
                        predicate_declaration();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                     {
                        setState(132);
                        type_declaration();
                    }
                    break;
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

    public static class Field_declarationContext extends ParserRuleContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public List<Variable_decContext> variable_dec() {
            return getRuleContexts(Variable_decContext.class);
        }

        public Variable_decContext variable_dec(int i) {
            return getRuleContext(Variable_decContext.class, i);
        }

        public Field_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_field_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterField_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitField_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitField_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Field_declarationContext field_declaration() throws RecognitionException {
        Field_declarationContext _localctx = new Field_declarationContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_field_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(135);
                type();
                setState(136);
                variable_dec();
                setState(141);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(137);
                            match(COMMA);
                            setState(138);
                            variable_dec();
                        }
                    }
                    setState(143);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(144);
                match(SEMICOLON);
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

    public static class Variable_decContext extends ParserRuleContext {

        public Token name;

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Variable_decContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_variable_dec;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterVariable_dec(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitVariable_dec(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitVariable_dec(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Variable_decContext variable_dec() throws RecognitionException {
        Variable_decContext _localctx = new Variable_decContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_variable_dec);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(146);
                ((Variable_decContext) _localctx).name = match(ID);
                setState(149);
                _la = _input.LA(1);
                if (_la == EQUAL) {
                    {
                        setState(147);
                        match(EQUAL);
                        setState(148);
                        expr(0);
                    }
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

    public static class Method_declarationContext extends ParserRuleContext {

        public Method_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_method_declaration;
        }

        public Method_declarationContext() {
        }

        public void copyFrom(Method_declarationContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class Void_method_declarationContext extends Method_declarationContext {

        public Token name;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public Void_method_declarationContext(Method_declarationContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterVoid_method_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitVoid_method_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitVoid_method_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Type_method_declarationContext extends Method_declarationContext {

        public Token name;

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public Type_method_declarationContext(Method_declarationContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterType_method_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitType_method_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitType_method_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Method_declarationContext method_declaration() throws RecognitionException {
        Method_declarationContext _localctx = new Method_declarationContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_method_declaration);
        int _la;
        try {
            setState(173);
            switch (_input.LA(1)) {
                case VOID:
                    _localctx = new Void_method_declarationContext(_localctx);
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(151);
                        match(VOID);
                        setState(152);
                        ((Void_method_declarationContext) _localctx).name = match(ID);
                        setState(153);
                        match(LPAREN);
                        setState(155);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                            {
                                setState(154);
                                typed_list();
                            }
                        }

                        setState(157);
                        match(RPAREN);
                        setState(158);
                        match(LBRACE);
                        setState(159);
                        block();
                        setState(160);
                        match(RBRACE);
                    }
                    break;
                case INT:
                case REAL:
                case BOOL:
                case STRING:
                case ID:
                    _localctx = new Type_method_declarationContext(_localctx);
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(162);
                        type();
                        setState(163);
                        ((Type_method_declarationContext) _localctx).name = match(ID);
                        setState(164);
                        match(LPAREN);
                        setState(166);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                            {
                                setState(165);
                                typed_list();
                            }
                        }

                        setState(168);
                        match(RPAREN);
                        setState(169);
                        match(LBRACE);
                        setState(170);
                        block();
                        setState(171);
                        match(RBRACE);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
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

    public static class Constructor_declarationContext extends ParserRuleContext {

        public Token name;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public Explicit_constructor_invocationContext explicit_constructor_invocation() {
            return getRuleContext(Explicit_constructor_invocationContext.class, 0);
        }

        public Constructor_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constructor_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterConstructor_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitConstructor_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitConstructor_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Constructor_declarationContext constructor_declaration() throws RecognitionException {
        Constructor_declarationContext _localctx = new Constructor_declarationContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_constructor_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(175);
                ((Constructor_declarationContext) _localctx).name = match(ID);
                setState(176);
                match(LPAREN);
                setState(178);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                    {
                        setState(177);
                        typed_list();
                    }
                }

                setState(180);
                match(RPAREN);
                setState(181);
                match(LBRACE);
                setState(183);
                switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
                    case 1: {
                        setState(182);
                        explicit_constructor_invocation();
                    }
                    break;
                }
                setState(185);
                block();
                setState(186);
                match(RBRACE);
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

    public static class Explicit_constructor_invocationContext extends ParserRuleContext {

        public Explicit_constructor_invocationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_explicit_constructor_invocation;
        }

        public Explicit_constructor_invocationContext() {
        }

        public void copyFrom(Explicit_constructor_invocationContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class Super_constructor_invocationContext extends Explicit_constructor_invocationContext {

        public Expr_listContext expr_list() {
            return getRuleContext(Expr_listContext.class, 0);
        }

        public Super_constructor_invocationContext(Explicit_constructor_invocationContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterSuper_constructor_invocation(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitSuper_constructor_invocation(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitSuper_constructor_invocation(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class This_constructor_invocationContext extends Explicit_constructor_invocationContext {

        public Expr_listContext expr_list() {
            return getRuleContext(Expr_listContext.class, 0);
        }

        public This_constructor_invocationContext(Explicit_constructor_invocationContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterThis_constructor_invocation(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitThis_constructor_invocation(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitThis_constructor_invocation(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Explicit_constructor_invocationContext explicit_constructor_invocation() throws RecognitionException {
        Explicit_constructor_invocationContext _localctx = new Explicit_constructor_invocationContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_explicit_constructor_invocation);
        int _la;
        try {
            setState(202);
            switch (_input.LA(1)) {
                case THIS:
                    _localctx = new This_constructor_invocationContext(_localctx);
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(188);
                        match(THIS);
                        setState(189);
                        match(LPAREN);
                        setState(191);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEW) | (1L << THIS) | (1L << SUPER) | (1L << TRUE) | (1L << FALSE) | (1L << LPAREN) | (1L << LBRACKET) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                            {
                                setState(190);
                                expr_list();
                            }
                        }

                        setState(193);
                        match(RPAREN);
                        setState(194);
                        match(SEMICOLON);
                    }
                    break;
                case SUPER:
                    _localctx = new Super_constructor_invocationContext(_localctx);
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(195);
                        match(SUPER);
                        setState(196);
                        match(LPAREN);
                        setState(198);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEW) | (1L << THIS) | (1L << SUPER) | (1L << TRUE) | (1L << FALSE) | (1L << LPAREN) | (1L << LBRACKET) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                            {
                                setState(197);
                                expr_list();
                            }
                        }

                        setState(200);
                        match(RPAREN);
                        setState(201);
                        match(SEMICOLON);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
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

    public static class Predicate_declarationContext extends ParserRuleContext {

        public Token name;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public Predicate_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_predicate_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterPredicate_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitPredicate_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitPredicate_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Predicate_declarationContext predicate_declaration() throws RecognitionException {
        Predicate_declarationContext _localctx = new Predicate_declarationContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_predicate_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(204);
                match(PREDICATE);
                setState(205);
                ((Predicate_declarationContext) _localctx).name = match(ID);
                setState(206);
                match(LPAREN);
                setState(208);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                    {
                        setState(207);
                        typed_list();
                    }
                }

                setState(210);
                match(RPAREN);
                setState(211);
                match(LBRACE);
                setState(212);
                block();
                setState(213);
                match(RBRACE);
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

    public static class StatementContext extends ParserRuleContext {

        public Assignment_statementContext assignment_statement() {
            return getRuleContext(Assignment_statementContext.class, 0);
        }

        public Local_variable_statementContext local_variable_statement() {
            return getRuleContext(Local_variable_statementContext.class, 0);
        }

        public Expression_statementContext expression_statement() {
            return getRuleContext(Expression_statementContext.class, 0);
        }

        public Preference_statementContext preference_statement() {
            return getRuleContext(Preference_statementContext.class, 0);
        }

        public Disjunction_statementContext disjunction_statement() {
            return getRuleContext(Disjunction_statementContext.class, 0);
        }

        public Formula_statementContext formula_statement() {
            return getRuleContext(Formula_statementContext.class, 0);
        }

        public Return_statementContext return_statement() {
            return getRuleContext(Return_statementContext.class, 0);
        }

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public StatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitStatement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitStatement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final StatementContext statement() throws RecognitionException {
        StatementContext _localctx = new StatementContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_statement);
        try {
            setState(226);
            switch (getInterpreter().adaptivePredict(_input, 20, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(215);
                        assignment_statement();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(216);
                        local_variable_statement();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(217);
                        expression_statement();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                     {
                        setState(218);
                        preference_statement();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                     {
                        setState(219);
                        disjunction_statement();
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                     {
                        setState(220);
                        formula_statement();
                    }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                     {
                        setState(221);
                        return_statement();
                    }
                    break;
                case 8:
                    enterOuterAlt(_localctx, 8);
                     {
                        setState(222);
                        match(LBRACE);
                        setState(223);
                        block();
                        setState(224);
                        match(RBRACE);
                    }
                    break;
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

    public static class BlockContext extends ParserRuleContext {

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public BlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_block;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterBlock(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitBlock(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitBlock(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final BlockContext block() throws RecognitionException {
        BlockContext _localctx = new BlockContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_block);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(231);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << GOAL) | (1L << FACT) | (1L << NEW) | (1L << THIS) | (1L << SUPER) | (1L << TRUE) | (1L << FALSE) | (1L << RETURN) | (1L << LPAREN) | (1L << LBRACKET) | (1L << LBRACE) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                    {
                        {
                            setState(228);
                            statement();
                        }
                    }
                    setState(233);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
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

    public static class Assignment_statementContext extends ParserRuleContext {

        public Qualified_idContext object;
        public Token field;
        public ExprContext value;

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Assignment_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignment_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterAssignment_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitAssignment_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitAssignment_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Assignment_statementContext assignment_statement() throws RecognitionException {
        Assignment_statementContext _localctx = new Assignment_statementContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_assignment_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(237);
                switch (getInterpreter().adaptivePredict(_input, 22, _ctx)) {
                    case 1: {
                        setState(234);
                        ((Assignment_statementContext) _localctx).object = qualified_id();
                        setState(235);
                        match(DOT);
                    }
                    break;
                }
                setState(239);
                ((Assignment_statementContext) _localctx).field = match(ID);
                setState(240);
                match(EQUAL);
                setState(241);
                ((Assignment_statementContext) _localctx).value = expr(0);
                setState(242);
                match(SEMICOLON);
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

    public static class Local_variable_statementContext extends ParserRuleContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public List<Variable_decContext> variable_dec() {
            return getRuleContexts(Variable_decContext.class);
        }

        public Variable_decContext variable_dec(int i) {
            return getRuleContext(Variable_decContext.class, i);
        }

        public Local_variable_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_local_variable_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterLocal_variable_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitLocal_variable_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitLocal_variable_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Local_variable_statementContext local_variable_statement() throws RecognitionException {
        Local_variable_statementContext _localctx = new Local_variable_statementContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_local_variable_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(244);
                type();
                setState(245);
                variable_dec();
                setState(250);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(246);
                            match(COMMA);
                            setState(247);
                            variable_dec();
                        }
                    }
                    setState(252);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(253);
                match(SEMICOLON);
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

    public static class Expression_statementContext extends ParserRuleContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Expression_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterExpression_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitExpression_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitExpression_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Expression_statementContext expression_statement() throws RecognitionException {
        Expression_statementContext _localctx = new Expression_statementContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_expression_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(255);
                expr(0);
                setState(256);
                match(SEMICOLON);
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

    public static class Preference_statementContext extends ParserRuleContext {

        public Token not;
        public ExprContext cost;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Preference_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_preference_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterPreference_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitPreference_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitPreference_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Preference_statementContext preference_statement() throws RecognitionException {
        Preference_statementContext _localctx = new Preference_statementContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_preference_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(259);
                _la = _input.LA(1);
                if (_la == MINUS) {
                    {
                        setState(258);
                        ((Preference_statementContext) _localctx).not = match(MINUS);
                    }
                }

                setState(261);
                match(LBRACKET);
                setState(262);
                block();
                setState(263);
                match(RBRACKET);
                setState(264);
                match(LPAREN);
                setState(265);
                ((Preference_statementContext) _localctx).cost = expr(0);
                setState(266);
                match(RPAREN);
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

    public static class Disjunction_statementContext extends ParserRuleContext {

        public List<DisjunctContext> disjunct() {
            return getRuleContexts(DisjunctContext.class);
        }

        public DisjunctContext disjunct(int i) {
            return getRuleContext(DisjunctContext.class, i);
        }

        public Disjunction_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_disjunction_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterDisjunction_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitDisjunction_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitDisjunction_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Disjunction_statementContext disjunction_statement() throws RecognitionException {
        Disjunction_statementContext _localctx = new Disjunction_statementContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_disjunction_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(268);
                disjunct();
                setState(271);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(269);
                            match(OR);
                            setState(270);
                            disjunct();
                        }
                    }
                    setState(273);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == OR);
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

    public static class DisjunctContext extends ParserRuleContext {

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public DisjunctContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_disjunct;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterDisjunct(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitDisjunct(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitDisjunct(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final DisjunctContext disjunct() throws RecognitionException {
        DisjunctContext _localctx = new DisjunctContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_disjunct);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(275);
                match(LBRACE);
                setState(276);
                block();
                setState(277);
                match(RBRACE);
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

    public static class Formula_statementContext extends ParserRuleContext {

        public Token goal;
        public Token fact;
        public Token name;
        public Qualified_idContext object;
        public Token predicate;

        public List<TerminalNode> ID() {
            return getTokens(DDLParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(DDLParser.ID, i);
        }

        public Assignment_listContext assignment_list() {
            return getRuleContext(Assignment_listContext.class, 0);
        }

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Formula_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_formula_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterFormula_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitFormula_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitFormula_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Formula_statementContext formula_statement() throws RecognitionException {
        Formula_statementContext _localctx = new Formula_statementContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_formula_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(281);
                switch (_input.LA(1)) {
                    case GOAL: {
                        setState(279);
                        ((Formula_statementContext) _localctx).goal = match(GOAL);
                    }
                    break;
                    case FACT: {
                        setState(280);
                        ((Formula_statementContext) _localctx).fact = match(FACT);
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(283);
                ((Formula_statementContext) _localctx).name = match(ID);
                setState(284);
                match(EQUAL);
                setState(285);
                match(NEW);
                setState(289);
                switch (getInterpreter().adaptivePredict(_input, 27, _ctx)) {
                    case 1: {
                        setState(286);
                        ((Formula_statementContext) _localctx).object = qualified_id();
                        setState(287);
                        match(DOT);
                    }
                    break;
                }
                setState(291);
                ((Formula_statementContext) _localctx).predicate = match(ID);
                setState(292);
                match(LPAREN);
                setState(294);
                _la = _input.LA(1);
                if (_la == ID) {
                    {
                        setState(293);
                        assignment_list();
                    }
                }

                setState(296);
                match(RPAREN);
                setState(297);
                match(SEMICOLON);
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

    public static class Return_statementContext extends ParserRuleContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Return_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_return_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterReturn_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitReturn_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitReturn_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Return_statementContext return_statement() throws RecognitionException {
        Return_statementContext _localctx = new Return_statementContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_return_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(299);
                match(RETURN);
                setState(300);
                expr(0);
                setState(301);
                match(SEMICOLON);
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

    public static class Assignment_listContext extends ParserRuleContext {

        public List<AssignmentContext> assignment() {
            return getRuleContexts(AssignmentContext.class);
        }

        public AssignmentContext assignment(int i) {
            return getRuleContext(AssignmentContext.class, i);
        }

        public Assignment_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignment_list;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterAssignment_list(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitAssignment_list(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitAssignment_list(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Assignment_listContext assignment_list() throws RecognitionException {
        Assignment_listContext _localctx = new Assignment_listContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_assignment_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(303);
                assignment();
                setState(308);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(304);
                            match(COMMA);
                            setState(305);
                            assignment();
                        }
                    }
                    setState(310);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
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

    public static class AssignmentContext extends ParserRuleContext {

        public Token field;
        public ExprContext value;

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public AssignmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterAssignment(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitAssignment(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitAssignment(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final AssignmentContext assignment() throws RecognitionException {
        AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_assignment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(311);
                ((AssignmentContext) _localctx).field = match(ID);
                setState(312);
                match(COLON);
                setState(313);
                ((AssignmentContext) _localctx).value = expr(0);
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

    public static class ExprContext extends ParserRuleContext {

        public ExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr;
        }

        public ExprContext() {
        }

        public void copyFrom(ExprContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class Cast_expressionContext extends ExprContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Cast_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterCast_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitCast_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitCast_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Qualified_id_expressionContext extends ExprContext {

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Qualified_id_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterQualified_id_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitQualified_id_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitQualified_id_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Division_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Division_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterDivision_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitDivision_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitDivision_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Subtraction_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Subtraction_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterSubtraction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitSubtraction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitSubtraction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Plus_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Plus_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterPlus_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitPlus_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitPlus_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Function_expressionContext extends ExprContext {

        public Qualified_idContext object;
        public Token function_name;

        public TerminalNode ID() {
            return getToken(DDLParser.ID, 0);
        }

        public Expr_listContext expr_list() {
            return getRuleContext(Expr_listContext.class, 0);
        }

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Function_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterFunction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitFunction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitFunction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Addition_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Addition_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterAddition_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitAddition_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitAddition_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Minus_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Minus_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterMinus_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitMinus_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitMinus_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Parentheses_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Parentheses_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterParentheses_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitParentheses_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitParentheses_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Implication_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Implication_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterImplication_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitImplication_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitImplication_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Lt_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Lt_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterLt_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitLt_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitLt_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Not_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Not_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterNot_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitNot_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitNot_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Conjunction_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Conjunction_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterConjunction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitConjunction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitConjunction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Geq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Geq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterGeq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitGeq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitGeq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Range_expressionContext extends ExprContext {

        public ExprContext min;
        public ExprContext max;

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Range_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterRange_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitRange_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitRange_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Multiplication_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Multiplication_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterMultiplication_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitMultiplication_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitMultiplication_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Leq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Leq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterLeq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitLeq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitLeq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Exclusive_disjunction_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Exclusive_disjunction_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterExclusive_disjunction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitExclusive_disjunction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitExclusive_disjunction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Gt_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Gt_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterGt_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitGt_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitGt_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Constructor_expressionContext extends ExprContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public Expr_listContext expr_list() {
            return getRuleContext(Expr_listContext.class, 0);
        }

        public Constructor_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterConstructor_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitConstructor_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitConstructor_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Disjunction_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Disjunction_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterDisjunction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitDisjunction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitDisjunction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Literal_expressionContext extends ExprContext {

        public LiteralContext literal() {
            return getRuleContext(LiteralContext.class, 0);
        }

        public Literal_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterLiteral_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitLiteral_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitLiteral_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Eq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Eq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterEq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitEq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitEq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Neq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Neq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterNeq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitNeq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitNeq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ExprContext expr() throws RecognitionException {
        return expr(0);
    }

    private ExprContext expr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExprContext _localctx = new ExprContext(_ctx, _parentState);
        ExprContext _prevctx = _localctx;
        int _startState = 50;
        enterRecursionRule(_localctx, 50, RULE_expr, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(358);
                switch (getInterpreter().adaptivePredict(_input, 33, _ctx)) {
                    case 1: {
                        _localctx = new Plus_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;

                        setState(316);
                        match(PLUS);
                        setState(317);
                        expr(18);
                    }
                    break;
                    case 2: {
                        _localctx = new Minus_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(318);
                        match(MINUS);
                        setState(319);
                        expr(17);
                    }
                    break;
                    case 3: {
                        _localctx = new Not_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(320);
                        match(BANG);
                        setState(321);
                        expr(16);
                    }
                    break;
                    case 4: {
                        _localctx = new Cast_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(322);
                        match(LPAREN);
                        setState(323);
                        type();
                        setState(324);
                        match(RPAREN);
                        setState(325);
                        expr(13);
                    }
                    break;
                    case 5: {
                        _localctx = new Literal_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(327);
                        literal();
                    }
                    break;
                    case 6: {
                        _localctx = new Parentheses_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(328);
                        match(LPAREN);
                        setState(329);
                        expr(0);
                        setState(330);
                        match(RPAREN);
                    }
                    break;
                    case 7: {
                        _localctx = new Qualified_id_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(332);
                        qualified_id();
                    }
                    break;
                    case 8: {
                        _localctx = new Function_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(336);
                        switch (getInterpreter().adaptivePredict(_input, 30, _ctx)) {
                            case 1: {
                                setState(333);
                                ((Function_expressionContext) _localctx).object = qualified_id();
                                setState(334);
                                match(DOT);
                            }
                            break;
                        }
                        setState(338);
                        ((Function_expressionContext) _localctx).function_name = match(ID);
                        setState(339);
                        match(LPAREN);
                        setState(341);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEW) | (1L << THIS) | (1L << SUPER) | (1L << TRUE) | (1L << FALSE) | (1L << LPAREN) | (1L << LBRACKET) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                            {
                                setState(340);
                                expr_list();
                            }
                        }

                        setState(343);
                        match(RPAREN);
                    }
                    break;
                    case 9: {
                        _localctx = new Range_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(344);
                        match(LBRACKET);
                        setState(345);
                        ((Range_expressionContext) _localctx).min = expr(0);
                        setState(346);
                        match(COMMA);
                        setState(347);
                        ((Range_expressionContext) _localctx).max = expr(0);
                        setState(348);
                        match(RBRACKET);
                    }
                    break;
                    case 10: {
                        _localctx = new Constructor_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(350);
                        match(NEW);
                        setState(351);
                        type();
                        setState(352);
                        match(LPAREN);
                        setState(354);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEW) | (1L << THIS) | (1L << SUPER) | (1L << TRUE) | (1L << FALSE) | (1L << LPAREN) | (1L << LBRACKET) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                            {
                                setState(353);
                                expr_list();
                            }
                        }

                        setState(356);
                        match(RPAREN);
                    }
                    break;
                }
                _ctx.stop = _input.LT(-1);
                setState(416);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 38, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(414);
                            switch (getInterpreter().adaptivePredict(_input, 37, _ctx)) {
                                case 1: {
                                    _localctx = new Multiplication_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(360);
                                    if (!(precpred(_ctx, 22))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 22)");
                                    }
                                    setState(361);
                                    match(STAR);
                                    setState(362);
                                    expr(23);
                                }
                                break;
                                case 2: {
                                    _localctx = new Division_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(363);
                                    if (!(precpred(_ctx, 21))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 21)");
                                    }
                                    setState(364);
                                    match(SLASH);
                                    setState(365);
                                    expr(22);
                                }
                                break;
                                case 3: {
                                    _localctx = new Addition_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(366);
                                    if (!(precpred(_ctx, 20))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 20)");
                                    }
                                    setState(367);
                                    match(PLUS);
                                    setState(368);
                                    expr(21);
                                }
                                break;
                                case 4: {
                                    _localctx = new Subtraction_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(369);
                                    if (!(precpred(_ctx, 19))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 19)");
                                    }
                                    setState(370);
                                    match(MINUS);
                                    setState(371);
                                    expr(20);
                                }
                                break;
                                case 5: {
                                    _localctx = new Eq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(372);
                                    if (!(precpred(_ctx, 10))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 10)");
                                    }
                                    setState(373);
                                    match(EQEQ);
                                    setState(374);
                                    expr(11);
                                }
                                break;
                                case 6: {
                                    _localctx = new Geq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(375);
                                    if (!(precpred(_ctx, 9))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 9)");
                                    }
                                    setState(376);
                                    match(GTEQ);
                                    setState(377);
                                    expr(10);
                                }
                                break;
                                case 7: {
                                    _localctx = new Leq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(378);
                                    if (!(precpred(_ctx, 8))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 8)");
                                    }
                                    setState(379);
                                    match(LTEQ);
                                    setState(380);
                                    expr(9);
                                }
                                break;
                                case 8: {
                                    _localctx = new Gt_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(381);
                                    if (!(precpred(_ctx, 7))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                                    }
                                    setState(382);
                                    match(GT);
                                    setState(383);
                                    expr(8);
                                }
                                break;
                                case 9: {
                                    _localctx = new Lt_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(384);
                                    if (!(precpred(_ctx, 6))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                                    }
                                    setState(385);
                                    match(LT);
                                    setState(386);
                                    expr(7);
                                }
                                break;
                                case 10: {
                                    _localctx = new Neq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(387);
                                    if (!(precpred(_ctx, 5))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    }
                                    setState(388);
                                    match(BANGEQ);
                                    setState(389);
                                    expr(6);
                                }
                                break;
                                case 11: {
                                    _localctx = new Implication_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(390);
                                    if (!(precpred(_ctx, 4))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    }
                                    setState(391);
                                    match(IMPLICATION);
                                    setState(392);
                                    expr(5);
                                }
                                break;
                                case 12: {
                                    _localctx = new Disjunction_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(393);
                                    if (!(precpred(_ctx, 3))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    }
                                    setState(396);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(394);
                                                    match(BARBAR);
                                                    setState(395);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(398);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 34, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                                case 13: {
                                    _localctx = new Conjunction_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(400);
                                    if (!(precpred(_ctx, 2))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    }
                                    setState(403);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(401);
                                                    match(AMPAMP);
                                                    setState(402);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(405);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 35, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                                case 14: {
                                    _localctx = new Exclusive_disjunction_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(407);
                                    if (!(precpred(_ctx, 1))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    }
                                    setState(410);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(408);
                                                    match(CARET);
                                                    setState(409);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(412);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 36, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                            }
                        }
                    }
                    setState(418);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 38, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class Expr_listContext extends ParserRuleContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Expr_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr_list;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterExpr_list(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitExpr_list(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitExpr_list(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Expr_listContext expr_list() throws RecognitionException {
        Expr_listContext _localctx = new Expr_listContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_expr_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(419);
                expr(0);
                setState(424);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(420);
                            match(COMMA);
                            setState(421);
                            expr(0);
                        }
                    }
                    setState(426);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
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

    public static class LiteralContext extends ParserRuleContext {

        public Token numeric;
        public Token string;
        public Token t;
        public Token f;

        public TerminalNode NumericLiteral() {
            return getToken(DDLParser.NumericLiteral, 0);
        }

        public TerminalNode StringLiteral() {
            return getToken(DDLParser.StringLiteral, 0);
        }

        public LiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_literal;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterLiteral(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitLiteral(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitLiteral(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final LiteralContext literal() throws RecognitionException {
        LiteralContext _localctx = new LiteralContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_literal);
        try {
            setState(431);
            switch (_input.LA(1)) {
                case NumericLiteral:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(427);
                        ((LiteralContext) _localctx).numeric = match(NumericLiteral);
                    }
                    break;
                case StringLiteral:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(428);
                        ((LiteralContext) _localctx).string = match(StringLiteral);
                    }
                    break;
                case TRUE:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(429);
                        ((LiteralContext) _localctx).t = match(TRUE);
                    }
                    break;
                case FALSE:
                    enterOuterAlt(_localctx, 4);
                     {
                        setState(430);
                        ((LiteralContext) _localctx).f = match(FALSE);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
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

    public static class Qualified_idContext extends ParserRuleContext {

        public Token t;
        public Token s;

        public List<TerminalNode> ID() {
            return getTokens(DDLParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(DDLParser.ID, i);
        }

        public Qualified_idContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_qualified_id;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterQualified_id(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitQualified_id(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitQualified_id(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Qualified_idContext qualified_id() throws RecognitionException {
        Qualified_idContext _localctx = new Qualified_idContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_qualified_id);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(436);
                switch (_input.LA(1)) {
                    case THIS: {
                        setState(433);
                        ((Qualified_idContext) _localctx).t = match(THIS);
                    }
                    break;
                    case SUPER: {
                        setState(434);
                        ((Qualified_idContext) _localctx).s = match(SUPER);
                    }
                    break;
                    case ID: {
                        setState(435);
                        match(ID);
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(442);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 42, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(438);
                                match(DOT);
                                setState(439);
                                match(ID);
                            }
                        }
                    }
                    setState(444);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 42, _ctx);
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

    public static class TypeContext extends ParserRuleContext {

        public Class_typeContext class_type() {
            return getRuleContext(Class_typeContext.class, 0);
        }

        public Primitive_typeContext primitive_type() {
            return getRuleContext(Primitive_typeContext.class, 0);
        }

        public TypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterType(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitType(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitType(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final TypeContext type() throws RecognitionException {
        TypeContext _localctx = new TypeContext(_ctx, getState());
        enterRule(_localctx, 58, RULE_type);
        try {
            setState(447);
            switch (_input.LA(1)) {
                case ID:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(445);
                        class_type();
                    }
                    break;
                case INT:
                case REAL:
                case BOOL:
                case STRING:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(446);
                        primitive_type();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
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

    public static class Class_typeContext extends ParserRuleContext {

        public List<TerminalNode> ID() {
            return getTokens(DDLParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(DDLParser.ID, i);
        }

        public Class_typeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_class_type;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterClass_type(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitClass_type(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitClass_type(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Class_typeContext class_type() throws RecognitionException {
        Class_typeContext _localctx = new Class_typeContext(_ctx, getState());
        enterRule(_localctx, 60, RULE_class_type);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(449);
                match(ID);
                setState(454);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == DOT) {
                    {
                        {
                            setState(450);
                            match(DOT);
                            setState(451);
                            match(ID);
                        }
                    }
                    setState(456);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
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

    public static class Primitive_typeContext extends ParserRuleContext {

        public Primitive_typeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_primitive_type;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterPrimitive_type(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitPrimitive_type(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitPrimitive_type(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Primitive_typeContext primitive_type() throws RecognitionException {
        Primitive_typeContext _localctx = new Primitive_typeContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_primitive_type);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(457);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << REAL) | (1L << BOOL) | (1L << STRING))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    consume();
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

    public static class Typed_listContext extends ParserRuleContext {

        public List<TypeContext> type() {
            return getRuleContexts(TypeContext.class);
        }

        public TypeContext type(int i) {
            return getRuleContext(TypeContext.class, i);
        }

        public List<TerminalNode> ID() {
            return getTokens(DDLParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(DDLParser.ID, i);
        }

        public Typed_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_typed_list;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).enterTyped_list(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DDLListener) {
                ((DDLListener) listener).exitTyped_list(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DDLVisitor) {
                return ((DDLVisitor<? extends T>) visitor).visitTyped_list(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Typed_listContext typed_list() throws RecognitionException {
        Typed_listContext _localctx = new Typed_listContext(_ctx, getState());
        enterRule(_localctx, 64, RULE_typed_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(459);
                type();
                setState(460);
                match(ID);
                setState(467);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(461);
                            match(COMMA);
                            setState(462);
                            type();
                            setState(463);
                            match(ID);
                        }
                    }
                    setState(469);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
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

    @Override
    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 25:
                return expr_sempred((ExprContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 22);
            case 1:
                return precpred(_ctx, 21);
            case 2:
                return precpred(_ctx, 20);
            case 3:
                return precpred(_ctx, 19);
            case 4:
                return precpred(_ctx, 10);
            case 5:
                return precpred(_ctx, 9);
            case 6:
                return precpred(_ctx, 8);
            case 7:
                return precpred(_ctx, 7);
            case 8:
                return precpred(_ctx, 6);
            case 9:
                return precpred(_ctx, 5);
            case 10:
                return precpred(_ctx, 4);
            case 11:
                return precpred(_ctx, 3);
            case 12:
                return precpred(_ctx, 2);
            case 13:
                return precpred(_ctx, 1);
        }
        return true;
    }
    public static final String _serializedATN
            = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\67\u01d9\4\2\t\2"
            + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
            + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
            + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
            + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
            + "\t!\4\"\t\"\3\2\3\2\3\2\3\2\7\2I\n\2\f\2\16\2L\13\2\3\2\3\2\3\3\3\3\3"
            + "\3\5\3S\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\7\5`\n\5\f\5\16"
            + "\5c\13\5\3\5\3\5\3\6\3\6\3\6\3\6\7\6k\n\6\f\6\16\6n\13\6\3\6\3\6\5\6r"
            + "\n\6\3\7\3\7\3\7\3\7\5\7x\n\7\3\7\3\7\7\7|\n\7\f\7\16\7\177\13\7\3\7\3"
            + "\7\3\b\3\b\3\b\3\b\3\b\5\b\u0088\n\b\3\t\3\t\3\t\3\t\7\t\u008e\n\t\f\t"
            + "\16\t\u0091\13\t\3\t\3\t\3\n\3\n\3\n\5\n\u0098\n\n\3\13\3\13\3\13\3\13"
            + "\5\13\u009e\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00a9"
            + "\n\13\3\13\3\13\3\13\3\13\3\13\5\13\u00b0\n\13\3\f\3\f\3\f\5\f\u00b5\n"
            + "\f\3\f\3\f\3\f\5\f\u00ba\n\f\3\f\3\f\3\f\3\r\3\r\3\r\5\r\u00c2\n\r\3\r"
            + "\3\r\3\r\3\r\3\r\5\r\u00c9\n\r\3\r\3\r\5\r\u00cd\n\r\3\16\3\16\3\16\3"
            + "\16\5\16\u00d3\n\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"
            + "\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00e5\n\17\3\20\7\20\u00e8\n\20\f"
            + "\20\16\20\u00eb\13\20\3\21\3\21\3\21\5\21\u00f0\n\21\3\21\3\21\3\21\3"
            + "\21\3\21\3\22\3\22\3\22\3\22\7\22\u00fb\n\22\f\22\16\22\u00fe\13\22\3"
            + "\22\3\22\3\23\3\23\3\23\3\24\5\24\u0106\n\24\3\24\3\24\3\24\3\24\3\24"
            + "\3\24\3\24\3\25\3\25\3\25\6\25\u0112\n\25\r\25\16\25\u0113\3\26\3\26\3"
            + "\26\3\26\3\27\3\27\5\27\u011c\n\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27"
            + "\u0124\n\27\3\27\3\27\3\27\5\27\u0129\n\27\3\27\3\27\3\27\3\30\3\30\3"
            + "\30\3\30\3\31\3\31\3\31\7\31\u0135\n\31\f\31\16\31\u0138\13\31\3\32\3"
            + "\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"
            + "\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u0153\n\33\3\33"
            + "\3\33\3\33\5\33\u0158\n\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33"
            + "\3\33\3\33\5\33\u0165\n\33\3\33\3\33\5\33\u0169\n\33\3\33\3\33\3\33\3"
            + "\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"
            + "\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"
            + "\33\3\33\3\33\3\33\3\33\6\33\u018f\n\33\r\33\16\33\u0190\3\33\3\33\3\33"
            + "\6\33\u0196\n\33\r\33\16\33\u0197\3\33\3\33\3\33\6\33\u019d\n\33\r\33"
            + "\16\33\u019e\7\33\u01a1\n\33\f\33\16\33\u01a4\13\33\3\34\3\34\3\34\7\34"
            + "\u01a9\n\34\f\34\16\34\u01ac\13\34\3\35\3\35\3\35\3\35\5\35\u01b2\n\35"
            + "\3\36\3\36\3\36\5\36\u01b7\n\36\3\36\3\36\7\36\u01bb\n\36\f\36\16\36\u01be"
            + "\13\36\3\37\3\37\5\37\u01c2\n\37\3 \3 \3 \7 \u01c7\n \f \16 \u01ca\13"
            + " \3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\7\"\u01d4\n\"\f\"\16\"\u01d7\13\"\3\""
            + "\2\3\64#\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668"
            + ":<>@B\2\3\3\2\4\7\u0208\2J\3\2\2\2\4R\3\2\2\2\6T\3\2\2\2\bZ\3\2\2\2\n"
            + "q\3\2\2\2\fs\3\2\2\2\16\u0087\3\2\2\2\20\u0089\3\2\2\2\22\u0094\3\2\2"
            + "\2\24\u00af\3\2\2\2\26\u00b1\3\2\2\2\30\u00cc\3\2\2\2\32\u00ce\3\2\2\2"
            + "\34\u00e4\3\2\2\2\36\u00e9\3\2\2\2 \u00ef\3\2\2\2\"\u00f6\3\2\2\2$\u0101"
            + "\3\2\2\2&\u0105\3\2\2\2(\u010e\3\2\2\2*\u0115\3\2\2\2,\u011b\3\2\2\2."
            + "\u012d\3\2\2\2\60\u0131\3\2\2\2\62\u0139\3\2\2\2\64\u0168\3\2\2\2\66\u01a5"
            + "\3\2\2\28\u01b1\3\2\2\2:\u01b6\3\2\2\2<\u01c1\3\2\2\2>\u01c3\3\2\2\2@"
            + "\u01cb\3\2\2\2B\u01cd\3\2\2\2DI\5\4\3\2EI\5\24\13\2FI\5\32\16\2GI\5\34"
            + "\17\2HD\3\2\2\2HE\3\2\2\2HF\3\2\2\2HG\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3"
            + "\2\2\2KM\3\2\2\2LJ\3\2\2\2MN\7\2\2\3N\3\3\2\2\2OS\5\6\4\2PS\5\b\5\2QS"
            + "\5\f\7\2RO\3\2\2\2RP\3\2\2\2RQ\3\2\2\2S\5\3\2\2\2TU\7\3\2\2UV\5@!\2VW"
            + "\5\64\33\2WX\7\62\2\2XY\7\31\2\2Y\7\3\2\2\2Z[\7\b\2\2[\\\7\62\2\2\\a\5"
            + "\n\6\2]^\7$\2\2^`\5\n\6\2_]\3\2\2\2`c\3\2\2\2a_\3\2\2\2ab\3\2\2\2bd\3"
            + "\2\2\2ca\3\2\2\2de\7\31\2\2e\t\3\2\2\2fg\7\36\2\2gl\7\64\2\2hi\7\27\2"
            + "\2ik\7\64\2\2jh\3\2\2\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2\2mo\3\2\2\2nl\3\2"
            + "\2\2or\7\37\2\2pr\5<\37\2qf\3\2\2\2qp\3\2\2\2r\13\3\2\2\2st\7\t\2\2tw"
            + "\7\62\2\2uv\7\f\2\2vx\5<\37\2wu\3\2\2\2wx\3\2\2\2xy\3\2\2\2y}\7\36\2\2"
            + "z|\5\16\b\2{z\3\2\2\2|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\u0080\3\2\2\2"
            + "\177}\3\2\2\2\u0080\u0081\7\37\2\2\u0081\r\3\2\2\2\u0082\u0088\5\20\t"
            + "\2\u0083\u0088\5\24\13\2\u0084\u0088\5\26\f\2\u0085\u0088\5\32\16\2\u0086"
            + "\u0088\5\4\3\2\u0087\u0082\3\2\2\2\u0087\u0083\3\2\2\2\u0087\u0084\3\2"
            + "\2\2\u0087\u0085\3\2\2\2\u0087\u0086\3\2\2\2\u0088\17\3\2\2\2\u0089\u008a"
            + "\5<\37\2\u008a\u008f\5\22\n\2\u008b\u008c\7\27\2\2\u008c\u008e\5\22\n"
            + "\2\u008d\u008b\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090"
            + "\3\2\2\2\u0090\u0092\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7\31\2\2"
            + "\u0093\21\3\2\2\2\u0094\u0097\7\62\2\2\u0095\u0096\7%\2\2\u0096\u0098"
            + "\5\64\33\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\23\3\2\2\2\u0099"
            + "\u009a\7\22\2\2\u009a\u009b\7\62\2\2\u009b\u009d\7\32\2\2\u009c\u009e"
            + "\5B\"\2\u009d\u009c\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009f"
            + "\u00a0\7\33\2\2\u00a0\u00a1\7\36\2\2\u00a1\u00a2\5\36\20\2\u00a2\u00a3"
            + "\7\37\2\2\u00a3\u00b0\3\2\2\2\u00a4\u00a5\5<\37\2\u00a5\u00a6\7\62\2\2"
            + "\u00a6\u00a8\7\32\2\2\u00a7\u00a9\5B\"\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9"
            + "\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\7\33\2\2\u00ab\u00ac\7\36\2\2"
            + "\u00ac\u00ad\5\36\20\2\u00ad\u00ae\7\37\2\2\u00ae\u00b0\3\2\2\2\u00af"
            + "\u0099\3\2\2\2\u00af\u00a4\3\2\2\2\u00b0\25\3\2\2\2\u00b1\u00b2\7\62\2"
            + "\2\u00b2\u00b4\7\32\2\2\u00b3\u00b5\5B\"\2\u00b4\u00b3\3\2\2\2\u00b4\u00b5"
            + "\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\7\33\2\2\u00b7\u00b9\7\36\2\2"
            + "\u00b8\u00ba\5\30\r\2\u00b9\u00b8\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bb"
            + "\3\2\2\2\u00bb\u00bc\5\36\20\2\u00bc\u00bd\7\37\2\2\u00bd\27\3\2\2\2\u00be"
            + "\u00bf\7\20\2\2\u00bf\u00c1\7\32\2\2\u00c0\u00c2\5\66\34\2\u00c1\u00c0"
            + "\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4\7\33\2\2"
            + "\u00c4\u00cd\7\31\2\2\u00c5\u00c6\7\21\2\2\u00c6\u00c8\7\32\2\2\u00c7"
            + "\u00c9\5\66\34\2\u00c8\u00c7\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00ca\3"
            + "\2\2\2\u00ca\u00cb\7\33\2\2\u00cb\u00cd\7\31\2\2\u00cc\u00be\3\2\2\2\u00cc"
            + "\u00c5\3\2\2\2\u00cd\31\3\2\2\2\u00ce\u00cf\7\r\2\2\u00cf\u00d0\7\62\2"
            + "\2\u00d0\u00d2\7\32\2\2\u00d1\u00d3\5B\"\2\u00d2\u00d1\3\2\2\2\u00d2\u00d3"
            + "\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5\7\33\2\2\u00d5\u00d6\7\36\2\2"
            + "\u00d6\u00d7\5\36\20\2\u00d7\u00d8\7\37\2\2\u00d8\33\3\2\2\2\u00d9\u00e5"
            + "\5 \21\2\u00da\u00e5\5\"\22\2\u00db\u00e5\5$\23\2\u00dc\u00e5\5&\24\2"
            + "\u00dd\u00e5\5(\25\2\u00de\u00e5\5,\27\2\u00df\u00e5\5.\30\2\u00e0\u00e1"
            + "\7\36\2\2\u00e1\u00e2\5\36\20\2\u00e2\u00e3\7\37\2\2\u00e3\u00e5\3\2\2"
            + "\2\u00e4\u00d9\3\2\2\2\u00e4\u00da\3\2\2\2\u00e4\u00db\3\2\2\2\u00e4\u00dc"
            + "\3\2\2\2\u00e4\u00dd\3\2\2\2\u00e4\u00de\3\2\2\2\u00e4\u00df\3\2\2\2\u00e4"
            + "\u00e0\3\2\2\2\u00e5\35\3\2\2\2\u00e6\u00e8\5\34\17\2\u00e7\u00e6\3\2"
            + "\2\2\u00e8\u00eb\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea"
            + "\37\3\2\2\2\u00eb\u00e9\3\2\2\2\u00ec\u00ed\5:\36\2\u00ed\u00ee\7\26\2"
            + "\2\u00ee\u00f0\3\2\2\2\u00ef\u00ec\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f1"
            + "\3\2\2\2\u00f1\u00f2\7\62\2\2\u00f2\u00f3\7%\2\2\u00f3\u00f4\5\64\33\2"
            + "\u00f4\u00f5\7\31\2\2\u00f5!\3\2\2\2\u00f6\u00f7\5<\37\2\u00f7\u00fc\5"
            + "\22\n\2\u00f8\u00f9\7\27\2\2\u00f9\u00fb\5\22\n\2\u00fa\u00f8\3\2\2\2"
            + "\u00fb\u00fe\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00ff"
            + "\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0100\7\31\2\2\u0100#\3\2\2\2\u0101"
            + "\u0102\5\64\33\2\u0102\u0103\7\31\2\2\u0103%\3\2\2\2\u0104\u0106\7!\2"
            + "\2\u0105\u0104\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0107\3\2\2\2\u0107\u0108"
            + "\7\34\2\2\u0108\u0109\5\36\20\2\u0109\u010a\7\35\2\2\u010a\u010b\7\32"
            + "\2\2\u010b\u010c\5\64\33\2\u010c\u010d\7\33\2\2\u010d\'\3\2\2\2\u010e"
            + "\u0111\5*\26\2\u010f\u0110\7\17\2\2\u0110\u0112\5*\26\2\u0111\u010f\3"
            + "\2\2\2\u0112\u0113\3\2\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114"
            + ")\3\2\2\2\u0115\u0116\7\36\2\2\u0116\u0117\5\36\20\2\u0117\u0118\7\37"
            + "\2\2\u0118+\3\2\2\2\u0119\u011c\7\n\2\2\u011a\u011c\7\13\2\2\u011b\u0119"
            + "\3\2\2\2\u011b\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d\u011e\7\62\2\2"
            + "\u011e\u011f\7%\2\2\u011f\u0123\7\16\2\2\u0120\u0121\5:\36\2\u0121\u0122"
            + "\7\26\2\2\u0122\u0124\3\2\2\2\u0123\u0120\3\2\2\2\u0123\u0124\3\2\2\2"
            + "\u0124\u0125\3\2\2\2\u0125\u0126\7\62\2\2\u0126\u0128\7\32\2\2\u0127\u0129"
            + "\5\60\31\2\u0128\u0127\3\2\2\2\u0128\u0129\3\2\2\2\u0129\u012a\3\2\2\2"
            + "\u012a\u012b\7\33\2\2\u012b\u012c\7\31\2\2\u012c-\3\2\2\2\u012d\u012e"
            + "\7\25\2\2\u012e\u012f\5\64\33\2\u012f\u0130\7\31\2\2\u0130/\3\2\2\2\u0131"
            + "\u0136\5\62\32\2\u0132\u0133\7\27\2\2\u0133\u0135\5\62\32\2\u0134\u0132"
            + "\3\2\2\2\u0135\u0138\3\2\2\2\u0136\u0134\3\2\2\2\u0136\u0137\3\2\2\2\u0137"
            + "\61\3\2\2\2\u0138\u0136\3\2\2\2\u0139\u013a\7\62\2\2\u013a\u013b\7\30"
            + "\2\2\u013b\u013c\5\64\33\2\u013c\63\3\2\2\2\u013d\u013e\b\33\1\2\u013e"
            + "\u013f\7 \2\2\u013f\u0169\5\64\33\24\u0140\u0141\7!\2\2\u0141\u0169\5"
            + "\64\33\23\u0142\u0143\7(\2\2\u0143\u0169\5\64\33\22\u0144\u0145\7\32\2"
            + "\2\u0145\u0146\5<\37\2\u0146\u0147\7\33\2\2\u0147\u0148\5\64\33\17\u0148"
            + "\u0169\3\2\2\2\u0149\u0169\58\35\2\u014a\u014b\7\32\2\2\u014b\u014c\5"
            + "\64\33\2\u014c\u014d\7\33\2\2\u014d\u0169\3\2\2\2\u014e\u0169\5:\36\2"
            + "\u014f\u0150\5:\36\2\u0150\u0151\7\26\2\2\u0151\u0153\3\2\2\2\u0152\u014f"
            + "\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0154\3\2\2\2\u0154\u0155\7\62\2\2"
            + "\u0155\u0157\7\32\2\2\u0156\u0158\5\66\34\2\u0157\u0156\3\2\2\2\u0157"
            + "\u0158\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u0169\7\33\2\2\u015a\u015b\7"
            + "\34\2\2\u015b\u015c\5\64\33\2\u015c\u015d\7\27\2\2\u015d\u015e\5\64\33"
            + "\2\u015e\u015f\7\35\2\2\u015f\u0169\3\2\2\2\u0160\u0161\7\16\2\2\u0161"
            + "\u0162\5<\37\2\u0162\u0164\7\32\2\2\u0163\u0165\5\66\34\2\u0164\u0163"
            + "\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0167\7\33\2\2"
            + "\u0167\u0169\3\2\2\2\u0168\u013d\3\2\2\2\u0168\u0140\3\2\2\2\u0168\u0142"
            + "\3\2\2\2\u0168\u0144\3\2\2\2\u0168\u0149\3\2\2\2\u0168\u014a\3\2\2\2\u0168"
            + "\u014e\3\2\2\2\u0168\u0152\3\2\2\2\u0168\u015a\3\2\2\2\u0168\u0160\3\2"
            + "\2\2\u0169\u01a2\3\2\2\2\u016a\u016b\f\30\2\2\u016b\u016c\7\"\2\2\u016c"
            + "\u01a1\5\64\33\31\u016d\u016e\f\27\2\2\u016e\u016f\7#\2\2\u016f\u01a1"
            + "\5\64\33\30\u0170\u0171\f\26\2\2\u0171\u0172\7 \2\2\u0172\u01a1\5\64\33"
            + "\27\u0173\u0174\f\25\2\2\u0174\u0175\7!\2\2\u0175\u01a1\5\64\33\26\u0176"
            + "\u0177\f\f\2\2\u0177\u0178\7*\2\2\u0178\u01a1\5\64\33\r\u0179\u017a\f"
            + "\13\2\2\u017a\u017b\7,\2\2\u017b\u01a1\5\64\33\f\u017c\u017d\f\n\2\2\u017d"
            + "\u017e\7+\2\2\u017e\u01a1\5\64\33\13\u017f\u0180\f\t\2\2\u0180\u0181\7"
            + "&\2\2\u0181\u01a1\5\64\33\n\u0182\u0183\f\b\2\2\u0183\u0184\7\'\2\2\u0184"
            + "\u01a1\5\64\33\t\u0185\u0186\f\7\2\2\u0186\u0187\7-\2\2\u0187\u01a1\5"
            + "\64\33\b\u0188\u0189\f\6\2\2\u0189\u018a\7.\2\2\u018a\u01a1\5\64\33\7"
            + "\u018b\u018e\f\5\2\2\u018c\u018d\7\60\2\2\u018d\u018f\5\64\33\2\u018e"
            + "\u018c\3\2\2\2\u018f\u0190\3\2\2\2\u0190\u018e\3\2\2\2\u0190\u0191\3\2"
            + "\2\2\u0191\u01a1\3\2\2\2\u0192\u0195\f\4\2\2\u0193\u0194\7/\2\2\u0194"
            + "\u0196\5\64\33\2\u0195\u0193\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0195\3"
            + "\2\2\2\u0197\u0198\3\2\2\2\u0198\u01a1\3\2\2\2\u0199\u019c\f\3\2\2\u019a"
            + "\u019b\7\61\2\2\u019b\u019d\5\64\33\2\u019c\u019a\3\2\2\2\u019d\u019e"
            + "\3\2\2\2\u019e\u019c\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a1\3\2\2\2\u01a0"
            + "\u016a\3\2\2\2\u01a0\u016d\3\2\2\2\u01a0\u0170\3\2\2\2\u01a0\u0173\3\2"
            + "\2\2\u01a0\u0176\3\2\2\2\u01a0\u0179\3\2\2\2\u01a0\u017c\3\2\2\2\u01a0"
            + "\u017f\3\2\2\2\u01a0\u0182\3\2\2\2\u01a0\u0185\3\2\2\2\u01a0\u0188\3\2"
            + "\2\2\u01a0\u018b\3\2\2\2\u01a0\u0192\3\2\2\2\u01a0\u0199\3\2\2\2\u01a1"
            + "\u01a4\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3\65\3\2\2"
            + "\2\u01a4\u01a2\3\2\2\2\u01a5\u01aa\5\64\33\2\u01a6\u01a7\7\27\2\2\u01a7"
            + "\u01a9\5\64\33\2\u01a8\u01a6\3\2\2\2\u01a9\u01ac\3\2\2\2\u01aa\u01a8\3"
            + "\2\2\2\u01aa\u01ab\3\2\2\2\u01ab\67\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad"
            + "\u01b2\7\63\2\2\u01ae\u01b2\7\64\2\2\u01af\u01b2\7\23\2\2\u01b0\u01b2"
            + "\7\24\2\2\u01b1\u01ad\3\2\2\2\u01b1\u01ae\3\2\2\2\u01b1\u01af\3\2\2\2"
            + "\u01b1\u01b0\3\2\2\2\u01b29\3\2\2\2\u01b3\u01b7\7\20\2\2\u01b4\u01b7\7"
            + "\21\2\2\u01b5\u01b7\7\62\2\2\u01b6\u01b3\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b6"
            + "\u01b5\3\2\2\2\u01b7\u01bc\3\2\2\2\u01b8\u01b9\7\26\2\2\u01b9\u01bb\7"
            + "\62\2\2\u01ba\u01b8\3\2\2\2\u01bb\u01be\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bc"
            + "\u01bd\3\2\2\2\u01bd;\3\2\2\2\u01be\u01bc\3\2\2\2\u01bf\u01c2\5> \2\u01c0"
            + "\u01c2\5@!\2\u01c1\u01bf\3\2\2\2\u01c1\u01c0\3\2\2\2\u01c2=\3\2\2\2\u01c3"
            + "\u01c8\7\62\2\2\u01c4\u01c5\7\26\2\2\u01c5\u01c7\7\62\2\2\u01c6\u01c4"
            + "\3\2\2\2\u01c7\u01ca\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9"
            + "?\3\2\2\2\u01ca\u01c8\3\2\2\2\u01cb\u01cc\t\2\2\2\u01ccA\3\2\2\2\u01cd"
            + "\u01ce\5<\37\2\u01ce\u01d5\7\62\2\2\u01cf\u01d0\7\27\2\2\u01d0\u01d1\5"
            + "<\37\2\u01d1\u01d2\7\62\2\2\u01d2\u01d4\3\2\2\2\u01d3\u01cf\3\2\2\2\u01d4"
            + "\u01d7\3\2\2\2\u01d5\u01d3\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6C\3\2\2\2"
            + "\u01d7\u01d5\3\2\2\2\60HJRalqw}\u0087\u008f\u0097\u009d\u00a8\u00af\u00b4"
            + "\u00b9\u00c1\u00c8\u00cc\u00d2\u00e4\u00e9\u00ef\u00fc\u0105\u0113\u011b"
            + "\u0123\u0128\u0136\u0152\u0157\u0164\u0168\u0190\u0197\u019e\u01a0\u01a2"
            + "\u01aa\u01b1\u01b6\u01bc\u01c1\u01c8\u01d5";
    public static final ATN _ATN
            = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
