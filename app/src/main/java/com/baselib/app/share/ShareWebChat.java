package com.baselib.app.share;//
//package com.baselib.app.share;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXTextObject;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
//import java.io.ByteArrayOutputStream;
//
///**
// * 微信分享
// * @author yangping.hyp
// *
// */
//public class ShareWebChat {
//
//    private static final String TAG = "ShareWebChat";
//    /**
//     * 发送的目标场景，表示发送到会话
//     */
//    public static final int WXSceneSession = SendMessageToWX.Req.WXSceneSession;
//
//    /**
//     * 发送的目标场景，表示发送到微信收藏
//     */
//    public static final int WXSceneFavorite = SendMessageToWX.Req.WXSceneFavorite;
//
//    /**
//     * 发送的目标场景，表示发送朋友圈
//     */
//    public static final int WXSceneTimeline = SendMessageToWX.Req.WXSceneTimeline;
//
//    private static final int THUMB_SIZE = 64;
//
//    private static IWXAPI sApi;
//    private static boolean sRregisted = false;
//
//    /**
//     * 注册到微信, 分享之前需要调用
//     *
//     * @param context
//     * @param appId 申请的appid
//     * @return 注册成功或者失败
//     */
//    public synchronized static boolean regToWx(Context context, String appId) {
//        if (sApi == null) {
//            sApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), appId);
//        }
//        if (!sRregisted) {
//            sRregisted = sApi.registerApp(appId);
//        }
//        return sRregisted;
//    }
//
//    /**
//     * 发送带链接的文本信息， 确保在调用该方法应用执行过{@link #regToWx}
//     *
//     * @param title 标题， 限制长度不超过512Bytes
//     * @param description 描述，限制长度不超过1KB
//     * @param bitmap 图片
//     * @param url 分享的url链接, 长度大于0且不超过10KB
//     * @param shareTo One of {@link #WXSceneSession}, {@link #WXSceneFavorite},
//     *            or {@link #WXSceneTimeline}.
//     * @return
//     */
//    public static boolean shareUrlToWebChat(String title, String description, Bitmap bitmap,
//            String url, int shareTo) {
//        if (TextUtils.isEmpty(url)) {
//            Log.e(TAG, "shareUrlToWebChat but url is null");
//            return false;
//        }
//        WXMediaMessage msg = new WXMediaMessage();
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = url;
//        msg.mediaObject = webpage;
//        msg.title = title;
//        msg.description = description;
//        if (bitmap.getWidth() <= THUMB_SIZE && bitmap.getHeight() <= THUMB_SIZE) {
//            msg.thumbData = bmpToByteArray(bitmap, true);
//        } else {
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
//            msg.thumbData = bmpToByteArray(thumbBmp, true); // 限制内容大小不超过32KB
//        }
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
//        req.message = msg;
//        req.scene = shareTo;
//        if (sApi.sendReq(req)) {
//            Log.d(TAG, "shareUrlToWebChat success");
//            return true;
//        } else {
//            Log.e(TAG, "shareUrlToWebChat error");
//            return false;
//        }
//    }
//
//    /**
//     * 发送文本信息，确保在调用该方法应用执行过{@link #regToWx}
//     *
//     * @param desc 描述，限制长度不超过1KB, 如为空则截取content的前20个字符(如果足够长)
//     * @param content 分享的文本信息, 长度需大于0且不超过10KB
//     * @param shareTo One of {@link #WXSceneSession}, {@link #WXSceneFavorite},
//     *            or {@link #WXSceneTimeline}.
//     * @return
//     */
//    public static boolean shareTextToWebChat(String desc, String content, int shareTo) {
//        if (TextUtils.isEmpty(content)) {
//            Log.e(TAG, "shareTextToWebChat but content is null");
//            return false;
//        }
//        WXMediaMessage msg = new WXMediaMessage();
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = content;
//
//        if (TextUtils.isEmpty(desc)) {
//            if (content.length() > 23) {
//                msg.description = content.substring(0, 20) + "...";
//            } else {
//                msg.description = content;
//            }
//        } else {
//            msg.description = desc;
//        }
//        msg.mediaObject = textObj;
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text");
//        req.message = msg;
//        req.scene = shareTo;
//        if (sApi.sendReq(req)) {
//            Log.d(TAG, "shareUrlToWebChat success");
//            return true;
//        } else {
//            Log.e(TAG, "shareUrlToWebChat error");
//            return false;
//        }
//    }
//
//    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        bmp.compress(CompressFormat.PNG, 100, output);
//        if (needRecycle) {
//            bmp.recycle();
//        }
//
//        byte[] result = output.toByteArray();
//        try {
//            output.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    private static String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type
//                + System.currentTimeMillis();
//    }
//}
