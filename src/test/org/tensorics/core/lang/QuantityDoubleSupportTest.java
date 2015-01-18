/**
 * Copyright (c) 2015 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.tensorics.core.lang;

import static javax.measure.unit.SI.METER;
import static javax.measure.unit.SI.MILLIMETER;
import static javax.measure.unit.SI.VOLT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.measure.converter.ConversionException;

import org.junit.Test;
import org.tensorics.core.quantity.ImmutableQuantifiedValue;
import org.tensorics.core.quantity.QuantifiedValue;
import org.tensorics.core.units.JScienceUnit;

public class QuantityDoubleSupportTest extends QuantityDoubleSupport {

    private static final QuantifiedValue<Double> ERRONOUS_IN_M = ImmutableQuantifiedValue
            .of(1.0, JScienceUnit.of(METER)).withError(0.1).withValidity(false);
    private final static QuantifiedValue<Double> ERRONOUS_IN_MM = ImmutableQuantifiedValue
            .of(1000.0, JScienceUnit.of(MILLIMETER)).withError(100.0).withValidity(false);

    @Test
    public void conversionWorksProperly() {
        QuantifiedValue<Double> converted = convert(valueOf(1.0, METER)).to(MILLIMETER);
        assertThat(converted, is(equalTo(valueOf(1000.0, MILLIMETER))));
    }

    @Test
    public void conversionWithErrorWorksProperly() {
        assertThat(convert(ERRONOUS_IN_M).to(MILLIMETER), is(equalTo(ERRONOUS_IN_MM)));
    }

    @Test(expected = ConversionException.class)
    public void incompatibleUnitsThrow() {
        convert(valueOf(1.0, METER)).to(VOLT);
    }
}
