package com.gmail.astroidchannel.membershipFunctions.functions;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.getBell;

public class BellShapedFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private TransitionCurve curve;

    public BellShapedFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.curve = getBell(a, b, c);
    }

    public BellShapedFunction(double a, double b, double c, TransitionCurve curve) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.curve = curve;
    }

    public BellShapedFunction(BellShapedFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.c = other.c;
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

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public TransitionCurve getCurve() {
        return curve;
    }

    public void setBell(TransitionCurve curve) {
        this.curve = curve;
    }

    @Override
    public double calculate(double x) {
        return curve.calculate(x);
    }

    @Override
    public double findHeight() {
        return 1;
    }

    @Override
    public Range<Double> findCarrier() {
        return Range.open(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Range<Double> findCore() {
        return Range.closed(c, c);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(Double.MIN_VALUE, c));
        boundaries.add(Range.open(c, Double.MAX_VALUE));

        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BellShapedFunction that = (BellShapedFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && Double.compare(c, that.c) == 0 && Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, curve);
    }

    @Override
    public String toString() {
        return "BellShapedFunction{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", curve=" + curve +
                '}';
    }
}
