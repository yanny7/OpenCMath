package com.opencmath;

import java.util.Arrays;

class MatrixNumber extends BaseNumber {
    private static final PoolTemplate<MatrixNumber> pool = new PoolTemplate<>(100, 100000, new PoolFactory<MatrixNumber>() {
        @Override
        public MatrixNumber create() {
            return new MatrixNumber();
        }
    });

    BaseNumber[] value;
    byte cols;
    byte rows;

    private MatrixNumber() {
        super(NumberType.MATRIX);
        cols = 0;
        rows = 0;
        value = new BaseNumber[0];
    }

    private void resizeMatrix(byte newCols, byte newRows) {
        this.cols = newCols;
        this.rows = newRows;

        for (BaseNumber num : value) {
            if (num != null) {
                put(num);
            }
        }

        value = new BaseNumber[newCols * newRows];

        for (int i = 0; i < value.length; i++) {
            value[i] = null;
        }
    }

    static MatrixNumber get(byte cols, byte rows) {
        MatrixNumber number = pool.get();
        number.cols = cols;
        number.rows = rows;
        return number;
    }

    static MatrixNumber get(byte cols, byte rows, BaseNumber[] items) {
        if (items.length != cols * rows) {
            throw new IllegalArgumentException();
        }

        MatrixNumber matrix = pool.get();
        matrix.resizeMatrix(cols, rows);

        System.arraycopy(items, 0, matrix.value, 0, items.length);

        return matrix;
    }

    static void put(MatrixNumber item) {
        for (int i = 0; i < item.value.length; i++) {
            if (item.value[i] != null) {
                put(item.value[i]);
            }

            item.value[i] = null;
        }

        item.cols = 0;
        item.rows = 0;
        item.value = new BaseNumber[0];
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

        MatrixNumber matrixNumber = (MatrixNumber) obj;

        if ((matrixNumber.cols != cols) || (matrixNumber.rows != rows)) {
            return false;
        }

        if (value.length != matrixNumber.value.length) {
            return false;
        }

        for (int i = 0; i < value.length; i++) {
            if (!value[i].equals(matrixNumber.value[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Matrix[" + cols + "x" + rows + "]" + Arrays.deepToString(value);
    }

    @Override
    public BaseNumber add(BaseNumber number) {
        switch (number.type) {
            case INVALID:
                put(this);
                return number;
            case INTEGER:
            case REAL:
            case CONSTANT:
            case COMPLEX:
                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].add(duplicate(number));
                }

                put(number);
                return simplify(this);
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                if ((matrixNumber.cols != cols) || (matrixNumber.rows != rows)) {
                    put(this);
                    put(number);
                    return InvalidNumber.get();
                }

                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].add(matrixNumber.value[i]);
                }

                matrixNumber.value = new BaseNumber[0];
                put(matrixNumber);
                return simplify(this);
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
            case INTEGER:
            case REAL:
            case CONSTANT:
            case COMPLEX:
                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].sub(duplicate(number));
                }

                put(number);
                return simplify(this);
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                if ((matrixNumber.cols != cols) || (matrixNumber.rows != rows)) {
                    put(this);
                    put(number);
                    return InvalidNumber.get();
                }

                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].sub(matrixNumber.value[i]);
                }

                matrixNumber.value = new BaseNumber[0];
                put(matrixNumber);
                return simplify(this);
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
            case INTEGER:
            case REAL:
            case CONSTANT:
            case COMPLEX:
                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].mul(duplicate(number));
                }

                put(number);
                return simplify(this);
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;
                byte matrixNumberCols = matrixNumber.cols;
                byte matrixNumberRows = matrixNumber.rows;

                if ((rows != matrixNumberCols) || (cols != matrixNumberRows)) {
                    put(this);
                    put(number);
                    return InvalidNumber.get();
                }

                BaseNumber[] items = new BaseNumber[rows * matrixNumberCols];

                int k = 0;
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < matrixNumberCols; j++) {
                        BaseNumber item = IntegerNumber.get(0);

                        for (int m = 0; m < cols; m++) {
                            item = item.add(duplicate(value[i * cols + m]).mul(duplicate(matrixNumber.value[m * matrixNumberCols + j])));
                        }

                        items[k] = item;
                        k++;
                    }
                }

                byte tmpRows = rows;
                put(matrixNumber);
                put(this);
                return simplify(MatrixNumber.get(matrixNumberCols, tmpRows, items));
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
            case INTEGER:
            case REAL:
            case CONSTANT:
            case COMPLEX:
                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].div(duplicate(number));
                }

                put(number);
                return simplify(this);
            case MATRIX: {
                MatrixNumber matrixNumber = (MatrixNumber) number;

                if ((matrixNumber.cols != cols) || (matrixNumber.rows != rows) || (matrixNumber.cols != rows)) { // only square
                    put(this);
                    put(number);
                    return InvalidNumber.get();
                }

                number = inverse(matrixNumber);
                return simplify(mul(number));
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber arg() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].arg();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber abs() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].abs();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber ln() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].ln();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber log(BaseNumber base) {
        switch (base.type) {
            case INVALID:
            case MATRIX:
                put(base);
                put(this);
                return InvalidNumber.get();
            case INTEGER:
            case REAL:
            case CONSTANT:
            case COMPLEX: {
                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].log(duplicate(base));
                }

                put(base);
                return simplify(this);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber exp() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].exp();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber pow(BaseNumber exp) {
        switch (exp.type) {
            case INVALID:
            case MATRIX:
                put(exp);
                put(this);
                return InvalidNumber.get();
            case INTEGER: {
                IntegerNumber integerNumber = (IntegerNumber) exp;
                if (integerNumber.value == -1) {
                    return inv();
                }
            }
            case REAL:
            case CONSTANT:
            case COMPLEX: {
                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].pow(duplicate(exp));
                }

                put(exp);
                return simplify(this);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber sqrt() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].sqrt();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber root(BaseNumber exp) {
        switch (exp.type) {
            case INVALID:
            case MATRIX:
                put(exp);
                put(this);
                return InvalidNumber.get();
            case INTEGER:
            case REAL:
            case CONSTANT:
            case COMPLEX: {
                for (int i = 0; i < cols * rows; i++) {
                    value[i] = value[i].root(duplicate(exp));
                }

                put(exp);
                return simplify(this);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public BaseNumber factorial() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].factorial();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber sin() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].sin();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber cos() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].cos();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber tan() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].tan();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber cot() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].cot();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber sec() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].sec();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber csc() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].csc();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber asin() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].asin();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber acos() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].acos();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber atan() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].atan();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber acot() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].acot();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber asec() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].asec();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber acsc() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].acsc();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber sinh() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].sinh();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber cosh() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].cosh();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber tanh() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].tanh();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber coth() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].coth();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber sech() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].sech();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber csch() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].csch();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber asinh() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].asinh();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber acosh() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].acosh();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber atanh() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].atanh();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber acoth() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].acoth();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber asech() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].asech();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber acsch() {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].acsch();
        }

        return simplify(this);
    }

    @Override
    public BaseNumber and(BaseNumber number) {
        put(number);
        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber or(BaseNumber number) {
        put(number);
        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber xor(BaseNumber number) {
        put(number);
        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber not() {
        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber shl(BaseNumber number) {
        put(number);
        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber shr(BaseNumber number) {
        put(number);
        put(this);
        return InvalidNumber.get();
    }

    @Override
    public BaseNumber det() {
        return determinant(this);
    }

    @Override
    public BaseNumber transpose() {
        return transpose(this);
    }

    @Override
    public BaseNumber trace() {
        if (cols != rows) {
            put(this);
            return InvalidNumber.get();
        }

        BaseNumber sum = IntegerNumber.get(0);

        for (int i = 0; i < rows; i++) {
            sum.add(duplicate(value[i * cols + i]));
        }

        put(this);
        return simplify(sum);
    }

    @Override
    public BaseNumber adjugate() {
        return adjugate(this);
    }

    @Override
    public BaseNumber rank() {
        if (value.length == 1) {
            BaseNumber tmp;

            if ((value[0] instanceof IntegerNumber) && (((IntegerNumber) value[0]).value == 0)) {
                tmp = IntegerNumber.get(0);
            } else {
                tmp = IntegerNumber.get(1);
            }

            put(this);
            return tmp;
        }

        MatrixNumber m = gaussElimination(this);

        int count = 0;
        for (int i = 0; i < ((m.cols < m.rows) ? m.cols : m.rows); i++) {
            BaseNumber number = m.value[i * m.cols + i];
            if (!((number instanceof IntegerNumber) && (((IntegerNumber) number).value == 0))) {
                count++;
            }
        }

        put(m);
        return IntegerNumber.get(count);
    }

    @Override
    public BaseNumber inv() {
        return inverse(this);
    }

    @Override
    public BaseNumber toRadians(AngleType angle) {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].toRadians(angle);
        }

        return simplify(this);
    }

    @Override
    public BaseNumber fromRadians(AngleType angle) {
        for (int i = 0; i < cols * rows; i++) {
            value[i] = value[i].fromRadians(angle);
        }

        return simplify(this);
    }

    private static MatrixNumber gaussElimination(MatrixNumber m) {
        if (m.value.length == 1) {
            put(m.value[0]);
            m.value[0] = IntegerNumber.get(1);
            return m;
        }

        upperTriangleNormalize(m);

        //
        // 2 6  8 14    1 3 4 7
        // 1 4  8 15 -> 0 1 4 8
        // 2 7 15 31    0 0 1 3
        //
        int col = 0;
        for (int i = 0; i < m.rows; i++) {
            for (int j = col; j < m.cols; j++) {
                BaseNumber number = m.value[i * m.cols + col];
                if ((number instanceof IntegerNumber) && (((IntegerNumber) number).value == 0)) { // switch to next column if this is zero
                    col++;
                } else {
                    break;
                }
            }

            if (col >= m.cols) { // row with zeroes
                break;
            }

            BaseNumber ref = m.value[i * m.cols + col];
            for (int k = col + 1; k < m.cols; k++) {
                m.value[i * m.cols + k] = m.value[i * m.cols + k].div(duplicate(ref));
            }
            m.value[i * m.cols + col] = IntegerNumber.get(1);
            put(ref);

            for (int j = i + 1; j < m.rows; j++) {
                BaseNumber number = m.value[j * m.cols + col];
                if ((number instanceof IntegerNumber) && (((IntegerNumber) number).value == 0)) {
                    break;
                }

                for (int k = col + 1; k < m.cols; k++) {
                    m.value[j * m.cols + k] = m.value[j * m.cols + k].sub(duplicate(m.value[i * m.cols + k]).mul(duplicate(m.value[j * m.cols + col])));
                }
                put(m.value[j * m.cols + col]);
                m.value[j * m.cols + col] = IntegerNumber.get(0);
            }

            upperTriangleNormalize(m);
        }

        //
        // 1 3 4 7      1 0 0  7
        // 0 1 4 8  ->  0 1 0 -4
        // 0 0 1 3      0 0 1  3
        //
        col = 0;
        for (int i = 1; i < m.rows; i++) {
            for (int j = col; j < m.cols; j++) {
                BaseNumber number = m.value[i * m.cols + col];
                if ((number instanceof IntegerNumber) && (((IntegerNumber) number).value == 0)) { // switch to next column if this is zero
                    col++;
                } else {
                    break;
                }
            }

            if (col >= m.cols) { // row with zeroes
                break;
            }

            for (int j = 0; j < i; j++) {
                for (int k = col + 1; k < m.cols; k++) {
                    m.value[j * m.cols + k] = m.value[j * m.cols + k].sub(duplicate(m.value[i * m.cols + k]).mul(duplicate(m.value[j * m.cols + col])));
                }

                put(m.value[j * m.cols + col]);
                m.value[j * m.cols + col] = IntegerNumber.get(0);
            }
        }

        return m;
    }

    private static void upperTriangleNormalize(MatrixNumber matrix) {
        if (matrix.rows == 1) {
            return;
        }

        int nullCol = 0;
        for (int i = 0; i < matrix.rows; i++) {
            BaseNumber number = matrix.value[i * matrix.cols + nullCol];
            if (!((number instanceof IntegerNumber) && (((IntegerNumber) number).value == 0)) && (i + 1 < matrix.rows)) {
                continue;
            }

            boolean found = false;
            boolean swapped = false;
            for (int j = i + 1; j < matrix.rows; j++) {
                BaseNumber number2 = matrix.value[j * matrix.cols + nullCol];
                if (!((number2 instanceof IntegerNumber) && (((IntegerNumber) number2).value == 0))) {
                    if (!swapped) {
                        swapRows(matrix, i, j);
                    }
                    swapped = true;
                } else {
                    found = true;
                }
            }

            if (found) {
                nullCol += 1;

                if (nullCol >= matrix.cols) {
                    break; // no more nul columns
                }
            } else {
                break; // no more null columns
            }
        }
    }

    private static void swapRows(MatrixNumber matrix, int from, int to) {
        if ((matrix.rows <= from) || (matrix.rows <= to)) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < matrix.cols; i++) {
            BaseNumber tmp = matrix.value[matrix.cols * to + i];
            matrix.value[matrix.cols * to + i] = matrix.value[matrix.cols * from + i];
            matrix.value[matrix.cols * from + i] = tmp;
        }
    }

    private static BaseNumber inverse(MatrixNumber m) {
        if (m.cols != m.rows) {
            put(m);
            return InvalidNumber.get();
        }

        if (m.value.length == 1) {
            m.value[0] = IntegerNumber.get(1).div(m.value[0]);
            return m;
        }

        MatrixNumber tmp = (MatrixNumber) duplicate(m);
        BaseNumber result = adjugate(m).div(determinant(tmp));
        return simplify(result);
    }

    private static BaseNumber adjugate(MatrixNumber m) {
        if (m.cols != m.rows) {
            put(m);
            return InvalidNumber.get();
        }

        if (m.value.length == 1) {
            put(m.value[0]);
            m.value[0] = IntegerNumber.get(1);
            return simplify(m);
        }

        BaseNumber[] data = new BaseNumber[m.value.length];
        int k = 0;

        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                BaseNumber[] subData = new BaseNumber[(m.cols - 1) * (m.rows - 1)];
                BaseNumber subDet = ((i + j) % 2 == 1) ? IntegerNumber.get(-1) : IntegerNumber.get(1);

                int c = 0;
                int d = 0; // second row
                for (int a = 0; a < m.rows; a++) {
                    for (int b = 0; b < m.cols; b++) {
                        if ((b != i) && (a != j)) {
                            subData[c] = duplicate(m.value[d]);
                            c++;
                        }

                        d++;
                    }
                }

                data[k] = subDet.mul(MatrixNumber.get((byte) (m.cols - 1), (byte) (m.rows - 1), subData).det());
                k++;
            }
        }

        byte tmpCols = m.cols;
        byte tmpRows = m.rows;
        put(m);
        return simplify(MatrixNumber.get(tmpRows, tmpCols, data));
    }

    private static BaseNumber transpose(MatrixNumber m) {
        int k = 0;
        byte cols = m.cols;
        byte rows = m.rows;
        BaseNumber[] data = new BaseNumber[cols * rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[j * rows + i] = duplicate(m.value[k]);
                k++;
            }
        }

        put(m);
        return MatrixNumber.get(rows, cols, data);
    }

    private static BaseNumber determinant(MatrixNumber m) {
        if (m.cols != m.rows) {
            put(m);
            return InvalidNumber.get();
        }

        switch (m.cols) {
            case 1: {
                BaseNumber number = duplicate(m.value[0]);
                put(m);
                return simplify(number);
            }
            case 2: {
                BaseNumber number = duplicate(m.value[0]).mul(duplicate(m.value[3])).sub(duplicate(m.value[1]).mul(duplicate(m.value[2])));
                put(m);
                return simplify(number);
            }
            case 3: {
                BaseNumber positive1 = duplicate(m.value[0]).mul(duplicate(m.value[4])).mul(duplicate(m.value[8]));
                BaseNumber positive2 = duplicate(m.value[2]).mul(duplicate(m.value[3])).mul(duplicate(m.value[7]));
                BaseNumber positive3 = duplicate(m.value[1]).mul(duplicate(m.value[5])).mul(duplicate(m.value[6]));
                BaseNumber positive = positive1.add(positive2.add(positive3));
                BaseNumber negative1 = duplicate(m.value[0]).mul(duplicate(m.value[5])).mul(duplicate(m.value[7]));
                BaseNumber negative2 = duplicate(m.value[1]).mul(duplicate(m.value[3])).mul(duplicate(m.value[8]));
                BaseNumber negative3 = duplicate(m.value[2]).mul(duplicate(m.value[4])).mul(duplicate(m.value[6]));
                BaseNumber negative = negative1.add(negative2).add(negative3);
                BaseNumber number = positive.sub(negative);
                put(m);
                return simplify(number);
            }
            default: // cols > 3
                BaseNumber det = IntegerNumber.get(0);

                for (int i = 0; i < m.cols; i++) {
                    BaseNumber[] data = new BaseNumber[(m.cols - 1) * (m.rows - 1)];
                    BaseNumber subDet = duplicate(m.value[i]).mul((i % 2 == 1) ? IntegerNumber.get(-1) : IntegerNumber.get(1));

                    int c = 0;
                    int d = m.cols; // second row
                    for (int a = 1; a < m.rows; a++) {
                        for (int b = 0; b < m.cols; b++) {
                            if (b != i) {
                                data[c] = duplicate(m.value[d]);
                                c++;
                            }

                            d++;
                        }
                    }

                    subDet = subDet.mul(determinant(MatrixNumber.get((byte) (m.cols - 1), (byte) (m.rows - 1), data)));
                    det = det.add(subDet);
                }

                put(m);
                return simplify(det);
        }
    }
}
