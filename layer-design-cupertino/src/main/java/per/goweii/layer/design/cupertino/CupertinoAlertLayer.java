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
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.dialog.DialogLayer;
import per.goweii.layer.visualeffectview.BackdropBlurView;
import per.goweii.layer.visualeffectview.BackdropIgnoreView;
import per.goweii.roundedshadowlayout.RoundedShadowLayout;

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
        setContentBackgroundColorRes(R.color.layer_design_cupertino_color_alert_blur_overlay);
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
            getViewHolder().setBackgroundBackdropBlurView(backdropBlurView);
            return backdropBlurView;
        }
        return super.onCreateBackground(inflater, parent);
    }

    @NonNull
    @Override
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View content = super.onCreateContent(inflater, parent);
        ViewGroup.LayoutParams contentLayoutParams = content.getLayoutParams();

        final BackdropBlurView backdropBlurView = new BackdropBlurView(getActivity());
        backdropBlurView.setOverlayColor(getConfig().mContentBackgroundColor);
        backdropBlurView.setSimpleSize(getConfig().mContentBlurSimple);
        backdropBlurView.setBlurRadius(getConfig().mContentBlurRadius);
        backdropBlurView.setBlurPercent(getConfig().mContentBlurPercent);
        backdropBlurView.addView(content, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        getViewHolder().setContentBackdropBlurView(backdropBlurView);

        RoundedShadowLayout shadowLayout = new RoundedShadowLayout(getActivity());
        shadowLayout.setCornerRadius(getConfig().mContentBlurCornerRadius);
        shadowLayout.setShadowColor(getActivity().getResources().getColor(R.color.layer_design_cupertino_color_shadow));
        shadowLayout.setShadowRadius(getActivity().getResources().getDimension(R.dimen.layer_design_cupertino_alert_shadow_radius));
        shadowLayout.setShadowOffsetY(getActivity().getResources().getDimension(R.dimen.layer_design_cupertino_alert_shadow_offset_y));
        shadowLayout.setShadowSymmetry(true);
        shadowLayout.addView(backdropBlurView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        BackdropIgnoreView backdropIgnoreView = new BackdropIgnoreView(getActivity());
        backdropIgnoreView.addView(shadowLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        backdropIgnoreView.setLayoutParams(contentLayoutParams);
        return backdropIgnoreView;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        View background = getViewHolder().getBackground();
        View content = getViewHolder().getContent();
        BackdropBlurView backgroundBackdropBlurView = getViewHolder().getBackgroundBackdropBlurView();
        BackdropBlurView contentBackdropBlurView = getViewHolder().getContentBackdropBlurView();
        if (content instanceof BackdropIgnoreView) {
            BackdropIgnoreView backdropIgnoreView = (BackdropIgnoreView) content;
            backdropIgnoreView.addBackdropBlurView(contentBackdropBlurView);
        }
        if (background instanceof BackdropIgnoreView) {
            BackdropIgnoreView backdropIgnoreView = (BackdropIgnoreView) background;
            backdropIgnoreView.addBackdropBlurView(contentBackdropBlurView);
            backdropIgnoreView.addBackdropBlurView(backgroundBackdropBlurView);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onInitContent() {
        super.onInitContent();
        final Config config = getConfig();
        final ViewHolder viewHolder = getViewHolder();
        if (!TextUtils.isEmpty(config.mTitle)) {
            viewHolder.getTitle().setVisibility(View.VISIBLE);
            viewHolder.getTitle().setText(config.mTitle);
        } else {
            viewHolder.getTitle().setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(config.mDesc)) {
            viewHolder.getDesc().setVisibility(View.VISIBLE);
            viewHolder.getDesc().setText(config.mDesc);
        } else {
            viewHolder.getDesc().setVisibility(View.GONE);
        }
        viewHolder.getActions().removeAllViews();
        if (config.mActions.isEmpty()) {
            viewHolder.getDivider().setVisibility(View.GONE);
            viewHolder.getActions().setVisibility(View.GONE);
        } else {
            viewHolder.getDivider().setVisibility(View.VISIBLE);
            viewHolder.getActions().setVisibility(View.VISIBLE);
            LayoutInflater inflater = getLayoutInflater();
            if (config.mActions.size() == 1) {
                viewHolder.getActions().setOrientation(LinearLayout.HORIZONTAL);
                viewHolder.getActions().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
                viewHolder.getActions().setDividerDrawable(null);
                TextView textView = onCreateAction(inflater, viewHolder.getActions());
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
                viewHolder.getActions().addView(textView);
            } else if (config.mActions.size() == 2) {
                viewHolder.getActions().setOrientation(LinearLayout.HORIZONTAL);
                viewHolder.getActions().setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                viewHolder.getActions().setDividerDrawable(getActivity().getResources().getDrawable(R.drawable.layer_design_cupertino_divider_v));
                TextView textView0 = onCreateAction(inflater, viewHolder.getActions());
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
                TextView textView1 = onCreateAction(inflater, viewHolder.getActions());
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
                viewHolder.getActions().addView(textView1);
                viewHolder.getActions().addView(textView0);
            } else {
                viewHolder.getActions().setOrientation(LinearLayout.VERTICAL);
                viewHolder.getActions().setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                viewHolder.getActions().setDividerDrawable(getActivity().getResources().getDrawable(R.drawable.layer_design_cupertino_divider_h));
                for (int i = 0; i < config.mActions.size(); i++) {
                    final Action action = config.mActions.get(i);
                    TextView textView = onCreateAction(inflater, viewHolder.getActions());
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
                    viewHolder.getActions().addView(textView);
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
            backdropIgnoreView.addBackdropBlurView(null);
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
    public CupertinoAlertLayer setContentBackgroundColorInt(@ColorInt int colorInt) {
        getConfig().mContentBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public CupertinoAlertLayer setContentBackgroundColorRes(@ColorRes int colorRes) {
        getConfig().mContentBackgroundColor = getActivity().getResources().getColor(colorRes);
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
        protected int mContentBackgroundColor = Color.TRANSPARENT;
        protected float mContentBlurCornerRadius = 0F;

        @ColorInt
        public int getBackgroundColor() {
            return mBackgroundColor;
        }
    }

    public static class ViewHolder extends DialogLayer.ViewHolder {
        private BackdropBlurView mContentBackdropBlurView = null;
        private BackdropBlurView mBackgroundBackdropBlurView = null;

        public void setContentBackdropBlurView(BackdropBlurView contentBackdropBlurView) {
            mContentBackdropBlurView = contentBackdropBlurView;
        }

        @Nullable
        public BackdropBlurView getContentBackdropBlurView() {
            return mContentBackdropBlurView;
        }

        public void setBackgroundBackdropBlurView(BackdropBlurView backgroundBackdropBlurView) {
            mBackgroundBackdropBlurView = backgroundBackdropBlurView;
        }

        @Nullable
        public BackdropBlurView getBackgroundBackdropBlurView() {
            return mBackgroundBackdropBlurView;
        }

        public TextView getTitle() {
            return getContent().findViewById(R.id.layer_design_cupertino_alert_title);
        }

        public TextView getDesc() {
            return getContent().findViewById(R.id.layer_design_cupertino_alert_desc);
        }

        public View getDivider() {
            return getContent().findViewById(R.id.layer_design_cupertino_alert_divider);
        }

        public LinearLayout getActions() {
            return getContent().findViewById(R.id.layer_design_cupertino_alert_actions);
        }
    }

    public static class ListenerHolder extends DialogLayer.ListenerHolder {
    }
}
