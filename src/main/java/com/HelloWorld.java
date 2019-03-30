package com;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloWorld {
    private final ExecutorService executorService;
    double[] mults = new double[]{9900, 220, 110, 82.5, 13.75, 9.166, 1.964, 3.409};
    private long t1;
    private static TestMysql testMysql;

    public HelloWorld() {
        executorService = Executors.newFixedThreadPool(1);

    }

    public static void main(String[] args) {
        testMysql = new TestMysql("");

        HelloWorld hell = new HelloWorld();
        System.out.println("Hello world!");
        hell.tiket();
    }

    public void tiket() {

//        test(100, 0, new int[]{5, 6, 7}, new HashMap());

//        test2(50, 0, new int[]{6, 7}, new HashMap());
        t1 = System.currentTimeMillis();
//        mult();
        System.err.println("start");
        for (int i = 0; i < 100; i++) {
            tickrt001(i, new int[]{5,6,7});
        }


        System.err.println("计算次数：" + aLong + " 耗时：" + (System.currentTimeMillis() - t1) / 1000 + " s");
    }

    long aLong = 0;

    private void tickrt001(int i, int[] ints) {
        List<List<Map>> ticketS = new ArrayList<>();
        for (int j = 1; j <= i; j++) {
            List<Map> integerList = new ArrayList<>();
            ticketS.add(integerList);
            Map map = new HashMap();
            map.put(ints[0], j);

            if (ints.length > 1) {
                int[] ints1 = new int[ints.length - 1];
                System.arraycopy(ints, 1, ints1, 0, ints1.length);
                tickrt001_02(i - j, ints1, map, integerList);
            }

        }
//        System.err.println( ticketS.toString());

        tickrt001_03(ticketS, ints);
    }

    private void tickrt001_02(int i, int[] ints, Map map, List<Map> integerList) {
        for (int j = 1; j <= i; j++) {
            aLong++;
            map.put(ints[0], j);
            if (ints.length > 1) {
                int[] ints1 = new int[ints.length - 1];
                System.arraycopy(ints, 1, ints1, 0, ints1.length);
                tickrt001_02(i - j, ints1, map, integerList);
            } else {


                Map map1 = new HashMap();
                map1.putAll(map);
                integerList.add(map1);
//                System.err.println( map1.toString());

            }
        }

    }


    private void tickrt001_03(List<List<Map>> ticketS, int[] ints) {

        for (int i = 0; i < ticketS.size(); i++) {
            for (int j = 0; j < ticketS.get(i).size(); j++) {
                Map map = ticketS.get(i).get(j);
                if (map.size() != ints.length)
                    continue;

                Info info = new Info();
                info.number = new int[ints.length];
                info.money = new int[ints.length];
                info.result = new double[ints.length];

                int money = 0;
                for (int k = 0; k < ints.length; k++) {
                    money += (int) map.get(ints[k]);
                }
                Map map1 = new HashMap();

                for (int k = 0; k < ints.length; k++) {
                    int money2 = (int) map.get(ints[k]);
                    double ticketNum = money2 * mults[ints[k]] - money;
                    map1.put(ints[k], ticketNum);
                    info.number[k] = ints[k];
                    info.money[k] = money2;
                    info.result[k] = ticketNum;
                }

                prepareInfo(info);
            }
        }
        try {
            testMysql.insertData(null, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public   void prepareInfo(Info info) {
        int[] number = info.number;
        int[] money = info.money;
        double[] result = info.result;

        if (!result(result))
            return;

        TicletTab ticletTab = new TicletTab();
        int i1 = 0;
        for (int i = 0; i < money.length; i++) {
            i1 += money[i];
        }

        for (int i = 0; i < number.length; i++) {
            switch (number[i]) {
                case 0:
                    ticletTab.setTouzhu_wutiao(money[i]);
                    ticletTab.setWutiao_zhongjiang_yingli(result[i]);
                    ticletTab.setWutiao_zhongjiang_kuisun(-i1);
                    break;
                case 1:
                    ticletTab.setTouzhu_zadan(money[i]);
                    ticletTab.setZadan_zhongjiang_yingli(result[i]);
                    ticletTab.setZadan_zhongjiang_kuisun(-i1);
                    break;
                case 2:
                    ticletTab.setTouzhu_hulu(money[i]);
                    ticletTab.setHulu_zhongjiang_yingli(result[i]);
                    ticletTab.setHulu_zhongjiang_kuisun(-i1);
                    break;
                case 3:
                    ticletTab.setTouzhu_shunzi(money[i]);
                    ticletTab.setShunzi_zhongjiang_yignli(result[i]);
                    ticletTab.setShunzi_zhongjiang_kuisun(i1);
                    break;

                case 4:
                    ticletTab.setTouzhu_santiao(money[i]);
                    ticletTab.setSantiao_zhongjiang_yingli(result[i]);
                    ticletTab.setSantiao_zhongjiang_kuisun(-i1);
                    break;
                case 5:

                    ticletTab.setTouzhu_liangdui(money[i]);
                    ticletTab.setLiangdui_zhongjiang_yingli(result[i]);
                    ticletTab.setLiangdui_zhongjiang_kuisun(-i1);

                    break;

                case 6:

                    ticletTab.setTouzhu_dandui(money[i]);
                    ticletTab.setDandui_zhongjiang_yingli(result[i]);
                    ticletTab.setDandui_zhongjiang_kuisun(-i1);

                    break;
                case 7:

                    ticletTab.setTouzhu_sanhao(money[i]);

                    ticletTab.setSanhao_zhongjiang_yingli(result[i]);
                    ticletTab.setSanhao_zhongjiang_kuisun(-i1);
                    break;


            }
        }

        try {
            testMysql.insertData(ticletTab ,false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (ticletTab.getSantiao_zhongjiang_yingli() > -10)
        writerLog(ticletTab.toString());
    }

    private boolean result(double[] result) {
        for (int i = 0; i < result.length; i++) {
            if (result[i] < -10)
                return false;
        }
        return true;
    }

    public void test(int money, int last,
                     int[] ints,
                     Map map) {
        if (map == null)
            map = new HashMap();

        for (int i = 0; i < money; i++) {


            double ticketNum = i * mults[ints[0]] - i - last;
            Info info = new Info();

            //if(info.list ==null)
            // info.list=new Arraylist();

            //list.add(ints[0]);
            map.put(ints[0], info);

            if (ticketNum > 0)
                if (ints.length > 1) {
                    int[] ints1 = new int[ints.length - 1];
                    System.arraycopy(ints, 1, ints1, 0, ints1.length);

                    test(money, i, ints1, map);
                } else {

                    System.out.println(
                            "输出： " +
                                    map.toString());

                }

        }

    }

    List<Double> ticketNs;
    List<Integer> intm;
    List<Double> minticketNs;
    List<Integer> minintm;

    public void test2(int money, int last, int[] ints, Map map) {

        if (map == null)
            map = new HashMap();


        for (int i = 0; i < money; i++) {

            System.err.println(" =================================  ");
            ticketNs = new ArrayList();
            intm = new ArrayList<>();
            minticketNs = new ArrayList();
            minintm = new ArrayList<>();

            double ticketNum = i * mults[ints[0]] - (last + i);
            Info info = new Info();
            info.number = new int[ints.length];
            info.money = new int[ints.length];
            info.result = new double[ints.length];

            info.number[0] = ints[0];
            info.money[0] = i;
            info.result[0] = ticketNum;

            if (ticketNum > 0)
                if (ints.length > 1) {
                    int[] ints1 = new int[ints.length - 1];
                    System.arraycopy(ints, 1, ints1, 0, ints1.length);
                    test2_3(info, 0, money - i, last + i, ints1, map);
                } else {
                    System.out.println("输出： " + map.toString());
                }

        }

    }

    public void test2_3(Info info, int index, int money, int last, int[] ints, Map map) {
        index += 1;
        if (map == null)
            map = new HashMap();


        for (int i = 0; i < money; i++) {
            double ticketNum = i * mults[ints[0]] - (last + i);


            if (ticketNum > 0 || conditions(info)) {
                info.number[index] = ints[0];
                info.money[index] = i;
                info.result[index] = ticketNum;

                map.put(ints[0], info);

                if (ints.length > 1) {
                    int[] ints1 = new int[ints.length - 1];
                    System.arraycopy(ints, 1, ints1, 0, ints1.length);
                    test2_3(info, index, money - i, last + i, ints1, map);
                } else {
                    System.out.println("输出： " + map.toString());
                }
            }

        }

    }

    private boolean conditions(Info info) {


        return false;
    }

    private void mult() {
        for (int i = 3; i < mults.length - 3; i++) {
            // 确定数组长度
            int[] ints = new int[i];
            //确定数组内容
            for (int k = mults.length - 1; k >= 0; k--) {
                ints[0] = k; // 0 -7
                prepareNum(ints);
            }
        }

    }
    private void prepareNum(int[] ints) {

        switch (ints.length) {
            case 2:
                for (int i = 1; i < mults.length; i++) {
                    ints[1] = ints[0] + i;
                    if (ints[1] >= mults.length)
                        continue;
                    sarray(ints);
                }

                break;
            case 3:
                for (int i = 1; i < mults.length; i++) {
                    ints[1] = ints[0] + i;
                    if (ints[1] >= mults.length)
                        continue;
                    for (int j = 1; j < mults.length; j++) {
                        ints[2] = ints[1] + j;
                        if (ints[2] >= mults.length)
                            continue;
                        sarray(ints);

                    }
                }

                break;
            case 4:
                for (int i = 1; i < mults.length; i++) {
                    ints[1] = ints[0] + i;
                    if (ints[1] >= mults.length)
                        continue;
                    for (int j = 1; j < mults.length; j++) {
                        ints[2] = ints[1] + j;
                        if (ints[2] >= mults.length)
                            continue;
                        for (int k = 1; k < mults.length; k++) {
                            ints[3] = ints[2] + k;
                            if (ints[3] >= mults.length)
                                continue;
                            sarray(ints);

                        }
                    }
                }
                break;
            case 5:
                for (int i = 1; i < mults.length; i++) {
                    ints[1] = ints[0] + i;
                    if (ints[1] >= mults.length)
                        continue;
                    for (int j = 1; j < mults.length; j++) {
                        ints[2] = ints[1] + j;
                        if (ints[2] >= mults.length)
                            continue;
                        for (int k = 1; k < mults.length; k++) {
                            ints[3] = ints[2] + k;
                            if (ints[3] >= mults.length)
                                continue;
                            for (int l = 1; l < mults.length; l++) {
                                ints[4] = ints[3] + k;
                                if (ints[4] >= mults.length)
                                    continue;
                                sarray(ints);

                            }
                        }
                    }
                }
                break;
            case 6:
                for (int i = 1; i < mults.length; i++) {
                    ints[1] = ints[0] + i;
                    if (ints[1] >= mults.length)
                        continue;
                    for (int j = 1; j < mults.length; j++) {
                        ints[2] = ints[1] + j;
                        if (ints[2] >= mults.length)
                            continue;
                        for (int k = 1; k < mults.length; k++) {
                            ints[3] = ints[2] + k;
                            if (ints[3] >= mults.length)
                                continue;
                            for (int l = 1; l < mults.length; l++) {
                                ints[4] = ints[3] + l;
                                if (ints[4] >= mults.length)
                                    continue;
                                for (int m = 1; m < mults.length; m++) {
                                    ints[5] = ints[4] + m;
                                    if (ints[5] >= mults.length)
                                        continue;
                                    sarray(ints);
                                }
                            }
                        }
                    }
                }
                break;
            case 7:
                for (int i = 1; i < mults.length; i++) {
                    ints[1] = ints[0] + i;
                    if (ints[1] >= mults.length)
                        continue;
                    for (int j = 1; j < mults.length; j++) {
                        ints[2] = ints[1] + j;
                        if (ints[2] >= mults.length)
                            continue;
                        for (int k = 1; k < mults.length; k++) {
                            ints[3] = ints[2] + k;
                            if (ints[3] >= mults.length)
                                continue;
                            for (int l = 1; l < mults.length; l++) {
                                ints[4] = ints[3] + l;
                                if (ints[4] >= mults.length)
                                    continue;
                                for (int m = 1; m < mults.length; m++) {
                                    ints[5] = ints[4] + m;
                                    if (ints[5] >= mults.length)
                                        continue;

                                    for (int n = 1; n < mults.length; n++) {
                                        ints[6] = ints[5] + n;
                                        if (ints[6] >= mults.length)
                                            continue;
                                        sarray(ints);
                                    }
                                }
                            }
                        }
                    }
                }

                break;


        }

    }
    private void sarray(final int[] ints) {

        final int[] ints1 = new int[ints.length];
        System.arraycopy(ints, 0, ints1, 0, ints.length);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                long t1 = System.currentTimeMillis();
                System.err.println("开始 计算 " + Arrays.toString(ints1) + " 组合 ");
                tickrt001(100, ints1);
                System.err.println(" 计算 " + Arrays.toString(ints1) + " 组合 完毕耗时：" + (System.currentTimeMillis() - t1) / 1000 + " s");

            }
        });


    }

    private Map maxDta(List<Double> ticketNs, List<Integer> intm, int ind, double ticketNum, Map map) {
        if (map == null)
            map = new HashMap<>();

        // 最大值 最小值  分别对应的 下注金额


        ticketNs.add(ticketNum);
        if (intm.size() == 0) {
            intm.add(ind);
            intm.set(0, ind);
        }
        if (map.size() == 0)
            map.put(ticketNs.get(0), intm);
        for (int i = 0; i < ticketNs.size(); i++) {
            Double aDouble = ticketNs.get(i);
            for (int j = 1; j < ticketNs.size(); j++) {
                Double aDouble1 = ticketNs.get(j);
                if (aDouble1 > aDouble) {
                    map.clear();
                    ticketNs.set(i, aDouble1);
                    ticketNs.set(j, aDouble);
                    intm.set(0, ind);
                    map.put(ticketNs.get(0), intm);
                }
            }
        }

        return map;
    }
    private Map minDta(List<Double> ticketNs, List<Integer> intm, int ind, double ticketNum, Map map) {
        if (map == null)
            map = new HashMap<>();

        ticketNs.add(ticketNum);
        if (intm.size() == 0) {
            intm.add(ind);
            intm.set(0, ind);

        }
        if (map.size() == 0)
            map.put(ticketNs.get(0), intm);
        for (int i = 0; i < ticketNs.size(); i++) {
            Double aDouble = ticketNs.get(i);
            for (int j = 1; j < ticketNs.size(); j++) {
                Double aDouble1 = ticketNs.get(j);
                if (aDouble1 < aDouble) {

                    map.clear();
                    ticketNs.set(i, aDouble1);
                    ticketNs.set(j, aDouble);
                    intm.set(0, ind);
                    map.put(ticketNs.get(0), intm);
                }
            }
        }
        return map;
    }
    public static class Info {
        public int[] number;
        public int[] money;
        public double[] result;
        public  int totalmoney ;
        public  double [] winRatio ;


        @Override
        public String toString() {
            return "Info{" +
                    "number=" + Arrays.toString(number) +
                    ", money=" + Arrays.toString(money) +
                    ", result=" + Arrays.toString(result) +
                    ", totalmoney=" + totalmoney +
                    ", winRatio=" + Arrays.toString(winRatio) +
                    '}';
        }
    }

    private void writerLog(String s) {
        System.err.println(s);
        if (s != null)
            return;
        File file = new File("G:/Temp/log/log4.txt");

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

        } catch ( Exception e) {
            e.printStackTrace();
        }
    }


}