/**
 * 
 */

package com.baselib.app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseListViewAdapter<T> extends BaseAdapter {

    private Context context;

    private List<T> data;

    private LayoutInflater inflater;

    private int resource;

    private int currentPosition;

    public Resources resources;

    public BaseListViewAdapter(Context context, int resource) {
        this(context, resource, null);
    }

    public BaseListViewAdapter(Context context, int resource, List<T> data) {
        setContext(context);
        setResource(resource);
        setData(data);
        setInflater((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        resources = getContext().getResources();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return (data != null && position >= 0 && position < data.size()) ? data.get(position)
                : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            convertView.setTag(view2Holder(convertView));
        }
        setCurrentPosition(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        bindView(viewHolder, getItem(position));
        return convertView;
    }

    protected abstract ViewHolder view2Holder(View view);

    protected abstract void bindView(ViewHolder viewHolder, T data);

    public void clear() {
        if (data != null && !data.isEmpty()) {
            data.clear();
        }
    }

    /**
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * @return the inflater
     */
    public LayoutInflater getInflater() {
        return inflater;
    }

    /**
     * @param inflater the inflater to set
     */
    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    /**
     * @return the resource
     */
    public int getResource() {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(int resource) {
        this.resource = resource;
    }

    /**
     * @return the currentPosition
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * @param currentPosition the currentPosition to set
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

}
