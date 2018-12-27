
package com.baselib.app.adapter;

import android.content.Context;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;

/**
 * Created by snowd on 14-8-21.
 */
public abstract class BaseEndlessAdapter extends EndlessAdapter {
    public BaseEndlessAdapter(ListAdapter wrapped) {
        super(wrapped);
    }

    public BaseEndlessAdapter(ListAdapter wrapped, boolean keepOnAppending) {
        super(wrapped, keepOnAppending);
    }

    public BaseEndlessAdapter(Context context, ListAdapter wrapped, int pendingResource) {
        super(context, wrapped, pendingResource);
    }

    public BaseEndlessAdapter(Context context, ListAdapter wrapped, int pendingResource,
            boolean keepOnAppending) {
        super(context, wrapped, pendingResource, keepOnAppending);
    }
}
