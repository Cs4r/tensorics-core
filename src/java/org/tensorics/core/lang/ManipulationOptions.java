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

package org.tensorics.core.lang;

import org.tensorics.core.commons.options.ImmutableOptionRegistry;
import org.tensorics.core.commons.options.ManipulationOption;
import org.tensorics.core.commons.options.OptionRegistry;
import org.tensorics.core.math.ExtendedField;
import org.tensorics.core.quantity.options.JScienceQuantificationStrategy;
import org.tensorics.core.quantity.options.RequireBothValidStrategy;
import org.tensorics.core.quantity.options.UncorrelatedErrorPropagationStrategy;
import org.tensorics.core.tensor.options.BroadcastMissingDimensionsStrategy;
import org.tensorics.core.tensor.options.IntersectionShapingStrategy;
import org.tensorics.core.tensor.options.LeftContextPreservedStrategy;

import com.google.common.collect.ImmutableList;

/**
 * A class that provides static methods to deal with manipulations options and creating repositories for them.
 * 
 * @author agorzaws
 */
public final class ManipulationOptions {

    private ManipulationOptions() {
        /* Only static methods */
    }

    // tag::classdef[]
    /**
     * Creates a new instance of an Option registry, supporting the given field, which will contain the default options,
     * as there are:
     * <ul>
     * <li> {@link IntersectionShapingStrategy} for shaping strategy.</li>
     * <li> {@link BroadcastMissingDimensionsStrategy} for broadcasting strategy</li>
     * <li> {@link RequireBothValidStrategy}</li>
     * <li> {@link UncorrelatedErrorPropagationStrategy}</li>
     * <li> {@link JScienceQuantificationStrategy}</li>
     * <li> {@link LeftContextPreservedStrategy}</li>
     * </ul>
     * 
     * @param field the for which to create the option-instances
     * @return a new (immutable) option registry, containing the default options
     */
    @SuppressWarnings("deprecation")
    public static <T> OptionRegistry<ManipulationOption> defaultOptions(ExtendedField<T> field) {
        return ImmutableOptionRegistry.of(ImmutableList.of(//
                new IntersectionShapingStrategy(), //
                new BroadcastMissingDimensionsStrategy(), //
                new RequireBothValidStrategy(), //
                new UncorrelatedErrorPropagationStrategy<>(field), //
                new JScienceQuantificationStrategy<>(field.cheating()), //
                new LeftContextPreservedStrategy()));
    }
    // end::classdef[]
}
