package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class FieldAddfieldAttributesModel implements Serializable {
    private Integer id;//属性ID
    private Integer option_id;//属性选项ID
    private String name;//属性 选项值名称
    private String mark;//属性备注（选项【其它】中输入的内容）
    private Integer multi;//属性类型
    private String pic_url;//资源图片url
    private Integer seq;//图片顺序 (1、2 . . ) //选项值顺序
    private int type;//控件类型（1:单选,2:多选,3:单文本,4:多文本）
    private int required;
    protected int level;//类目级别（1,2,3,4四种）
    private int enable;//是否启用（0:停用,1:启用）
    private int is_input;//是否有输入框
    private int attribute_id;
    private int all_inputs;//多选是否全部有输入框
    private String hint = "";//属性框的提示语
    private String placeholder = "";//单选多选提示语
    private Integer max_length;
    private Integer min_length;
    private ArrayList<FieldAddfieldAttributesModel> attributes = new ArrayList<>();
    private ArrayList<FieldAddfieldAttributesModel> options = new ArrayList<>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOption_id() {
        return option_id;
    }

    public void setOption_id(Integer option_id) {
        this.option_id = option_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getMulti() {
        return multi;
    }

    public void setMulti(Integer multi) {
        this.multi = multi;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getIs_input() {
        return is_input;
    }

    public void setIs_input(int is_input) {
        this.is_input = is_input;
    }

    public int getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }

    public int getAll_inputs() {
        return all_inputs;
    }

    public void setAll_inputs(int all_inputs) {
        this.all_inputs = all_inputs;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Integer getMax_length() {
        return max_length;
    }

    public void setMax_length(Integer max_length) {
        this.max_length = max_length;
    }

    public Integer getMin_length() {
        return min_length;
    }

    public void setMin_length(Integer min_length) {
        this.min_length = min_length;
    }

    public ArrayList<FieldAddfieldAttributesModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<FieldAddfieldAttributesModel> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<FieldAddfieldAttributesModel> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<FieldAddfieldAttributesModel> options) {
        this.options = options;
    }
}
