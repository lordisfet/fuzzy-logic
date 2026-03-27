package com.gmail.astroidchannel;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LinguisticVariable {
    public static final double EPS = 0.00001;
    private String name;
    private double
            leftBorder;
    private double rightBorder;
    private Map<String, MembershipFunction> terms = new HashMap<>();

    public LinguisticVariable(String name, double leftBorder, double rightBorder) {
        this.name = name;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    public LinguisticVariable(String name, double leftBorder, double rightBorder, Map<String, MembershipFunction> terms) {
        this.name = name;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.terms = new HashMap<>(terms);}

    public LinguisticVariable(LinguisticVariable other) {
        this.name = other.name;
        this.leftBorder = other.leftBorder;
        this.rightBorder = other.rightBorder;this.terms = new HashMap<>(other.terms);
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }

    public Map<String, MembershipFunction> getTerms() {
        return new HashMap<>(terms);
    }

    public void setTerms(Map<String, MembershipFunction> terms) {
        this.terms = new HashMap<>(terms);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LinguisticVariable that = (LinguisticVariable) o;
        return Double.compare(leftBorder, that.leftBorder) == 0 && Double.compare(rightBorder, that.rightBorder) == 0 && Objects.equals(name, that.name) && Objects.equals(terms, that.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, leftBorder, rightBorder, terms);
    }

    @Override
    public String toString() {
        return "LinguisticVariable{" +
                "name='" + name + '\'' +
                ", leftBorder=" + leftBorder +
                ", rightBorder=" + rightBorder +
                ", terms=" + terms +
                '}';
    }

    public void addTerms(String termName, MembershipFunction membershipFunction) {
        terms.put(termName, membershipFunction);
    }

    public Map<String, Double> fuzzify(double value) {
        Map<String, Double> result = new HashMap<>();

        for (Map.Entry<String, MembershipFunction> entry : terms.entrySet()) {
            double degree = entry.getValue().calculate(value);

            if (degree > EPS) {
                result.put(entry.getKey(), degree);
            }
        }

        return result;
    }
}
