package com.iyuile.caelum.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * @Description sd卡文件操作
 */
public class SDFileOperate {

    /**
     * 创建文件夹及文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @throws IOException
     */
    public static File createFile(String filePath, String fileName) {

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File dir = new File(filePath + File.separator + fileName);
        if (!dir.exists()) {
            try {
                // 在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
            }
        }

        return dir;
    }

    /**
     * 创建文件夹
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    public static File createFolder(String filePath) {

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     *
     观点1:
     FileWriter是字符流，文本类的可以用它效率高。
     FileOutputStream 是字节流，任何类型都可以用它操作。操作文本内容肯定没Writer快。
     观点2:
     FileWriter是往缓存中写，然后通过flush写到文件中。
     FileOutputStream是直接写入文件，没有缓存的过程。
     */

    /**
     * 向已创建的文件中写入数据
     * <p/>
     * 文件路径加名字
     *
     * @param strContent 内容
     */
    public static boolean writeFile(String filePathName, String strContent) {

        File file = new File(filePathName);
        if (file.exists()) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(filePathName, true);//
                // 创建FileWriter对象，用来写入字符流
                bw = new BufferedWriter(fw); // 将缓冲对文件的输出

                bw.write(strContent); // 写入文件 "\n"
                bw.newLine();
                bw.flush(); // 刷新该流的缓冲
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    bw.close();
                    fw.close();
                } catch (IOException e1) {
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 写数据到SD中的文件
     *
     * @param fileName
     * @throws IOException
     */
    public static void writeFileSdcardFile(String fileName, byte[] bytes)
            throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读SD中的文件
     *
     * @param fileName
     * @return content
     * @throws IOException
     */
    public static String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);

            res = EncodingUtils.getString(buffer, "UTF-8");

            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * resource的raw中读取文件数据
     *
     * @param context
     * @param resId
     */
    public static String readResourceRaw(Context context, int resId) {
        String res = "";
        try {

            // 得到资源中的Raw数据流
            InputStream in = context.getResources().openRawResource(resId);

            // 得到数据的大小
            int length = in.available();

            byte[] buffer = new byte[length];

            // 读取数据
            in.read(buffer);
            // 依test.txt的编码类型选择合适的编码，如果不调整会乱码
            res = EncodingUtils.getString(buffer, "BIG5");

            // 关闭
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * resource的asset中读取文件数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readResourceAsset(Context context, String fileName) {
        String res = "";
        try {

            // 得到资源中的asset数据流
            InputStream in = context.getResources().getAssets().open(fileName);

            int length = in.available();
            byte[] buffer = new byte[length];

            in.read(buffer);
            in.close();

            res = EncodingUtils.getString(buffer, "UTF-8");

        } catch (Exception e) {

            e.printStackTrace();

        }
        return res;
    }

    /**
     * 获取文件路径下的所有子文件
     *
     * @param filePath
     * @return
     * @Description list()方法返回的是没完整路径的文件名 ; listFiles()方法有完整路径的文件名
     */
    public static String[] getFilePathSubfile(String filePath) {
        File file = new File(filePath);
        return file.list();
    }

    /**
     * 删除文件或文件目录
     *
     * @param filePath
     */
    public static boolean deleteFilePath(File filePath) {
        if (filePath.exists()) {
            if (filePath.isFile()) {
                filePath.delete();
            }

            if (filePath.isDirectory()) {
                File[] childFiles = filePath.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    filePath.delete();
                }

                for (int i = 0; i < childFiles.length; i++) {
                    deleteFilePath(childFiles[i]);
                }
                filePath.delete();
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * 删除文件夹下的子文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFilePathSubfile(File filePath) {
        if (filePath.exists()) {
            File[] filePathSubfile = filePath.listFiles();
            if (filePathSubfile.length == 0)
                return true;
            for (File file : filePathSubfile) {
                SDFileOperate.deleteFilePath(file);
            }
            return true;
        }
        return false;
    }

    /**
     * Uri转file
     *
     * @param uri
     * @param data
     * @param context
     * @return
     * @Description uri如content://media/external/image/media/102 转换成 file真实路径
     */
    public static File UriConvertFile(Uri uri, Intent data, Context context) {
        uri = data.getData();

        String[] proj = {MediaStore.Images.Media.DATA};

//		Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
        Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor.getString(actual_image_column_index);

        return new File(img_path);
    }

    // ---------------------------------------------------------------------------------------------------------------

    public static long getFileSizes(File f) throws Exception {// 取得文件大小
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }

    // 递归
    public static long getFolderSize(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFolderSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static String FormetFileSizeM(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format((double) fileS / 1048576) + "MB";
    }

    public static long getListCount(File f) {// 递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getListCount(flist[i]);
                size--;
            }
        }
        return size;
    }


    /**
     * 获取文件内容
     *
     * @param fileName
     * @return
     */
    public static String getFromFileContent(String fileName) {

        File file = new File(fileName);
        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
            String line = "";
            String Result = "";
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                Result += line + "\r\n";
            }
            return Result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
