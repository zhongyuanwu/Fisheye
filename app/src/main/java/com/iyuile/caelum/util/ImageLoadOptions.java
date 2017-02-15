package com.iyuile.caelum.util;

import android.graphics.Bitmap;

import com.iyuile.caelum.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * @author WangYao
 * @version 1
 * @Description 图片加载选项
 * @ClassName {@link ImageLoadOptions}
 * @Date 2016-4-7 下午4:46:49
 * http://blog.csdn.net/vipzjyno1/article/details/23206387
 * <p>
 * <p>
 * .displayer(new CircleBitmapDisplayer(ContextCompat.getColor(MyApplication.getInstance(), R.color.theme_red), PixelUtil.dp2px(4l)))//圆形头像,可设置边框(边框颜色可半透明)
 * .displayer(new RoundedBitmapDisplayer(PixelUtil.dp2px(4l)))//是否设置为圆角
 * .displayer(new RoundedVignetteBitmapDisplayer(PixelUtil.dp2px(4l),0))//是否设置为圆角,自带内阴影
 * .displayer(new FadeInBitmapDisplayer(450))// 淡入
 * .displayer(new SimpleBitmapDisplayer())//正常显示
 */
public class ImageLoadOptions {

    /**
     * 头像
     *
     * @return
     */
    public static DisplayImageOptions getOptionAvatar() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_img_user)// 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.no_img_user)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.no_img_user)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)// 设置图片下载前的延迟//int delayInMillis为你设置的延迟时间
                // 。preProcessor(BitmapProcessor preProcessor)// 设置图片加入缓存前，对bitmap进行设置
                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                .displayer(new SimpleBitmapDisplayer())
                .build();
        return options;
    }
    /**
     * 商城商品
     *
     * @return
     */
    public static DisplayImageOptions getOptionItem() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_img_item)// 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.no_img_item)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.no_img_item)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)// 设置图片下载前的延迟//int delayInMillis为你设置的延迟时间
                // 。preProcessor(BitmapProcessor preProcessor)// 设置图片加入缓存前，对bitmap进行设置
                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                .displayer(new SimpleBitmapDisplayer())
                .build();
        return options;
    }

    /**
     * 图片验证码
     *
     * @return
     */
    @Deprecated
    public static DisplayImageOptions getOptionImageCode() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_imag_code)// 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_imag_code)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_imag_code)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(false)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)// 设置图片下载前的延迟//int delayInMillis为你设置的延迟时间
                // 。preProcessor(BitmapProcessor preProcessor)// 设置图片加入缓存前，对bitmap进行设置
                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(PixelUtil.dp2px(4l)))//是否设置为圆角
                .build();
        return options;
    }

    /*@Deprecated
    static Map<String, String> headers = new HashMap<String, String>();

    static {
        headers.put(NetworkConstants._PARAM_ACCEPT, NetworkConstants._VALUE_ACCEPT);
    }

    *//**
     * 图片验证码
     *
     * @return
     *//*
    @Deprecated
    public static DisplayImageOptions getOptionImageCode() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_imag_code)// 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_imag_code)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_imag_code)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(false)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .extraForDownloader(headers)// :::图片验证码头信息
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)// 设置图片下载前的延迟//int delayInMillis为你设置的延迟时间
                // 。preProcessor(BitmapProcessor preProcessor)// 设置图片加入缓存前，对bitmap进行设置
                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                .displayer(new SimpleBitmapDisplayer())
                .build();
        return options;
    }*/


}
