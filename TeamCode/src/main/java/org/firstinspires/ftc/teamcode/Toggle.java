package org.firstinspires.ftc.teamcode;

//Class that acts as a boolean that turns false when true and true when false
public class Toggle {
    private boolean currentState = false;
    public Toggle() {

    }
    public Toggle(boolean initialState) {
        currentState = initialState;
    }
    public void input(boolean initialState) {
        currentState = initialState;
    }
    public void toggle() {
        currentState = !currentState;
    }

    public boolean output() {
        return currentState;
    }
}
