package app.gkai.ai.core.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 加法：add
 *
 * 减法：subtract
 *
 * 乘法：multiply
 *
 * 除法：divide
 */
public class BigDecimalCalculation {
    private final BigDecimal amount;

    public BigDecimalCalculation(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimalCalculation(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public BigDecimalCalculation plus(BigDecimal value) {
        return new BigDecimalCalculation(this.amount.add(value));
    }

    public BigDecimalCalculation plus(String value) {
        return plus(new BigDecimal(value));
    }

    public BigDecimalCalculation minus(BigDecimal value) {
        return new BigDecimalCalculation(this.amount.subtract(value));
    }

    public BigDecimalCalculation minus(String value) {
        return minus(new BigDecimal(value));
    }

    public BigDecimalCalculation mul(BigDecimal value) {
        return new BigDecimalCalculation(this.amount.multiply(value));
    }

    public BigDecimalCalculation mul(String value) {
        return mul(new BigDecimal(value));
    }

    public BigDecimalCalculation div(BigDecimal value) {
        return new BigDecimalCalculation(this.amount.divide(value, RoundingMode.HALF_EVEN));
    }

    public BigDecimalCalculation div(String value) {
        return div(new BigDecimal(value));
    }

    public BigDecimalCalculation div(BigDecimal value, int scale) {
        return new BigDecimalCalculation(this.amount.divide(value, scale, RoundingMode.HALF_EVEN));
    }

    public BigDecimalCalculation div(String value, int scale) {
        return div(new BigDecimal(value), scale);
    }

    public BigDecimal result() {
        return this.amount;
    }
}
