package com.opencmath;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

class TestHelper {

    private static final String PRINT_STRING = "    %27.16e  ==  %27.16e  =>  %11.6e %% (%s)\n";

    private static void printDifference(BaseNumber a, BaseNumber b) {
        assertEquals(a.type, b.type);

        switch (a.type) {
            case INVALID: {
                System.out.printf(PRINT_STRING, Double.NaN, Double.NaN, 0.0, NumberType.INVALID);
                break;
            }
            case INTEGER:
            case REAL:
            case CONSTANT:
            case COMPLEX:
            case MATRIX: {
                double x1 = getValue(a);
                double x2 = getValue(b);

                if ((x1 == 0) || (x2 == 0) || Double.isInfinite(x1) || Double.isInfinite(x2)) {
                    System.out.printf(PRINT_STRING, x1, x2, 0.0, a.type);
                    break;
                }

                double precision = Math.abs(x1 - x2) / Math.min(Math.abs(x1), Math.abs(x2)) * 100.0;
                System.out.printf(PRINT_STRING, x1, x2, precision, a.type);
                break;
            }
        }
    }

    private static double getValue(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                return Double.NaN;
            case INTEGER:
                return ((IntegerNumber) number).value;
            case REAL:
                return ((RealNumber) number).value;
            case COMPLEX: {
                ComplexNumber complexNumber = (ComplexNumber) number;
                return Math.sqrt(complexNumber.re * complexNumber.re + complexNumber.im * complexNumber.im);
            }
            case CONSTANT:
                return ((ConstantNumber) number).value.value;
            case MATRIX: {
                BaseNumber tmp = BaseNumber.duplicate(number).det();
                double value = getValue(tmp);
                BaseNumber.put(tmp);
                return value;
            }
        }

        throw new IllegalArgumentException();
    }

    static void compareResultAndPut(BaseNumber expected, BaseNumber output) {
        assertEquals(expected, output);
        printDifference(expected, output);
        BaseNumber.put(expected);
        BaseNumber.put(output);
    }

    static BaseNumber parseNumber(String number) {
        if (number.startsWith("M")) {
            ArrayList<BaseNumber> list = new ArrayList<>();
            String[] tokens = number.substring(number.indexOf("["), number.indexOf("]")).replace("[", "").replace("]", "").replace(" ", "").split(";");
            String[] dimen = number.substring(0, number.indexOf("[")).replace("M", "").split("x");
            byte cols = Byte.parseByte(dimen[0]);
            byte rows = Byte.parseByte(dimen[1]);

            for (String token : tokens) {
                if (token.isEmpty()) {
                    continue;
                }

                list.add(parseNumber(token));
            }

            return MatrixNumber.get(cols, rows, list.toArray(new BaseNumber[0]));
        } else if ((number.compareTo("ComplexInfinity") == 0) || (number.compareTo("NaN") == 0)) {
            return InvalidNumber.get();
        } else if (number.equals("Infinity")) {
            return RealNumber.get(Double.POSITIVE_INFINITY);
        } else if (number.equals("-Infinity")) {
            return RealNumber.get(Double.NEGATIVE_INFINITY);
        } else if (number.contains("i")) {
            String[] tmp = number.split("[-+]");
            double re = 0, im;
            if (tmp.length == 0) {
                throw new IllegalArgumentException(number);
            }
            try {
                if (tmp.length == 1) {
                    im = Double.parseDouble(number.substring(0, number.length() -1));
                } else if (tmp.length == 2) {
                    if (tmp[0].length() == 0) {
                        im = Double.parseDouble(number.substring(0, number.length() -1));
                    } else {
                        re = Double.parseDouble(tmp[0]);
                        im = Double.parseDouble(tmp[1].substring(0, tmp[1].length() - 1));

                        if (number.charAt(tmp[0].length()) == '-') {
                            im = -im;
                        }
                    }
                } else {
                    re = -Double.parseDouble(tmp[1]);
                    im = Double.parseDouble(tmp[2].substring(0, tmp[2].length() - 1));

                    if (number.charAt(tmp[0].length() + tmp[1].length() + 1) == '-') {
                        im = -im;
                    }
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(number);
            }

            return ComplexNumber.get(re, im);
        } else if (number.matches("^-?\\d+$")) {
            try {
                return IntegerNumber.get(Long.parseLong(number));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(number);
            }
        } else if (number.matches("[\\x00-\\x20]*[+-]?(Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.(\\p{Digit}+)([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*")) {
            try {
                return RealNumber.get(Double.parseDouble(number));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(number);
            }
        } else if (number.compareTo("PI") == 0) {
            return ConstantNumber.get(ConstantType.PI);
        } else if (number.compareTo("E") == 0) {
            return ConstantNumber.get(ConstantType.E);
        } else {
            throw new IllegalArgumentException(number);
        }
    }

    static ArrayList<BaseNumber> parseValues(String input) {
        ArrayList<BaseNumber> list = new ArrayList<>();
        String[] tokens = input.replace("{", "").replace("}", "").replace(" ", "").split(",");

        for (String number : tokens) {
            list.add(parseNumber(number));
        }

        return list;
    }
}
