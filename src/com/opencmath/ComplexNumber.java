package com.opencmath;

class ComplexNumber extends BaseNumber {
    private static final PoolTemplate<ComplexNumber> pool = new PoolTemplate<>(100, 100000, new PoolFactory<ComplexNumber>() {
        @Override
        public ComplexNumber create() {
            return new ComplexNumber();
        }
    });

    double re;
    double im;

    private ComplexNumber() {
        super(NumberType.COMPLEX);
    }

    static ComplexNumber get(double re, double im) {
        ComplexNumber number = pool.get();
        number.re = re;
        number.im = im;
        return number;
    }

    static void put(ComplexNumber item) {
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

        ComplexNumber number = (ComplexNumber) obj;

        if (re == 0) {
            return (compareRelative(im, number.im) || (im == number.im));
        }

        return ((compareRelative(im, number.im) || (im == number.im)) && (compareRelative(re, number.re) || (re == number.re)));
    }

    @Override
    public String toString() {
        return "[" + re + (im < 0.0 ? " - " : " + ") + Math.abs(im) + "i]";
    }

    @Override
    public BaseNumber add(BaseNumber number) {
        // http://mathworld.wolfram.com/ComplexAddition.html
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                re += integerNumber.value;
                put(number);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                re += realNumber.value;
                put(number);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                re += complexNumber.re;
                im += complexNumber.im;
                put(number);
                return simplify(this);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                re += constantNumber.getValue();
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
        // http://mathworld.wolfram.com/ComplexSubtraction.html
        // (a + bi) - (c + di) = (a-c) + (b-d)i
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                re -= integerNumber.value;
                put(number);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                re -= realNumber.value;
                put(number);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                re -= complexNumber.re;
                im -= complexNumber.im;
                put(number);
                return simplify(this);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                re -= constantNumber.getValue();
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
        // http://mathworld.wolfram.com/ComplexMultiplication.html
        // (a + bi)(c + di) = (ac - bd) + (ad + bc)i
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                re *= integerNumber.value;
                im *= integerNumber.value;
                put(number);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                re *= realNumber.value;
                im *= realNumber.value;
                put(number);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                double tmpRe = re * complexNumber.re - im * complexNumber.im;
                double tmpIm = re * complexNumber.im + im * complexNumber.re;
                re = tmpRe;
                im = tmpIm;
                put(number);
                return simplify(this);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                re *= constantNumber.getValue();
                im *= constantNumber.getValue();
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
        // http://mathworld.wolfram.com/ComplexDivision.html
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                double mul = integerNumber.value * integerNumber.value;
                re = (re * integerNumber.value) / mul;
                im = (im * integerNumber.value) / mul;
                put(number);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                double mul = realNumber.value * realNumber.value;
                re = (re * realNumber.value) / mul;
                im = (im * realNumber.value) / mul;
                put(number);
                return simplify(this);
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                double mul = complexNumber.re * complexNumber.re + complexNumber.im * complexNumber.im;
                double tmpRe = (re * complexNumber.re + im * complexNumber.im) / mul;
                double tmpIm = (im * complexNumber.re - re * complexNumber.im) / mul;
                re = tmpRe;
                im = tmpIm;
                put(number);
                return simplify(this);
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                double mul = constantNumber.getValue() * constantNumber.getValue();
                re = (re * constantNumber.getValue()) / mul;
                im = (im * constantNumber.getValue()) / mul;
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
        // http://mathworld.wolfram.com/ComplexArgument.html
        double value = Math.atan2(im, re);
        BaseNumber tmp = RealNumber.get(value);
        put(this);
        return simplify(tmp);
    }

    @Override
    public BaseNumber abs() {
        // http://mathworld.wolfram.com/ComplexModulus.html
        BaseNumber tmp = RealNumber.get(Math.sqrt(re * re + im * im));
        put(this);
        return simplify(tmp);
    }

    @Override
    public BaseNumber ln() {
        // http://mathworld.wolfram.com/NaturalLogarithm.html
        // log(a + bi) = ln(|a + bi|) + arg(a + bi)i
        double ln = Math.log(Math.sqrt(re * re + im * im));
        double arg = Math.atan2(im, re);
        re = ln;
        im = arg;
        return simplify(this);
    }

    @Override
    public BaseNumber log(BaseNumber base) {
        // http://mathworld.wolfram.com/Logarithm.html
        switch (base.type) {
            case INVALID:
                put(base);
                put(this);
                return InvalidNumber.get();
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) base;

                if (integerNumber.value == 0) {
                    put(this);
                    return base;
                } else if (integerNumber.value == 1) {
                    put(base);
                    put(this);
                    return InvalidNumber.get();
                }

                return simplify(ln().div(base.ln()));
            }
            case REAL:
            case COMPLEX:
            case CONSTANT:
            case MATRIX:
                return simplify(ln().div(base.ln()));
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber exp() {
        // http://mathworld.wolfram.com/ExponentialFunction.html
        // exp(a + bi) = exp(a)cos(b) + exp(a)sin(b)i
        double exp = Math.exp(re);
        re = exp * Math.cos(im);
        im = exp * Math.sin(im);
        return simplify(this);
    }

    @Override
    public BaseNumber pow(BaseNumber exp) {
        // http://mathworld.wolfram.com/Power.html
        // y^x = exp(x*log(y))
        switch (exp.type) {
            case INVALID:
                put(exp);
                put(this);
                return InvalidNumber.get();
            case INTEGER:
            case REAL:
            case COMPLEX:
            case CONSTANT:
            case MATRIX:
                return simplify(ln().mul(exp).exp());
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber sqrt() {
        // http://mathworld.wolfram.com/SquareRoot.html
        double t = Math.sqrt((Math.abs(re) + Math.sqrt(re * re + im * im)) / 2.0);
        if (re >= 0) {
            re = t;
            im = im / (2 * t);
            return simplify(this);
        } else {
            re = Math.abs(im) / (2 * t);
            im = ((im >= 0) ? 1 : -1) * t;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber root(BaseNumber exp) {
        // http://mathworld.wolfram.com/nthRoot.html
        switch (exp.type) {
            case INVALID:
                put(exp);
                put(this);
                return InvalidNumber.get();
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) exp;
                double inv = 1.0 / integerNumber.value;
                double theta = Math.atan2(im, re);
                double w = Math.pow(Math.sqrt(re * re + im * im), inv);
                re = w * Math.cos(inv * theta);
                im = w * Math.sin(inv * theta);
                put(exp);
                return simplify(this);
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) exp;
                double inv = 1.0 / realNumber.value;
                double theta = Math.atan2(im, re);
                double w = Math.pow(Math.sqrt(re * re + im * im), inv);
                re = w * Math.cos(inv * theta);
                im = w * Math.sin(inv * theta);
                put(exp);
                return simplify(this);
            }
            case COMPLEX: {
                return simplify(ln().mul(getOne().div(exp)).exp());
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) exp;
                double inv = 1.0 / constantNumber.getValue();
                double theta = Math.atan2(im, re);
                double w = Math.pow(Math.sqrt(re * re + im * im), inv);
                re = w * Math.cos(inv * theta);
                im = w * Math.sin(inv * theta);
                put(exp);
                return simplify(this);
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
        // http://mathworld.wolfram.com/GammaFunction.html
        // http://my.fit.edu/~gabdo/gamma.txt --- Lanczos gamma function coefficients
        /*
         * Lanczos gamma function approximation
         *
         *        |                             |
         *        |sqrt(2 PI) |      8    p(n) ||      (z+0.5) -(z+5.5)
         * Γ(z) = |---------- |p(0)+sum -------||(z+5.5)      e
         *        |     z     |     n=1  z + n ||
         *        |                             |
         *
         *         ----------           -------  ------------ ---------
         *              A                 D-K         B           C
         */
        add(getOne());
        BaseNumber tmp = duplicate(this).add(RealNumber.get(7.5));
        BaseNumber A = RealNumber.get(2 * Math.PI).sqrt().div(duplicate(this));
        BaseNumber B = duplicate(tmp).pow(duplicate(this).add(RealNumber.get(0.5)));
        BaseNumber C = RealNumber.get(Math.E).pow(IntegerNumber.get(0).sub(tmp));
        BaseNumber D = RealNumber.get(676.520368121885098567009190444019).div(duplicate(this).add(IntegerNumber.get(1)));
        BaseNumber E = RealNumber.get(-1259.13921672240287047156078755283).div(duplicate(this).add(IntegerNumber.get(2)));
        BaseNumber F = RealNumber.get(771.3234287776530788486528258894).div(duplicate(this).add(IntegerNumber.get(3)));
        BaseNumber G = RealNumber.get(-176.61502916214059906584551354).div(duplicate(this).add(IntegerNumber.get(4)));
        BaseNumber H = RealNumber.get(12.507343278686904814458936853).div(duplicate(this).add(IntegerNumber.get(5)));
        BaseNumber I = RealNumber.get(-0.13857109526572011689554707).div(duplicate(this).add(IntegerNumber.get(6)));
        BaseNumber J = RealNumber.get(9.984369578019570859563e-6).div(duplicate(this).add(IntegerNumber.get(7)));
        BaseNumber K = RealNumber.get(1.50563273514931155834e-7).div(duplicate(this).add(IntegerNumber.get(8)));
        BaseNumber ret = A.mul(RealNumber.get(0.99999999999980993227684700473478).add(D.add(E.add(F.add(G.add(H.add(I.add(J.add(K))))))))).mul(B).mul(C);
        put(this);
        return simplify(ret);
    }

    @Override
    public BaseNumber sin() {
        // http://mathworld.wolfram.com/Sine.html
        // sin(a + bi) = sin(a)cosh(b) + cos(a)sinh(b)i
        double tmpRe = Math.sin(re) * Math.cosh(im);
        double tmpIm = Math.cos(re) * Math.sinh(im);
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber cos() {
        // http://mathworld.wolfram.com/Cosine.html
        // cos(a + bi) = cos(a)cosh(b) - sin(a)sinh(b)i
        double tmpRe = Math.cos(re) * Math.cosh(im);
        double tmpIm = -Math.sin(re) * Math.sinh(im);
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber tan() {
        // http://mathworld.wolfram.com/Tangent.html
        // tan(a + bi) = sin(2a)/(cos(2a)+cosh(2b)) + [sinh(2b)/(cos(2a)+cosh(2b))]i
        double twoRe = 2 * re;
        double twoIm = 2 * im;
        double tmp = Math.cos(twoRe) + Math.cosh(twoIm);
        re = Math.sin(twoRe) / tmp;
        im = Math.sinh(twoIm) / tmp;
        return simplify(this);
    }

    @Override
    public BaseNumber cot() {
        // http://mathworld.wolfram.com/Cotangent.html
        // cot(a + bi) = -sin(2a)/(cos(2a)-cosh(2b)) + [sinh(2b)/(cos(2a)-cosh(2b))]i
        double twoRe = 2 * re;
        double twoIm = 2 * im;
        double tmp = Math.cos(twoRe) - Math.cosh(twoIm);
        re = -Math.sin(twoRe) / tmp;
        im = Math.sinh(twoIm) / tmp;
        return simplify(this);
    }

    @Override
    public BaseNumber sec() {
        // http://mathworld.wolfram.com/Secant.html
        // sec(a + bi) = (2*cos(a)cos(b))/(cos(2a)+cosh(2b)) + [(2 * sin(a)sinh(b))/(cos(2a)+cosh(2b))]i
        double tmp = Math.cos(2 * re) + Math.cosh(2 * im);
        double tmpRe = (2 * Math.cos(re) * Math.cosh(im)) / tmp;
        double tmpIm = (2 * Math.sin(re) * Math.sinh(im)) / tmp;
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber csc() {
        // http://mathworld.wolfram.com/Cosecant.html
        // csc(a + bi) = -(2*sin(a)cosh(b))/(cos(2a)cosh(2b)) + [(2*cos(a)sinh(b))/(cos(2a)cosh(2b))]i
        double tmp = Math.cos(2 * re) - Math.cosh(2 * im);
        double tmpRe = -(2 * Math.sin(re) * Math.cosh(im)) / tmp;
        double tmpIm = (2 * Math.cos(re) * Math.sinh(im)) / tmp;
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber asin() {
        // http://mathworld.wolfram.com/InverseSine.html
        // asin(z) = -i (log(sqrt(1 - z^2) + iz))
        BaseNumber a = getOne().sub(duplicate(this).mul(duplicate(this))).sqrt(); // sqrt(1 - z^2)
        BaseNumber b = a.add(duplicate(this).mul(getComplexOne())).ln().mul(getComplexMinusOne()); // -i (log(a + iz))
        put(this);
        return simplify(b);
    }

    @Override
    public BaseNumber acos() {
        // http://mathworld.wolfram.com/InverseCosine.html
        // acos(z) = -i (log(z + i (sqrt(1 - z^2))))
        BaseNumber a = getOne().sub(duplicate(this).mul(duplicate(this))).sqrt().mul(getComplexOne()); // i (sqrt(1 - z^2))
        BaseNumber b = duplicate(this).add(a).ln().mul(getComplexMinusOne()); // -i (log(z + a))
        put(this);
        return simplify(b);
    }

    @Override
    public BaseNumber atan() {
        // http://mathworld.wolfram.com/InverseTangent.html
        // re(atan(x + iy)) = (1/2 atan2(x, 1 - y) - 1/2 atan2(-x, 1 + y)) * (Sign of Im if Re = 0)
        // im(atan(x + iy)) = 1/4 ln(x^2 + (y + 1)^2) - 1/4 ln(x^2 + (1 - y)^2)
        if ((re == 0) && ((im == 1) || (im == -1))) { // undefined for atan(i), atan(-i)
            put(this);
            return InvalidNumber.get();
        }

        double twoRe = re * re;
        double A = 1 + im;
        double B = 1 - im;
        re = (0.5 * Math.atan2(re, B) - 0.5 * Math.atan2(-re, A)) * (((re == 0) && (im < 0)) ? -1 : 1);
        im = 0.25 * Math.log(twoRe + A * A) - 0.25 * Math.log(twoRe + B * B);
        return simplify(this);
    }

    @Override
    public BaseNumber acot() {
        // http://mathworld.wolfram.com/InverseCotangent.html
        // re(acot(x + iy)) = 1/2 atan2(x/(x^2 + y^2), y/(x^2 + y^2) + 1) - 1/2 atan2(-x/(x^2 + y^2), 1 - y/(x^2 + y^2))
        // im(acot(x + iy)) = 1/4 ln((x^2)/(x^2 + y^2)^2 + (1 - y/(x^2 + y^2))^2) - 1/4 ln ((x^2)/(x^2 + y^2)^2 + (y/(x^2 + y^2) + 1)^2)
        if ((re == 0) && ((im == -1) || (im == 1))) { // undefined for atan(i), atan(-i)
            put (this);
            return InvalidNumber.get();
        }

        double twoRe = re * re;
        double pow = twoRe + im * im;
        double len = pow * pow;
        double A = im / pow;
        double B = twoRe / len;
        double C = re / pow;
        re = 0.5 * Math.atan2(C, A + 1) - 0.5 * Math.atan2(-C, 1 - A);
        im = 0.25 * Math.log(B + (1 - A) * (1 - A)) -  0.25 * Math.log(B + (1 + A) * (1 + A));
        return simplify(this);
    }

    @Override
    public BaseNumber asec() {
        // http://mathworld.wolfram.com/InverseSecant.html
        // asec(z) = 1/2 PI + i ln(sqrt(1 - 1/(z^2)) + i/z)
        // asec(z) = acos(1/z) --- USED
        return simplify(getOne().div(this).acos());
    }

    @Override
    public BaseNumber acsc() {
        // http://mathworld.wolfram.com/InverseCosecant.html
        // acsc(z) = -i ln(sqrt(1 - 1/(z^2)) + i/z)
        // acsc(z) = asin(1/z) --- USED
        return simplify(getOne().div(this).asin());
    }

    @Override
    public BaseNumber sinh() {
        // http://mathworld.wolfram.com/HyperbolicSine.html
        // sinh(a + bi) = sinh(a)cos(b) + cosh(a)sin(b)i
        double tmpRe = Math.sinh(re) * Math.cos(im);
        double tmpIm = Math.cosh(re) * Math.sin(im);
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber cosh() {
        // http://mathworld.wolfram.com/HyperbolicCosine.html
        // cosh(a + bi) = cosh(a)cos(b) + sinh(a)sin(b)i
        double tmpRe = Math.cosh(re) * Math.cos(im);
        double tmpIm = Math.sinh(re) * Math.sin(im);
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber tanh() {
        // http://mathworld.wolfram.com/HyperbolicTangent.html
        // tan(a + bi) = sinh(2a)/(cosh(2a)+cos(2b)) + [sin(2b)/(cosh(2a)+cos(2b))]i
        double twoRe = 2 * re;
        double twoIm = 2 * im;
        double tmp = Math.cosh(twoRe) + Math.cos(twoIm);
        re = Math.sinh(twoRe) / tmp;
        im = Math.sin(twoIm) / tmp;
        return simplify(this);
    }

    @Override
    public BaseNumber coth() {
        // http://mathworld.wolfram.com/HyperbolicCotangent.html
        // coth(a + bi) = -sinh(2a)/(cos(2b) - cosh(2a)) + [sin(2b)/(cos(2b) - cosh(2a))]i
        double twoRe = 2 * re;
        double twoIm = 2 * im;
        double tmp = Math.cos(twoIm) - Math.cosh(twoRe);
        re = -Math.sinh(twoRe) / tmp;
        im = Math.sin(twoIm) / tmp;
        return simplify(this);
    }

    @Override
    public BaseNumber sech() {
        // http://mathworld.wolfram.com/HyperbolicSecant.html
        // sech(a + bi) = (2 cosh(a)cos(b))/(cosh(2a) + cos(2b)) - [(2 sinh(a)sin(b))/(cosh(2a) + cos(2b))]i
        double tmp = Math.cosh(2 * re) + Math.cos(2 * im);
        double tmpRe = (2 * Math.cosh(re) * Math.cos(im)) / tmp;
        double tmpIm = -(2 * Math.sinh(re) * Math.sin(im)) / tmp;
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber csch() {
        // http://mathworld.wolfram.com/HyperbolicCosecant.html
        // csch(a + bi) = -(2 sinh(a)cos(b))/(cos(2b) - cosh(2a)) + [(2 cosh(a)sin(b))/(cos(2b) - cosh(2a))]i
        double tmp = Math.cos(2 * im) - Math.cosh(2 * re);
        double tmpRe = -(2 * Math.sinh(re) * Math.cos(im)) / tmp;
        double tmpIm = (2 * Math.cosh(re) * Math.sin(im)) / tmp;
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber asinh() {
        // http://mathworld.wolfram.com/InverseHyperbolicSine.html
        // asinh(z) = ln(z + sqrt(1 + z^2))
        BaseNumber pow = duplicate(this).mul(duplicate(this));
        BaseNumber tmp = this.add(getOne().add(pow).sqrt()).ln();
        return simplify(tmp);
    }

    @Override
    public BaseNumber acosh() {
        // http://mathworld.wolfram.com/InverseHyperbolicCosine.html
        // acosh(z) = ln(z + sqrt(z + 1)sqrt(z - 1))
        BaseNumber tmp = duplicate(this).add(duplicate(this).add(getOne()).sqrt().mul(duplicate(this).sub(getOne()).sqrt())).ln();
        put(this);
        return simplify(tmp);
    }

    @Override
    public BaseNumber atanh() {
        // http://mathworld.wolfram.com/InverseHyperbolicTangent.html
        // re(atanh(x + iy)) = 1/4 ln((x + 1)^2 + y^2) - 1/4 ln ((1 - x)^2 + y^2)
        // im(atanh(x + iy)) = 1/2 atan2(y, 1 + x) - 1/2 atan2(-y, 1 - x)
        double Im2 = im * im;
        double A = 1 + re;
        double B = 1 - re;
        double tmpRe = 0.25 * Math.log(A * A + Im2) - 0.25 * Math.log(B * B + Im2);
        double tmpIm = 0.5 * Math.atan2(im, A) - 0.5 * Math.atan2(-im, B);
        re = tmpRe;
        im = tmpIm;
        return simplify(this);
    }

    @Override
    public BaseNumber acoth() {
        // http://mathworld.wolfram.com/InverseHyperbolicCotangent.html
        // re(acoth(x + iy)) = 1/4 ln((y^2)/(x^2 + y^2)^2 + (x/(x^2 + y^2) + 1)^2) - 1/4 ln ((y^2)/(x^2 + y^2)^2 + (1 - x/(x^2 + y^2))^2)
        // im(acoth(x + iy)) = 1/2 atan2(-y/(x^2 + y^2), x/(x^2 + y^2) + 1) - 1/2 atan2(y/(x^2 + y^2), 1 - x/(x^2 + y^2))
        double twoRe = re * re;
        double twoIm = im * im;
        double sum = twoRe + twoIm;
        double pow = sum * sum;
        double A = 1 + re / sum;
        double B = 1 - re / sum;
        double C = im / sum;
        double D = twoIm / pow;
        re = 0.25 * Math.log(D + A * A) - 0.25 * Math.log(D + B * B);
        im = 0.5 * Math.atan2(-C, A) - 0.5 * Math.atan2(C, B);
        return simplify(this);
    }

    @Override
    public BaseNumber asech() {
        // http://mathworld.wolfram.com/InverseHyperbolicSecant.html
        // asech(z) = ln(sqrt(1/z - 1)sqrt(1/z + 1) + 1/z)
        BaseNumber invZ = getOne().div(this); // 1/z
        BaseNumber tmp = duplicate(invZ).add(duplicate(invZ).sub(getOne()).sqrt().mul(duplicate(invZ).add(getOne()).sqrt())).ln();
        put(invZ);
        return simplify(tmp);
    }

    @Override
    public BaseNumber acsch() {
        // http://mathworld.wolfram.com/InverseHyperbolicCosecant.html
        // acsch(z) = ln(sqrt(1/(z^2) + 1) + 1/z)
        BaseNumber pow = duplicate(this).mul(duplicate(this));
        BaseNumber tmp = getOne().add(getOne().div(pow)).sqrt().add(getOne().div(this)).ln();
        return simplify(tmp);
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

        re *= angle.toValue;
        im *= angle.toValue;
        return simplify(this);
    }

    @Override
    public BaseNumber fromRadians(AngleType angle) {
        if (angle == AngleType.RAD) {
            return this;
        }

        re *= angle.fromValue;
        im *= angle.fromValue;
        return simplify(this);
    }
}
