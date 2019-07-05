package com.whty.eschoolbag.filemanager.ui.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.adapter.HistoryTagAdapter;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.presenter.LoadPresenter;
import com.whty.eschoolbag.filemanager.util.PreferencesUtil;
import com.whty.eschoolbag.filemanager.widgetview.DeleteSearchDialog;
import com.whty.eschoolbag.filemanager.widgetview.flowlayout.FlowLayout;
import com.whty.eschoolbag.filemanager.widgetview.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements LoadPresenter.OnLoadCallBack {


    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;
    @BindView(R.id.btn_search)
    Button btnSearch;


    private DeleteSearchDialog dialog;
    private LoadPresenter loadPresenter;
    private FileExploreFragment exploreFragment;
    private String lastContent;
    private Context mContext;
    private HistoryTagAdapter tagAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mContext = this;


        initHistoryLayout();

        loadPresenter = new LoadPresenter(this);
    }

    private void initHistoryLayout() {
        ArrayList<String> historyList = PreferencesUtil.getInstance(this).getHistoryList();
        tagAdapter = new HistoryTagAdapter(this, historyList);
        flowlayout.setAdapter(tagAdapter);

        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(SearchActivity.this, historyList.get(position), Toast.LENGTH_SHORT).show();
                //view.setVisibility(View.GONE);
                String tag = historyList.get(position);
                etContent.setText(tag);
                btnSearch.performClick();
                return true;
            }
        });

    }


//    TagAdapter adapter = new TagAdapter<String>(historyList) {
//
//        @Override
//        public View getView(FlowLayout parent, int position, String s) {
//            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.tv, flowlayout, false);
//            TextView tvTag = (TextView) view.findViewById(R.id.tv_tag);
//            tvTag.setText(s);
//            return view;
//        }
//    };


    @OnClick({R.id.iv_back, R.id.iv_delete, R.id.btn_search,R.id.iv_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_del:
                etContent.setText("");
                break;
            case R.id.iv_delete:
                ArrayList<String> historyList = PreferencesUtil.getInstance(this).getHistoryList();
                if(historyList!=null&&historyList.size()>0){
                    showDeleteDialog();
                }

                break;

            case R.id.btn_search:
                String currentContent = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(currentContent)) {
                    return;
                }

                if (currentContent.equals(lastContent)) {
                    return;
                }
                lastContent = currentContent;

                loadPresenter.searchFile(this, currentContent);
                PreferencesUtil.getInstance(this).addItem(currentContent);

                break;
        }
    }

    private void showDeleteDialog() {
        if (dialog == null) {
            dialog = new DeleteSearchDialog(this);
        }
        dialog.show();
        dialog.setCallBack(new DeleteSearchDialog.CallBack() {
            @Override
            public void onConfirmClick() {
                PreferencesUtil.getInstance(mContext).removeAll();
                initHistoryLayout();
            }
        });
    }


    @Override
    public void onLoadFinish(ArrayList<FileInfo> list) {
        llHistory.setVisibility(View.GONE);

        if (exploreFragment != null && exploreFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(exploreFragment).commit();
        }
        exploreFragment = new FileExploreFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", list);
        bundle.putString("type", FileExploreFragment.TYPE_SEARCH);
        exploreFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, exploreFragment).commit();
    }


    private void onBack() {
        if (exploreFragment != null) {
            exploreFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBack();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }
}
