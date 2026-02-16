package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.gmail.astroidchannel.LinguisticVariable.EPS;
import static org.junit.jupiter.api.Assertions.*;
class BellShapedFunctionTest {

    @ParameterizedTest
    @CsvSource({
            "0, 0.0121951219512",
            "1, 0.0588235294118",
            "2, 0.5",
            "3, 1",
            "3.8, 0.709421112372",
            "4.7, 0.106927855776",
            "6, 0.0121951219512",
    })
    void calculate(double input, double expected) {
        double a = 1, b = 2 , c = 3;
        MembershipFunction function = new BellShapedFunction(a, b, c);

        assertEquals(expected, function.calculate(input), EPS);
    }
}