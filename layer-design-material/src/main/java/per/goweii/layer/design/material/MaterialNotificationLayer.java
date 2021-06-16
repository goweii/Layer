package per.goweii.layer.design.material;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import per.goweii.layer.notification.NotificationLayer;

public class MaterialNotificationLayer extends NotificationLayer {
    public MaterialNotificationLayer(@NonNull Context context) {
        super(context);
    }

    public MaterialNotificationLayer(@NonNull Activity activity) {
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
        return inflater.inflate(R.layout.layer_design_material_notification, parent, false);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        bindDefaultContentData();
    }

    private void bindDefaultContentData() {
        if (getViewHolder().getTop() != null) {
            if (getViewHolder().getIcon() != null) {
                if (getConfig().mIcon != null) {
                    getViewHolder().getIcon().setVisibility(View.VISIBLE);
                    getViewHolder().getIcon().setImageDrawable(getConfig().mIcon);
                } else {
                    getViewHolder().getIcon().setVisibility(View.GONE);
                }
            }
            if (getViewHolder().getLabel() != null) {
                if (!TextUtils.isEmpty(getConfig().mLabel)) {
                    getViewHolder().getLabel().setVisibility(View.VISIBLE);
                    getViewHolder().getLabel().setText(getConfig().mLabel);
                } else {
                    getViewHolder().getLabel().setVisibility(View.GONE);
                }
            }
            if (getViewHolder().getTime() != null) {
                if (!TextUtils.isEmpty(getConfig().mTime)) {
                    getViewHolder().getTime().setVisibility(View.VISIBLE);
                    getViewHolder().getTime().setText(getConfig().mTime);
                } else {
                    if (!TextUtils.isEmpty(getConfig().mTimePattern)) {
                        getViewHolder().getTime().setVisibility(View.VISIBLE);
                        String time = new SimpleDateFormat(getConfig().mTimePattern, Locale.getDefault()).format(new Date());
                        getViewHolder().getTime().setText(time);
                    } else {
                        getViewHolder().getTime().setVisibility(View.GONE);
                    }
                }
            }
            LinearLayout topView = getViewHolder().getTop();
            topView.setVisibility(View.GONE);
            for (int i = 0; i < topView.getChildCount(); i++) {
                if (topView.getChildAt(i).getVisibility() == View.VISIBLE) {
                    topView.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        if (getViewHolder().getTitle() != null) {
            if (!TextUtils.isEmpty(getConfig().mTitle)) {
                getViewHolder().getTitle().setVisibility(View.VISIBLE);
                getViewHolder().getTitle().setText(getConfig().mTitle);
            } else {
                getViewHolder().getTitle().setVisibility(View.GONE);
            }
        }
        if (getViewHolder().getDesc() != null) {
            if (!TextUtils.isEmpty(getConfig().mDesc)) {
                getViewHolder().getDesc().setVisibility(View.VISIBLE);
                getViewHolder().getDesc().setText(getConfig().mDesc);
            } else {
                getViewHolder().getDesc().setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    public MaterialNotificationLayer setIcon(@DrawableRes int drawableId) {
        getConfig().mIcon = ContextCompat.getDrawable(getActivity(), drawableId);
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setIcon(@Nullable Drawable drawable) {
        getConfig().mIcon = drawable;
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setLabel(@Nullable CharSequence label) {
        getConfig().mLabel = label;
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setLabel(@StringRes int labelRes) {
        getConfig().mLabel = getActivity().getString(labelRes);
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setTime(@Nullable CharSequence time) {
        getConfig().mTime = time;
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setTimePattern(@Nullable String pattern) {
        getConfig().mTimePattern = pattern;
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setTitle(@NonNull CharSequence title) {
        getConfig().mTitle = title;
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setTitle(@StringRes int titleRes) {
        getConfig().mTitle = getActivity().getString(titleRes);
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setDesc(@NonNull CharSequence desc) {
        getConfig().mDesc = desc;
        return this;
    }

    @NonNull
    public MaterialNotificationLayer setDesc(@StringRes int descRes) {
        getConfig().mDesc = getActivity().getString(descRes);
        return this;
    }

    protected static class Config extends NotificationLayer.Config {
        protected CharSequence mLabel = null;
        protected Drawable mIcon = null;
        protected CharSequence mTime = null;
        protected String mTimePattern = null;
        protected CharSequence mTitle = null;
        protected CharSequence mDesc = null;
    }

    public static class ViewHolder extends NotificationLayer.ViewHolder {
        @Nullable
        public LinearLayout getTop() {
            return findViewInChild(R.id.layer_design_material_notification_top);
        }

        @Nullable
        public ImageView getIcon() {
            return getContent().findViewById(R.id.layer_design_material_notification_icon);
        }

        @Nullable
        public TextView getLabel() {
            return getContent().findViewById(R.id.layer_design_material_notification_label);
        }

        @Nullable
        public TextView getTime() {
            return getContent().findViewById(R.id.layer_design_material_notification_time);
        }

        @Nullable
        public TextView getTitle() {
            return getContent().findViewById(R.id.layer_design_material_notification_title);
        }

        @Nullable
        public TextView getDesc() {
            return getContent().findViewById(R.id.layer_design_material_notification_desc);
        }
    }

    protected static class ListenerHolder extends NotificationLayer.ListenerHolder {
    }
}
