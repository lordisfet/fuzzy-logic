package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation;
import com.google.common.collect.Range;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * If a>0 function will be S-shaped, if a<0 function will be z-shaped
 **/
public class SigmoidFunction implements MembershipFunction {
    private double a;
    private double b;
    private TransitionCurve curve;

    public SigmoidFunction(double a, double b) {
        //TODO: Am i need exception if a==b?
        this.a = a;
        this.b = b;
        this.curve = CurveCalculation.getSigmoid(a,b);
    }

    public SigmoidFunction(double a, double b, TransitionCurve curve) {
        this.a = a;
        this.b = b;
        this.curve = curve;
    }

    public SigmoidFunction(SigmoidFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.curve = CurveCalculation.getSigmoid(other.a, other.b);
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

    public TransitionCurve getCurve() {
        return curve;
    }

    public void setCurve(TransitionCurve curve) {
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
        return null;
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(Double.MIN_VALUE, Double.MAX_VALUE));

        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SigmoidFunction that = (SigmoidFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "SigmoidFunction{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
