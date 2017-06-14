package com.opencmath;

import java.util.ArrayList;

class PoolTemplate<T> {
    private final ArrayList<T> pool;
    private final PoolFactory<T> factory;
    private final int maxSize;
    private int currentSize;

    PoolTemplate(int initialSize, int maximumSize, PoolFactory<T> poolFactory) {
        maxSize = maximumSize;
        currentSize = initialSize;
        factory = poolFactory;
        pool = new ArrayList<>();

        for (int i = 0; i < initialSize; i++) {
            pool.add(factory.create());
        }
    }

    synchronized void put(T t) {
        pool.add(t);
    }

    synchronized T get() {
        if (pool.size() == 0) {
            if (currentSize < maxSize) {
                if (currentSize * 2 > maxSize) {
                    int i;
                    for (i = 0; i < maxSize - currentSize; i++) {
                        pool.add(factory.create());
                    }
                    currentSize += i;
                } else {
                    for (int i = 0; i < currentSize; i++) {
                        pool.add(factory.create());
                    }

                    currentSize += currentSize;
                }
            } else {
                throw new RuntimeException("Maximum number of pool object removed, no more data");
            }
        }

        return pool.remove(pool.size() - 1);
    }

    synchronized boolean checkConsistency() {
        for (int i = 0; i < pool.size() - 1; i++) {
            T t1 = pool.get(i);

            for (int j = i + 1; j < pool.size(); j++) {
                T t2 = pool.get(j);

                if (t1 == t2) {
                    return false;
                }
            }
        }

        return true;
    }

    synchronized int size() {
        return pool.size();
    }
}
