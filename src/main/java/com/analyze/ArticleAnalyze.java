package com.analyze;

import com.runn.DataTask;
import com.TestMysql;
import com.time.MyTimerTask;
import com.time.TicketTime;
import com.ui.OutUi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;

public class ArticleAnalyze {
    double[] multiplying = new double[]{9900d, 220d, 110d, 82.5d, 13.75d, 9.166d, 1.964d, 3.409d};
    String[] detailS = new String[]{"五条", "炸弹", "葫芦", "顺子", "三条", "两对", "单对", "散号"};
    private TestMysql testMysql;
    private Statement queryStatement;
    private static ArticleAnalyze articleAnalyze;
    static int count = 10;

    public static void main(String[] args) throws ParseException {
        articleAnalyze = new ArticleAnalyze();

        new TicketTime().test(new MyTimerTask.TimeCAll() {
            @Override
            public void onTime(int postion, long nexTime, long time) {
                count++;
                if (count < 10) {
                } else
                    try {
                        count = 0;
                        articleAnalyze.start();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
           

            }
        }, 1 * 1000, 20 * 1000);


    }


    public ArticleAnalyze() {
        testMysql = new TestMysql("ticket_data_vr");
    }

    private void start() throws SQLException {
        TestMysql ticket_data_vr = new TestMysql("ticket_data_vr");
        ResultSet resultSet = ticket_data_vr.prepareQuery().executeQuery("  select  *from ticket_data_vr   ORDER BY  date asc,ordern asc ");
    
        try {
            List<DataTask.Info> infoList = new ArrayList<>();
            while (resultSet.next()) {
                DataTask.Info info = new DataTask.Info();

                info.order = resultSet.getString(2);
                info.number = resultSet.getString(3);
                info.periods = resultSet.getString(4);
                info.location = resultSet.getInt(5);
                info.detail = resultSet.getString(6);
                info.date = resultSet.getString(7);
                infoList.add(info);
            }
//            System.err.println(infoList.size() + "  \n" + infoList.toString());
            match01(infoList);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            resultSet.close();
              resultSet = ticket_data_vr.prepareQuery().executeQuery("  select  max(periods) from ticket_data_vr  ");
            while (resultSet.next()) {
                System.err.println("当前：" +(Long.parseLong(resultSet.getString(1) )+1)+" 期" );
            }
            resultSet.close();
            ticket_data_vr.prepareQuery().cancel();
            ticket_data_vr.prepareQuery().close();
            ticket_data_vr.close();
        }
    }

    /**
     * @param infoList
     */

    public void match01(List<DataTask.Info> infoList) {

        List<MatchInfo> matchInfos = new ArrayList<>();
        for (int i = 0; i < infoList.size() - 1; i++) {
            DataTask.Info info = infoList.get(i);
            if (info.location == 4 && !info.date.equals(20190314)) {
                for (int j = i + 1; j < infoList.size(); j++) {
                    DataTask.Info info1 = infoList.get(j);
                    if (info1.location == 4) {

                        MatchInfo matchInfo = new MatchInfo();
                        matchInfo.info1 = info;
                        matchInfo.info2 = info1;
                        matchInfo.interval = Math.abs(j - i);
                        matchInfo.containMap = new HashMap<>();

                        List<MatchInfo> zdInfo = match(infoList, i, j, 1);
                        matchInfo.containMap.put(1, zdInfo);

                        List<MatchInfo> hlInfo = match(infoList, i, j, 2);
                        matchInfo.containMap.put(2, hlInfo);

                        List<MatchInfo> szInfo = match(infoList, i, j, 3);
                        matchInfo.containMap.put(3, szInfo);

                        List<MatchInfo> ldInfo = match(infoList, i, j, 5);
                        matchInfo.containMap.put(5, ldInfo);

                        List<MatchInfo> ddInfo = match(infoList, i, j, 6);
                        matchInfo.containMap.put(6, ddInfo);

                        List<MatchInfo> shInfo = match(infoList, i, j, 7);
                        matchInfo.containMap.put(7, shInfo);

                        List<MatchInfo> wtInfo = match(infoList, i, j, 0);
                        matchInfo.containMap.put(0, wtInfo);

                        matchInfos.add(matchInfo);
                        i = j - 1;
                        break;
                    }
                }
            }
        }

        System.err.println(" id    日期             第一期号       第二期号       数字         描述      间隔          包含");
        for (int i = 0; i < matchInfos.size(); i++) {
            MatchInfo info = matchInfos.get(i);
            if (Long.parseLong(info.info2.date) > Long.parseLong(info.info1.date))
                System.err.println(" id    日期             第一期号       第二期号       数字         描述      间隔          包含");
            System.err.println( i+"  "+info.info2.date + "       " + info.info1.periods + " - " + info.info2.periods + "      " + info.info2.number + "       " + info.info2.detail + "       " + info.interval + "       " + logMap(info));
        }
        
        ui(matchInfos);

    }

    private void ui(List<MatchInfo> matchInfos) {
        OutUi outUi = new OutUi();
        for (int i = 0; i < matchInfos.size(); i++) {
            if (!matchInfos.get(i) .info2.date .equals(matchInfos.get(i) .info1.date)){
                System.err.println("    …………………………………………………………"+matchInfos.get(i) .info2.date +"………………………………………………");
            }
            outUi.draw(i,matchInfos.get(i));
        }

    }

    private String logMap(MatchInfo info) {

        StringBuilder builder = new StringBuilder();
        Iterator<Integer> iterator = info.containMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            List<MatchInfo> matchInfos = info.containMap.get(next);
            if (matchInfos.size() <= 0)
                continue;
            builder.append(detailS[next] + " " + (matchInfos.size()  ) + " 次  ");

        }

        return builder.toString();
    }

    private List<MatchInfo>   match_mm(List<DataTask.Info> infoList, int start, int end, int nu) {
        List<MatchInfo> matchInfos = new ArrayList<>();
        for (int k = start; k < end; k++) {
            DataTask.Info infoK = infoList.get(k);
//            System.err.println("  match " + nu +"  "+infoK .toString() );
            if (infoK.location == nu && !infoK.date.equals("20190314")) {
                for (int l = k + 1; l <= end; l++) {
                    DataTask.Info infoL = infoList.get(l);
//                    System.err.println("  match 2 " + nu +"  "+infoL .toString() );
                    if (infoL.location == nu) {
                        MatchInfo matchInfo = new MatchInfo();
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
    
    
 private List<MatchInfo> match(List<DataTask.Info> infoList, int start, int end, int nu ) {
        List<MatchInfo> matchInfos = new ArrayList<>();
        for (int k = start; k < end; k++) {
            DataTask.Info infoK = infoList.get(k);
//            System.err.println("  match " + nu +"  "+infoK .toString() );
            if (infoK.location == nu && !infoK.date.equals("20190314")) {
                MatchInfo matchInfo = new MatchInfo();
                matchInfo.info1 = infoK;
                matchInfo.interval =  1;
                matchInfos.add(matchInfo);
            }
        }

        return matchInfos;
    }


    public static class MatchInfo {
        public DataTask.Info info1;
        public DataTask.Info info2;
        public int interval;
        public Map<Integer, List<MatchInfo>> containMap;

        @Override
        public String toString() {
            return "MatchInfo{" +
                    "info1=" + info1 +
                    ", info2=" + info2 +
                    ", interval=" + interval +
                    ", containMap=" + containMap +
                    '}';
        }
    }
}
