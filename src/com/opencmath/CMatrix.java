package com.opencmath;

final class CMatrix extends CBase {

    private static final PoolTemplate<CMatrix> pool = new PoolTemplate<>(10, 100000, new PoolFactory<CMatrix>() {
        @Override
        public CMatrix create() {
            return new CMatrix();
        }
    });

    CBase[] items;
    short rows;
    short cols;

    private CMatrix() {
        super(NumberType.MATRIX);
        items = new CNumber[0];
        rows = 0;
        cols = 0;
    }

    @Override
    public CBase add(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].add(duplicate(number));
                }
                put(number);
                return this;
            }
            case MATRIX: {
                CMatrix matrix = (CMatrix) number;
                if ((rows != matrix.rows) || (cols != matrix.cols)) {
                    put(number);
                    put(this);
                    return getNaN();
                }

                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].add(duplicate(matrix.items[i]));
                }

                put(number);
                return this;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase sub(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].sub(duplicate(number));
                }
                put(number);
                return this;
            }
            case MATRIX: {
                CMatrix matrix = (CMatrix) number;
                if ((rows != matrix.rows) || (cols != matrix.cols)) {
                    put(number);
                    put(this);
                    return getNaN();
                }

                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].sub(duplicate(matrix.items[i]));
                }

                put(number);
                return this;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase mul(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].mul(duplicate(number));
                }
                put(number);
                return this;
            }
            case MATRIX: {
                CMatrix matrix = (CMatrix) number;
                if ((rows != matrix.rows) || (cols != matrix.cols)) {
                    put(number);
                    put(this);
                    return getNaN();
                }

                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].mul(duplicate(matrix.items[i]));
                }

                put(number);
                return this;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase div(CBase number) {
        switch (number.type) {
            case COMPLEX: {
                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].div(duplicate(number));
                }
                put(number);
                return this;
            }
            case MATRIX: {
                CMatrix matrix = (CMatrix) number;
                if ((rows != matrix.rows) || (cols != matrix.cols)) {
                    put(number);
                    put(this);
                    return getNaN();
                }

                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].div(duplicate(matrix.items[i]));
                }

                put(number);
                return this;
            }
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public CBase arg() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].arg();
        }

        return this;
    }

    @Override
    public CBase ln() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].ln();
        }

        return this;
    }

    @Override
    public CBase log(CBase base) {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].log(duplicate(base));
        }

        put(base);
        return this;
    }

    @Override
    public CBase exp() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].exp();
        }

        return this;
    }

    @Override
    public CBase abs() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].abs();
        }

        return this;
    }

    @Override
    public CBase pow(CBase exponent) {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].log(duplicate(exponent));
        }

        put(exponent);
        return this;
    }

    @Override
    public CBase root(CBase index) {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].log(duplicate(index));
        }

        put(index);
        return this;
    }

    @Override
    public CBase sqrt() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].sqrt();
        }

        return this;
    }

    @Override
    public CBase factorial() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].factorial();
        }

        return this;
    }

    @Override
    public CBase sin() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].sin();
        }

        return this;
    }

    @Override
    public CBase cos() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].cos();
        }

        return this;
    }

    @Override
    public CBase tan() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].tan();
        }

        return this;
    }

    @Override
    public CBase cot() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].cot();
        }

        return this;
    }

    @Override
    public CBase sec() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].sec();
        }

        return this;
    }

    @Override
    public CBase csc() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].csc();
        }

        return this;
    }

    @Override
    public CBase sinh() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].sinh();
        }

        return this;
    }

    @Override
    public CBase cosh() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].cosh();
        }

        return this;
    }

    @Override
    public CBase tanh() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].tanh();
        }

        return this;
    }

    @Override
    public CBase coth() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].coth();
        }

        return this;
    }

    @Override
    public CBase sech() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].sech();
        }

        return this;
    }

    @Override
    public CBase csch() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].csch();
        }

        return this;
    }

    @Override
    public CBase asin() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].asin();
        }

        return this;
    }

    @Override
    public CBase acos() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].acos();
        }

        return this;
    }

    @Override
    public CBase atan() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].atan();
        }

        return this;
    }

    @Override
    public CBase acot() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].acot();
        }

        return this;
    }

    @Override
    public CBase asec() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].asec();
        }

        return this;
    }

    @Override
    public CBase acsc() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].acsc();
        }

        return this;
    }

    @Override
    public CBase asinh() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].asinh();
        }

        return this;
    }

    @Override
    public CBase acosh() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].acosh();
        }

        return this;
    }

    @Override
    public CBase atanh() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].atanh();
        }

        return this;
    }

    @Override
    public CBase acoth() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].acoth();
        }

        return this;
    }

    @Override
    public CBase asech() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].asech();
        }

        return this;
    }

    @Override
    public CBase acsch() {
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].acsch();
        }

        return this;
    }

    @Override
    public CBase toRadians(AngleType angleType) {
        for (int i = 0; i < cols * rows; i++) {
            items[i] = items[i].toRadians(angleType);
        }

        return this;
    }

    @Override
    public CBase fromRadians(AngleType angleType) {
        for (int i = 0; i < cols * rows; i++) {
            items[i] = items[i].fromRadians(angleType);
        }

        return this;
    }

    @Override
    public CBase inv() {
        if (cols != rows) {
            put(this);
            return getNaN();
        }

        if (items.length == 1) {
            items[0] = CNumber.get(1, 0).div(items[0]);
            return this;
        }

        CMatrix tmp = (CMatrix) duplicate(this);
        return adjugate().div(tmp.det());
    }

    @Override
    public CBase transpose() {
        int k = 0;
        short tmpCols = cols;
        short tmpRows = rows;
        CBase[] data = new CBase[cols * rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[j * rows + i] = duplicate(items[k]);
                k++;
            }
        }

        put(this);
        return CMatrix.get(tmpRows, tmpCols, data);
    }

    @Override
    public CBase det() {
        if (cols != rows) {
            put(this);
            return getNaN();
        }

        switch (cols) {
            case 1: {
                CBase number = duplicate(items[0]);
                put(this);
                return number;
            }
            case 2: {
                CBase number = duplicate(items[0]).mul(duplicate(items[3])).sub(duplicate(items[1]).mul(duplicate(items[2])));
                put(this);
                return number;
            }
            case 3: {
                CBase positive1 = duplicate(items[0]).mul(duplicate(items[4])).mul(duplicate(items[8]));
                CBase positive2 = duplicate(items[2]).mul(duplicate(items[3])).mul(duplicate(items[7]));
                CBase positive3 = duplicate(items[1]).mul(duplicate(items[5])).mul(duplicate(items[6]));
                CBase positive = positive1.add(positive2.add(positive3));
                CBase negative1 = duplicate(items[0]).mul(duplicate(items[5])).mul(duplicate(items[7]));
                CBase negative2 = duplicate(items[1]).mul(duplicate(items[3])).mul(duplicate(items[8]));
                CBase negative3 = duplicate(items[2]).mul(duplicate(items[4])).mul(duplicate(items[6]));
                CBase negative = negative1.add(negative2).add(negative3);
                CBase number = positive.sub(negative);
                put(this);
                return number;
            }
            default: // cols > 3
                CBase det = CNumber.get(0, 0);

                for (int i = 0; i < cols; i++) {
                    CBase[] data = new CBase[(cols - 1) * (rows - 1)];
                    CBase subDet = duplicate(items[i]).mul((i % 2 == 1) ? CNumber.get(-1, 0) : CNumber.get(1, 0));

                    int c = 0;
                    int d = cols; // second row
                    for (int a = 1; a < rows; a++) {
                        for (int b = 0; b < cols; b++) {
                            if (b != i) {
                                data[c] = duplicate(items[d]);
                                c++;
                            }

                            d++;
                        }
                    }

                    subDet = subDet.mul(CMatrix.get((byte) (cols - 1), (byte) (rows - 1), data).det());
                    det = det.add(subDet);
                }

                put(this);
                return det;
        }
    }

    @Override
    public CBase rank() {
        if (items.length == 1) {
            CBase tmp;

            if (items[0].isInteger() && (((CNumber) items[0]).re == 0)) {
                tmp = CNumber.get(0, 0);
            } else {
                tmp = CNumber.get(1, 0);
            }

            put(this);
            return tmp;
        }

        CMatrix m = gaussElimination(this);

        int count = 0;
        for (int i = 0; i < ((m.cols < m.rows) ? m.cols : m.rows); i++) {
            CBase number = m.items[i * m.cols + i];
            if (!(number.isInteger() && (((CNumber) number).re == 0))) {
                count++;
            }
        }

        put(m);
        return CNumber.get(count, 0);
    }

    @Override
    public CBase cond() {
        return null;
    }

    @Override
    public CBase trace() {
        if (cols != rows) {
            put(this);
            return getNaN();
        }

        CBase sum = CNumber.get(0, 0);

        for (int i = 0; i < rows; i++) {
            sum.add(duplicate(items[i * cols + i]));
        }

        put(this);
        return sum;
    }

    @Override
    public CBase adjugate() {
        if (cols != rows) {
            put(this);
            return getNaN();
        }

        if (items.length == 1) {
            put(items[0]);
            items[0] = CNumber.get(1, 0);
            return this;
        }

        CBase[] data = new CBase[items.length];
        int k = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CBase[] subData = new CBase[(cols - 1) * (rows - 1)];
                CBase subDet = ((i + j) % 2 == 1) ? CNumber.get(-1, 0) : CNumber.get(1, 0);

                int c = 0;
                int d = 0; // second row
                for (int a = 0; a < rows; a++) {
                    for (int b = 0; b < cols; b++) {
                        if ((b != i) && (a != j)) {
                            subData[c] = duplicate(items[d]);
                            c++;
                        }

                        d++;
                    }
                }

                data[k] = subDet.mul(CMatrix.get((byte) (cols - 1), (byte) (rows - 1), subData).det());
                k++;
            }
        }

        short tmpCols = cols;
        short tmpRows = rows;
        put(this);
        return CMatrix.get(tmpRows, tmpCols, data);
    }

    @Override
    public CBase shr(CBase count) {
        put(this);
        return getNaN();
    }

    @Override
    public CBase shl(CBase count) {
        put(this);
        return getNaN();
    }

    @Override
    public CBase and(CBase number) {
        put(this);
        return getNaN();
    }

    @Override
    public CBase or(CBase number) {
        put(this);
        return getNaN();
    }

    @Override
    public CBase xor(CBase number) {
        put(this);
        return getNaN();
    }

    @Override
    public CBase not() {
        put(this);
        return getNaN();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("M").append(rows).append("x").append(cols).append("[");

        for (CBase item : items) {
            sb.append(item.toString()).append("|");
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CMatrix) {
            CMatrix matrix = (CMatrix) other;

            if ((rows != matrix.rows) || (cols != matrix.cols)) {
                return false;
            }

            for (int i = 0; i < matrix.items.length; i++) {
                if (!items[i].equals(matrix.items[i])) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    static CMatrix get(short rows, short cols) {
        assert ((rows > 0) && (cols > 0));
        CMatrix number = pool.get();
        number.cols = cols;
        number.rows = rows;
        return number;
    }

    static CMatrix get(short rows, short cols, CBase[] items) {
        assert ((rows > 0) && (cols > 0) && (items != null) && (items.length == rows * cols));
        CMatrix number = pool.get();
        number.items = items;
        number.cols = cols;
        number.rows = rows;
        return number;
    }

    static void put(CMatrix matrix) {
        for (int i = 0; i < matrix.items.length; i++) {
            put(matrix.items[i]);
        }

        matrix.cols = 0;
        matrix.rows = 0;
        matrix.items = new CBase[0];
        pool.put(matrix);
    }

    static int poolSize() {
        return pool.size();
    }

    static boolean checkConsistency() {
        return pool.checkConsistency();
    }

    private static CMatrix gaussElimination(CMatrix m) {
        if (m.items.length == 1) {
            put(m.items[0]);
            m.items[0] = CNumber.get(1, 0);
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
                CBase number = m.items[i * m.cols + col];
                if (number.isInteger() && (((CNumber) number).re == 0)) { // switch to next column if this is zero
                    col++;
                } else {
                    break;
                }
            }

            if (col >= m.cols) { // row with zeroes
                break;
            }

            CBase ref = m.items[i * m.cols + col];
            for (int k = col + 1; k < m.cols; k++) {
                m.items[i * m.cols + k] = m.items[i * m.cols + k].div(duplicate(ref));
            }
            m.items[i * m.cols + col] = CNumber.get(1, 0);
            put(ref);

            for (int j = i + 1; j < m.rows; j++) {
                CBase number = m.items[j * m.cols + col];
                if (number.isInteger() && (((CNumber) number).re == 0)) {
                    break;
                }

                for (int k = col + 1; k < m.cols; k++) {
                    m.items[j * m.cols + k] = m.items[j * m.cols + k].sub(duplicate(m.items[i * m.cols + k]).mul(duplicate(m.items[j * m.cols + col])));
                }
                put(m.items[j * m.cols + col]);
                m.items[j * m.cols + col] = CNumber.get(0, 0);
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
                CBase number = m.items[i * m.cols + col];
                if (number.isInteger() && (((CNumber) number).re == 0)) { // switch to next column if this is zero
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
                    m.items[j * m.cols + k] = m.items[j * m.cols + k].sub(duplicate(m.items[i * m.cols + k]).mul(duplicate(m.items[j * m.cols + col])));
                }

                put(m.items[j * m.cols + col]);
                m.items[j * m.cols + col] = CNumber.get(0, 0);
            }
        }

        return m;
    }

    private static void upperTriangleNormalize(CMatrix matrix) {
        if (matrix.rows == 1) {
            return;
        }

        int nullCol = 0;
        for (int i = 0; i < matrix.rows; i++) {
            CBase number = matrix.items[i * matrix.cols + nullCol];
            if (!(number.isInteger() && (((CNumber) number).re == 0)) && (i + 1 < matrix.rows)) {
                continue;
            }

            boolean found = false;
            boolean swapped = false;
            for (int j = i + 1; j < matrix.rows; j++) {
                CBase number2 = matrix.items[j * matrix.cols + nullCol];
                if (!(number2.isInteger() && (((CNumber) number2).re == 0))) {
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

    private static void swapRows(CMatrix matrix, int from, int to) {
        if ((matrix.rows <= from) || (matrix.rows <= to)) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < matrix.cols; i++) {
            CBase tmp = matrix.items[matrix.cols * to + i];
            matrix.items[matrix.cols * to + i] = matrix.items[matrix.cols * from + i];
            matrix.items[matrix.cols * from + i] = tmp;
        }
    }

}
