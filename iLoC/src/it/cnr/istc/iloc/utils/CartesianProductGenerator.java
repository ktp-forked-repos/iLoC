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
package it.cnr.istc.iloc.utils;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class CartesianProductGenerator<T> implements Iterable<T[]> {

    private final T[][] elements;
    private final int[] productIndices;
    private final int size;

    @SuppressWarnings("unchecked")
    public CartesianProductGenerator(T[]... elements) {
        this.elements = elements;
        int c_size = 1;
        for (T[] c_elements : elements) {
            c_size *= c_elements.length;
        }
        this.productIndices = new int[elements.length];
        this.size = c_size;
    }

    public long getSize() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T[]> iterator() {
        return new Iterator<T[]>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T[] next() {
                int j = 1;
                for (int i = elements.length - 1; i >= 0; i--) {
                    productIndices[i] = (cursor / j) % elements[i].length;
                    j *= elements[i].length;
                }
                T[] ret = (T[]) Array.newInstance(elements[0].getClass().getComponentType(), elements.length);
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = elements[i][productIndices[i]];
                }
                cursor++;
                return ret;
            }
        };
    }
}
