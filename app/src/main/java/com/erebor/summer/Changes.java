package com.erebor.summer;

import java.util.ArrayList;

class Changes
{
    private final ArrayList<PositionChanged> arrayList;

    Changes(){
        arrayList = new ArrayList<>();
    }

    public void addNewChange(int i, int j, int value){
        arrayList.add(new PositionChanged(i,j,value));
    }

    public void makeChanges(Bitmap2048 bitmap2048){
        for (PositionChanged i : arrayList){
            bitmap2048.setValue(i.getI(), i.getJ(), i.getValue());
        }
    }
}

