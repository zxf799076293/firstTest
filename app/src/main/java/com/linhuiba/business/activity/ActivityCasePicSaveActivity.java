package com.linhuiba.business.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.ActivityCasePicSaveAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.util.TitleBarUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ActivityCasePicSaveActivity extends BaseMvpActivity {
    @InjectView(R.id.case_pic_rv)
    RecyclerView mRecyclerView;
    private ArrayList<String> mPicList;
    private ActivityCasePicSaveAdapter mActivityCasePicSaveAdapter;
    private TextView mChooseTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_activity_case_pic_save);
        ButterKnife.inject(this);
        initView();
    }
    private void initView() {
        mChooseTV = (TextView) findViewById(R.id.action_text_top_other);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_case_pic_save_title));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.showRegisterText(this, getResources().getString(R.string.commoditypay_checkall_text),
                16,
                getResources().getColor(R.color.default_bluebg), new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        //2019/1/10 全选
                        ActivityCasePicSaveAdapter.cleanIsChecked();
                        if (mChooseTV.getText().toString().equals(getResources().getString(R.string.commoditypay_checkall_text))) {
                            for (int i = 0; i < mPicList.size(); i++) {
                                ActivityCasePicSaveAdapter.getIsChecked().put(i,true);
                            }

                        } else if (mChooseTV.getText().toString().equals(getResources().getString(R.string.module_case_pic_unchoosed))) {
                            for (int i = 0; i < mPicList.size(); i++) {
                                ActivityCasePicSaveAdapter.getIsChecked().put(i,false);
                            }

                        }
                        mActivityCasePicSaveAdapter.notifyDataSetChanged();
                        setSelectAllStatus();
                    }
                });
        mChooseTV.setText(getResources().getString(R.string.commoditypay_checkall_text));
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("pic_list") != null) {
            mPicList = (ArrayList<String>) intent.getExtras().get("pic_list");
            GridLayoutManager gridLayoutManager = new GridLayoutManager(ActivityCasePicSaveActivity.this,4);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            for (int i = 0; i < mPicList.size(); i++) {
                ActivityCasePicSaveAdapter.getIsChecked().put(i,false);
            }
            mActivityCasePicSaveAdapter = new ActivityCasePicSaveAdapter(R.layout.module_recycle_item_activity_case_pic_save,
                    mPicList,ActivityCasePicSaveActivity.this,ActivityCasePicSaveActivity.this);
            mRecyclerView.setAdapter(mActivityCasePicSaveAdapter);
        } else {
            MessageUtils.showToast(getResources().getString(R.string.review_error_text));
        }
    }
    @OnClick({
            R.id.activity_case_pic_save_btn
    })
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.activity_case_pic_save_btn:
                //2019/1/10 保存到相册
                boolean isChoosed = false;
                for (int i = 0; i < mPicList.size(); i++) {
                    if (ActivityCasePicSaveAdapter.getIsChecked().get(i)) {
                        isChoosed = true;
                        break;
                    }
                }
                if (!isChoosed) {
                    MessageUtils.showToast(getResources().getString(R.string.module_case_pic_choose_remind));
                    return;
                }
                AndPermission.with(ActivityCasePicSaveActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();

                break;
            default:
                break;
        }
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                //2019/1/10 保存图片到相册
                showProgressDialog();
                new Thread(){
                    public void run() {
                        for (int i = 0; i < mPicList.size(); i++) {
                            if (mPicList.get(i) != null &&
                                    mPicList.get(i).length() > 0 &&
                                    ActivityCasePicSaveAdapter.getIsChecked().get(i)) {
                                com.linhuiba.linhuifield.connector.Constants.saveToSystemGallery(ActivityCasePicSaveActivity.this,
                                        mPicList.get(i).toString());
                            }
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }.start();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    hideProgressDialog();
                    MessageUtils.showToast(getResources().getString(R.string.save_success_prompt));
                    break;
                default:
                    break;
            }
        }

    };
    public void setSelectAllStatus() {
        int choosedSize = 0;
        for (int i = 0; i < mPicList.size(); i++) {
            if (ActivityCasePicSaveAdapter.getIsChecked().get(i)) {
                choosedSize ++;
                break;
            }
        }
        if (choosedSize == mPicList.size()) {
            mChooseTV.setText(getResources().getString(R.string.module_case_pic_unchoosed));
        } else {
            mChooseTV.setText(getResources().getString(R.string.commoditypay_checkall_text));
        }
    }
}
