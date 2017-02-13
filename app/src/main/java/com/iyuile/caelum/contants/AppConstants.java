package com.iyuile.caelum.contants;


/**
 * @author WangYao
 * @version 1
 * @Description 应用常量
 * @date 2015-10-15 15:47:48
 */
public interface AppConstants {

    /**
     * 资源地址
     */
    String RESOURCE_URL = "http://res.qiyiwenhua.cn/";

    String ABOUTUS_WEB_URL = "http://support.caelum.iyuile.com/about";

    String PRIVACYPOLICY_WEB_URL = "http://support.caelum.iyuile.com/privacy";

//    @Deprecated
//    String FEEDBACK_WEB_URL = "http://support.apus.iyuile.com/feedback";
//    @Deprecated
//    String _PARAMS_FEEDBACK = "?version=%s&build=%s&system=%s&device=%s";

    String ITEM_URL_PARAM = "http://support.caelum.iyuile.com/items/%s";

//    @Deprecated
//    String SHARE_URL_PARAM = "http://support.sagitta.iyuile.com/shows/%s/share";
//
//    @Deprecated
//    String SHARE_URL_APP_DOWNLOAD = "http://support.sagitta.iyuile.com/download";
//
//
//
    String RECRUITING_AND_TRAINING_TELEPHONE = "010-89393217";

    /**
     * 验证码长度
     */
    int VERIFY_CODE_LENGTH = 4;

    /**
     * 图片码长度
     */
    int VERIFY_IMAGE_CODE_LENGTH = 4;

    /**
     * 再次发送验证码的时间(单位秒 )
     */
    long AGAIN_SEND_VERIFY_CODE_TIME = 60l;

    /**
     * 密码最小强度
     */
    int PASSWORD_INTENSITY_LENGTH_MIN = 6;

    /**
     * 密码最大强度
     */
    int PASSWORD_INTENSITY_LENGTH_MAX = 18;

    /**
     * 收货地址 最小
     */
    int RECEIPT_ADDRESS_NEW_ADDRESS_LENGTH_MIN = 5;

    /**
     * 收货人姓名 最大
     */
    int RECEIPT_ADDRESS_NEW_NAME_LENGTH_MAX = 30;

    /**
     * 收货地址 最大
     */
    int RECEIPT_ADDRESS_NEW_ADDRESS_LENGTH_MAX = 200;

    /**
     * 昵称正则表达式
     * <p/>
     * 1-12个字符，支持中英文、数字、"_"或减号
     */
    String NICKNAME_REGEX = "([\u4E00-\u9FA50-9a-zA-Z_-]{1,12})";

    /**
     * 真实姓名长度
     */
    int REAL_NAME_MAX = 10;

    /**
     * 公司名称
     */
    int COMPANY_NAME_MAX = 30;

    /**
     * 公司地址
     */
    int COMPANY_ADDRESS_MAX = 150;

    /**
     * 职位
     */
    int JOB_MAX = 20;

    /**
     * 反馈最长
     */
    int FEEDBACK_CONTENT_LENGTH_MAX = 400;

    /**
     * 用户背景图尺寸(上传)
     */
    int USERINFO_BACKGROUND_WIDTH = 640;
    int USERINFO_BACKGROUND_HEIGHT = 640;

    /**
     * 用户头像尺寸(上传)
     */
    int USERINFO_AVATAR_WIDTH = 500;
    int USERINFO_AVATAR_HEIGHT = 500;


    String IMAGENAME_SUFFIX_PNG = ".PNG";
    String IMAGENAME_SUFFIX_JPG = ".jpg";
    String VIDEONAME_SUFFIX_MP4 = ".mp4";

    String IMAGE_URL_BREAK = "|";

    String IMAGE_URL_BREAK_ANALYZE = "\\|";

    String IMAGE_USERDATA = "userdata/";

    String _IMAGE_AVATAR = IMAGE_USERDATA + "avatars/%s" + IMAGENAME_SUFFIX_JPG;

    String _IMAGE_GOD_SHEFUSHENCAI_ITEM = IMAGE_USERDATA + "guesses/%s" + IMAGENAME_SUFFIX_JPG;

    String _IMAGE_BG = IMAGE_USERDATA + "backgrounds/%s" + IMAGENAME_SUFFIX_JPG;

    String _IMAGE_VIDEO = IMAGE_USERDATA + "videos/%s" + VIDEONAME_SUFFIX_MP4;

    /**
     * 小头像
     * { http://developer.qiniu.com/code/v6/api/kodo-api/image/imageview2.html}
     */
    String IMAGE_URL_AVATAR_SMALL_THUMBNAIL_PARAMETER = "?imageView2/1/w/160/interlace/1/q/40";

    /**
     * 中头像
     */
    String IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER = "?imageView2/1/w/250/interlace/1/q/60";

    /**
     * 大头像
     */
    String IMAGE_URL_AVATAR_BIG_THUMBNAIL_PARAMETER = "?imageView2/1/w/320/interlace/1/q/90";

    /**
     * 用于listview等->单图模式缩略图(等比)
     */
    String IMAGE_URL_LISTVIEW_SINGLE_GEOMETRIC_THUMBNAIL_PARAMETER = "?imageView2/1/w/500/interlace/1/q/75";

    /**
     * 用于listview等->单图模式缩略图(按照宽度比)
     */
    String IMAGE_URL_LISTVIEW_SINGLE_THUMBNAIL_PARAMETER = "?imageView2/2/w/500/interlace/1/q/75";

    /**
     * 用于listview等->多图模式缩略图(等比)
     */
    String IMAGE_URL_LISTVIEW_MULTI_GEOMETRIC_THUMBNAIL_PARAMETER = "?imageView2/1/w/150/interlace/1/q/75";

    /**
     * 用于listview等->多图模式缩略图(按照宽度比)
     */
    String IMAGE_URL_LISTVIEW_MULTI_THUMBNAIL_PARAMETER = "?imageView2/2/w/150/interlace/1/q/75";

    /**
     * 用于原图显示
     */
    String IMAGE_URL_ORIGINAL_THUMBNAIL_PARAMETER = "?imageView2/2/w/640/interlace/1/q/75";

    /**
     * 自定义${模式}1 or 2 ${宽}  ${质量} [1-100]
     */
    String IMAGE_URL_CUSTOM_1_THUMBNAIL_PARAMETER = "?imageView2/%s/w/%s/interlace/1/q/%s";

    /**
     * 自定义${模式}1 or 2 ${高} ${质量} [1-100]
     */
    String IMAGE_URL_CUSTOM_2_THUMBNAIL_PARAMETER = "?imageView2/%s/h/%s/interlace/1/q/%s";

    /**
     * 视频缩略图
     * <p/>
     * {http://developer.qiniu.com/code/v6/api/dora-api/av/vframe.html}
     */
    String VIDEO_URL_THUMBNAIL_PARAMETER = "?vframe/jpg/offset/0/w/320";

    /**
     * 视频缩略图
     * <p/>
     * {http://developer.qiniu.com/code/v6/api/dora-api/av/vframe.html}
     */
    String VIDEO_URL_THUMBNAIL_PARAMETER_WIDTH_120 = "?vframe/jpg/offset/0/w/120";


    /**
     * 用于商城广告缩略图(按照宽度比)
     */
    String IMAGE_URL_BANNER_THUMBNAIL_PARAMETER = "?imageView2/2/w/400/interlace/1/q/75";

}
