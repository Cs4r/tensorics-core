/**
 * Copyright (c) 2014 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.tensorics.core.quantity;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * An immutable object that guarantees to have a value and an error.
 * 
 * @author kfuchsbe
 * @param <V> the type of the value and the error
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public final class ImmutableErronousValue<V> implements ErronousValue<V> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final V value;

    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Optional<V> error;

    private ImmutableErronousValue(V value, Optional<V> error) {
        Preconditions.checkArgument(value != null, "Argument '" + "value" + "' must not be null!");
        Preconditions.checkArgument(error != null, "Argument '" + "error" + "' must not be null!");
        this.value = value;
        this.error = error;
    }

    public static <V> ImmutableErronousValue<V> ofValue(V value) {
        return new ImmutableErronousValue<V>(value, Optional.<V> absent());
    }

    public static <V> ImmutableErronousValue<V> ofValueAndError(V value, V error) {
        return new ImmutableErronousValue<V>(value, Optional.of(error));
    }

    public static <V> ImmutableErronousValue<V> ofValueAndError(V value, Optional<V> error) {
        return new ImmutableErronousValue<V>(value, error);
    }

    @Override
    public V value() {
        return this.value;
    }

    @Override
    public Optional<V> error() {
        return this.error;
    }

    @Override
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImmutableErronousValue<?> other = (ImmutableErronousValue<?>) obj;
        if (error == null) {
            if (other.error != null) {
                return false;
            }
        } else if (!error.equals(other.error)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return value + "±" + error;
    }

}
