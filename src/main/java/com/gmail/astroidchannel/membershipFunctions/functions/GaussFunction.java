package com.gmail.astroidchannel.membershipFunctions.functions;

import com.gmail.astroidchannel.membershipFunctions.MembershipFunction;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.CurveCalculation;
import com.gmail.astroidchannel.membershipFunctions.curvesTypes.TransitionCurve;
import com.google.common.collect.Range;

import java.util.Objects;
import java.util.Set;

/**
 * Реалізує функцію належності Гаусса (нормальний розподіл).
 *
 * Бізнес-контекст: В архітектурі системи моніторингу (на рівні Backend Server)
 * ця функція ідеально підходить для моделювання фізичних явищ, які мають
 * природне, плавне відхилення від цільового показника. Наприклад, вона
 * найточніше описує розсіювання тепла в ангарі довжиною 100 метрів між
 * вимірювальними вузлами (N1-N6), оскільки температура розподіляється
 * за законами термодинаміки без різких просторових стрибків.
 *
 * Математична модель: Розраховується за формулою $\mu(x)=e^{-\frac{(x-c)^2}{2\sigma^2}}$.
 */
public class GaussFunction implements MembershipFunction {
    private double c;
    private double sigma;
    private TransitionCurve curve;

    /**
     * Ініціалізує функцію Гаусса з автоматичною генерацією перехідної кривої.
     *
     * @param c Математичне очікування (центр симетрії, наприклад, ідеальна цільова температура).
     * @param sigma Середньоквадратичне відхилення (визначає ширину "дзвона" та толерантність до відхилень).
     */
    public GaussFunction(double c, double sigma) {
        this.c = c;
        this.sigma = sigma;
        this.curve = CurveCalculation.getGauss(c, sigma);
    }

    /**
     * Ініціалізує функцію Гаусса із заздалегідь визначеною перехідною кривою (Inversion of Control).
     *
     * NFR: Дозволяє перевикористовувати існуючі об'єкти лямбда-виразів, що критично
     * знижує навантаження на Garbage Collector під час агрегації тисяч пакетів телеметрії.
     *
     * @param c Математичне очікування.
     * @param sigma Середньоквадратичне відхилення.
     * @param curve Готовий лямбда-вираз для розрахунку ступеня належності.
     */
    public GaussFunction(double c, double sigma, TransitionCurve curve) {
        this.c = c;
        this.sigma = sigma;
        this.curve = curve;
    }

    /**
     * Конструктор копіювання (Copy Constructor).
     *
     * @param other Об'єкт функції Гаусса для глибокого копіювання її стану.
     */
    public GaussFunction(GaussFunction other) {
        this.c = other.c;
        this.sigma = other.sigma;
        this.curve = other.curve;
    }

    // Тривіальні гетери та сетери ігноруються лінтером (MissingJavadocMethod з allowMissingPropertyJavadoc=true)

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
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
        return 1;
    }

    @Override
    public Range<Double> findCarrier() {
        // Фізично функція Гаусса ніколи не досягає абсолютного 0, тому носій - це вся числова вісь
        return Range.open(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Range<Double> findCore() {
        // Ядро - це множина точок, де ступінь належності дорівнює точно 1.0 (точка 'c')
        return Range.closed(c, c);
    }

    @Override
    public Set<Range<Double>> findSpectrum() {
        // TODO (Technical Debt): Повертати порожній Set математично некоректно.
        // Необхідно імплементувати розрахунок alpha-cut (альфа-зрізу) для заданого спектру.
        return Set.of();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass()!= o.getClass()) return false;
        GaussFunction that = (GaussFunction) o;
        return Double.compare(c, that.c) == 0 && Double.compare(sigma, that.sigma) == 0 && Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, sigma, curve);
    }

    @Override
    public String toString() {
        return "GaussFunction{" +
                "c=" + c +
                ", sigma=" + sigma +
                ", curve=" + curve +
                '}';
    }
}