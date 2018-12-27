# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------
-keep class com.linhuiba.business.model.** { *; }
-keep class com.linhuiba.business.fieldmodel.** { *; }
-keep class com.linhuiba.business.mvpmodel.** { *; }
-keep class com.linhuiba.business.mvppresenter.** { *; }
-keep class com.linhuiba.business.mvpview.** { *; }
-keep class com.linhuiba.business.connector.** { *; }
-keep class com.linhuiba.business.adapter.** { *; }
-keep class com.linhuiba.business.CalendarClass.** { *; }
-keep class com.linhuiba.business.basemvp.** { *; }
#-keep class com.linhuiba.linhuifield.util.** { *; }
#-keep class com.linhuiba.linhuifield.View_StickyGridHeaders.** { *; }
#-keep class com.linhuiba.linhuifield.fragment.** { *; }
#-keep class com.linhuiba.linhuifield.fieldview.** { *; }
-keep class com.linhuiba.linhuifield.fieldmodel.** { *; }
-keep class com.linhuiba.linhuifield.sqlite.** { *; }
-keep class com.linhuiba.linhuifield.fieldadapter.** { *; }
-keep class com.linhuiba.linhuifield.CalendarClass.** { *; }
-keep class com.linhuiba.linhuifield.connector.** { *; }
-keep class com.linhuiba.linhuipublic.callbackmodel.** { *; }
-keep class com.linhuiba.linhuipublic.config.** { *; }
-keep class com.linhuiba.linhuipublic.fieldbasemvp.** { *; }
#-keep class com.linhuiba.linhuifield.fieldcallback.** { *; }
-keepattributes EnclosingMetho
#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------
-keep class com.qiniu.android.dns.** { *; }
-keep class org.apache.http.entity.mime.** { *; }
-keep class com.tencent.** { *; }
-keep class com.qiniu.android.** { *; }
-keep class org.apache.** { *; }
#百度地图
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**
#友盟推送
-dontwarn com.umeng.**
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.meizu.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
 -dontwarn com.umeng.message.**
 -keep class com.umeng.message.** { *;}
 #友盟统计
 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }
-keep public class [com.linhuiba.business].R$*{
public static final int *;
}
 #微信
-keep class com.tencent.mm.opensdk.** {

*;

}

-keep class com.tencent.wxop.** {

*;

}

-keep class com.tencent.mm.sdk.** {

*;

}
 #支付宝
 -keep class com.alipay.android.app.IAlixPay{*;}
 -keep class com.alipay.android.app.IAlixPay$Stub{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
 -keep class com.alipay.sdk.app.PayTask{ public *;}
 -keep class com.alipay.sdk.app.AuthTask{ public *;}
 -keep class com.alipay.sdk.app.H5PayCallback {
     <fields>;
     <methods>;
 }
 -keep class com.alipay.android.phone.mrpc.core.** { *; }
 -keep class com.alipay.apmobilesecuritysdk.** { *; }
 -keep class com.alipay.mobile.framework.service.annotation.** { *; }
 -keep class com.alipay.mobilesecuritysdk.face.** { *; }
 -keep class com.alipay.tscenter.biz.rpc.** { *; }
 -keep class org.json.alipay.** { *; }
 -keep class com.alipay.tscenter.** { *; }
 -keep class com.ta.utdid2.** { *;}
 -keep class com.ut.device.** { *;}
 -dontwarn android.net.**
 -keep class android.net.SSLCertificateSocketFactory{*;}
#Butter Knife
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector{ *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#banner glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#13 picasso
-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {    native <methods>;}
#fastjson
-keepattributes Signature
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*; }
#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 不混淆内部类
-keepattributes InnerClasses
#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------

# 保护注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}

# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod

#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#support.v4/v7包不混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**    # 忽略警告

#保持注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}
#保持Serializable实现类不被混淆
-keepnames class * implements java.io.Serializable
#保持Serializable不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举enum类不被混淆
-keepclassmembers enum * {
  public static **[] values();
 public static ** valueOf(java.lang.String);
}
#自定义组件不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#美洽
#     OkHttp相关

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**


#      Okio相关

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

#     UIL相关

-keep class com.nostra13.universalimageloader.** { *; }
-keepclassmembers class com.nostra13.universalimageloader.** {*;}
-dontwarn com.nostra13.universalimageloader.**

#     Glide相关

-keep class com.bumptech.glide.Glide { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.**

#     Picasso相关

-keep class com.squareup.picasso.Picasso { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.picasso.**

#     xUtils3相关

-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
-dontwarn org.xutils.**

#不混淆android-async-http(这里的与你用的httpClient框架决定)
-keep class com.loopj.android.http.**{*;}

 #不混淆org.apache.http.legacy.jar
 -dontwarn android.net.compatibility.**
 -dontwarn android.net.http.**
 -dontwarn com.android.internal.http.multipart.**
 -dontwarn org.apache.commons.**
 -dontwarn org.apache.http.**
 -keep class android.net.compatibility.**{*;}
 -keep class android.net.http.**{*;}
 -keep class com.android.internal.http.multipart.**{*;}
 -keep class org.apache.commons.**{*;}
 -keep class org.apache.http.**{*;}
 #rxjava
 -dontwarn rx.**
 -keep class rx.** { *; }

 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
  long producerIndex;
  long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }
 #compile 'com.jph.takephoto:takephoto_library:4.0.3'
 -keep class com.jph.takephoto.** { *; }
 -dontwarn com.jph.takephoto.**

 -keep class com.darsh.multipleimageselect.** { *; }
 -dontwarn com.darsh.multipleimageselect.**

 -keep class com.soundcloud.android.crop.** { *; }
 -dontwarn com.soundcloud.android.crop.**
 ###排除所有注解类
 -keep class * extends java.lang.annotation.Annotation { *; }
 -keep interface * extends java.lang.annotation.Annotation { *; }


 ###保留使用xUtils的方法和类，并且不要混淆名字
 -keep @com.lidroid.xutils.db.annotation.Table class *
 -keepclassmembers class * {
     @com.lidroid.xutils.db.annotation.* <fields>;
 }
 -keepclassmembers, allowobfuscation class * {
 @com.lidroid.xutils.view.annotation.* <fields>;
     @com.lidroid.xutils.view.annotation.event.* <methods>;
 }
 -keep class **$$PermissionProxy { *; }
 # 从而我们在layout里面编写onClick就不会影响
 -keepclassmembers class * extends android.app.Activity {
     public void * (android.view.View);
 }
 # 对于带有回调函数onXXEvent的，不能混淆
 -keepclassmembers class * {
     void *(**On*Event);
 }
 -keep class com.null.test.entities.** {
     *;
 }
 -keep class com.null.test.MainActivity$* {
     *;
 }
 #魔窗
 -keep class com.tencent.mm.sdk.** {*;}
 -keep class cn.magicwindow.** {*;}
 -dontwarn cn.magicwindow.**
 #GrowingIo
 -keep class com.growingio.android.sdk.** {
     *;
 }
 -dontwarn com.growingio.android.sdk.**
 -keepnames class * extends android.view.View
 -keep class * extends android.app.Fragment {
     public void setUserVisibleHint(boolean);
     public void onHiddenChanged(boolean);
     public void onResume();
     public void onPause();
 }
 -keep class android.support.v4.app.Fragment {
     public void setUserVisibleHint(boolean);
     public void onHiddenChanged(boolean);
     public void onResume();
     public void onPause();
 }
 -keep class * extends android.support.v4.app.Fragment {
     public void setUserVisibleHint(boolean);
     public void onHiddenChanged(boolean);
     public void onResume();
     public void onPause();
 }
 -keep class com.growingio.android.sdk.collection.GrowingIOInstrumentation {
     public *;
     static <fields>;
 }
 #腾讯应用分析
 -keep class com.tencent.stat.*{*;}
 -keep class com.tencent.mid.*{*;}
 #recyclerview
 -keep class com.chad.library.adapter.** {
 *;
 }
 -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
 -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
 -keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
      <init>(...);
 }
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------
