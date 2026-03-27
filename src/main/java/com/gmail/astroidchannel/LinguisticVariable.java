package com.gmail.astroidchannel;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a linguistic variable in the fuzzy inference system.
 *
 * A linguistic variable characterizes a physical or abstract concept (e.g., "Temperature")
 * using fuzzy sets (e.g., "Cold", "Warm", "Hot"). This class holds the definitions
 * of these sets and maps crisp numerical inputs to fuzzy membership degrees.
 *
 * @author lordisfet
 * @version 1.0
 */
public class LinguisticVariable {
    public static final double EPS = 0.00001;
    private String name;
    private double leftBorder;
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
        this.terms = new HashMap<>(terms);
    }

    public LinguisticVariable(LinguisticVariable other) {
        this.name = other.name;
        this.leftBorder = other.leftBorder;
        this.rightBorder = other.rightBorder;
        this.terms = new HashMap<>(other.terms);
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

    /**
     * Додає лінгвістичний термін та його функцію належності до цієї змінної.
     *
     * Бізнес-логіка: Лінгвістична змінна (наприклад, "Температура") складається з множини
     * термінів (наприклад, "Норма", "Спекотно"). Цей метод пов'язує зрозумілу для людини
     * назву терміну з її математичною моделлю (функцією належності), яка безпосередньо
     * визначає геометричну форму нечіткої множини.
     *
     * @param termName Назва лінгвістичного терміну (наприклад, "Критичний_рівень").
     * @param membershipFunction Математична функція (наприклад, трикутна або трапецієподібна),
     *                           що розраховує ступінь належності для цього терміну.
     */
    public void addTerms(String termName, MembershipFunction membershipFunction) {
        terms.put(termName, membershipFunction);
    }

    /**
     * Перетворює чітке числове значення (crisp value) на нечітку множину (fuzzy set),
     * розраховуючи ступінь належності для всіх зареєстрованих термінів.
     *
     * Математичний апарат: Це етап фазифікації, який трансформує реальні фізичні показники
     * (наприклад, дані з сенсорів) у нечіткі значення для подальшого використання
     * в механізмі логічного висновку ``.
     *
     * NFR (Оптимізація ресурсів): Щоб уникнути виснаження пам'яті (Heap Exhaustion) та
     * пришвидшити обробку правил, терміни зі ступенем належності $\mu(x)$, меншим або
     * рівним математичній похибці (EPS), вважаються нульовими і свідомо відкидаються.
     *
     * @param value Чітке числове значення для фазифікації (наприклад, показник 28.5).
     * @return Мапа, що містить лише активні лінгвістичні терміни (ключі) та їхні
     *         відповідні ступені належності (значення строго більші за EPS).
     */
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
