package com.opencmath;

class InvalidNumber extends BaseNumber {
    private static final PoolTemplate<InvalidNumber> pool = new PoolTemplate<>(100, 100000, new PoolFactory<InvalidNumber>() {
        @Override
        public InvalidNumber create() {
            return new InvalidNumber();
        }
    });

    private InvalidNumber() {
        super(NumberType.INVALID);
    }

    static InvalidNumber get() {
        return pool.get();
    }

    static void put(InvalidNumber item) {
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

        return true;
    }

    @Override
    public String toString() {
        return "NaN";
    }

    @Override
    public BaseNumber add(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber sub(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber mul(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber div(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber arg() {
        return this;
    }

    @Override
    public BaseNumber abs() {
        return this;
    }

    @Override
    public BaseNumber ln() {
        return this;
    }

    @Override
    public BaseNumber log(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber exp() {
        return this;
    }

    @Override
    public BaseNumber pow(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber sqrt() {
        return this;
    }

    @Override
    public BaseNumber root(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber factorial() {
        return this;
    }

    @Override
    public BaseNumber sin() {
        return this;
    }

    @Override
    public BaseNumber cos() {
        return this;
    }

    @Override
    public BaseNumber tan() {
        return this;
    }

    @Override
    public BaseNumber cot() {
        return this;
    }

    @Override
    public BaseNumber sec() {
        return this;
    }

    @Override
    public BaseNumber csc() {
        return this;
    }

    @Override
    public BaseNumber asin() {
        return this;
    }

    @Override
    public BaseNumber acos() {
        return this;
    }

    @Override
    public BaseNumber atan() {
        return this;
    }

    @Override
    public BaseNumber acot() {
        return this;
    }

    @Override
    public BaseNumber asec() {
        return this;
    }

    @Override
    public BaseNumber acsc() {
        return this;
    }

    @Override
    public BaseNumber sinh() {
        return this;
    }

    @Override
    public BaseNumber cosh() {
        return this;
    }

    @Override
    public BaseNumber tanh() {
        return this;
    }

    @Override
    public BaseNumber coth() {
        return this;
    }

    @Override
    public BaseNumber sech() {
        return this;
    }

    @Override
    public BaseNumber csch() {
        return this;
    }

    @Override
    public BaseNumber asinh() {
        return this;
    }

    @Override
    public BaseNumber acosh() {
        return this;
    }

    @Override
    public BaseNumber atanh() {
        return this;
    }

    @Override
    public BaseNumber acoth() {
        return this;
    }

    @Override
    public BaseNumber asech() {
        return this;
    }

    @Override
    public BaseNumber acsch() {
        return this;
    }

    @Override
    public BaseNumber and(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber or(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber xor(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber not() {
        return this;
    }

    @Override
    public BaseNumber shl(BaseNumber number) {
        put(number);
        return this;
    }

    @Override
    public BaseNumber shr(BaseNumber number) {
        put(number);
        return this;
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
        return this;
    }

    @Override
    public BaseNumber rank() {
        return this;
    }

    @Override
    public BaseNumber inv() {
        return this;
    }

    @Override
    public BaseNumber toRadians(AngleType angle) {
        return this;
    }

    @Override
    public BaseNumber fromRadians(AngleType angle) {
        return this;
    }
}
