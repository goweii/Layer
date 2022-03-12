package per.goweii.layer.design.material;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.dialog.DialogLayer;

public class MaterialDialogLayer extends DialogLayer {
    public MaterialDialogLayer(@NonNull Context context) {
        super(context);
    }

    public MaterialDialogLayer(@NonNull Activity activity) {
        super(activity);
        setContentView(R.layout.layer_design_material_dialog);
        setBackgroundDimDefault();
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
            viewHolder.getActions().setVisibility(View.GONE);
        } else {
            viewHolder.getActions().setVisibility(View.VISIBLE);
            LayoutInflater inflater = getLayoutInflater();
            for (int i = 0; i < config.mActions.size(); i++) {
                final Action action = config.mActions.get(i);
                TextView textView = onCreateAction(inflater, viewHolder.getActions());
                textView.setText(action.mName);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        action.mOnClickListener.onClick(MaterialDialogLayer.this, v);
                    }
                });
                viewHolder.getActions().addView(textView);
            }
        }
    }

    protected TextView onCreateAction(@NonNull LayoutInflater inflater, @NonNull LinearLayout parent) {
        return (TextView) inflater.inflate(R.layout.layer_design_material_dialog_action, parent, false);
    }

    public MaterialDialogLayer addAction(@NonNull String actionName, @NonNull OnClickListener onClickListener) {
        getConfig().mActions.add(new Action(actionName, onClickListener));
        return this;
    }

    public MaterialDialogLayer addAction(@StringRes int actionName, @NonNull OnClickListener onClickListener) {
        return addAction(getActivity().getString(actionName), onClickListener);
    }

    public MaterialDialogLayer setTitle(@NonNull String title) {
        getConfig().mTitle = title;
        return this;
    }

    public MaterialDialogLayer setTitle(@StringRes int title) {
        return setTitle(getActivity().getString(title));
    }

    public MaterialDialogLayer setDesc(@NonNull String desc) {
        getConfig().mDesc = desc;
        return this;
    }

    public MaterialDialogLayer setDesc(@StringRes int desc) {
        return setDesc(getActivity().getString(desc));
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
    }

    public static class ViewHolder extends DialogLayer.ViewHolder {
        public TextView getTitle() {
            return getContent().findViewById(R.id.layer_design_material_dialog_title);
        }

        public TextView getDesc() {
            return getContent().findViewById(R.id.layer_design_material_dialog_desc);
        }

        public LinearLayout getActions() {
            return getContent().findViewById(R.id.layer_design_material_dialog_actions);
        }
    }

    public static class ListenerHolder extends DialogLayer.ListenerHolder {
    }
}
