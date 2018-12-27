package com.linhuiba.business.connector;

import com.linhuiba.business.model.FieldInfoSizeModel;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/12/26.
 */

public class MyCompartor  implements Comparator {
    private final int priceSort = 0;
    private final int sizeSort = 1;
    private int mSortType;
    public MyCompartor(int type) {
        this.mSortType = type;
    }
    @Override
    public int compare(Object o1, Object o2) {
        FieldInfoSizeModel sdto1= (FieldInfoSizeModel )o1;
        FieldInfoSizeModel sdto2= (FieldInfoSizeModel )o2;
        int compareInt = -2;
        if (mSortType == priceSort) {
            compareInt = sdto1.getDimension().getMin_price().compareTo(sdto2.getDimension().getMin_price());

        } else if (mSortType == sizeSort) {
            compareInt = sdto1.getDimension().getMin_price().compareTo(sdto2.getDimension().getMin_price());
        }
        return compareInt;
    }
}
