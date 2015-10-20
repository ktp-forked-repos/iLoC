// Generated from C:\Users\pst\Documents\NetBeansProjects\lab\iLoC\src\it\cnr\istc\iloc\ddl\DDL.g4 by ANTLR 4.5.1
package it.cnr.istc.iloc.ddl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
class DDLLexer extends Lexer {

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
    public static String[] modeNames = {
        "DEFAULT_MODE"
    };
    public static final String[] ruleNames = {
        "TYPE_DEF", "INT", "REAL", "BOOL", "STRING", "ENUM", "CLASS", "GOAL",
        "FACT", "EXTENDS", "PREDICATE", "NEW", "OR", "THIS", "SUPER", "VOID",
        "TRUE", "FALSE", "RETURN", "DOT", "COMMA", "COLON", "SEMICOLON", "LPAREN",
        "RPAREN", "LBRACKET", "RBRACKET", "LBRACE", "RBRACE", "PLUS", "MINUS",
        "STAR", "SLASH", "BAR", "EQUAL", "GT", "LT", "BANG", "QUESTIONMARK", "EQEQ",
        "LTEQ", "GTEQ", "BANGEQ", "IMPLICATION", "AMPAMP", "BARBAR", "CARET",
        "ID", "NumericLiteral", "StringLiteral", "ESC", "LINE_COMMENT", "COMMENT",
        "WS"
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

    DDLLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
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
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }
    public static final String _serializedATN
            = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\67\u016c\b\1\4\2"
            + "\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"
            + "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"
            + "\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"
            + "\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"
            + " \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"
            + "+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"
            + "\t\64\4\65\t\65\4\66\t\66\4\67\t\67\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"
            + "\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6"
            + "\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3"
            + "\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f"
            + "\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3"
            + "\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3"
            + "\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3"
            + "\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3"
            + "\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3"
            + " \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\3*\3"
            + "*\3*\3+\3+\3+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3/\3\60\3\60\3\61\3\61"
            + "\7\61\u011e\n\61\f\61\16\61\u0121\13\61\3\62\6\62\u0124\n\62\r\62\16\62"
            + "\u0125\3\62\3\62\6\62\u012a\n\62\r\62\16\62\u012b\5\62\u012e\n\62\3\62"
            + "\3\62\6\62\u0132\n\62\r\62\16\62\u0133\5\62\u0136\n\62\3\63\3\63\3\63"
            + "\7\63\u013b\n\63\f\63\16\63\u013e\13\63\3\63\3\63\3\64\3\64\3\64\3\64"
            + "\5\64\u0146\n\64\3\65\3\65\3\65\3\65\7\65\u014c\n\65\f\65\16\65\u014f"
            + "\13\65\3\65\5\65\u0152\n\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\7"
            + "\66\u015c\n\66\f\66\16\66\u015f\13\66\3\66\3\66\3\66\3\66\3\66\3\67\6"
            + "\67\u0167\n\67\r\67\16\67\u0168\3\67\3\67\5\u013c\u014d\u015d\28\3\3\5"
            + "\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"
            + "!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!"
            + "A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\2i\65k\66m\67\3\2"
            + "\6\5\2C\\aac|\6\2\62;C\\aac|\3\2\62;\5\2\13\f\16\17\"\"\u0177\2\3\3\2"
            + "\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"
            + "\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"
            + "\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3"
            + "\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3"
            + "\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2"
            + "=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3"
            + "\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2"
            + "\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2"
            + "c\3\2\2\2\2e\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\3o\3\2\2\2\5w\3"
            + "\2\2\2\7{\3\2\2\2\t\u0080\3\2\2\2\13\u0085\3\2\2\2\r\u008c\3\2\2\2\17"
            + "\u0091\3\2\2\2\21\u0097\3\2\2\2\23\u009c\3\2\2\2\25\u00a1\3\2\2\2\27\u00a9"
            + "\3\2\2\2\31\u00b3\3\2\2\2\33\u00b7\3\2\2\2\35\u00ba\3\2\2\2\37\u00bf\3"
            + "\2\2\2!\u00c5\3\2\2\2#\u00ca\3\2\2\2%\u00cf\3\2\2\2\'\u00d5\3\2\2\2)\u00dc"
            + "\3\2\2\2+\u00de\3\2\2\2-\u00e0\3\2\2\2/\u00e2\3\2\2\2\61\u00e4\3\2\2\2"
            + "\63\u00e6\3\2\2\2\65\u00e8\3\2\2\2\67\u00ea\3\2\2\29\u00ec\3\2\2\2;\u00ee"
            + "\3\2\2\2=\u00f0\3\2\2\2?\u00f2\3\2\2\2A\u00f4\3\2\2\2C\u00f6\3\2\2\2E"
            + "\u00f8\3\2\2\2G\u00fa\3\2\2\2I\u00fc\3\2\2\2K\u00fe\3\2\2\2M\u0100\3\2"
            + "\2\2O\u0102\3\2\2\2Q\u0104\3\2\2\2S\u0107\3\2\2\2U\u010a\3\2\2\2W\u010d"
            + "\3\2\2\2Y\u0110\3\2\2\2[\u0113\3\2\2\2]\u0116\3\2\2\2_\u0119\3\2\2\2a"
            + "\u011b\3\2\2\2c\u0135\3\2\2\2e\u0137\3\2\2\2g\u0145\3\2\2\2i\u0147\3\2"
            + "\2\2k\u0157\3\2\2\2m\u0166\3\2\2\2op\7v\2\2pq\7{\2\2qr\7r\2\2rs\7g\2\2"
            + "st\7f\2\2tu\7g\2\2uv\7h\2\2v\4\3\2\2\2wx\7k\2\2xy\7p\2\2yz\7v\2\2z\6\3"
            + "\2\2\2{|\7t\2\2|}\7g\2\2}~\7c\2\2~\177\7n\2\2\177\b\3\2\2\2\u0080\u0081"
            + "\7d\2\2\u0081\u0082\7q\2\2\u0082\u0083\7q\2\2\u0083\u0084\7n\2\2\u0084"
            + "\n\3\2\2\2\u0085\u0086\7u\2\2\u0086\u0087\7v\2\2\u0087\u0088\7t\2\2\u0088"
            + "\u0089\7k\2\2\u0089\u008a\7p\2\2\u008a\u008b\7i\2\2\u008b\f\3\2\2\2\u008c"
            + "\u008d\7g\2\2\u008d\u008e\7p\2\2\u008e\u008f\7w\2\2\u008f\u0090\7o\2\2"
            + "\u0090\16\3\2\2\2\u0091\u0092\7e\2\2\u0092\u0093\7n\2\2\u0093\u0094\7"
            + "c\2\2\u0094\u0095\7u\2\2\u0095\u0096\7u\2\2\u0096\20\3\2\2\2\u0097\u0098"
            + "\7i\2\2\u0098\u0099\7q\2\2\u0099\u009a\7c\2\2\u009a\u009b\7n\2\2\u009b"
            + "\22\3\2\2\2\u009c\u009d\7h\2\2\u009d\u009e\7c\2\2\u009e\u009f\7e\2\2\u009f"
            + "\u00a0\7v\2\2\u00a0\24\3\2\2\2\u00a1\u00a2\7g\2\2\u00a2\u00a3\7z\2\2\u00a3"
            + "\u00a4\7v\2\2\u00a4\u00a5\7g\2\2\u00a5\u00a6\7p\2\2\u00a6\u00a7\7f\2\2"
            + "\u00a7\u00a8\7u\2\2\u00a8\26\3\2\2\2\u00a9\u00aa\7r\2\2\u00aa\u00ab\7"
            + "t\2\2\u00ab\u00ac\7g\2\2\u00ac\u00ad\7f\2\2\u00ad\u00ae\7k\2\2\u00ae\u00af"
            + "\7e\2\2\u00af\u00b0\7c\2\2\u00b0\u00b1\7v\2\2\u00b1\u00b2\7g\2\2\u00b2"
            + "\30\3\2\2\2\u00b3\u00b4\7p\2\2\u00b4\u00b5\7g\2\2\u00b5\u00b6\7y\2\2\u00b6"
            + "\32\3\2\2\2\u00b7\u00b8\7q\2\2\u00b8\u00b9\7t\2\2\u00b9\34\3\2\2\2\u00ba"
            + "\u00bb\7v\2\2\u00bb\u00bc\7j\2\2\u00bc\u00bd\7k\2\2\u00bd\u00be\7u\2\2"
            + "\u00be\36\3\2\2\2\u00bf\u00c0\7u\2\2\u00c0\u00c1\7w\2\2\u00c1\u00c2\7"
            + "r\2\2\u00c2\u00c3\7g\2\2\u00c3\u00c4\7t\2\2\u00c4 \3\2\2\2\u00c5\u00c6"
            + "\7x\2\2\u00c6\u00c7\7q\2\2\u00c7\u00c8\7k\2\2\u00c8\u00c9\7f\2\2\u00c9"
            + "\"\3\2\2\2\u00ca\u00cb\7v\2\2\u00cb\u00cc\7t\2\2\u00cc\u00cd\7w\2\2\u00cd"
            + "\u00ce\7g\2\2\u00ce$\3\2\2\2\u00cf\u00d0\7h\2\2\u00d0\u00d1\7c\2\2\u00d1"
            + "\u00d2\7n\2\2\u00d2\u00d3\7u\2\2\u00d3\u00d4\7g\2\2\u00d4&\3\2\2\2\u00d5"
            + "\u00d6\7t\2\2\u00d6\u00d7\7g\2\2\u00d7\u00d8\7v\2\2\u00d8\u00d9\7w\2\2"
            + "\u00d9\u00da\7t\2\2\u00da\u00db\7p\2\2\u00db(\3\2\2\2\u00dc\u00dd\7\60"
            + "\2\2\u00dd*\3\2\2\2\u00de\u00df\7.\2\2\u00df,\3\2\2\2\u00e0\u00e1\7<\2"
            + "\2\u00e1.\3\2\2\2\u00e2\u00e3\7=\2\2\u00e3\60\3\2\2\2\u00e4\u00e5\7*\2"
            + "\2\u00e5\62\3\2\2\2\u00e6\u00e7\7+\2\2\u00e7\64\3\2\2\2\u00e8\u00e9\7"
            + "]\2\2\u00e9\66\3\2\2\2\u00ea\u00eb\7_\2\2\u00eb8\3\2\2\2\u00ec\u00ed\7"
            + "}\2\2\u00ed:\3\2\2\2\u00ee\u00ef\7\177\2\2\u00ef<\3\2\2\2\u00f0\u00f1"
            + "\7-\2\2\u00f1>\3\2\2\2\u00f2\u00f3\7/\2\2\u00f3@\3\2\2\2\u00f4\u00f5\7"
            + ",\2\2\u00f5B\3\2\2\2\u00f6\u00f7\7\61\2\2\u00f7D\3\2\2\2\u00f8\u00f9\7"
            + "~\2\2\u00f9F\3\2\2\2\u00fa\u00fb\7?\2\2\u00fbH\3\2\2\2\u00fc\u00fd\7@"
            + "\2\2\u00fdJ\3\2\2\2\u00fe\u00ff\7>\2\2\u00ffL\3\2\2\2\u0100\u0101\7#\2"
            + "\2\u0101N\3\2\2\2\u0102\u0103\7A\2\2\u0103P\3\2\2\2\u0104\u0105\7?\2\2"
            + "\u0105\u0106\7?\2\2\u0106R\3\2\2\2\u0107\u0108\7>\2\2\u0108\u0109\7?\2"
            + "\2\u0109T\3\2\2\2\u010a\u010b\7@\2\2\u010b\u010c\7?\2\2\u010cV\3\2\2\2"
            + "\u010d\u010e\7#\2\2\u010e\u010f\7?\2\2\u010fX\3\2\2\2\u0110\u0111\7/\2"
            + "\2\u0111\u0112\7@\2\2\u0112Z\3\2\2\2\u0113\u0114\7(\2\2\u0114\u0115\7"
            + "(\2\2\u0115\\\3\2\2\2\u0116\u0117\7~\2\2\u0117\u0118\7~\2\2\u0118^\3\2"
            + "\2\2\u0119\u011a\7`\2\2\u011a`\3\2\2\2\u011b\u011f\t\2\2\2\u011c\u011e"
            + "\t\3\2\2\u011d\u011c\3\2\2\2\u011e\u0121\3\2\2\2\u011f\u011d\3\2\2\2\u011f"
            + "\u0120\3\2\2\2\u0120b\3\2\2\2\u0121\u011f\3\2\2\2\u0122\u0124\t\4\2\2"
            + "\u0123\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0123\3\2\2\2\u0125\u0126"
            + "\3\2\2\2\u0126\u012d\3\2\2\2\u0127\u0129\7\60\2\2\u0128\u012a\t\4\2\2"
            + "\u0129\u0128\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u0129\3\2\2\2\u012b\u012c"
            + "\3\2\2\2\u012c\u012e\3\2\2\2\u012d\u0127\3\2\2\2\u012d\u012e\3\2\2\2\u012e"
            + "\u0136\3\2\2\2\u012f\u0131\7\60\2\2\u0130\u0132\t\4\2\2\u0131\u0130\3"
            + "\2\2\2\u0132\u0133\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134"
            + "\u0136\3\2\2\2\u0135\u0123\3\2\2\2\u0135\u012f\3\2\2\2\u0136d\3\2\2\2"
            + "\u0137\u013c\7$\2\2\u0138\u013b\5g\64\2\u0139\u013b\13\2\2\2\u013a\u0138"
            + "\3\2\2\2\u013a\u0139\3\2\2\2\u013b\u013e\3\2\2\2\u013c\u013d\3\2\2\2\u013c"
            + "\u013a\3\2\2\2\u013d\u013f\3\2\2\2\u013e\u013c\3\2\2\2\u013f\u0140\7$"
            + "\2\2\u0140f\3\2\2\2\u0141\u0142\7^\2\2\u0142\u0146\7$\2\2\u0143\u0144"
            + "\7^\2\2\u0144\u0146\7^\2\2\u0145\u0141\3\2\2\2\u0145\u0143\3\2\2\2\u0146"
            + "h\3\2\2\2\u0147\u0148\7\61\2\2\u0148\u0149\7\61\2\2\u0149\u014d\3\2\2"
            + "\2\u014a\u014c\13\2\2\2\u014b\u014a\3\2\2\2\u014c\u014f\3\2\2\2\u014d"
            + "\u014e\3\2\2\2\u014d\u014b\3\2\2\2\u014e\u0151\3\2\2\2\u014f\u014d\3\2"
            + "\2\2\u0150\u0152\7\17\2\2\u0151\u0150\3\2\2\2\u0151\u0152\3\2\2\2\u0152"
            + "\u0153\3\2\2\2\u0153\u0154\7\f\2\2\u0154\u0155\3\2\2\2\u0155\u0156\b\65"
            + "\2\2\u0156j\3\2\2\2\u0157\u0158\7\61\2\2\u0158\u0159\7,\2\2\u0159\u015d"
            + "\3\2\2\2\u015a\u015c\13\2\2\2\u015b\u015a\3\2\2\2\u015c\u015f\3\2\2\2"
            + "\u015d\u015e\3\2\2\2\u015d\u015b\3\2\2\2\u015e\u0160\3\2\2\2\u015f\u015d"
            + "\3\2\2\2\u0160\u0161\7,\2\2\u0161\u0162\7\61\2\2\u0162\u0163\3\2\2\2\u0163"
            + "\u0164\b\66\2\2\u0164l\3\2\2\2\u0165\u0167\t\5\2\2\u0166\u0165\3\2\2\2"
            + "\u0167\u0168\3\2\2\2\u0168\u0166\3\2\2\2\u0168\u0169\3\2\2\2\u0169\u016a"
            + "\3\2\2\2\u016a\u016b\b\67\2\2\u016bn\3\2\2\2\20\2\u011f\u0125\u012b\u012d"
            + "\u0133\u0135\u013a\u013c\u0145\u014d\u0151\u015d\u0168\3\b\2\2";
    public static final ATN _ATN
            = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
