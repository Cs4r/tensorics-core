/**
 * Copyright (c) 2013 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.tensorics.core.scalar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.tensorics.core.fields.doubles.Structures;
import org.tensorics.core.math.ExtendedField;
import org.tensorics.core.scalar.lang.ScalarSupport;

public class ScalarSupportTest {

    private ExtendedField<Double> field = Structures.doubles();

    @Test
    public void testPlus() {
        assertEquals(1.5, scalarSupport().calculate(1.0).plus(0.5), 1.e-6);
    }

    private final ScalarSupport<Double> scalarSupport() {
        return new ScalarSupport<>(field);
    }

    @Test
    public void testMinus() {
        assertEquals(1.7, scalarSupport().calculate(2.0).minus(0.3), 1.e-6);
    }

    @Test
    public void testTimes() {
        assertEquals(4.0, scalarSupport().calculate(2.0).times(2.0), 1.e-6);
    }

    @Test
    public void testDivide() {
        assertEquals(1.0, scalarSupport().calculate(4.0).dividedBy(4.0), 1.e-6);
    }

    @Test
    public void testSquereAndSquereRoot() {
        assertEquals(4.0, scalarSupport().calculate(2.0).toThePowerOf(2.0), 1.e-6);
        assertEquals(1.44, scalarSupport().calculate(1.2).toThePowerOf(2.0), 1.e-6);
        assertEquals(11.0, scalarSupport().calculate(121.0).root(2.0), 1.e-6);
    }

    @Test
    public void testDivideByZero() {
        assertEquals(Double.POSITIVE_INFINITY, scalarSupport().calculate(4.0).dividedBy(0.0), 1.e-6);
    }

}
