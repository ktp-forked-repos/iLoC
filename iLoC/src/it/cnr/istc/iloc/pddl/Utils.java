package it.cnr.istc.iloc.pddl;

import it.cnr.istc.iloc.api.Constants;

/**
 *
 * @author Riccardo De Benedictis
 */
class Utils {

    private static final String KEYWORDS_EXPR;

    static {
        Keyword[] values = Keyword.values();
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                regex.append("|");
            }
            regex.append(values[i].name);
        }
        KEYWORDS_EXPR = regex.toString();
    }

    static String capitalize(final String str) {
        final char[] buffer = str.toLowerCase().replace('-', '_').toCharArray();
        buffer[0] = Character.toTitleCase(buffer[0]);
        // We don't need to check for keywords since all DDL keywords start lower case..
        return new String(buffer);
    }

    static String lowercase(final String str) {
        final char[] buffer = str.replace('-', '_').toCharArray();
        buffer[0] = Character.toLowerCase(buffer[0]);
        String c_str = new String(buffer);
        // We check for DDL keywords
        if (c_str.matches(KEYWORDS_EXPR)) {
            c_str = "_" + c_str;
        }
        return c_str;
    }

    private Utils() {
    }

    enum Keyword {

        TYPEDEF("typedef"),
        NUMBER(Constants.NUMBER),
        INT(Constants.INT),
        REAL(Constants.REAL),
        BOOL(Constants.BOOL),
        STRING(Constants.STRING),
        ENUM("enum"),
        CLASS("class"),
        GOAL("goal"),
        FACT("fact"),
        EXTENDS("extends"),
        PREDICATE("predicate"),
        NEW("new"),
        OR("or"),
        THIS("this"),
        SUPER("super"),
        VOID("void"),
        INF("inf"),
        TRUE("true"),
        FALSE("false"),
        STATE("state"),
        RETURN("return"),
        AT("at"),
        START("start"),
        END("end"),
        DURATION("duration");
        public final String name;

        private Keyword(String name) {
            this.name = name;
        }
    }
}
