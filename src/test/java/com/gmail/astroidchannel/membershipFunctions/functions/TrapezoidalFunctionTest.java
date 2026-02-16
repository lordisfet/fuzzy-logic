package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import org.junit.jupiter.api.Test;

import static com.gmail.astroidchannel.LinguisticVariable.EPS;
import static org.junit.jupiter.api.Assertions.*;

class TrapezoidalFunctionTest {

    @Test
    void calculate() {
        double a = 1, b = 3, c = 5, d = 8;
        MembershipFunction function = new TrapezoidalFunction(a, b, c, d);

        assertEquals(0, function.calculate(-10), EPS);
        assertEquals(0, function.calculate(0), EPS);
        assertEquals(0, function.calculate(a), EPS);
        assertEquals(1. / 2, function.calculate(2), EPS);
        assertEquals(1, function.calculate(b), EPS);
        assertEquals(1, function.calculate(4), EPS);
        assertEquals(1, function.calculate(c), EPS);
        assertEquals(2. / 3, function.calculate(6), EPS);
        assertEquals(0, function.calculate(d), EPS);
    }
}