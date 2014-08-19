/**
 * Copyright (c) 2013 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.tensorics.core.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.tensorics.core.fields.doubles.Structures.doubles;
import static org.tensorics.core.tensor.lang.TensorStructurals.from;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.tensorics.core.fields.doubles.Structures;
import org.tensorics.core.tensor.ImmutableTensor;
import org.tensorics.core.tensor.ImmutableTensor.Builder;
import org.tensorics.core.tensor.Position;
import org.tensorics.core.tensor.Tensor;
import org.tensorics.core.tensor.Tensor.Entry;
import org.tensorics.core.tensor.lang.TensorStructurals;

import com.google.common.collect.ImmutableSet;

/**
 * @author agorzaws
 */
public class TensorCalculationsTest {

    private static final int Z_COOR_NUMBER = 1;
    private static final int Y_COOR_NUMBER = 2;
    private static final int X_COOR_NUMBER = 512;

    private Tensor<Double> tensor1;
    private Tensor<Boolean> tensor1Flags;
    private Tensor<Double> tensor2;
    private Tensor<Double> tensor3Big;
    private TensoricSupport<Double> tensoricFieldUsage;

    @Before
    public void setUp() throws Exception {
        tensoricFieldUsage = Tensorics.using(Structures.doubles());
        tensor1 = prepareValues(1.0);
        tensor1Flags = prepareOnlyEvenValuesTrueFlag();
        tensor2 = prepareValues(2.5);
    }

    @Test
    public void averageOverOneDimensionOnValue1() {
        YCoordinate y = new YCoordinate(1);
        Tensor<Double> averagedOverX = TensorStructurals.from(tensor1).reduce(XCoordinate.class)
                .byAveragingIn(doubles());
        assertEquals(4.5, averagedOverX.get(Position.of(y)), 0.0);
    }

    @Test
    public void averageOverOneDimensionKeepingOther() {
        Tensor<Double> averageOverYCoordinte = TensorStructurals.from(tensor1).reduce(XCoordinate.class)
                .byAveragingIn(doubles());
        assertEquals(tensor1.shape().dimensionSet().size() - 1, averageOverYCoordinte.shape().dimensionSet().size());
        Set<YCoordinate> coordinateValues = TensorStructurals.from(averageOverYCoordinte).extractCoordinatesOfType(
                YCoordinate.class);
        assertEquals(10, coordinateValues.size());
    }

    @Test
    public void testMultiplyByNumber() {
        YCoordinate y = new YCoordinate(1);
        XCoordinate x = new XCoordinate(3);
        Tensor<Double> tensor = tensoricFieldUsage.calculate(tensor1).elementTimesV(-1.0);
        assertEquals(-3, tensor.get(x, y).doubleValue(), 0.0);
    }

    @Test
    public void testDivideByNumber() {
        YCoordinate y = new YCoordinate(1);
        XCoordinate x = new XCoordinate(3);
        Tensor<Double> tensor = tensoricFieldUsage.calculate(tensor1).elementDividedByV(2.0);
        assertEquals(1.5, tensor.get(x, y).doubleValue(), 0.0);
    }

    @Test
    public void testRMSCalculation() {
        YCoordinate y = new YCoordinate(1);
        double rms = TensorStructurals.from(tensor1).reduce(XCoordinate.class).byRmsIn(doubles()).get(y);

        assertEquals(5.33, rms, 0.01);
        double rms2 = TensorStructurals.from(tensor1).reduce(XCoordinate.class).byRmsIn(doubles()).get(Position.of(y));
        assertEquals(5.33, rms2, 0.01);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRMSCalculationTooManyCoordiantes() {
        YCoordinate y = new YCoordinate(1);
        XCoordinate x = new XCoordinate(2);
        double rms = TensorStructurals.from(tensor1).reduce(XCoordinate.class).byRmsIn(doubles()).get(x, y);

        assertEquals(5.33, rms, 0.01);
    }

    @Test
    public void testMeanCalculation() {
        YCoordinate y = new YCoordinate(1);
        double mean2 = TensorStructurals.from(TensorStructurals.from(tensor1).extractSliceAt(y))
                .reduce(XCoordinate.class).byAveragingIn(doubles()).get(Position.empty());
        assertEquals(4.5, mean2, 0.0);
    }

    @Test
    public void testAddition() {
        YCoordinate y = new YCoordinate(2);
        XCoordinate x = new XCoordinate(6);
        Tensor<Double> tensor = tensoricFieldUsage.calculate(tensor1).plus(tensor1);
        assertEquals(24.0, tensor.get(x, y).doubleValue(), 0.0);
    }

    @Test
    public void testSubtraction() {
        YCoordinate y = new YCoordinate(2);
        XCoordinate x = new XCoordinate(6);
        Tensor<Double> tensor = tensoricFieldUsage.calculate(tensor1).minus(tensor1);
        assertEquals(0.0, tensor.get(x, y).doubleValue(), 0.0);
    }

    @Test
    public void testFluentAddition() {
        YCoordinate y = new YCoordinate(2);
        XCoordinate x = new XCoordinate(6);
        Tensor<Double> tensor = tensoricFieldUsage.calculate(tensor1).plus(tensor1);
        assertEquals(24.0, tensor.get(x, y).doubleValue(), 0.0);
    }

    @Test
    public void testAdditionOf2elementsTo100WithResult2() {
        YCoordinate y = new YCoordinate(2);
        XCoordinate x = new XCoordinate(6);
        XCoordinate x2 = new XCoordinate(3);

        Builder<Double> builder = ImmutableTensor.builder(ImmutableSet.of(y.getClass(), x.getClass()));
        builder.at(Position.of(ImmutableSet.of(x, y))).put(13.2);
        builder.at(Position.of(ImmutableSet.of(x2, y))).put(-1.2);
        Tensor<Double> testTensor = builder.build();

        Tensor<Double> tensor = tensoricFieldUsage.calculate(tensor1).plus(testTensor);
        assertEquals(25.2, tensor.get(x, y).doubleValue(), 0.0);
    }

    @Test
    public void testAdditionOf2elementsTo2() {
        YCoordinate y = new YCoordinate(2);
        XCoordinate x = new XCoordinate(6);
        XCoordinate x2 = new XCoordinate(3);

        Builder<Double> builder = ImmutableTensor.builder(ImmutableSet.of(y.getClass(), x.getClass()));
        builder.at(Position.of(ImmutableSet.of(x, y))).put(13.2);
        builder.at(Position.of(ImmutableSet.of(x2, y))).put(-1.2);
        Tensor<Double> testTensor = builder.build();

        Builder<Double> builder2 = ImmutableTensor.builder(ImmutableSet.of(y.getClass(), x.getClass()));
        builder2.at(Position.of(ImmutableSet.of(x, y))).put(1.2);
        builder2.at(Position.of(ImmutableSet.of(x2, y))).put(1.2);
        Tensor<Double> testTensor2 = builder2.build();
        Tensor<Double> tensor = tensoricFieldUsage.calculate(testTensor).plus(testTensor2);
        assertEquals(14.4, tensor.get(x, y).doubleValue(), 0.001);
    }

    @Test
    public void testFlagApplience() {
        Tensor<Double> tensor = from(tensor1).extractWhereTrue(tensor1Flags);
        YCoordinate y = new YCoordinate(2);
        XCoordinate x = new XCoordinate(6);
        assertEquals(12.0, tensor.get(Position.of(ImmutableSet.of(x, y))).doubleValue(), 0.0);
        assertEquals(5, TensorStructurals.from(tensor).extractCoordinatesOfType(x.getClass()).size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlagApplienceFailure() {
        Tensor<Double> tensor = from(tensor1).extractWhereTrue(tensor1Flags);
        YCoordinate y = new YCoordinate(2);
        XCoordinate x = new XCoordinate(7);
        assertEquals(14.0, tensor.get(x, y).doubleValue(), 0.0);
    }

    @Test
    public void testAdditionFrom2elementsTo100WithWrongShapes() {
        XCoordinate x = new XCoordinate(6);
        XCoordinate x2 = new XCoordinate(3);
        Builder<Double> builder = ImmutableTensor.builder(ImmutableSet.<Class<? extends TestCoordinate>> of(x
                .getClass()));
        double x1Add = 13.2;
        double x2Add = -1.2;
        builder.at(Position.of(x)).put(x1Add);
        builder.at(Position.of(x2)).put(x2Add);
        Tensor<Double> testTensor = builder.build();
        Tensor<Double> result = tensoricFieldUsage.calculate(tensor1).plus(testTensor);

        /*
         * from broadcasting the y dimension of the test tensor to the values of tensor1 (10 values) together with the
         * two x-coordinates, we expect a tensor of sixe 20.
         */
        assertEquals(20, result.shape().size());

        checkCorrectlyAdded(x, x1Add, result);
        checkCorrectlyAdded(x2, x2Add, result);
    }

    private void checkCorrectlyAdded(XCoordinate x, double x1Add, Tensor<Double> result) {
        for (Entry<Double> entry : result.entrySet()) {
            Position position = entry.getPosition();
            if (x.equals(position.coordinateFor(XCoordinate.class))) {
                assertEquals(tensor1.get(position) + x1Add, entry.getValue(), 0.0000001);
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testReductionOnNonExistingCoordinate() {
        TensorStructurals.from(tensor1).extractSliceAt(new ZCoordinate(1));
    }

    @Test
    public void testOperationsOnBig512x2x1Tensor() {
        long totalDiff = calculateOnTensor(X_COOR_NUMBER, Y_COOR_NUMBER, Z_COOR_NUMBER, true);
        assertTrue(totalDiff < 600);
    }

    @Test
    public void testOperationsOnBig1024x2x2Tensor() {
        long totalDiff = calculateOnTensor(X_COOR_NUMBER * 2, Y_COOR_NUMBER, Z_COOR_NUMBER * 2, true);
        assertTrue(totalDiff < 4000);
    }

    @Test
    public void testSimpleTensoricsTask() {

        Tensor<Double> result = new TensoricTask<Double, Tensor<Double>>(EnvironmentImpl.of(doubles(),
                ManipulationOptions.defaultOptions(doubles()))) {

            @Override
            public Tensor<Double> run() {
                return calculate(tensor1).plus(tensor2);
            }

        }.run();
        assertEquals(100, result.shape().size());
    }

    private long calculateOnTensor(int xCoorNumber, int yCoorNumber, int zCoorNumber, boolean printLog) {
        long memoryBefore = getMemoryUsage();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:MM:ss.SSS");
        Date date = new Date();
        int nbOfElements = xCoorNumber * yCoorNumber * zCoorNumber;
        if (printLog) {
            System.out.println(" Creation of [" + xCoorNumber + "," + yCoorNumber + "," + zCoorNumber
                    + "] (total nb of elements: " + nbOfElements + ")\t" + sdf.format(date));
        }
        tensor3Big = prepareValuesForBig(xCoorNumber, yCoorNumber, zCoorNumber, 1.0);

        System.out.println("used memory :" + (getMemoryUsage() - memoryBefore));

        YCoordinate y = new YCoordinate(0);
        XCoordinate x = new XCoordinate(0);
        ZCoordinate z = new ZCoordinate(0);
        Date date2 = new Date();
        if (printLog) {
            System.out.println("done after (" + (date2.getTime() - date.getTime())
                    + "ms); \n Multiplying (base*(-1))\t" + sdf.format(date2));
        }
        Tensor<Double> inversedTensor = tensoricFieldUsage.negativeOf(tensor3Big);
        Date date3 = new Date();
        if (printLog) {
            System.out.println("done after (" + (date3.getTime() - date2.getTime())
                    + "ms); \n Adding (base*(-1) to base)\t" + sdf.format(date3));
        }
        Tensor<Double> tensorOut = tensoricFieldUsage.calculate(tensor3Big).plus(inversedTensor);

        Date date4 = new Date();
        if (printLog) {
            System.out.println("Done \t" + sdf.format(date4) + " (" + (date4.getTime() - date3.getTime()) + "ms)");
        }
        assertEquals(0.0, tensorOut.get(x, y, z).doubleValue(), 0.0);
        Date date5 = new Date();
        if (printLog) {
            System.out.println("get value at [0,0,0] \t" + sdf.format(date5) + " done after ("
                    + (date5.getTime() - date4.getTime()) + "ms)");
        }

        TensorStructurals.from(TensorStructurals.from(tensorOut).extractSliceAt(z, y)).reduce(XCoordinate.class)
                .byAveragingIn(doubles());

        Date date6 = new Date();
        if (printLog) {
            System.out.println("get mean on [x,0,0] \t" + sdf.format(date6) + " done after ("
                    + (date6.getTime() - date5.getTime()) + "ms)");
        }
        // Tensors.getRmsOf(tensorOut).slicingAt(ImmutableSet.of(y, z));
        TensorStructurals.from(tensorOut).reduce(XCoordinate.class).byRmsIn(doubles()).get(y, z);

        Date date7 = new Date();
        if (printLog) {
            System.out.println("get rms on [x,0,0] \t" + sdf.format(date7) + " done after ("
                    + (date7.getTime() - date6.getTime()) + "ms)");
        }
        long totalDiff = date7.getTime() - date2.getTime();
        if (printLog) {
            System.out.println("Total (since first calc)  done after (" + totalDiff + "ms)");
            System.out.println("used memory :" + (getMemoryUsage() - memoryBefore));
            System.out.println("=====================================");
        }
        return totalDiff;
    }

    public long getMemoryUsage() {
        for (int i = 0; i < 10; i++) {
            System.gc(); // NOSONAR benchmarking code
        }

        int kb = 1024;
        Runtime runtime = Runtime.getRuntime();

        // Print used memory
        long usedMemoryinKb = (runtime.totalMemory() - runtime.freeMemory()) / kb;
        return usedMemoryinKb;
    }

    @SuppressWarnings("boxing")
    private Tensor<Double> prepareValues(double factor) {
        ImmutableSet<Class<? extends TestCoordinate>> dimensions = ImmutableSet
                .of(XCoordinate.class, YCoordinate.class);
        Builder<Double> builder = ImmutableTensor.builder(dimensions);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                builder.at(Position.of(coordinatesFor(i, j))).put(valueFor(i, j, factor));
            }
        }
        return builder.build();
    }

    private Tensor<Boolean> prepareOnlyEvenValuesTrueFlag() {
        ImmutableSet<Class<? extends TestCoordinate>> dimensions = ImmutableSet
                .of(XCoordinate.class, YCoordinate.class);
        Builder<Boolean> builder = ImmutableTensor.builder(dimensions);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                builder.at(Position.of(coordinatesFor(i, j))).put(flagFor(i, j));
            }
        }
        return builder.build();
    }

    private Boolean flagFor(int i, int j) {
        if (i % 2 == 0 && j % 2 == 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private double valueFor(int i, int j, double factor) {
        return j * i * factor;
    }

    private Set<TestCoordinate> coordinatesFor(int i, int j) {
        Set<TestCoordinate> coordinates = new HashSet<>();
        coordinates.add(new XCoordinate(i));
        coordinates.add(new YCoordinate(j));
        return coordinates;
    }

    private Set<TestCoordinate> coordinatesFor(int i, int j, int k) {
        Set<TestCoordinate> coordinates = coordinatesFor(i, j);
        coordinates.add(new ZCoordinate(k));
        return coordinates;
    }

    @SuppressWarnings("boxing")
    private Tensor<Double> prepareValuesForBig(int Nx, int Ny, int Nz, double factor) {
        ImmutableSet<Class<? extends TestCoordinate>> dimensions = ImmutableSet.of(XCoordinate.class,
                YCoordinate.class, ZCoordinate.class);
        Builder<Double> builder = ImmutableTensor.builder(dimensions);
        for (int i = 0; i < Nx; i++) {
            for (int j = 0; j < Ny; j++) {
                for (int k = 0; k < Nz; k++) {
                    builder.at(Position.of(coordinatesFor(i, j, k))).put(valueForBig(i, j, k, factor));
                }
            }
        }
        return builder.build();
    }

    private double valueForBig(int i, int j, int k, double factor) {
        return j * i * k * factor;
    }

}
