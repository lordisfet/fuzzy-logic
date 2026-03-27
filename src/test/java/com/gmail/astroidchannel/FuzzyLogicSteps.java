package com.gmail.astroidchannel;

import com.gmail.astroidchannel.membershipFunctions.functions.TriangularFunction;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class FuzzyLogicSteps {

    private LinguisticVariable linguisticVariable;
    private Map<String, Double> fuzzifiedResult;

    @Given("an empty linguistic variable {string} with range {double} to {double}")
    public void an_empty_linguistic_variable_with_range(String name, double min, double max) {
        // Ініціалізація базової змінної з твоєї бібліотеки
        linguisticVariable = new LinguisticVariable(name, min, max);
    }

    @Given("it has a triangular term {string} with points {double}, {double}, {double}")
    public void it_has_a_triangular_term_with_points(String termName, double a, double b, double c) {
        // Додавання математичної функції належності
        linguisticVariable.addTerms(termName, new TriangularFunction(a, b, c));
    }

    @When("I fuzzify the crisp input {double}")
    public void i_fuzzify_the_crisp_input(double inputValue) {
        // Виклик методу фазифікації, який ми документували раніше
        fuzzifiedResult = linguisticVariable.fuzzify(inputValue);
    }

    @Then("the membership degree for {string} should be {double}")
    public void the_membership_degree_for_should_be(String termName, double expectedDegree) {
        // Отримання результату. Якщо термін відкинуто через EPS, вважаємо його 0.0
        Double actualDegree = fuzzifiedResult.getOrDefault(termName, 0.0);

        // Перевірка математичної точності з допустимою похибкою (дельта) 0.0001
        Assertions.assertEquals(expectedDegree, actualDegree, 0.0001,
                "Помилка математичного висновку: ступінь належності не збігається");
    }
}