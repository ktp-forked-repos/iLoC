/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.smt.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IModel {

    /**
     * Evaluates the given boolean variable according to the current model.
     *
     * @param var the boolean variable of which the current domain is requested.
     * @return the current domain of the given boolean variable or {@code null}
     * if it is not possible to compute the current domain for the given boolean
     * variable.
     */
    public Boolean evaluateBoolean(ISMTBool var);

    /**
     * Evaluates the given integer variable according to the current model.
     *
     * @param var the integer variable of which the current domain is requested.
     * @return the current domain of the given integer variable or {@code null}
     * if it is not possible to compute the current domain for the given integer
     * variable.
     */
    public BigInteger evaluateInt(ISMTInt var);

    /**
     * Evaluates the given real variable according to the current model.
     *
     * @param var the real variable of which the current domain is requested.
     * @return the current domain of the given real variable or {@code null} if
     * it is not possible to compute the current domain for the given real
     * variable.
     */
    public BigDecimal evaluateReal(ISMTReal var);
}
