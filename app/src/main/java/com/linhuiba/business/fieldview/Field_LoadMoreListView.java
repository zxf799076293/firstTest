package com.linhuiba.business.fieldview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.linhuiba.business.R;
import com.linhuiba.business.config.CommonValue;

import java.util.List;

public class Field_LoadMoreListView extends ListView implements OnScrollListener{
	private View footer;
	
	private int totalItem;
	private int lastItem;
	
	private boolean isLoading;
	
	private OnLoadMore onLoadMore;
	
	private LayoutInflater inflater;
	private List mData;
	
	public Field_LoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	public Field_LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public Field_LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressLint("InflateParams")
	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.field_load_more_footer,null ,false);
		footer.setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.lastItem = firstVisibleItem+visibleItemCount;
		this.totalItem = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(this.totalItem == lastItem&&scrollState == SCROLL_STATE_IDLE){
            if(mData != null && mData.size() < CommonValue.MAX){
                return;
            }
			Log.v("isLoading", "yes");
			if(!isLoading){
				isLoading=true;
				footer.setVisibility(View.VISIBLE);
				if(onLoadMore != null){
					onLoadMore.loadMore();
				}

			}
		}
	}
	public void setLoadMoreListen(OnLoadMore onLoadMore){
		this.onLoadMore = onLoadMore;
	}
	/**
	 * 加载完成调用此方法
	 */
	public void onLoadComplete(){
		footer.setVisibility(View.GONE);
		isLoading = false;
		
	}
	
	public interface OnLoadMore{
		public void loadMore();
	}

    public List getmData() {
        return mData;
    }

    public void setmData(List mData) {
        this.mData = mData;
    }
}