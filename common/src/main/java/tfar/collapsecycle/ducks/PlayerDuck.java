package tfar.collapsecycle.ducks;

public interface PlayerDuck {
    int timeInBeam();
    void setTimeInBeam(int timeInBeam);
    default void reset() {
        setTimeInBeam(0);
    }
}
