package com.erebor.summer;

/**
 * Summer
 * Created on 15.05.17.
 */
class Position {
    private float curI, curJ;
    private float stepI, stepJ;
    private int value, prevValue, nextValue;

    Position(int i, int j) {
        curI = i;
        curJ = j;

        value = 0;

        prevValue = -1;
        nextValue = -1;
    }

    public void dubValue() {
        value *= 2;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        nextValue = -1;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public float getCurI() {
        return curI;
    }

    public void setCurI(float curI) {
        this.curI = curI;
    }

    public float getCurJ() {
        return curJ;
    }

    public void setCurJ(float curJ) {
        this.curJ = curJ;
    }

    public void step() {
        curI += stepI;
        curJ += stepJ;
    }

    public void fixed() {
        prevValue = value;
    }

    public void calculate(int i, int j) {
        if (value == 0) {
            stepI = 0.0f;
            stepJ = 0.0f;
        } else {
            stepI = (i - curI) / 20;
            stepJ = (j - curJ) / 20;
        }

        nextValue = value;
        value = prevValue;
    }

    public void toNextState() {
        if (nextValue == -1)
            return;
        value = nextValue;
        nextValue = -1;
    }

    public int getNextValue() {
        return nextValue;
    }
}
