package com.opencmath;

class RealNumber extends BaseNumber {

    private static final PoolTemplate<RealNumber> pool = new PoolTemplate<>(100, 100000, new PoolFactory<RealNumber>() {
        @Override
        public RealNumber create() {
            return new RealNumber();
        }
    });

    double value;

    private RealNumber() {
        super(NumberType.REAL);
    }

    static RealNumber get(double value) {
        RealNumber number = pool.get();
        number.value = value;
        return number;
    }

    static void put(RealNumber item) {
        pool.put(item);
    }

    static int poolSize() {
        return pool.size();
    }

    static boolean checkConsistency() {
        return pool.checkConsistency();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        RealNumber number = (RealNumber) obj;

        return compareRelative(value, number.value) || (value == number.value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public BaseNumber add(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value += integerNumber.value;
                put(number);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                value += realNumber.value;
                put(number);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re += value;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                value += constantNumber.getValue();
                put(number);
                return simplify(this);
            }
            case MATRIX: {
                return simplify(number.add(this));
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber sub(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value -= integerNumber.value;
                put(integerNumber);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                value -= realNumber.value;
                put(realNumber);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re = value - complexNumber.re;
                complexNumber.im = -complexNumber.im;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                value -= constantNumber.getValue();
                put(number);
                return simplify(this);
            }
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                for (int i = 0; i < matrixNumber.value.length; i++) {
                    matrixNumber.value[i] = duplicate(this).sub(matrixNumber.value[i]);
                }

                put(this);
                return simplify(matrixNumber);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber mul(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value *= integerNumber.value;
                put(integerNumber);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                value *= realNumber.value;
                put(realNumber);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re *= value;
                complexNumber.im *= value;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                value *= constantNumber.getValue();
                put(number);
                return simplify(this);
            }
            case MATRIX: {
                return simplify(number.mul(this));
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber div(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value /= integerNumber.value;
                put(integerNumber);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                value /= realNumber.value;
                put(realNumber);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                double mul = complexNumber.re * complexNumber.re + complexNumber.im * complexNumber.im;
                complexNumber.re = (value * complexNumber.re) / mul;
                complexNumber.im = (-value * complexNumber.im) / mul;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                value /= constantNumber.getValue();
                put(number);
                return simplify(this);
            }
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                for (int i = 0; i < matrixNumber.value.length; i++) {
                    matrixNumber.value[i] = duplicate(this).div(matrixNumber.value[i]);
                }

                put(this);
                return simplify(matrixNumber);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber arg() {
        if (value >= 0) {
            put(this);
            return IntegerNumber.get(0);
        }

        put(this);
        return ConstantNumber.get(ConstantType.PI);
    }

    @Override
    public BaseNumber abs() {
        if (value < 0) {
            value = -value;
        }

        return simplify(this);
    }

    @Override
    public BaseNumber ln() {
        if (value < 0) {
            BaseNumber tmp = ComplexNumber.get(Math.log(-value), Math.PI);
            put(this);
            return simplify(tmp);
        }

        value = Math.log(value);
        return simplify(this);
    }

    @Override
    public BaseNumber log(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(number);
                put(this);
                return InvalidNumber.get();
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                BaseNumber numerator;
                BaseNumber denominator;

                if (integerNumber.value == 0) {
                    put(this);
                    return number;
                }
                if (value >= 0) {
                    numerator = RealNumber.get(Math.log(value));
                } else {
                    numerator = ComplexNumber.get(Math.log(-value), Math.PI);
                }
                if (integerNumber.value >= 0) {
                    denominator = RealNumber.get(Math.log(integerNumber.value));
                } else {
                    denominator = ComplexNumber.get(Math.log(-integerNumber.value), Math.PI);
                }

                put(number);
                put(this);
                return simplify(numerator.div(denominator));
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                value = Math.log10(value) / Math.log10(realNumber.value);
                put(number);
                return simplify(this);
            }
            case COMPLEX: {
                BaseNumber tmp = RealNumber.get(Math.log(value)).div(number.ln());
                put(this);
                return simplify(tmp);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                value = Math.log10(value) / Math.log10(constantNumber.getValue());
                put(number);
                return simplify(this);
            }
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                for (int i = 0; i < matrixNumber.value.length; i++) {
                    matrixNumber.value[i] = duplicate(this).log(matrixNumber.value[i]);
                }

                put(this);
                return simplify(matrixNumber);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber exp() {
        value = Math.exp(value);
        return simplify(this);
    }

    @Override
    public BaseNumber pow(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value = Math.pow(value, integerNumber.value);
                put(number);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;

                if (value >= 0) {
                    value = Math.pow(value, realNumber.value);
                    put(number);
                    return simplify(this);
                }

                double theta = realNumber.value * Math.PI;
                double tmp = Math.pow(Math.abs(value), realNumber.value);
                ComplexNumber complexNumber = ComplexNumber.get(tmp * Math.cos(theta), tmp * Math.sin(theta));
                put(number);
                put(this);
                return simplify(complexNumber);
            }
            case COMPLEX: {
                BaseNumber tmp = RealNumber.get(Math.log(Math.abs(value))).mul(number).exp();
                put(this);
                return simplify(tmp);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;

                if (value >= 0) {
                    value = Math.pow(value, constantNumber.getValue());
                    put(number);
                    return simplify(this);
                }

                double theta = constantNumber.getValue() * Math.PI;
                double tmp = Math.pow(Math.abs(value), constantNumber.getValue());
                ComplexNumber complexNumber = ComplexNumber.get(0, tmp * Math.sin(theta));
                put(number);
                put(this);
                return simplify(complexNumber);
            }
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                for (int i = 0; i < matrixNumber.value.length; i++) {
                    matrixNumber.value[i] = duplicate(this).pow(matrixNumber.value[i]);
                }

                put(this);
                return simplify(matrixNumber);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber sqrt() {
        if (value >= 0) {
            value = Math.sqrt(value);
            return simplify(this);
        }

        ComplexNumber complexNumber = ComplexNumber.get(0, Math.sqrt(-value));
        put(this);
        return complexNumber;
    }

    @Override
    public BaseNumber root(BaseNumber exp) {
        switch (exp.type) {
            case INVALID:
                put(exp);
                put(this);
                return InvalidNumber.get();
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) exp;

                if (integerNumber.value == 1) {
                    put(exp);
                    return this;
                }

                double inv = 1.0 / integerNumber.value;
                double theta = inv * Math.atan2(0, value);
                double val = Math.pow(Math.abs(value), inv);

                if (value >= 0) {
                    value = val * Math.cos(theta);
                    put(exp);
                    return simplify(this);
                } else {
                    BaseNumber tmp = ComplexNumber.get(val * Math.cos(theta), val * Math.sin(theta));
                    put(exp);
                    put(this);
                    return simplify(tmp);
                }
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) exp;
                double inv = 1.0 / realNumber.value;
                double theta = inv * Math.atan2(0, value);
                double val = Math.pow(Math.abs(value), inv);

                if (value >= 0) {
                    value = val * Math.cos(theta);
                    put(exp);
                    return simplify(this);
                } else {
                    BaseNumber tmp = ComplexNumber.get(val * Math.cos(theta), val * Math.sin(theta));
                    put(exp);
                    put(this);
                    return simplify(tmp);
                }
            }
            case COMPLEX: {
                return simplify(ln().mul(getOne().div(exp)).exp());
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) exp;
                double inv = 1.0 / constantNumber.getValue();
                double theta = inv * Math.atan2(0, value);
                double val = Math.pow(Math.abs(value), inv);

                if (value >= 0) {
                    value = val * Math.cos(theta);
                    put(exp);
                    return simplify(this);
                } else {
                    BaseNumber tmp = ComplexNumber.get(val * Math.cos(theta), val * Math.sin(theta));
                    put(exp);
                    put(this);
                    return simplify(tmp);
                }
            }
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) exp;

                for (int i = 0; i < matrixNumber.value.length; i++) {
                    matrixNumber.value[i] = duplicate(this).root(matrixNumber.value[i]);
                }

                put(this);
                return simplify(matrixNumber);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber factorial() {
        value++;
        double A = Math.sqrt(2 * Math.PI) / value;
        double B = Math.pow(value + 7.5, value + 0.5) * Math.exp(-value - 7.5);
        double C = 0.99999999999980993227684700473478;
        double D = 676.520368121885098567009190444019 / (value + 1);
        double E = -1259.13921672240287047156078755283 / (value + 2);
        double F = 771.3234287776530788486528258894 / (value + 3);
        double G = -176.61502916214059906584551354 / (value + 4);
        double H = 12.507343278686904814458936853 / (value + 5);
        double I = -0.13857109526572011689554707 / (value + 6);
        double J = 9.984369578019570859563e-6 / (value + 7);
        double K = 1.50563273514931155834e-7 / (value + 8);
        value = B * A * (C + D + E + F + G + H + I + J + K);
        return simplify(this);
    }

    @Override
    public BaseNumber sin() {
        value = Math.sin(value);
        return simplify(this);
    }

    @Override
    public BaseNumber cos() {
        value = Math.cos(value);
        return simplify(this);
    }

    @Override
    public BaseNumber tan() {
        value = Math.tan(value);
        return simplify(this);
    }

    @Override
    public BaseNumber cot() {
        value = 1.0 / Math.tan(value);
        return simplify(this);
    }

    @Override
    public BaseNumber sec() {
        value = 1.0 / Math.cos(value);
        return simplify(this);
    }

    @Override
    public BaseNumber csc() {
        value = 1.0 / Math.sin(value);
        return simplify(this);
    }

    @Override
    public BaseNumber asin() {
        if (value > 1) {
            BaseNumber tmp = ComplexNumber.get(M_PI2, -Math.log(Math.sqrt(value * value - 1) + value));
            put(this);
            return tmp;
        }
        if (value < -1) {
            BaseNumber tmp = ComplexNumber.get(-M_PI2, Math.log(Math.sqrt(value * value - 1) - value));
            put(this);
            return tmp;
        }

        value = Math.asin(value);
        return simplify(this);
    }

    @Override
    public BaseNumber acos() {
        if (value > 1) {
            BaseNumber tmp = ComplexNumber.get(0, Math.log(Math.sqrt(value * value - 1) + value));
            put(this);
            return tmp;
        }
        if (value < -1) {
            BaseNumber tmp = ComplexNumber.get(Math.PI, -Math.log(Math.sqrt(value * value - 1) - value));
            put(this);
            return tmp;
        }

        value = Math.acos(value);
        return simplify(this);
    }

    @Override
    public BaseNumber atan() {
        value = Math.atan(value);
        return simplify(this);
    }

    @Override
    public BaseNumber acot() {
        value = Math.atan(1.0 / value);
        return simplify(this);
    }

    @Override
    public BaseNumber asec() {
        if ((value > 0) && (value < 1)) {
            double inv = 1 / value;
            value = Math.log(Math.sqrt(inv * inv - 1) + inv);
            BaseNumber tmp = ComplexNumber.get(0, value);
            put(this);
            return simplify(tmp);
        }
        if ((value > -1) && (value < 0)) {
            double inv = 1 / -value;
            value = -Math.log(Math.sqrt(inv * inv - 1) + inv);
            BaseNumber tmp = ComplexNumber.get(Math.PI, value);
            put(this);
            return simplify(tmp);
        }

        value = Math.acos(1.0 / value);
        return simplify(this);
    }

    @Override
    public BaseNumber acsc() {
        if ((value > 0) && (value < 1)) {
            double inv = 1 / value;
            value = -Math.log(Math.sqrt(inv * inv - 1) + inv);
            BaseNumber tmp = ComplexNumber.get(M_PI2, value);
            put(this);
            return simplify(tmp);
        }
        if ((value > -1) && (value < 0)) {
            double inv = 1 / -value;
            value = Math.log(Math.sqrt(inv * inv - 1) + inv);
            BaseNumber tmp = ComplexNumber.get(-M_PI2, value);
            put(this);
            return simplify(tmp);
        }

        value = Math.asin(1.0 / value);
        return simplify(this);
    }

    @Override
    public BaseNumber sinh() {
        value = Math.sinh(value);
        return simplify(this);
    }

    @Override
    public BaseNumber cosh() {
        value = Math.cosh(value);
        return simplify(this);
    }

    @Override
    public BaseNumber tanh() {
        value = Math.tanh(value);
        return simplify(this);
    }

    @Override
    public BaseNumber coth() {
        value = 1.0 / Math.tanh(value);
        return simplify(this);
    }

    @Override
    public BaseNumber sech() {
        value = 1.0 / Math.cosh(value);
        return simplify(this);
    }

    @Override
    public BaseNumber csch() {
        value = 1.0 / Math.sinh(value);
        return simplify(this);
    }

    @Override
    public BaseNumber asinh() {
        // asinh(z) = ln(z + sqrt(1 + z^2))
        value = Math.log(value + Math.sqrt(1 + value * value));
        return simplify(this);
    }

    @Override
    public BaseNumber acosh() {
        // acosh(z) = ln(z + sqrt(z + 1)sqrt(z - 1))
        if (value > 1) { // re=ln(x+sqrt(x+1)*sqrt(x-1)) im=0
            value = Math.log(value + Math.sqrt(value + 1) * Math.sqrt(value - 1));
            return this;
        }
        if (value < -1) { // re=ln(abs(x)+sqrt(abs(x)+1)*sqrt(abs(x)-1)) im=PI
            value = -value;
            value = Math.log(value + Math.sqrt(value + 1) * Math.sqrt(value - 1));
            BaseNumber tmp = ComplexNumber.get(value, Math.PI);
            put(this);
            return simplify(tmp);
        }

        // re=0 im=atan2(sqrt(x+1)*sqrt(abs(x-1)), x)
        BaseNumber tmp = ComplexNumber.get(0, Math.atan2(Math.sqrt(value + 1) * Math.sqrt(Math.abs(value - 1)), value));
        put(this);
        return simplify(tmp);
    }

    @Override
    public BaseNumber atanh() {
        // atanh(z) = 1/2 (ln(1 + z) - ln(1 - z))
        if ((value > -1) && (value < 1)) { // re=0.5*ln(x+1)-0.5*ln(1-x) im=0
            value = 0.5 * Math.log(value + 1) - 0.5 * Math.log(1 - value);
            return this;
        }

        // re = 0.25*ln((x+1)^2)-0.25*ln((1-x)^2) im=(value>0) ? -PI/2 : PI/2
        double a = value + 1;
        double b = 1 - value;
        double re = 0.25 * Math.log(a * a) - 0.25 * Math.log(b * b);
        double im = (value > 0) ? -M_PI2 : M_PI2;
        BaseNumber tmp = ComplexNumber.get(re, im);
        put(this);
        return simplify(tmp);
    }

    @Override
    public BaseNumber acoth() {
        // atanh(z) = 1/2 (ln(1 + 1/z) - ln(1 - 1/z))
        if ((value < -1) || (value > 1)) { // re=0.5*ln(1/x+1)-0.5*ln(1-1/x) im=0
            double inv = 1 / value;
            value = 0.5 * Math.log(inv + 1) - 0.5 * Math.log(1 - inv);
            return this;
        }

        // re=0.25*ln((1/x+1)^2)-0.25*ln((1-1/x)^2) im=(value>0) ? -PI/2 : PI/2
        double inv = 1 / value;
        double a = inv + 1;
        double b = 1 - inv;
        double re = 0.25 * Math.log(a * a) - 0.25 * Math.log(b * b);
        double im = (value > 0) ? -M_PI2 : M_PI2;
        BaseNumber tmp = ComplexNumber.get(re, im);
        put(this);
        return simplify(tmp);
    }

    @Override
    public BaseNumber asech() {
        // asech(z) = ln(sqrt(1/z - 1)sqrt(1/z + 1) + 1/z)
        if (value > 1) { // re=0 im=atan2(sqrt((x-1)/(x+1))*(1/x + 1), 1/x)
            double inv = 1.0 / value;
            ComplexNumber complexNumber = ComplexNumber.get(0, Math.atan2(Math.sqrt((value - 1) / (value + 1)) * (inv + 1), inv));
            put(this);
            return simplify(complexNumber);
        } else if (value < -1) { // re=0 im=atan2(sqrt((1-x)/(-x-1))*(1/x + 1), 1/x)
            double inv = 1.0 / value;
            ComplexNumber complexNumber = ComplexNumber.get(0, Math.atan2(Math.sqrt((1 - value) / (-value - 1)) * (inv + 1), inv));
            put(this);
            return simplify(complexNumber);
        } else if (value > 0) { // re=ln(sqrt(1/x-1)*sqrt(1/x+1)+1/x) im=0
            double inv = 1.0 / value;
            value = Math.log(Math.sqrt(inv - 1) * Math.sqrt(inv + 1) + inv);
            return this;
        } else /*if (value < 0)*/ { // re=ln(sqrt(1/x-1)*sqrt(1/x+1)+1/x) im=PI
            double inv = 1.0 / (-value);
            ComplexNumber complexNumber = ComplexNumber.get(Math.log(Math.sqrt(inv - 1) * Math.sqrt(inv + 1) + inv), Math.PI);
            put(this);
            return simplify(complexNumber);
        }
    }

    @Override
    public BaseNumber acsch() {
        // re=ln(sqrt(1/(x^2)+1)+1/x) im=0
        double inv = 1 / value;
        value = Math.log(Math.sqrt(inv * inv + 1) + inv);
        return this;
    }

    @Override
    public BaseNumber and(BaseNumber number) {
        put(this);
        put(number);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber or(BaseNumber number) {
        put(this);
        put(number);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber xor(BaseNumber number) {
        put(this);
        put(number);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber not() {
        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber shl(BaseNumber number) {
        put(this);
        put(number);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber shr(BaseNumber number) {
        put(this);
        put(number);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber det() {
        return this;
    }

    @Override
    public BaseNumber transpose() {
        return this;
    }

    @Override
    public BaseNumber trace() {
        return this;
    }

    @Override
    public BaseNumber adjugate() {
        put(this);
        return IntegerNumber.get(1);
    }

    @Override
    public BaseNumber gauss() {
        put(this);
        return IntegerNumber.get(0);
    }

    @Override
    public BaseNumber rank() {
        put(this);
        return IntegerNumber.get(1);
    }

    @Override
    public BaseNumber inv() {
        return IntegerNumber.get(1).div(this);
    }

    @Override
    public BaseNumber toRadians(AngleType angle) {
        if (angle == AngleType.RAD) {
            return this;
        }

        value *= angle.toValue;
        return simplify(this);
    }

    @Override
    public BaseNumber fromRadians(AngleType angle) {
        if (angle == AngleType.RAD) {
            return this;
        }

        value *= angle.fromValue;
        return simplify(this);
    }
}
