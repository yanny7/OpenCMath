package com.opencmath;

import java.io.*;

@SuppressWarnings("WeakerAccess")
public abstract class BaseNumber {
    private static final double EPSILON = 1.0E-12;

    final NumberType type;

    BaseNumber(NumberType type) {
        this.type = type;
    }

    /**
     * Gets current type of number
     * @return Current number type
     * @see NumberType
     */
    public NumberType getType() {
        return type;
    }

    /**
     * Gets INVALID number from pool
     * @return INVALID number type
     * @see NumberType
     */
    public static BaseNumber getNaN() {
        return InvalidNumber.get();
    }

    /**
     * Gets INTEGER number from pool
     * @param value New value
     * @return INTEGER number with new value
     * @see NumberType
     */
    public static BaseNumber getInteger(long value) {
        return IntegerNumber.get(value);
    }

    /**
     * Gets REAL number from pool
     * @param value New value
     * @return REAL number with new value
     * @see NumberType
     */
    public static BaseNumber getReal(double value) {
        return RealNumber.get(value);
    }

    /**
     * Gets CONSTANT number from pool
     * @param type New value
     * @return CONSTANT number with new value
     * @see NumberType
     * @see ConstantNumber
     */
    public static BaseNumber getConstant(ConstantType type) {
        return ConstantNumber.get(type);
    }

    /**
     * Gets COMPLEX number from pool
     * @param re Real part of complex number
     * @param im Imaginary part of complex number
     * @return COMPLEX number with new value
     * @see NumberType
     */
    public static BaseNumber getComplex(double re, double im) {
        return ComplexNumber.get(re, im);
    }

    /**
     * Gets MATRIX number from pool, with empty items
     * @param rows Number of rows
     * @param cols Number of columns
     * @return MATRIX number with defined size
     * @see NumberType
     */
    public static BaseNumber getMatrix(byte rows, byte cols) {
        return MatrixNumber.get(rows, cols);
    }

    /**
     * Gets MATRIX number from pool
     * @param rows Number of rows
     * @param cols Number of columns
     * @param items Array of matrix items. All items are in 1D array
     * @return MATRIX number with defined size and items
     * @see NumberType
     */
    public static BaseNumber getMatrix(byte rows, byte cols, BaseNumber[] items) {
        return MatrixNumber.get(rows, cols, items);
    }

    /**
     * Returns integer value as Long. Valid only for INTEGER type
     * @return For INTEGER returns current value, otherwise Long.MIN_VALUE
     * @see NumberType
     */
    public long getInteger() {
        if (type == NumberType.INTEGER) {
            IntegerNumber integerNumber = (IntegerNumber) this;
            return integerNumber.value;
        } else {
            return Long.MIN_VALUE;
        }
    }

    /**
     * Returns constant value as ConstantType. Valid only for CONSTANT type
     * @return For CONSTANT returns current value, otherwise null
     * @see NumberType
     */
    public ConstantType getConstant() {
        if (type == NumberType.CONSTANT) {
            ConstantNumber constantNumber = (ConstantNumber) this;
            return constantNumber.value;
        } else {
            return null;
        }
    }

    /**
     * Returns real value as Double. Valid for INTEGER, REAL and COMPLEX type
     * @return For INTEGER, REAL and COMPLEX number returns current value, otherwise Double.NaN
     * @see NumberType
     */
    public double getReal() {
        switch (type) {
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) this;
                return integerNumber.value;
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) this;
                return realNumber.value;
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) this;
                return complexNumber.re;
            }
            case MATRIX:
            case INVALID:
                return Double.NaN;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Returns imaginary part as Double. Valid for INTEGER, REAL and COMPLEX type
     * @return For INTEGER and REAL returns 0, for COMPLEX returns current value. Otherwise Double.NaN
     * @see NumberType
     */
    public double getImag() {
        switch (type) {
            case INTEGER:
            case REAL:
                return 0;
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) this;
                return complexNumber.im;
            }
            case MATRIX:
            case INVALID:
                return Double.NaN;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Returns items of matrix number. Valid only for MATRIX type
     * @return For MATRIX return array of matrix items, otherwise null
     * @see NumberType
     */
    public BaseNumber[] getMatrixItems() {
        if (type == NumberType.MATRIX) {
            MatrixNumber matrixNumber = (MatrixNumber) this;
            return  matrixNumber.value;
        } else {
            return null;
        }
    }

    /**
     * Return count of matrix rows. Valid only for MATRIX type
     * @return For MATRIX return number of rows, otherwise -1
     * @see NumberType
     */
    public byte getRows() {
        if (type == NumberType.MATRIX) {
            MatrixNumber matrixNumber = (MatrixNumber) this;
            return  matrixNumber.rows;
        } else {
            return -1;
        }
    }

    /**
     * Return count of matrix columns. Valid only for MATRIX type
     * @return For MATRIX return number of columns, otherwise -1
     * @see NumberType
     */
    public byte getCols() {
        if (type == NumberType.MATRIX) {
            MatrixNumber matrixNumber = (MatrixNumber) this;
            return  matrixNumber.cols;
        } else {
            return -1;
        }
    }

    /**
     * Put number back into pool. Each number can be put only once
     * @param number Number previously retrieved by getNaN(), getInteger(), getReal(), getComplex() or getMatrix() function
     */
    public static void put(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                InvalidNumber.put((InvalidNumber) number);
                return;
            case INTEGER:
                IntegerNumber.put((IntegerNumber) number);
                return;
            case REAL:
                RealNumber.put((RealNumber) number);
                return;
            case COMPLEX:
                ComplexNumber.put((ComplexNumber) number);
                return;
            case CONSTANT:
                ConstantNumber.put((ConstantNumber) number);
                return;
            case MATRIX:
                MatrixNumber.put((MatrixNumber) number);
                return;
        }

        throw new IllegalStateException();
    }

    /**
     * Try to simplify number. E.g. simplify(BaseNumber.getComplex(1.0, 0.0)).getType() returns INTEGER (with value 1)
     * @param value Number for simplification
     * @return Simplified value
     */
    public static BaseNumber simplify(BaseNumber value) {
        switch (value.type) {
            case INVALID:
            case INTEGER:
            case CONSTANT:
                return value;
            case REAL: {
                RealNumber realNumber = (RealNumber) value;

                if (isInteger(realNumber.value)) {
                    IntegerNumber integerNumber = IntegerNumber.get((long) realNumber.value);
                    put(value);
                    return integerNumber;
                }
                if (Double.isNaN(realNumber.value)) {
                    put(value);
                    return InvalidNumber.get();
                }

                return value;
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) value;

                if (complexNumber.im == 0) {
                    if (isInteger(complexNumber.re)) {
                        IntegerNumber integerNumber = IntegerNumber.get((long) complexNumber.re);
                        put(value);
                        return integerNumber;
                    }

                    RealNumber realNumber = RealNumber.get(complexNumber.re);
                    put(value);
                    return realNumber;
                }
                if (Double.isNaN(complexNumber.re) || Double.isNaN(complexNumber.im)) {
                    put(value);
                    return InvalidNumber.get();
                }
                if (Double.isInfinite(complexNumber.re) || Double.isInfinite(complexNumber.im)) {
                    put(value);
                    return InvalidNumber.get();
                }

                return value;
            }
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) value;

                if (matrixNumber.value.length == 1) {
                    BaseNumber number = matrixNumber.value[0];
                    matrixNumber.value = new BaseNumber[0];
                    put(matrixNumber);
                    return number;
                }

                return value;
            }
        }

        throw new IllegalStateException();
    }

    /**
     * Duplicate number and returns new pointer from pool with same value
     * @param number Number for duplication
     * @return Duplicated value
     */
    public static BaseNumber duplicate(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                return InvalidNumber.get();
            case INTEGER:
                return IntegerNumber.get(((IntegerNumber) number).value);
            case REAL:
                return RealNumber.get(((RealNumber) number).value);
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                return ComplexNumber.get(complexNumber.re, complexNumber.im);
            }
            case CONSTANT:
                return ConstantNumber.get(((ConstantNumber) number).value);
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                if (matrixNumber.value.length > 0) {
                    BaseNumber[] items = new BaseNumber[matrixNumber.cols * matrixNumber.rows];

                    for (int i = 0; i < items.length; i++) {
                        items[i] = duplicate(matrixNumber.value[i]);
                    }

                    return MatrixNumber.get(matrixNumber.cols, matrixNumber.rows, items);
                } else {
                    return MatrixNumber.get(matrixNumber.cols, matrixNumber.rows);
                }
            }
        }

        throw new IllegalStateException();
    }

    /**
     * Convert number to byte array
     * @param number Number for storing
     * @return Byte array representation of number
     */
    public static byte[] toData(BaseNumber number) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        try {
            numberToData(dos, number);
            dos.flush();
        } catch (IOException e) {
            return null;
        }

        return out.toByteArray();
    }

    /**
     * Construct number from byte array
     * @param data Byte array
     * @return Constructed number
     */
    public static BaseNumber fromData(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(in);
        BaseNumber number;

        try {
            number = numberFromData(dis);
            dis.close();
        } catch (Exception e) {
            return null;
        }

        return number;
    }

    /**
     * Addition
     * @param number Another value
     * @return Result of addition
     */
    public abstract BaseNumber add(BaseNumber number);

    /**
     * Subtraction
     * @param number Another value
     * @return Result of subtraction
     */
    public abstract BaseNumber sub(BaseNumber number);

    /**
     * Linear algebraic multiplication
     * @param number Another value
     * @return Result of linear algebraic multiplication
     */
    public abstract BaseNumber mul(BaseNumber number);

    /**
     * Division (element-by-element)
     * @param number Another value
     * @return Result of division
     */
    public abstract BaseNumber div(BaseNumber number);

    /**
     * Complex argument
     * @return Result of complex argument
     */
    public abstract BaseNumber arg();

    /**
     * Natural logarithm
     * @return Result of natural logarithm
     */
    public abstract BaseNumber ln();

    /**
     * Logarithm
     * @param base Base of logarithm
     * @return Result of logarithm
     */
    public abstract BaseNumber log(BaseNumber base);

    /**
     * Exponential function
     * @return Result of exponential function
     */
    public abstract BaseNumber exp();

    /**
     * Absolute value
     * @return Result of absolute value
     */
    public abstract BaseNumber abs();

    /**
     * Power function
     * @param exponent Exponent
     * @return Result of power function
     */
    public abstract BaseNumber pow(BaseNumber exponent);

    /**
     * Root function
     * @param index Index
     * @return Result of power function
     */
    public abstract BaseNumber root(BaseNumber index);

    /**
     * Square root
     * @return Result of square root
     */
    public abstract BaseNumber sqrt();

    /**
     * Factorial
     * @return Result of factorial
     */
    public abstract BaseNumber factorial();

    /**
     * Sine
     * @return Result of sine
     */
    public abstract BaseNumber sin();

    /**
     * Cosine
     * @return Result of cosine
     */
    public abstract BaseNumber cos();

    /**
     * Tangent
     * @return Result of tangent
     */
    public abstract BaseNumber tan();

    /**
     * Cotangent
     * @return Result of cotangent
     */
    public abstract BaseNumber cot();

    /**
     * Secant
     * @return Result of secant
     */
    public abstract BaseNumber sec();

    /**
     * Cosecant
     * @return Result of cosecant
     */
    public abstract BaseNumber csc();

    /**
     * Hyperbolic sine
     * @return Result of hyperbolic sine
     */
    public abstract BaseNumber sinh();

    /**
     * Hyperbolic cosine
     * @return Result of hyperbolic cosine
     */
    public abstract BaseNumber cosh();

    /**
     * Hyperbolic tangent
     * @return Result of hyperbolic tangent
     */
    public abstract BaseNumber tanh();

    /**
     * Hyperbolic cotangent
     * @return Result of hyperbolic cotangent
     */
    public abstract BaseNumber coth();

    /**
     * Hyperbolic secant
     * @return Result of hyperbolic secant
     */
    public abstract BaseNumber sech();

    /**
     * Hyperbolic cosecant
     * @return Result of hyperbolic cosecant
     */
    public abstract BaseNumber csch();

    /**
     * Inverse sine
     * @return Result of inverse sine
     */
    public abstract BaseNumber asin();

    /**
     * Inverse cosine
     * @return Result of inverse cosine
     */
    public abstract BaseNumber acos();

    /**
     * Inverse tangent
     * @return Result of inverse tangent
     */
    public abstract BaseNumber atan();

    /**
     * Inverse cotangent
     * @return Result of inverse cotangent
     */
    public abstract BaseNumber acot();

    /**
     * Inverse secant
     * @return Result of inverse secant
     */
    public abstract BaseNumber asec();

    /**
     * Inverse cosecant
     * @return Result of inverse cosecant
     */
    public abstract BaseNumber acsc();

    /**
     * Inverse hyperbolic sine
     * @return Result of inverse hyperbolic sine
     */
    public abstract BaseNumber asinh();

    /**
     * Inverse hyperbolic cosine
     * @return Result of inverse hyperbolic cosine
     */
    public abstract BaseNumber acosh();

    /**
     * Inverse hyperbolic tangent
     * @return Result of inverse hyperbolic tangent
     */
    public abstract BaseNumber atanh();

    /**
     * Inverse hyperbolic cotangent
     * @return Result of inverse hyperbolic cotangent
     */
    public abstract BaseNumber acoth();

    /**
     * Inverse hyperbolic secant
     * @return Result of inverse hyperbolic secant
     */
    public abstract BaseNumber asech();

    /**
     * Inverse hyperbolic cosecant
     * @return Result of inverse hyperbolic cosecant
     */
    public abstract BaseNumber acsch();

    /**
     * Change value to radians from current type
     * @param angleType Current angle type
     * @return Value in radians
     */
    public abstract BaseNumber toRadians(AngleType angleType);

    /**
     * Change value defined type from radians
     * @param angleType New angle type
     * @return Value in new type
     */
    public abstract BaseNumber fromRadians(AngleType angleType);

    /**
     * Inverse value
     * @return Result of inverse
     */
    public abstract BaseNumber inv();

    /**
     * Transpose
     * @return Result of transpose
     */
    public abstract BaseNumber transpose();

    /**
     * Determinant
     * @return Result of determinant
     */
    public abstract BaseNumber det();

    /**
     * Rank
     * @return Result of rank
     */
    public abstract BaseNumber rank();

    /**
     * Trace
     * @return Result of trace
     */
    public abstract BaseNumber trace();

    /**
     * Adjugate
     * @return Result of adjugate
     */
    public abstract BaseNumber adjugate();

    /**
     * Shift bits to right
     * @param count How much shift, must be of type INTEGER
     * @return Shifted value
     * @see NumberType
     */
    public abstract BaseNumber shr(BaseNumber count);

    /**
     * Shift bits to left
     * @param count How much shift, must be of type INTEGER
     * @return Shifted value
     * @see NumberType
     */
    public abstract BaseNumber shl(BaseNumber count);

    /**
     * Binary and
     * @param number Another value
     * @return Result of binary and
     */
    public abstract BaseNumber and(BaseNumber number);

    /**
     * Binary or
     * @param number Another value
     * @return Result of binary or
     */
    public abstract BaseNumber or(BaseNumber number);

    /**
     * Binary xor
     * @param number Another value
     * @return Result of binary xor
     */
    public abstract BaseNumber xor(BaseNumber number);

    /**
     * Binary negation
     * @return Result of binary negation
     */
    public abstract BaseNumber not();

    @Override public abstract String toString();
    @Override public abstract boolean equals(Object other);


    static boolean isInteger(double variable) {
        return ((variable == Math.floor(variable)) && !Double.isInfinite(variable) && (Math.abs(variable) < Long.MAX_VALUE));
    }

    static boolean compareRelative(double a, double b) {
        double diff = Math.abs(a - b);
        a = Math.abs(a);
        b = Math.abs(b);
        double largest = (b > a) ? b : a;
        return (diff <= (largest * EPSILON));
    }

    static BaseNumber getComplexOne() {
        return ComplexNumber.get(0, 1);
    }

    static BaseNumber getComplexMinusOne() {
        return ComplexNumber.get(0, -1);
    }

    static BaseNumber getOne() {
        return IntegerNumber.get(1);
    }

    static BaseNumber getZero() {
        return IntegerNumber.get(0);
    }

    private static void numberToData(DataOutputStream dos, BaseNumber number) throws IOException {
        dos.writeByte(number.type.value);

        switch (number.type) {
            case INVALID: {
                break;
            }
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) number;
                dos.writeLong(integerNumber.value);
                break;
            }
            case REAL: {
                RealNumber realNumber = (RealNumber) number;
                dos.writeDouble(realNumber.value);
                break;
            }
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                dos.writeDouble(complexNumber.re);
                dos.writeDouble(complexNumber.im);
                break;
            }
            case CONSTANT: {
                ConstantNumber constantNumber = (ConstantNumber) number;
                dos.writeInt(constantNumber.value.id);
                break;
            }
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;
                dos.writeByte(matrixNumber.cols);
                dos.writeByte(matrixNumber.rows);

                for (int i = 0; i < matrixNumber.cols * matrixNumber.rows; i++) {
                    numberToData(dos, matrixNumber.value[i]);
                }
                break;
            }
            default:
                throw new IllegalStateException();
        }
    }

    private static BaseNumber numberFromData(DataInputStream dis) throws IOException {
        BaseNumber number = null;
        NumberType type = NumberType.fromValue(dis.readByte());

        if (type != null) {
            switch (type) {
                case INVALID:
                    number = InvalidNumber.get();
                    break;
                case INTEGER: {
                    long value = dis.readLong();
                    number = IntegerNumber.get(value);
                    break;
                }
                case REAL: {
                    double value = dis.readDouble();
                    number = RealNumber.get(value);
                    break;
                }
                case COMPLEX: {
                    double re = dis.readDouble();
                    double im = dis.readDouble();
                    number = ComplexNumber.get(re, im);
                    break;
                }
                case CONSTANT: {
                    ConstantType constant = ConstantType.fromValue(dis.readInt());

                    if (constant == null) {
                        break;
                    }

                    number = ConstantNumber.get(constant);
                    break;
                }
                case MATRIX: {
                    byte cols = dis.readByte();
                    byte rows = dis.readByte();

                    BaseNumber[] items = new BaseNumber[cols * rows];

                    for (int i = 0; i < cols * rows; i++) {
                        BaseNumber item = numberFromData(dis);

                        if (item != null) {
                            items[i] = item;
                        }
                    }

                    number = MatrixNumber.get(cols, rows, items);
                    break;
                }
                default:
                    throw new IllegalStateException();
            }
        }

        return number;
    }

}
