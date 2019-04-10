package baidu.utils;

import baidu.Ecai.Main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class LogUtils {

    public static void main(String[] args) {
        LogUtils logUtils = new LogUtils("G:\\Temp\\log\\"+  new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) +"\\test.txt");
        
        for (int i = 0; i < 1000; i++) {
            logUtils.saveLog2File("" + i );
        }
    }
    boolean isSave = true;
    String logFilePath;
    private final ExecutorService threadPool;
    public LogUtils(String logFilePath) {
        this.logFilePath = logFilePath;
        Out.e(logFilePath);
        File file = new File(logFilePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                new File(file.getParent() + "/" + file.getName() + "_buff").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        threadPool = Executors.newFixedThreadPool(5);
    }
    public synchronized void saveLog2File(String logStr) {
//        Main.pushAllMessage(logStr);
        threadPool.submit(new LogRunn(logStr));
    }
    private synchronized void saveLog(String logStr) {
        Out.d(logStr);
        if (!isSave)
            return;
        try {
            logStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S").format(new Date(System.currentTimeMillis())) + ">>>" + logStr + " \n";
//            RandomAccessFile randomAccessFile = new RandomAccessFile(logFilePath, "rwd");
//            randomAccessFile.seek(0);
//            randomAccessFile.write(logStr.getBytes());
//            randomAccessFile.close();
            File file = new File(logFilePath);
            File buffFile = new File(file.getParent() + "/" + file.getName() + "_buff");
            if (!buffFile.exists())
                buffFile.createNewFile();
            FileOutputStream buffOut = new FileOutputStream(buffFile);
            FileInputStream buffIn = new FileInputStream(buffFile);
            
            FileInputStream logInput = new FileInputStream(file);
    
            int len = 0;
            byte[] buff = new byte[20480];

            while ((len = logInput.read(buff)) > 0) {
                buffOut.write(buff, 0, len);
            }
            logInput.close();

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.flush();
            fileWriter.close();

            FileOutputStream logOut = new FileOutputStream(file, true);
            logOut.write(logStr.getBytes());

            while ((len = buffIn.read(buff)) > 0) {
                logOut.write(buff, 0, len);
            }
            buffOut.close();
            buffIn.close();
            logOut.close();
            buffIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class LogRunn implements Runnable {
        String logStr;
        public LogRunn(String logStr) {
            this.logStr = logStr;
        }

        @Override
        public void run() {
            saveLog(logStr);
        }
    }
}
