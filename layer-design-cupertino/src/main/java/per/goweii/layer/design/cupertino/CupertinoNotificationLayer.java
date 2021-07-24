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
import android.widget.FrameLayout;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import per.goweii.layer.notification.NotificationLayer;
import per.goweii.layer.core.utils.Utils;
import per.goweii.visualeffect.blur.RSBlurEffect;
import per.goweii.visualeffect.core.VisualEffect;
import per.goweii.visualeffect.view.BackdropVisualEffectFrameLayout;

public class CupertinoNotificationLayer extends NotificationLayer {
    public CupertinoNotificationLayer(@NonNull Context context) {
        super(context);
    }

    public CupertinoNotificationLayer(@NonNull Activity activity) {
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
        View content = inflater.inflate(R.layout.layer_design_cupertino_notification, parent, false);
        final Context context = getActivity();
        if (Utils.findViewByClass(content, BackdropVisualEffectFrameLayout.class) == null) {
            if (getConfig().mContentBlurPercent > 0 || getConfig().mContentBlurRadius > 0) {
                final BackdropVisualEffectFrameLayout backdropVisualEffectFrameLayout = new BackdropVisualEffectFrameLayout(context);
                backdropVisualEffectFrameLayout.setShowDebugInfo(false);
                backdropVisualEffectFrameLayout.setOverlayColor(getConfig().mContentBlurColor);

                FrameLayout.LayoutParams contentLayoutParams = (FrameLayout.LayoutParams) content.getLayoutParams();
                content.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));
                if (content.getBackground() != null) {
                    content.getBackground().setAlpha(0);
                }
                backdropVisualEffectFrameLayout.addView(content);

                CardView cardView = new CardView(context);
                cardView.setCardBackgroundColor(Color.TRANSPARENT);
                cardView.setMaxCardElevation(0);
                cardView.setCardElevation(0);
                cardView.setRadius(getConfig().mContentBlurCornerRadius);
                cardView.setLayoutParams(contentLayoutParams);

                cardView.addView(backdropVisualEffectFrameLayout, new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.MATCH_PARENT
                ));

                if (getConfig().mContentBlurPercent > 0) {
                    Utils.onViewLayout(backdropVisualEffectFrameLayout, new Runnable() {
                        @Override
                        public void run() {
                            int w = backdropVisualEffectFrameLayout.getWidth();
                            int h = backdropVisualEffectFrameLayout.getHeight();
                            float radius = Math.min(w, h) * getConfig().mContentBlurPercent;
                            float simple = getConfig().mContentBlurSimple;
                            if (radius > 25) {
                                simple = simple * (radius / 25);
                                radius = 25;
                            }
                            backdropVisualEffectFrameLayout.setSimpleSize(simple);
                            VisualEffect visualEffect = new RSBlurEffect(getActivity(), radius);
                            backdropVisualEffectFrameLayout.setVisualEffect(visualEffect);
                        }
                    });
                } else {
                    float radius = getConfig().mContentBlurRadius;
                    float simple = getConfig().mContentBlurSimple;
                    if (radius > 25) {
                        simple = simple * (radius / 25);
                        radius = 25;
                    }
                    backdropVisualEffectFrameLayout.setSimpleSize(simple);
                    VisualEffect visualEffect = new RSBlurEffect(getActivity(), radius);
                    backdropVisualEffectFrameLayout.setVisualEffect(visualEffect);
                }

                content = cardView;
            }
        }
        return content;
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
    public CupertinoNotificationLayer setContentBlurColorInt(@ColorInt int colorInt) {
        getConfig().mContentBlurColor = colorInt;
        return this;
    }

    @NonNull
    public CupertinoNotificationLayer setContentBlurColorRes(@ColorRes int colorRes) {
        getConfig().mContentBlurColor = getActivity().getResources().getColor(colorRes);
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
    public CupertinoNotificationLayer setContentBlurCornerRadiusPx(float radius) {
        getConfig().mContentBlurCornerRadius = radius;
        return this;
    }

    protected static class Config extends NotificationLayer.Config {
        protected float mContentBlurPercent = 0F;
        protected float mContentBlurRadius = 0F;
        protected float mContentBlurSimple = 4F;
        @ColorInt
        protected int mContentBlurColor = Color.TRANSPARENT;
        protected float mContentBlurCornerRadius = 0F;

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
