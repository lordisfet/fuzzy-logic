package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.getLinear;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Реалізує трапецієподібну функцію належності.
 *
 * Бізнес-контекст: В архітектурі системи моніторингу ця функція використовується
 * для визначення "зон комфорту" або "плато" мікроклімату (наприклад, цільовий
 * діапазон температури від 21°C до 24°C). На відміну від трикутної функції,
 * трапеція дозволяє системі управління не змінювати стан апаратури (вентиляторів),
 * поки фізичні показники перебувають у межах ядра (Core). Це захищає реле від
 * зносу через занадто часті перемикання (NFR: Ресурс апаратури).
 *
 * Математична модель: Функція визначається чотирма точками $(a, b, c, d)$, де
 * проміжок $[b, c]$ утворює ядро (ступінь $1.0$), а проміжки $(a, b)$ та $(c, d)$ —
 * лінійні або криволінійні схили.
 */
public class TrapezoidalFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private double d;
    private TransitionCurve leftPart;
    private TransitionCurve rightPart;

    /**
     * Ініціалізує трапецієподібну функцію зі стандартними лінійними схилами.
     *
     * @param a Точка початку зростання на осі X ($\mu(a) = 0.0$).
     * @param b Точка початку плато на осі X ($\mu(b) = 1.0$).
     * @param c Точка завершення плато на осі X ($\mu(c) = 1.0$).
     * @param d Точка завершення спадання на осі X ($\mu(d) = 0.0$).
     * @throws IllegalArgumentException (Рекомендовано) якщо порушується умова $a \le b \le c \le d$.
     */
    public TrapezoidalFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.leftPart = getLinear(a, b);
        this.rightPart = getLinear(c, d);
    }

    /**
     * Ініціалізує трапецієподібну функцію з кастомними перехідними кривими (Inversion of Control).
     *
     * NFR: Дозволяє комбінувати різні математичні моделі для асиметричних реакцій.
     * Наприклад, лінійне зростання для нагрівання та експоненційне спадання
     * (через переданий лямбда-вираз) для охолодження приміщення.
     *
     * @param a Точка початку лівого схилу.
     * @param b Точка початку ядра.
     * @param c Точка кінця ядра.
     * @param d Точка кінця правого схилу.
     * @param leftPart Лямбда-вираз, що описує математику лівого схилу.
     * @param rightPart Лямбда-вираз, що описує математику правого схилу.
     */
    public TrapezoidalFunction(double a, double b, double c, double d, TransitionCurve leftPart, TransitionCurve rightPart) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    /**
     * Конструктор копіювання (Copy Constructor).
     *
     * Виконує поверхневе копіювання (shallow copy) об'єктів TransitionCurve
     * для оптимізації використання пам'яті (Heap) під час масштабування правил.
     *
     * @param other Об'єкт трапецієподібної функції для копіювання.
     */
    public TrapezoidalFunction(TrapezoidalFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.c = other.c;
        this.d = other.d;
        this.leftPart = other.leftPart;
        this.rightPart = other.rightPart;
    }

    // Тривіальні гетери та сетери ігноруються лінтером

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

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public TransitionCurve getLeftPart() {
        return leftPart;
    }

    public void setLeftPart(TransitionCurve leftPart) {
        this.leftPart = leftPart;
    }

    public TransitionCurve getRightPart() {
        return rightPart;
    }

    public void setRightPart(TransitionCurve rightPart) {
        this.rightPart = rightPart;
    }

    // Перевизначені методи (@Override) успадковують документацію автоматично

    @Override
    public double calculate(double x) {
        if (Double.compare(x, a) <= 0 || Double.compare(x, d) >= 0) {
            return 0.0;
        }
        else if (Double.compare(x, b) < 0) {
            return leftPart.calculate(x);
        }
        else if (Double.compare(x, c) <= 0) {
            return 1.0;
        }
        else {
            return MembershipFunction.invert0to1Value(rightPart.calculate(x));
        }
    }

    @Override
    public double findHeight() {
        return calculate(b);
    }

    @Override
    public Range<Double> findCarrier() {
        return Range.open(a, d);
    }

    @Override
    public Range<Double> findCore() {
        return Range.closed(b, c);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(a, b));
        boundaries.add(Range.open(c, d));

        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass()!= o.getClass()) return false;
        TrapezoidalFunction that = (TrapezoidalFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && Double.compare(c, that.c) == 0 && Double.compare(d, that.d) == 0 && Objects.equals(leftPart, that.leftPart) && Objects.equals(rightPart, that.rightPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d, leftPart, rightPart);
    }

    @Override
    public String toString() {
        return "TrapezoidalFunction{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", leftPart=" + leftPart +
                ", rightPart=" + rightPart +
                '}';
    }
}