package com.runn;

import baidu.utils.Out;
import com.TestMysql;
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

        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date >'20190301'  ORDER BY date DESC ,  periods DESC";

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
            for (int i = 0; i < 8; i++) {
                matchA(infoList, i);
            }
      
            DataInfo dataInfo = matchD(infoList, 7);
            Out.e(dataInfo.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void matchA(List<DataTask.Info> infoList, int i) {

        Map<Integer ,Integer> inMap =new HashMap<>();
        for (int i1 = 0; i1 < infoList.size()-1; i1++) {
            DataTask.Info info = infoList.get(i1);
            if (info.location != i && i1!=0)
                continue;
            
            for (int i2 = i1+1; i2 < infoList.size(); i2++) {
                DataTask.Info info2= infoList.get(i2);
                if (info2.location==i){
                    int i3 = i2 - i1-1;
                            
                    if (inMap.containsKey(i3)){
                        inMap.put(i3,inMap.get(i3)+1);
                    }else {
                        inMap.put(i3,1);
                    }
                    i1 = i2  ;    
                }
            }
        }
        
        Out.e(inMap.toString());
 
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


        return dataInfo;
    }

    public static class DataContinInfo {


        public int count;
        // 对应的数据集合
        public List<DataTask.Info> infoList = new ArrayList<>();
        int startIndex;
        int endIndex;

        @Override
        public String toString() {
            return " \nDataContinInfo{" +
                    "count=" + count +
                    ", infoList=" + infoList.size() +
                    ", startIndex=" + startIndex +
                    ", endIndex=" + endIndex +
                    '}';
        }
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
                    "nCountMap=" + nCountMap.toString() +
                    ", count=" + count +
                    ", totalCount=" + totalCount +
                    ", infoList=" + infoList.size() +
                    ", indexS=" + indexS +
                    '}';
        }


    }


}
