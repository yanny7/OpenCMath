package com.opencmath;

import java.io.*;

@SuppressWarnings("WeakerAccess")
public abstract class CBase {
    final NumberType type;

    CBase(NumberType type) {
        this.type = type;
    }

    public static void put(CBase number) {
        switch (number.type) {
            case COMPLEX:
                CNumber.put((CNumber) number);
                break;
            case MATRIX:
                CMatrix.put((CMatrix) number);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public static CBase getComplex(double re, double im) {
        return CNumber.get(re, im);
    }

    public static CBase getMatrix(short rows, short cols) {
        return CMatrix.get(rows, cols);
    }

    public static CBase getMatrix(short rows, short cols, CBase[] items) {
        return CMatrix.get(rows, cols, items);
    }

    public static CBase duplicate(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) number;
                return CNumber.get(cNumber.re, cNumber.im);
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) number;
                CBase[] items = new CNumber[cMatrix.rows * cMatrix.cols];

                for (int i = 0; i < items.length; i++) {
                    items[i] = duplicate(cMatrix.items[i]);
                }

                return CMatrix.get(cMatrix.rows, cMatrix.cols, items);
            }
            default:
                throw new IllegalStateException();
        }
    }

    public NumberType getType() {
        return type;
    }

    public double getRe() {
        if (type == NumberType.COMPLEX) {
            CNumber cNumber = (CNumber) this;
            return cNumber.re;
        } else {
            return Double.NaN;
        }
    }

    public double getIm() {
        if (type == NumberType.COMPLEX) {
            CNumber cNumber = (CNumber) this;
            return cNumber.im;
        } else {
            return Double.NaN;
        }
    }

    public CBase[] getItems() {
        if (type == NumberType.MATRIX) {
            CMatrix cMatrix = (CMatrix) this;
            return cMatrix.items;
        } else {
            return null;
        }
    }

    public short getCols() {
        if (type == NumberType.MATRIX) {
            CMatrix cMatrix = (CMatrix) this;
            return cMatrix.cols;
        } else {
            return -1;
        }
    }

    public short getRows() {
        if (type == NumberType.MATRIX) {
            CMatrix cMatrix = (CMatrix) this;
            return cMatrix.rows;
        } else {
            return -1;
        }
    }

    public boolean isInteger() {
        if (type == NumberType.COMPLEX) {
            CNumber cNumber = (CNumber) this;
            return (cNumber.re == Math.floor(cNumber.re)) && !Double.isInfinite(cNumber.re) && (Math.abs(cNumber.re) < Long.MAX_VALUE);
        } else {
            return false;
        }
    }

    public static CBase getNaN() {
        return CNumber.get(Double.NaN, Double.NaN);
    }

    public static byte[] toData(CBase number) {
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

    public static CBase fromData(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(in);
        CBase number;

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
    public abstract CBase add(CBase number);

    /**
     * Subtraction
     * @param number Another value
     * @return Result of subtraction
     */
    public abstract CBase sub(CBase number);

    /**
     * Linear algebraic multiplication
     * @param number Another value
     * @return Result of linear algebraic multiplication
     */
    public abstract CBase mul(CBase number);

    /**
     * Division (element-by-element)
     * @param number Another value
     * @return Result of division
     */
    public abstract CBase div(CBase number);

    /**
     * Complex argument
     * @return Result of complex argument
     */
    public abstract CBase arg();

    /**
     * Natural logarithm
     * @return Result of natural logarithm
     */
    public abstract CBase ln();

    /**
     * Logarithm
     * @param base Base of logarithm
     * @return Result of logarithm
     */
    public abstract CBase log(CBase base);

    /**
     * Exponential function
     * @return Result of exponential function
     */
    public abstract CBase exp();

    /**
     * Absolute value
     * @return Result of absolute value
     */
    public abstract CBase abs();

    /**
     * Power function
     * @param exponent Exponent
     * @return Result of power function
     */
    public abstract CBase pow(CBase exponent);

    /**
     * Root function
     * @param index Index
     * @return Result of power function
     */
    public abstract CBase root(CBase index);

    /**
     * Square root
     * @return Result of square root
     */
    public abstract CBase sqrt();

    /**
     * Factorial
     * @return Result of factorial
     */
    public abstract CBase factorial();

    /**
     * Sine
     * @return Result of sine
     */
    public abstract CBase sin();

    /**
     * Cosine
     * @return Result of cosine
     */
    public abstract CBase cos();

    /**
     * Tangent
     * @return Result of tangent
     */
    public abstract CBase tan();

    /**
     * Cotangent
     * @return Result of cotangent
     */
    public abstract CBase cot();

    /**
     * Secant
     * @return Result of secant
     */
    public abstract CBase sec();

    /**
     * Cosecant
     * @return Result of cosecant
     */
    public abstract CBase csc();

    /**
     * Hyperbolic sine
     * @return Result of hyperbolic sine
     */
    public abstract CBase sinh();

    /**
     * Hyperbolic cosine
     * @return Result of hyperbolic cosine
     */
    public abstract CBase cosh();

    /**
     * Hyperbolic tangent
     * @return Result of hyperbolic tangent
     */
    public abstract CBase tanh();

    /**
     * Hyperbolic cotangent
     * @return Result of hyperbolic cotangent
     */
    public abstract CBase coth();

    /**
     * Hyperbolic secant
     * @return Result of hyperbolic secant
     */
    public abstract CBase sech();

    /**
     * Hyperbolic cosecant
     * @return Result of hyperbolic cosecant
     */
    public abstract CBase csch();

    public abstract CBase asin();
    public abstract CBase acos();
    public abstract CBase atan();
    public abstract CBase acot();
    public abstract CBase asec();
    public abstract CBase acsc();

    public abstract CBase asinh();
    public abstract CBase acosh();
    public abstract CBase atanh();
    public abstract CBase acoth();
    public abstract CBase asech();
    public abstract CBase acsch();

    public abstract CBase toRadians(AngleType angleType);
    public abstract CBase fromRadians(AngleType angleType);

    public abstract CBase inv();
    public abstract CBase transpose();
    public abstract CBase det();
    public abstract CBase rank();
    public abstract CBase cond();
    public abstract CBase trace();
    public abstract CBase adjugate();

    public abstract CBase shr(CBase count);
    public abstract CBase shl(CBase count);
    public abstract CBase and(CBase number);
    public abstract CBase or(CBase number);
    public abstract CBase xor(CBase number);
    public abstract CBase not();

    @Override public abstract String toString();
    @Override public abstract boolean equals(Object other);

    private static void numberToData(DataOutputStream dos, CBase number) throws IOException {
        dos.writeByte(number.getType().value);

        switch (number.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) number;
                dos.writeDouble(cNumber.re);
                dos.writeDouble(cNumber.im);
                break;
            }
            case MATRIX: {
                CMatrix cMatrix = (CMatrix) number;
                dos.writeByte(cMatrix.cols);
                dos.writeByte(cMatrix.rows);

                for (int i = 0; i < cMatrix.cols * cMatrix.rows; i++) {
                    numberToData(dos, cMatrix.items[i]);
                }
                break;
            }
            default:
                throw new IllegalStateException();
        }
    }

    private static CBase numberFromData(DataInputStream dis) throws IOException {
        CBase number = null;
        NumberType type = NumberType.fromValue(dis.readByte());

        if (type != null) {
            switch (type) {
                case COMPLEX: {
                    double re = dis.readDouble();
                    double im = dis.readDouble();
                    number = CNumber.get(re, im);
                    break;
                }
                case MATRIX: {
                    byte cols = dis.readByte();
                    byte rows = dis.readByte();

                    CBase[] items = new CBase[cols * rows];

                    for (int i = 0; i < cols * rows; i++) {
                        CBase item = numberFromData(dis);

                        if (item != null) {
                            items[i] = item;
                        }
                    }

                    number = CMatrix.get(cols, rows, items);
                    break;
                }
                default:
                    throw new IllegalStateException();
            }
        }

        return number;
    }
}
