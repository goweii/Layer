package per.goweii.layer.design.cupertino;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import per.goweii.layer.toast.ToastLayer;

public class CupertinoToastLayer extends ToastLayer {
    public CupertinoToastLayer(@NonNull Context context) {
        super(context);
    }

    public CupertinoToastLayer(@NonNull Activity activity) {
        super(activity);
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
    protected Config onCreateConfig() {
        return new Config();
    }

    @NonNull
    @Override
    public Config getConfig() {
        return (Config) super.getConfig();
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
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.layer_design_cupertino_toast_content, parent, false);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        bindDefaultContentData();
    }

    @NonNull
    public CupertinoToastLayer setMessage(@NonNull CharSequence message) {
        getConfig().mMessage = message;
        return this;
    }

    @NonNull
    public CupertinoToastLayer setMessage(int message) {
        getConfig().mMessage = getActivity().getString(message);
        return this;
    }

    @NonNull
    public CupertinoToastLayer setIcon(@DrawableRes int icon) {
        getConfig().mIcon = icon;
        return this;
    }

    @NonNull
    public CupertinoToastLayer setBackgroundDrawable(@NonNull Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
        return this;
    }

    @NonNull
    public CupertinoToastLayer setBackgroundResource(@DrawableRes int resource) {
        getConfig().mBackgroundResource = resource;
        return this;
    }

    @NonNull
    public CupertinoToastLayer setBackgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public CupertinoToastLayer setBackgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public CupertinoToastLayer setTextColorInt(@ColorInt int colorInt) {
        getConfig().mTextColorInt = colorInt;
        return this;
    }

    @NonNull
    public CupertinoToastLayer setTextColorRes(@ColorRes int colorRes) {
        getConfig().mTextColorRes = colorRes;
        return this;
    }

    private void bindDefaultContentData() {
        if (getConfig().mBackgroundDrawable != null) {
            getViewHolder().getContent().setBackgroundDrawable(getConfig().mBackgroundDrawable);
        } else if (getConfig().mBackgroundResource > 0) {
            getViewHolder().getContent().setBackgroundResource(getConfig().mBackgroundResource);
        }
        if (getViewHolder().getContent().getBackground() != null) {
            getViewHolder().getContent().getBackground().setColorFilter(getConfig().mBackgroundColor, PorterDuff.Mode.SRC_ATOP);
        }
        if (getViewHolder().getIcon() != null) {
            if (getConfig().mIcon > 0) {
                getViewHolder().getIcon().setVisibility(View.VISIBLE);
                getViewHolder().getIcon().setImageResource(getConfig().mIcon);
            } else {
                getViewHolder().getIcon().setVisibility(View.GONE);
            }
        }
        if (getViewHolder().getMessage() != null) {
            if (getConfig().mTextColorInt != Color.TRANSPARENT) {
                getViewHolder().getMessage().setTextColor(getConfig().mTextColorInt);
            } else if (getConfig().mTextColorRes != -1) {
                getViewHolder().getMessage().setTextColor(ContextCompat.getColor(getActivity(), getConfig().mTextColorRes));
            }
            if (TextUtils.isEmpty(getConfig().mMessage)) {
                getViewHolder().getMessage().setVisibility(View.GONE);
                getViewHolder().getMessage().setText("");
            } else {
                getViewHolder().getMessage().setVisibility(View.VISIBLE);
                getViewHolder().getMessage().setText(getConfig().mMessage);
            }
        }
    }

    public static class ViewHolder extends ToastLayer.ViewHolder {
        @Nullable
        public ImageView getIcon() {
            return getContent().findViewById(R.id.layer_design_cupertino_toast_content_icon);
        }

        @Nullable
        public TextView getMessage() {
            return getContent().findViewById(R.id.layer_design_cupertino_toast_content_msg);
        }
    }

    protected static class Config extends ToastLayer.Config {
        @NonNull
        private CharSequence mMessage = "";
        private int mIcon = 0;
        @Nullable
        private Drawable mBackgroundDrawable = null;
        private int mBackgroundResource = -1;
        private int mBackgroundColor = Color.TRANSPARENT;
        @ColorInt
        private int mTextColorInt = Color.TRANSPARENT;
        @ColorRes
        private int mTextColorRes = -1;
    }

    protected static class ListenerHolder extends ToastLayer.ListenerHolder {
    }
}
