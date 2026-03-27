package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import static com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation.getCosine;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Реалізує Z-подібну або S-подібну функцію належності на базі косинусоїдального переходу.
 *
 * Бізнес-контекст: Згідно з нашою архітектурою, ця функція розгортається на
 * Backend-сервері для аналізу нелінійних трендів мікроклімату.
 * S-подібна форма (Зростання) ідеально підходить для моделювання накопичення
 * небезпечних факторів (наприклад, концентрації газів від вузлів N1-N6), де
 * загроза плавно зростає від безпечного рівня 'a' до критичного 'b'.
 * Z-подібна форма (Спадання) використовується для моделювання виснаження
 * ресурсів або падіння температури вночі.
 *
 * NFR (Пам'ять та Продуктивність): Використання Enum для визначення форми
 * (Shape) є type-safe рішенням, яке уникає використання "магічних чисел",
 * гарантуючи стабільність розрахунків у багатопотоковому середовищі.
 */
public class ZSShapedFunction implements MembershipFunction {
    private double a;
    private double b;
    private Shape shape;
    private TransitionCurve curve;

    /**
     * Визначає математичну форму кривої (Спадаюча або Зростаюча).
     */
    public enum Shape {
        Z, S
    }

    /**
     * Ініціалізує Z/S-подібну функцію з автоматичною генерацією косинусоїдальної кривої.
     *
     * @param a Точка на осі X, де починається перехід (для S-форми $\mu(a)=0.0$, для Z-форми $\mu(a)=1.0$).
     * @param b Точка на осі X, де завершується перехід (для S-форми $\mu(b)=1.0$, для Z-форми $\mu(b)=0.0$).
     * @param shape Тип функції: Shape.S (зростаюча) або Shape.Z (спадна).
     */
    public ZSShapedFunction(double a, double b, Shape shape) {
        this.a = a;
        this.b = b;
        this.shape = shape;
        this.curve = getCosine(a, b);
    }

    /**
     * Ініціалізує Z/S-подібну функцію з кастомною перехідною кривою (Inversion of Control).
     *
     * @param a Точка початку переходу.
     * @param b Точка завершення переходу.
     * @param shape Тип функції (Z або S).
     * @param curve Заздалегідь скомпільований лямбда-вираз для розрахунку переходу.
     */
    public ZSShapedFunction(double a, double b, Shape shape, TransitionCurve curve) {
        this.a = a;
        this.b = b;
        this.shape = shape;
        this.curve = curve;
    }

    /**
     * Конструктор копіювання (Copy Constructor).
     *
     * Виконує поверхневе копіювання (shallow copy) об'єкта TransitionCurve
     * для збереження обчислювальних ресурсів сервера.
     *
     * @param other Об'єкт функції для копіювання.
     */
    public ZSShapedFunction(ZSShapedFunction other) {
        this.a = other.a;
        this.b = other.b;
        this.shape = other.shape;
        this.curve = other.curve;
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

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public TransitionCurve getCurve() {
        return curve;
    }

    public void setCurve(TransitionCurve curve) {
        this.curve = curve;
    }

    // Перевизначені методи (@Override) успадковують документацію автоматично

    @Override
    public double calculate(double x) {
        if (x <= a) {
            return shape.ordinal() == Shape.Z.ordinal()? 1.0 : 0.0;
        }
        if (x >= b) {
            return shape.ordinal() == Shape.Z.ordinal()? 0.0 : 1.0;
        }
        if (x > a && x < b) {
            double value = curve.calculate(x);
            return shape.ordinal() == Shape.Z.ordinal()? value : MembershipFunction.invert0to1Value(value);
        }

        throw new IllegalArgumentException("x = " + x + " is not in conditions");
    }

    @Override
    public double findHeight() {
        return 1.0;
    }

    @Override
    public Range<Double> findCarrier() {
        return shape.ordinal() == Shape.Z.ordinal()? Range.open(Double.MIN_VALUE, b) : Range.open(a, Double.MAX_VALUE);
    }

    @Override
    public Range<Double> findCore() {
        return shape.ordinal() == Shape.Z.ordinal()? Range.closed(Double.MIN_VALUE, a) : Range.closed(b, Double.MAX_VALUE);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        // TODO: КРИТИЧНА ПОМИЛКА БІЗНЕС-ЛОГІКИ.
        // Поточна реалізація повертає лише перехідну зону (a, b).
        // Для S-форми спектр має бути (a, +∞), оскільки після 'b' ступінь належності = 1.0 (>0).
        // Для Z-форми спектр має бути (-∞, b), оскільки до 'a' ступінь належності = 1.0 (>0).
        Set<Range<Double>> result = new LinkedHashSet<>();
        result.add((Range.open(a, b)));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass()!= o.getClass()) return false;
        ZSShapedFunction that = (ZSShapedFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0 && shape == that.shape && Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, shape, curve);
    }

    @Override
    public String toString() {
        return "ZSShapedFunction{" +
                "a=" + a +
                ", b=" + b +
                ", shape=" + shape +
                ", curve=" + curve +
                '}';
    }
}