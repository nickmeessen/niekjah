package nl.nickmeessen.niekjah;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class BubbleController extends Thread implements SurfaceHolder.Callback {

    private BubbleView bubbleView;
    private OrbManager orbManager;

    public static final int STATE_LOSE = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_RUNNING = 3;
    public static final int STATE_WIN = 4;

    private int currentMode;
    private boolean running = false;

    private SurfaceHolder surfaceHolder;
    private Handler bubbleHandler;

    public BubbleController(BubbleView view) {

        view.setController(this);

        bubbleView = view;
        bubbleHandler = new Handler();

        surfaceHolder = view.getHolder();

        surfaceHolder.addCallback(this);

        orbManager = new OrbManager(this);


    }

    public void setView(BubbleView view) {
        bubbleView = view;
    }

    public BubbleView getView() {
        return bubbleView;
    }

    public void click(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            orbManager.clickOrb((int) ev.getX(), (int) ev.getY());
        }
    }


    private void updateGame(Canvas canvas) {

        orbManager.checkRemoving();

        if (!orbManager.checkGravity()) {

            if (!orbManager.checkGravityRight()) {

                if (!orbManager.checkEmptyColumns()) {

                    if (orbManager.checkGameOver()) {

                        Toast.makeText(bubbleView.getContext(), "GAME OVER!", Toast.LENGTH_LONG).show();


                    }
                }
            }

        }

        canvas.drawARGB(255, 0, 0, 0);

        for (Orb orb : orbManager.getOrbs()) {

            canvas.drawBitmap(orb.getImage(), orb.getX(), orb.getY(), new Paint());

            if (orb.getSelected()) {

                Bitmap sel = BitmapFactory.decodeResource(bubbleView.getContext().getResources(), R.drawable.highlight);

                canvas.drawBitmap(sel, orb.getX(), orb.getY(), new Paint());
            }


        }

    }


    public void newGame() {

//        synchronized (surfaceHolder) {
            orbManager.reset();
//        }
    }

    public void pauseGame() {

//        synchronized (surfaceHolder) {

            if (currentMode == STATE_RUNNING) {
                setState(STATE_PAUSE);
            }

//        }
    }

    public void resumeGame() {

        if (currentMode == STATE_PAUSE) {
            setState(STATE_RUNNING);
        }
    }

    @Override
    public void run() {

        while (running) {

            Canvas c = null;

            try {

                c = surfaceHolder.lockCanvas(null);

//                synchronized (surfaceHolder) {

                    if (currentMode == STATE_RUNNING) {
                        updateGame(c);
                    }


//                }

            } finally {

                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setState(int mode) {
//        synchronized (surfaceHolder) {
            currentMode = mode;
//        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // @todo needed?
    }

    public void surfaceCreated(SurfaceHolder holder) {
        running = true;
        start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;

        running = false;

        while (retry) {
            try {
                this.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void undoMove() {
//        @todo orbManager.undo();
    }

}
