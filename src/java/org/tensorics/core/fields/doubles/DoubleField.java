/**
 * Copyright (c) 2014 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.tensorics.core.fields.doubles;

import org.tensorics.core.math.operations.CommutativeAssociativeOperation;
import org.tensorics.core.math.operations.UnaryOperation;
import org.tensorics.core.math.structures.grouplike.AbelianGroup;
import org.tensorics.core.math.structures.ringlike.Field;

/**
 * Provides mathematical structures for double values (which form a field). Usually, users will not construct instances
 * of this class manually, but use the convenience methods from the {@link Structures} class. The reason is that
 * instances of this class are per se of limited use. They only become useful, if they are extended by additional
 * functionality.
 * 
 * @author kfuchsbe
 * @see Structures
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class DoubleField implements Field<Double> {

    private static final double ZERO = 0.0;
    private static final double ONE = 1.0;

    private final CommutativeAssociativeOperation<Double> additionOperation //
    = new CommutativeAssociativeOperation<Double>() {
        @Override
        public Double perform(Double left, Double right) {
            return left + right;
        }
    };

    private final CommutativeAssociativeOperation<Double> multiplicationOperation //
    = new CommutativeAssociativeOperation<Double>() {
        @Override
        public Double perform(Double left, Double right) {
            return left * right;
        }
    };

    private final UnaryOperation<Double> additiveInverseOperation = new UnaryOperation<Double>() {
        @Override
        public Double perform(Double value) {
            return -value;
        }
    };

    private final UnaryOperation<Double> multiplicativeInverseOperation = new UnaryOperation<Double>() {
        @Override
        public Double perform(Double value) {
            return 1 / value;
        }
    };

    private final AbelianGroup<Double> additionGroup = new AbelianGroup<Double>() {
        @Override
        public CommutativeAssociativeOperation<Double> operation() {
            return additionOperation;
        }

        @Override
        public Double identity() {
            return ZERO;
        }

        @Override
        public UnaryOperation<Double> inversion() {
            return additiveInverseOperation;
        }
    };

    private final AbelianGroup<Double> multiplicationGroup = new AbelianGroup<Double>() {

        @Override
        public CommutativeAssociativeOperation<Double> operation() {
            return multiplicationOperation;
        }

        @Override
        public Double identity() {
            return ONE;
        }

        @Override
        public UnaryOperation<Double> inversion() {
            return multiplicativeInverseOperation;
        }
    };

    /**
     * Package visible constructor to allow the structures class to instantiate the class. Use the lookup methods in the
     * {@link Structures} class to retrieve an instance of the field.
     */
    DoubleField() {
        /* Use Factory methods */
    }

    @Override
    public AbelianGroup<Double> additionStructure() {
        return additionGroup;
    }

    @Override
    public AbelianGroup<Double> multiplicationStructure() {
        return multiplicationGroup;
    }

}
