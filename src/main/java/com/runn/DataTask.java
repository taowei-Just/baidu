package com.runn;

import com.CoreMath;
import com.PattenUtil;
import com.TestMysql;
import com.TicketInfoInsert;
import com.ejin.quickhttp.QuickClient;
import niuniu.NiuNIuMatch;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class DataTask {

    private final TestMysql ticket_data_vr;
    private final TicketInfoInsert infoInsertVr;
    private final TestMysql ticket_data_vr_3;
    private final TicketInfoInsert infoInsertVr_3;
    String startperiods = "20190312047";
    String endperiods = "20190305028";

    String url = "https://zst.cjcp.com.cn/cjwssc/view/ssc_zst5-ssc.html?startqi=" + startperiods + "&endqi=" + endperiods + "&searchType=9";

    //post
    /**
     * ctl00_ctl00_ScriptManager1_HiddenField:AjaxControlToolkit, Version=4.1.60919.0, Culture=neutral, PublicKeyToken=28f01b0e84b6d53e::ee051b62-9cd6-49a5-87bb-93c07bc43d63:de1feab2:fcf0e993:f2c8e708:720a52bf:f9cec9bc:589eaa30:698129cf:fb9b4c57:ccb96cf9
     * __EVENTTARGET
     * __EVENTARGUMENT
     * __VIEWSTATE: /wEPDwULLTEyNjU4ODY5MzYPZBYCZg9kFgJmD2QWBAIBD2QWAgIHDxYCHgRocmVmBQ1jc3Mvc3R5bGUuY3NzZAIDD2QWDgIBDw8WBB4PQ29tbWFuZEFyZ3VtZW50BQJ6aB4EVGV4dAUM566A5L2T5Lit5paHZGQCAg8PFgQfAQUEYmlnNR8CBQznuYHpq5TkuK3mlodkZAIDDw8WBB8BBQJlbh8CBQdFbmdsaXNoZGQCBg8PFgQfAQUCemgfAgUM566A5L2T5Lit5paHZGQCBw8PFgQfAQUEYmlnNR8CBQznuYHpq5TkuK3mlodkZAIIDw8WBB8BBQJlbh8CBQdFbmdsaXNoZGQCCQ9kFgICAQ9kFgoCAQ8PFgIeCE9wZW5EYXRlBQoyMDE5LzAzLzEyZGQCAg8PFgIfAwUKMjAxOS8wMy8xMmRkAgMPDxYCHwMFCjIwMTkvMDMvMTFkZAIEDw8WAh8DBQoyMDE5LzAzLzEwZGQCBQ8PFgIfAwUKMjAxOS8wMy8xM2QWAmYPFgIeB1Zpc2libGVnFgYCAg8PFgIeB0VuYWJsZWRnZGQCAw8PFgIfBWdkZAIFDw8WAh8FZ2RkZB/wfxQItfficoudgtADZatT5r3EZgjl677UmHdhS3RW
     * __VIEWSTATEGENERATOR: DE20975B
     * __EVENTVALIDATION: /wEdAAz3J7NmJu6Rr53sunhOSKUlRgicPb4ZyES7Oqe+Va/BgVxMUgULda9b5s2iTor+5SnGX6xxQcGnfBYwui1WnGsaI+6z1WdqNz1soLNrdv9jVarCCun2wWnX0OmLWMs3OEV/FhoYnlgXEBb/vJmanGSKlL7HESpvs/UtmDyfzUwl/kPW04lT9bOPCFUc0nlziSjjXoxNgDLTY4F4gIsBUkaQxuTTIgIPyi3NpjhYjuet3e5bi9gPyzSfxUQzu0PGmw+VGw76Jx331aam3aYbrxU+why/SlCihL9XwgJyaTrK9g==
     * ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$TabName:Search
     * ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$MyCtrl4$txtDate: 2019/02/12
     * ctl00$ctl00$ContentPlaceHolder1$ContentPlaceHolder1$MyCtrl4$btnSearch: 查询
     */
    String vrurl = "https://numbers.videoracing.com/open_3_2.aspx";
    //    String vrurl = "https://numbers.videoracing.com/analy_3_1.aspx";
    String vrurl_3 = "https://numbers.videoracing.com/analy_1_1.aspx";


    private final OkHttpClient httpClient;
    private final TestMysql testMysql;
    private TicketInfoInsert infoInsert;
    private final QuickClient quick;

    public DataTask() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        httpClient = builder.build();
        testMysql = new TestMysql("ticket_data");
        ticket_data_vr = new TestMysql("ticket_data_vr");
        ticket_data_vr_3 = new TestMysql("ticket_data_vr_3");
        infoInsert = new TicketInfoInsert(testMysql);
        infoInsertVr = new TicketInfoInsert(ticket_data_vr);
        infoInsertVr_3 = new TicketInfoInsert(ticket_data_vr_3);
        quick = new QuickClient.Builder().connectTimeout(20 * 1000).build();
    }

    static boolean run = false;

    public static void main(String s[]) {
        final DataTask dataTask = new DataTask();
        dataTask.prepareUrl();
        final Timer[] timer = {new Timer()};
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (run)
                    return;
                run = true;
                boolean isError = false;
                dataTask.prepareUrl();
//                dataTask.test();
                try {
                    dataTask.test2();
//                    dataTask.test3();
                } catch (IOException e) {
                    isError=true ;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
       
                System.gc();
                run = false;

            }

        };
        timer[0].scheduleAtFixedRate(timerTask, 1 * 1000, 10 * 1000);
//        try {
//            new TicketTime().test(new MyTimerTask.TimeCAll() {
//                @Override
//                public void onTime(int postion, long nexTime, long time) {
//
//                    System.err.println(Thread.currentThread()+" 当前期数:" + postion +  " 下一期时间 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(nexTime))+" 当前时间 time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)) + " 倒计时：" + (nexTime-System.currentTimeMillis())/1000 + "s");
//
//                    if ((nexTime - System.currentTimeMillis()) / 1000  < 1) {
//                        System.err.println("刷新数据");
//                        timer[0].cancel();
//                        timer[0] =new Timer();
//                       
//                    }
//                }
//            });
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

//    private void test3()  throws IOException {
//            List<Info> list = new ArrayList<>();
//            Response execute = httpClient.newCall(new Request.Builder().url(vrurl_3).get().build()).execute();
//            String string = PattenUtil.replaceBlank(execute.body().string());
//            string = string.replace(" ", "");
//
//            String regex = "<divclass=\"tab-content\">(.*?)</section>";
//            List<String> subUtil = PattenUtil.getSubUtil(string, regex, true);
//            for (String s : subUtil) {
//                System.err.println("test3:" + s);
//                regex = "<divclass='css_tr(.*?)</div></div>";
//                List<String> subUtil2 = PattenUtil.getSubUtil(s, regex, true);
//                for (String st : subUtil2) {
//                    regex = "<divclass='css_td'>(.*?)</div>";
//                    List<String> subUtil12 = PattenUtil.getSubUtil(st, regex, false);
//                    System.err.println("test3 日期 :" + subUtil12.toString());
//                    regex = "<divclass='css_td3'>(.*?)</div>";
//                    List<String> subUtil23 = PattenUtil.getSubUtil(st, regex, false);
//                    System.err.println("test3 编号 :" + subUtil23.toString());
//
//                    regex = "<divclass='css_td2redbb'>(.*?)</div>";
//                    List<String> subUtil24 = PattenUtil.getSubUtil(st, regex, false);
//                    System.err.println("test3 号码 :" + subUtil24.toString());
//
//                    StringBuilder stringBuilder = new StringBuilder();
//                    for (String str : subUtil24)
//                        stringBuilder.append(str);
//
//                    try {
//                        Info info = new Info();
//                        info.order = subUtil23.get(0);
//                        info.periods = subUtil12.get(0).replace("/", "") + info.order;
//                        info.number = stringBuilder.toString();
//                        info.location = CoreMath.mth(stringBuilder.toString()) - 1;
//                        info.detail = CoreMath.detail(info.location);
//                        info.date = subUtil12.get(0).replace("/", "");
//                        list.add(info);
//                        System.err.println("test3 info :" + info.toString());
//                    } catch (Exception e) {
//
//                    }
//                }
//
//            }
//            for (int i = list.size() - 1; i >= 0; i--) {
//                Info info = list.get(i);
//                try {
//                    if (i < 100)
//                        writerLog(info.toString());
//                    ticket_data_vr_3.insertData(infoInsertVr_3, info);
//                } catch (SQLException e) {
//
//                }
//            }
//        
//    }

    private int checkglfun(String str) {

        if (str == null || str.length() < 5)
            return -1;

        char[] chars = str.toCharArray();

        int g = chars[0];
        int s = chars[1];
        int b = chars[2];
        int q = chars[3];
        int w = chars[4];

        if (g < s && s < b && b < q && q < w) {
            return 106; //顺子
        }
        int[] src = {g, s, b, q, w};
//            sort(src,src+5);


        int[] cnt = new int[20];
        cnt[0] = 0;

        int jump_t = 0;
        int jump_s = 0;
        int jump_f = 0;
        int jump_five = 0;
//            memset(cnt,0, sizeof(cnt));
        for (int i = 0; i < 5; i++) {
		/*if(++cnt[src[i]] == 2)
		{
			jump++;
		}	*/
            ++cnt[src[i]];

        }
        for (int i = 0; i < 20; i++) {
            if (cnt[i] == 2)
                jump_t++;
            if (cnt[i] == 3)
                jump_s++;
            if (cnt[i] == 4)
                jump_f++;
            if (cnt[i] == 5)
                jump_five++;
        }
        if (jump_t == 1 && jump_s != 1)
            return 101;//单对
        if (jump_t == 1 && jump_s == 1)
            return 108; //葫芦
        if (jump_t == 2)
            return 102; //两对
        if (jump_s == 1)
            return 103; //三条
        if (jump_f == 1)
            return 104; //炸掉
        if (jump_five == 1)
            return 105; //五条

        return 107;

    }

    private void test2() throws IOException {
        List<Info> list = new ArrayList<>();
//     
//       quick.get(vrurl, new StringCallback() {
//            @Override
//            public void onSuccess(String std) {
//               
//            }
//
//            @Override
//            public void onError(int code, String error) {
//
//            }
//        });
//        
        Response execute = httpClient.newCall(new Request.Builder().url(vrurl).get().build()).execute();
        String string = PattenUtil.replaceBlank(execute.body().string());
        string = string.replace(" ", "");

        String regex = "<divclass=\"tab-content\">(.*?)</section>";
        List<String> subUtil = PattenUtil.getSubUtil(string, regex, true);
        for (String s : subUtil) {
            System.err.println("test2:" + s);
            regex = "<divclass='css_tr(.*?)</div></div>";
            List<String> subUtil2 = PattenUtil.getSubUtil(s, regex, true);
            for (String st : subUtil2) {
                regex = "<divclass='css_td'>(.*?)</div>";
                List<String> subUtil12 = PattenUtil.getSubUtil(st, regex, false);
//                System.err.println("test2 日期 :" + subUtil12.toString());
                regex = "<divclass='css_td3'>(.*?)</div>";
                List<String> subUtil23 = PattenUtil.getSubUtil(st, regex, false);
//                System.err.println("test2 编号 :" + subUtil23.toString());

                regex = "<divclass='css_td2redbb'>(.*?)</div>";
                List<String> subUtil24 = PattenUtil.getSubUtil(st, regex, false);
//                System.err.println("test2 号码 :" + subUtil24.toString());

                StringBuilder stringBuilder = new StringBuilder();
                for (String str : subUtil24)
                    stringBuilder.append(str);

                try {
                    Info info = new Info();
                    info.order = subUtil23.get(0);
                    info.periods = subUtil12.get(0).replace("/", "") + info.order;
                    info.number = stringBuilder.toString();
                    info.location = CoreMath.mth(stringBuilder.toString()) - 1;
                    info.detail = CoreMath.detail(info.location);
                    info.date = subUtil12.get(0).replace("/", "");
                    int niuniu = NiuNIuMatch.matchNiu(info.number);
                    info.niuniu = (niuniu >-1? niuniu==0? "牛牛" : "牛" + niuniu : "无牛");
                    list.add(info);
                    System.err.println("test2 info :" + info.toString());
                } catch (Exception e) {

                }
            }

        }
        for (int i = list.size() - 1; i >= 0; i--) {
            Info info = list.get(i);
            try {
                if (i < 100)
                    writerLog(info.toString());
                ticket_data_vr.insertData(infoInsertVr, info);
            } catch (SQLException e) {

            }
        }
    }

    private void prepareUrl() {
        try {
            String s = infoInsert.queryMaxPro();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                System.err.println(" " + s);
                Date parse = dateFormat.parse(s.substring(0, 8));
                int day = parse.getDay();
                int month = parse.getMonth();
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(System.currentTimeMillis());
                int i = instance.get(Calendar.DAY_OF_MONTH);
                int m = instance.get(Calendar.MONTH);

                System.err.println("  " + day);
                System.err.println(" " + i);

                if (day < i || month < m) {
                    endperiods = dateFormat.format(new Date(System.currentTimeMillis())) + "060";
                }
                instance.add(Calendar.DAY_OF_MONTH, -1);
                startperiods = dateFormat.format(instance.getTime()) + "001";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        url = "https://zst.cjcp.com.cn/cjwssc/view/ssc_zst5-ssc.html?startqi=" + startperiods + "&endqi=" + endperiods + "&searchType=9";
        System.err.println(url);
    }

    private void test() {
        List<Info> list = new ArrayList();
        try {
            Response execute = httpClient.newCall(new Request.Builder().url(url).get().build()).execute();
            String string = PattenUtil.replaceBlank(execute.body().string());
            string = string.replace(" ", "");
            String regex = "<tbody.*?>(.*?)</tbody>";
            List<String> subUtil = PattenUtil.getSubUtil(string, regex, true);

//                writerLog(string );
            regex = "<tr.*?>(.*?)</tr>";
            for (String str : subUtil) {
                List<String> sub = PattenUtil.getSubUtil(str, regex, true);

                for (String st : sub) {
                    if (!st.contains("<tdclass='z_bg_05'>"))
                        continue;
//                    writerLog(st);
                    regex = "<tdclass='z_bg_05'>(.*?)</td>";
                    List<String> subUtil2 = PattenUtil.getSubUtil(st, regex, false);
//                    writerLog(subUtil2.toString());
//                    writerLog(st);
                    regex = "<tdclass='z_bg_13'>(.*?)</td>";
                    List<String> subUtil3 = PattenUtil.getSubUtil(st, regex, false);

                    try {
                        Info info = new Info();
                        info.order = subUtil2.get(0);
                        info.periods = subUtil2.get(1);
                        info.number = subUtil3.get(0);
                        info.location = CoreMath.mth(subUtil3.get(0)) - 1;
                        info.detail = CoreMath.detail(info.location);
                        info.date = info.periods.substring(0, 8);
                        int niuniu = NiuNIuMatch.matchNiu(info.number);
                        info.niuniu = (niuniu >-1? niuniu==0? "牛牛" : "牛" + niuniu : "无牛");
                        list.add(info);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Info info : list) {
            try {
                writerLog(info.toString());
                testMysql.insertData(infoInsert, info);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void writerLog(String s) {
        System.err.println(s);
        if (s != null)
            return;
        File file = new File("G:/Temp/log/log_cp.txt");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file, true);
            try {
                outputStream.write((s + "\n ").getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Info {

        public String order;
        public String number;
        public String periods;
        public int location;
        public String detail;
        public String date;
        public String niuniu;

        @Override
        public String toString() {
            return "Info{" +
                    "order='" + order + '\'' +
                    ", number='" + number + '\'' +
                    ", periods='" + periods + '\'' +
                    ", location=" + location +
                    ", detail='" + detail + '\'' +
                    ", date='" + date + '\'' +
                    ", niuniu='" + niuniu + '\'' +
                    '}';
        }
    }
}
