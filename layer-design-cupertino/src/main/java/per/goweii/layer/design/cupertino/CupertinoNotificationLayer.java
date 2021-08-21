package per.goweii.layer.design.cupertino;

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
import android.widget.RelativeLayout;
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
import per.goweii.layer.visualeffectview.BackdropBlurView;
import per.goweii.layer.visualeffectview.BackdropIgnoreView;
import per.goweii.layer.visualeffectview.RoundedShadowLayout;

public class CupertinoNotificationLayer extends NotificationLayer {
    public CupertinoNotificationLayer(@NonNull Context context) {
        super(context);
        init();
    }

    public CupertinoNotificationLayer(@NonNull Activity activity) {
        super(activity);
        init();
    }

    private void init() {
        setContentBlurSimple(10F);
        setContentBlurRadius(10F);
        setContentBackgroundColorRes(R.color.layer_design_cupertino_color_notification_blur_overlay);
        setContentCornerRadiusPx(getActivity().getResources().getDimensionPixelSize(R.dimen.layer_design_cupertino_corner_radius_big));
        setContentView(R.layout.layer_design_cupertino_notification);
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
        View content = super.onCreateContent(inflater, parent);
        ViewGroup.LayoutParams contentLayoutParams = content.getLayoutParams();

        final BackdropBlurView backdropBlurView = new BackdropBlurView(getActivity());
        backdropBlurView.setOverlayColor(getConfig().mContentBackgroundColor);
        backdropBlurView.setSimpleSize(getConfig().mContentBlurSimple);
        backdropBlurView.setBlurRadius(getConfig().mContentBlurRadius);
        backdropBlurView.setBlurPercent(getConfig().mContentBlurPercent);
        backdropBlurView.addView(content, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RoundedShadowLayout shadowLayout = new RoundedShadowLayout(getActivity());
        shadowLayout.setCornerRadius(getConfig().mContentBlurCornerRadius);
        shadowLayout.setShadowColor(getActivity().getResources().getColor(R.color.layer_design_cupertino_color_shadow));
        shadowLayout.setShadowRadius(getActivity().getResources().getDimension(R.dimen.layer_design_cupertino_notification_shadow_radius));
        shadowLayout.setShadowOffsetY(getActivity().getResources().getDimension(R.dimen.layer_design_cupertino_notification_shadow_offset_y));
        shadowLayout.setShadowSymmetry(false);
        shadowLayout.addView(backdropBlurView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        BackdropIgnoreView backdropIgnoreView = new BackdropIgnoreView(getActivity());
        backdropIgnoreView.addBackdropBlurView(backdropBlurView);
        backdropIgnoreView.addView(shadowLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        backdropIgnoreView.setLayoutParams(contentLayoutParams);
        return backdropIgnoreView;
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
            RelativeLayout topView = getViewHolder().getTop();
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
    public CupertinoNotificationLayer setIcon(@DrawableRes int drawableId) {
        getConfig().mIcon = ContextCompat.getDrawable(getActivity(), drawableId);
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setIcon(@Nullable Drawable drawable) {
        getConfig().mIcon = drawable;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setLabel(@Nullable CharSequence label) {
        getConfig().mLabel = label;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setLabel(@StringRes int labelRes) {
        getConfig().mLabel = getActivity().getString(labelRes);
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setTime(@Nullable CharSequence time) {
        getConfig().mTime = time;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setTimePattern(@Nullable String pattern) {
        getConfig().mTimePattern = pattern;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setTitle(@NonNull CharSequence title) {
        getConfig().mTitle = title;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setTitle(@StringRes int titleRes) {
        getConfig().mTitle = getActivity().getString(titleRes);
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setDesc(@NonNull CharSequence desc) {
        getConfig().mDesc = desc;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setDesc(@StringRes int descRes) {
        getConfig().mDesc = getActivity().getString(descRes);
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBlurRadius(@FloatRange(from = 0F) float radius) {
        getConfig().mContentBlurRadius = radius;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBlurPercent(@FloatRange(from = 0F) float percent) {
        getConfig().mContentBlurPercent = percent;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBlurSimple(@FloatRange(from = 1F) float simple) {
        getConfig().mContentBlurSimple = simple;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBackgroundColorInt(@ColorInt int colorInt) {
        getConfig().mContentBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBackgroundColorRes(@ColorRes int colorRes) {
        getConfig().mContentBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBlurCornerRadius(float radius, int unit) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                unit, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBlurCornerRadiusDp(float radius) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentCornerRadiusPx(float radius) {
        getConfig().mContentBlurCornerRadius = radius;
        return this;
    }

    protected static class Config extends NotificationLayer.Config {
        protected float mContentBlurPercent = 0F;
        protected float mContentBlurRadius = 0F;
        protected float mContentBlurSimple = 8F;
        protected float mContentBlurCornerRadius = 0F;
        @ColorInt
        protected int mContentBackgroundColor = Color.TRANSPARENT;

        protected CharSequence mLabel = null;
        protected Drawable mIcon = null;
        protected CharSequence mTime = null;
        protected String mTimePattern = null;
        protected CharSequence mTitle = null;
        protected CharSequence mDesc = null;
    }

    public static class ViewHolder extends NotificationLayer.ViewHolder {
        @Nullable
        public RelativeLayout getTop() {
            return findViewInChild(R.id.layer_design_cupertino_notification_top);
        }

        @Nullable
        public ImageView getIcon() {
            return getContent().findViewById(R.id.layer_design_cupertino_notification_icon);
        }

        @Nullable
        public TextView getLabel() {
            return getContent().findViewById(R.id.layer_design_cupertino_notification_label);
        }

        @Nullable
        public TextView getTime() {
            return getContent().findViewById(R.id.layer_design_cupertino_notification_time);
        }

        @Nullable
        public TextView getTitle() {
            return getContent().findViewById(R.id.layer_design_cupertino_notification_title);
        }

        @Nullable
        public TextView getDesc() {
            return getContent().findViewById(R.id.layer_design_cupertino_notification_desc);
        }
    }

    protected static class ListenerHolder extends NotificationLayer.ListenerHolder {
    }
}
