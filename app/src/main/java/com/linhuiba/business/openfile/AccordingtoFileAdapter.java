package com.linhuiba.business.openfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.ApplyForInvoiceActivity;
import com.linhuiba.business.openfile.utils.FileTypeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AccordingtoFileAdapter extends BaseAdapter {
    private List<File> data = new ArrayList<>();
    private Context mcontext;
    private AccordingtoFileActivity mactivity;
    private LayoutInflater mInflater = null;
    private int type;

    public AccordingtoFileAdapter(Context context, AccordingtoFileActivity activity, List<File> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = invoicetype;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_file, null);
            holder.mFileTite = (TextView) convertView.findViewById(R.id.item_file_title);
            holder.mFileSubtitle = (TextView) convertView.findViewById(R.id.item_file_subtitle);
            holder.mFileImage = (ImageView) convertView.findViewById(R.id.item_file_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File currentFile = data.get(position);

        FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(currentFile);
        holder.mFileImage.setImageResource(fileType.getIcon());
        holder.mFileSubtitle.setText(fileType.getDescription());
        holder.mFileTite.setText(currentFile.getName());

        if (type == 0) {
            holder.mFileTite.setTextColor(mactivity.getResources().getColor(R.color.applyforinvoice_falsestate));
            holder.mFileSubtitle.setTextColor(mactivity.getResources().getColor(R.color.applyforinvoice_falsestate));
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView mFileImage;
        public TextView mFileTite;
        public TextView mFileSubtitle;

    }
}