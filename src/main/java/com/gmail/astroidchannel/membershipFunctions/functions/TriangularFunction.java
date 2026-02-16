package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.*;      
import com.google.common.collect.Range;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class TriangularFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private TransitionCurve leftPart;
    private TransitionCurve rightPart;

//    private double height;
//    private Range<Double> carrier;
//    private Range<Double> core;
//    private Set<Range<Double>> spectrum;
//    private Shape shape; // Опуклість

    public TriangularFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.leftPart = getLinear(a, b);
        this.rightPart = getLinear(b, c);
    }

    public TriangularFunction(double a, double b, double c, TransitionCurve leftPart, TransitionCurve rightPart) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    public TriangularFunction(TriangularFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.c = other.c;
        this.leftPart = other.leftPart;
        this.rightPart = other.rightPart;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public TransitionCurve getLeftPart() {
        return leftPart;
    }

    public void setLeftPart(TransitionCurve leftPart) {
        this.leftPart = leftPart;
    }

    public TransitionCurve getRightPart() {
        return rightPart;
    }

    public void setRightPart(TransitionCurve rightPart) {
        this.rightPart = rightPart;
    }

    @Override
    public double calculate(double x) {
        if (x == b) {
            return 1;
        }
        if (Double.compare(x, a) <= 0) {
            return 0;
        }
        if (Double.compare(x, c) >= 0) {
            return 0;
        }
        if (Double.compare(x, a) > 0 && Double.compare(x, b) <= 0) {
            return leftPart.calculate(x);
        }
        if (Double.compare(x, b) > 0 && Double.compare(x, c) <= 0) {
            return MembershipFunction.invert0to1Value(rightPart.calculate(x));
        }

        throw new IllegalArgumentException("x = " + x + " is not in conditions");
    }

    @Override
    public double findHeight() {
        return calculate(b);
    }

    @Override
    public Range<Double> findCarrier() {
        return Range.open(a, c);
    }

    @Override
    public Range<Double> findCore() {
        return Range.closed(b, b);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(a, b));
        boundaries.add(Range.open(b, c));

        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TriangularFunction that = (TriangularFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && Double.compare(c, that.c) == 0 && Objects.equals(leftPart, that.leftPart) && Objects.equals(rightPart, that.rightPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, leftPart, rightPart);
    }

    @Override
    public String toString() {
        return "TriangularFunction{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", leftPart=" + leftPart +
                ", rightPart=" + rightPart +
                '}';
    }
}
