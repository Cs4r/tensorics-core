// @formatter:off
 /*******************************************************************************
 *
 * This file is part of tensorics.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

package org.tensorics.core.tensor.lang;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.tensorics.core.tensor.Context;
import org.tensorics.core.tensor.ImmutableTensor;
import org.tensorics.core.tensor.ImmutableTensor.Builder;
import org.tensorics.core.tensor.Position;
import org.tensorics.core.tensor.Tensor;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * Structural manipulations of tensors (which do not require any knowledge about the mathematical behaviour of the
 * elements).
 * 
 * @author kfuchsbe
 */
public final class TensorStructurals {

    /**
     * Private constructor to avoid instantiation
     */
    private TensorStructurals() {
        /* only static methods */
    }

    public static <V> OngoingTensorManipulation<V> from(Tensor<V> tensor) {
        return new OngoingTensorManipulation<>(tensor);
    }

    /**
     * Merges the given set of the Tensors based on information in their context and dimensions. This operation is only
     * possible, if the following preconditions are fulfilled:
     * <ul>
     * <li>The contexts of all the tensors have THE SAME dimensions</li> AND
     * <li>The dimensions of all the tensors are THE SAME (first found tensor dimension is taken as an reference)</li>
     * </ul
     * 
     * @param tensors to be merged.
     * @return a merged tensor.
     * @throws IllegalArgumentException if zero or one tensor is put to be merged OR if tensors dimensionality and their
     *             context positions dimensionality is not equal OR tensor context is empty.
     */
    public static <E> Tensor<E> merge(Iterable<Tensor<E>> tensors) {
        if (Iterables.isEmpty(tensors) || Iterables.size(tensors) == 1) {
            throw new IllegalArgumentException("Cannot merge empty or one element list of tensors!");
        }
        Tensor<E> firstTensor = tensors.iterator().next();
        Set<Class<?>> refDimensionSet = firstTensor.shape().dimensionSet();
        Position refContextPosition = firstTensor.context().getPosition();
        Set<Class<?>> outcomeDimensionSet = new HashSet<>(refDimensionSet);
        Set<Class<?>> dimensionSet = refContextPosition.dimensionSet();
        if (dimensionSet.isEmpty()) {
            throw new IllegalArgumentException("Cannot merge tensors with empty context!");
        }
        outcomeDimensionSet.addAll(dimensionSet);
        Builder<E> tensorBuilder = ImmutableTensor.builder(outcomeDimensionSet);

        for (Tensor<E> oneTensor : tensors) {
            if (TensorStructurals.isValidInTermsOfDimensions(oneTensor, refDimensionSet,
                    refContextPosition.dimensionSet())) {
                tensorBuilder.putAllAt(oneTensor, oneTensor.context().getPosition());
            } else {
                throw new IllegalArgumentException(
                        "One of the tensors in the provided list does not fit the others on dimensions."
                                + " Cannot continiue merging" + oneTensor);
            }
        }
        return tensorBuilder.build();
    }

    /**
     * Checks if the provided tensor has: the same dimension as the reference dimensions, equal number of them, as well
     * as equal number of Context Position dimension and referenceContext Position and their dimensionality.
     * 
     * @param tensor the tensor to check
     * @param refDimensions the dimensions that the tensor should have
     * @param refContextDimensions the context position the context dimensions, to which the tensor shall be compatible
     * @return {@code true} if the conditions are fulfilled, {@code false} if not.
     */
    private static <E> boolean isValidInTermsOfDimensions(Tensor<E> tensor, Set<Class<?>> refDimensions,
            Set<Class<?>> refContextDimensions) {
        Set<Class<?>> tensorDimensionSet = tensor.shape().dimensionSet();
        Set<Class<?>> contextPositionDimensionSet = tensor.context().getPosition().dimensionSet();
        return (tensorDimensionSet.equals(refDimensions) && contextPositionDimensionSet.equals(refContextDimensions));
    }

    public static <S> OngoingFlattening<S> flatten(Tensor<S> tensor) {
        return new OngoingFlattening<S>(tensor);
    }

    public static final <S> OngoingCompletion<S> complete(Tensor<S> tensor) {
        return new OngoingCompletion<S>(tensor);
    }

    public static <S, T> Tensor<T> transformEntries(Tensor<S> tensor, Function<Entry<Position, S>, T> function) {
        Builder<T> builder = ImmutableTensor.builder(tensor.shape().dimensionSet());
        for (Entry<Position, S> entry : tensor.asMap().entrySet()) {
            builder.putAt(function.apply(entry), entry.getKey());
        }
        return builder.build();
    }

    public static <S, T> Tensor<T> transformScalars(Tensor<S> tensor, Function<S, T> function) {
        Builder<T> builder = ImmutableTensor.builder(tensor.shape().dimensionSet());
        builder.setTensorContext(tensor.context());
        for (Entry<Position, S> entry : tensor.asMap().entrySet()) {
            builder.putAt(function.apply(entry.getValue()), entry.getKey());
        }
        return builder.build();
    }

    public static final <S> Tensor<S> stripContext(Tensor<S> tensor) {
        Builder<S> builder = ImmutableTensor.builder(tensor.shape().dimensionSet());
        builder.putAll(tensor);
        return builder.build();
    }

    public static final <S> Tensor<S> setContext(Tensor<S> tensor, Context context) {
        Builder<S> builder = ImmutableTensor.builder(tensor.shape().dimensionSet());
        builder.setTensorContext(context);
        builder.putAll(tensor);
        return builder.build();
    }

    public static final <S> OngoingTensorFiltering<S> filter(Tensor<S> tensor) {
        return new OngoingTensorFiltering<>(tensor);
    }

}
