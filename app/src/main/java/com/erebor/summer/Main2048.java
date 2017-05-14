package com.erebor.summer;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.erebor.summer.exception.IllegalMovementException;

public class Main2048 extends Activity {
    private Field2048 f;
    private final TextView t;
    private Spinner s;
    private Button b;

    private final String[] data = {"2", "3", "4", "5", "6", "7", "8", "9"};

    private int width;
    private int height;

    public Main2048(TextView t) {
        this.t = t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2048);

        f = (Field2048)findViewById(R.id.field2048);
        s = (Spinner)findViewById(R.id.nValue);
        b = (Button)findViewById(R.id.undo);

        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.goToPrevState();
            }
        };

        b.setOnClickListener(o);

        // Узнаем размеры экрана из ресурсов
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();

        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s.setAdapter(adapter);
        // заголовок
        s.setPrompt("Title");
        // выделяем элемент
        s.setSelection(1);
        // устанавливаем обработчик нажатия
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {@Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // показываем позиция нажатого элемента
            Toast.makeText(getBaseContext(), "Вибран размер = " + (position+2), Toast.LENGTH_SHORT).show();

            f.setN(position+2);
            f.invalidate();
        }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(),
                y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                f.setCurX(x);
                f.setCurY(y);
                break;
            case MotionEvent.ACTION_MOVE:
                try {
                    if (Math.abs(f.getCurX()-x) > width/5.0) {
                        if (f.getCurX() > x){
                            f.addMoves(Movement.MOVE_LEFT);
                        }else{
                            f.addMoves(Movement.MOVE_RIGHT);
                        }
                        f.invalidate();
                        f.setCurX(x);
                    }

                    if (Math.abs(f.getCurY()-y) > height/5.0) {
                        if (f.getCurY() > y) {
                            f.addMoves(Movement.MOVE_UP);
                        }else {
                            f.addMoves(Movement.MOVE_DOWN);
                        }
                        f.invalidate();
                        f.setCurY(y);
                    }
                } catch(IllegalMovementException ex)
                {
                    System.out.println(ex.getMessage());
                }
                break;
            case MotionEvent.ACTION_UP:
                f.setCurX(-1.0f);
                f.setCurY(-1.0f);
        }
        return super.onTouchEvent(event);
    }
}