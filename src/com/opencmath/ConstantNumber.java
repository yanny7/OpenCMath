package com.opencmath;

class ConstantNumber extends BaseNumber {
    private static final PoolTemplate<ConstantNumber> pool = new PoolTemplate<>(100, 100000, new PoolFactory<ConstantNumber>() {
        @Override
        public ConstantNumber create() {
            return new ConstantNumber();
        }
    });

    ConstantType value;

    private ConstantNumber() {
        super(NumberType.CONSTANT);
        value = ConstantType.PI;
    }

    static ConstantNumber get(ConstantType value) {
        ConstantNumber number = pool.get();
        number.value = value;
        return number;
    }

    static void put(ConstantNumber item) {
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

        ConstantNumber number = (ConstantNumber) obj;
        return (value == number.value);
    }

    double getValue() {
        switch (value) {
            case PI:
                return Math.PI;
            case E:
                return Math.E;
        }

        throw new IllegalStateException();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public BaseNumber add(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;

                if (integerNumber.value == 0) {
                    put(number);
                    return this;
                }

                RealNumber realNumber = RealNumber.get(getValue() + integerNumber.value);
                put(number);
                put(this);
                return simplify(realNumber);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value += getValue();
                put(this);
                return simplify(realNumber);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re += getValue();
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                RealNumber realNumber = RealNumber.get(getValue() + constantNumber.getValue());
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

                if (integerNumber.value == 0) {
                    put(number);
                    return this;
                }

                RealNumber realNumber = RealNumber.get(getValue() - integerNumber.value);
                put(number);
                put(this);
                return simplify(realNumber);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value = getValue() - realNumber.value;
                put(this);
                return simplify(realNumber);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re = getValue() - complexNumber.re;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                RealNumber realNumber = RealNumber.get(getValue() - constantNumber.getValue());
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
                RealNumber realNumber = RealNumber.get(getValue() * integerNumber.value);
                put(number);
                put(this);
                return simplify(realNumber);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value = getValue() * realNumber.value;
                put(this);
                return simplify(realNumber);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                complexNumber.re *= getValue();
                complexNumber.im *= getValue();
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                RealNumber realNumber = RealNumber.get(getValue() * constantNumber.getValue());
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
                RealNumber realNumber = RealNumber.get(getValue() / integerNumber.value);
                put(number);
                put(this);
                return simplify(realNumber);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                realNumber.value = getValue() / realNumber.value;
                put(this);
                return simplify(realNumber);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                double mul = complexNumber.re * complexNumber.re + complexNumber.im * complexNumber.im;
                complexNumber.re = (getValue() * complexNumber.re) / mul;
                complexNumber.im = (-getValue() * complexNumber.im) / mul;
                put(this);
                return simplify(number);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                RealNumber realNumber = RealNumber.get(getValue() / constantNumber.getValue());
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
        put(this);
        return getZero();
    }

    @Override
    public BaseNumber abs() {
        return this;
    }

    @Override
    public BaseNumber ln() {
        RealNumber tmp = RealNumber.get(Math.log(getValue()));
        put(this);
        return simplify(tmp);
    }

    @Override
    public BaseNumber log(BaseNumber number) {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.log(number));
    }

    @Override
    public BaseNumber exp() {
        RealNumber realNumber = RealNumber.get(Math.exp(getValue()));
        put(this);
        return simplify(realNumber);
    }

    @Override
    public BaseNumber pow(BaseNumber number) {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.pow(number));
    }

    @Override
    public BaseNumber sqrt() {
        RealNumber realNumber = RealNumber.get(Math.sqrt(getValue()));
        put(this);
        return simplify(realNumber);
    }

    @Override
    public BaseNumber root(BaseNumber number) {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.root(number));
    }

    @Override
    public BaseNumber factorial() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.factorial());
    }

    @Override
    public BaseNumber sin() {
        switch (value) {
            case PI: {
                put(this);
                return getZero();
            }
            case E: {
                RealNumber realNumber = RealNumber.get(getValue());
                put(this);
                return simplify(realNumber.sin());
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber cos() {
        switch (value) {
            case PI: {
                put(this);
                return IntegerNumber.get(-1);
            }
            case E: {
                RealNumber realNumber = RealNumber.get(getValue());
                put(this);
                return simplify(realNumber.cos());
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber tan() {
        switch (value) {
            case PI: {
                put(this);
                return getZero();
            }
            case E: {
                RealNumber realNumber = RealNumber.get(getValue());
                put(this);
                return simplify(realNumber.tan());
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber cot() {
        switch (value) {
            case PI: {
                put(this);
                return InvalidNumber.get();
            }
            case E: {
                RealNumber realNumber = RealNumber.get(getValue());
                put(this);
                return simplify(realNumber.cot());
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber sec() {
        switch (value) {
            case PI: {
                put(this);
                return IntegerNumber.get(-1);
            }
            case E: {
                RealNumber realNumber = RealNumber.get(getValue());
                put(this);
                return simplify(realNumber.sec());
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber csc() {
        switch (value) {
            case PI: {
                put(this);
                return InvalidNumber.get();
            }
            case E: {
                RealNumber realNumber = RealNumber.get(getValue());
                put(this);
                return simplify(realNumber.csc());
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber asin() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.asin());
    }

    @Override
    public BaseNumber acos() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.acos());
    }

    @Override
    public BaseNumber atan() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.atan());
    }

    @Override
    public BaseNumber acot() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.acot());
    }

    @Override
    public BaseNumber asec() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.asec());
    }

    @Override
    public BaseNumber acsc() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.acsc());
    }

    @Override
    public BaseNumber sinh() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.sinh());
    }

    @Override
    public BaseNumber cosh() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.cosh());
    }

    @Override
    public BaseNumber tanh() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.tanh());
    }

    @Override
    public BaseNumber coth() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.coth());
    }

    @Override
    public BaseNumber sech() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.sech());
    }

    @Override
    public BaseNumber csch() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.csch());
    }

    @Override
    public BaseNumber asinh() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.asinh());
    }

    @Override
    public BaseNumber acosh() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.acosh());
    }

    @Override
    public BaseNumber atanh() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.atanh());
    }

    @Override
    public BaseNumber acoth() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.acoth());
    }

    @Override
    public BaseNumber asech() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.asech());
    }

    @Override
    public BaseNumber acsch() {
        RealNumber realNumber = RealNumber.get(getValue());
        put(this);
        return simplify(realNumber.acsch());
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

        RealNumber realNumber = RealNumber.get(getValue() * angle.toValue);
        put(this);
        return simplify(realNumber);
    }

    @Override
    public BaseNumber fromRadians(AngleType angle) {
        if (angle == AngleType.RAD) {
            return this;
        }

        RealNumber realNumber = RealNumber.get(getValue() * angle.fromValue);
        put(this);
        return simplify(realNumber);
    }
}
