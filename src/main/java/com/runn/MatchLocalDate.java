package com.runn;

import baidu.Ecai.DoPour_1_1;
import baidu.Ecai.Main;
import baidu.Ecai.MatchWin;
import baidu.bean.PourInfo;
import baidu.utils.Out;
import com.CoreMath;
import com.TestMysql;
import matchore.MatchBig;
import matchore.MatchCore;
import subweb.subRun.Sql.VR1_1Insert;

import java.sql.ResultSet;
import java.util.*;

public class MatchLocalDate {

    private TestMysql testMysql;
    private VR1_1Insert vr1_1Insert;

    public MatchLocalDate() {
        testMysql = new TestMysql("ticket_data_vr_1_1");
        vr1_1Insert = new VR1_1Insert(testMysql);
    }

    public static void main(String[] args) {
        new MatchLocalDate().run();
    }

    private void run() {

        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date >'20190401'  ORDER BY date DESC ,  periods DESC";

        ResultSet resultSet = testMysql.queryData(str);
        try {
            List<DataTask.Info> infoList = new ArrayList<>();

            while (resultSet.next()) {
                DataTask.Info info = new DataTask.Info();
                info.periods = resultSet.getString(1);
                info.date = resultSet.getString(2);
                info.number = resultSet.getString(3);
                info.order = resultSet.getString(4);
                info.location = resultSet.getInt(5);
                info.detail = resultSet.getString(6);
//                System.err.println("" + info.toString());
                infoList.add(info);
            }


//            for (int i = 0; i < 8; i++) {
//              
//            }
            int dta = 6;
//            Map<Integer, Integer> integerIntegerMap = matchA(infoList, dta);
//            Out.e(" inde:" + dta, integerIntegerMap.toString());
//            matchWin(integerIntegerMap, dta);

            for (int i = 0; i < MatchCore.indedeS.length; i++) {
                Out.e(" \n  》》》》》》》》》》》》》》》》》》》》》 " + MatchCore.detailS[i]);
//                DataInfo dataInfo = matchD(infoList, i);
//                Out.e(dataInfo.toString());

                // 获取数据连续出现次数 对应的数量
                matchE(infoList, i);
            }

//         

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void matchE(List<DataTask.Info> infoList, int i) {
            
        Map<Integer , Integer> countMap =new HashMap<>();
     

        for (int i1 = 0; i1 < infoList.size(); i1++) {
            DataTask.Info info = infoList.get(i1);
            if (info.location != i)
                continue;
            for (int i2 = i1+1; i2 < infoList.size(); i2++) {
                DataTask.Info info1 = infoList.get(i2);
                if (info1.location!=i){
                    List<MatchBig.GroupInfo> groupInfos = new ArrayList<>();
                    int i3 = i2 - i1;
                   if ( countMap.containsKey(i3)){
                       countMap.put(i3,countMap.get(i3)+1);
                   }else {
                       countMap.put(i3,1);
                   }

                    MatchBig.groupS(infoList, groupInfos, i1);
                   Out.e(MatchCore.detailS[i]+" 连续 "+ i3+ " 出现后");
                    for (MatchBig.GroupInfo groupInfo : groupInfos) {
                        Out.e(groupInfo.toString());
                    }

                    i1 =i2 ;
                   break;
                }
            }
            
        }

     
    }

    int index = 0;
    double totalMoney = 0;
    double winMoney = 0;

    private void matchWin(Map<Integer, Integer> integerIntegerMap, int i) {
        Main.Info info3 = new Main.Info();
        info3.account = "wtw960424";
        info3.password = "953934cap";
        info3.indede = i;
        info3.ticketKind = 1;
        info3.tag = "_San_tiao_0424_";
        info3.dub = 3;
        info3.mulripe = 1;
        info3.precent = 0.6;
        info3.minIss = 70;
        info3.expectW = 20;
        info3.money = 5000;
        info3.ticketKind = 3;
        info3.stopLoss = 1;
        info3.rongDuanPre = 1;
        info3.decreasP = 0.05;

        DoPour_1_1 doPour_1_1 = null;
        try {
            doPour_1_1 = new DoPour_1_1(null, info3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<Integer> iterator = integerIntegerMap.keySet().iterator();

        while (iterator.hasNext()) {
            Integer next = iterator.next();
            Integer integer = integerIntegerMap.get(next);
            // 目前出现间隔期数

            Out.e(" 处理" + next, next + "");
            List<Long> longs = doPour_1_1.mathPriods(0 + "", 0);
            Out.e(" 得到投注", longs.toString() + "");

            Map<Long, PourInfo> longPourInfoMap = doPour_1_1.matchBetAmount02(longs);

            Out.e(longPourInfoMap.toString());

            PourInfo pourInfo = null;

            Iterator<Long> iterator1 = longPourInfoMap.keySet().iterator();
            while (iterator1.hasNext()) {
                Long next1 = iterator1.next();
                if (next1.longValue() == next) {
                    pourInfo = longPourInfoMap.get(next1);
                    break;
                }
            }

            if (pourInfo == null) {
                continue;
//                List<PourInfo> pourInfos = new MatchWin(doPour_1_1.mults).matchWin(MatchWin.creatMatchInfo(info3), next);
//                winMoney-= pourInfos.get(pourInfos.size()-1).total* doPour_1_1.dubS[info3.dub];
//                Out.e(next+" 当期亏损："  +pourInfos.get(pourInfos.size()-1).total* doPour_1_1.dubS[info3.dub]);
            }

            winMoney += pourInfo.win * MatchCore.dubS[info3.dub] * integer;

            Out.e(integer + " 当期总投入：" + pourInfo.total * MatchCore.dubS[info3.dub] + " 当期盈利：" + pourInfo.win * MatchCore.dubS[info3.dub] + " 同类型 盈利 " + pourInfo.win * MatchCore.dubS[info3.dub] * integer);

        }

        Out.e("总盈利为：" + winMoney);


    }

    private Map<Integer, Integer> matchA(List<DataTask.Info> infoList, int i) {

        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i1 = 0; i1 < infoList.size() - 1; i1++) {
            DataTask.Info info = infoList.get(i1);
            if (info.location != i && i1 != 0)
                continue;

            for (int i2 = i1 + 1; i2 < infoList.size(); i2++) {
                DataTask.Info info2 = infoList.get(i2);
                if (info2.location == i) {
                    int i3 = i2 - i1 - 1;

                    if (inMap.containsKey(i3)) {
                        inMap.put(i3, inMap.get(i3) + 1);
                    } else {
                        inMap.put(i3, 1);
                    }
                    i1 = i2;
                }
            }
        }


        return inMap;
    }


    /**
     * d得到历史列表中  指定类目的 连续出现的次数对应的所有数据
     *
     * @param infoList
     * @param location
     * @return
     */

    private DataInfo matchD(List<DataTask.Info> infoList, int location) {
        return matchD(infoList, 0, infoList.size(), location);
    }

    private DataInfo matchD(List<DataTask.Info> infoList, int start, int endIndex, int location) {

        DataInfo dataInfo = new DataInfo();
        dataInfo.totalCount = infoList.size();
        ArrayList<MatchBig.GroupInfo> groupInfos = new ArrayList<>();

        for (int i = start; i < endIndex - 1; i++) {
            DataTask.Info info = infoList.get(i);
            if (info.location != location)
                continue;

            for (int i1 = i + 1; i1 < endIndex; i1++) {
                DataTask.Info info1 = infoList.get(i1);
                if (info1.location != location) {
                    int i2 = i1 - i;

                    List<DataContinInfo> dataContinInfos;
                    if (dataInfo.nCountMap.containsKey(i2)) {
                        dataContinInfos = dataInfo.nCountMap.get(i2);

                        for (int i3 = i; i3 < i1; i3++) {
                            DataContinInfo dataContinInfo = new DataContinInfo();
                            dataContinInfos.add(dataContinInfo);
                            dataContinInfo.count = i2;
                            dataContinInfo.startIndex = i;
                            dataContinInfo.endIndex = i1;
                            dataContinInfo.infoList.add(infoList.get(i3));
                        }
                    } else {
                        dataContinInfos = new ArrayList<>();
                        dataInfo.nCountMap.put(i2, dataContinInfos);
                    }


                    MatchBig.groupS(infoList, groupInfos, i1);


                    i = i1 + 1;
//                    Map<Integer, Integer> countmap = new HashMap<>();
//                    for (int i3 = 0; i3 < endIndex; i3++) {
//                        DataTask.Info info2 = infoList.get(i3);
//                        if (info2.location == location) {
//                            break;
//                        } else {
//                            if (countmap.containsKey(info2.location)) {
//                                countmap.put(info2.location, countmap.get(info2.location) + 1);
//                            } else {
//                                countmap.put(info2.location, 1);
//                            }
//                        }
//                      dataInfo.interCountMap.put(i2 , countmap);
//                    }

                    break;
                } else {
                    dataInfo.count++;
                    dataInfo.infoList.add(info1);
                }
            }

        }

        for (MatchBig.GroupInfo groupInfo : groupInfos) {
            Out.d(groupInfo.toString());
        }

        return dataInfo;
    }

    public static class DataContinInfo {


        public int count;
        // 对应的数据集合
        public List<DataTask.Info> infoList = new ArrayList<>();
        int startIndex;
        int endIndex;

//        @Override
//        public String toString() {
//            return " DataContinInfo{" +
//                    "count=" + count +
//                    ", infoList=" + infoList.size() +
//                    ", startIndex=" + startIndex +
//                    ", endIndex=" + endIndex +
//                    '}';
//        }
    }


    public static class DataInfo {

        //连续n次对应的数量
        public Map<Integer, List<DataContinInfo>> nCountMap = new HashMap<>();
        //连续n次之后再出现间隔m次的数量
        public Map<Integer, Map> interCountMap = new HashMap<>();
        // 间隔m次之间对应其他值的出现次数
        public Map[] indeMap = new Map[8];
        //出现的次数
        public int count;
        //数据总次数
        public int totalCount;
        // 对应的数据集合
        public List<DataTask.Info> infoList = new ArrayList<>();

        // 两条数据之间的数据集合
        public List<DataTask.Info> interinfoList = new ArrayList<>();

        public List<Integer> indexS = new ArrayList<>();

        @Override
        public String toString() {
            return "DataInfo{" +
                    interCountMap.toString() +
                    ", count=" + count +
                    ", totalCount=" + totalCount +
                    ", indexS=" + indexS +
                    '}';
        }


    }


}
