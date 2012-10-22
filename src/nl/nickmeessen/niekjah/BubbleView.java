package nl.nickmeessen.niekjah;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;

class BubbleView extends SurfaceView {

    private BubbleController bubbleController;
    private Context context;

    public BubbleView(Context context, AttributeSet attrs) {

        super(context, attrs);

        setFocusable(true);

    }

    public void setController(BubbleController ctrl) {
        bubbleController = ctrl;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        bubbleController.click(ev);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return false;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {

        if (!hasWindowFocus) {
            bubbleController.pauseGame();
        } else {
            bubbleController.resumeGame();
        }

    }

    // @todo change 25 -> 50

    public int getMaxRows() {
        return (getHeight() / 25);
    }

    public int getMaxColumns() {
        return (getWidth() / 25) - 1;
    }


}
