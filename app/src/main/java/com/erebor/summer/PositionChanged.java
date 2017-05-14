package com.erebor.summer;

/**
 * Summer
 * Created on 15.05.17.
 */
class PositionChanged{
    private int i;
    private int j;
    private int value;
    PositionChanged(int setI, int setJ, int val){
        i = setI;
        j = setJ;
        value = val;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public void setI(int i) {
        this.i = i;
    }
    public void setJ(int j) {
        this.j = j;
    }
    public int getValue() {
        return value;
    }
    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }
}
