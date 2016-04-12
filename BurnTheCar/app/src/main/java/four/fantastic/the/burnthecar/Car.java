package four.fantastic.the.burnthecar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Amir on 4/11/2016.
 */
public class Car {
    private Context context;
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed;
    /*Send car in screen bound*/
    private int maxX;
    private int minX;
    /*Detect Car leaving the screen*/
    private int maxY;
    private int minY;
    private Rect hitBox;
    private int miss;
    private int burn;

    public Car(Context context,int screenX,int screenY) {
        this.context = context;
        maxX=screenX;
        maxY=screenY;
        minX=0;
        minY=0;
        miss=0;
        burn =0;
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.car);
        makeCordinate();
        hitBox=new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());

    }

    /*make new variable for new car*/
    private void makeCordinate() {
        x=createRandomNumber(maxX - bitmap.getWidth(),0);
        y=maxY;
        speed=createRandomNumber(10, 10);
    }

    private int createRandomNumber(int number, int constant) {
        Random random=new Random();
        return random.nextInt(number)+constant;
    }

    public int getBurn() {
        return burn;
    }

    public int getMiss() {
        return miss;
    }

    public void update(){

        /*if car disapire with or without hiting reswain*/
        if(y<minY-bitmap.getHeight()){

            /*if hit a car increase burn*/
            if(y==(-1000)){
                burn++;

            }else {
                /*if car pass screen without hitting increase miss*/
                miss++;
            }
            makeCordinate();
        }

        y-=speed;

        hitBox.left=x;
        hitBox.top=y;
        hitBox.right=x+bitmap.getWidth();
        hitBox.bottom=y+bitmap.getHeight();

    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
