/**
 * Copyright (c) 2013 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.tensorics.incubate.function;

import javax.measure.quantity.Quantity;

import org.jscience.physics.amount.Amount;
import org.tensorics.core.tensor.Tensor;

import com.google.common.base.Preconditions;

/**
 * @author agorzaws
 * @param <QX> type of arguments
 * @param <QY> type of values
 */
public final class ConstantFunction<QX extends Quantity, QY extends Quantity> implements
        AnalyticalFunction<Amount<QX>, Amount<QY>> {

    private final Amount<QY> constant;

    private ConstantFunction(Amount<QY> constant) {
        this.constant = constant;
        Preconditions.checkArgument(constant != null, "Argument '" + "constant" + "' must not be null!");
    }

    @Override
    public Amount<QY> getY(Amount<QX> xValue) {
        return constant;
    }

    @Override
    public String toText() {
        return "[y = A]";
    }

    public Tensor<Amount<QY>> tensor() {
        throw new UnsupportedOperationException("Cannot get a discrete tensor from analytical function");
    }

    public Amount<QY> getConstant() {
        return constant;
    }

    @SuppressWarnings("PMD.ShortMethodName")
    public static <QX extends Quantity, QY extends Quantity> ConstantFunction<QX, QY> of(Amount<QY> amount) {
        return new ConstantFunction<QX, QY>(amount);
    }

}
