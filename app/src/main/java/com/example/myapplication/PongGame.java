package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongGame extends SurfaceView implements Runnable {

    private int mScreenX;
    private int mScreenY;

    private int mFontSize;
    private int mFontMargin;

    private int mScore;
    private int mLives;

    private Bat mBat;
    private Ball mBall;

    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    private final boolean DEBUGGING = true;
    private long mFPS;
    private final int MILLIS_IN_SECOND = 1000;

    private Thread mGameThread = null;
    private volatile boolean mPlaying;
    private boolean mPaused = true;


    public PongGame(Context context, int x, int y) {

        super(context);

        mScreenX = x;
        mScreenY = y;
        mFontSize = mScreenX / 20;
        mFontMargin = mScreenX / 75;
        mOurHolder = getHolder();
        mPaint = new Paint();
        mBall = new Ball(mScreenX);
        mBall.update(mFPS);


        startNewGame();
        mBall.reset(mScreenX, mScreenY);
    }

    private void startNewGame() {

        mScore = 0;
        mLives = 3;
    }

    private void draw() {

        if (mOurHolder.getSurface().isValid()) {
            mCanvas = mOurHolder.lockCanvas(); // Lock the canvas (graphics memory)
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(mFontSize);
            mCanvas.drawText("Score: " + mScore + " Lives: " + mLives,mFontMargin, mFontSize, mPaint);
            mCanvas.drawRect(mBall.getRect(), mPaint);
            if (DEBUGGING) {
                printDebuggingText();
            }
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void printDebuggingText() {
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS, 10, debugStart + debugSize, mPaint);
    }

    public void pause() {

        mPlaying = false;
        try {

            mGameThread.join();

        } catch (InterruptedException e) {

            Log.e("Error:", "joining thread");

        }
    }

    public void resume() {
        mPlaying = true;
// Initialize the instance of Thread
        mGameThread = new Thread(this);
// Start the thread
        mGameThread.start();
    }


    @Override
    public void run() {
        while (mPlaying) {
// What time is it now at the start of the loop?
            long frameStartTime = System.currentTimeMillis();
// Provided the game isn't paused call the update method
            if (!mPaused) {
                update(); // update new positions
                detectCollisions(); // detect collisions
            }
//draw the scene
            draw();
// How long did this frame/loop take?
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
// check timeThisFrame > 0 ms because dividing by 0 will crashes game
            if (timeThisFrame > 0) {
// Store frame rate to pass to the update methods of mBat and mBall
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }
    private void update() {
    }
    private void detectCollisions() {
    }
}
