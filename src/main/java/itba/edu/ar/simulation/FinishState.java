package itba.edu.ar.simulation;

public class FinishState {
    public final double time;
    public final int num_zombies;
    public final int num_humans;

    public FinishState(double time, int num_zombies, int num_humans) {
        this.time = time;
        this.num_zombies = num_zombies;
        this.num_humans = num_humans;
    }
}
