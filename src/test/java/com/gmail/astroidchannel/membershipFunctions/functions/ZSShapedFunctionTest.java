package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation;
import org.junit.jupiter.api.Test;

import static com.gmail.astroidchannel.LinguisticVariable.EPS;
import static org.junit.jupiter.api.Assertions.*;

class ZSShapedFunctionTest {

    @Test
    void calculate() {
        double a = 3, b = 6;
        MembershipFunction function1 = new ZSShapedFunction(a, b, ZSShapedFunction.Shape.Z);

        assertEquals(1, function1.calculate(a - 1), EPS);
        assertEquals(1, function1.calculate(a), EPS);
        assertEquals(1. / 2, function1.calculate((a + b) / 2), EPS);
        assertEquals(0.20611, function1.calculate(0.85 * b), EPS);
        assertEquals(0, function1.calculate(b), EPS);
        assertEquals(0, function1.calculate(b + 1), EPS);

        MembershipFunction function2 = new ZSShapedFunction(a, b, ZSShapedFunction.Shape.S);

        assertEquals(0, function2.calculate(a - 1), EPS);
        assertEquals(0, function2.calculate(a), EPS);
        assertEquals(1. / 2, function2.calculate((a + b) / 2), EPS);
        assertEquals(1 - 0.20611, function2.calculate(0.85 * b), EPS);
        assertEquals(1, function2.calculate(b), EPS);
        assertEquals(1, function2.calculate(b + 1), EPS);

        a = 2; b = 4;
        double c = 7;
        MembershipFunction function3 = new ZSShapedFunction(a, b, ZSShapedFunction.Shape.Z, CurveCalculation.getLinear(a, b));

        assertEquals(1, function3.calculate(1), EPS);
        assertEquals(1, function3.calculate(a), EPS);
        assertEquals(0.5, function3.calculate(3), EPS);
        assertEquals(0, function3.calculate(b), EPS);
        assertEquals(0, function3.calculate(c), EPS);

    }
}