package tfar.collapsecycle;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

public enum Scaling {
    LINEAR(d -> d),QUADRATIC(d -> d * d),CUBIC(d -> d * d * d);
    public final Double2DoubleFunction function;

    Scaling(Double2DoubleFunction function) {
        this.function = function;
    }
}
