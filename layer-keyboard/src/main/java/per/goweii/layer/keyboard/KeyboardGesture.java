package per.goweii.layer.keyboard;

import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class KeyboardGesture {
    private final KeyboardLayer mLayer;
    private final View mView;
    private final boolean mRepeatable;
    private final Runnable mOnKeyAction;
    private final KeyboardGestureListener mGestureListener;

    public KeyboardGesture(
            @NonNull KeyboardLayer layer,
            @NonNull View view,
            boolean repeatable,
            @NonNull Runnable action
    ) {
        this.mLayer = layer;
        this.mView = view;
        this.mRepeatable = repeatable;
        this.mOnKeyAction = action;
        this.mGestureListener = new KeyboardGestureListener();
        final GestureDetector gestureDetector = new GestureDetector(view.getContext(), mGestureListener);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mGestureListener.onUp(event);
                    case MotionEvent.ACTION_CANCEL:
                        mGestureListener.onCancel(event);
                        break;
                }
                return true;
            }
        });
    }

    private class KeyboardGestureListener implements GestureDetector.OnGestureListener {
        private final Runnable mRepeatRunnable = new Runnable() {
            @Override
            public void run() {
                if (mRepeatable && mRepeating) {
                    mOnKeyAction.run();
                    mLayer.performFeedback();
                    mView.postDelayed(mRepeatRunnable, 50);
                }
            }
        };

        private boolean mRepeating = false;

        @Override
        public boolean onDown(MotionEvent e) {
            mRepeating = false;
            mView.removeCallbacks(mRepeatRunnable);
            mView.setPressed(true);
            return true;
        }

        public void onUp(MotionEvent e) {
            mRepeating = false;
            mView.removeCallbacks(mRepeatRunnable);
            mView.setPressed(false);
        }

        public void onCancel(MotionEvent e) {
            mRepeating = false;
            mView.removeCallbacks(mRepeatRunnable);
            mView.setPressed(false);
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mOnKeyAction.run();
            mLayer.performFeedback();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mRepeating = false;
            mView.removeCallbacks(mRepeatRunnable);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mRepeatable) {
                mRepeating = true;
                mView.post(mRepeatRunnable);
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}