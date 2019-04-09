package com.runn;

 

import com.TestMysql;
import com.TicletTab;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MathTicket {

    private static TestMysql testMysql;
    double[] multiplying = new double[]{9900d, 220d, 110d, 82.5d, 13.75d, 9.166d, 1.964d, 3.409d};
    String[] detailS = new String[]{"五条", "炸弹", "葫芦", "顺子", "三条", "两对", "单对", "散号"};

    public MathTicket() {

    }

    public static void main(String[] s) {
        MathTicket mathTicket = new MathTicket();
        testMysql = new TestMysql("ticket_data_vr_1_1");
//        for (int i = 0; i < ; i++) {
//        }
        // 25/ 5
        for (int i = 7; i >=0 ; i--) {
            mathTicket.start(20190402, i);
        }
    }

    private void start(long date , int inde) {
        System.err.println("\n"+date);
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date ='"+date+"'  ORDER BY date DESC ,  periods DESC";
        ResultSet resultSet = testMysql.queryData(str);
        try {
            List<TicletTab> ticletTabList = new ArrayList<>();
            List<DataTask.Info> infoList = new ArrayList<>();

            while (resultSet.next()) {
                DataTask.Info info = new DataTask.Info();
                TicletTab ticletTab = new TicletTab();
//
//                ticletTab.setTouzhu_wutiao(resultSet.getInt(10));
//                ticletTab.setTouzhu_zadan(resultSet.getInt(11));
//                ticletTab.setTouzhu_hulu(resultSet.getInt(12));
//                ticletTab.setTouzhu_shunzi(resultSet.getInt(13));
//                ticletTab.setTouzhu_santiao(resultSet.getInt(14));
//                ticletTab.setTouzhu_liangdui(resultSet.getInt(14));
//                ticletTab.setTouzhu_dandui(resultSet.getInt(16));
//                ticletTab.setTouzhu_sanhao(resultSet.getInt(17));
//                ticletTab.setWutiao_zhongjiang_yingli(resultSet.getDouble(2));
//                ticletTab.setWutiao_zhongjiang_kuisun(resultSet.getDouble(18));
//                ticletTab.setZadan_zhongjiang_yingli(resultSet.getDouble(3));
//                ticletTab.setZadan_zhongjiang_kuisun(resultSet.getDouble(19));
//                ticletTab.setHulu_zhongjiang_yingli(resultSet.getDouble(4));
//                ticletTab.setHulu_zhongjiang_kuisun(resultSet.getDouble(20));
//                ticletTab.setShunzi_zhongjiang_yignli(resultSet.getDouble(5));
//                ticletTab.setShunzi_zhongjiang_kuisun(resultSet.getDouble(21));
//                ticletTab.setSantiao_zhongjiang_yingli(resultSet.getDouble(6));
//                ticletTab.setSantiao_zhongjiang_kuisun(resultSet.getDouble(22));
//                ticletTab.setLiangdui_zhongjiang_yingli(resultSet.getDouble(7));
//                ticletTab.setLiangdui_zhongjiang_kuisun(resultSet.getDouble(23));
//                ticletTab.setDandui_zhongjiang_yingli(resultSet.getDouble(8));
//                ticletTab.setDandui_zhongjiang_kuisun(resultSet.getDouble(24));
//                ticletTab.setSanhao_zhongjiang_yingli(resultSet.getDouble(9));
//                ticletTab.setSanhao_zhongjiang_kuisun(resultSet.getDouble(25));
//                ticletTabList.add(ticletTab);
                info.periods = resultSet.getString(1);
                info.date = resultSet.getString(2);
                info.number = resultSet.getString(3);
                info.order = resultSet.getString(4);
                info.location = resultSet.getInt(5);
                info.detail = resultSet.getString(6);
//                System.err.println("" + info.toString());
                infoList.add(info);
            }
//            match01(ticletTabList);
            match02(infoList , inde);

//            match04(infoList);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    //三条之后 单对的长度
    //
    private void match04(List<DataTask.Info> infoList) {
        Map<Integer, Integer> singleFot = new HashMap();
        Map<Integer, Integer> scattered = new HashMap();
        Map<Integer, Integer> meanwhileMap = new HashMap();
        int count =0 ;
        int tcount =0 ;

        for (int i = 0; i < infoList.size(); i++) {
            DataTask.Info info = infoList.get(i);
            if (info.location != 4)
                continue;
            tcount++ ;
            for (int j = i; j < infoList.size(); j++) {
                DataTask.Info info1 = infoList.get(j);
                if (info1.location == 6) {
                    if (singleFot.containsKey(j - i))
                        singleFot.put(j - i, singleFot.get(j - i) + 1);
                    else
                        singleFot.put(j - i, 1);
                    break;
                }

                for (int k = i; k < infoList.size(); k++) {
                    DataTask.Info info2 = infoList.get(k);
                    if (info2.location == 6) {
                        if (scattered.containsKey(k - i))
                            scattered.put(k - i, scattered.get(k - i) + 1);
                        else
                            scattered.put(k - i, 1);
                        break;
                    }
                }
                for (int k = 0;k < infoList.size()-1; k++) {

                    DataTask.Info info2 = infoList.get(k);
                    if (( info2.location == 6  && infoList.get(k+1).location == 4)|| (info2.location ==4 || infoList.get(k+1).location ==6)  ) {
                       count++;
                        break;
                    }

                }
            }
        }


        System.err.println("三条之后单对的间隔统计 " + singleFot.toString());
        System.err.println("三条之后散号的间隔统计 " + scattered.toString());
        System.err.println("三条之后两位散号和单对同时存在的统计 " + count  +" 三条出现的次数  "+tcount +"  几率 "+ count/(float)tcount*100 +" %" );
    }


    private void match01(List<TicletTab> ticletTabList) {

        for (TicletTab tab : ticletTabList) {

            int[] ints = new int[2];
            int[] ints2 = new int[]{7, 6};
            double[] ints3 = new double[2];

            int touzhu_sanhao = tab.getTouzhu_sanhao();
            int touzhu_dandui = tab.getTouzhu_dandui();
            ints[0] = touzhu_dandui;
            ints[1] = touzhu_sanhao;


            double sanhao = tab.getSanhao_zhongjiang_yingli();
            double dandui = tab.getDandui_zhongjiang_yingli();
            ints3[0] = sanhao;
            ints3[1] = dandui;

            System.err.println(" touzhu_sanhao：" + touzhu_sanhao + " touzhu_dandui " + touzhu_dandui + " sanhao " + sanhao + " dandui:" + dandui + " 收益比为：sanhao:" + sanhao * 1f / (touzhu_sanhao + touzhu_dandui) + " dandui : " + dandui * 1f / (touzhu_sanhao + touzhu_dandui));
        }
    }


    private void match02(List<DataTask.Info> infoList , int inde) {
            match03(infoList, inde);
    }

    private void match03(List<DataTask.Info> infoList, int i) {
        List<DataTask.Info> recodeList =new ArrayList<>();
        Info info1 = new Info();
        int lastIndex = -1;
//        System.err.println("" + i);
        List<Integer> interS = new ArrayList<>();
        Map<Integer, CountInfo> countMap = new HashMap<>();
        int maxint = 0;
        int count = 0;
        for (int j = 0; j < infoList.size(); j++) {
            DataTask.Info info = infoList.get(j);
            if (info.location == i || j==infoList.size()-1) {
                count++;
                if (lastIndex != -1)
                    interS.add(j - lastIndex);

                if (j - lastIndex > maxint)
                    maxint = j - lastIndex;
                if (countMap.containsKey(j - lastIndex)) {
                    CountInfo countInfo = countMap.get(j - lastIndex);
                    countInfo.count+=1;
                    countMap.put(j - lastIndex, countInfo );
                } else {

                    CountInfo countInfo = new CountInfo();
//                    System.err.println("info1 " + info.toString());
                    countInfo.count+=1;
                    countInfo.info =info ;
                    countMap.put(j - lastIndex, countInfo);
                }
                lastIndex = j;
            }

        }
        info1.maxIntervl = maxint;
        info1.count = count;
        info1.location = i;
        info1.countMap = countMap;
        info1.total = infoList.size();
        
        System.err.println("info1 " + info1.toString());
        Iterator<Integer> iterator = countMap.keySet().iterator();
        while (iterator.hasNext()){
           
            Integer next = iterator.next();
            CountInfo countInfo = countMap.get(next);
//            System.err.println(" count " + next + " " + countInfo.toString());
        }

    }
    
    public class CountInfo {
        public     int count ;
        public   DataTask.Info info ;

        @Override
        public String toString() {
            return "CountInfo{" +
                    "count=" + count +
                    ", info=" + info +
                    '}';
        }
    }

    public class Info {

        // 出现次数
        // 最大间隔
        //间隔0 -最大 分别对应的 次数 /概率
        int total;
        int location;
        // 出现次数
        int count;
        // 最大间隔
        int maxIntervl;
        //
        Map<Integer, CountInfo> countMap;

        @Override
        public String toString() {
            return "Info{" +
                    "location=" + location +
                    ", count=" + count +
                    ", maxIntervl=" + maxIntervl +
                    ", countMap=" + countMap +
                    '}';
        }
    }


}
