package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation;
import com.google.common.collect.Range;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Реалізує сигмоїдну (S-подібну або Z-подібну) функцію належності.
 *
 * Бізнес-контекст: Згідно з архітектурою (Зображення 3), ця функція виконується
 * на Backend-сервері для оцінки критичних станів мікроклімату, де загроза зростає експоненційно.
 * Наприклад, при оцінці рівня токсичних газів, де перевищення точки перегину
 * призводить до різкого формування події "Critical situation" та активації
 * системи сповіщень (AVAS або Telegram API).[1]
 *
 * Математична модель: Обчислюється за логістичною формулою $\mu(x) = \frac{1}{1 + e^{-a(x - b)}}$.
 * Якщо $a &gt; 0$, функція є S-подібною (моделює зростання небезпеки).
 * Якщо $a &lt; 0$, функція є Z-подібною (моделює спадання показника).
 */
public class SigmoidFunction implements MembershipFunction {
    private double a;
    private double b;
    private TransitionCurve curve;

    /**
     * Ініціалізує сигмоїдну функцію з автоматичною генерацією перехідної кривої.
     *
     * @param a Крутизна кривої (швидкість зміни стану мікроклімату).
     * @param b Точка перегину (inflection point), координата на осі X, де $\mu(b) = 0.5$.
     */
    public SigmoidFunction(double a, double b) {
        this.a = a;
        this.b = b;
        this.curve = CurveCalculation.getSigmoid(a, b);
    }

    /**
     * Ініціалізує сигмоїдну функцію із заздалегідь визначеною перехідною кривою (Inversion of Control).
     *
     * NFR: Цей метод дозволяє перевикористовувати stateless лямбда-вирази,
     * що зменшує навантаження на купу (Heap) Spring Boot сервера при створенні бази правил.
     *
     * @param a Крутизна кривої.
     * @param b Точка перегину.
     * @param curve Готовий лямбда-вираз для розрахунку ступеня належності.
     */
    public SigmoidFunction(double a, double b, TransitionCurve curve) {
        this.a = a;
        this.b = b;
        this.curve = curve;
    }

    /**
     * Конструктор копіювання (Copy Constructor).
     *
     * Виконує поверхневе копіювання (shallow copy) об'єкта кривої для збереження
     * обчислювальних ресурсів мікроконтролерів та серверів.
     *
     * @param other Об'єкт сигмоїдної функції для копіювання.
     */
    public SigmoidFunction(SigmoidFunction other) {
        this.a = other.a;
        this.b = other.b;
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

    public TransitionCurve getCurve() {
        return curve;
    }

    public void setCurve(TransitionCurve curve) {
        this.curve = curve;
    }

    // Перевизначені методи (@Override) успадковують документацію автоматично

    @Override
    public double calculate(double x) {
        return curve.calculate(x);
    }

    @Override
    public double findHeight() {
        return 1.0;
    }

    @Override
    public Range<Double> findCarrier() {
        // Сигмоїда асимптотично наближається до 0, тому носієм є вся числова вісь.
        return Range.open(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Range<Double> findCore() {
        // TODO: Архітектурний ризик. Повернення 'null' порушує контракти безпечного коду.
        // Математично ядро сигмоїди порожнє (ніколи не досягає абсолютної 1.0).
        // Бібліотека Guava не має методу Range.empty(), тому в майбутньому слід
        // змінити сигнатуру на Optional<Range<Double>> або викидати виняток.
        return null;
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        Set<Range<Double>> boundaries = new LinkedHashSet<>();
        boundaries.add(Range.open(Double.MIN_VALUE, Double.MAX_VALUE));
        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass()!= o.getClass()) return false;
        SigmoidFunction that = (SigmoidFunction) o;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "SigmoidFunction{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}