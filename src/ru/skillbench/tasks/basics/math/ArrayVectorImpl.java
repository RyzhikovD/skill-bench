package ru.skillbench.tasks.basics.math;

import java.util.Arrays;

public class ArrayVectorImpl implements ArrayVector{
    private double[] elements;

    @Override
    public void set(double... elements) {
        this.elements = elements;
    }

    @Override
    public double[] get() {
        return elements;
    }

    @Override
    public ArrayVector clone() {
        ArrayVector arrayVector = new ArrayVectorImpl();
        arrayVector.set(elements.clone());
        return arrayVector;
    }

    @Override
    public int getSize() {
        return elements.length;
    }

    @Override
    public void set(int index, double value) {
        if(index >= 0) {
            if(index >= elements.length) {
                double[] tmp = new double[index + 1];
                System.arraycopy(elements, 0, tmp, 0, elements.length);
                elements = tmp;
            }
            elements[index] = value;
        }
    }

    @Override
    public double get(int index) throws ArrayIndexOutOfBoundsException {
        return elements[index];
    }

    @Override
    public double getMax() {
        double max = elements[0];
        for(int i = 1; i < elements.length; i++) {
            max = max > elements[i] ? max : elements[i];
        }
        return max;
    }

    @Override
    public double getMin() {
        double min = elements[0];
        for(int i = 1; i < elements.length; i++) {
            min = min < elements[i] ? min : elements[i];
        }
        return min;
    }

    @Override
    public void sortAscending() {
        Arrays.sort(elements);
    }

    @Override
    public void mult(double factor) {
        for(int i = 0; i < elements.length; i++) {
            elements[i] *= factor;
        }
    }

    @Override
    public ArrayVector sum(ArrayVector anotherVector) {
        for(int i = 0; i < elements.length; i++) {
            elements[i] += anotherVector.get()[i];
        }
        return this;
    }

    @Override
    public double scalarMult(ArrayVector anotherVector) {
        double result = 0;
        for(int i = 0; i < elements.length; i++) {
            result += elements[i] * anotherVector.get()[i];
        }
        return result;
    }

    @Override
    public double getNorm() {
        return Math.sqrt(this.scalarMult(this));
    }
}