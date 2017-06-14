package com.opencmath;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

class TestHelper {
    private static final String PRINT_STRING = "    %27.16e  ==  %27.16e  =>  %11.5e %% (%s)\n";

    static void compareResultAndPut(CBase expected, CBase output) {
        assertEquals(expected, output);
        printDifference(expected, output);
        CBase.put(expected);
        CBase.put(output);
    }

    static CBase parseValue(String input) {
        input = input.replace("{", "").replace("}", "").replace(" ", "");
        return parseNumber(input);
    }

    private static void printDifference(CBase a, CBase b) {
        assertEquals(a.type, b.type);

        switch (a.type) {
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

    private static double getValue(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                CNumber cNumber = (CNumber) number;

                if (cNumber.im != 0) {
                    return Math.sqrt(cNumber.re * cNumber.re + cNumber.im * cNumber.im);
                } else {
                    return cNumber.re;
                }
            }
            case MATRIX: {
                //CBase tmp = CBase.duplicate(number);
                CBase tmp = CNumber.get(0, 0);
                double value = getValue(tmp);
                CBase.put(tmp);
                return value;
            }
        }

        throw new IllegalArgumentException();
    }

    private static CBase parseNumber(String number) {
        if (number.startsWith("M")) {
            ArrayList<CBase> list = new ArrayList<>();
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

            return CMatrix.get(cols, rows, list.toArray(new CBase[0]));
        } else if ((number.compareTo("ComplexInfinity") == 0) || (number.compareTo("NaN") == 0)) {
            return CNumber.get(Double.NaN, Double.NaN);
        } else if (number.equals("Infinity")) {
            return CNumber.get(Double.POSITIVE_INFINITY, 0);
        } else if (number.equals("-Infinity")) {
            return CNumber.get(Double.NEGATIVE_INFINITY, 0);
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

            return CNumber.get(re, im);
        } else if (number.matches("[\\x00-\\x20]*[+-]?(Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.(\\p{Digit}+)([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*")) {
            try {
                return CNumber.get(Double.parseDouble(number), 0);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(number);
            }
        } else if (number.compareTo("PI") == 0) {
            return CNumber.get(Math.PI, 0);
        } else if (number.compareTo("E") == 0) {
            return CNumber.get(Math.E, 0);
        } else {
            throw new IllegalArgumentException(number);
        }
    }
}
