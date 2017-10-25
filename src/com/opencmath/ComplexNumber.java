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
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) exp;

                if (integerNumber.value == 0) {
                    put(this);
                    integerNumber.value = 1;
                    return integerNumber;
                }

                if (re == 0) {
                    if (integerNumber.value % 2 != 0) {
                        im = Math.pow(im, integerNumber.value) * (im > 0 ? 1 : -1);
                        put(exp);
                        return this;
                    } else {
                        BaseNumber tmp = RealNumber.get(Math.pow(im, integerNumber.value) * (im > 0 ? 1 : -1));
                        put(this);
                        put(exp);
                        return simplify(tmp);
                    }
                } else {
                    double pow = Math.pow(re * re + im * im, integerNumber.value / 2.0);
                    double arg = integerNumber.value * Math.atan2(im, re);
                    double tmpRe = pow * Math.cos(arg);
                    double tmpIm = pow * Math.sin(arg);
                    re = tmpRe;
                    im = tmpIm;
                    put(exp);
                    return this;
                }
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) exp;
                double pow = Math.pow(re * re + im * im, realNumber.value / 2.0);
                double arg = realNumber.value * Math.atan2(im, re);
                double tmpRe = pow * Math.cos(arg);
                double tmpIm = pow * Math.sin(arg);
                re = tmpRe;
                im = tmpIm;
                put(exp);
                return this;
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) exp;
                double pow = Math.pow(re * re + im * im, constantNumber.value.value / 2.0);
                double arg = constantNumber.value.value * Math.atan2(im, re);
                double tmpRe = pow * Math.cos(arg);
                double tmpIm = pow * Math.sin(arg);
                re = tmpRe;
                im = tmpIm;
                put(exp);
                return this;
            }
            case COMPLEX:
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
        if (re == 0) {
            im = Math.sinh(im);
            return this;
        } else {
            double tmpRe = Math.sin(re) * Math.cosh(im);
            double tmpIm = Math.cos(re) * Math.sinh(im);
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber cos() {
        // http://mathworld.wolfram.com/Cosine.html
        // cos(a + bi) = cos(a)cosh(b) - sin(a)sinh(b)i
        if (re == 0) {
            BaseNumber tmp = RealNumber.get(Math.cosh(im));
            put(this);
            return simplify(tmp);
        } else {
            double tmpRe = Math.cos(re) * Math.cosh(im);
            double tmpIm = -Math.sin(re) * Math.sinh(im);
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber tan() {
        // http://mathworld.wolfram.com/Tangent.html
        // tan(a + bi) = sin(2a)/(cos(2a)+cosh(2b)) + [sinh(2b)/(cos(2a)+cosh(2b))]i
        if (re == 0) {
            im = Math.tanh(im);
            return this;
        } else {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cos(twoRe) + Math.cosh(twoIm);
            re = Math.sin(twoRe) / tmp;
            im = Math.sinh(twoIm) / tmp;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber cot() {
        // http://mathworld.wolfram.com/Cotangent.html
        // cot(a + bi) = -sin(2a)/(cos(2a)-cosh(2b)) + [sinh(2b)/(cos(2a)-cosh(2b))]i
        if (re == 0) {
            double twoIm = 2 * im;
            im = Math.sinh(twoIm) / (1 - Math.cosh(twoIm));
            return this;
        } else {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cos(twoRe) - Math.cosh(twoIm);
            re = -Math.sin(twoRe) / tmp;
            im = Math.sinh(twoIm) / tmp;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber sec() {
        // http://mathworld.wolfram.com/Secant.html
        // sec(a + bi) = (2*cos(a)cos(b))/(cos(2a)+cosh(2b)) + [(2 * sin(a)sinh(b))/(cos(2a)+cosh(2b))]i
        if (re == 0) {
            BaseNumber tmp = RealNumber.get(2 * Math.cosh(im) / (1 + Math.cosh(2 * im)));
            put(this);
            return simplify(tmp);
        } else {
            double tmp = Math.cos(2 * re) + Math.cosh(2 * im);
            double tmpRe = (2 * Math.cos(re) * Math.cosh(im)) / tmp;
            double tmpIm = (2 * Math.sin(re) * Math.sinh(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber csc() {
        // http://mathworld.wolfram.com/Cosecant.html
        // csc(a + bi) = -(2*sin(a)cosh(b))/(cos(2a)cosh(2b)) + [(2*cos(a)sinh(b))/(cos(2a)cosh(2b))]i
        if (re == 0) {
            im = (2 * Math.sinh(im) / (1 - Math.cosh(2 * im)));
            return this;
        } else {
            double tmp = Math.cos(2 * re) - Math.cosh(2 * im);
            double tmpRe = -(2 * Math.sin(re) * Math.cosh(im)) / tmp;
            double tmpIm = (2 * Math.cos(re) * Math.sinh(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber asin() {
        // http://mathworld.wolfram.com/InverseSine.html
        // asin(z) = -i (log(sqrt(1 - z^2) + iz))
        // (1-z^2)
        if (re == 0) {
            im = Math.log(im + Math.sqrt(1 + im * im));
            return this;
        } else {
            double sqRe = 1 - re * re + im * im;
            double sqIm = -2 * re * im;
            // sqrt()
            double t = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqIm * sqIm)) / 2.0);
            double sqrtRe, sqrtIm;
            if (sqRe >= 0) {
                sqrtRe = t;
                sqrtIm = sqIm / (2 * t);
            } else {
                sqrtRe = Math.abs(sqIm) / (2 * t);
                sqrtIm = ((sqIm >= 0) ? 1 : -1) * t;
            }
            // log(sqrt()+iz)
            double tmpRe = -im + sqrtRe;
            double tmpIm = re + sqrtIm;
            double logRe = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            // -i
            re = Math.atan2(tmpIm, tmpRe);
            im = -logRe;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber acos() {
        // http://mathworld.wolfram.com/InverseCosine.html
        // acos(z) = -i (log(z + i (sqrt(1 - z^2))))
        // (1-z^2)
        if (re == 0) {
            re = M_PI2;
            im = -Math.log(im + Math.sqrt(1 + im * im));
            return this;
        } else {
            double sqRe = 1 - re * re + im * im;
            double sqIm = -2 * re * im;
            // sqrt()
            double t = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqIm * sqIm)) / 2.0);
            double sqrtRe, sqrtIm;
            if (sqRe >= 0) {
                sqrtRe = t;
                sqrtIm = sqIm / (2 * t);
            } else {
                sqrtRe = Math.abs(sqIm) / (2 * t);
                sqrtIm = ((sqIm >= 0) ? 1 : -1) * t;
            }
            // log(z+i*sqrt())
            double tmpRe = re - sqrtIm;
            double tmpIm = im + sqrtRe;
            double logRe = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            // -i
            re = Math.atan2(tmpIm, tmpRe);
            im = -logRe;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber atan() {
        // http://mathworld.wolfram.com/InverseTangent.html
        // re(atan(x + iy)) = (1/2 atan2(x, 1 - y) - 1/2 atan2(-x, 1 + y)) * (Sign of Im if Re = 0)
        // im(atan(x + iy)) = 1/4 ln(x^2 + (y + 1)^2) - 1/4 ln(x^2 + (1 - y)^2)
        if (re == 0) {
            if ((im == 1) || (im == -1)) { // undefined for atan(i), atan(-i)
                put(this);
                return InvalidNumber.get();
            }

            if ((im > -1) && (im < 1)) {
                im = 0.5 * Math.log(im + 1) - 0.5 * Math.log(1 - im);
                return this;
            }

            // re = 0.25*ln((x+1)^2)-0.25*ln((1-x)^2) im=(value>0) ? -PI/2 : PI/2
            double a = im + 1;
            double b = 1 - im;
            im = 0.25 * Math.log(a * a) - 0.25 * Math.log(b * b);
            re = (im > 0) ? M_PI2 : -M_PI2;
            return this;
        } else {
            double twoRe = re * re;
            double A = 1 + im;
            double B = 1 - im;
            re = (0.5 * Math.atan2(re, B) - 0.5 * Math.atan2(-re, A)) * (((re == 0) && (im < 0)) ? -1 : 1);
            im = 0.25 * Math.log(twoRe + A * A) - 0.25 * Math.log(twoRe + B * B);
            return simplify(this);
        }
    }

    @Override
    public BaseNumber acot() {
        // http://mathworld.wolfram.com/InverseCotangent.html
        // re(acot(x + iy)) = 1/2 atan2(x/(x^2 + y^2), y/(x^2 + y^2) + 1) - 1/2 atan2(-x/(x^2 + y^2), 1 - y/(x^2 + y^2))
        // im(acot(x + iy)) = 1/4 ln((x^2)/(x^2 + y^2)^2 + (1 - y/(x^2 + y^2))^2) - 1/4 ln ((x^2)/(x^2 + y^2)^2 + (y/(x^2 + y^2) + 1)^2)
        if (re == 0) {
            if ((im == -1) || (im == 1)) { // undefined for acot(i), acot(-i)
                put(this);
                return InvalidNumber.get();
            }

            if ((im < -1) || (im > 1)) { // re=0.5*ln(1/x+1)-0.5*ln(1-1/x) im=0
                double inv = 1 / im;
                im = 0.5 * Math.log(1 - inv) - 0.5 * Math.log(inv + 1);
                return this;
            }

            // re=0.25*ln((1/x+1)^2)-0.25*ln((1-1/x)^2) im=(value>0) ? -PI/2 : PI/2
            double inv = 1 / im;
            double a = inv + 1;
            double b = 1 - inv;
            re = (im > 0) ? -M_PI2 : M_PI2;
            im = 0.25 * Math.log(b * b) - 0.25 * Math.log(a * a);
            return this;
        } else {
            double twoRe = re * re;
            double pow = twoRe + im * im;
            double len = pow * pow;
            double A = im / pow;
            double B = twoRe / len;
            double C = re / pow;
            double D = 1 + A;
            double E = 1 - A;
            re = 0.5 * Math.atan2(C, D) - 0.5 * Math.atan2(-C, E);
            im = 0.25 * Math.log(B + E * E) - 0.25 * Math.log(B + D * D);
            return simplify(this);
        }
    }

    @Override
    public BaseNumber asec() {
        // http://mathworld.wolfram.com/InverseSecant.html
        // asec(z) = 1/2 PI + i ln(sqrt(1 - 1/(z^2)) + i/z)
        if (re == 0) {
            im = 1 / im;
            im = Math.log(im + Math.sqrt(1 + im * im));
            re = M_PI2;
            return this;
        } else {
            double powRe = re * re;
            double powIm = im * im;
            double powSum = powRe + powIm;
            double pow2 = powSum * powSum;
            double sqRe = powIm / pow2 - powRe / pow2 + 1;
            double sqIm = (2 * re * im) / pow2;
            // sqrt()
            double t = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqIm * sqIm)) / 2.0);
            double sqrtRe = 0, sqrtIm = 0;
            if (sqRe >= 0) {
                sqrtRe = t;
                sqrtIm = sqIm / (2 * t);
            }/* else { //unaccessible code due to expression y^2/(x^2+y^2)-x^2(x^2+y^2)+1 is always > 0

            sqrtRe = Math.abs(sqIm) / (2 * t);
            sqrtIm = ((sqIm >= 0) ? 1 : -1) * t;
        }*/
            // log(sqrt()+1/z)
            double tmpRe = im / powSum + sqrtRe;
            double tmpIm = re / powSum + sqrtIm;
            double logRe = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            double logIm = Math.atan2(tmpIm, tmpRe);
            re = M_PI2 - logIm;
            im = logRe;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber acsc() {
        // http://mathworld.wolfram.com/InverseCosecant.html
        // acsc(z) = -i ln(sqrt(1 - 1/(z^2)) + i/z)
        if (re == 0) {
            im = 1 / im;
            im = -Math.log(im + Math.sqrt(1 + im * im));
            return this;
        } else {
            double powRe = re * re;
            double powIm = im * im;
            double powSum = powRe + powIm;
            double pow2 = powSum * powSum;
            double sqRe = powIm / pow2 - powRe / pow2 + 1;
            double sqIm = (2 * re * im) / pow2;
            // sqrt()
            double t = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqIm * sqIm)) / 2.0);
            double sqrtRe = 0, sqrtIm = 0;
            if (sqRe >= 0) {
                sqrtRe = t;
                sqrtIm = sqIm / (2 * t);
            }/* else { //unaccessible code due to expression y^2/(x^2+y^2)-x^2(x^2+y^2)+1 is always > 0
                sqrtRe = Math.abs(sqIm) / (2 * t);
                sqrtIm = ((sqIm >= 0) ? 1 : -1) * t;
            }*/
            // log(sqrt()+1/z)
            double tmpRe = im / powSum + sqrtRe;
            double tmpIm = re / powSum + sqrtIm;
            double logRe = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            re = Math.atan2(tmpIm, tmpRe);
            im = -logRe;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber sinh() {
        // http://mathworld.wolfram.com/HyperbolicSine.html
        // sinh(a + bi) = sinh(a)cos(b) + cosh(a)sin(b)i
        if (re == 0) {
            im = Math.sin(im);
            return this;
        } else {
            double tmpRe = Math.sinh(re) * Math.cos(im);
            double tmpIm = Math.cosh(re) * Math.sin(im);
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber cosh() {
        // http://mathworld.wolfram.com/HyperbolicCosine.html
        // cosh(a + bi) = cosh(a)cos(b) + sinh(a)sin(b)i
        if (re == 0) {
            BaseNumber tmp = RealNumber.get(Math.cos(im));
            put(this);
            return simplify(tmp);
        } else {
            double tmpRe = Math.cosh(re) * Math.cos(im);
            double tmpIm = Math.sinh(re) * Math.sin(im);
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber tanh() {
        // http://mathworld.wolfram.com/HyperbolicTangent.html
        // tan(a + bi) = sinh(2a)/(cosh(2a)+cos(2b)) + [sin(2b)/(cosh(2a)+cos(2b))]i
        if (re == 0) {
            im = Math.tan(im);
            return this;
        } else {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cosh(twoRe) + Math.cos(twoIm);
            re = Math.sinh(twoRe) / tmp;
            im = Math.sin(twoIm) / tmp;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber coth() {
        // http://mathworld.wolfram.com/HyperbolicCotangent.html
        // coth(a + bi) = -sinh(2a)/(cos(2b) - cosh(2a)) + [sin(2b)/(cos(2b) - cosh(2a))]i
        if (re == 0) {
            im = -Math.cos(im) / Math.sin(im);
            return this;
        } else {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cos(twoIm) - Math.cosh(twoRe);
            re = -Math.sinh(twoRe) / tmp;
            im = Math.sin(twoIm) / tmp;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber sech() {
        // http://mathworld.wolfram.com/HyperbolicSecant.html
        // sech(a + bi) = (2 cosh(a)cos(b))/(cosh(2a) + cos(2b)) - [(2 sinh(a)sin(b))/(cosh(2a) + cos(2b))]i
        if (re == 0) {
            BaseNumber tmp = RealNumber.get(1.0 / Math.cos(im));
            put(this);
            return simplify(tmp);
        } else {
            double tmp = Math.cosh(2 * re) + Math.cos(2 * im);
            double tmpRe = (2 * Math.cosh(re) * Math.cos(im)) / tmp;
            double tmpIm = -(2 * Math.sinh(re) * Math.sin(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber csch() {
        // http://mathworld.wolfram.com/HyperbolicCosecant.html
        // csch(a + bi) = -(2 sinh(a)cos(b))/(cos(2b) - cosh(2a)) + [(2 cosh(a)sin(b))/(cos(2b) - cosh(2a))]i
        if (re == 0) {
            im = -1.0 / Math.sin(im);
            return this;
        } else {
            double tmp = Math.cos(2 * im) - Math.cosh(2 * re);
            double tmpRe = -(2 * Math.sinh(re) * Math.cos(im)) / tmp;
            double tmpIm = (2 * Math.cosh(re) * Math.sin(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber asinh() {
        // http://mathworld.wolfram.com/InverseHyperbolicSine.html
        // asinh(z) = ln(z + sqrt(1 + z^2))
        // 1 + z^2
        if (re == 0) {
            if (im > 1) {
                re = Math.log(Math.sqrt(im * im - 1) + im);
                im = M_PI2;
                return this;
            }
            if (im < -1) {
                re = -Math.log(Math.sqrt(im * im - 1) - im);
                im = -M_PI2;
                return this;
            }

            im = Math.asin(im);
            return this;
        } else {
            double sqRe = re * re - im * im + 1;
            double sqIm = 2 * re * im;
            // sqrt()
            double t = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqIm * sqIm)) / 2.0);
            double sqrtRe, sqrtIm;
            if (sqRe >= 0) {
                sqrtRe = t;
                sqrtIm = sqIm / (2 * t);
            } else {
                sqrtRe = Math.abs(sqIm) / (2 * t);
                sqrtIm = ((sqIm >= 0) ? 1 : -1) * t;
            }
            // log()
            double tmpRe = sqrtRe + re;
            double tmpIm = sqrtIm + im;
            re = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            im = Math.atan2(tmpIm, tmpRe);
            return simplify(this);
        }
    }

    @Override
    public BaseNumber acosh() {
        // http://mathworld.wolfram.com/InverseHyperbolicCosine.html
        // acosh(z) = ln(z + sqrt(z + 1)sqrt(z - 1))
        if (re == 0) {
            // sqrt1()
            double sqImPow = im * im;
            double t1 = Math.sqrt((1 + Math.sqrt(1 + sqImPow)) / 2.0);
            double sqrtIm1 = im / (2 * t1);
            // sqrt2()
            double t2 = Math.sqrt((1 + Math.sqrt(1 + sqImPow)) / 2.0);
            double sqrtRe2 = Math.abs(im) / (2 * t2);
            double sqrtIm2 = ((im >= 0) ? 1 : -1) * t2;
            // log(sqrt1()*sqrt2() + z)
            double tmpRe = t1 * sqrtRe2 - sqrtIm1 * sqrtIm2;
            double tmpIm = t1 * sqrtIm2 + sqrtIm1 * sqrtRe2 + im;
            re = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            im = (im > 0) ? M_PI2 : -M_PI2;
            return this;
        } else {
            // sqrt1()
            double sqRe = re + 1;
            double sqIm = im;
            double sqImPow = sqIm * sqIm;
            double t1 = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqImPow)) / 2.0);
            double sqrtRe1, sqrtIm1;
            if (sqRe >= 0) {
                sqrtRe1 = t1;
                sqrtIm1 = sqIm / (2 * t1);
            } else {
                sqrtRe1 = Math.abs(sqIm) / (2 * t1);
                sqrtIm1 = ((sqIm >= 0) ? 1 : -1) * t1;
            }
            // sqrt2()
            sqRe = re - 1;
            double t2 = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqImPow)) / 2.0);
            double sqrtRe2, sqrtIm2;
            if (sqRe >= 0) {
                sqrtRe2 = t2;
                sqrtIm2 = sqIm / (2 * t2);
            } else {
                sqrtRe2 = Math.abs(sqIm) / (2 * t2);
                sqrtIm2 = ((sqIm >= 0) ? 1 : -1) * t2;
            }
            // log(sqrt1()*sqrt2() + z)
            double tmpRe = sqrtRe1 * sqrtRe2 - sqrtIm1 * sqrtIm2 + re;
            double tmpIm = sqrtRe1 * sqrtIm2 + sqrtIm1 * sqrtRe2 + im;
            re = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            im = Math.atan2(tmpIm, tmpRe);
            return simplify(this);
        }
    }

    @Override
    public BaseNumber atanh() {
        // http://mathworld.wolfram.com/InverseHyperbolicTangent.html
        // re(atanh(x + iy)) = 1/4 ln((x + 1)^2 + y^2) - 1/4 ln ((1 - x)^2 + y^2)
        // im(atanh(x + iy)) = 1/2 atan2(y, 1 + x) - 1/2 atan2(-y, 1 - x)
        if (re == 0) {
            im = Math.atan(im);
            return this;
        } else {
            double Im2 = im * im;
            double A = 1 + re;
            double B = 1 - re;
            double tmpRe = 0.25 * Math.log(A * A + Im2) - 0.25 * Math.log(B * B + Im2);
            double tmpIm = 0.5 * Math.atan2(im, A) - 0.5 * Math.atan2(-im, B);
            re = tmpRe;
            im = tmpIm;
            return simplify(this);
        }
    }

    @Override
    public BaseNumber acoth() {
        // http://mathworld.wolfram.com/InverseHyperbolicCotangent.html
        // re(acoth(x + iy)) = 1/4 ln((y^2)/(x^2 + y^2)^2 + (x/(x^2 + y^2) + 1)^2) - 1/4 ln ((y^2)/(x^2 + y^2)^2 + (1 - x/(x^2 + y^2))^2)
        // im(acoth(x + iy)) = 1/2 atan2(-y/(x^2 + y^2), x/(x^2 + y^2) + 1) - 1/2 atan2(y/(x^2 + y^2), 1 - x/(x^2 + y^2))
        if (re == 0) {
            im = -Math.atan(1 / im);
            return this;
        } else {
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
    }

    @Override
    public BaseNumber asech() {
        // http://mathworld.wolfram.com/InverseHyperbolicSecant.html
        // asech(z) = ln(sqrt(1/z - 1)sqrt(1/z + 1) + 1/z)
        if (re == 0) {
            double sum = im * im;
            double imDiv = im / sum;
            double sqIm1 = -imDiv;
            double sqIm2 = -imDiv;
            // sqrt1()
            double t1 = Math.sqrt((1 + Math.sqrt(1 + sqIm1 * sqIm1)) / 2.0);
            double sqrtRe1 = Math.abs(sqIm1) / (2 * t1);
            double sqrtIm1 = ((sqIm1 >= 0) ? 1 : -1) * t1;
            // sqrt2()
            double t2 = Math.sqrt((1 + Math.sqrt(1 + sqIm2 * sqIm2)) / 2.0);
            double sqrtIm2 = sqIm2 / (2 * t2);
            // log()
            double tmpRe = sqrtRe1 * t2 - sqrtIm1 * sqrtIm2;
            double tmpIm = sqrtRe1 * sqrtIm2 + sqrtIm1 * t2 - imDiv;
            re = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            im = (im > 0) ? -M_PI2 : M_PI2;
            return simplify(this);
        } else {
            double re2 = re * re;
            double im2 = im * im;
            double sum = re2 + im2;
            double reDiv = re / sum;
            double imDiv = im / sum;
            double sqRe1 = reDiv - 1;
            double sqIm1 = -imDiv;
            double sqRe2 = reDiv + 1;
            double sqIm2 = -imDiv;
            // sqrt1()
            double t1 = Math.sqrt((Math.abs(sqRe1) + Math.sqrt(sqRe1 * sqRe1 + sqIm1 * sqIm1)) / 2.0);
            double sqrtRe1, sqrtIm1;
            if (sqRe1 >= 0) {
                sqrtRe1 = t1;
                sqrtIm1 = sqIm1 / (2 * t1);
            } else {
                sqrtRe1 = Math.abs(sqIm1) / (2 * t1);
                sqrtIm1 = ((sqIm1 >= 0) ? 1 : -1) * t1;
            }
            // sqrt2()
            double t2 = Math.sqrt((Math.abs(sqRe2) + Math.sqrt(sqRe2 * sqRe2 + sqIm2 * sqIm2)) / 2.0);
            double sqrtRe2, sqrtIm2;
            if (sqRe2 >= 0) {
                sqrtRe2 = t2;
                sqrtIm2 = sqIm2 / (2 * t2);
            } else {
                sqrtRe2 = Math.abs(sqIm2) / (2 * t2);
                sqrtIm2 = ((sqIm2 >= 0) ? 1 : -1) * t2;
            }
            // log()
            double tmpRe = sqrtRe1 * sqrtRe2 - sqrtIm1 * sqrtIm2 + reDiv;
            double tmpIm = sqrtRe1 * sqrtIm2 + sqrtIm1 * sqrtRe2 - imDiv;
            re = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            im = Math.atan2(tmpIm, tmpRe);
            return simplify(this);
        }
    }

    @Override
    public BaseNumber acsch() {
        // http://mathworld.wolfram.com/InverseHyperbolicCosecant.html
        // acsch(z) = ln(sqrt(1/(z^2) + 1) + 1/z)
        if (re == 0) {
            if ((im > 0) && (im < 1)) {
                double inv = 1 / im;
                re = -Math.log(Math.sqrt(inv * inv - 1) + inv);
                im = -M_PI2;
                return this;
            }
            if ((im > -1) && (im < 0)) {
                double inv = 1 / -im;
                re = Math.log(Math.sqrt(inv * inv - 1) + inv);
                im = M_PI2;
                return this;
            }

            im = -Math.asin(1.0 / im);
            return this;
        } else {
            double re2 = re * re;
            double im2 = im * im;
            double sum = re2 + im2;
            double sum2 = sum * sum;
            double sqRe = re2 / sum2 - im2 / sum2 + 1;
            double sqIm = -2 * re * im / sum2;
            // sqrt()
            double t = Math.sqrt((Math.abs(sqRe) + Math.sqrt(sqRe * sqRe + sqIm * sqIm)) / 2.0);
            double sqrtRe = 0, sqrtIm = 0;
            if (sqRe >= 0) {
                sqrtRe = t;
                sqrtIm = sqIm / (2 * t);
            }/* else { //unaccessible code due to expression x^2/(x^2+y^2)-y^2(x^2+y^2)+1 is always > 0
                sqrtRe = Math.abs(sqIm) / (2 * t);
                sqrtIm = ((sqIm >= 0) ? 1 : -1) * t;
            }*/
            // log()
            double tmpRe = sqrtRe + re / sum;
            double tmpIm = sqrtIm - im / sum;
            re = 0.5 * Math.log(tmpRe * tmpRe + tmpIm * tmpIm);
            im = Math.atan2(tmpIm, tmpRe);
            return simplify(this);
        }
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
