package com.fast;

import com.CoreMath;
import com.runn.DataTask;
import com.analyze.ArticleAnalyze;
import com.ejin.quickhttp.QuickClient;
import com.ejin.quickhttp.StringCallback;
import com.google.gson.Gson;
import com.time.MyTimerTask;
import com.time.TicketTime;
import matchore.MatchCore;
import okhttp3.OkHttpClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FastAllColor {
    String[] detailS = new String[]{"五条", "炸弹", "葫芦", "顺子", "三条", "两对", "单对", "散号"};

    static OkHttpClient okHttpClient;
    private static FastAllColor fastAllColor;
    private final FastMysql fastMysql;
    private final FastInsert iinsert;

    public FastAllColor() {
        fastMysql = new FastMysql("fast_ticket");
        iinsert = new FastInsert(fastMysql);

    }

    static int count = 0;

    static boolean runn =false ;
    public static void main(String[] a) {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();

        fastAllColor = new FastAllColor();
        
        try {
            new TicketTime().test(new MyTimerTask.TimeCAll() {
                @Override
                public void onTime(int postion, long nexTime, long time) {
                    count++;
                    if (count >= 5) {
                        if (!runn)
                        fastAllColor.test01();
                        
                        count = 0;
                    }
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void test01() {
        runn =true ;
        String url = "http://www.6618222.com/frontend/v1/lottery/trend";
//        try {
//            FormBody.Builder builder = new FormBody.Builder();
//            
//            builder.add("lottery", "16");
//            builder.add("pageSize", "100");
//
//            Request.Builder builder1 = new Request.Builder();
//            builder1.addHeader("Content-Type", "application/json");
//            builder1.addHeader("Transfer-Encoding", "chunked");
//            builder1.addHeader("Connection", "keep-alive");
//            builder1.addHeader("pragma", "no-cache");
//            builder1.addHeader("expires", "-1");
//            builder1.addHeader("Content-Encoding", "gzip");
//            builder1.addHeader("Vary", "Accept-Encoding");
//            
//            
//            Response response = okHttpClient.newCall(builder1.url(url).post(builder.build()).build()).execute();
//
//            String string = response.body().string();
//            System.err.println("string:" +string);
//
//            Gson gson = new Gson();
//            Info info = gson.fromJson(string, Info.class);
//
//            System.err.println("info:" + info.toString());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        QuickClient quickClient = new QuickClient.Builder().build();
        String str = "{\"lotteryId\":\"16\",\"pageSize\":\"100000\"  } ";
        quickClient.post(url, (Object) str, new StringCallback() {
            @Override
            public void onSuccess(String s) {
                Gson gson = new Gson();
                Info info = gson.fromJson(s, Info.class);
//                System.err.println("info:" + info.toString());
                try {
                    insertSql(info);
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    runn =false ;
                    try {
                        fastAllColor.querydata();
                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                }
//                try {
//                    querydata();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onError(int code, String error) {
                runn =false ;
            }
        });

    }

    private void querydata() throws  Exception {

        ResultSet resultSet = fastMysql.prepareQuery().executeQuery(" select DISTINCT(issue),resultInfo ,sh,location,alie FROM fast_ticket  ORDER BY issue asc ");

        try {
            List<DataTask.Info> infoList = new ArrayList<>();
            while (resultSet.next()) {
                DataTask.Info info = new DataTask.Info();
                info.periods = resultSet.getString(1);
                info.number = resultSet.getString(2);
                info.detail = resultSet.getString(3);
                info.location = resultSet.getInt(4);
                info.alie = resultSet.getString(5);
                info.date = info.periods.substring(0,8);
                info.index =  Integer.parseInt(info.periods .substring(7,info.periods.length()));
                infoList.add(info);
            }
            resultSet.close();
//            System.err.println(infoList.size() + "  \n" + infoList.toString());
            match01(infoList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void match01(List<DataTask.Info> infoList) {

        List<ArticleAnalyze.MatchInfo> matchInfos = new ArrayList<>();
        for (int i = 0; i < infoList.size() - 1; i++) {
            DataTask.Info info = infoList.get(i);
            if (info.location == 6  ) {
                for (int j = i + 1; j < infoList.size(); j++) {
                    DataTask.Info info1 = infoList.get(j);
                    if (info1.location == 4) {

                        ArticleAnalyze.MatchInfo matchInfo = new ArticleAnalyze.MatchInfo();
                        matchInfo.info1 = info;
                        matchInfo.info2 = info1;
                        matchInfo.interval = Math.abs(j - i);
                        matchInfo.containMap = new HashMap<>();

                        List<ArticleAnalyze.MatchInfo> zdInfo = match(infoList, i, j, 1);
                        matchInfo.containMap.put(1, zdInfo);

                        List<ArticleAnalyze.MatchInfo> hlInfo = match(infoList, i, j, 2);
                        matchInfo.containMap.put(2, hlInfo);

                        List<ArticleAnalyze.MatchInfo> szInfo = match(infoList, i, j, 3);
                        matchInfo.containMap.put(3, szInfo);

                        List<ArticleAnalyze.MatchInfo> ldInfo = match(infoList, i, j, 5);
                        matchInfo.containMap.put(5, ldInfo);

                        List<ArticleAnalyze.MatchInfo> ddInfo = match(infoList, i, j, 6);
                        matchInfo.containMap.put(6, ddInfo);

                        List<ArticleAnalyze.MatchInfo> shInfo = match(infoList, i, j, 7);
                        matchInfo.containMap.put(7, shInfo);

                        List<ArticleAnalyze.MatchInfo> wtInfo = match(infoList, i, j, 0);
                        matchInfo.containMap.put(0, wtInfo);

                        matchInfos.add(matchInfo);
                        i = j - 1;
                        break;
                    }
                }
            }
        }
        Collections.sort( matchInfos);
        System.err.println("日期                第一期号     第二期号       数字         描述      间隔          包含");
        for (ArticleAnalyze.MatchInfo info : matchInfos) {
            if (Long.parseLong(info.info2.date) > Long.parseLong(info.info1.date))
                System.err.println("日期                第一期号     第二期号       数字         描述      间隔          包含");

            System.err.println(info.info2.date + "       " + info.info1.periods + " - " + info.info2.periods + "      " + info.info2.number + "       " + info.info2.detail + "       " + info.interval + "       " + logMap(info));
        }
        Collections.sort( matchInfos);
        for (ArticleAnalyze.MatchInfo matchInfo : matchInfos) {
            System.err.println( matchInfo.toString());
        }
    }


    private List<ArticleAnalyze.MatchInfo> match(List<DataTask.Info> infoList, int start, int end, int nu) {
        List<ArticleAnalyze.MatchInfo> matchInfos = new ArrayList<>();
        for (int k = start; k < end; k++) {
            DataTask.Info infoK = infoList.get(k);
//            System.err.println("  match " + nu +"  "+infoK .toString() );
            if (infoK.location == nu && !infoK.date.equals("20190314")) {
                for (int l = k + 1; l <= end; l++) {
                    DataTask.Info infoL = infoList.get(l);
//                    System.err.println("  match 2 " + nu +"  "+infoL .toString() );
                    if (infoL.location == nu) {
                        ArticleAnalyze.MatchInfo matchInfo = new ArticleAnalyze.MatchInfo();
                        matchInfo.info1 = infoK;
                        matchInfo.info2 = infoL;
                        matchInfo.interval = Math.abs(k - l);
                        matchInfos.add(matchInfo);

                        k = l;
                        break;
                    }

                }
            }
        }

        return matchInfos;
    }


    private String logMap(ArticleAnalyze.MatchInfo info) {

        StringBuilder builder = new StringBuilder();
        Iterator<Integer> iterator = info.containMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            List<ArticleAnalyze.MatchInfo> matchInfos = info.containMap.get(next);
            if (matchInfos.size() <= 0)
                continue;
            builder.append(detailS[next] + " " + (matchInfos.size() * 2) + " 次  ");

        }

        return builder.toString();
    }

    private void insertSql(Info info) throws SQLException {

        System.err.println("" + info.data.data.list.size());

        ResultSet resultSet = fastMysql.queryData("select * from " + fastMysql.getTableName() + "");
        if (resultSet.getFetchSize() > 0)
            while (resultSet.next()) {
                String string = resultSet.getString(4);
                for (int i = 0; i < info.data.data.list.size(); i++) {
                    Content content = info.data.data.list.get(i);
                  
                    if (content.issue.equals(string)) {
                        info.data.data.list.remove(content);
                        i--;
                    }
                }
            }
        resultSet.close();
        for (Content content : info.data.data.list) {
            try {
                content.openingTime=new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(content.openingTime));
                content.resultInfo = content.resultInfo.replace(",", "");
                content.location = CoreMath.mth(content.resultInfo)-1 ;
                content.alie = CoreMath.alaie( content.location );
                fastMysql.insertData(iinsert, content);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Info {
        public int code;
        public Data_01 data;
        public String message;
        public long sign;
        public String status;

        @Override
        public String toString() {
            return "Info{" +
                    "code=" + code +
                    ", data=" + data +
                    ", message='" + message + '\'' +
                    ", sign=" + sign +
                    ", status='" + status + '\'' +
                    '}';
        }


    }

    public static class Data_01 {
        public String current_page;
        public String first_page_url;
        public String from;
        public String last_page;
        public String last_page_url;
        public String next_page_url;
        public String path;
        public String per_page;
        public String to;
        public String total;
        public Data_ii data;

        @Override
        public String toString() {
            return "Data_01{" +
                    "current_page='" + current_page + '\'' +
                    ", first_page_url='" + first_page_url + '\'' +
                    ", from='" + from + '\'' +
                    ", last_page='" + last_page + '\'' +
                    ", last_page_url='" + last_page_url + '\'' +
                    ", next_page_url='" + next_page_url + '\'' +
                    ", path='" + path + '\'' +
                    ", per_page='" + per_page + '\'' +
                    ", to='" + to + '\'' +
                    ", total='" + total + '\'' +
                    ", data=" + data +
                    '}';
        }


    }

    public static class Data_ii {

        public Gather gather;
        public ArrayList<Content> list;

        @Override
        public String toString() {
            return "Data_02{" +
                    "gather=" + gather +
                    ", list=" + list +
                    '}';
        }
    }

    public class Gather {

    }

    public static class Content {

        String bw;//	大单
        String ds;//	单
        String dx;//		大
        String gw;//	大双
        String hs;//	对子
        String hz;//	31
        String issue;//		201903190040
        String lotteryId;//		16
        String lotteryName;//		ksssc
        String nn;//	没牛
        String openingTime;//	1552956613
        String qs;//	杂六
        String qw;//	大单
        String resultInfo;//	3,9,7,6,6
        String sh;//	一对
        String sw;//	大双
        String ww;//	小单
        String zs;//	半顺
        String alie;
        int location;
        

        @Override
        public String toString() {
            return "Content{" +
                    "bw='" + bw + '\'' +
                    ", ds='" + ds + '\'' +
                    ", dx='" + dx + '\'' +
                    ", gw='" + gw + '\'' +
                    ", hs='" + hs + '\'' +
                    ", hz='" + hz + '\'' +
                    ", issue='" + issue + '\'' +
                    ", lotteryId='" + lotteryId + '\'' +
                    ", lotteryName='" + lotteryName + '\'' +
                    ", nn='" + nn + '\'' +
                    ", openingTime='" + openingTime + '\'' +
                    ", qs='" + qs + '\'' +
                    ", qw='" + qw + '\'' +
                    ", resultInfo='" + resultInfo + '\'' +
                    ", sh='" + sh + '\'' +
                    ", sw='" + sw + '\'' +
                    ", ww='" + ww + '\'' +
                    ", zs='" + zs + '\'' +
                    '}';
        }
    }

}
