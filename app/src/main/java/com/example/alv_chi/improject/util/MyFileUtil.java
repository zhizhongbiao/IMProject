package com.example.alv_chi.improject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by idea on 2016/9/6.
 */
public class MyFileUtil {

    /**
     * 根据给定的文件路径获取其字节数组
     * @param context
     * @param dirPath
     * @param fileName
     * @return
     */
    public static byte[] getBytesFromFile(Context context,String dirPath, String fileName) {
        FileInputStream fis = null;
        try{
            //拿到目标文件
            if(!checkIfFileExists(dirPath, fileName)){
                Toast.makeText(context, "目标文件不存在！", Toast.LENGTH_SHORT).show();
                return null;
            }

            //从目标文件中读取数据
            File file = new File(dirPath + File.separator + fileName);
            fis = new FileInputStream(file);
            byte[] bytes = getBytesFromStream(context, fis);
            if(bytes==null){
                Toast.makeText(context, "没有得到数据", Toast.LENGTH_SHORT).show();
            }

            return bytes;

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "异常:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将给定的输入流灌装到给定路径的文件下
     * @param context
     * @param inputStream
     * @param dirPath
     * @param fileName
     * @return
     */
    public static boolean writeFile(Context context, InputStream inputStream, String dirPath, String fileName) {

        //目标文件如果存在，清空先
        if (checkIfFileExists(dirPath, fileName)) {
            new File(dirPath + File.separator + fileName).delete();
        }

        //重新建立目标文件
        File dirFile = createDir(dirPath);
        if (dirFile == null) {
            return false;
        }
        File file = createFile(context, dirFile, fileName);
        if (file == null) {
            return false;
        }

        byte[] videoBytes = getBytesFromStream(context, inputStream);//1、将输入流转化为bytes
        if(videoBytes==null){
            return false;
        }

        return writeFile(file, videoBytes, false);//向文件中填装字节数组

    }

    /**
     * 从输入流中获取字节数组
     * @param context
     * @param inputStream
     * @return
     */
    public static byte[] getBytesFromStream(Context context, InputStream inputStream) {
        try {
            //1、将输入流转化为bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);//将小水桶中的内容倒入大水缸
            }
            byte[] videoBytes = baos.toByteArray();

            return videoBytes;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    /**
     * 根据给定的图片路径得到Bitmap对象
     *
     * @param context
     * @param dirPath
     * @param fileName
     * @return
     */
    public static Bitmap readBitmap(Context context, String dirPath, String fileName) {
        FileInputStream fis = null;
        try {
            //定位目标文件
            boolean exists = checkIfFileExists(dirPath, fileName);
            if (!exists) {
                Toast.makeText(context, "目标文件不存在！", Toast.LENGTH_SHORT).show();
                return null;
            }

            File file = new File(dirPath + File.separator + fileName);
            //读入内容并转化为图片
            fis = new FileInputStream(file);

            return BitmapFactory.decodeStream(fis);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkIfFileExists(String dirPath, String fileName) {
        return new File(dirPath + File.separator + fileName).exists();
    }

    /**
     * 将Bitmap对象存储到指定的路径下
     *
     * @param context
     * @param bitmap
     * @param dirPath
     * @param fileName
     * @param format
     * @return
     */
    public static boolean writeBitmap(Context context, Bitmap bitmap, String dirPath, String fileName, Bitmap.CompressFormat format) {
        FileOutputStream fos = null;
        try {
            //定义写出的目标文件
            File file = null;
            //创建目标文件夹
            File dirFile = createDir(dirPath);
            if (dirFile == null) {
                return false;
            }

            //创建目标文件
            file = createFile(context, dirFile, fileName);
            if (file == null) {
                return false;
            }

            //向目标文件输出内容
            fos = new FileOutputStream(file, false);
            bitmap.compress(format, 100, fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文本路径读出文本内容
     *
     * @param context
     * @param dirPath
     * @param fileName
     * @return
     */
    public static String readText(Context context, String dirPath, String fileName) {
        FileInputStream fis = null;
        try {
            File file = null;
            //创建目标文件夹
            File dirFile = createDir(dirPath);
            if (dirFile == null) {
                return null;
            }

            //创建目标文件
            file = createFile(context, dirFile, fileName);
            if (file == null) {
                return null;
            }

            //读入文件内容
            fis = new FileInputStream(file);
            return decodeStream(context, fis);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "发生了一些异常...", Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从输入流中拿取字符串
     *
     * @param context
     * @param fis
     * @return
     */
    public static String decodeStream(Context context, FileInputStream fis) {
        try {
            byte[] bytes = new byte[1024];
            int len = -1;
            String result = "";
            while ((len = fis.read(bytes)) != -1) {
                String s = new String(bytes, 0, len);
                result += s;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "decodeStream时发生异常" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 写入文本
     *
     * @param context
     * @param dirPath
     * @param fileName
     * @param text
     * @param append
     * @return
     */
    public static boolean writeText(Context context, String dirPath, String fileName, String text, boolean append) {
        //创建目标文件夹
        File dirFile = createDir(dirPath);
        if (dirFile == null) {
            Toast.makeText(context, "创建文件夹失败！", Toast.LENGTH_SHORT).show();
            return false;
        }

        //创建文本文件
        File file = createFile(context, dirFile, fileName);
        if (file == null) {
            Toast.makeText(context, "创建目标文件失败！", Toast.LENGTH_SHORT).show();
            return false;
        }

        //向文件中写入数据
        byte[] bytes = new String(text).getBytes();
        return writeFile(file, bytes, append);
    }

    /**
     * 向目标文件写入字节数组
     *
     * @param file
     * @param bytes
     * @param append
     * @return
     */
    public static boolean writeFile(File file, byte[] bytes, boolean append) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 在给定目录中创建目标文件
     *
     * @param fileName
     * @param dirFile
     * @return
     */
    public static File createFile(Context context, File dirFile, String fileName) {
        try {
            File file = new File(dirFile.getAbsolutePath() + File.separator + fileName);
            if (!file.exists() || file.isDirectory()) {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "创建目标文件失败！", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 根据给定的路径创建文件夹
     *
     * @param dirPath
     * @return
     */
    public static File createDir(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            boolean success = dirFile.mkdirs();
            if (!success) {
                return null;
            }
        }

        return dirFile;
    }

}
