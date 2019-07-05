package com.whty.eschoolbag.filemanager.widgetview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.whty.eschoolbag.filemanager.R;

public class OperationDialog extends Dialog implements View.OnClickListener {


    public MenuClickListener clickListener;
    private final LayoutInflater inflater;
    private boolean favorite;

    public OperationDialog(Context context) {
        super(context,R.style.Dialog);
        inflater = LayoutInflater.from(context);
    }


    public OperationDialog(Context context,boolean favorite) {
        super(context,R.style.Dialog);
        this.favorite=favorite;
        inflater = LayoutInflater.from(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View conentView = inflater.inflate(R.layout.dialog_operation, null);
        this.setContentView(conentView);

        TextView tvFavourit = (TextView) findViewById(R.id.tv_favourit);

        conentView.findViewById(R.id.tv_favourit).setOnClickListener(this);
        conentView.findViewById(R.id.tv_detail).setOnClickListener(this);

        if(favorite){
            tvFavourit.setText("取消收藏");
        }else{
            tvFavourit.setText("收藏");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_favourit:
                if (clickListener != null) {
                    clickListener.onFavouritClick();
                }
                break;

            case R.id.tv_detail:
                if (clickListener != null) {
                    clickListener.onDetailClick();
                }
                break;
        }
        dismiss();
    }


    public void setClickListener(MenuClickListener clickListener) {
        this.clickListener = clickListener;

    }

    public interface MenuClickListener {
        void onFavouritClick();

        void onDetailClick();
    }
}
