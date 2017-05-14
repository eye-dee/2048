package com.erebor.summer;

import com.erebor.summer.exception.IllegalMovementException;

class Movement {
    static final int MOVE_LEFT = 0,
            MOVE_UP = 1,
            MOVE_RIGHT = 2,
            MOVE_DOWN = 3;

    private int type;
    private boolean isMain;

    Movement(int t, boolean m) throws IllegalMovementException {
        if (t != MOVE_UP && t != MOVE_DOWN && t != MOVE_LEFT && t != MOVE_RIGHT)
            throw new IllegalMovementException();

        isMain = m;
        type = t;
    }

    int getDx(){
        if (type == MOVE_LEFT)
            return -1;
        if (type == MOVE_RIGHT)
            return 1;
        return 0;
    }

    int getDy(){
        if (type == MOVE_UP)
            return -1;
        if (type == MOVE_DOWN)
            return 1;
        return 0;
    }

    public int getType() {
        return type;
    }

    public boolean isMain() {
        return isMain;
    }
}
