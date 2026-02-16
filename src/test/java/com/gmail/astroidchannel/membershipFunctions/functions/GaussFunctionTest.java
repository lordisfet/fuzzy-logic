package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.gmail.astroidchannel.LinguisticVariable.EPS;
import static org.junit.jupiter.api.Assertions.*;

class GaussFunctionTest {
    @ParameterizedTest
    @CsvSource({
            "0, 0.135335283237",
            "1, 0.324652467358",
            "2, 0.606530659713",
            "3, 0.882496902585",
            "4, 1",
            "5, 0.882496902585",
            "6, 0.606530659713",
            "7, 0.324652467358",
            "8, 0.135335283237",
            "9, 0.0439369336234",
            "10, 0.0111089965382",
    })
    void calculate(double input, double expected) {
        double c = 4, s = 2;
        MembershipFunction function = new GaussFunction(c, s);

        assertEquals(expected, function.calculate(input), EPS);
    }
}