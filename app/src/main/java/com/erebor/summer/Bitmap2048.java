package com.erebor.summer;

import com.erebor.summer.exception.IllegalRandomizeBitmap2048;

import java.util.Random;
import java.util.Stack;

class Bitmap2048 {
    private Position[][] A;
    private String[][] Astr;
    private int N;
    private final Random r = new Random();
    private final Stack<Changes> prevState = new Stack<>();

    Bitmap2048(int setN) {
        A = new Position[setN][setN];

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j] = new Position(i, j);

        Astr = new String[setN][setN];
        N = setN;

        allocateStrings();
    }

    Bitmap2048() {
    }

    private void allocateStrings() {
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                Astr[i][j] = "";
    }

    public void allocA(int setN) {
        A = new Position[setN][setN];
        Astr = new String[setN][setN];
        N = setN;

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j] = new Position(i, j);

        allocateStrings();
    }

    public int getN() {
        return N;
    }

    public int getValue(int i, int j) {
        return A[i][j].getValue();
    }

    public String getStringValue(int i, int j) {
        return Astr[i][j];
    }

    public void setValue(int i, int j, int val) {
        A[i][j].setValue(val);
        Astr[i][j] = String.valueOf(A[i][j]);
    }

    public void dubValue(int i, int j) {
        A[i][j].dubValue();
        Astr[i][j] = String.valueOf(A[i][j]);
    }

    public void nullValue(int i, int j) {
        A[j][j].setValue(0);
        Astr[i][j] = String.valueOf(A[i][j].getValue());
    }

    public void setRandomNumbers(int n) throws IllegalRandomizeBitmap2048 {
        if (n > N * N)
            throw new IllegalRandomizeBitmap2048();
        int count = 0;
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                if ((A[i][j].getNextValue() == -1 && A[i][j].getValue() == 0) || (A[i][j].getNextValue() == 0))
                    ++count;

        if (n > count)
            n = count;

        for (int p = 0; p < n; ++p) {
            int i = r.nextInt(N), j = r.nextInt(N);
            while (true) {
                if (A[i][j].getNextValue() == -1 && A[i][j].getValue() == 0)
                    break;
                if (A[i][j].getNextValue() == 0)
                    break;
                i = r.nextInt(N);
                j = r.nextInt(N);
            }

            A[i][j].setCurI(i);
            A[i][j].setCurJ(j);
            A[i][j].setValue((int) Math.pow(2, 1 + r.nextInt(2)));
            Astr[i][j] = String.valueOf(A[i][j].getValue());
        }
    }

    public void setRandomNumbers() throws IllegalRandomizeBitmap2048 {
        setRandomNumbers(1/*+r.nextInt(2)*/);
    }

    public void stepForward() {
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].step();
    }

    float getCurI(int i, int j) {
        return A[i][j].getCurI();
    }

    float getCurJ(int i, int j) {
        return A[i][j].getCurJ();
    }

    public void toNextState() {
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].toNextState();
    }

    private void swap(int i1, int j1, int i2, int j2) {
        Position temp = A[i1][j1];
        A[i1][j1] = A[i2][j2];
        A[i2][j2] = temp;
    }

    private void swapStr(int i1, int j1, int i2, int j2) {
        String temp = Astr[i1][j1];
        Astr[i1][j1] = Astr[i2][j2];
        Astr[i2][j2] = temp;
    }

    public void goToPrevState() {
        if (!prevState.empty()) {
            Changes c = prevState.pop();

            if (c != null) {
                c.makeChanges(this);
            }
        }
    }

    public void moveLeft() {
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int i = 0; i < N; ++i) {
            for (int j = N - 1; j > 0; --j) {
                if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i][j - 1].getValue()) {
                    swap(i, j - 1, i, j);
                    swapStr(i, j - 1, i, j);
                    A[i][j - 1].dubValue();
                    Astr[i][j - 1] = String.valueOf(A[i][j - 1].getValue());
                    A[i][j].setValue(0);
                    Astr[i][j] = "";
                    for (int p = j; p < N - 1; ++p) {
                        swap(i, p, i, p + 1);
                        swapStr(i, p, i, p + 1);
                    }
                }

                if (A[i][j].getValue() != 0 && A[i][j - 1].getValue() == 0) {
                    for (int p = j - 1; p < N - 1; ++p) {
                        swap(i, p, i, p + 1);
                        swapStr(i, p, i, p + 1);
                    }
                }
            }
        }
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].calculate(i, j);

        Changes c = new Changes();
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j) {
                if (i != A[i][j].getCurI() || j != A[i][j].getCurJ() || A[i][j].getNextValue() != A[i][j].getPrevValue()) {
                    c.addNewChange((int) A[i][j].getCurI(), (int) A[i][j].getCurJ(), A[i][j].getPrevValue());
                }
            }

        prevState.add(c);
    }

    public void moveRight() {
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N - 1; ++j) {
                if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i][j + 1].getValue()) {
                    swap(i, j + 1, i, j);
                    swapStr(i, j + 1, i, j);
                    A[i][j + 1].dubValue();
                    Astr[i][j + 1] = String.valueOf(A[i][j + 1].getValue());
                    A[i][j].setValue(0);
                    Astr[i][j] = "";
                    for (int p = j; p > 0; --p) {
                        swap(i, p, i, p - 1);
                        swapStr(i, p, i, p - 1);
                    }
                }

                if (A[i][j].getValue() != 0 && A[i][j + 1].getValue() == 0) {
                    for (int p = j + 1; p > 0; --p) {
                        swap(i, p, i, p - 1);
                        swapStr(i, p, i, p - 1);
                    }
                }
            }
        }

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].calculate(i, j);
    }

    public void moveUp() {
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int j = 0; j < N; ++j) {
            for (int i = N - 1; i > 0; --i) {
                if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i - 1][j].getValue()) {
                    swap(i - 1, j, i, j);
                    swapStr(i - 1, j, i, j);
                    A[i - 1][j].dubValue();
                    Astr[i - 1][j] = String.valueOf(A[i - 1][j].getValue());
                    A[i][j].setValue(0);
                    Astr[i][j] = "";
                    for (int p = i; p < N - 1; ++p) {
                        swap(p, j, p + 1, j);
                        swapStr(p, j, p + 1, j);
                    }
                }

                if (A[i][j].getValue() != 0 && A[i - 1][j].getValue() == 0) {
                    for (int p = i - 1; p < N - 1; ++p) {
                        swap(p, j, p + 1, j);
                        swapStr(p, j, p + 1, j);
                    }
                }
            }
        }

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].calculate(i, j);
    }

    public void moveDown() {
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int j = 0; j < N; ++j) {
            for (int i = 0; i < N - 1; ++i) {
                if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i + 1][j].getValue()) {
                    swap(i + 1, j, i, j);
                    swapStr(i + 1, j, i, j);
                    A[i + 1][j].dubValue();
                    Astr[i + 1][j] = String.valueOf(A[i + 1][j].getValue());
                    A[i][j].setValue(0);
                    Astr[i][j] = "";
                    for (int p = i; p > 0; --p) {
                        swap(p, j, p - 1, j);
                        swapStr(p, j, p - 1, j);
                    }
                }

                if (A[i][j].getValue() != 0 && A[i + 1][j].getValue() == 0) {
                    for (int p = i + 1; p > 0; --p) {
                        swap(p, j, p - 1, j);
                        swapStr(p, j, p - 1, j);
                    }
                }
            }
        }

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].calculate(i, j);
    }
}

/*public void moveLeft(){
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int i = 0; i < N; ++i){
            for (int j = N-1; j > 0; --j){
                /*if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i][j-1].getValue()){
                    A[i][j-1].dubValue();
                    Astr[i][j-1] = String.valueOf(A[i][j-1].getValue());
                    A[i][j].setValue(0);
                    Astr[i][j] = new String();
                    for (int p = j; p < N-1; ++p){
                        A[i][p].setValue(A[i][p+1].getValue());
                        Astr[i][p] = String.valueOf(A[i][p].getValue());
                    }
                }

if (A[i][j].getValue() != 0 && A[i][j-1].getValue() == 0){
        for (int p = j-1; p < N-1; ++p){

        A[i][p].setValue(A[i][p+1].getValue());
        Astr[i][p] = String.valueOf(A[i][p].getValue());
        }
        A[i][N-1].setValue(0);
        Astr[i][N-1] = String.valueOf(A[i][N-1].getValue());
        }
        }
        }
        for (int i = 0; i < N; ++i)
        for (int j = 0; j < N; ++j)
        A[i][j].calculate();
        }
public void moveRight(){
       for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int i = 0; i < N; ++i){
        for (int j = 0; j < N-1; ++j){
        if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i][j+1].getValue()){
        A[i][j+1].dubValue();
        Astr[i][j+1] = String.valueOf(A[i][j+1].getValue());
        A[i][j].setValue(0);
        Astr[i][j] = new String();
        for (int p = j; p > 0; --p){
        A[i][p].setValue(A[i][p-1].getValue());
        Astr[i][p] = String.valueOf(A[i][p].getValue());
        }
        }

        if (A[i][j].getValue() != 0 && A[i][j+1].getValue() == 0){
        for (int p = j+1; p > 0; --p){
        A[i][p].setValue(A[i][p-1].getValue());
        Astr[i][p] = String.valueOf(A[i][p].getValue());
        }
        A[i][0].setValue(0);
        Astr[i][0] = String.valueOf(A[i][0].getValue());
        }
        }
        }

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].calculate();
        }
public void moveUp(){
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int j = 0; j < N; ++j){
        for (int i = N-1; i > 0; --i){
        if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i-1][j].getValue()){
        A[i-1][j].dubValue();
        Astr[i-1][j] = String.valueOf(A[i-1][j].getValue());
        A[i][j].setValue(0);
        Astr[i][j] = new String();
        for (int p = i; p < N-1; ++p){
        A[p][j].setValue(A[p+1][j].getValue());
        Astr[p][j] = String.valueOf(A[p][j].getValue());
        }
        }

        if (A[i][j].getValue() != 0 && A[i-1][j].getValue() == 0){
        for (int p = i-1; p < N-1; ++p){
        A[p][j].setValue(A[p+1][j].getValue());
        Astr[p][j] = String.valueOf(A[p][j].getValue());
        }
        A[N-1][j].setValue(0);
        Astr[N-1][j] = String.valueOf(A[N-1][j].getValue());
        }
        }
        }

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].calculate();
        }
public void moveDown(){
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].fixed();

        for (int j = 0; j < N; ++j){
        for (int i = 0; i < N-1; ++i){
        if (A[i][j].getValue() != 0 && A[i][j].getValue() == A[i+1][j].getValue()){
        A[i+1][j].dubValue();
        Astr[i+1][j] = String.valueOf(A[i+1][j].getValue());
        A[i][j].setValue(0);
        Astr[i][j] = new String();
        for (int p = i; p > 0; --p){
        A[p][j].setValue(A[p-1][j].getValue());
        Astr[p][j] = String.valueOf(A[p][j].getValue());
        }
        }

        if (A[i][j].getValue() != 0 && A[i+1][j].getValue() == 0){
        for (int p = i+1; p > 0; --p){
        A[p][j].setValue(A[p-1][j].getValue());
        Astr[p][j] = String.valueOf(A[p][j].getValue());
        }
        A[0][j].setValue(0);
        Astr[0][j] = String.valueOf(A[0][j].getValue());
        }
        }
        }

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                A[i][j].calculate();
        }
*/
