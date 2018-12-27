package com.linhuiba.business.fieldcallback;

import com.linhuiba.business.model.AddressContactModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Field_AddFieldChoosePictureCallBack {
    public interface AdFieldChoosePictureCall {
        public void	 addfieldchoosepicture(ArrayList<String> path);
    }
    public interface AdField_deleteChoosePictureCall {
        public void	 addfield_deletechoosepicture(int item_num);
    }
    public interface AdField_showPreviewImgCall {
        public void	 AdField_showPreviewImg(int position);
    }
    public interface GetconfigurateCall {
        public void	 AdField_showPreviewImg(String errormsg);
    }
    public interface CalendarClickCall {
        public void	 CalendarClick(int position);
    }
    public interface Fieldsize_PriceunitCall {
        public void	 getfieldsize_pricenuit(int position,String str);
    }
    public interface AddfieldFourCall {
        public void	 getAddfieldFouritemcall(int position);
    }
    public interface ShopCartCall {
        public void	 getcartitemdata(Boolean check,String price,String subsidy_fee,String orderpaylistsize);
    }
    public interface EditorAddress {
        public void	 editor(AddressContactModel data,int position);
    }
    public interface FieldOrderApproved {
        public void	 postOrderApproved(int state,int position,String str);
    }
    public interface FieldreviewCall {
        public void	fieldshowreviewpicture(ArrayList<String> path,int position);
    }

}
