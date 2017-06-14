package com.opencmath;

final class CNumber extends CBase {

    private static final PoolTemplate<CNumber> pool = new PoolTemplate<>(10, 100000, new PoolFactory<CNumber>() {
        @Override
        public CNumber create() {
            return new CNumber();
        }
    });

    double re;
    double im;

    private CNumber() {
        super(NumberType.COMPLEX);
        re = 0;
        im = 0;
    }

    @Override
    public CBase add(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) number;
                re += cNumber.re;
                im += cNumber.im;
                put(number);
                return this;
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) number;
                for (int i = 0; i < cMatrix.items.length; i++) {
                    cMatrix.items[i] = duplicate(this).add(cMatrix.items[i]);
                }
                put(this);
                return cMatrix;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase sub(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) number;
                re -= cNumber.re;
                im -= cNumber.im;
                put(number);
                return this;
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) number;
                for (int i = 0; i < cMatrix.items.length; i++) {
                    cMatrix.items[i] = duplicate(this).sub(cMatrix.items[i]);
                }
                put(this);
                return cMatrix;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase mul(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) number;
                double _re = re;
                re = re * cNumber.re - im * cNumber.im;
                im = _re * cNumber.im + im * cNumber.re;
                put(number);
                return this;
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) number;
                for (int i = 0; i < cMatrix.items.length; i++) {
                    cMatrix.items[i] = duplicate(this).mul(cMatrix.items[i]);
                }
                put(this);
                return cMatrix;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase div(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) number;
                if ((im != 0) || (cNumber.im != 0)) {
                    double mul = cNumber.re * cNumber.re + cNumber.im * cNumber.im;
                    double tmpRe = (re * cNumber.re + im * cNumber.im) / mul;
                    double tmpIm = (im * cNumber.re - re * cNumber.im) / mul;
                    re = tmpRe;
                    im = tmpIm;
                    put(number);
                } else {
                    if (cNumber.re != 0) {
                        re /= cNumber.re;
                    } else {
                        re = Double.NaN;
                        im = Double.NaN;
                    }
                    put(number);
                }
                return this;
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) number;
                for (int i = 0; i < cMatrix.items.length; i++) {
                    cMatrix.items[i] = duplicate(this).div(cMatrix.items[i]);
                }
                put(this);
                return cMatrix;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase arg() {
        re = Math.atan2(im, re);
        im = 0;
        return this;
    }

    @Override
    public CBase ln() {
        if ((im != 0) || (re < 0)) {
            double ln = Math.log(Math.sqrt(re * re + im * im));
            double arg = Math.atan2(im, re);
            re = ln;
            im = arg;
        } else {
            re = Math.log(re);
        }

        return this;
    }

    @Override
    public CBase log(CBase base) {
        switch (base.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) base;

                if ((cNumber.re == 0) && (cNumber.im == 0)) {
                    put(this);
                    return base;
                } else {
                    return ln().div(base.ln());
                }
            }
            case MATRIX:
                return ln().div(base.ln());
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase exp() {
        if (im != 0) {
            double exp = Math.exp(re);
            re = exp * Math.cos(im);
            im = exp * Math.sin(im);
        } else {
            re = Math.exp(re);
        }

        return this;
    }

    @Override
    public CBase abs() {
        if (im != 0) {
            re = Math.sqrt(re * re + im * im);
            im = 0;
        } else {
            re = (re < 0) ? -re : re;
        }
        return this;
    }

    @Override
    public CBase pow(CBase exponent) {
        switch (exponent.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) exponent;

                if (im != 0) {
                    return ln().mul(exponent).exp();
                } else {
                    if (cNumber.im != 0) {
                        CBase x = CNumber.get(Math.log(re), 0);
                        put(this);
                        return x.mul(exponent).exp();
                    } else {
                        re = Math.pow(re, cNumber.re);
                        put(exponent);
                        return this;
                    }
                }
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) exponent;
                for (int i = 0; i < cMatrix.items.length; i++) {
                    cMatrix.items[i] = duplicate(this).pow(cMatrix.items[i]);
                }
                put(this);
                return cMatrix;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase root(CBase index) {
        switch (index.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) index;

                if (cNumber.im != 0) {
                    return ln().mul(one().div(index)).exp();
                } else {
                    if (im != 0) {
                        double inv = 1.0 / cNumber.re;
                        double theta = Math.atan2(im, re);
                        double w = Math.pow(Math.sqrt(re * re + im * im), inv);
                        re = w * Math.cos(inv * theta);
                        im = w * Math.sin(inv * theta);
                        put(index);
                        return this;
                    } else {
                        re = Math.pow(re, 1.0 / cNumber.re);
                        put(index);
                        return this;
                    }
                }
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) index;
                for (int i = 0; i < cMatrix.items.length; i++) {
                    cMatrix.items[i] = duplicate(this).root(cMatrix.items[i]);
                }
                put(this);
                return cMatrix;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase sqrt() {
        double t = Math.sqrt((Math.abs(re) + Math.sqrt(re * re + im * im)) / 2.0);

        if (re >= 0) {
            re = t;
            im = im / (2 * t);
            return this;
        } else {
            re = Math.abs(im) / (2 * t);
            im = ((im >= 0) ? 1 : -1) * t;
            return this;
        }
    }

    @Override
    public CBase factorial() {
        if (im != 0) {
            add(one());
            CBase tmp = duplicate(this).add(CNumber.get(7.5));
            CBase A = CNumber.get(2 * Math.PI).sqrt().div(duplicate(this));
            CBase B = duplicate(tmp).pow(duplicate(this).add(CNumber.get(0.5)));
            CBase C = CNumber.get(Math.E).pow(CNumber.get(0).sub(tmp));
            CBase D = CNumber.get(676.520368121885098567009190444019).div(duplicate(this).add(CNumber.get(1)));
            CBase E = CNumber.get(-1259.13921672240287047156078755283).div(duplicate(this).add(CNumber.get(2)));
            CBase F = CNumber.get(771.3234287776530788486528258894).div(duplicate(this).add(CNumber.get(3)));
            CBase G = CNumber.get(-176.61502916214059906584551354).div(duplicate(this).add(CNumber.get(4)));
            CBase H = CNumber.get(12.507343278686904814458936853).div(duplicate(this).add(CNumber.get(5)));
            CBase I = CNumber.get(-0.13857109526572011689554707).div(duplicate(this).add(CNumber.get(6)));
            CBase J = CNumber.get(9.984369578019570859563e-6).div(duplicate(this).add(CNumber.get(7)));
            CBase K = CNumber.get(1.50563273514931155834e-7).div(duplicate(this).add(CNumber.get(8)));
            CBase ret = A.mul(CNumber.get(0.99999999999980993227684700473478).add(D.add(E.add(F.add(G.add(H.add(I.add(J.add(K))))))))).mul(B).mul(C);
            put(this);
            return ret;
        } else {
            if (isInteger()) {
                if (re > 20) {
                    re++;
                    double tmp1 = Math.sqrt(2.0 * Math.PI / re);
                    double tmp2 = re + 1.0 / (12.0 * re - 1.0 / (10.0 * re));
                    tmp2 = Math.pow(tmp2 / Math.E, re);
                    tmp2 *= tmp1;

                    put(this);
                    return CNumber.get(tmp2);
                } else if (re > 0) {
                    long sum = 1;

                    for (int i = 1; i <= re; i++) {
                        sum *= i;
                    }

                    re = sum;
                    return this;
                } else if (re == 0) {
                    re = 1;
                    return this;
                }

                put(this);
                return getNaN();
            } else {
                re++;
                double A = Math.sqrt(2 * Math.PI) / re;
                double B = Math.pow(re + 7.5, re + 0.5) * Math.exp(-re - 7.5);
                double C = 0.99999999999980993227684700473478;
                double D = 676.520368121885098567009190444019 / (re + 1);
                double E = -1259.13921672240287047156078755283 / (re + 2);
                double F = 771.3234287776530788486528258894 / (re + 3);
                double G = -176.61502916214059906584551354 / (re + 4);
                double H = 12.507343278686904814458936853 / (re + 5);
                double I = -0.13857109526572011689554707 / (re + 6);
                double J = 9.984369578019570859563e-6 / (re + 7);
                double K = 1.50563273514931155834e-7 / (re + 8);
                re = B * A * (C + D + E + F + G + H + I + J + K);
                return this;
            }
        }
    }

    @Override
    public CBase sin() {
        if (im != 0) {
            double tmpRe = Math.sin(re) * Math.cosh(im);
            double tmpIm = Math.cos(re) * Math.sinh(im);
            re = tmpRe;
            im = tmpIm;
        } else {
            re = Math.sin(re);
        }

        return this;
    }

    @Override
    public CBase cos() {
        if (im != 0) {
            double tmpRe = Math.cos(re) * Math.cosh(im);
            double tmpIm = -Math.sin(re) * Math.sinh(im);
            re = tmpRe;
            im = tmpIm;
        } else {
            re = Math.cos(re);
        }

        return this;
    }

    @Override
    public CBase tan() {
        if (im != 0) {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cos(twoRe) + Math.cosh(twoIm);
            re = Math.sin(twoRe) / tmp;
            im = Math.sinh(twoIm) / tmp;
        } else {
            re = Math.tan(re);
        }

        return this;
    }

    @Override
    public CBase cot() {
        if (im != 0) {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cos(twoRe) - Math.cosh(twoIm);
            re = -Math.sin(twoRe) / tmp;
            im = Math.sinh(twoIm) / tmp;
        } else {
            re = 1.0 / Math.tan(re);
        }

        return this;
    }

    @Override
    public CBase sec() {
        if (im != 0) {
            double tmp = Math.cos(2 * re) + Math.cosh(2 * im);
            double tmpRe = (2 * Math.cos(re) * Math.cosh(im)) / tmp;
            double tmpIm = (2 * Math.sin(re) * Math.sinh(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
        } else {
            re = 1.0 / Math.cos(re);
        }

        return this;
    }

    @Override
    public CBase csc() {
        if (im != 0) {
            double tmp = Math.cos(2 * re) - Math.cosh(2 * im);
            double tmpRe = -(2 * Math.sin(re) * Math.cosh(im)) / tmp;
            double tmpIm = (2 * Math.cos(re) * Math.sinh(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
        } else {
            re = 1.0 / Math.sin(re);
        }

        return this;
    }

    @Override
    public CBase sinh() {
        if (im != 0) {
            double tmpRe = Math.sinh(re) * Math.cos(im);
            double tmpIm = Math.cosh(re) * Math.sin(im);
            re = tmpRe;
            im = tmpIm;
        } else {
            re = Math.sinh(re);
        }

        return this;
    }

    @Override
    public CBase cosh() {
        if (im != 0) {
            double tmpRe = Math.cosh(re) * Math.cos(im);
            double tmpIm = Math.sinh(re) * Math.sin(im);
            re = tmpRe;
            im = tmpIm;
        } else {
            re = Math.cosh(re);
        }

        return this;
    }

    @Override
    public CBase tanh() {
        if (im != 0) {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cosh(twoRe) + Math.cos(twoIm);
            re = Math.sinh(twoRe) / tmp;
            im = Math.sin(twoIm) / tmp;
        } else {
            re = Math.tanh(re);
        }

        return this;
    }

    @Override
    public CBase coth() {
        if (im != 0) {
            double twoRe = 2 * re;
            double twoIm = 2 * im;
            double tmp = Math.cos(twoIm) - Math.cosh(twoRe);
            re = -Math.sinh(twoRe) / tmp;
            im = Math.sin(twoIm) / tmp;
        } else {
            re = 1.0 / Math.tanh(re);
        }

        return this;
    }

    @Override
    public CBase sech() {
        if (im != 0) {
            double tmp = Math.cosh(2 * re) + Math.cos(2 * im);
            double tmpRe = (2 * Math.cosh(re) * Math.cos(im)) / tmp;
            double tmpIm = -(2 * Math.sinh(re) * Math.sin(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
        } else {
            re = 1.0 / Math.cosh(re);
        }

        return this;
    }

    @Override
    public CBase csch() {
        if (im != 0) {
            double tmp = Math.cos(2 * im) - Math.cosh(2 * re);
            double tmpRe = -(2 * Math.sinh(re) * Math.cos(im)) / tmp;
            double tmpIm = (2 * Math.cosh(re) * Math.sin(im)) / tmp;
            re = tmpRe;
            im = tmpIm;
        } else {
            re = 1.0 / Math.sinh(re);
        }

        return this;
    }

    @Override
    public CBase asin() {
        if (im != 0) {
            CBase a = one().sub(duplicate(this).mul(duplicate(this))).sqrt(); // sqrt(1 - z^2)
            CBase b = a.add(duplicate(this).mul(cone())).ln().mul(mcone()); // -i (log(a + iz))
            put(this);
            return b;
        } else {
            if ((re < -1) || (re > 1)) {
                // asin(z) = -i (log(sqrt(1 - z^2) + iz))
                CBase tmp = one().sub(CNumber.get(re * re)).sqrt();
                CBase ret = tmp.add(cone().mul(CNumber.get(re))).ln().mul(mcone());
                put(this);
                return ret;
            }

            re = Math.asin(re);
            return this;
        }
    }

    @Override
    public CBase acos() {
        if (im != 0) {
            CBase a = one().sub(duplicate(this).mul(duplicate(this))).sqrt().mul(cone()); // i (sqrt(1 - z^2))
            CBase b = duplicate(this).add(a).ln().mul(mcone()); // -i (log(z + a))
            put(this);
            return b;
        } else {
            if ((re < -1) || (re > 1)) {
                // acos(z) = -i (log(z + i (sqrt(1 - z^2))))
                CBase tmp = one().sub(CNumber.get(re * re)).sqrt();
                CBase ret = tmp.mul(cone()).add(CNumber.get(re)).ln().mul(mcone());
                put(this);
                return ret;
            }

            if (re == -1) {
                put(this);
                return pi();
            }

            re = Math.acos(re);
        }

        return this;
    }

    @Override
    public CBase atan() {
        if (im != 0) {
            if ((re == 0) && ((im == 1) || (im == -1))) { // undefined for atan(i), atan(-i)
                put(this);
                return getNaN();
            }

            double twoRe = re * re;
            double A = 1 + im;
            double B = 1 - im;
            re = (0.5 * Math.atan2(re, B) - 0.5 * Math.atan2(-re, A)) * (((re == 0) && (im < 0)) ? -1 : 1);
            im = 0.25 * Math.log(twoRe + A * A) - 0.25 * Math.log(twoRe + B * B);
        } else {
            re = Math.atan(re);
        }

        return this;
    }

    @Override
    public CBase acot() {
        if (im != 0) {
            if ((re == 0) && ((im == -1) || (im == 1))) { // undefined for atan(i), atan(-i)
                put (this);
                return getNaN();
            }

            double twoRe = re * re;
            double pow = twoRe + im * im;
            double len = pow * pow;
            double A = im / pow;
            double B = twoRe / len;
            double C = re / pow;
            re = 0.5 * Math.atan2(C, A + 1) - 0.5 * Math.atan2(-C, 1 - A);
            im = 0.25 * Math.log(B + (1 - A) * (1 - A)) -  0.25 * Math.log(B + (1 + A) * (1 + A));
            return this;
        } else {
            re = Math.atan(1.0 / re);
            return this;
        }
    }

    @Override
    public CBase asec() {
        if (im != 0) {
            return one().div(this).acos();
        } else {
            if ((re > -1) && (re < 1)) {
                // asec(z) = acos(1/z)
                CBase tmp = one().div(CNumber.get(re)).acos();
                put(this);
                return tmp;
            }

            if (re == -1) {
                put(this);
                return pi();
            }

            re = Math.acos(1.0 / re);
            return this;
        }
    }

    @Override
    public CBase acsc() {
        if (im != 0) {
            return one().div(this).asin();
        } else {
            if ((re > -1) & (re < 1)) {
                // acsc(z) = asin(1/z)
                CBase tmp = one().div(CNumber.get(re)).asin();
                put(this);
                return tmp;
            }

            re = Math.asin(1.0 / re);
            return this;
        }
    }

    @Override
    public CBase asinh() {
        if (im != 0) {
            CBase pow = duplicate(this).mul(duplicate(this));
            return this.add(one().add(pow).sqrt()).ln();
        } else {
            re = Math.log(re + Math.sqrt(1 + re * re));
            return this;
        }
    }

    @Override
    public CBase acosh() {
        if (im != 0) {
            CBase tmp = duplicate(this).add(duplicate(this).add(one()).sqrt().mul(duplicate(this).sub(one()).sqrt())).ln();
            put(this);
            return tmp;
        } else {
            if (re >= 1) {
                re = Math.log(re + Math.sqrt(re + 1) * Math.sqrt(re - 1));
                return this;
            }

            CBase sqrt1 = CNumber.get(re + 1).sqrt();
            CBase sqrt2 = CNumber.get(re - 1).sqrt();
            CBase tmp = CNumber.get(re).add(sqrt1.mul(sqrt2)).ln();
            put(this);
            return tmp;
        }
    }

    @Override
    public CBase atanh() {
        if (im != 0) {
            double Im2 = im * im;
            double A = 1 + re;
            double B = 1 - re;
            double tmpRe = 0.25 * Math.log(A * A + Im2) - 0.25 * Math.log(B * B + Im2);
            double tmpIm = 0.5 * Math.atan2(im, A) - 0.5 * Math.atan2(-im, B);
            re = tmpRe;
            im = tmpIm;
            return this;
        } else {
            CBase ln1 = one().add(CNumber.get(re)).ln();
            CBase ln2 = one().sub(CNumber.get(re)).ln();
            CBase tmp = CNumber.get(0.5).mul(ln1.sub(ln2));
            put(this);
            return tmp;
        }
    }

    @Override
    public CBase acoth() {
        if (im != 0) {
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
            return this;
        } else {
            double val = 1.0 / re;
            CBase ln1 = one().add(CNumber.get(val)).ln();
            CBase ln2 = one().sub(CNumber.get(val)).ln();
            CBase tmp = CNumber.get(0.5).mul(ln1.sub(ln2));
            put(this);
            return tmp;
        }
    }

    @Override
    public CBase asech() {
        if (im != 0) {
            CBase invZ = one().div(this); // 1/z
            CBase tmp = duplicate(invZ).add(duplicate(invZ).sub(one()).sqrt().mul(duplicate(invZ).add(one()).sqrt())).ln();
            put(invZ);
            return tmp;
        } else {
            double val = 1.0 / re;
            CBase sqrt1 = CNumber.get(val).sub(one()).sqrt();
            CBase sqrt2 = CNumber.get(val).add(one()).sqrt();
            CBase tmp = sqrt1.mul(sqrt2).add(CNumber.get(val)).ln();
            put(this);
            return tmp;
        }
    }

    @Override
    public CBase acsch() {
        if (im != 0) {
            CBase pow = duplicate(this).mul(duplicate(this));
            return one().add(one().div(pow)).sqrt().add(one().div(this)).ln();
        } else {
            CBase tmp = CNumber.get(1 / re).asinh();
            put(this);
            return tmp;
        }
    }

    @Override
    public CBase toRadians(AngleType angleType) {
        if (angleType != AngleType.RAD) {
            re *= angleType.toValue;
            im *= angleType.toValue;
        }

        return this;
    }

    @Override
    public CBase fromRadians(AngleType angleType) {
        if (angleType != AngleType.RAD) {
            re *= angleType.fromValue;
            im *= angleType.fromValue;
        }

        return this;
    }

    @Override
    public CBase inv() {
        return one().sub(this);
    }

    @Override
    public CBase transpose() {
        return this;
    }

    @Override
    public CBase det() {
        return this;
    }

    @Override
    public CBase rank() {
        re = 1;
        im = 0;
        return this;
    }

    @Override
    public CBase cond() {
        if ((re == 0) && (im == 0)) {
            re = Double.POSITIVE_INFINITY;
            im = 0;
            return this;
        } else {
            re = 1;
            im = 0;
            return this;
        }
    }

    @Override
    public CBase trace() {
        return this;
    }

    @Override
    public CBase adjugate() {
        re = 1;
        im = 0;
        return this;
    }

    @Override
    public CBase shr(CBase count) {
        if (isInteger() && count.isInteger()) {
            long value = Math.round(re);
            long shift = Math.round(((CNumber) count).re);
            re = value >>> shift;
            return this;
        } else {
            put(this);
            return getNaN();
        }
    }

    @Override
    public CBase shl(CBase count) {
        if (isInteger() && count.isInteger()) {
            long value = Math.round(re);
            long shift = Math.round(((CNumber) count).re);
            re = value << shift;
            return this;
        } else {
            put(this);
            return getNaN();
        }
    }

    @Override
    public CBase and(CBase number) {
        if (isInteger() && number.isInteger()) {
            long x = Math.round(re);
            long y = Math.round(((CNumber) number).re);
            re = x & y;
            return this;
        } else {
            put(this);
            return getNaN();
        }
    }

    @Override
    public CBase or(CBase number) {
        if (isInteger() && number.isInteger()) {
            long x = Math.round(re);
            long y = Math.round(((CNumber) number).re);
            re = x | y;
            return this;
        } else {
            put(this);
            return getNaN();
        }
    }

    @Override
    public CBase xor(CBase number) {
        if (isInteger() && number.isInteger()) {
            long x = Math.round(re);
            long y = Math.round(((CNumber) number).re);
            re = x ^ y;
            return this;
        } else {
            put(this);
            return getNaN();
        }
    }

    @Override
    public CBase not() {
        if (isInteger()) {
            long x = Math.round(re);
            re = ~x;
            return this;
        } else {
            put(this);
            return getNaN();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        if (re == 0) {
            if (im == 0) {
                sb.append("0");
            } else {
                if (im < 0) {
                    sb.append("-").append(-im);
                } else {
                    sb.append(im);
                }

                sb.append("i");
            }
        } else {
            sb.append(re);

            if (im != 0) {
                if (im < 0) {
                    sb.append("-").append(-im);
                } else {
                    sb.append("+").append(im);
                }

                sb.append("i");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CNumber) {
            CNumber number = (CNumber) other;
            //return (Double.isNaN(number.re) || Double.isNaN(number.im)) && (Double.isNaN(number.re) || Double.isNaN(number.im)) || ((re == number.re) && (im == number.im));
            //TODO remove?
            if ((Double.isNaN(number.re) || Double.isNaN(number.im)) && (Double.isNaN(number.re) || Double.isNaN(number.im))) {
                return true;
            }

            if (re == 0) {
                return (compareRelative(im, number.im) || (im == number.im));
            }

            return ((compareRelative(im, number.im) || (im == number.im)) && (compareRelative(re, number.re) || (re == number.re)));
        } else {
            return false;
        }
    }

    static CNumber get(double re, double im) {
        CNumber number = pool.get();
        number.re = re;
        number.im = im;
        return number;
    }

    static void put(CNumber item) {
        pool.put(item);
    }

    static int poolSize() {
        return pool.size();
    }

    static boolean checkConsistency() {
        return pool.checkConsistency();
    }

    private static CNumber get(double re) {
        CNumber number = pool.get();
        number.re = re;
        number.im = 0;
        return number;
    }

    private static CBase one() {
        return CNumber.get(1);
    }

    private static CBase cone() {
        return CNumber.get(0, 1);
    }

    private static CBase mcone() {
        return CNumber.get(0, -1);
    }

    private static CBase pi() {
        return CNumber.get(Math.PI);
    }

    private static boolean compareRelative(double a, double b) {
        double diff = Math.abs(a - b);
        a = Math.abs(a);
        b = Math.abs(b);
        double largest = (b > a) ? b : a;
        return (diff <= (largest * 1.0E-12));
    }

}
