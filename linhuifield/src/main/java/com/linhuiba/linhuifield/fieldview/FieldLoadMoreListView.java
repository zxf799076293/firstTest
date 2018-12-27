package com.linhuiba.linhuifield.fieldview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.CommonValue;

import java.util.List;

public class FieldLoadMoreListView extends ListView implements OnScrollListener{
	private View footer;
	private LinearLayout mload_more_loaded_layout;
	private LinearLayout mload_more_loading_layout;
	private int totalItem;
	private int lastItem;
	private boolean isLoaded;

	private OnLoadMore onLoadMore;

	private LayoutInflater inflater;
	private List mData;

	public FieldLoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	public FieldLoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FieldLoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressLint("InflateParams")
	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.load_more_footer,null ,false);
		mload_more_loading_layout = (LinearLayout)footer.findViewById(R.id.load_more_loading_layout);
		mload_more_loaded_layout = (LinearLayout)footer.findViewById(R.id.load_more_loaded_layout);
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
			if(!isLoaded){
				set_loading();
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
		set_refresh();
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

	public void set_loading() {
		footer.setVisibility(View.VISIBLE);
		mload_more_loaded_layout.setVisibility(GONE);
		mload_more_loading_layout.setVisibility(VISIBLE);
	}
	public void set_refresh() {
		isLoaded = false;
		footer.setVisibility(View.GONE);
	}
	public void set_loaded() {
		isLoaded = true;
		footer.setVisibility(View.VISIBLE);
		mload_more_loaded_layout.setVisibility(VISIBLE);
		mload_more_loading_layout.setVisibility(GONE);
	}
}