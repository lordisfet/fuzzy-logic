package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.getCosine;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ZSShapedFunction implements MembershipFunction {
    private double a;
    private double b;
    private Shape shape;
    private TransitionCurve curve;

    public enum Shape {
        Z, S
    }

        public ZSShapedFunction(double a, double b, Shape shape) {
        this.a = a;
        this.b = b;
        this.shape = shape;
        this.curve = getCosine(a, b);
    }

    public ZSShapedFunction(double a, double b, Shape shape, TransitionCurve curve) {
        this.a = a;
        this.b = b;
        this.shape = shape;
        this.curve = curve;
    }

    public ZSShapedFunction(ZSShapedFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.shape = other.shape;
        this.curve = other.curve;
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

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public TransitionCurve getCurve() {
        return curve;
    }

    public void setCurve(TransitionCurve curve) {
        this.curve = curve;
    }

    @Override
    public double calculate(double x) {
        if (x <= a) {
            return shape.ordinal() == Shape.Z.ordinal() ? 1 : 0;
        }
        if (x >= b) {
            return shape.ordinal() == Shape.Z.ordinal() ? 0 : 1;
        }
        if (x > a && x < b) {
            double value = curve.calculate(x);
            return shape.ordinal() == Shape.Z.ordinal() ? value : MembershipFunction.invert0to1Value(value);
        }

        throw new IllegalArgumentException("x = " + x + " is not in conditions");
    }

    @Override
    public double findHeight() {
        return 1;
    }

    @Override
    public Range<Double> findCarrier() {
        return shape.ordinal() == Shape.Z.ordinal() ? Range.open(Double.MIN_VALUE, b) : Range.open(a, Double.MAX_VALUE);
    }

    @Override
    public Range<Double> findCore() {
        return shape.ordinal() == Shape.Z.ordinal() ? Range.closed(Double.MIN_VALUE, a) : Range.closed(b, Double.MAX_VALUE);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> result = new LinkedHashSet<>();
        result.add((Range.open(a, b)));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ZSShapedFunction that = (ZSShapedFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && shape == that.shape && Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, shape, curve);
    }

    @Override
    public String toString() {
        return "ZSShapedFunction{" +
                "a=" + a +
                ", b=" + b +
                ", shape=" + shape +
                ", curve=" + curve +
                '}';
    }
}
