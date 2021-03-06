package org.tensorics.core.examples.scripting;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.tensorics.core.expressions.CreationOperationExpression;
import org.tensorics.core.fields.doubles.Structures;
import org.tensorics.core.lang.DoubleScript;
import org.tensorics.core.lang.TensoricExpressionSupport;
import org.tensorics.core.lang.TensoricScript;
import org.tensorics.core.math.operations.CreationOperation;
import org.tensorics.core.resolve.engine.ResolvingEngine;
import org.tensorics.core.resolve.engine.ResolvingEngines;
import org.tensorics.core.resolve.options.ResolvingOptions;
import org.tensorics.core.tensor.ImmutableTensor;
import org.tensorics.core.tensor.ImmutableTensor.Builder;
import org.tensorics.core.tensor.Tensor;
import org.tensorics.core.tree.domain.Expression;

import com.google.common.collect.ImmutableList;

public class ScriptingExample {

	private double someFactorForFakingChangingEnvironment = 1.0;
	private final Expression<Iterable<Double>> someFakeIterable = fakeSignalByCreationCallback();
	private final Expression<Tensor<Double>> someFakeTensorToResolve = fakeTensorByCreationCallback();

	private final DoubleScript<Double> script = new DoubleScript<Double>() {

		@Override
		public Expression<Double> describe() {
			Expression<Double> average = averageOf(someFakeIterable);
			Expression<Double> rms = rmsOf(someFakeIterable);
			Expression<Double> size = sizeOf(someFakeIterable);
			Expression<Double> someNonesenseValue = calculate(average).plus(rms);
			Expression<Double> anotherNonsense = calculate(someNonesenseValue).times(size);
			return calculate(anotherNonsense).minus(2.0);
		}

	};

	private ResolvingEngine engine = ResolvingEngines.defaultEngine();

	@Test
	public void resolveScriptWithDifferentConditions() {
		someFactorForFakingChangingEnvironment = 1.0;
		Double result1 = engine.resolve(script);
		System.out.println(result1);

		someFactorForFakingChangingEnvironment = 2.0;
		Double result2 = engine.resolve(script);
		System.out.println(result2);

		someFactorForFakingChangingEnvironment = 1.0;
		Expression<Double> expression = script.getInternalExpression();
		Double result3 = engine.resolve(expression);
		System.out.println(result3);
	}

	private final Expression<Iterable<Double>> fakeSignalByCreationCallback() {
		/*
		 * A creation operation expression most probably maps to an akka source
		 * (which again might correspond more or leass to a StreamId in the new
		 * streaming layer)
		 */
		return new CreationOperationExpression<>(new CreationOperation<Iterable<Double>>() {
			@Override
			public Iterable<Double> perform() {
				/*
				 * In reality, one could e.g. look up a real signal here, or
				 * write some dedicated expression instead of the
				 * CreationOperation.
				 */
				return ImmutableList.of(1.0 * someFactorForFakingChangingEnvironment,
						2.0 * someFactorForFakingChangingEnvironment, 3.0 * someFactorForFakingChangingEnvironment);
			}
		});
	}

	private static final Expression<Tensor<Double>> fakeTensorByCreationCallback() {
		return new CreationOperationExpression<>(new CreationOperation<Tensor<Double>>() {
			@Override
			public Tensor<Double> perform() {
				Builder<Double> builder = ImmutableTensor.builder(String.class, Integer.class);
				builder.putAt(1.0, "A", 1);
				builder.putAt(2.0, "A", 2);
				builder.putAt(3.0, "B", 2);
				return builder.build();
			}
		});
	}

}
