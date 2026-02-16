package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TransferQueue;

public class GaussFunction implements MembershipFunction {
    private double c;
    private double sigma;
    private TransitionCurve curve;

    public GaussFunction(double c, double sigma) {
        this.c = c;
        this.sigma = sigma;
        this.curve = CurveCalculation.getGauss(c, sigma);
    }

    public GaussFunction(double c, double sigma, TransitionCurve curve) {
        this.c = c;
        this.sigma = sigma;
        this.curve = curve;
    }

    public GaussFunction(GaussFunction other) {
        this.c = other.c;
        this.sigma = other.sigma;
        this.curve = other.curve;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
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
        return null;
    }

    @Override
    public Range<Double> findCore() {
        return null;
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        return Set.of();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GaussFunction that = (GaussFunction) o;
        return Double.compare(c, that.c) == 0 && Double.compare(sigma, that.sigma) == 0 && Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, sigma, curve);
    }

    @Override
    public String toString() {
        return "GaussFunction{" +
                "c=" + c +
                ", sigma=" + sigma +
                ", curve=" + curve +
                '}';
    }
}
