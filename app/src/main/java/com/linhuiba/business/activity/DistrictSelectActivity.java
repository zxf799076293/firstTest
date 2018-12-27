package com.linhuiba.business.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldChooseResOptionsActivity;
import com.linhuiba.linhuifield.fieldmodel.AddfieldCommunityCategoriesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by Administrator on 2016/8/13.
 */
public class DistrictSelectActivity {
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
//    所有市
    protected String[] mcityDatas;
    private String[] DistrictNames;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    private Map<String, String> mCitisDatasidMap = new HashMap<String, String>();
    private Map<String, String> mDistrictDatasidMap = new HashMap<String, String>();
    public Map<Integer, String> mCitisDeliveryFeeMap = new HashMap<Integer, String>();//快递费

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";
    protected String mCurrentCityCode ="";
    protected String mCurrentProviceCode ="";
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private int type;
    private InvoiceInfoEditorAddress invoiceInfoEditorAddress;
    private EnterpriseCertificationActivity enterpriseCertificationActivity;
    private InvoiceInformationActivity invoiceInfoEntryActivity;
    private InvoiceTitleEditActivity mInvoiceTitleEditActivity;
    public DistrictSelectActivity() {

    }
    public DistrictSelectActivity(final InvoiceInfoEditorAddress activity, final View textEntryView, final Dialog dialog,int type) {
        this.invoiceInfoEditorAddress = activity;
        this.type = type;
        show_WheelViewDialog(invoiceInfoEditorAddress,textEntryView,dialog);
        //2017/12/8 选择指定的地址
        if (LoginManager.getInstance().getTrackProvinceName().length() > 0 &&
                LoginManager.getInstance().getTrackCityName().length() > 0) {
            int mProvinceDatasInt = 0;
            int mcityDatasInt = 0;
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].equals(LoginManager.getInstance().getTrackProvinceName())) {
                    mProvinceDatasInt = i;
                    break;
                }
            }
            mcityDatas = mCitisDatasMap.get(LoginManager.getInstance().getTrackProvinceName());
            for (int i = 0; i < mcityDatas.length; i++) {
                if (mcityDatas[i].equals(LoginManager.getInstance().getTrackCityName())) {
                    mcityDatasInt = i;
                    break;
                }
            }
            mViewProvince.setCurrentItem(mProvinceDatasInt);
            mViewCity.setCurrentItem(mcityDatasInt);
            mViewDistrict.setCurrentItem(1);
            mViewDistrict.setCurrentItem(0);
        }
    }
    public DistrictSelectActivity(final InvoiceInfoEditorAddress activity,int type) {
        this.invoiceInfoEditorAddress = activity;
        this.type = type;
        //2017/12/8 选择指定的地址
        if (Constants.getInstance().mProvinceDatas_tmp != null  &&
                Constants.getInstance().mCitisDatasMap_tmp != null &&Constants.getInstance().mDistrictDatasMap_tmp != null &&
                Constants.getInstance().mCitisDatasidMap_tmp != null && Constants.getInstance().mDistrictDatasidMap_tmp != null &&
                Constants.getInstance().mZipcodeDatasMap_tmp != null) {
            if (Constants.getInstance().mProvinceDatas_tmp.length > 0 &&
                    Constants.getInstance().mCitisDatasMap_tmp.size() > 0 && Constants.getInstance().mDistrictDatasMap_tmp.size() > 0 && Constants.getInstance().mCitisDatasidMap.size() > 0 &&
                    Constants.getInstance().mDistrictDatasidMap_tmp.size() > 0 && Constants.getInstance().mDistrictDatasidMap_tmp.size() > 0) {
                mProvinceDatas = Constants.getInstance().mProvinceDatas_tmp;
                mCitisDatasMap = Constants.getInstance().mCitisDatasMap_tmp;
                mDistrictDatasMap = Constants.getInstance().mDistrictDatasMap_tmp;
                mCitisDatasidMap = Constants.getInstance().mCitisDatasidMap_tmp;
                mDistrictDatasidMap = Constants.getInstance().mDistrictDatasidMap_tmp;
                mZipcodeDatasMap = Constants.getInstance().mZipcodeDatasMap_tmp;
                mCurrentProviceName = mProvinceDatas[0];
                mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[0];
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
                mCurrentZipCode=mZipcodeDatasMap.get(mCurrentDistrictName);
                mCurrentCityCode = mDistrictDatasidMap.get(mCurrentCityName);
                mCurrentProviceCode = mCitisDatasidMap.get(mCurrentProviceName);
            } else {
                getdistrictconfig(false,activity);
            }
        } else {
            getdistrictconfig(false,activity);
        }
        if (LoginManager.getInstance().getTrackProvinceName().length() > 0 &&
                LoginManager.getInstance().getTrackCityName().length() > 0) {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].equals(LoginManager.getInstance().getTrackProvinceName())) {
                    mCurrentProviceName  = LoginManager.getInstance().getTrackProvinceName();
                    break;
                }
            }
            mcityDatas = mCitisDatasMap.get(LoginManager.getInstance().getTrackProvinceName());
            for (int i = 0; i < mcityDatas.length; i++) {
                if (mcityDatas[i].equals(LoginManager.getInstance().getTrackCityName())) {
                    mCurrentCityName  = LoginManager.getInstance().getTrackCityName();
                    break;
                }
            }
            DistrictNames = mDistrictDatasMap.get(LoginManager.getInstance().getTrackCityName());
            mCurrentDistrictName = DistrictNames[0];
            mCurrentZipCode=mZipcodeDatasMap.get(mCurrentDistrictName);
            mCurrentCityCode = mDistrictDatasidMap.get(mCurrentCityName);
            mCurrentProviceCode = mCitisDatasidMap.get(mCurrentProviceName);
            invoiceInfoEditorAddress.province_id = mCurrentProviceCode;
            invoiceInfoEditorAddress.city_id = mCurrentCityCode;
            invoiceInfoEditorAddress.district_id = mCurrentZipCode;
            invoiceInfoEditorAddress.provinceStr = mCurrentProviceName;
            invoiceInfoEditorAddress.cityStr = mCurrentCityName;
            invoiceInfoEditorAddress.districtStr = mCurrentDistrictName;
            invoiceInfoEditorAddress.mareatxt.setText(mCurrentProviceName+mCurrentCityName + mCurrentDistrictName);
        }
    }

    public DistrictSelectActivity(final EnterpriseCertificationActivity activity, final View textEntryView, final Dialog dialog,int type) {
        this.enterpriseCertificationActivity = activity;
        this.type = type;
        show_WheelViewDialog(enterpriseCertificationActivity,textEntryView,dialog);
    }
    public DistrictSelectActivity(final InvoiceInformationActivity activity, final View textEntryView, final Dialog dialog,int type) {
        this.invoiceInfoEntryActivity = activity;
        this.type = type;
        show_WheelViewDialog(invoiceInfoEntryActivity,textEntryView,dialog);
    }
    public DistrictSelectActivity(final InvoiceTitleEditActivity activity, final View textEntryView, final Dialog dialog,int type) {
        this.mInvoiceTitleEditActivity = activity;
        this.type = type;
        show_WheelViewDialog(mInvoiceTitleEditActivity,textEntryView,dialog);
    }

    private void show_WheelViewDialog(final Activity activity, final View textEntryView, final Dialog dialog) {
        if (dialog == null || !dialog.isShowing()) {
            mViewProvince= (WheelView) textEntryView.findViewById(R.id.id_province);
            mViewCity= (WheelView) textEntryView.findViewById(R.id.id_city);
            mViewDistrict = (WheelView) textEntryView.findViewById(R.id.id_district);
            TextView mBtnConfirm= (TextView) textEntryView.findViewById(R.id.btn_confirm);
            // 添加change事件
            mViewProvince.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    if (wheel == mViewProvince) {
                        updateCities(activity);
                    } else if (wheel == mViewCity) {
                        updateAreas(activity);
                    } else if (wheel == mViewDistrict) {
                        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
                    }
                }
            });
            // 添加change事件
            mViewCity.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    if (wheel == mViewProvince) {
                        updateCities(activity);
                    } else if (wheel == mViewCity) {
                        updateAreas(activity);
                    } else if (wheel == mViewDistrict) {
                        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
                    }
                }
            });
            // 添加change事件
            mViewDistrict.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    if (wheel == mViewProvince) {
                        updateCities(activity);
                    } else if (wheel == mViewCity) {
                        updateAreas(activity);
                    } else if (wheel == mViewDistrict) {
                        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
                    }
                }
            });
            // 添加onclick事件
            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrentProviceName.contains("香港") ||
                            mCurrentProviceName.contains("澳门") ||
                            mCurrentProviceName.contains("台湾")) {
                        MessageUtils.showToast("暂未开放");
                    } else {
                        showSelectedResult(type,dialog);
                    }
                }
            });
            if (Constants.getInstance().mProvinceDatas_tmp != null  &&
                    Constants.getInstance().mCitisDatasMap_tmp != null &&Constants.getInstance().mDistrictDatasMap_tmp != null &&
                    Constants.getInstance().mCitisDatasidMap_tmp != null && Constants.getInstance().mDistrictDatasidMap_tmp != null &&
                    Constants.getInstance().mZipcodeDatasMap_tmp != null) {
                if (Constants.getInstance().mProvinceDatas_tmp.length > 0 &&
                        Constants.getInstance().mCitisDatasMap_tmp.size() > 0 && Constants.getInstance().mDistrictDatasMap_tmp.size() > 0 && Constants.getInstance().mCitisDatasidMap.size() > 0 &&
                        Constants.getInstance().mDistrictDatasidMap_tmp.size() > 0 && Constants.getInstance().mDistrictDatasidMap_tmp.size() > 0) {
                    mProvinceDatas = Constants.getInstance().mProvinceDatas_tmp;
                    mCitisDatasMap = Constants.getInstance().mCitisDatasMap_tmp;
                    mDistrictDatasMap = Constants.getInstance().mDistrictDatasMap_tmp;
                    mCitisDatasidMap = Constants.getInstance().mCitisDatasidMap_tmp;
                    mDistrictDatasidMap = Constants.getInstance().mDistrictDatasidMap_tmp;
                    mCitisDeliveryFeeMap = Constants.getInstance().mCitisDeliveryFeeMapTmp;
                    mZipcodeDatasMap = Constants.getInstance().mZipcodeDatasMap_tmp;
                    mCurrentProviceName = mProvinceDatas[0];
                    mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[0];
                    mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
                    mCurrentZipCode=mZipcodeDatasMap.get(mCurrentDistrictName);
                    mCurrentCityCode = mDistrictDatasidMap.get(mCurrentCityName);
                    mCurrentProviceCode = mCitisDatasidMap.get(mCurrentProviceName);
                } else {
                    getdistrictconfig(false,activity);
                }
            } else {
                getdistrictconfig(false,activity);
            }
            mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(activity, mProvinceDatas));
            // 设置可见条目数量
            mViewProvince.setVisibleItems(11);
            mViewCity.setVisibleItems(11);
            mViewDistrict.setVisibleItems(11);
            updateCities(activity);

            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(textEntryView);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            dialog.getWindow().setAttributes(lp);
            textEntryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        textEntryView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas(Activity activity) {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        mCurrentCityCode = mDistrictDatasidMap.get(mCurrentCityName);
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(activity, areas));
        mViewDistrict.setCurrentItem(0);
        if (areas.length > 0) {
            mCurrentDistrictName = areas[0];
        } else {
            mCurrentDistrictName = "";
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities(Activity activity) {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        mCurrentProviceCode = mCitisDatasidMap.get(mCurrentProviceName);
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        if (cities.length > 0) {
            mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(activity, cities));
            mViewCity.setCurrentItem(0);
            mCurrentCityName = cities[0];
            updateAreas(activity);
        } else {
            mCurrentCityName = "";
            mCurrentDistrictName = "";
            String[] citie = new String[] { "" };
            mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(activity, citie));
            mViewCity.setCurrentItem(0);
            String[] areas = new String[] { "" };
            mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(activity, areas));
            mViewDistrict.setCurrentItem(0);
        }

    }

    private void showSelectedResult(int type,Dialog dialog) {
        if (type == 1) {
            invoiceInfoEditorAddress.province_id = mCurrentProviceCode;
            invoiceInfoEditorAddress.city_id = mCurrentCityCode;
            invoiceInfoEditorAddress.district_id = mCurrentZipCode;
            invoiceInfoEditorAddress.provinceStr = mCurrentProviceName;
            invoiceInfoEditorAddress.cityStr = mCurrentCityName;
            invoiceInfoEditorAddress.districtStr = mCurrentDistrictName;
            invoiceInfoEditorAddress.mareatxt.setText(mCurrentProviceName+mCurrentCityName + mCurrentDistrictName);
        } else if (type == 2) {
            enterpriseCertificationActivity.province_id = mCurrentProviceCode;
            enterpriseCertificationActivity.city_id = mCurrentCityCode;
            enterpriseCertificationActivity.district_id = mCurrentZipCode;
            enterpriseCertificationActivity.mregion_textview.setText(mCurrentProviceName+mCurrentCityName + mCurrentDistrictName);
        } else if (type == 3) {
            invoiceInfoEntryActivity.province_id = mCurrentProviceCode;
            invoiceInfoEntryActivity.city_id = mCurrentCityCode;
            invoiceInfoEntryActivity.district_id = mCurrentZipCode;
            invoiceInfoEntryActivity.mInvoiceCompanyEareTV.setText(mCurrentProviceName+mCurrentCityName + mCurrentDistrictName);
        } else if (type == 4) {
            mInvoiceTitleEditActivity.province_id = mCurrentProviceCode;
            mInvoiceTitleEditActivity.city_id = mCurrentCityCode;
            mInvoiceTitleEditActivity.district_id = mCurrentZipCode;
            mInvoiceTitleEditActivity.mInvoiceCompanyEareTV.setText(mCurrentProviceName+mCurrentCityName + mCurrentDistrictName);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    private void getdistrictconfig(boolean booleanintent,Activity activity) {
        //2018/4/13 城市列表请求接口
        String districtstr = com.linhuiba.linhuifield.connector.Constants.readAssetsFileString(activity,"district").trim();
        JSONArray ProvinceArray = JSON.parseArray(districtstr);
        mProvinceDatas = new String[ProvinceArray.size()];
        for (int i = 0; i < ProvinceArray.size(); i++) {
            com.alibaba.fastjson.JSONObject Provincejsonobject = ProvinceArray.getJSONObject(i);
            mProvinceDatas[i] = Provincejsonobject.getString("name");
            com.alibaba.fastjson.JSONArray Cityarray = JSON.parseArray(Provincejsonobject.getString("city"));
            mcityDatas = new String[Cityarray.size()];
            for (int j = 0; j < Cityarray.size(); j++) {
                com.alibaba.fastjson.JSONObject Cityjsonobject = Cityarray.getJSONObject(j);
                mcityDatas[j] = Cityjsonobject.getString("name");
                com.alibaba.fastjson.JSONArray Districtarray = JSON.parseArray(Cityjsonobject.getString("district"));
                DistrictNames = new String[Districtarray.size()];
                for (int k = 0; k < Districtarray.size(); k++) {
                    com.alibaba.fastjson.JSONObject Districtjsonobject = Districtarray.getJSONObject(k);
                    DistrictNames[k] = Districtjsonobject.getString("name");
                    mZipcodeDatasMap.put(Districtjsonobject.getString("name"),Districtjsonobject.getString("id"));
                }
                mDistrictDatasidMap.put(Cityjsonobject.getString("name"),Cityjsonobject.getString("id"));
                mDistrictDatasMap.put(Cityjsonobject.getString("name"),DistrictNames);
            }
            mCitisDatasidMap.put(Provincejsonobject.getString("name"),Provincejsonobject.getString("id"));
            mCitisDatasMap.put(Provincejsonobject.getString("name"),mcityDatas);
        }
    }
}
