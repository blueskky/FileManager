package com.whty.eschoolbag.filemanager.widgetview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.whty.eschoolbag.filemanager.R;

public class PopMenu extends PopupWindow implements View.OnClickListener {

    private MenuClickListener listener;

    public PopMenu(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.lost_pop_menu, null);
        this.setContentView(conentView);

        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);


        conentView.findViewById(R.id.rl_clean).setOnClickListener(this);
        conentView.findViewById(R.id.rl_setting).setOnClickListener(this);

    }


    public void setOnMenuClickListener(MenuClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_clean:
                if (listener != null) {
                    listener.onCleanClick();
                }
                break;

            case R.id.rl_setting:
                if (listener != null) {
                    listener.onSettingClick();
                }
                break;
        }
        dismiss();
    }


    public interface MenuClickListener {
        void onCleanClick();

        void onSettingClick();
    }

}
