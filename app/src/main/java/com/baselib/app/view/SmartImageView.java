package com.baselib.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.linhuiba.business.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartImageView extends ImageView {
    private static final int LOADING_THREADS = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    private int photoWidth,screen_W;
    private int photoHeight,screen_H;
    private boolean isActionImage=false;
//	private int imagenum;
//	private int MAX_W[]=new int [20],MAX_H[]=new int [20];
//	private int MIN_W[]=new int [20],MIN_H[]=new int [20];

    private SmartImageTask currentTask;


    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /** 可见屏幕宽度 **/
    public void setScreen_W(int screen_W) {
        this.screen_W = screen_W;
    }

    /** 可见屏幕高度 **/
    public void setScreen_H(int screen_H) {
        this.screen_H = screen_H;
    }
    public int getscreen_W()
    {
        return this.screen_W;
    }
    public int getScreen_H()
    {
        return this.screen_H;
    }

    // Helpers to set image by URL
    public void setImageUrl(String url) {
        url = url;
        setImage(new WebImage(url));
    }

    public void setImageUrl(String url, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource) {
        setImage(new WebImage(url), fallbackResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new WebImage(url), fallbackResource, loadingResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, loadingResource, completeListener);
    }


    // Helpers to set image by contact address book id
    public void setImageContact(long contactId) {
        setImage(new ContactImage(contactId));
    }

    public void setImageContact(long contactId, final Integer fallbackResource) {
        setImage(new ContactImage(contactId), fallbackResource);
    }

    public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
    }


    // Set image using SmartImage object
    public void setImage(final SmartImage image) {
        setImage(image, null, null, null);
    }

    public void setImage(final SmartImage image, final SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, null, null, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, fallbackResource, fallbackResource, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource, final SmartImageTask.OnCompleteListener completeListener) {
        // Set a loading resource
        if(loadingResource != null){
            setImageResource(loadingResource);
        }

        // Cancel any existing tasks for this image view
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        // Set up the new task
        currentTask = new SmartImageTask(getContext(), image);
        currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                    setImageBitmap(bitmap);
                    photoHeight=bitmap.getHeight()/2;
                    photoWidth=bitmap.getWidth()/2;
                    setphoto(screen_W-photoWidth,screen_H-photoHeight);
                    Log.e("setImageMatrix", "smartimage");
                } else {
                    // Set fallback resource
                    if(fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                }

                if(completeListener != null){
                    completeListener.onComplete(bitmap);
                }
            }
        });

        // Run the task in a threadpool
        threadPool.execute(currentTask);
    }

    public static void cancelAllTasks() {
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    }
    public int getphotoheight(){
        return this.photoHeight;
    }
    public int getphotowidth(){
        return this.photoWidth;
    }
    public boolean getisAcImage(){
        return this.isActionImage;
    }
    public void setisAcImage(boolean ActionImage){
        this.isActionImage=ActionImage;
    }
    public void setphoto(int getw,int geth){
        Matrix matrix = new Matrix();
        matrix.postTranslate(getw,geth);
        Log.d("setphoto", "getw" + getw + "geth" + geth);
        setImageMatrix(matrix);
    }
//    public Matrix getMatrix()
//    {
//    	return this.matrix;
//    }

}