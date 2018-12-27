package com.linhuiba.business.fieldcallback;

import com.linhuiba.business.model.AddressContactModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Field_MyAllCallBack {
    private ArrayList<String> path;
    private int position;
    private String getconfigurate_errormsg;
    private boolean check;
    private String price;
    private String subsidy_fee;
    private String orderpaylistsize;
    private AddressContactModel addressContactModel;
    private int state;
    public Field_MyAllCallBack(ArrayList<String> picturepath) {
        this.path = picturepath;
    }
    public Field_MyAllCallBack(int item) {
        this.position = item;
    }
    public Field_MyAllCallBack() {

    }
    public Field_MyAllCallBack(String msg) {
        this.getconfigurate_errormsg = msg;
    }
    public Field_MyAllCallBack(int type,String str) {
        this.getconfigurate_errormsg = str;
        this.position = type;
    }
    public Field_MyAllCallBack(Boolean check,String price,String subsidy_fee,String orderpaylistsize) {
        this.check = check;
        this.price = price;
        this.subsidy_fee = subsidy_fee;
        this.orderpaylistsize = orderpaylistsize;
    }
    public Field_MyAllCallBack(AddressContactModel addressContactModel,int position) {
        this.addressContactModel = addressContactModel;
        this.position = position;
    }
    public Field_MyAllCallBack(int state,int position,String str) {
        this.getconfigurate_errormsg = str;
        this.state = state;
        this.position = position;
    }
    public Field_MyAllCallBack(ArrayList<String> path,int type) {
        this.path = path;
        this.position = type;
    }

    public void MyChoosePicture(Field_AddFieldChoosePictureCallBack.AdFieldChoosePictureCall callBack) {
        callBack.addfieldchoosepicture(path);
    }
    public void MyDeleteChoosePicture(Field_AddFieldChoosePictureCallBack.AdField_deleteChoosePictureCall callBack) {
        callBack.addfield_deletechoosepicture(position);
    }
    public void MyshowPreviewPicture(Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall callBack) {
        callBack.AdField_showPreviewImg(position);
    }
    public void Mygetconfigurate(Field_AddFieldChoosePictureCallBack.GetconfigurateCall callBack) {
        callBack.AdField_showPreviewImg(getconfigurate_errormsg);
    }
    public void CalendarClicklisten(Field_AddFieldChoosePictureCallBack.CalendarClickCall callBack) {
        callBack.CalendarClick(position);
    }
    public void getfieldsize_pricenuit(Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall callBack) {
        callBack.getfieldsize_pricenuit(position, getconfigurate_errormsg);
    }
    public void AddfieldFouritemcall(Field_AddFieldChoosePictureCallBack.AddfieldFourCall callBack) {
        callBack.getAddfieldFouritemcall(position);
    }
    public void getcartitemdata(Field_AddFieldChoosePictureCallBack.ShopCartCall callBack) {
        callBack.getcartitemdata(check, price, subsidy_fee, orderpaylistsize);
    }
    public void edotoraddressinfo(Field_AddFieldChoosePictureCallBack.EditorAddress callBack) {
        callBack.editor(addressContactModel, position);
    }
    public void editorOrderApproved(Field_AddFieldChoosePictureCallBack.FieldOrderApproved callBack) {
        callBack.postOrderApproved(state, position, getconfigurate_errormsg);
    }
    public void showreview(Field_AddFieldChoosePictureCallBack.FieldreviewCall callBack) {
        callBack.fieldshowreviewpicture(path, position);
    }
}
