package com.linhuiba.linhuifield.network;


import com.field_baselib.app.network.FieldUrlFactory;

/**
 * Created by snowd on 15/3/26.
 */
public interface Coder extends FieldUrlFactory.ParameterEncoder {
    public String decode(String str);
}
