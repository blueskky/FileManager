package com.whty.eschoolbag.filemanager.widgetview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.whty.eschoolbag.filemanager.R;

public class DeleteSearchDialog extends Dialog implements View.OnClickListener {


    private final LayoutInflater inflater;
    private TextView tvMessage;
    private CallBack callBack;

    public DeleteSearchDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        inflater = LayoutInflater.from(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.dialog, null);
        setContentView(view);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);

        setCanceledOnTouchOutside(true);
    }

    public void setMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            tvMessage.setText(msg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (callBack != null) {
                    callBack.onConfirmClick();
                }
                dismiss();
                break;

        }
    }


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public interface CallBack {
        void onConfirmClick();
    }
}
