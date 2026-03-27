package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.*;
import com.google.common.collect.Range;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Реалізує трикутну функцію належності.
 *
 * Бізнес-контекст: В архітектурі моніторингу птахофабрики (Backend Server)
 * ця функція використовується для моделювання жорстких цільових показників,
 * які не мають допустимого "плато" (наприклад, критичний поріг тиску в трубах
 * охолодження). Будь-яке відхилення від ідеальної точки 'b' миттєво і лінійно
 * знижує ступінь належності, провокуючи пропорційну агресивну реакцію системи.
 *
 * NFR (Продуктивність): Обчислення базується на простій лінійній алгебрі $O(1)$.
 * Це найшвидша з усіх функцій, що робить її ідеальною для обробки масивів даних
 * телеметрії в реальному часі без перевантаження потоків Spring Boot.
 *
 * Математична модель: Визначається трьома точками $(a, b, c)$, де $b$ — вершина
 * трикутника ($\mu(b) = 1.0$), а $a$ та $c$ — його основи ($\mu(a) = 0.0, \mu(c) = 0.0$).
 */
public class TriangularFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private TransitionCurve leftPart;
    private TransitionCurve rightPart;

    /**
     * Ініціалізує трикутну функцію зі стандартними лінійними схилами.
     *
     * @param a Точка початку зростання на осі X ($\mu(a) = 0.0$).
     * @param b Вершина трикутника на осі X ($\mu(b) = 1.0$).
     * @param c Точка завершення спадання на осі X ($\mu(c) = 0.0$).
     * @throws IllegalArgumentException (Рекомендовано) якщо порушується просторова логіка $a \le b \le c$.
     */
    public TriangularFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.leftPart = getLinear(a, b);
        this.rightPart = getLinear(b, c);
    }

    /**
     * Ініціалізує трикутну функцію з кастомними перехідними кривими (Inversion of Control).
     *
     * NFR: Дозволяє інжектувати готові лямбда-вирази для уникнення зайвих алокацій
     * пам'яті (Memory Allocation) при масовому створенні об'єктів на бекенді.
     *
     * @param a Точка початку лівого схилу.
     * @param b Вершина трикутника.
     * @param c Точка кінця правого схилу.
     * @param leftPart Заздалегідь скомпільований лямбда-вираз для лівого схилу.
     * @param rightPart Заздалегідь скомпільований лямбда-вираз для правого схилу.
     */
    public TriangularFunction(double a, double b, double c, TransitionCurve leftPart, TransitionCurve rightPart) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    /**
     * Конструктор копіювання (Copy Constructor).
     *
     * Виконує поверхневе копіювання (shallow copy) об'єктів TransitionCurve
     * для мінімізації навантаження на Garbage Collector.
     *
     * @param other Об'єкт трикутної функції для копіювання.
     */
    public TriangularFunction(TriangularFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.c = other.c;
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
        if (Double.compare(x,b) == 0) {
            return 1.0;
        }
        if (Double.compare(x, a) <= 0) {
            return 0.0;
        }
        if (Double.compare(x, c) >= 0) {
            return 0.0;
        }
        if (Double.compare(x, a) > 0 && Double.compare(x, b) <= 0) {
            return leftPart.calculate(x);
        }
        if (Double.compare(x, b) > 0 && Double.compare(x, c) <= 0) {
            return MembershipFunction.invert0to1Value(rightPart.calculate(x));
        }

        // Захист від непередбачених станів (наприклад, NaN)
        throw new IllegalArgumentException("x = " + x + " is not in conditions");
    }

    @Override
    public double findHeight() {
        return calculate(b);
    }

    @Override
    public Range<Double> findCarrier() {
        return Range.open(a, c);
    }

    @Override
    public Range<Double> findCore() {
        return Range.closed(b, b);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(a, b));
        boundaries.add(Range.open(b, c));

        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass()!= o.getClass()) return false;
        TriangularFunction that = (TriangularFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && Double.compare(c, that.c) == 0 && Objects.equals(leftPart, that.leftPart) && Objects.equals(rightPart, that.rightPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, leftPart, rightPart);
    }

    @Override
    public String toString() {
        return "TriangularFunction{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", leftPart=" + leftPart +
                ", rightPart=" + rightPart +
                '}';
    }
}