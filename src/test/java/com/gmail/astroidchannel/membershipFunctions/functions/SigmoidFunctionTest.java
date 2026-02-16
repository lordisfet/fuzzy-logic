package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import org.junit.jupiter.api.Test;

import static com.gmail.astroidchannel.LinguisticVariable.EPS;
import static org.junit.jupiter.api.Assertions.*;

class SigmoidFunctionTest {

    @Test
    void calculate() {
        double a = 3, b = 6;
        MembershipFunction function = new SigmoidFunction(3, 6);

        assertEquals(0.000123394575986, function.calculate(a), EPS);
        assertEquals(0.0109869426306, function.calculate((a + b) / 2), EPS);
        assertEquals(0.973403006423, function.calculate(1.2 * b), EPS);
        assertEquals(1. / 2, function.calculate(b), EPS);
    }
}