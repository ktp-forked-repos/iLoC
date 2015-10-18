package it.cnr.istc.iloc.utils;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 *
 * @param <T> The type of elements that the combinations are made from.
 *
 * @author Riccardo De Benedictis
 */
public class CombinationGenerator<T> implements Iterable<T[]> {

    private final T[] elements;
    private final int[] combinationIndices;
    private final int size;

    @SuppressWarnings("unchecked")
    public CombinationGenerator(int length, T... elements) {
        this.elements = elements;
        this.combinationIndices = new int[length];
        this.size = MathUtils.combinations(elements.length, length);
    }

    public long getSize() {
        return size;
    }

    @Override
    public Iterator<T[]> iterator() {
        return new Iterator<T[]>() {
            private int cursor = 0;

            {
                for (int i = 0; i < combinationIndices.length; i++) {
                    combinationIndices[i] = i;
                }
            }

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            /**
             * Algorithm from Kenneth H. Rosen, Discrete Mathematics and Its
             * Applications, 2nd edition (NY: McGraw-Hill, 1991), p. 286.
             */
            @Override
            public T[] next() {
                @SuppressWarnings("unchecked")
                T[] ret = (T[]) Array.newInstance(elements.getClass().getComponentType(), combinationIndices.length);
                for (int j = 0; j < combinationIndices.length; j++) {
                    ret[j] = elements[combinationIndices[j]];
                }
                if (++cursor < size) {
                    int i = combinationIndices.length - 1;
                    while (combinationIndices[i] == elements.length - combinationIndices.length + i) {
                        i--;
                    }
                    combinationIndices[i]++;
                    for (int j = i + 1; j < combinationIndices.length; j++) {
                        combinationIndices[j] = combinationIndices[i] + j - i;
                    }
                }
                return ret;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
