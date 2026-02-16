package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import org.junit.jupiter.api.Test;

import static com.gmail.astroidchannel.LinguisticVariable.EPS;
import static org.junit.jupiter.api.Assertions.*;
class TriangularFunctionTest {

    @Test
    void calculate() {
        double a = 2, b = 4, c = 7;
        MembershipFunction function = new TriangularFunction(a, b, c);

        assertEquals(0, function.calculate(1), EPS);
        assertEquals(0, function.calculate(a), EPS);
        assertEquals(0.5, function.calculate(3), EPS);
        assertEquals(1, function.calculate(b), EPS);
        assertEquals(1./3, function.calculate(6), EPS);
        assertEquals(0, function.calculate(c), EPS);
        assertEquals(0, function.calculate(8), EPS);
    }
}