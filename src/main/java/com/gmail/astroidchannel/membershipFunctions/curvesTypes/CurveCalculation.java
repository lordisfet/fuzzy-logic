package com.gmail.astroidchannel.membershipFunctions.curvesTypes;

import static java.lang.Math.*;

public class CurveCalculation {
    public static TransitionCurve getLinear(double a, double b) {
        return t -> (t - a) / (b - a);
    }
    public static TransitionCurve getCosine(double a, double b) {
        return t -> 1.0 / 2.0 + Math.cos(Math.PI * (t - a) / (b - a)) / 2.0;
    }
    public static TransitionCurve getSigmoid(double slope, double inflectionPoint) {
        return t -> 1.0 / (1.0 + Math.exp(-slope * (t - inflectionPoint)));
    }
    public static TransitionCurve getBell(double a, double b, double c) {
        return t -> 1.0 / (1.0 + Math.pow(Math.abs((t - c) / a), 2 * b));
    }
    public static TransitionCurve getGauss(double c, double s) {
        return t -> pow(E, -pow(t-c, 2)/(2 * pow(s, 2)));
    }
}
