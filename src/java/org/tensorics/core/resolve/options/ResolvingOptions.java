/**
 * Copyright (c) 2013 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.tensorics.core.resolve.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.tensorics.core.commons.options.ImmutableOptionRegistry;
import org.tensorics.core.commons.options.OptionRegistry;

import com.google.common.collect.ImmutableList;

/**
 * Utility class which provides factory methods to different processing options.
 * 
 * @author kfuchsbe
 */
public final class ResolvingOptions {

    /**
     * The list of default options.
     */
    private static final ImmutableList<ResolvingOption> DEFAULT_OPTIONS = ImmutableList.<ResolvingOption> of(
            new RethrowExceptionHandlingStrategy(), new TakeFirstResolverSelectionStrategy());

    private ResolvingOptions() {
        /* only static methods */
    }

    /**
     * Creates a option-registry which will contain the default options plus the ones given as parameters.
     * 
     * @param options all the options which should be put into the registry in addition to (or overriding) default
     *            options.
     * @return a new processing options registry, which can be used by the processor to retrieve the options.
     */
    public static OptionRegistry<ResolvingOption> createRegistryWithDefaultsExcept(ResolvingOption... options) {
        List<ResolvingOption> processingOptions = new ArrayList<>(defaultOptions());
        processingOptions.addAll(Arrays.asList(options));
        return ImmutableOptionRegistry.of(processingOptions);
    }

    public static ResolvingOption rethrowExceptions() {
        return new RethrowExceptionHandlingStrategy();
    }

    public static ResolvingOption handleExceptionsInFirstAncestor() {
        return new HandleWithFirstCapableAncestorStrategy();
    }

    public static Collection<ResolvingOption> defaultOptions() {
        return DEFAULT_OPTIONS;
    }

}
