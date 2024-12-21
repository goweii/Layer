package per.goweii.layer.core;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.core.listener.DefaultAnimatorListener;
import per.goweii.layer.core.utils.Utils;

public class Layer {

    /**
     * 从子 view 获取其所属的 Layer ，必须为 layer 内部的 view ，否则会抛出空指针异常。
     * 需要获取可空类型可使用方法 {@link #findLayer(View)}
     *
     * @param view view
     * @param <T>  具体的 layer 实现类型
     * @return view 所属的 layer
     */
    @NonNull
    public static <T extends Layer> T requireLayer(@NonNull View view) {
        T layer = findLayer(view);
        return Utils.requireNonNull(layer, "view 不在 Layer 内部或还没有显示");
    }

    /**
     * 从 view 获取其所属的 Layer ，view 不属于对应类型的 layer 将返回 null 。
     *
     * @param view view
     * @param <T>  具体的 layer 实现类型
     * @return view 所属的 layer
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Layer> T findLayer(@NonNull View view) {
        while (true) {
            Object tag = view.getTag(R.id.layer_tag);
            if (tag != null) {
                try {
                    return (T) tag;
                } catch (ClassCastException ignore) {
                }
            }
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof ViewGroup) {
                view = (ViewGroup) viewParent;
                continue;
            }
            return null;
        }
    }


    private final ViewTreeObserver.OnPreDrawListener mOnGlobalPreDrawListener = new OnGlobalPreDrawListener();
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new OnGlobalLayoutListener();
    private final ViewManager.OnKeyListener mOnViewKeyListener = new OnViewKeyListener();

    private final Runnable mInAnimEndCallback = new OnInAnimEndCallback();
    private final Runnable mOutAnimEndCallback = new OnOutAnimEndCallback();

    private final ViewManager mViewManager;

    private final Config mConfig;
    private final ViewHolder mViewHolder;
    private final ListenerHolder mListenerHolder;

    private ViewTreeObserver.OnPreDrawListener mShowOnPreDrawListener = null;

    private boolean mShowWithAnim = false;
    private boolean mDismissWithAnim = false;

    private Animator mAnimatorIn = null;
    private Animator mAnimatorOut = null;

    private boolean mInitialized = false;

    private boolean mViewCacheable = false;

    public Layer() {
        mViewManager = new ViewManager();
        mConfig = onCreateConfig();
        mViewHolder = onCreateViewHolder();
        mListenerHolder = onCreateListenerHolder();
    }

    /**
     * 创建配置管理类，子类可以通过重写返回自己的继承自 {@link Config} 配置管理类，以添加自定义配置。
     *
     * @return Config 或其子类实例
     */
    @NonNull
    protected Config onCreateConfig() {
        return new Config();
    }

    /**
     * 获取配置管理类，返回对应的类型。
     *
     * @return Config 或其子类实例
     */
    @NonNull
    public Config getConfig() {
        return mConfig;
    }

    /**
     * 创建视图管理类，子类可以通过重写返回自己的继承自 {@link ViewHolder} 的视图管理类。
     *
     * @return ViewHolder 或其子类实例
     */
    @NonNull
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    /**
     * 获取视图管理类，返回对应的类型。
     *
     * @return ViewHolder 或其子类实例
     */
    @NonNull
    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    /**
     * 创建监听器管理类，子类可以通过重写返回自己的继承自 {@link ListenerHolder} 的监听器管理类。
     *
     * @return ListenerHolder 或其子类实例
     */
    @NonNull
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    /**
     * 获取监听器管理类，返回对应的类型。
     *
     * @return ListenerHolder 或其子类实例
     */
    @NonNull
    public ListenerHolder getListenerHolder() {
        return mListenerHolder;
    }

    /**
     * 生命周期，视图的创建。
     * <lt>
     * <li>1. 获取和创建父容器，父容器通过 {@link #onGetParent()} 方法获取。子视图将会被父容器的 addView 方法添加从而显示。
     * <li>2. 获取和创建子视图，子视图通过 {@link #onCreateChild(LayoutInflater, ViewGroup)} 方法获取。
     * <li>3. 检测子视图的 LayoutParams ，不存在则通过 {@link #generateDefaultLayoutParams()} 方法创建。
     * <li>4. 将父容器和子视图设置给 {@link ViewManager} ，并根据需要设置按键拦截。
     * <li>5. 根据需要回调生命周期监听器。
     * </lt>
     */
    @CallSuper
    protected void onCreate() {
        if (mViewHolder.getParentOrNull() == null) {
            ViewGroup parent = onGetParent();
            mViewHolder.setParent(parent);
        }
        if (mViewHolder.getChildOrNull() == null) {
            View child = onCreateChild(getLayoutInflater(), mViewHolder.getParent());
            mViewHolder.setChild(child);
        }
        ViewGroup.LayoutParams layoutParams = mViewHolder.getChild().getLayoutParams();
        if (layoutParams == null) {
            mViewHolder.getChild().setLayoutParams(generateDefaultLayoutParams());
        }
        mViewManager.setParent(mViewHolder.getParent());
        mViewManager.setChild(mViewHolder.getChild());
        mViewManager.setOnKeyListener(mConfig.mInterceptKeyEvent ? mOnViewKeyListener : null);
        if (!mInitialized) {
            mInitialized = true;
            mListenerHolder.notifyOnInitialize(this);
        }
    }

    /**
     * 生命周期，视图已被添加到父容器。
     * <lt>
     * <li>1. 标记 tag ，便于从 view 查找所属的 layer ，见 {@link #findLayer(View)} 。
     * <li>2. 对 ViewTreeObserver 设置必要的的监听，用来完成后续的生命周期。
     * <li>3. 绑定用户自定义的点击事件监听器。
     * <li>4. 回调生命周期监听器。
     * </lt>
     */
    @CallSuper
    protected void onAttach() {
        getViewHolder().getChild().setTag(R.id.layer_tag, this);
        if (getViewTreeObserver().isAlive()) {
            getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
            getViewTreeObserver().addOnPreDrawListener(mOnGlobalPreDrawListener);
        }
        mListenerHolder.bindOnClickListeners(this);
        mListenerHolder.bindOnLongClickListeners(this);
        mListenerHolder.notifyOnVisibleChangeToShow(this);
        mListenerHolder.notifyOnBindData(this);
    }

    /**
     * 生命周期，子视图已可见，开始绘制，将执行显示动画。
     * <lt>
     * <li>1. 回调生命周期监听器。
     * </lt>
     */
    @CallSuper
    protected void onPreShow() {
        mListenerHolder.notifyOnPreShow(this);
    }

    /**
     * 生命周期，子视图显示动画结束，完成显示相关生命周期。
     * <lt>
     * <li>1. 回调生命周期监听器。
     * </lt>
     */
    @CallSuper
    protected void onPostShow() {
        mListenerHolder.notifyOnPostShow(this);
    }

    /**
     * 生命周期，子视图开始执行消失动画。
     * <lt>
     * <li>1. 回调生命周期监听器。
     * </lt>
     */
    @CallSuper
    protected void onPreDismiss() {
        mListenerHolder.notifyOnPreDismiss(this);
    }

    /**
     * 生命周期，子视图消失动画结束，视图不可见。
     * <lt>
     * <li>1. 回调生命周期监听器。
     * </lt>
     */
    @CallSuper
    protected void onPostDismiss() {
        mListenerHolder.notifyOnPostDismiss(this);
    }

    /**
     * 生命周期，子视图已从父容器移除。
     * <lt>
     * <li>1. 回调生命周期监听器。
     * <li>2. 移除 onAttach 时设置的监听器。
     * <li>3. 移除 onAttach 时设置的 tag。
     * </lt>
     */
    @SuppressLint("ObsoleteSdkInt")
    @CallSuper
    protected void onDetach() {
        getListenerHolder().notifyOnVisibleChangeToDismiss(this);
        if (getViewTreeObserver().isAlive()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            } else {
                //noinspection deprecation
                getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            }
            getViewTreeObserver().removeOnPreDrawListener(mOnGlobalPreDrawListener);
        }
        getViewHolder().getChild().setTag(R.id.layer_tag, null);
    }

    /**
     * 生命周期，视图销毁。
     * <lt>
     * <li>1. 根据配置销毁子视图和父容器。
     * <li>2. 与 {@link ViewManager} 解绑。
     * </lt>
     */
    @CallSuper
    protected void onDestroy() {
        if (!mViewCacheable) {
            onResetParent();
            mViewHolder.setParent(null);
            onDestroyChild();
            mViewHolder.setChild(null);
        }
        mViewManager.setParent(null);
        mViewManager.setChild(null);
        mViewManager.setOnKeyListener(null);
    }

    /**
     * 获取子视图的直接父容器，根据需要为子视图创建直接父容器。
     *
     * @return 直接父容器
     */
    @NonNull
    protected ViewGroup onGetParent() {
        if (mConfig.mParentView != null) {
            return mConfig.mParentView;
        }
        throw new IllegalStateException("未设置父布局");
    }

    /**
     * 销毁直接父容器
     */
    protected void onResetParent() {
    }

    /**
     * 获取子视图。
     *
     * @return 子视图
     */
    @NonNull
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (mConfig.mChildView != null) {
            return mConfig.mChildView;
        }
        if (mConfig.mChildLayoutId != View.NO_ID) {
            return inflater.inflate(mConfig.mChildLayoutId, parent, false);
        }
        throw new IllegalStateException("未设置子控件");
    }

    /**
     * 销毁子视图。
     */
    protected void onDestroyChild() {
    }

    /**
     * 子视图无 LayoutParams 时，创建默认 LayoutParams 。
     *
     * @return 默认 LayoutParams
     */
    @NonNull
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 创建显示动画，返回 null 将不执行显示动画。
     *
     * @param view 子视图
     * @return 显示动画
     */
    @Nullable
    protected Animator onCreateInAnimator(@NonNull View view) {
        if (getConfig().mAnimatorCreator != null) {
            return getConfig().mAnimatorCreator.createInAnimator(view);
        }
        return null;
    }

    /**
     * 创建隐藏动画，返回 null 将不执行隐藏动画。
     *
     * @param view 子视图
     * @return 隐藏动画
     */
    @Nullable
    protected Animator onCreateOutAnimator(@NonNull View view) {
        if (getConfig().mAnimatorCreator != null) {
            return getConfig().mAnimatorCreator.createOutAnimator(view);
        }
        return null;
    }

    /**
     * 按键事件的拦截回调。
     *
     * @param event KeyEvent
     * @return true 表示自己消费，不再交由焦点 view 处理，false 相反
     */
    protected boolean onKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                return onKeyBack();
            }
        }
        return false;
    }

    /**
     * 返回键的拦截，默认实现为隐藏 layer 。
     *
     * @return 是否已消费
     */
    protected boolean onKeyBack() {
        if (getConfig().mCancelableOnKeyBack) {
            dismiss();
            return true;
        }
        return false;
    }

    /**
     * 全局视图树发生 onLayout 事件的回调。
     */
    protected void onGlobalLayout() {
    }

    /**
     * 全局视图树发生 onPreDraw 事件的回调，在 onDraw 之前。
     *
     * @return 是否拦截绘制，返回 true 将继续绘制，false 将跳过这一帧的绘制。
     */
    protected boolean onGlobalPreDraw() {
        return true;
    }

    /**
     * 执行显示流程，主要包括下面的生命周期：
     * <lt>
     * <li>{@link #onCreate()}
     * <li>{@link #onAttach()}
     * <li>{@link #onPreShow()}
     * <li>{@link #onPostShow()}
     * </lt>
     */
    @CallSuper
    protected void handleShow() {
        if (isShown()) {
            if (isOutAnimRunning()) {
                startAnimatorIn();
            }
            return;
        }
        onCreate();
        mViewManager.attach();
        onAttach();
        getViewHolder().getChild().setVisibility(View.VISIBLE);
        if (mShowOnPreDrawListener == null) {
            mShowOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (getViewTreeObserver().isAlive()) {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    mShowOnPreDrawListener = null;
                    onPreShow();
                    startAnimatorIn();
                    return true;
                }
            };
        }
        getViewTreeObserver().addOnPreDrawListener(mShowOnPreDrawListener);
    }

    private void startAnimatorIn() {
        cancelAnimator();
        if (mShowWithAnim) {
            mAnimatorIn = onCreateInAnimator(mViewHolder.getChild());
            if (mAnimatorIn != null) {
                mAnimatorIn.addListener(new DefaultAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimatorIn = null;
                    }

                    @Override
                    public void onAnimationEndNotCanceled(Animator animation) {
                        super.onAnimationEndNotCanceled(animation);
                        mInAnimEndCallback.run();
                    }
                });
                mAnimatorIn.start();
            } else {
                mInAnimEndCallback.run();
            }
        } else {
            mInAnimEndCallback.run();
        }
    }

    private void handleInAnimEnd() {
        onPostShow();
    }

    /**
     * 执行隐藏流程，主要包括下面的生命周期：
     * <lt>
     * <li>{@link #onPreDismiss()}
     * <li>{@link #onPostDismiss()}
     * <li>{@link #onDetach()}
     * <li>{@link #onDestroy()}
     * </lt>
     */
    @CallSuper
    protected void handleDismiss() {
        if (!isShown()) return;
        if (isOutAnimRunning()) return;
        if (mShowOnPreDrawListener != null) {
            if (getViewTreeObserver().isAlive()) {
                getViewTreeObserver().removeOnPreDrawListener(mShowOnPreDrawListener);
            }
            mShowOnPreDrawListener = null;
            mViewManager.detach();
            onDetach();
            onDestroy();
            return;
        }
        onPreDismiss();
        startAnimatorOut();
    }

    private void startAnimatorOut() {
        cancelAnimator();
        if (mDismissWithAnim) {
            mAnimatorOut = onCreateOutAnimator(mViewHolder.getChild());
            if (mAnimatorOut != null) {
                mAnimatorOut.addListener(new DefaultAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimatorOut = null;
                    }

                    @Override
                    public void onAnimationEndNotCanceled(Animator animation) {
                        super.onAnimationEndNotCanceled(animation);
                        getViewHolder().getChild().setVisibility(View.INVISIBLE);
                        getViewHolder().getParent().post(mOutAnimEndCallback);
                    }
                });
                mAnimatorOut.start();
            } else {
                getViewHolder().getChild().setVisibility(View.INVISIBLE);
                mOutAnimEndCallback.run();
            }
        } else {
            getViewHolder().getChild().setVisibility(View.INVISIBLE);
            mOutAnimEndCallback.run();
        }
    }

    private void handleOutAnimEnd() {
        onPostDismiss();
        mViewManager.detach();
        onDetach();
        onDestroy();
    }

    private ViewTreeObserver getViewTreeObserver() {
        return getViewHolder().getParent().getViewTreeObserver();
    }

    private void cancelAnimator() {
        final ViewGroup parent = getViewHolder().getParentOrNull();
        if (parent != null) {
            parent.removeCallbacks(mInAnimEndCallback);
            parent.removeCallbacks(mOutAnimEndCallback);
        }
        if (mAnimatorIn != null) {
            mAnimatorIn.removeAllListeners();
            mAnimatorIn.cancel();
            mAnimatorIn = null;
        }
        if (mAnimatorOut != null) {
            mAnimatorOut.removeAllListeners();
            mAnimatorOut.cancel();
            mAnimatorOut = null;
        }
    }

    /**
     * 显示视图，默认执行显示动画，见 {@link #show(boolean)}
     */
    public void show() {
        show(true);
    }

    /**
     * 显示 Layer ，将会触发显示流程，从而执行显示的生命周期。详见 {@link #handleShow()}
     *
     * @param withAnim 是否执行显示动画
     */
    public void show(boolean withAnim) {
        mShowWithAnim = withAnim;
        handleShow();
    }

    /**
     * 隐藏 Layer ，默认执行隐藏动画，见 {@link #dismiss(boolean)}
     */
    public void dismiss() {
        dismiss(true);
    }

    /**
     * 隐藏 Layer ，将会触发隐藏流程，从而执行隐藏的生命周期。详见 {@link #handleDismiss()}
     *
     * @param withAnim 是否执行隐藏动画
     */
    public void dismiss(boolean withAnim) {
        mDismissWithAnim = withAnim;
        handleDismiss();
    }

    /**
     * 判断 Layer 是否已经显示，即子视图已经添加到父容器
     *
     * @return 是否显示
     */
    public boolean isShown() {
        return mViewManager.isAttached();
    }

    /**
     * 怕断显示动画是否正在运行
     *
     * @return 是否正在运行显示动画
     */
    public boolean isInAnimRunning() {
        return mAnimatorIn != null && mAnimatorIn.isStarted();
    }

    /**
     * 怕断隐藏动画是否正在运行
     *
     * @return 是否正在运行隐藏动画
     */
    public boolean isOutAnimRunning() {
        return mAnimatorOut != null && mAnimatorOut.isStarted();
    }

    /**
     * 获取 LayoutInflater ，为工具方法。
     *
     * @return LayoutInflater
     */
    @NonNull
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mViewHolder.getParent().getContext());
    }

    /**
     * 是否需要在隐藏时缓存视图，见 {@link #onDestroy()}
     *
     * @return 是否需要在隐藏时缓存视图
     */
    public boolean isViewCacheable() {
        return mViewCacheable;
    }

    /**
     * 设置是否需要在隐藏时缓存视图，见 {@link #onDestroy()}
     */
    public void setViewCacheable(boolean viewCacheable) {
        mViewCacheable = viewCacheable;
    }

    /**
     * 获取父容器
     *
     * @return 父容器
     */
    @NonNull
    public ViewGroup getParent() {
        return mViewHolder.getParent();
    }

    /**
     * 获取子视图
     *
     * @return 子视图
     */
    @NonNull
    public View getChild() {
        return mViewHolder.getChild();
    }

    /**
     * 根据 id 获取对应的 view 。会进行判空。
     *
     * @param id  view 的 id
     * @param <V> view 的类型
     * @return 对应 view
     */
    @NonNull
    public <V extends View> V requireViewById(@IdRes int id) {
        V view = mViewHolder.findViewById(id);
        return Utils.requireNonNull(view);
    }

    /**
     * 根据 id 获取对应的 view 。
     *
     * @param id  view 的 id
     * @param <V> view 的类型
     * @return 对应 view
     */
    @Nullable
    public <V extends View> V findViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    /**
     * 设置父容器
     *
     * @param parent 父容器
     * @return this
     */
    @NonNull
    public Layer setParent(@NonNull ViewGroup parent) {
        mConfig.mParentView = parent;
        return this;
    }

    /**
     * 设置子视图
     *
     * @param child 子视图
     * @return this
     */
    @NonNull
    public Layer setChild(@Nullable View child) {
        mConfig.mChildView = child;
        return this;
    }

    /**
     * 设置子视图布局 id
     *
     * @param child 子视图布局 id
     * @return this
     */
    @NonNull
    public Layer setChild(@LayoutRes int child) {
        mConfig.mChildLayoutId = child;
        return this;
    }

    /**
     * 设置动画创建器，见 {@link AnimatorCreator} 。
     *
     * @param creator 动画创建器
     * @return this
     */
    @NonNull
    public Layer setAnimator(@Nullable AnimatorCreator creator) {
        mConfig.mAnimatorCreator = creator;
        return this;
    }

    /**
     * 设置是否拦截按键事件，如返回键。
     *
     * @param intercept 是否拦截
     * @return this
     */
    @NonNull
    public Layer setInterceptKeyEvent(boolean intercept) {
        mConfig.mInterceptKeyEvent = intercept;
        return this;
    }

    /**
     * 获取是否拦截按键事件。
     *
     * @return 是否拦截
     */
    public boolean isInterceptKeyEvent() {
        return mConfig.mInterceptKeyEvent;
    }

    /**
     * 设置是否可以通过返回按钮关闭 layer 。
     * 设置为 ture 会强制开启按键拦截，即 {@link #setInterceptKeyEvent(boolean)} 也会设为 true 。
     *
     * @param cancelable 是否可以通过返回按钮关闭 layer
     * @return this
     */
    @NonNull
    public Layer setCancelableOnKeyBack(boolean cancelable) {
        if (cancelable) {
            setInterceptKeyEvent(true);
        }
        mConfig.mCancelableOnKeyBack = cancelable;
        return this;
    }

    /**
     * 获取是否可以通过返回按钮关闭 layer 。
     *
     * @return 是否可以通过返回按钮关闭 layer 。
     */
    public boolean isCancelableOnKeyBack() {
        return mConfig.mCancelableOnKeyBack;
    }

    /**
     * 绑定数据
     * 获取子控件 ID 为 {@link #findViewById(int)}
     *
     * @param onBindDataListener 实现该接口进行数据绑定
     */
    @NonNull
    public Layer addOnBindDataListener(@NonNull OnBindDataListener onBindDataListener) {
        mListenerHolder.addOnBindDataListener(onBindDataListener);
        return this;
    }

    /**
     * 初始化
     * 获取子控件 ID 为 {@link #findViewById(int)}
     *
     * @param onInitializeListener 该接口仅在第一次加载时调用，可加载初始化数据
     */
    @NonNull
    public Layer addOnInitializeListener(@NonNull OnInitializeListener onInitializeListener) {
        mListenerHolder.addOnInitializeListener(onInitializeListener);
        return this;
    }

    /**
     * 设置显示状态改变的监听
     *
     * @param onVisibleChangedListener OnVisibleChangeListener
     */
    @NonNull
    public Layer addOnVisibleChangeListener(@NonNull OnVisibleChangedListener onVisibleChangedListener) {
        mListenerHolder.addOnVisibleChangeListener(onVisibleChangedListener);
        return this;
    }

    /**
     * 设置变更为显示状态监听
     *
     * @param onShowListener OnShowListener
     */
    @NonNull
    public Layer addOnShowListener(@NonNull OnShowListener onShowListener) {
        mListenerHolder.addOnShowListener(onShowListener);
        return this;
    }

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onDismissListener OnDismissListener
     */
    @NonNull
    public Layer addOnDismissListener(@NonNull OnDismissListener onDismissListener) {
        mListenerHolder.addOnDismissListener(onDismissListener);
        return this;
    }

    /**
     * 对多个 View 绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewIds 控件I D
     */
    @NonNull
    public Layer addOnClickToDismissListener(int... viewIds) {
        addOnClickToDismissListener(null, viewIds);
        return this;
    }

    /**
     * 对多个 View 绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param listener 监听器
     * @param viewIds  控件 ID
     */
    @NonNull
    public Layer addOnClickToDismissListener(@Nullable final OnClickListener listener, int... viewIds) {
        addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull Layer layer, @NonNull View v) {
                layer.dismiss();
                if (listener != null) {
                    listener.onClick(layer, v);
                }
            }
        }, viewIds);
        return this;
    }

    /**
     * 对多个 View 绑定点击事件
     *
     * @param listener 回调
     * @param viewIds  控件 ID
     */
    @NonNull
    public Layer addOnClickListener(@NonNull OnClickListener listener, int... viewIds) {
        mListenerHolder.addOnClickListener(listener, viewIds);
        return this;
    }

    /**
     * 对多个 View 绑定长按事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewIds 控件 ID
     */
    @NonNull
    public Layer addOnLongClickToDismissListener(int... viewIds) {
        addOnLongClickToDismissListener(null, viewIds);
        return this;
    }

    /**
     * 对多个 View 绑定长按事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param listener 监听器
     * @param viewIds  控件 ID
     */
    @NonNull
    public Layer addOnLongClickToDismissListener(@Nullable final OnLongClickListener listener, int... viewIds) {
        addOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(@NonNull Layer layer, @NonNull View v) {
                if (listener == null) {
                    dismiss();
                    return true;
                } else {
                    dismiss();
                    return listener.onLongClick(layer, v);
                }
            }
        }, viewIds);
        return this;
    }

    /**
     * 对多个 View 绑定长按事件
     *
     * @param listener 回调
     * @param viewIds  控件 ID
     */
    @NonNull
    public Layer addOnLongClickListener(@NonNull OnLongClickListener listener, int... viewIds) {
        mListenerHolder.addOnLongClickListener(listener, viewIds);
        return this;
    }

    /**
     * 配置管理类。
     * 子类可以通过继承扩充配置，之后在 {@link #onCreateConfig()} 和 {@link #getConfig()} 创建和返回。
     */
    protected static class Config {
        @Nullable
        private ViewGroup mParentView = null;
        @Nullable
        private View mChildView = null;
        @LayoutRes
        private int mChildLayoutId = View.NO_ID;

        private boolean mInterceptKeyEvent = false;
        private boolean mCancelableOnKeyBack = false;

        private AnimatorCreator mAnimatorCreator = null;
    }

    /**
     * 视图管理类。
     * 子类可以通过继承扩充配置，之后在 {@link #onCreateViewHolder()} 和 {@link #getViewHolder()} 创建和返回。
     */
    public static class ViewHolder {
        private ViewGroup mParent;
        private View mChild;

        private SparseArray<View> mViewCaches = null;

        public void setParent(@Nullable ViewGroup parent) {
            mParent = parent;
        }

        @NonNull
        public ViewGroup getParent() {
            return Utils.requireNonNull(mParent, "parent未创建");
        }

        @Nullable
        protected ViewGroup getParentOrNull() {
            return mParent;
        }

        public void setChild(@Nullable View child) {
            mChild = child;
        }

        @NonNull
        public View getChild() {
            return Utils.requireNonNull(mChild, "child未创建");
        }

        @Nullable
        protected View getChildOrNull() {
            return mChild;
        }

        @Nullable
        protected View getNoIdClickView() {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Nullable
        public final <V extends View> V findViewById(@IdRes int id) {
            if (mChild == null) {
                return null;
            }
            if (mViewCaches == null) {
                mViewCaches = new SparseArray<>();
            }
            View view = mViewCaches.get(id);
            if (view == null) {
                view = mChild.findViewById(id);
                if (view != null) {
                    mViewCaches.put(id, view);
                }
            }
            return (V) view;
        }
    }

    /**
     * 监听器管理类。
     * 子类可以通过继承扩充配置，之后在 {@link #onCreateListenerHolder()} 和 {@link #getListenerHolder()} 创建和返回。
     */
    protected static class ListenerHolder {
        private SparseArray<OnClickListener> mOnClickListeners = null;
        private SparseArray<OnLongClickListener> mOnLongClickListeners = null;
        private List<OnInitializeListener> mOnInitializeListeners = null;
        private List<OnBindDataListener> mOnBindDataListeners = null;
        private List<OnVisibleChangedListener> mOnVisibleChangedListeners = null;
        private List<OnShowListener> mOnShowListeners = null;
        private List<OnDismissListener> mOnDismissListeners = null;

        private void bindOnClickListeners(@NonNull final Layer layer) {
            if (mOnClickListeners == null) return;
            for (int i = 0; i < mOnClickListeners.size(); i++) {
                final int viewId = mOnClickListeners.keyAt(i);
                final OnClickListener listener = mOnClickListeners.valueAt(i);
                final View view;
                if (viewId == View.NO_ID) {
                    view = layer.getViewHolder().getNoIdClickView();
                } else {
                    view = layer.findViewById(viewId);
                }
                if (view != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(@NonNull View v) {
                            listener.onClick(layer, v);
                        }
                    });
                }
            }
        }

        private void bindOnLongClickListeners(@NonNull final Layer layer) {
            if (mOnLongClickListeners == null) return;
            for (int i = 0; i < mOnLongClickListeners.size(); i++) {
                final int viewId = mOnLongClickListeners.keyAt(i);
                final OnLongClickListener listener = mOnLongClickListeners.valueAt(i);
                final View view;
                if (viewId == View.NO_ID) {
                    view = layer.getViewHolder().getNoIdClickView();
                } else {
                    view = layer.findViewById(viewId);
                }
                if (view != null) {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return listener.onLongClick(layer, v);
                        }
                    });
                }
            }
        }

        public void addOnClickListener(@NonNull final OnClickListener listener, int... viewIds) {
            if (mOnClickListeners == null) {
                mOnClickListeners = new SparseArray<>();
            }
            if (viewIds != null && viewIds.length > 0) {
                for (int id : viewIds) {
                    mOnClickListeners.put(id, listener);
                }
            } else {
                mOnClickListeners.put(View.NO_ID, listener);
            }
        }

        public void addOnLongClickListener(@NonNull final OnLongClickListener listener, int... viewIds) {
            if (mOnLongClickListeners == null) {
                mOnLongClickListeners = new SparseArray<>();
            }
            if (viewIds != null && viewIds.length > 0) {
                for (int id : viewIds) {
                    mOnLongClickListeners.put(id, listener);
                }
            } else {
                mOnLongClickListeners.put(View.NO_ID, listener);
            }
        }

        private void addOnBindDataListener(@NonNull final OnBindDataListener onBindDataListener) {
            if (mOnBindDataListeners == null) {
                mOnBindDataListeners = new ArrayList<>(1);
            }
            mOnBindDataListeners.add(onBindDataListener);
        }

        private void addOnInitializeListener(@NonNull final OnInitializeListener onInitializeListener) {
            if (mOnInitializeListeners == null) {
                mOnInitializeListeners = new ArrayList<>(1);
            }
            mOnInitializeListeners.add(onInitializeListener);
        }

        private void addOnVisibleChangeListener(@NonNull final OnVisibleChangedListener onVisibleChangedListener) {
            if (mOnVisibleChangedListeners == null) {
                mOnVisibleChangedListeners = new ArrayList<>(1);
            }
            mOnVisibleChangedListeners.add(onVisibleChangedListener);
        }

        private void addOnShowListener(@NonNull final OnShowListener onShowListener) {
            if (mOnShowListeners == null) {
                mOnShowListeners = new ArrayList<>(1);
            }
            mOnShowListeners.add(onShowListener);
        }

        private void addOnDismissListener(@NonNull final OnDismissListener onDismissListener) {
            if (mOnDismissListeners == null) {
                mOnDismissListeners = new ArrayList<>(1);
            }
            mOnDismissListeners.add(onDismissListener);
        }

        private void notifyOnBindData(@NonNull final Layer layer) {
            if (mOnBindDataListeners != null) {
                for (OnBindDataListener onBindDataListener : mOnBindDataListeners) {
                    onBindDataListener.onBindData(layer);
                }
            }
        }

        private void notifyOnInitialize(@NonNull final Layer layer) {
            if (mOnInitializeListeners != null) {
                for (OnInitializeListener onInitializeListener : mOnInitializeListeners) {
                    onInitializeListener.onInitialize(layer);
                }
            }
        }

        private void notifyOnVisibleChangeToShow(@NonNull final Layer layer) {
            if (mOnVisibleChangedListeners != null) {
                for (OnVisibleChangedListener onVisibleChangedListener : mOnVisibleChangedListeners) {
                    onVisibleChangedListener.onShow(layer);
                }
            }
        }

        private void notifyOnVisibleChangeToDismiss(@NonNull final Layer layer) {
            if (mOnVisibleChangedListeners != null) {
                for (OnVisibleChangedListener onVisibleChangedListener : mOnVisibleChangedListeners) {
                    onVisibleChangedListener.onDismiss(layer);
                }
            }
        }

        private void notifyOnPreShow(@NonNull final Layer layer) {
            if (mOnShowListeners != null) {
                for (OnShowListener onShowListener : mOnShowListeners) {
                    onShowListener.onPreShow(layer);
                }
            }
        }

        private void notifyOnPostShow(@NonNull final Layer layer) {
            if (mOnShowListeners != null) {
                for (OnShowListener onShowListener : mOnShowListeners) {
                    onShowListener.onPostShow(layer);
                }
            }
        }

        private void notifyOnPreDismiss(@NonNull final Layer layer) {
            if (mOnDismissListeners != null) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onPreDismiss(layer);
                }
            }
        }

        private void notifyOnPostDismiss(@NonNull final Layer layer) {
            if (mOnDismissListeners != null) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onPostDismiss(layer);
                }
            }
        }
    }

    private class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            Layer.this.onGlobalLayout();
        }
    }

    private class OnGlobalPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        @Override
        public boolean onPreDraw() {
            return Layer.this.onGlobalPreDraw();
        }
    }

    private class OnViewKeyListener implements ViewManager.OnKeyListener {
        @Override
        public boolean onKey(int keyCode, KeyEvent keyEvent) {
            return Layer.this.onKeyEvent(keyEvent);
        }
    }

    private class OnInAnimEndCallback implements Runnable {
        @Override
        public void run() {
            Layer.this.handleInAnimEnd();
        }
    }

    private class OnOutAnimEndCallback implements Runnable {
        @Override
        public void run() {
            Layer.this.handleOutAnimEnd();
        }
    }

    /**
     * 动画创建器
     */
    public interface AnimatorCreator {
        /**
         * 进入动画
         *
         * @param target 目标View
         */
        @Nullable
        Animator createInAnimator(@NonNull View target);

        /**
         * 消失动画
         *
         * @param target 目标View
         */
        @Nullable
        Animator createOutAnimator(@NonNull View target);
    }

    /**
     * 首次加载的回调，只会在首次调用 show 方法后的 onCreate 时回调一次。
     */
    public interface OnInitializeListener {
        /**
         * 首次加载
         */
        void onInitialize(@NonNull Layer layer);
    }

    /**
     * 用于绑定数据，会在每次子视图添加到父容器的时候回调一次，即每次显示时都会对调。
     */
    public interface OnBindDataListener {
        /**
         * 绑定数据
         */
        void onBindData(@NonNull Layer layer);
    }

    /**
     * 点击事件监听器
     */
    public interface OnClickListener {
        /**
         * 点击事件回调
         */
        void onClick(@NonNull Layer layer, @NonNull View view);
    }

    /**
     * 长按事件监听器
     */
    public interface OnLongClickListener {
        /**
         * 长按事件回调
         */
        boolean onLongClick(@NonNull Layer layer, @NonNull View view);
    }

    /**
     * 隐藏事件监听器
     */
    public interface OnDismissListener {
        /**
         * 开始隐藏，动画刚开始执行
         */
        void onPreDismiss(@NonNull Layer layer);

        /**
         * 已隐藏，浮层已被移除
         */
        void onPostDismiss(@NonNull Layer layer);
    }

    /**
     * 显示事件监听器
     */
    public interface OnShowListener {
        /**
         * 开始显示，动画刚开始执行
         */
        void onPreShow(@NonNull Layer layer);

        /**
         * 已显示，浮层已显示且动画结束
         */
        void onPostShow(@NonNull Layer layer);
    }

    /**
     * 可见状态变化事件监听器
     */
    public interface OnVisibleChangedListener {
        /**
         * 浮层显示，刚被添加到父布局，进入动画未开始
         */
        void onShow(@NonNull Layer layer);

        /**
         * 浮层隐藏，已被从父布局移除，隐藏动画已结束
         */
        void onDismiss(@NonNull Layer layer);
    }
}
