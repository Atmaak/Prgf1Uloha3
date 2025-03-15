package utils;

import structure.Vectorizable;

public class Lerp<T extends Vectorizable<T>> {
    // Vertex AB = A.mul(1 - tAB).add(b.mul(tAB));
    public T lerp(T v1, T v2, double t) {
        return v1.mul(1 - t).add(v2.mul(t));
    }
}
