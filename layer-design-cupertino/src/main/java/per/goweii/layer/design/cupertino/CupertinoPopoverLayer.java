package per.goweii.layer.design.cupertino;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.core.content.ContextCompat;

import per.goweii.layer.core.anim.AnimatorHelper;
import per.goweii.layer.popup.PopupLayer;
import per.goweii.layer.visualeffectview.PopupShadowLayout;

public class CupertinoPopoverLayer extends PopupLayer {
    public CupertinoPopoverLayer(@NonNull Context context) {
        super(context);
        init();
    }

    public CupertinoPopoverLayer(@NonNull Activity activity) {
        super(activity);
        init();
    }

    public CupertinoPopoverLayer(@NonNull View targetView) {
        super(targetView);
        init();
    }

    private void init() {
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
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View content = super.onCreateContent(inflater, parent);
        PopupShadowLayout popupShadowLayout = new PopupShadowLayout(getActivity());
        ViewGroup.LayoutParams contentLayoutParams = content.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        if (contentLayoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if (contentLayoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        content.setLayoutParams(layoutParams);
        popupShadowLayout.setLayoutParams(contentLayoutParams);
        popupShadowLayout.addView(content);
        return popupShadowLayout;
    }

    @Override
    protected void onInitContent() {
        super.onInitContent();
        PopupShadowLayout content = getViewHolder().getContent();
        content.setArrowSide(getConfig().mArrowSide);
        content.setArrowRadius(getConfig().mArrowRadius);
        content.setArrowOffset(getConfig().mArrowOffset);
        content.setArrowWidth(getConfig().mArrowWidth);
        content.setArrowHeight(getConfig().mArrowHeight);
        content.setCornerRadius(getConfig().mCornerRadius);
        content.setSolidColor(getConfig().mSolidColor);
        content.setShadowColor(getActivity().getResources().getColor(R.color.layer_design_cupertino_color_shadow));
        content.setShadowRadius(getActivity().getResources().getDimension(R.dimen.layer_design_cupertino_alert_shadow_radius));
        content.setShadowOffsetY(getActivity().getResources().getDimension(R.dimen.layer_design_cupertino_alert_shadow_offset_y));
        content.setShadowSymmetry(false);
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentInAnimator(@NonNull View view) {
        switch (getConfig().mArrowSide) {
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_TOP:
                return AnimatorHelper.createZoomAlphaInAnim(view,
                        (int) getViewHolder().getContent().getRealArrowOffset(), 0,
                        0.618F
                );
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_BOTTOM:
                return AnimatorHelper.createZoomAlphaInAnim(view,
                        (int) getViewHolder().getContent().getRealArrowOffset(), view.getHeight(),
                        0.618F
                );
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_LEFT:
                return AnimatorHelper.createZoomAlphaInAnim(view,
                        0, (int) getViewHolder().getContent().getRealArrowOffset(),
                        0.618F
                );
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_RIGHT:
                return AnimatorHelper.createZoomAlphaInAnim(view,
                        view.getWidth(), (int) getViewHolder().getContent().getRealArrowOffset(),
                        0.618F
                );
            default:
                return super.onCreateDefContentInAnimator(view);
        }
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentOutAnimator(@NonNull View view) {
        switch (getConfig().mArrowSide) {
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_TOP:
                return AnimatorHelper.createZoomAlphaOutAnim(view,
                        (int) getViewHolder().getContent().getRealArrowOffset(), 0,
                        0.618F
                );
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_BOTTOM:
                return AnimatorHelper.createZoomAlphaOutAnim(view,
                        (int) getViewHolder().getContent().getRealArrowOffset(), view.getHeight(),
                        0.618F
                );
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_LEFT:
                return AnimatorHelper.createZoomAlphaOutAnim(view,
                        0, (int) getViewHolder().getContent().getRealArrowOffset(),
                        0.618F
                );
            case PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_RIGHT:
                return AnimatorHelper.createZoomAlphaOutAnim(view,
                        view.getWidth(), (int) getViewHolder().getContent().getRealArrowOffset(),
                        0.618F
                );
            default:
                return super.onCreateDefContentOutAnimator(view);
        }
    }

    public CupertinoPopoverLayer setUseDefaultConfig() {
        setDirection(Align.Direction.VERTICAL);
        setHorizontal(Align.Horizontal.CENTER);
        setVertical(Align.Vertical.BELOW);
        setInside(true);
        setArrowSide(PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_TOP);
        setArrowOffset(PopupShadowLayout.PopupShadowOutlineProvider.ARROW_CENTER);
        setArrowRadius(getActivity().getResources().getDimensionPixelOffset(R.dimen.layer_design_cupertino_popover_arrow_corner_radius));
        setArrowWidth(getActivity().getResources().getDimensionPixelOffset(R.dimen.layer_design_cupertino_popover_arrow_width));
        setArrowHeight(getActivity().getResources().getDimensionPixelOffset(R.dimen.layer_design_cupertino_popover_arrow_height));
        setCornerRadius(getActivity().getResources().getDimensionPixelOffset(R.dimen.layer_design_cupertino_popover_corner_radius));
        setSolidColor(ContextCompat.getColor(getActivity(), R.color.layer_design_res_color_surface));
        return this;
    }

    public CupertinoPopoverLayer setArrowRadius(@Px int arrowRadius) {
        getConfig().mArrowRadius = arrowRadius;
        return this;
    }

    /**
     * 箭头距离左或上的偏移量
     * 居中可以使用{@link PopupShadowLayout.PopupShadowOutlineProvider#ARROW_CENTER}
     *
     * @param arrowOffset 偏移量
     */
    public CupertinoPopoverLayer setArrowOffset(int arrowOffset) {
        getConfig().mArrowOffset = arrowOffset;
        return this;
    }

    public CupertinoPopoverLayer setArrowHeight(@Px int arrowHeight) {
        getConfig().mArrowHeight = arrowHeight;
        return this;
    }

    public CupertinoPopoverLayer setSolidColor(@ColorInt int solidColor) {
        getConfig().mSolidColor = solidColor;
        return this;
    }

    public CupertinoPopoverLayer setArrowWidth(@Px int arrowWidth) {
        getConfig().mArrowWidth = arrowWidth;
        return this;
    }

    public CupertinoPopoverLayer setCornerRadius(@Px int cornerRadius) {
        getConfig().mCornerRadius = cornerRadius;
        return this;
    }

    public CupertinoPopoverLayer setArrowSide(@PopupShadowLayout.PopupShadowOutlineProvider.ArrowSide int arrowSide) {
        getConfig().mArrowSide = arrowSide;
        return this;
    }

    public static class Config extends PopupLayer.Config {
        protected int mArrowSide = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_NONE;
        protected int mArrowOffset = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_CENTER;
        protected int mArrowRadius = 0;
        protected int mArrowWidth = 0;
        protected int mArrowHeight = 0;
        protected int mCornerRadius = 0;
        protected int mSolidColor = Color.TRANSPARENT;
    }

    public static class ViewHolder extends PopupLayer.ViewHolder {
        @Override
        protected void setContent(@NonNull View content) {
            super.setContent(content);
        }

        @NonNull
        @Override
        public PopupShadowLayout getContent() {
            return (PopupShadowLayout) super.getContent();
        }
    }

    public static class ListenerHolder extends PopupLayer.ListenerHolder {
    }
}
