package ru.skillbench.tasks.basics.math;

import java.util.Arrays;

public class ComplexNumberImpl implements ComplexNumber {
    private double re;
    private double im;

    public ComplexNumberImpl(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public ComplexNumberImpl() {}

    @Override
    public double getRe() {
        return re;
    }

    @Override
    public double getIm() {
        return im;
    }

    @Override
    public boolean isReal() {
        return im==0;
    }

    @Override
    public void set(double re, double im) {
        this.re = re;
        this.im = im;
    }

    @Override
    public void set(String value) throws NumberFormatException {
        int plus = value.indexOf("+");
        if(plus == 0) {
            set(value.substring(1));
        } else {
            int sign = plus == -1 ? value.lastIndexOf("-") : plus;
            int i = value.lastIndexOf("i");
            if(i == value.length()-1) {
                if(sign != -1 ) {
                    im = sign+1 != i ? Double.parseDouble(value.substring(sign, i)) : Double.parseDouble(value.charAt(sign)+"1");
                    re = value.substring(0, sign).length() != 0 ? Double.parseDouble(value.substring(0, sign)) : 0;
                } else {
                    im = sign+1 != i ? Double.parseDouble(value.substring(0, i)) : 1;
                    re = 0;
                }
            } else {
                im = 0;
                re = Double.parseDouble(value);
            }
        }
    }

    @Override
    public ComplexNumber copy() {
        return new ComplexNumberImpl(re, im);
    }

    @Override
    public ComplexNumber clone() throws CloneNotSupportedException {
        return (ComplexNumber) super.clone();
    }

    public String toString() {
        return (re != 0 ? re : "") + (im != 0 ? ( im > 0 && re != 0 ? "+" + im : im) + "i" : "");
    }

    public boolean equals(Object other) {
        if(other instanceof ComplexNumber){
            return re==((ComplexNumber) other).getRe() && im == ((ComplexNumber) other).getIm();
        }
        return false;
    }

    @Override
    public int compareTo(ComplexNumber other) {
        if(this.equals(other)){
            return 0;
        }
        return (re*re + im*im) > (Math.pow(other.getRe(), 2) + Math.pow(other.getIm(), 2)) ? 1 : -1;
    }

    @Override
    public void sort(ComplexNumber[] array) {
        Arrays.sort(array);
    }

    @Override
    public ComplexNumber negate() {
        re = -re;
        im = -im;
        return this;
    }

    @Override
    public ComplexNumber add(ComplexNumber arg2) {
        re += arg2.getRe();
        im += arg2.getIm();
        return this;
    }

    @Override
    public ComplexNumber multiply(ComplexNumber arg2) {
        set(re * arg2.getRe() - im * arg2.getIm(), im * arg2.getRe() + re * arg2.getIm());
        return this;
    }
}