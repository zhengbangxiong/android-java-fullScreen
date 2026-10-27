package app.gkai.ai.core.common.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class BeginRainning extends View {
    private int       base      = 50;
    private int       cur       = 0;
    private RainArray arrayList = new RainArray();
    //    private int color = 0xFF988C90;
    private float     TextSize;
    private Random    random    = new Random();

    public BeginRainning(Context context) {
        super(context);
    }

    public BeginRainning(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BeginRainning(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BeginRainning(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = canvas.getWidth();
        float y = canvas.getHeight();
        float textSize = y / base + 4;
        this.TextSize = textSize;
        Paint paint = new Paint();
        //        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setLetterSpacing(1);
        int Count = random.nextInt(4) + 1;    //每次随机生成的雨滴数
        arrayList.NextLines((int) (y + TextSize));  //字符下移
        for (int i = 0; i < Count; i++) {           //添加雨滴
            arrayList.addDataMem(new DataMember(getChar(), 0xff,
                    random.nextInt((int) x), (int) y + 200));
        }

        for (int i = 0; i < arrayList.getSize(); i++) {    //渲染到屏幕
            DataMember dataMember = arrayList.getDataMem(i);
//            if (RainARGB) {
                paint.setARGB(dataMember.A, 0x98, 0x8C, 0x90);
//            }else{
//                paint.setARGB(0x00, 0x98, 0x8C, 0x90);
//            }
            canvas.drawText(String.valueOf(dataMember.c), dataMember.x, dataMember.y, paint);
        }

        if(cleanCanvas){
//            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//            canvas.drawPaint(paint);
        }

    }

    public boolean RainARGB = false;
    public void setRainARGB(int index) {
        if (index == 1) {
            RainARGB = true;
        } else {
            RainARGB = false;
        }
    }

    private Boolean cleanCanvas = false;
    public void setRemove(boolean cleanCanvas){
        this.cleanCanvas = cleanCanvas;
    }

    /**
     * c:要在屏幕上显示的字符
     * A:字符的透明度
     * x，y:字符在屏幕上显示的位置坐标
     */
    class DataMember {
        private char  c;
        private int   A;
        private float x;
        private float y;

        public DataMember(char c, int a, float x, float y) {
            this.c = c;
            A = a;
            this.x = x;
            this.y = y;
        }
    }

    class RainArray {
        private ArrayList<DataMember> arrayXY = new ArrayList<>();

        public int getSize() {
            return arrayXY.size();
        }

        public void addDataMem(DataMember dataMember) {

            int len = random.nextInt(6) + 1;  //一列雨滴的长度
            if (RainARGB){
                arrayXY.add(dataMember);
            }else{
                len = -1;
            }

            for (int i = 0; i < len; i++) {     //透明度和位置按线性变化加入数组
                    arrayXY.add(new DataMember(getChar(),
                            (0xff / (len * 2)) * (len - i),
                            dataMember.x, (dataMember.y - (i + 1) * TextSize)));

            }
        }

        public void remove(int i) {
            arrayXY.remove(i);
        }

        public DataMember getDataMem(int i) {
            return arrayXY.get(i);
        }

        public void NextLines(int Y) {        //让每个字符下移
            //            for(int i = 0; i < arrayXY.size(); i++) {
            //                arrayXY.get(i).y += TextSize;
            //                if(arrayXY.get(i).y >= Y) {arrayXY.remove(i); i--;}
            //            }

            for (int i = 0; i < arrayXY.size(); i++) {
                arrayXY.get(i).y -= TextSize;
                if (arrayXY.get(i).y <= 2) {
                    arrayXY.remove(i);
                    i--;
                }
            }
        }
    }

    public char getChar() {
        int r = random.nextInt(10);
        return (char) (r + '0');
    }
}

