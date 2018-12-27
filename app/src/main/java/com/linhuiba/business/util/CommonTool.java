package com.linhuiba.business.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by ap on 2015/12/15.
 */
public class CommonTool {

    public static ArrayList map2list (Map map) {
        ArrayList listValue = new ArrayList();
        if (map != null){
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                listValue.add(map.get(key));
            }
        }
        return listValue;
    }

    public static boolean isMobileNO(String mobiles){
        String reg = "^1\\d{10}$";
        Pattern p = Pattern.compile( reg );
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isTelephone(String telephone){
        String reg = "^1[2|3|4|5|7|8|9]\\d{9}$|^(\\d{3,4}-?)?\\d{7,8}(-\\d{1,4})?$";
        Pattern p = Pattern.compile( reg );
        Matcher m = p.matcher(telephone);
        return m.matches();
    }
    public static boolean isResourcesSpecifications(String telephone){
        String reg = "^(\\s+)?(\\d+)(\\.\\d+)?\\s*\\*(\\s+)?(\\d+)(\\.\\d+)?\\s*$";
        Pattern p = Pattern.compile( reg );
        Matcher m = p.matcher(telephone);
        return m.matches();
    }
    public static boolean isEmail(String telephone){
        String reg = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern p = Pattern.compile( reg );
        Matcher m = p.matcher(telephone);
        return m.matches();
    }
    public static boolean isIdcard(String idcardnum){
        String reg = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X|x)$";
        Pattern p = Pattern.compile( reg );
        Matcher m = p.matcher(idcardnum);
        return m.matches();
    }
    public static boolean istaxNum(String taxNum){
        String reg = "^[A-Z0-9]+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(taxNum);
        return m.matches();
    }
}
