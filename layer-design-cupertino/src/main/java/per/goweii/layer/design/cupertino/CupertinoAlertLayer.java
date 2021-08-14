package per.goweii.layer.design.cupertino;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.dialog.DialogLayer;
import per.goweii.layer.visualeffectview.BackdropBlurView;
import per.goweii.layer.visualeffectview.BackdropIgnoreView;

public class CupertinoAlertLayer extends DialogLayer {
    public CupertinoAlertLayer(@NonNull Context context) {
        super(context);
        init();
    }

    public CupertinoAlertLayer(@NonNull Activity activity) {
        super(activity);
        init();
    }

    private void init() {
        setContentView(R.layout.layer_design_cupertino_alert);
        setBackgroundDimDefault();
        setContentBlurColorRes(R.color.layer_design_cupertino_color_alert_blur_overlay);
        setContentBlurSimple(10F);
        setContentBlurRadius(10F);
        setContentBlurCornerRadiusPx(getActivity().getResources().getDimensionPixelSize(R.dimen.layer_design_cupertino_corner_radius_big));
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
    @Override
    protected View onCreateBackground(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getConfig().mContentBlurPercent > 0 || getConfig().mContentBlurRadius > 0) {
            BackdropIgnoreView backdropIgnoreView = new BackdropIgnoreView(getActivity());
            backdropIgnoreView.setBackgroundColor(getConfig().getBackgroundColor());
            return backdropIgnoreView;
        }
        if (getConfig().mBackgroundBlurPercent > 0 || getConfig().mBackgroundBlurRadius > 0) {
            final BackdropBlurView backdropBlurView = new BackdropBlurView(getActivity());
            backdropBlurView.setOverlayColor(getConfig().mBackgroundBlurColor);
            backdropBlurView.setSimpleSize(getConfig().mBackgroundBlurSimple);
            backdropBlurView.setBlurRadius(getConfig().mBackgroundBlurRadius);
            backdropBlurView.setBlurPercent(getConfig().mBackgroundBlurPercent);
            return backdropBlurView;
        }
        return super.onCreateBackground(inflater, parent);
    }

    @NonNull
    @Override
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View content = super.onCreateContent(inflater, parent);
        if (getConfig().mContentBlurPercent > 0 || getConfig().mContentBlurRadius > 0) {
            final BackdropBlurView backdropBlurView = new BackdropBlurView(getActivity());
            backdropBlurView.setOverlayColor(getConfig().mContentBlurColor);
            backdropBlurView.setCornerRadius(getConfig().mContentBlurCornerRadius);
            backdropBlurView.setSimpleSize(getConfig().mContentBlurSimple);
            backdropBlurView.setBlurRadius(getConfig().mContentBlurRadius);
            backdropBlurView.setBlurPercent(getConfig().mContentBlurPercent);
            backdropBlurView.setLayoutParams(content.getLayoutParams());
            content.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            backdropBlurView.addView(content);
            if (content.getBackground() != null) {
                content.getBackground().setAlpha(0);
            }
            content = backdropBlurView;
        }
        return content;
    }

    @Override
    protected void onInitBackground() {
        super.onInitBackground();
        View background = getViewHolder().getBackground();
        View content = getViewHolder().getContent();
        if (background instanceof BackdropIgnoreView && content instanceof BackdropBlurView) {
            BackdropIgnoreView backdropIgnoreView = (BackdropIgnoreView) background;
            BackdropBlurView backdropBlurView = (BackdropBlurView) content;
            backdropIgnoreView.setBackdropBlurView(backdropBlurView);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onInitContent() {
        super.onInitContent();
        final Config config = getConfig();
        final ViewHolder viewHolder = getViewHolder();
        if (!TextUtils.isEmpty(config.mTitle)) {
            viewHolder.mTitle.setVisibility(View.VISIBLE);
            viewHolder.mTitle.setText(config.mTitle);
        } else {
            viewHolder.mTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(config.mDesc)) {
            viewHolder.mDesc.setVisibility(View.VISIBLE);
            viewHolder.mDesc.setText(config.mDesc);
        } else {
            viewHolder.mDesc.setVisibility(View.GONE);
        }
        viewHolder.mActions.removeAllViews();
        if (config.mActions.isEmpty()) {
            viewHolder.mDivider.setVisibility(View.GONE);
            viewHolder.mActions.setVisibility(View.GONE);
        } else {
            viewHolder.mDivider.setVisibility(View.VISIBLE);
            viewHolder.mActions.setVisibility(View.VISIBLE);
            LayoutInflater inflater = getLayoutInflater();
            if (config.mActions.size() == 1) {
                viewHolder.mActions.setOrientation(LinearLayout.HORIZONTAL);
                viewHolder.mActions.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
                viewHolder.mActions.setDividerDrawable(null);
                TextView textView = onCreateAction(inflater, viewHolder.mActions);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.weight = 0F;
                textView.setText(config.mActions.get(0).mName);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        config.mActions.get(0).mOnClickListener.onClick(CupertinoAlertLayer.this, v);
                    }
                });
                viewHolder.mActions.addView(textView);
            } else if (config.mActions.size() == 2) {
                viewHolder.mActions.setOrientation(LinearLayout.HORIZONTAL);
                viewHolder.mActions.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                viewHolder.mActions.setDividerDrawable(getActivity().getResources().getDrawable(R.drawable.layer_design_cupertino_divider_v));
                TextView textView0 = onCreateAction(inflater, viewHolder.mActions);
                textView0.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) textView0.getLayoutParams();
                params2.width = 0;
                params2.weight = 1F;
                textView0.setText(config.mActions.get(0).mName);
                textView0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        config.mActions.get(0).mOnClickListener.onClick(CupertinoAlertLayer.this, v);
                    }
                });
                TextView textView1 = onCreateAction(inflater, viewHolder.mActions);
                textView1.setTypeface(null, Typeface.NORMAL);
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) textView1.getLayoutParams();
                params1.width = 0;
                params1.weight = 1F;
                textView1.setText(config.mActions.get(1).mName);
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        config.mActions.get(1).mOnClickListener.onClick(CupertinoAlertLayer.this, v);
                    }
                });
                viewHolder.mActions.addView(textView1);
                viewHolder.mActions.addView(textView0);
            } else {
                viewHolder.mActions.setOrientation(LinearLayout.VERTICAL);
                viewHolder.mActions.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                viewHolder.mActions.setDividerDrawable(getActivity().getResources().getDrawable(R.drawable.layer_design_cupertino_divider_h));
                for (int i = 0; i < config.mActions.size(); i++) {
                    final Action action = config.mActions.get(i);
                    TextView textView = onCreateAction(inflater, viewHolder.mActions);
                    if (i == 0) {
                        textView.setTypeface(null, Typeface.BOLD);
                    } else {
                        textView.setTypeface(null, Typeface.NORMAL);
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.weight = 0F;
                    textView.setText(action.mName);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            action.mOnClickListener.onClick(CupertinoAlertLayer.this, v);
                        }
                    });
                    viewHolder.mActions.addView(textView);
                }
            }
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        View background = getViewHolder().getBackground();
        if (background instanceof BackdropIgnoreView) {
            BackdropIgnoreView backdropIgnoreView = (BackdropIgnoreView) background;
            backdropIgnoreView.setBackdropBlurView(null);
        }
    }

    protected TextView onCreateAction(@NonNull LayoutInflater inflater, @NonNull LinearLayout parent) {
        return (TextView) inflater.inflate(R.layout.layer_design_cupertino_alert_action, parent, false);
    }

    public CupertinoAlertLayer addAction(@NonNull String actionName, @NonNull OnClickListener onClickListener) {
        getConfig().mActions.add(new Action(actionName, onClickListener));
        return this;
    }

    public CupertinoAlertLayer addAction(@StringRes int actionName, @NonNull OnClickListener onClickListener) {
        return addAction(getActivity().getString(actionName), onClickListener);
    }

    public CupertinoAlertLayer setTitle(@NonNull String title) {
        getConfig().mTitle = title;
        return this;
    }

    public CupertinoAlertLayer setTitle(@StringRes int title) {
        return setTitle(getActivity().getString(title));
    }

    public CupertinoAlertLayer setDesc(@NonNull String desc) {
        getConfig().mDesc = desc;
        return this;
    }

    public CupertinoAlertLayer setDesc(@StringRes int desc) {
        return setDesc(getActivity().getString(desc));
    }

    @NonNull
    public CupertinoAlertLayer setBackgroundBlurRadius(@FloatRange(from = 0F) float radius) {
        getConfig().mBackgroundBlurRadius = radius;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setBackgroundBlurPercent(@FloatRange(from = 0F) float percent) {
        getConfig().mBackgroundBlurPercent = percent;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setBackgroundBlurSimple(@FloatRange(from = 1F) float simple) {
        getConfig().mBackgroundBlurSimple = simple;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setBackgroundBlurColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundBlurColor = colorInt;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setBackgroundBlurColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundBlurColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurRadius(@FloatRange(from = 0F) float radius) {
        getConfig().mContentBlurRadius = radius;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurPercent(@FloatRange(from = 0F) float percent) {
        getConfig().mContentBlurPercent = percent;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurSimple(@FloatRange(from = 1F) float simple) {
        getConfig().mContentBlurSimple = simple;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurColorInt(@ColorInt int colorInt) {
        getConfig().mContentBlurColor = colorInt;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurColorRes(@ColorRes int colorRes) {
        getConfig().mContentBlurColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurCornerRadius(float radius, int unit) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                unit, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurCornerRadiusDp(float radius) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBlurCornerRadiusPx(float radius) {
        getConfig().mContentBlurCornerRadius = radius;
        return this;
    }

    private static class Action {
        private final String mName;
        private final OnClickListener mOnClickListener;

        public Action(String name, OnClickListener onClickListener) {
            mName = name;
            mOnClickListener = onClickListener;
        }

        public String getName() {
            return mName;
        }

        public OnClickListener getOnClickListener() {
            return mOnClickListener;
        }
    }

    public static class Config extends DialogLayer.Config {
        private String mTitle = null;
        private String mDesc = null;
        private final List<Action> mActions = new ArrayList<>(3);

        protected float mBackgroundBlurPercent = 0F;
        protected float mBackgroundBlurRadius = 0F;
        protected float mBackgroundBlurSimple = 8F;
        @ColorInt
        protected int mBackgroundBlurColor = Color.TRANSPARENT;

        protected float mContentBlurPercent = 0F;
        protected float mContentBlurRadius = 0F;
        protected float mContentBlurSimple = 8F;
        @ColorInt
        protected int mContentBlurColor = Color.TRANSPARENT;
        protected float mContentBlurCornerRadius = 0F;

        @ColorInt
        public int getBackgroundColor() {
            return mBackgroundColor;
        }
    }

    public static class ViewHolder extends DialogLayer.ViewHolder {
        private TextView mTitle;
        private TextView mDesc;
        private View mDivider;
        private LinearLayout mActions;

        @Override
        protected void setContent(@NonNull View content) {
            super.setContent(content);
            mTitle = content.findViewById(R.id.layer_design_cupertino_alert_title);
            mDesc = content.findViewById(R.id.layer_design_cupertino_alert_desc);
            mDivider = content.findViewById(R.id.layer_design_cupertino_alert_divider);
            mActions = content.findViewById(R.id.layer_design_cupertino_alert_actions);
        }
    }

    public static class ListenerHolder extends DialogLayer.ListenerHolder {
    }
}
