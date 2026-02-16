package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.getLinear;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class TrapezoidalFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private double d;
    private TransitionCurve leftPart;
    private TransitionCurve rightPart;

    public TrapezoidalFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.leftPart = getLinear(a, b);
        this.rightPart = getLinear(c, d);
    }

    public TrapezoidalFunction(double a, double b, double c, double d, TransitionCurve leftPart, TransitionCurve rightPart) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    public TrapezoidalFunction(TrapezoidalFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.c = other.c;
        this.d = other.d;
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

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
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
        if (Double.compare(x, a) <= 0) {
            return 0;
        }
        if (Double.compare(x, d) >= 0) {
            return 0;
        }
        if (Double.compare(x, b) >= 0 && Double.compare(x, c) <= 0) {
            return 1;
        }
        if (Double.compare(x, a) > 0 && Double.compare(x, b) <= 0) {
            return leftPart.calculate(x);
        }
        if (Double.compare(x, c) > 0 && Double.compare(x, d) <= 0) {
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
        return Range.open(a, d);
    }

    @Override
    public Range<Double> findCore() {
        return Range.closed(b, c);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(a, b));
        boundaries.add(Range.open(b, d));

        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrapezoidalFunction that = (TrapezoidalFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && Double.compare(c, that.c) == 0 && Double.compare(d, that.d) == 0 && Objects.equals(leftPart, that.leftPart) && Objects.equals(rightPart, that.rightPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d, leftPart, rightPart);
    }

    @Override
    public String toString() {
        return "TrapezoidalFunction{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", leftPart=" + leftPart +
                ", rightPart=" + rightPart +
                '}';
    }
}
