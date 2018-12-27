package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldadapter.ChildAccountManagementEditorAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldmodel.ChildAccountManagementModel;
import com.linhuiba.linhuifield.fieldmodel.ReceiveAccountModel;
import com.linhuiba.linhuifield.fieldview.FieldMyListView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.CommonTool;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Administrator on 2016/7/20.
 */
public class ChildAccountManagementEditorActivity extends FieldBaseMvpActivity {
    private FieldMyListView mfieldchildaccount_dutyfield_listview;
    private TextView minvitation_dialog;
    private EditText mchildaccount_nameedit;
    private EditText mchildaccount_mobileedit;
    private EditText mchildaccount_emailedit;
    private ChildAccountManagementEditorAdapter editorAdapter;
    private ChildAccountManagementModel getintentdata;
    private int type;
    private final int AddInt = 0;
    private final int EditorInt = 1;
    private ArrayList<Integer> checklistdata = new ArrayList<>();
    private ArrayList<HashMap<String,String>> resListmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childaccountmanagementeditor);
        mfieldchildaccount_dutyfield_listview = (FieldMyListView)findViewById(R.id.fieldchildaccount_dutyfield_listview);
        minvitation_dialog = (TextView)findViewById(R.id.invitation_dialog);
        mchildaccount_nameedit = (EditText)findViewById(R.id.childaccount_nameedit);
        mchildaccount_mobileedit = (EditText)findViewById(R.id.childaccount_mobileedit);
        mchildaccount_emailedit = (EditText)findViewById(R.id.childaccount_emailedit);
        sendinvitationclick();
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.txt_childaccount_titletxt));
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.myselfinfo_save_pw), 14, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < resListmap.size(); i++) {
                    if (ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().get(resListmap.get(i).get("id").toString())) {
                        checklistdata.add(Integer.parseInt(resListmap.get(i).get("id").toString()));
                    }
                }
                if (type == AddInt) {
                    if (mchildaccount_nameedit.getText().toString().length() == 0) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.applyforrelease_contect_hinttxt));
                        return;
                    }
                    if (mchildaccount_mobileedit.getText().toString().length() == 0) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_mobile));
                        return;
                    }
                    if (!CommonTool.isMobileNO(mchildaccount_mobileedit.getText().toString())) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                        return;
                    }

                    if (mchildaccount_emailedit.getText().toString().length() > 0) {
                        if (!CommonTool.isEmail(mchildaccount_emailedit.getText().toString())) {
                            BaseMessageUtils.showToast(ChildAccountManagementEditorActivity.this, getResources().getString(R.string.applyforrelease_email_errortxt));
                            return;
                        }
                    }
                    if (checklistdata == null || checklistdata.size() == 0) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_childaccount_dutyfieldtxt_choose_remind));
                        return;
                    }
                    Field_FieldApi.addmessageuser(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), addmessageuserHandler,
                            mchildaccount_mobileedit.getText().toString(), mchildaccount_nameedit.getText().toString(),
                            mchildaccount_emailedit.getText().toString(), JSON.toJSONString(checklistdata,true));
                } else if (type == EditorInt) {
                    if (mchildaccount_nameedit.getText().toString().length() == 0) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.applyforrelease_contect_hinttxt));
                        return;
                    }
                    if (mchildaccount_mobileedit.getText().toString().length() == 0) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_mobile));
                        return;
                    }
                    if (mchildaccount_emailedit.getText().toString().length() > 0) {
                        if (!CommonTool.isEmail(mchildaccount_emailedit.getText().toString())) {
                            BaseMessageUtils.showToast(ChildAccountManagementEditorActivity.this, getResources().getString(R.string.applyforrelease_email_errortxt));
                            return;
                        }
                    }
                    if (checklistdata == null || checklistdata.size() == 0) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_childaccount_dutyfieldtxt_choose_remind));
                        return;
                    }
                    if (getintentdata.getId() != null && getintentdata.getId().length() > 0) {
                        Field_FieldApi.editormessageuser(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), editormessageuserHandler, getintentdata.getId(),
                                mchildaccount_mobileedit.getText().toString(), mchildaccount_nameedit.getText().toString(),
                                mchildaccount_emailedit.getText().toString(), JSON.parseArray(JSON.toJSONString(checklistdata, true)));
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
                        ChildAccountManagementEditorActivity.this.finish();
                    }
                }
            }
        });
        initview();
    }
    private void initview() {
        Intent childaccount = getIntent();
        if (childaccount.getExtras() != null) {
            if (childaccount.getExtras().get("type") != null) {
                type = childaccount.getExtras().getInt("type");
                if (childaccount.getExtras().get("resListmap") != null) {
                    resListmap = (ArrayList<HashMap<String, String>>) childaccount.getSerializableExtra("resListmap");
                }
                if (type == EditorInt) {
                    if (childaccount.getExtras() != null) {
                        if (childaccount.getExtras().get("itemdata") != null) {
                            getintentdata = (ChildAccountManagementModel) childaccount.getSerializableExtra("itemdata");
                            mchildaccount_nameedit.setText(getintentdata.getName());
                            mchildaccount_mobileedit.setText(getintentdata.getMobile());
                        }
                    }

                }
            }
        }
        if (resListmap != null) {
            if (type == AddInt) {
                for (int i = 0; i < resListmap.size(); i++) {
                    ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(resListmap.get(i).get("id").toString(),false);
                }
            } else if (type == EditorInt) {
                for (int i = 0; i < resListmap.size(); i++) {
                    for (int j = 0; j < getintentdata.getRes_ids().size(); j++) {
                        if (getintentdata.getRes_ids().get(j).get("res_id").toString().equals(resListmap.get(i).get("id").toString())) {
                            ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(resListmap.get(i).get("id").toString(),true);
                            break;
                        } else {
                            if (j == getintentdata.getRes_ids().size()-1) {
                                ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(resListmap.get(i).get("id").toString(),false);
                            }
                        }
                    }
                }
            }
            ArrayList<ReceiveAccountModel> list = new ArrayList<>();
            for (int i = 0; i < resListmap.size(); i++) {
                ReceiveAccountModel receiveAccountModel = new ReceiveAccountModel();
                receiveAccountModel.setId(Integer.parseInt(resListmap.get(i).get("id")));
                receiveAccountModel.setAccount(resListmap.get(i).get("name"));
                list.add(receiveAccountModel);
            }
            editorAdapter = new ChildAccountManagementEditorAdapter(this,this,list,0);
            mfieldchildaccount_dutyfield_listview.setAdapter(editorAdapter);
        }
    }
    private void sendinvitationclick() {

    }
    private LinhuiAsyncHttpResponseHandler addmessageuserHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            minvitation_dialog.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    minvitation_dialog.setVisibility(View.GONE);
                }
            }, 500);
            ChildAccountManagementEditorActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode,okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());

            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler editormessageuserHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            BaseMessageUtils.showToast(getResources().getString(R.string.pw_modify_success_text));
            ChildAccountManagementEditorActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());

            checkAccess(error);
        }
    };
}
