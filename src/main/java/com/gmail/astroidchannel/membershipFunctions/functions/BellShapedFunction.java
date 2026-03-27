package com.gmail.astroidchannel.membershipFunctions.functions;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.getBell;

/**
 * Реалізує дзвоноподібну (Bell-shaped) функцію належності.
 *
 * Бізнес-контекст: В архітектурі моніторингу мікроклімату ця функція ідеально
 * підходить для моделювання лінгвістичного терміну "Норма" (наприклад, цільова
 * температура або вологість у пташнику). Вона забезпечує плавний, симетричний
 * перехід від стану комфорту до стану стресу для птиці, що дозволяє системі
 * управління м'яко змінювати оберти витяжних вентиляторів без різких стрибків
 * напруги в електромережі.
 *
 * Математична модель: Розраховується за формулою $\mu(x) = \frac{1}{1 + |\frac{x - c}{a}|^{2b}}$.
 */
public class BellShapedFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private TransitionCurve curve;

    /**
     * Створює дзвоноподібну функцію з автоматичною генерацією перехідної кривої.
     *
     * @param a Ширина дзвона (визначає зону допустимого відхилення від ідеального
     *          показника, наприклад, $\pm 2^\circ C$).
     * @param b Крутизна схилів (визначає, наскільки швидко система реагує на
     *          погіршення мікроклімату; чим більше значення, тим різкіший спад).
     * @param c Центр дзвона (математичне очікування або ідеальний фізичний показник,
     *          де ступінь належності $\mu(x) = 1.0$).
     */
    public BellShapedFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.curve = getBell(a, b, c);
    }

    /**
     * Створює дзвоноподібну функцію із заздалегідь визначеною перехідною кривою (Inversion of Control).
     *
     * NFR: Цей конструктор дозволяє перевикористовувати існуючі об'єкти кривих,
     * зменшуючи кількість алокацій пам'яті на бекенді при масовому створенні правил.
     *
     * @param a Ширина дзвона.
     * @param b Крутизна схилів.
     * @param c Центр дзвона.
     * @param curve Готовий лямбда-вираз для розрахунку ступеня належності.
     */
    public BellShapedFunction(double a, double b, double c, TransitionCurve curve) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.curve = curve;
    }

    /**
     * Конструктор копіювання (Copy Constructor).
     *
     * @param other Об'єкт дзвоноподібної функції для глибокого копіювання її параметрів.
     */
    public BellShapedFunction(BellShapedFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.c = other.c;
        this.curve = other.curve;
    }

    // Тривіальні гетери та сетери ігноруються лінтером (MissingJavadocMethodCheck з allowMissingPropertyJavadoc=true)

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public TransitionCurve getCurve() {
        return curve;
    }

    public void setCurve(TransitionCurve curve) {
        this.curve = curve;
    }

    // Усі перевизначені методи (@Override) автоматично проходять перевірку,
    // успадковуючи контракти з інтерфейсу MembershipFunction та класу Object.

    @Override
    public double calculate(double x) {
        return curve.calculate(x);
    }

    @Override
    public double findHeight() {
        return 1;
    }

    @Override
    public Range<Double> findCarrier() {
        return Range.open(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Range<Double> findCore() {
        return Range.closed(c, c);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(Double.MIN_VALUE, c));
        boundaries.add(Range.open(c, Double.MAX_VALUE));

        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass()!= o.getClass()) return false;
        BellShapedFunction that = (BellShapedFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && Double.compare(c, that.c) == 0 && Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, curve);
    }

    @Override
    public String toString() {
        return "BellShapedFunction{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", curve=" + curve +
                '}';
    }
}