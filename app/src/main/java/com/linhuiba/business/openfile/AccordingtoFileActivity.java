package com.linhuiba.business.openfile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.openfile.utils.FileComparator;
import com.linhuiba.business.util.TitleBarUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AccordingtoFileActivity extends BaseMvpActivity {
    @InjectView(R.id.filelistview)
    ListView mfilelistview;
    private AccordingtoFileAdapter accordingtoFileAdapter;
    private static List<File> filelist = new ArrayList<File>();
    private Handler mHandler;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordingtofile);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.apply_plan_txt));
        TitleBarUtils.showBackImg(this, true);
        initview();
        mHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 1:
                        if (accordingtoFileAdapter != null) {
                            accordingtoFileAdapter.notifyDataSetChanged();
                            mfilelistview.requestLayout();
                            mfilelistview.setAdapter(accordingtoFileAdapter);
                        }

                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getFileListByDirPath("/storage/emulated/0",mHandler);
            }
        });
        thread.start();
    }
    private void initview() {
        accordingtoFileAdapter = new AccordingtoFileAdapter(this,this,filelist ,0);
        mfilelistview.setAdapter(accordingtoFileAdapter);
    }


    public static List<File> getFileListByDirPath(String path,Handler handler) {
        if (filelist != null) {
            filelist.clear();
        }
        File directory = new File(path);
        getAllFiles(directory,"txt","pdf","doc",handler);
        if (filelist == null) {
            return new ArrayList<>();
        }

        Collections.sort(filelist, new FileComparator());
        return filelist;
    }
    public static void getAllFiles(File root, String txt,String pdf,String doc,Handler mhandler){
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                if(f.isFile()){
                    if (f.getPath().substring(f.getPath().length() - txt.length()).equals(txt) ||
                            f.getPath().substring(f.getPath().length() - pdf.length()).equals(pdf) ||
                            f.getPath().substring(f.getPath().length() - doc.length()).equals(doc)) {
                        filelist.add(f);
                        Message message=new Message();
                        message.what=1;
                        mhandler.sendMessage(message);
                    }

                }else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) {
                    getAllFiles(f,"txt","pdf","doc",mhandler);

                }
            }
        }
    }

}
