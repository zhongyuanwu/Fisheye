package com.iyuile.caelum.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import okio.ByteString;

/**
 * 加密
 * Created by WangYao on 2016/11/28.
 */
public class EncryptionUtils {

    /**
     * Returns a 32 character string containing an MD5 hash of {@code s}.
     */
    public static String md5Hex(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(s.getBytes("UTF-8"));
            return ByteString.of(md5bytes).hex();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns a Base 64-encoded string containing a SHA-1 hash of {@code s}.
     */
    public static String shaBase64(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] sha1Bytes = messageDigest.digest(s.getBytes("UTF-8"));
            return ByteString.of(sha1Bytes).base64();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    /**
     * {@links http://blog.csdn.net/u012416914/article/details/50395508}
     * 错误描述
     * <p>
     * 有问题的方法在算大部分的文件的md5值都是没有问题的，只有当文件的md5值是0开头的时候会出问题，
     * 之前的方法会把开头的0去掉，就导致本来没有问题的文件，我这边比较的md5值也不一致。
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param file
     * @param listChild ;true递归子目录中的文件
     * @return
     */
    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String md5;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5 = getFileMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

}
