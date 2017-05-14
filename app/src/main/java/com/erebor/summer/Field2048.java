package com.erebor.summer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.erebor.summer.exception.IllegalMovementException;
import com.erebor.summer.exception.IllegalRandomizeBitmap2048;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Summer
 * Created on 15.05.17.
 */
class Field2048 extends View {
    private int N;
    private int width, height, sizeW, sizeH;
    private final Paint p = new Paint();
    private Paint fontPaint;
    private float curX, curY;
    private final Queue<Movement> moves = new LinkedList<>();
    private final Bitmap2048 bitmap2048 = new Bitmap2048();

    public float getCurX() {
        return curX;
    }

    public void setCurX(float curX) {
        this.curX = curX;
    }

    public void setCurY(float curY) {
        this.curY = curY;
    }

    public float getCurY() {
        return curY;
    }

    public void addMoves(int type) throws IllegalMovementException {
        moves.add(new Movement(type,true));
        int p1 = 20;
        for (int i = 0; i < p1; ++i)
            moves.add(new Movement(type,false));
    }

    public void goToPrevState(){
        bitmap2048.goToPrevState();
        invalidate();
    }

    private void commonConstructor(){
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //fontPaint.setARGB(0,0,0,0);
        fontPaint.setStyle(Paint.Style.STROKE);

        curX = -1.0f;
        curY = -1.0f;

        N = 6;
        bitmap2048.allocA(N);
        try {
            bitmap2048.setRandomNumbers(4);
        } catch (IllegalRandomizeBitmap2048 ex){

        }
    }

    public void setN(int n) {
        N = n;

        bitmap2048.allocA(N);
        try {
            bitmap2048.setRandomNumbers(4);
        } catch (IllegalRandomizeBitmap2048 ex){

        }

        sizeW = width / N;
        sizeH = height / N;
    }

    public Field2048(Context context) {
        super(context);

        commonConstructor();
    }
    public Field2048(Context context, AttributeSet attrs) {
        super(context, attrs);

        commonConstructor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        N = 3;
        sizeW = width / N;
        sizeH = height / N;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.restore();
        boolean flag = true;

        Movement cur = moves.poll();
        if (cur != null) {
            if (!cur.isMain()) {
                bitmap2048.stepForward();
            } else {
                bitmap2048.toNextState();
                switch(cur.getType()){
                    case Movement.MOVE_DOWN :
                        moveDown();
                        break;
                    case Movement.MOVE_LEFT:
                        moveLeft();
                        break;
                    case Movement.MOVE_RIGHT:
                        moveRight();
                        break;
                    case Movement.MOVE_UP:
                        moveUp();
                        break;
                }
            }
        } else {
            flag = false;
            bitmap2048.toNextState();
        }

        super.onDraw(canvas);

        p.setARGB(255, 0, 0, 0);
        for (int i = 0; i < N; ++i) {
            canvas.drawLine(0, i * sizeH, width, i * sizeH, p);
            canvas.drawLine(i * sizeW, 0, i * sizeW, height, p);
        }

        p.setARGB(125, 10, 190, 10);
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j) {
                canvas.drawRect(new Rect(i * sizeW, j * sizeH, (i + 1) * sizeW, (j + 1) * sizeH), p);
            }

        p.setARGB(125, 180, -180, 10);
        float curI = 1;
        float curJ = 1;
        canvas.drawRect(new RectF(curI * sizeW, curJ * sizeH, (curI + 1) * sizeW, (curJ + 1) * sizeH), p);

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j) {
                if (bitmap2048.getValue(i,j) != 0){
                    int fontSize = 100;
                    fontPaint.setTextSize(fontSize - bitmap2048.getStringValue(i,j).length()*10 -8*(N-3));
                    canvas.drawText(bitmap2048.getStringValue(i,j), (bitmap2048.getCurJ(i,j)+0.5f)*sizeW, (bitmap2048.getCurI(i,j)+0.5f)*sizeH, fontPaint);
                }
            }

        if (flag)
            invalidate();
    }

    private void moveLeft(){
        bitmap2048.moveLeft();
        try {
            bitmap2048.setRandomNumbers();
        } catch (IllegalRandomizeBitmap2048 ex){
            System.out.println(ex.getMessage());
        }
    }
    private void moveRight(){
        bitmap2048.moveRight();
        try {
            bitmap2048.setRandomNumbers();
        } catch (IllegalRandomizeBitmap2048 ex){
            System.out.println(ex.getMessage());

        }
    }
    private void moveUp(){
        bitmap2048.moveUp();
        try {
            bitmap2048.setRandomNumbers();
        } catch (IllegalRandomizeBitmap2048 ex){
            System.out.println(ex.getMessage());
        }
    }
    private void moveDown(){
        bitmap2048.moveDown();
        try {
            bitmap2048.setRandomNumbers();
        } catch (IllegalRandomizeBitmap2048 ex){
            System.out.println(ex.getMessage());

        }
    }
}
