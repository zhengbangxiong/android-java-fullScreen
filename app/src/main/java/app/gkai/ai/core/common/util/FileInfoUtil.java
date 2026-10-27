package app.gkai.ai.core.common.util;


import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * @author YPF
 */
public class FileInfoUtil {

//    /**
//     * 根据文件路径获取文件头前两个字节
//     *
//     * @param filePath 文件路径
//     * @return 文件头前两个字节信息
//     */
//    public static void copyAssets (Context context) {
//        String value = null;
//        try{
//            String[] fileNames=context.getAssets().list("/animation");
//            if (fileNames.length > 0) {//如果是目录
//                File file = new File(newPath);
//                file.mkdirs();//如果文件夹不存在，则递归
//                for (String fileName : fileNames) {
//                    copyFilesFassets(context,fileName);
//                }
//            } else {//如果是文件
//                InputStream is = context.getAssets().open(oldPath);
//                FileOutputStream fos = new FileOutputStream(new File(newPath));
//                byte[] buffer = new byte[1024];
//                int byteCount=0;
//                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
//                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
//                }
//                fos.flush();//刷新缓冲区
//                is.close();
//                fos.close();
//            }
//
//
//
//        } catch (Exception e) {
//            Log.e("FileInfoUtil", filePath);
//            e.printStackTrace();
//        }
//        return value;
//    }
//
//
//    /**
//     * 将assets文件夹下的文件拷贝到/data/data/下     *
//     */
//    private static void copyFilesFassets(Context context, String fileName) {
//        InputStream in = null;
//        FileOutputStream out = null;
//        String path = "/data/data/" + context.getPackageName() + "/animation/";
//        File file = new File(path + fileName);
//
//        //创建文件夹
//        File filePath = new File(path);
//        if (!filePath.exists())
//            filePath.mkdirs();
//
//        if (file.exists())
//            return;
//
//        try {
//            in = context.getAssets().open(fileName); // 从assets目录下复制
//            out = new FileOutputStream(file);
//
//            int headerLen=0;
//            byte[] hexStringToBytes = hexStringToBytes("40");
//
//            int length = -1;
//            byte[] buf = new byte[1024];
//            while ((length = in.read(buf)) != -1) {
//                if(headerLen<1){
//                    ++headerLen;
//                    out.write(hexStringToBytes,0,hexStringToBytes.length);
//                }else{
//                    out.write(buf, 0, length);
//                }
//            }
//            out.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) in.close();
//                if (out != null) out.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//
//
//    private static void modifyAssets(Context context) {
//        InputStream in = null;
//        String path = "/data/data/" + context.getPackageName() + "/animation/";
//        FileOutputStream out = null;
//        File file = new File(path + fileName);
//
//        //创建文件夹
//        File filePath = new File(path);
//        if (!filePath.exists())
//            copyAssets(context);
//
//        try {
//            if(file.isDirectory()){
//                String[] fileNames= file.list();
//                for (String fileName : fileNames){
//                    in = context.getAssets().open(fileName); // 从assets目录下复制
//                    out = new FileOutputStream(path);
//                    File file=new File
//
//
//                    int length = -1;
//                    byte[] buf = new byte[1024];
//                    while ((length = in.read(buf)) != -1) {
//                        out.write(buf, 0, length);
//                    }
//                    out.flush();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) in.close();
//                if (out != null) out.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }



    public static File readGif(Context context){
        File file = null;
        try(
                InputStream is = context.getAssets().open("animation/ceshi.gif");
                ) {

            String header=getFileHeader(is,context);
            is.reset();
            if(header!=null && header.equals("4000")){
                is.skip(2);
            }


            file =File.createTempFile("111","22");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();





//            byte[] buffer = new byte[1024];
//            int byteCount=0;
//            while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
//                outputStream.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
//            }
//            return outputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;

        //                FileOutputStream fos = new FileOutputStream(new File(newPath));
        //                byte[] buffer = new byte[1024];
        //                int byteCount=0;
        //                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
        //                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
        //                }
        //                fos.flush();//刷新缓冲区
        //                is.close();
        //                fos.close();
    }




    public static byte[] readGifByte(Context context){

        try(
                InputStream is = context.getAssets().open("animation/mining_ceshi");
                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        ) {

            String header=getFileHeader(is,context);
            is.reset();
            if(header!=null && header.equals("4000")){
                is.skip(2);
            }

            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                outputStream.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            return outputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static InputStream readGif1(Context context){
        InputStream is;
        try{
            is = context.getAssets().open("animation/ceshi.gif");
            String header=getFileHeader(is,context);
            is.reset();
            if(header!=null && header.equals("4000")){
                is.skip(2);
            }
            return is;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改文件头的前两个字节
     * @param header
     * @param filePath
     * @throws Exception
     */
    public static void modifyFileHeader(byte[] header, String filePath, Context context) {
        if (header.length == 2) {
            try (
                RandomAccessFile src = new RandomAccessFile(filePath, "rw")) {
                int srcLength = (int)src.length();
                // 略过前两个字节
                src.skipBytes(2);
                byte[] buff = new byte[srcLength - 2];
                // 读取除前两个字节之后的字节
                src.read(buff);
                src.seek(0);
                src.write(header);
                src.seek(header.length);
                src.write(buff);
            } catch (Exception e) {
                Log.e("FileInfoUtil", filePath);
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文件路径获取文件头前两个字节
     *
     * @return 文件头前两个字节信息
     */
    public static String getFileHeader(InputStream is, Context context) {
        String value = null;
        try {
            byte[] b = new byte[2];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据文件路径获取文件头前两个字节
     *
     * @param filePath 文件路径
     * @return 文件头前两个字节信息
     */
    public static String getFileHeader(String filePath, Context context) {
        String value = null;
        try (
//                FileInputStream is = new FileInputStream(filePath)
                InputStream is = context.getAssets().open(filePath);
        ) {
            byte[] b = new byte[2];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            Log.e("FileInfoUtil", filePath);
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 将byte数组转换成string类型表示
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }

        return builder.toString();
    }

    /**
     * 将Hex String转换为Byte数组
     *
     * @param hexString the hex string
     * @return the byte [ ]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index  > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }




    public static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

}

