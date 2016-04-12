package four.fantastic.the.burnthecar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Amir on 4/11/2016.
 */
public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;

    Thread gameThread = null;

    private Car car1;
    private Car car2;
    private Car car3;

    private Paint paint;

    private SurfaceHolder surfaceHolder;

    private Canvas canvas;

    private Context context;

    private int screenX;
    private int screenY;
    private int carBurn;
    private int carMiss;

    private boolean gameEnded;
    /*to check is user touced the screen*/
    private boolean action;

    private Rect userHitBox;



    public GameView(Context context, int screenX, int screenY) {
        super(context);
        playing=true;
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        surfaceHolder = getHolder();
        paint = new Paint();
        startGame();
    }

    private void startGame() {
        gameEnded=false;
        car1 = new Car(context, screenX, screenY);
        car2 = new Car(context, screenX, screenY);
        car3 = new Car(context, screenX, screenY);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            myDraw();
            control();

        }
    }

    private void myDraw() {
        if (surfaceHolder.getSurface().isValid()) {
            /*lock canvas*/
            canvas = surfaceHolder.lockCanvas();
            /*clear canvas to draw*/
            canvas.drawColor(Color.argb(255, 0, 0, 0));
//            /*draw hit box for car1*/
//            drawCarHitBox(car1.getHitBox());
//            /*draw hit box for car2*/
//            drawCarHitBox(car2.getHitBox());
//            /*draw hit box for car3*/
//            drawCarHitBox(car3.getHitBox());
            /*draw car1*/
            drawCar(canvas, car1);
            /*draw car2*/
            drawCar(canvas, car2);
            /*draw car3*/
            drawCar(canvas, car3);


            if(!gameEnded){
                /*Hud Display*/
                drawHud();
            }else {
                /*Ending Hud*/
                drawEndingHud();
            }
//            if (action){
//                drawUserHitBox(userHitBox);
//            }

            /*unlock canvas*/
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawHud() {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.argb(255,255,255,255));
        paint.setTextSize(25);
        /*draw text upper left corner*/
        canvas.drawText("Car Burn:"+carBurn,30,30,paint);
        canvas.drawText("Car Missed:"+carMiss,screenX/2,30,paint);
    }

    private void drawEndingHud() {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.argb(255,255,255,255));
        paint.setTextSize(80);
        /*draw Game Over in center*/
        canvas.drawText("Game Over:",screenX/2,100,paint);
        paint.setTextSize(25);
        canvas.drawText("Car Burn:"+carBurn,screenX/2,160,paint);
        paint.setTextSize(80);
        canvas.drawText("Tap to replay",screenX/2,350,paint);

    }

    private void drawCar(Canvas canvas, Car car) {
        canvas.drawBitmap(car.getBitmap(), car.getX(), car.getY(), paint);
    }


    private void update() {
        /*update car y cordinate*/
        car1.update();
        car2.update();
        car3.update();
        if(!gameEnded){
            carMiss=car1.getMiss()+car2.getMiss()+car3.getMiss();
            carBurn=car1.getBurn()+car2.getBurn()+car3.getBurn();

        }

        if(carMiss>5){
            gameEnded=true;
        }


    }

    private void control() {
        try {
            gameThread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                userHitBox = createHitBox(event.getX() - 50, event.getY() - 50, event.getX() + 50, event.getY() + 50);
                action=true;
                checkDetection(userHitBox);
                if(gameEnded){
                    startGame();
                }
                break;
            case MotionEvent.ACTION_UP:
                action=false;
                break;
        }
        return true;
    }

    private void checkDetection(Rect myHitBox) {
        if(Rect.intersects(myHitBox,car1.getHitBox())){
            car1.setY(-1000);
        }
        if(Rect.intersects(myHitBox,car2.getHitBox())){
            car2.setY(-1000);
        }
        if(Rect.intersects(myHitBox,car3.getHitBox())){
            car3.setY(-1000);
        }

    }

    private void drawUserHitBox(Rect hitbox) {

        paint.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawRect(hitbox.left, hitbox.top, hitbox.right, hitbox.bottom, paint);

    }

    private void drawCarHitBox(Rect hitbox) {

        paint.setColor(Color.argb(255, 0, 255, 0));
        canvas.drawRect(hitbox.left, hitbox.top, hitbox.right, hitbox.bottom, paint);


    }

    private Rect createHitBox(float x, float y, float width, float hight) {
        int left = (int) x;
        int top = (int) y;
        int right = (int) width;
        int bottom = (int) hight;
        Rect hitBox = new Rect(left, top, right, bottom);
        return hitBox;
    }
}
