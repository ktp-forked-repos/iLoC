package it.cnr.istc.iloc.translators.pddl;

import java.io.IOException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.misc.Utils;

/**
 *
 * @author Riccardo De Benedictis
 */
class CaseInsensitiveFileStream extends ANTLRInputStream {

    protected String fileName;
    protected char[] lookaheadData;

    CaseInsensitiveFileStream(String fileName) throws IOException {
        this(fileName, null);
    }

    CaseInsensitiveFileStream(String fileName, String encoding) throws IOException {
        this.fileName = fileName;
        data = Utils.readFile(fileName, encoding);
        this.n = data.length;
        lookaheadData = new String(data).toLowerCase().toCharArray();
    }

    @Override
    public String getSourceName() {
        return fileName;
    }

    @Override
    public int LA(int i) {
        if (i == 0) {
            return 0; // undefined
        }
        if (i < 0) {
            i++; // e.g., translate LA(-1) to use offset i=0; then data[p+0-1]
            if ((p + i - 1) < 0) {
                return IntStream.EOF; // invalid; no char before first char
            }
        }

        if ((p + i - 1) >= n) {
            return IntStream.EOF;
        }

        return lookaheadData[p + i - 1];
    }
}
