package com.iyuile.caelum.util;


/**
 * @author WangYao
 * @version 1
 * @Description 使用Log来显示调试信息, 因为log在实现上每个message有4k字符长度限制
 * @ClassName {@link LUtils}
 * @Date 2015-11-13 下午4:45:50
 */
public class LUtils {

    // 使用Log来显示调试信息,因为log在实现上每个message有4k字符长度限制
    // 所以这里使用自己分节的方式来输出足够长度的message
    public static void show(String str) {
        str = str.trim();
        int index = 0;
        int maxLength = 4000;
        String sub;
        while (index < str.length()) {
            // java的字符不允许指定超过总的长度end
            if (str.length() <= index + maxLength) {
                sub = str.substring(index);
            } else {
                sub = str.substring(index, maxLength);
            }

            index += maxLength;
            Log.e(sub.trim());
        }
    }
}
