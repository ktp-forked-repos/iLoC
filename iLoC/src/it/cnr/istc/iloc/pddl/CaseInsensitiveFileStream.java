/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.iloc.pddl;

import java.io.IOException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.misc.Utils;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
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
