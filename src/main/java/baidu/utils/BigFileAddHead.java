package baidu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BigFileAddHead {

    public static void main(String[] args) {

//        addHead2(" im is Head " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(System.currentTimeMillis())));
//        for (int i = 0; i < 10; i++) {
//            String file = "G:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\test.txt";
//            String cacheFile = "G:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\test.txt_cache";
//            try {
//                addHead(">>" + i, file, cacheFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        for (int i = 0; i < 10; i++) {
            addHead2(">>"+i);
        }

    }

    private static void addHead2(String str) {
        String filePath = "G:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\test.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            byte[] bytes = str.getBytes();
            RandomAccessFile rwd = new RandomAccessFile(file, "rwd");
            byte[] lastBuff = new byte[2*bytes.length];
            byte[] buff = new byte[2048];
            int lastBuffLen =0;
            int len ;
            int totalLen  =0;
           while ((len = rwd.read(buff))>0){
               if (bytes.length > totalLen){
                   // 数据长度未读完
                   if (bytes.length  >= totalLen + len){
                        // 数据长度足够长
                       rwd.seek(totalLen);
                       rwd.write(bytes,totalLen ,len);
                      System.arraycopy(buff ,0 , lastBuff , totalLen , len);
                      
                   }else {
                       // 数据长度有部分未读完
                       rwd.seek(totalLen);
                       rwd.write(bytes,totalLen ,bytes.length-totalLen);
                       
                       rwd.seek(bytes.length-1);
                       lastBuffLen = buff.length - (totalLen +len - bytes.length);
                       rwd.write(lastBuff, 0 , lastBuffLen);
                       System.arraycopy(buff ,0 , lastBuff , totalLen , len);
         
                   }
               }else {
                   // 数据长度已读完
                  
                   rwd.seek(totalLen);
                   rwd.write(lastBuff ,0,len);
                   System.arraycopy(buff ,0 , lastBuff , (totalLen - bytes.length) , len);
                   System.arraycopy(lastBuff,len , lastBuff ,0, bytes.length);
               }
               totalLen+=len ;
           }
//            rwd.seek(totalLen);
//            rwd.write(lastBuff ,(totalLen - bytes.length),len);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private static void addHead(String strHead, String destFilePath, String cacheFile) throws  Exception {

        File file = new File(destFilePath);
        File cache = new File(cacheFile);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } if (!cache.exists()) {
            cache.getParentFile().mkdirs();
            cache.createNewFile();
        }


        // 将282兆的文件内容头部添加一行字符  "This is a head!"

//        String srcFilePath = "big_file"; // 原文件路径
//        String destFilePath = "big_file_has_head"; // 添加头部后文件路径 （最终添加头部生成的文件路径）
        long startTime = System.currentTimeMillis();
        try {
            // 映射原文件到内存
            RandomAccessFile srcRandomAccessFile = new RandomAccessFile(cacheFile, "r");
            FileChannel srcAccessFileChannel = srcRandomAccessFile.getChannel();
            long srcLength = srcAccessFileChannel.size();
            System.out.println("src file size:" + srcLength);  // src file size:296354010
            MappedByteBuffer srcMap = srcAccessFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, srcLength);


            // 映射目标文件到内存
            RandomAccessFile destRandomAccessFile = new RandomAccessFile(destFilePath, "rw");
            FileChannel destAccessFileChannel = destRandomAccessFile.getChannel();
            long destLength = srcLength + strHead.getBytes().length;
            System.out.println("dest file size:" + destLength);  // dest file size:296354025
            MappedByteBuffer destMap = destAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, destLength);

            // 开始文件追加 : 先添加头部内容，再添加原来文件内容
            destMap.position(0);
            destMap.put(strHead.getBytes());
            destMap.put(srcMap);
            destAccessFileChannel.close();
            System.out.println("dest real file size:" + new RandomAccessFile(destFilePath, "r").getChannel().size());
            System.out.println("total time :" + (System.currentTimeMillis() - startTime));// 貌似时间不准确，异步操作？
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



