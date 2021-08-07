package per.goweii.layer.keyboard;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import per.goweii.layer.core.anim.AnimStyle;
import per.goweii.layer.core.utils.Utils;
import per.goweii.layer.dialog.DialogLayer;

public class KeyboardLayer extends DialogLayer {
    private KeyboardSoundEffect mSoundEffect = null;
    private KeyboardVibratorEffect mVibratorEffect = null;

    public KeyboardLayer(@NonNull Context context) {
        super(context);
        init();
    }

    public KeyboardLayer(@NonNull Activity activity) {
        super(activity);
        init();
    }

    private void init() {
        setContentView(R.layout.layer_keyboard);
        setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setContentAnimator(AnimStyle.BOTTOM);
        setCancelableOnClickKeyBack(true);
        setCancelableOnTouchOutside(true);
        setOutsideInterceptTouchEvent(false);
        setOutsideTouchToDismiss(true);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder() {
        return (ViewHolder) super.getViewHolder();
    }

    @NonNull
    @Override
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @NonNull
    @Override
    public ListenerHolder getListenerHolder() {
        return (ListenerHolder) super.getListenerHolder();
    }

    @NonNull
    @Override
    protected Config onCreateConfig() {
        return new Config();
    }

    @NonNull
    @Override
    public Config getConfig() {
        return (Config) super.getConfig();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if (getConfig().mSoundEffectEnabled) {
            mSoundEffect = new KeyboardSoundEffect(getActivity());
        }
        if (getConfig().mVibratorEffectEnabled) {
            mVibratorEffect = new KeyboardVibratorEffect(getActivity());
        }
        getListenerHolder().bindListeners(this);
        refreshInputType();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        if (mSoundEffect != null) {
            mSoundEffect.release();
        }
        if (mVibratorEffect != null) {
            mVibratorEffect.release();
        }
    }

    @Override
    protected void fitDecorInsides() {
        fitDecorInsidesToViewPadding(getViewHolder().getContent());
        Utils.setViewPaddingTop(getViewHolder().getContent(), 0);
    }

    private void bindTextView() {
        TextView textView = getViewHolder().getAttachedTextView();
        if (textView == null) return;
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !isShown()) {
                    show();
                } else if (!hasFocus && isShown()) {
                    dismiss();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShown()) {
                    show();
                }
            }
        });
    }

    private void unbindTextView() {
        TextView textView = getViewHolder().getAttachedTextView();
        if (textView == null) return;
        textView.setOnFocusChangeListener(null);
        textView.setOnClickListener(null);
    }

    @NonNull
    public KeyboardLayer attachTextView(@NonNull TextView textView) {
        getViewHolder().attachTextView(textView);
        bindTextView();
        return this;
    }

    @NonNull
    public KeyboardLayer detachTextView() {
        unbindTextView();
        getViewHolder().detachTextView();
        return this;
    }

    @NonNull
    public KeyboardLayer addOnActionListener(@NonNull OnActionListener listener) {
        getListenerHolder().addOnActionListener(listener);
        return this;
    }

    @NonNull
    public KeyboardLayer removeOnActionListener(@NonNull OnActionListener listener) {
        getListenerHolder().removeOnActionListener(listener);
        return this;
    }

    @NonNull
    public KeyboardLayer setBindingActionListener(@NonNull BindingActionListener listener) {
        getListenerHolder().setBindingActionListener(listener);
        return this;
    }

    @NonNull
    public KeyboardLayer setSoundEffectEnabled(boolean soundEffect) {
        getConfig().mSoundEffectEnabled = soundEffect;
        return this;
    }

    public boolean isSoundEffectEnabled() {
        return getConfig().mSoundEffectEnabled;
    }

    @NonNull
    public KeyboardLayer setVibratorEffectEnabled(boolean vibratorEffect) {
        getConfig().mVibratorEffectEnabled = vibratorEffect;
        return this;
    }

    public boolean isVibratorEffectEnabled() {
        return getConfig().mVibratorEffectEnabled;
    }

    @NonNull
    public InputType getInputType() {
        return getConfig().mInputType;
    }

    public KeyboardLayer setInputType(@NonNull InputType inputType) {
        if (getConfig().mInputType != inputType) {
            getConfig().mInputType = inputType;
            refreshInputType();
        }
        return this;
    }

    public void refreshInputType() {
        switch (getConfig().mInputType) {
            case LATTER:
                if (getViewHolder().getPadLatter() != null) {
                    getViewHolder().getPadLatter().setVisibility(View.VISIBLE);
                }
                if (getViewHolder().getPadNumber() != null) {
                    getViewHolder().getPadNumber().setVisibility(View.GONE);
                }
                if (getViewHolder().getPadSymbol() != null) {
                    getViewHolder().getPadSymbol().setVisibility(View.GONE);
                }
                break;
            case NUMBER:
                if (getViewHolder().getPadLatter() != null) {
                    getViewHolder().getPadLatter().setVisibility(View.GONE);
                }
                if (getViewHolder().getPadNumber() != null) {
                    getViewHolder().getPadNumber().setVisibility(View.VISIBLE);
                }
                if (getViewHolder().getPadSymbol() != null) {
                    getViewHolder().getPadSymbol().setVisibility(View.GONE);
                }
                break;
            case SYMBOL:
                if (getViewHolder().getPadLatter() != null) {
                    getViewHolder().getPadLatter().setVisibility(View.GONE);
                }
                if (getViewHolder().getPadNumber() != null) {
                    getViewHolder().getPadNumber().setVisibility(View.GONE);
                }
                if (getViewHolder().getPadSymbol() != null) {
                    getViewHolder().getPadSymbol().setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public boolean isCaps() {
        return getViewHolder().isCaps();
    }

    public void setCaps(boolean isCaps) {
        getViewHolder().setCaps(isCaps);
    }

    public void toggleCaps() {
        setCaps(!isCaps());
    }

    public void performFeedback() {
        performSoundEffect();
        performVibratorEffect();
    }

    public void performSoundEffect() {
        if (getConfig().mSoundEffectEnabled && mSoundEffect != null) {
            mSoundEffect.performClick();
        }
    }

    public void performVibratorEffect() {
        if (getConfig().mVibratorEffectEnabled && mVibratorEffect != null) {
            mVibratorEffect.performClick();
        }
    }

    public static class Config extends DialogLayer.Config {
        private InputType mInputType = InputType.LATTER;
        private boolean mSoundEffectEnabled = true;
        private boolean mVibratorEffectEnabled = true;
    }

    public static class ViewHolder extends DialogLayer.ViewHolder {
        private WeakReference<TextView> mTextViewRef = null;
        private View mPadLatter = null;
        private View mPadNumber = null;
        private View mPadSymbol = null;
        private View mCapsView = null;

        @Override
        protected void setContent(@NonNull View content) {
            super.setContent(content);
            mPadLatter = content.findViewById(R.id.layer_keyboard_pad_latter);
            mPadNumber = content.findViewById(R.id.layer_keyboard_pad_number);
            mPadSymbol = content.findViewById(R.id.layer_keyboard_pad_symbol);
            if (mPadLatter != null) {
                mCapsView = mPadLatter.findViewById(R.id.layer_keyboard_keycode_caps);
            }
        }

        @Nullable
        public View getPadLatter() {
            return mPadLatter;
        }

        @Nullable
        public View getPadNumber() {
            return mPadNumber;
        }

        @Nullable
        public View getPadSymbol() {
            return mPadSymbol;
        }

        public boolean isCaps() {
            if (mCapsView == null) return false;
            return mCapsView.isSelected();
        }

        public void setCaps(boolean isCaps) {
            if (mCapsView != null) {
                mCapsView.setSelected(isCaps);
            }
            for (int keyCode : KeyCodes.KEYCODE_LETTERS) {
                List<TextView> textViews = findViewsById(keyCode);
                for (TextView textView : textViews) {
                    textView.setAllCaps(isCaps);
                }
            }
        }

        @Nullable
        public TextView getAttachedTextView() {
            if (mTextViewRef != null) {
                return mTextViewRef.get();
            }
            return null;
        }

        public void attachTextView(@NonNull TextView textView) {
            detachTextView();
            mTextViewRef = new WeakReference<>(textView);
        }

        public void detachTextView() {
            if (mTextViewRef != null) {
                mTextViewRef.clear();
                mTextViewRef = null;
            }
        }

        @NonNull
        private <V extends View> List<V> findViewsById(@IdRes int id) {
            final List<V> views = new LinkedList<>();
            if (getPadLatter() != null) {
                V view = getPadLatter().findViewById(id);
                if (view != null && !views.contains(view)) {
                    views.add(view);
                }
            }
            if (getPadNumber() != null) {
                V view = getPadNumber().findViewById(id);
                if (view != null && !views.contains(view)) {
                    views.add(view);
                }
            }
            if (getPadSymbol() != null) {
                V view = getPadSymbol().findViewById(id);
                if (view != null && !views.contains(view)) {
                    views.add(view);
                }
            }
            V view = findViewInChild(id);
            if (view != null && !views.contains(view)) {
                views.add(view);
            }
            return views;
        }
    }

    public static class ListenerHolder extends DialogLayer.ListenerHolder {
        private BindingActionListener mBindingActionListener = new DefaultBindingActionListener();
        private List<OnActionListener> mOnActionListeners = null;

        public void setBindingActionListener(@NonNull BindingActionListener bindingActionListener) {
            mBindingActionListener = bindingActionListener;
        }

        public void addOnActionListener(@NonNull OnActionListener listener) {
            if (mOnActionListeners == null) {
                mOnActionListeners = new ArrayList<>(1);
            }
            mOnActionListeners.add(listener);
        }

        public void removeOnActionListener(@NonNull OnActionListener listener) {
            if (mOnActionListeners != null) {
                mOnActionListeners.remove(listener);
            }
        }

        public void bindListeners(@NonNull final KeyboardLayer layer) {
            for (int keyCode : KeyCodes.KEYCODE_LETTERS) {
                bindGestureListener(layer, keyCode, false);
            }
            for (int keyCode : KeyCodes.KEYCODE_NUMBERS) {
                bindGestureListener(layer, keyCode, false);
            }
            for (int keyCode : KeyCodes.KEYCODE_SPACES) {
                bindGestureListener(layer, keyCode, false);
            }
            for (int keyCode : KeyCodes.KEYCODE_SYMBOLS) {
                bindGestureListener(layer, keyCode, false);
            }
            bindGestureListener(layer, KeyCodes.KEYCODE_DEL, true);
            bindClickListener(layer, KeyCodes.KEYCODE_ENTER, new Runnable() {
                @Override
                public void run() {
                    notifyAllActionListeners(layer, KeyCodes.KEYCODE_ENTER);
                }
            });
            bindLongClickListener(layer, KeyCodes.KEYCODE_ENTER, new Runnable() {
                @Override
                public void run() {
                    notifyAllActionListeners(layer, KeyCodes.KEYCODE_LINE_FEED);
                }
            });
            bindClickListener(layer, KeyCodes.KEYCODE_CAPS, new Runnable() {
                @Override
                public void run() {
                    layer.setCaps(!layer.isCaps());
                }
            });
            bindClickListener(layer, KeyCodes.KEYCODE_HIDE, new Runnable() {
                @Override
                public void run() {
                    layer.dismiss();
                }
            });
            bindClickListener(layer, KeyCodes.KEYCODE_INPUT_TYPE_LETTER, new Runnable() {
                @Override
                public void run() {
                    layer.setInputType(InputType.LATTER);
                }
            });
            bindClickListener(layer, KeyCodes.KEYCODE_INPUT_TYPE_NUMBER, new Runnable() {
                @Override
                public void run() {
                    layer.setInputType(InputType.NUMBER);
                }
            });
            bindClickListener(layer, KeyCodes.KEYCODE_INPUT_TYPE_SYMBOL, new Runnable() {
                @Override
                public void run() {
                    layer.setInputType(InputType.SYMBOL);
                }
            });
        }

        private void bindGestureListener(
                @NonNull final KeyboardLayer layer,
                @IdRes final int keyCode,
                boolean repeatable
        ) {
            final List<View> views = layer.getViewHolder().findViewsById(keyCode);
            for (View view : views) {
                view.setSoundEffectsEnabled(false);
                view.setHapticFeedbackEnabled(false);
                new KeyboardGesture(layer, view, repeatable, new Runnable() {
                    @Override
                    public void run() {
                        notifyAllActionListeners(layer, keyCode);
                    }
                });
            }
        }

        private void bindClickListener(
                @NonNull final KeyboardLayer layer,
                @IdRes int id,
                @NonNull final Runnable action
        ) {
            final List<View> views = layer.getViewHolder().findViewsById(id);
            for (View view : views) {
                view.setSoundEffectsEnabled(false);
                view.setHapticFeedbackEnabled(false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        action.run();
                        layer.performFeedback();
                    }
                });
            }
        }

        private void bindLongClickListener(
                @NonNull final KeyboardLayer layer,
                @IdRes int id,
                @NonNull final Runnable action
        ) {
            final List<View> views = layer.getViewHolder().findViewsById(id);
            for (View view : views) {
                view.setSoundEffectsEnabled(false);
                view.setHapticFeedbackEnabled(false);
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        action.run();
                        layer.performFeedback();
                        return true;
                    }
                });
            }
        }

        private void notifyAllActionListeners(
                @NonNull final KeyboardLayer layer,
                @IdRes final int keyCode
        ) {
            boolean isCaps = layer.isCaps();
            String keyText = KeyboardUtils.getKeyText(layer.getActivity(), keyCode, isCaps);
            KeyAction action = KeyAction.obtain(keyCode, keyText);
            if (!notifyCustomActionListeners(layer, action)) {
                notifyBindingActionListener(layer, action);
            }
        }

        private boolean notifyCustomActionListeners(
                @NonNull final KeyboardLayer layer,
                @NonNull final KeyAction action
        ) {
            if (mOnActionListeners != null) {
                for (OnActionListener listener : mOnActionListeners) {
                    if (listener.onAction(layer, action)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean notifyBindingActionListener(
                @NonNull final KeyboardLayer layer,
                @NonNull final KeyAction action
        ) {
            return mBindingActionListener.onAction(layer, action);
        }
    }

    public interface OnActionListener {
        boolean onAction(@NonNull KeyboardLayer layer, @NonNull KeyAction action);
    }

    public enum InputType {
        LATTER, NUMBER, SYMBOL
    }
}
