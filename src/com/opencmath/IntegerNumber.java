package com.opencmath;

class IntegerNumber extends BaseNumber {
    private static final PoolTemplate<IntegerNumber> pool = new PoolTemplate<>(100, 100000, new PoolFactory<IntegerNumber>() {
        @Override
        public IntegerNumber create() {
            return new IntegerNumber();
        }
    });

    long value;

    private IntegerNumber() {
        super(NumberType.INTEGER);
    }

    static IntegerNumber get(long value) {
        IntegerNumber number = pool.get();
        number.value = value;
        return number;
    }

    static void put(IntegerNumber item) {
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

        IntegerNumber number = (IntegerNumber) obj;
        return value == number.value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public BaseNumber add(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                try {
                    value = addChecked(value, integerNumber.value);
                } catch (ArithmeticException e) {
                    RealNumber realNumber = RealNumber.get((double)value + (double)integerNumber.value);
                    put(number);
                    put(this);
                    return simplify(realNumber);
                }
                put(number);
                return this;
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value += value;
                put(this);
                return simplify(number);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re += value;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                if (value == 0) {
                    put(this);
                    return number;
                }

                ConstantNumber constantNumber = (ConstantNumber) number;
                RealNumber realNumber = RealNumber.get(value + constantNumber.getValue());
                put(number);
                put(this);
                return simplify(realNumber);
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
                try {
                    value = subChecked(value, integerNumber.value);
                    put(number);
                } catch (ArithmeticException e) {
                    RealNumber realNumber = RealNumber.get((double)value - (double)integerNumber.value);
                    put(number);
                    put(this);
                    return simplify(realNumber);
                }
                return this;
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value = value - realNumber.value;
                put(this);
                return simplify(number);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re = value - complexNumber.re;
                complexNumber.im = -complexNumber.im;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                if (value == 0) {
                    RealNumber realNumber = RealNumber.get(-Math.PI);
                    put(number);
                    put(this);
                    return realNumber;
                }

                ConstantNumber constantNumber = (ConstantNumber) number;
                RealNumber realNumber = RealNumber.get(value - constantNumber.getValue());
                put(number);
                put(this);
                return simplify(realNumber);
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
                try {
                    value = mulChecked(value, integerNumber.value);
                    put(number);
                } catch (ArithmeticException e) {
                    RealNumber realNumber = RealNumber.get((double)value * (double)integerNumber.value);
                    put(number);
                    put(this);
                    return simplify(realNumber);
                }
                return this;
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value *= value;
                put(this);
                return simplify(number);
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
                RealNumber realNumber = RealNumber.get(value * constantNumber.getValue());
                put(number);
                put(this);
                return simplify(realNumber);
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

                if (integerNumber.value == 0) { //divide by zero
                    put(number);
                    put(this);
                    return InvalidNumber.get();
                }

                if (value % integerNumber.value == 0) {
                    value /= integerNumber.value;
                    put(number);
                    return this;
                }

                RealNumber realNumber = RealNumber.get(value / (double)integerNumber.value);
                put(number);
                put(this);
                return simplify(realNumber);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value = value / realNumber.value;
                put(this);
                return simplify(number);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                double tmp = complexNumber.re * complexNumber.re + complexNumber.im * complexNumber.im;
                complexNumber.re = (value * complexNumber.re) / tmp;
                complexNumber.im = -(value * complexNumber.im) / tmp;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                RealNumber realNumber = RealNumber.get(value / constantNumber.getValue());
                put(number);
                put(this);
                return simplify(realNumber);
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
            value = 0;
            return this;
        }

        put(this);
        return ConstantNumber.get(ConstantType.PI);
    }

    @Override
    public BaseNumber abs() {
        if (value < 0) {
            value = -value;
        }
        return this;
    }

    @Override
    public BaseNumber ln() {
        BaseNumber number = RealNumber.get(value).ln();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber log(BaseNumber base) {
        BaseNumber number = RealNumber.get(value).log(base);
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber exp() {
        BaseNumber number = RealNumber.get(value).exp();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber pow(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;

                if ((value >= 0) && (integerNumber.value >= 0)) {
                    double tmp = Math.pow(value, integerNumber.value);

                    if (tmp > Long.MAX_VALUE) {
                        RealNumber realNumber = RealNumber.get(tmp);
                        put(number);
                        put(this);
                        return simplify(realNumber);
                    }

                    value = intPower(value, integerNumber.value);
                    put(number);
                    return this;
                }

                RealNumber realNumber = RealNumber.get(Math.pow(value, integerNumber.value));
                put(number);
                put(this);
                return simplify(realNumber);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;

                if ((value >= 0)) {
                    realNumber.value = Math.pow(value, realNumber.value);
                    put(this);
                    return simplify(realNumber);
                }

                double theta = realNumber.value * Math.PI;
                double tmp = Math.pow(Math.abs(value), realNumber.value);
                ComplexNumber complexNumber = ComplexNumber.get(tmp * Math.cos(theta), tmp * Math.sin(theta));
                put(number);
                put(this);
                return simplify(complexNumber);
            }
            case COMPLEX: {
                BaseNumber theta = duplicate(number).mul(RealNumber.get(Math.atan2(0, value)));
                BaseNumber result = duplicate(this).ln().mul(number).exp().mul(duplicate(theta).cos().add(getComplexOne().mul(duplicate(theta).sin())));
                put(theta);
                put(this);
                return simplify(result);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;

                if ((value >= 0)) {
                    RealNumber realNumber = RealNumber.get(constantNumber.getValue());
                    realNumber.value = Math.pow(value, realNumber.value);
                    put(number);
                    put(this);
                    return simplify(realNumber);
                }

                double theta = constantNumber.getValue() * Math.PI;
                double tmp = Math.pow(Math.abs(value), constantNumber.getValue());
                ComplexNumber complexNumber = ComplexNumber.get(tmp * Math.cos(theta), tmp * Math.sin(theta));
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
        BaseNumber number = RealNumber.get(value).sqrt();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber root(BaseNumber exp) {
        BaseNumber number = RealNumber.get(value).root(exp);
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber factorial() {
        if (value > 20) {
            value++;
            double tmp1 = Math.sqrt(2.0 * Math.PI / value);
            double tmp2 = value + 1.0 / (12.0 * value - 1.0 / (10.0 * value));
            tmp2 = Math.pow(tmp2 / Math.E, value);
            tmp2 *= tmp1;

            put(this);
            return RealNumber.get(tmp2);
        } else if (value > 0) {
            long sum = 1;

            for (int i = 1; i <= value; i++) {
                sum *= i;
            }

            value = sum;
            return this;
        } else if (value == 0) {
            value = 1;
            return this;
        }

        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber sin() {
        if (value == 0) {
            return this;
        }
        BaseNumber number = RealNumber.get(value).sin();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber cos() {
        if (value == 0) {
            value = 1;
            return this;
        }
        BaseNumber number = RealNumber.get(value).cos();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber tan() {
        if (value == 0) {
            return this;
        }
        BaseNumber number = RealNumber.get(value).tan();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber cot() {
        if (value == 0) {
            put(this);
            return InvalidNumber.get();
        }
        BaseNumber number = RealNumber.get(value).cot();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber sec() {
        if (value == 0) {
            value = 1;
            return this;
        }
        BaseNumber number = RealNumber.get(value).sec();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber csc() {
        if (value == 0) {
            put(this);
            return InvalidNumber.get();
        }
        BaseNumber number = RealNumber.get(value).csc();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber asin() {
        if (value == 0) {
            return this;
        }
        if (value == 1) {
            put(this);
            return RealNumber.get(M_PI2);
        }
        if (value == -1) {
            put(this);
            return RealNumber.get(-M_PI2);
        }
        BaseNumber number = RealNumber.get(value).asin();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber acos() {
        if (value == 0) {
            put(this);
            return RealNumber.get(M_PI2);
        }
        if (value == 1) {
            value = 0;
            return this;
        }
        if (value == -1) {
            put(this);
            return ConstantNumber.get(ConstantType.PI);
        }
        BaseNumber number = RealNumber.get(value).acos();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber atan() {
        if (value == 0) {
            return this;
        }
        BaseNumber number = RealNumber.get(value).atan();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber acot() {
        if (value == 0) {
            BaseNumber tmp = RealNumber.get(M_PI2);
            put(this);
            return tmp;
        }
        BaseNumber number = RealNumber.get(value).acot();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber asec() {
        if (value == 0) {
            put(this);
            return InvalidNumber.get();
        }
        if (value == 1) {
            value = 0;
            return this;
        }
        if (value == -1) {
            put(this);
            return ConstantNumber.get(ConstantType.PI);
        }
        BaseNumber number = RealNumber.get(value).asec();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber acsc() {
        if (value == 0) {
            put(this);
            return InvalidNumber.get();
        }
        if (value == 1) {
            put(this);
            return RealNumber.get(M_PI2);
        }
        if (value == -1) {
            put(this);
            return RealNumber.get(-M_PI2);
        }
        BaseNumber number = RealNumber.get(value).acsc();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber sinh() {
        if (value == 0) {
            return this;
        }
        BaseNumber number = RealNumber.get(value).sinh();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber cosh() {
        if (value == 0) {
            value = 1;
            return this;
        }
        BaseNumber number = RealNumber.get(value).cosh();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber tanh() {
        if (value == 0) {
            return this;
        }
        BaseNumber number = RealNumber.get(value).tanh();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber coth() {
        if (value == 0) {
            put(this);
            return InvalidNumber.get();
        }
        BaseNumber number = RealNumber.get(value).coth();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber sech() {
        if (value == 0) {
            value = 1;
            return this;
        }
        BaseNumber number = RealNumber.get(value).sech();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber csch() {
        if (value == 0) {
            put(this);
            return InvalidNumber.get();
        }
        BaseNumber number = RealNumber.get(value).csch();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber asinh() {
        if (value == 0) {
            return this;
        }
        BaseNumber number = RealNumber.get(value).asinh();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber acosh() {
        if (value == 0) {
            BaseNumber tmp = ComplexNumber.get(0, M_PI2);
            put(this);
            return tmp;
        }
        if (value == 1) {
            value = 0;
            return this;
        }
        if (value == -1) {
            BaseNumber tmp = ComplexNumber.get(0, Math.PI);
            put(this);
            return tmp;
        }
        BaseNumber number = RealNumber.get(value).acosh();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber atanh() {
        if (value == 0) {
            return this;
        }
        if (value == 1) {
            BaseNumber tmp = RealNumber.get(Double.POSITIVE_INFINITY);
            put(this);
            return tmp;
        }
        if (value == -1) {
            BaseNumber tmp = RealNumber.get(Double.NEGATIVE_INFINITY);
            put(this);
            return tmp;
        }
        BaseNumber number = RealNumber.get(value).atanh();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber acoth() {
        if (value == 0) {
            BaseNumber tmp = ComplexNumber.get(0, Math.PI / 2.0);
            put(this);
            return tmp;
        }
        if (value == 1) {
            BaseNumber tmp = RealNumber.get(Double.POSITIVE_INFINITY);
            put(this);
            return tmp;
        }
        if (value == -1) {
            BaseNumber tmp = RealNumber.get(Double.NEGATIVE_INFINITY);
            put(this);
            return tmp;
        }
        BaseNumber number = RealNumber.get(value).acoth();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber asech() {
        if (value == 1) {
            value = 0;
            return this;
        }
        if (value == 0) {
            put(this);
            return RealNumber.get(Double.POSITIVE_INFINITY);
        }
        if (value == -1) {
            put(this);
            return ComplexNumber.get(0, Math.PI);
        }
        BaseNumber number = RealNumber.get(value).asech();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber acsch() {
        if (value == 0) {
            put(this);
            return InvalidNumber.get();
        }
        BaseNumber number = RealNumber.get(value).acsch();
        put(this);
        return simplify(number);
    }

    @Override
    public BaseNumber and(BaseNumber number) {
        switch (number.type) {
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value &= integerNumber.value;
                put(number);
                return this;
            }
            case INVALID:
            case REAL:
            case CONSTANT:
            case COMPLEX:
            case MATRIX:
                put(number);
                put(this);
                return InvalidNumber.get();
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber or(BaseNumber number) {
        switch (number.type) {
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value |= integerNumber.value;
                put(number);
                return this;
            }
            case INVALID:
            case REAL:
            case CONSTANT:
            case COMPLEX:
            case MATRIX:
                put(number);
                put(this);
                return InvalidNumber.get();
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber xor(BaseNumber number) {
        switch (number.type) {
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value ^= integerNumber.value;
                put(number);
                return this;
            }
            case INVALID:
            case REAL:
            case CONSTANT:
            case COMPLEX:
            case MATRIX:
                put(number);
                put(this);
                return InvalidNumber.get();
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber not() {
        value = ~value;
        return this;
    }

    @Override
    public BaseNumber shl(BaseNumber number) {
        switch (number.type) {
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;

                if (integerNumber.value < 64) {
                    value <<= integerNumber.value;
                } else {
                    value = 0; // overflow
                }

                put(number);
                return this;
            }
            case INVALID:
            case REAL:
            case CONSTANT:
            case COMPLEX:
            case MATRIX:
                put(number);
                put(this);
                return InvalidNumber.get();
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber shr(BaseNumber number) {
        switch (number.type) {
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                value >>>= integerNumber.value;
                put(number);
                return this;
            }
            case INVALID:
            case REAL:
            case CONSTANT:
            case COMPLEX:
            case MATRIX:
                put(number);
                put(this);
                return InvalidNumber.get();
        }

        throw new IllegalStateException();
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
        value = 1;
        return this;
    }

    @Override
    public BaseNumber gauss() {
        value = 0;
        return this;
    }

    @Override
    public BaseNumber rank() {
        if (value != 0) {
            value = 1;
        }

        return this;
    }

    @Override
    public BaseNumber inv() {
        if (value != 0) {
            return IntegerNumber.get(1).div(this);
        } else {
            put(this);
            return InvalidNumber.get();
        }
    }

    @Override
    public BaseNumber toRadians(AngleType angle) {
        if (angle == AngleType.RAD) {
            return this;
        }

        RealNumber realNumber = RealNumber.get(value * angle.toValue);
        put(this);
        return simplify(realNumber);
    }

    @Override
    public BaseNumber fromRadians(AngleType angle) {
        if (angle == AngleType.RAD) {
            return this;
        }

        RealNumber realNumber = RealNumber.get(value * angle.fromValue);
        put(this);
        return simplify(realNumber);
    }

    private static long addChecked(final long s, final long d) throws ArithmeticException {
        long r = s + d;
        if (((s ^ r) & (d ^ r)) < 0) {
            throw new ArithmeticException("Long overflow add(" + s + ", " + d + ")");
        }
        return r;
    }

    private static long subChecked(final long s, final long d) throws ArithmeticException {
        long r = s - d;
        if (((s ^ d) & (s ^ r)) < 0) {
            throw new ArithmeticException("Long underflow sub(" + s + ", " + d + ")");
        }
        return r;
    }

    private static long mulChecked(final long s, final long d) {
        long r = s * d;
        long ax = Math.abs(s);
        long ay = Math.abs(d);
        if (((ax | ay) >>> 31 != 0)) {
            if (((d != 0) && (r / d != s)) || (s == Long.MIN_VALUE && d == -1)) {
                throw new ArithmeticException("Long overflow mul(" + s + ", " + d + ")");
            }
        }
        return r;
    }

    private long intPower(long base, long exp) {
        long result = 1;
        while (exp != 0) {
            if ((exp & 1) == 1) {
                result *= base;
            }

            exp >>= 1;
            base *= base;
        }

        return result;
    }
}
