package com.algorithm.probabillity;

import baidu.utils.Out;
import com.TestMysql;
import com.runn.DataTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProbabilitySpecluate {

    private final TestMysql testMysql;

    public static void main(String[] args) {
        ProbabilitySpecluate specluate = new ProbabilitySpecluate();
        Calendar instance = Calendar.getInstance();
        instance.set(2019, 03, 01);
        int i1 = instance.get(Calendar.DAY_OF_YEAR);
        long timeInMillis = instance.getTimeInMillis();

        instance.setTimeInMillis(System.currentTimeMillis());
        int i2 = instance.get(Calendar.DAY_OF_YEAR);
        instance.setTimeInMillis(timeInMillis);

        for (int i = 0; i <= (i2 - i1); i++) {
            instance.add(Calendar.DAY_OF_YEAR, 1);
            Out.e("\n\n " + instance.getTime());
            specluate.start(new SimpleDateFormat("yyyyMMdd").format(instance.getTime()));
        }

    }

    public ProbabilitySpecluate() {
        testMysql = new TestMysql("ticket_data_vr_1_1");
    }


    private void start(String strd) {
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date ='" + strd + "'  ORDER BY date asc ,  periods asc";
        ResultSet resultSet = testMysql.queryData(str);
        List<DataTask.Info> infoList = new ArrayList<>();
        try {

            while (resultSet.next()) {
                DataTask.Info info = new DataTask.Info();
                info.periods = resultSet.getString(1);
                info.date = resultSet.getString(2);
                info.number = resultSet.getString(3);
                info.order = resultSet.getString(4);
                info.location = resultSet.getInt(5);
                info.detail = resultSet.getString(6);
                infoList.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int location = 4;
        Out.e("location " + location);
        Map<Integer, Integer> match01 = match01(infoList, location);
        out01(match01);
        Map<Integer, Match02Info> match02InfoMap = match02(infoList, location);
//        out02(match02InfoMap);
//
        Map<Integer, Match02Info> match03InfoMap = match03(infoList, location);
//        out03(match03InfoMap);
//
        List<Integer> integerList = match04(infoList, location, null);
//
//        Collections.sort(integerList);
//
//        if (integerList.size()>0) {
//            Out.e(integerList.size() + " \n " + integerList.toString());
//            List<DataTask.Info> list = match05(infoList, location, integerList.get(integerList.size() - 1)+4);
//
//            Out.e(" 算法漏网之鱼数量：" +list .size() +" \n " +list.toString());
//        }
        
        Map<Integer, Integer> interMap = match06(infoList, location);
        out06(interMap);
    }

    private void out06(Map<Integer, Integer> interMap) {
        int count = 0;
        Iterator<Integer> iterator = interMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            Integer integer = interMap.get(next);
            Out.e(" 间隔 " + next + " 次出现 的数量为：" + integer);
            count += integer  ;
        }
        Out.e(" 数量 " + count);
        Out.e(" ///////////////////// //////////");
        
    }

    public static Map<Integer, Integer> match06(List<DataTask.Info> infoList, int location) {

        Map<Integer, Integer> intMap = new HashMap<>();

        for (int i = 0; i < infoList.size() - 1; i++) {

            DataTask.Info info = infoList.get(i);
            if (info.location != location)
                continue;

            for (int i1 = i+1; i1 < infoList.size(); i1++) {
                DataTask.Info info1 = infoList.get(i1);
                if (info1.location == location) {
                    int i2 = i1 - i;
                    if (intMap.containsKey(i2)) {
                        intMap.put(i2, intMap.get(i2) + 1);
                    } else {
                        intMap.put(i2, 1);
                    }
                    i = i1;
                    break;
                }
            }
        }
        return intMap;
    }


    private List<DataTask.Info> match05(List<DataTask.Info> infoList, int location, Integer integer) {
        Out.e("" + integer);
        int count = 0;
        List<DataTask.Info> list = new ArrayList<>();
        for (int i = 0; i < infoList.size(); i += integer) {
            boolean have = false;
            for (int i1 = i; i1 < (i + integer < infoList.size() ? i + integer : infoList.size()); i1++) {
                DataTask.Info info = infoList.get(i1);
                if (info.location == location) {
                    have = true;
                    break;
                }
            }
            if (!have)
                list.addAll(infoList.subList(i, (i + integer < infoList.size() ? i + integer : infoList.size())));

        }
        return list;
    }

    /**
     * @param infoList
     * @param location 分区间投注 找出出现的最小区间
     */

    private List<Integer> match04(List<DataTask.Info> infoList, int location, List<Integer> minS) {
        if (minS == null)
            minS = new ArrayList<>();
        for (int i = 2; i < infoList.size() / 2; i *= 2) {
            int i1 = infoList.size() / i;
            if (i1 == 0)
                break;

            for (int j = 0; j < i; j++) {
                List<DataTask.Info> infos = infoList.subList(j * i1, (j + 1) * i1);
                boolean have = false;
                for (DataTask.Info info : infos) {
                    if (info.location == location) {
                        have = true;
                        break;
                    }
                }

                if (!have)
                    minS.add(i1);

            }
        }
        return minS;
    }

    private boolean contin(int location, List<Integer> minS, List<DataTask.Info> infos, int size) {
        if (infos.size() > 0) {
            boolean have = false;
            for (int i = 0; i < infos.size(); i++) {
                DataTask.Info info = infos.get(i);
                if (info.location == location) {
                    have = true;
                    break;
                }
            }
            if (!have) {
                minS.add(size);
                return have;
            }
        }
        return true;
    }


    private void out02(Map<Integer, Match02Info> match02InfoMap) {
        Iterator<Integer> iterator1 = match02InfoMap.keySet().iterator();
        while (iterator1.hasNext()) {
            Integer next = iterator1.next();
            Match02Info match02Info = match02InfoMap.get(next);
            Iterator<Integer> iterator2 = match02Info.interMap.keySet().iterator();
            Out.e(" \n" + " 连续 " + next + " 次 数量 " + match02Info.count);

            while (iterator2.hasNext()) {
                Integer next1 = iterator2.next();
                Integer integer = match02Info.interMap.get(next1);

                Out.e(" 间隔 " + next1 + " 次 数量 " + integer);
            }

        }
        Out.e(" ///////////////////// //////////");
    }

    private void out03(Map<Integer, Match02Info> match02InfoMap) {
        Iterator<Integer> iterator1 = match02InfoMap.keySet().iterator();
        while (iterator1.hasNext()) {
            Integer next = iterator1.next();
            Match02Info match02Info = match02InfoMap.get(next);
            Iterator<Integer> iterator2 = match02Info.interMap.keySet().iterator();
            Out.e(" \n" + " 连续 " + next + " 次 数量 " + match02Info.count);

            while (iterator2.hasNext()) {
                Integer next1 = iterator2.next();
                Integer integer = match02Info.interMap.get(next1);

                Out.e(" 距离上一次间隔之间出现 【" + next1 + "】 的 数量 " + integer);
            }

        }
        Out.e(" ///////////////////// //////////");
    }

    private void out01(Map<Integer, Integer> match01) {
        int count = 0;
        Iterator<Integer> iterator = match01.keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            Integer integer = match01.get(next);
            Out.e(" 连续出现 " + next + " 次的数量为：" + integer);
            count += next * integer;
        }
        Out.e(" 数量 " + count);
        Out.e(" ///////////////////// //////////");
    }

    /**
     * @param infoList 2.找出连续出现n次后第m次再次出现的数量z
     */

    public static Map<Integer, Match02Info> match02(List<DataTask.Info> infoList, int location) {
        Map<Integer, Match02Info> nMMap = new HashMap<>();

        int count = 0;
        for (int i = 0; i < infoList.size() - 1; i++) {
            DataTask.Info info = infoList.get(i);
            if (info.location != location)
                continue;
            count++;
            for (int i1 = i + 1; i1 < infoList.size(); i1++) {
                DataTask.Info info1 = infoList.get(i1);
                if (info1.location != location) {
                    int i2 = i1 - i;
                    i = i1;
                    if (nMMap.containsKey(i2)) {
                        Match02Info match02Info = nMMap.get(i2);
                        match02Info.count += 1;
                        match02Info = matchInter(infoList, location, i1, match02Info);
                        nMMap.put(i2, match02Info);
                    } else {
                        Match02Info value = new Match02Info();
                        value.continuous = i2;
                        value.count = 1;
                        value = matchInter(infoList, location, i1, value);
                        nMMap.put(i2, value);
                    }

                    break;
                } else count++;
            }
        }
        return nMMap;
    }

    private static Match02Info matchInter(List<DataTask.Info> infoList, int location, int i1, Match02Info match02Info) {
        for (int i3 = i1 + 1; i3 < infoList.size(); i3++) {
            DataTask.Info info2 = infoList.get(i3);
            if (info2.location == location) {
                int i4 = i3 - i1 - 1;
                if (match02Info.interMap.containsKey(i4)) {
                    match02Info.interMap.put(i4, match02Info.interMap.get(i4) + 1);
                } else {
                    match02Info.interMap.put(i4, 1);
                }
                return match02Info;

            }
        }
        return match02Info;
    }

    /**
     * @param infoList 1.找出连续出现n次的数量m
     */

    public static Map<Integer, Integer> match01(List<DataTask.Info> infoList, int location) {
        Map<Integer, Integer> nMMap = new HashMap<>();
        int count = 0;
        for (int i = 0; i < infoList.size() - 1; i++) {
            DataTask.Info info = infoList.get(i);
            if (info.location != location)
                continue;
            count++;
            for (int i1 = i + 1; i1 < infoList.size(); i1++) {
                DataTask.Info info1 = infoList.get(i1);
                if (info1.location != location) {
                    int i2 = i1 - i;
                    i = i1;
                    if (nMMap.containsKey(i2)) {
                        nMMap.put(i2, nMMap.get(i2) + 1);
                    } else {
                        nMMap.put(i2, 1);
                    }
                    break;
                } else count++;
            }
        }
        Out.e(" 总出现次数为：" + count);
        return nMMap;
    }

    /**
     * 3.找出连续出现n次之前出现到上一次出现之间出现其他组合的分别的数量
     *
     * @param infoList
     * @param location
     */
    public static Map<Integer, Match02Info> match03(List<DataTask.Info> infoList, int location) {
        Map<Integer, Match02Info> nMMap = new HashMap<>();

        int count = 0;
        for (int i = 0; i < infoList.size() - 1; i++) {
            DataTask.Info info = infoList.get(i);
            if (info.location != location)
                continue;
            count++;
            for (int i1 = i + 1; i1 < infoList.size(); i1++) {
                DataTask.Info info1 = infoList.get(i1);
                if (info1.location != location) {
                    int i2 = i1 - i;
                    i = i1;
                    if (nMMap.containsKey(i2)) {
                        Match02Info match02Info = nMMap.get(i2);
                        match02Info.count += 1;
                        match02Info = matchInter2(infoList, location, i1, match02Info);
                        nMMap.put(i2, match02Info);
                    } else {
                        Match02Info value = new Match02Info();
                        value.continuous = i2;
                        value.count = 1;
                        value = matchInter2(infoList, location, i1, value);
                        nMMap.put(i2, value);
                    }

                    break;
                } else count++;
            }
        }
        return nMMap;
    }

    private static Match02Info matchInter2(List<DataTask.Info> infoList, int location, int i1, Match02Info match02Info) {

        for (int i = i1 - 2; i >= 0; i--) {
            DataTask.Info info = infoList.get(i);
            if (info.location == location) {
                return match02Info;
            } else {
                if (match02Info.interMap.containsKey(info.location)) {
                    match02Info.interMap.put(info.location, match02Info.interMap.get(info.location) + 1);
                } else {
                    match02Info.interMap.put(info.location, 1);
                }
            }
        }
        return match02Info;
    }

}
