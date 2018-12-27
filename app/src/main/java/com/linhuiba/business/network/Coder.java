package com.linhuiba.business.network;

import com.linhuiba.linhuipublic.config.BaseUrlFactory;

/**
 * Created by snowd on 15/3/26.
 */
public interface Coder extends BaseUrlFactory.ParameterEncoder {
    public String decode(String str);

}
