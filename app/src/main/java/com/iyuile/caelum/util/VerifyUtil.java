package com.iyuile.caelum.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 验证号码
 */
public class VerifyUtil {
    /**
     * 验证手机格式
     *
     * @param mobiles
     * @return true=确认的;false=格式不对
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        return mobiles.matches(telRegex);
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 判断邮编
     *
     * @param zipString
     * @return
     */
    public static boolean isZipNO(String zipString) {
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    /**
     * 验证是否是URL
     * or使用android自带的
     * URLUtil.isValidUrl 返回 true，则该 url 是一个有效的 url 的任何 url type （http url / url 等文件）。
     * 而 URLUtil.isNetworkUrl 则仅返回 true 如果 url 是一个 http / https url (即基于 http Protocol的网络资源的网络 url 交代)
     * (查看源码)
     *
     * @param url
     * @return
     * @author YOLANDA
     */
    public static boolean isURL(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        return matcher.matches();
    }

    // ------------------------------------------------------------------------------

    //用于匹配手机号码
    private final static String REGEX_MOBILEPHONE = "^0?1[3458]\\d{9}$";

    //用于匹配固定电话号码
    private final static String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";

    //用于获取固定电话中的区号
    private final static String REGEX_ZIPCODE = "^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$";

    private static Pattern PATTERN_MOBILEPHONE;
    private static Pattern PATTERN_FIXEDPHONE;
    private static Pattern PATTERN_ZIPCODE;


    static {
        PATTERN_FIXEDPHONE = Pattern.compile(REGEX_FIXEDPHONE);
        PATTERN_MOBILEPHONE = Pattern.compile(REGEX_MOBILEPHONE);
        PATTERN_ZIPCODE = Pattern.compile(REGEX_ZIPCODE);
    }

    public static enum PhoneType {
        /**
         * 手机
         */
        CELLPHONE,

        /**
         * 固定电话
         */
        FIXEDPHONE,

        /**
         * 非法格式号码
         */
        INVALIDPHONE
    }

    public static class Number {
        private PhoneType type;
        /**
         * 如果是手机号码，则该字段存储的是手机号码 前七位；如果是固定电话，则该字段存储的是区号
         */
        private String code;
        private String number;

        public Number(PhoneType _type, String _code, String _number) {
            this.type = _type;
            this.code = _code;
            this.number = _number;
        }

        public PhoneType getType() {
            return type;
        }

        public String getCode() {
            return code;
        }

        public String getNumber() {
            return number;
        }

        public String toString() {
            return String.format("[number:%s, type:%s, code:%s]", number, type.name(), code);
        }
    }

    /**
     * 判断是否为手机号码
     *
     * @param number 手机号码
     * @return
     */
    public static boolean isCellPhone(String number) {
        Matcher match = PATTERN_MOBILEPHONE.matcher(number);
        return match.matches();
    }

    /**
     * 判断是否为固定电话号码
     *
     * @param number 固定电话号码
     * @return
     */
    public static boolean isFixedPhone(String number) {
        Matcher match = PATTERN_FIXEDPHONE.matcher(number);
        return match.matches();
    }


    /**
     * 获取固定号码号码中的区号
     *
     * @param strNumber
     * @return
     */
    public static String getZipFromHomephone(String strNumber) {
        Matcher matcher = PATTERN_ZIPCODE.matcher(strNumber);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * 检查号码类型，并获取号码前缀，手机获取前7位，固话获取区号
     *
     * @param number
     * @return
     */
    public static Number checkNumber(String _number) {
        String number = _number;
        Number rtNum = null;

        if (number != null && number.length() > 0) {
            if (isCellPhone(number)) {
                //如果手机号码以0开始，则去掉0
                if (number.charAt(0) == '0') {
                    number = number.substring(1);
                }

                rtNum = new Number(PhoneType.CELLPHONE, number.substring(0, 7), _number);
            } else if (isFixedPhone(number)) {
                //获取区号
                String zipCode = getZipFromHomephone(number);
                rtNum = new Number(PhoneType.FIXEDPHONE, zipCode, _number);
            } else {
                rtNum = new Number(PhoneType.INVALIDPHONE, null, _number);
            }
        }

        return rtNum;
    }

    @NonNull
    public static String processTelephone(String telephone) {
        telephone = telephone.substring(0, 3) + "****" + telephone.substring(7, telephone.length());
        return telephone;
    }

    public static void main(String[] args) {
        Number num = checkNumber("013951699549");
        System.out.println(num);

        num = checkNumber("13951699549");
        System.out.println(num);

        num = checkNumber("051687189099");
        System.out.println(num);

        num = checkNumber("02552160433");
        System.out.println(num);

        num = checkNumber("52160433");
        System.out.println(num);
    }

}
