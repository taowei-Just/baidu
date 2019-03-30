package com.runn;


import com.*;

import java.sql.SQLException;
import java.util.*;

public class TicketWhere {
//    double[] mults = new double[]{9900, 220, 110, 82.5, 13.75, 9.166, 1.964, 3.409};
//    double[] mults = new double[]{9900, 220, 72, 63, 10.9, 7.3, 1.86, 2.86};
        double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};
    String[] detailS = new String[]{"五条", "炸弹", "葫芦", "顺子", "三条", "两对", "单对", "散号"};

    private long t1;
    private int total;
    private final TestMysql testMysql;

    public TicketWhere() {
        testMysql = new TestMysql("new_ticket457");
    }

    public static void main(String s[]) {
        System.err.println("main start");
//        new TicketWhere().tiket();
        new TicketWhere().tiket2();
        new TicketWhere().tiket4();
    }

    private void tiket4() {
        System.err.println("tiket4_=========================");

        double expectwin = 20 ;
        double precent = 0.1;

        for (int i = 1; i < 2; i++) {
            for (int j = 4; j < mults.length; j++) {
                match__000(expectwin, precent,   j);
            }
        }
    }

    private void match__000(double expectwin, double precent,   int j) {
     

        double lastTMult  =0;
        double lastnum = 1;
        double last = 1;
        for (int k = 1; k < Integer.MAX_VALUE; k++) {
            double mult = mults[j];
            double n = matchWin(mult, last, lastnum, lastnum * precent, expectwin , lastTMult);
//                    double n = matchWin02(mult, last, lastnum, lastnum * precent, expectwin , lastTMult);
            last = n;
            lastnum += n;
            lastTMult = (n * mult - lastnum) ;
            if ( k >100 || lastnum<0 || (n * mult - lastnum) <-10)
                break;
            System.err.println(detailS[j] + "第" + k + "期 预计当期投入" + n + " 总投入 " + lastnum + " 当期盈利 ：" + n * mult + " 总盈亏状况 ：" + (n * mult - lastnum) + " 盈利百分比  " + (n * mult - lastnum) / lastnum * 100 + "%");
        }
    }

    /**
     *
     * @param mult 倍率
     * @param last 上一次投注
     * @param lastnum  往期总投注
     * @param expectwin 期望盈利1
     * @param v     期望盈利2
     * @param lastTMult  上一期盈利
     * @return
     */
    private double matchWin(double mult, double last, double lastnum, double expectwin, double v, double lastTMult) {
//        matchWin02(mult, last, lastnum, expectwin, v , lastTMult);
        for (double i = last; i < Integer.MAX_VALUE; i++) {
            if ( i*(mult-1)>lastnum+expectwin && (i*(mult-1)>v+i &&  i*(mult-1)>lastnum*2 ||  i*(mult-1)>lastnum+lastTMult)){
                return i ;
            }
        }

        return -1;
    }
    /**
     *
     * @param mult 倍率
     * @param last 上一次投注
     * @param lastnum  往期总投注
     * @param expectwin 期望盈利1
     * @param v     期望盈利2
     * @param lastTMult  上一期盈利
     * @return
     */
    private double matchWin02(double mult, double last, double lastnum, double expectwin, double v, double lastTMult) {


//        double v1 = lastnum / (mult - 1) + expectwin > v ? expectwin > lastTMult ? expectwin : lastTMult : v > lastTMult ? v : lastTMult;

        // 计算盈利不低于期望值的最小投入


        for (double i = last; i < Integer.MAX_VALUE; i++) {

            if (  i*mult > lastnum +(expectwin > v ? expectwin : v ) +i){

                System.err.println("matchWin02:"+i);
                return i ;
            }
        }


        return -1;
    }

    private void tiket2() {
        System.err.println("tiket2_=========================");
        int init = 10;
        for (int k = 1; k < 2; k++) {
            System.err.println("初始投注 " + k);
            for (int i = 4; i < 8; i++) {
                double mult = mults[i];
                float num = 1f;
                float total = 0;
                double lastv = 0;
                float ii =  0.2f;
                System.err.println(" 开始计算  " + detailS[i] + "============ \n");

                for (int j = 0; j < Integer.MAX_VALUE; j++) {

                    //第一种方式  每次下注在上一注的基础上加上基础值
                    //                int i1 = num += k ;
                    //第二种方式  没次下注在上一注的基础上加上基础值再加 追加的注次
//                int i1 = num += k + j;
                    //第三种方式 在追投没有超过倍率时使用第一种方式 在超过倍率时使用追加基础值的两倍
                    float i1 = num += k * ii;
                    double v = mult * i1;
                    if (lastv > (v - total)) {
                        System.err.println(" 到达 " + j + " 注次开始递减 需要提高倍率。");
                        ii += ii;
                        i1 = (num += k * ii);
                        v = mult * i1;
                    }
                    lastv = (v - total);
                    total += num;
                    System.err.println("" + detailS[i] + " 倍率" + mult + " 坚持投注 " + (j + 1) + "期 如果中的话  当前盈利：" + v + " 总盈利 " + (v - total) + " 当前投入:" + i1 + "预计投入总：" + total + " 盈利比 ： " + (v - total) * 100f / total / 100);

                    Info info = new Info();
                    info.initNum =num;
                    info.adding =ii;
                    info.location =i;
                    info.ratio =mult ;
                    info.detail =detailS[i] ;
                    info.periods =(j + 1)  ;
                    info.curentIn =i1;
                    info.totalIn = total;
                    info.currentWin =v  ;
                    info.toatlWin = (v - total)  ;
                    info.winPercent = (v - total) * 100f / total / 100;

                    if (lastv <  -10 || total > 10000)
                        break;
                }
            }
        }
    }

    public void tiket() {
        t1 = System.currentTimeMillis();
        WhereInfo whereInfo = new WhereInfo();
        whereInfo.expect = new int[]{7};
        whereInfo.maxOneMoey = 100;
        whereInfo.maxTotalMoey = 100;
        whereInfo.ints = new int[]{4, 5, 7};
        whereInfo.money = new int[]{-1, -1, -1};
        whereInfo.minWin = 4;
        tickrt001(whereInfo);

        System.err.println("计算次数：" + aLong + " 耗时：" + (System.currentTimeMillis() - t1) / 1000 + " s");
    }

    private void tickrt001(WhereInfo whereInfo) {
        if (whereInfo == null)
            return;
//        getMoeyS(whereInfo ,null,0);
        // 按投注条件计算出 已选注数的投入和其他注数的投入  和相应的结果

        // 1. 计算出 投注的所有的组合
        // 2.计算出 所有组合的 结果
        // 3. 筛选结果
        List<int[]> moeyS = getMoeyS(whereInfo);
        Map<Integer, HelloWorld.Info> integerInfoMap = matchResult(whereInfo, moeyS);
        List<HelloWorld.Info> infoList = matchEcxpect(whereInfo, integerInfoMap);
        integerInfoMap.clear();
        moeyS.clear();
        System.gc();
        filtrateResult(whereInfo, infoList);

    }

    private void filtrateResult(WhereInfo whereInfo, List<HelloWorld.Info> infoList) {
        // 按条件筛选 结果
        // 1. 按投入金额 分类
        // 2. 按期望盈利注 分类 排序
        // 3. 按盈利比 分来 排序

        Ranking ranking = new Ranking();
        ranking.pour(whereInfo, infoList);
        System.err.println(infoList.toString());

        try {
            for (HelloWorld.Info inf : infoList) {
                testMysql.insertData(new TicketWhereInsert(testMysql), inf);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private List<HelloWorld.Info> matchEcxpect(WhereInfo whereInfo, Map<Integer, HelloWorld.Info> integerInfoMap) {
        List<HelloWorld.Info> infoList = new ArrayList<>();

        Iterator<Integer> iterator = integerInfoMap.keySet().iterator();
        while (iterator.hasNext()) {

            Integer integer = iterator.next();
            HelloWorld.Info info = integerInfoMap.get(integer);

            boolean result = true;
            for (int i = 0; i < info.result.length; i++) {
                double v = info.result[i];
                if (v < whereInfo.minWin) {
                    result = false;
                    break;
                }
            }
            if (result)
                infoList.add(info);

            aLong++;
        }

        return infoList;
    }

    private Map<Integer, HelloWorld.Info> matchResult(WhereInfo whereInfo, List<int[]> moeyS) {
        Map map = new HashMap();
        for (int i = 0; i < moeyS.size(); i++) {
            int[] ints = moeyS.get(i);
            int[] ints1 = whereInfo.ints;
            if (ints.length != ints1.length)
                continue;
            int totalmoney = 0;
            for (int j = 0; j < ints.length; j++) {
                totalmoney += ints[j];
            }
            HelloWorld.Info info = new HelloWorld.Info();
            info.number = new int[ints.length];
            info.money = new int[ints.length];
            info.result = new double[ints.length];
            info.winRatio = new double[ints.length];

            for (int j = 0; j < ints1.length; j++) {
                double ticketNum = ints[j] * mults[ints1[j]] - totalmoney;
                info.number[j] = ints1[j];
                info.money[j] = ints[j];
                info.result[j] = ticketNum;
                info.totalmoney = totalmoney;
                info.winRatio[j] = ticketNum / totalmoney;
                aLong++;

            }
            boolean hh = true;
            for (int j = 0; j < info.result.length; j++) {
                if (info.result[j] < whereInfo.minWin)
                    hh = false;
            }

            if (hh)
                map.put(i, info);
            System.err.println(" 运算：:" + aLong + "次 耗时：" + (System.currentTimeMillis() - t1) / 1000 + " /s");
        }
        return map;
    }


    private List<int[]> getMoeyS(WhereInfo whereInfo) {
        int[] money = whereInfo.money;
        List<int[]> intLis = new ArrayList<>();
        for (int i = 0; i < money.length; i++) {
            if (money[i] < 0) {
                mm(whereInfo, null, intLis, 0);
            }
        }
        return intLis;
    }

    private void mm(WhereInfo whereInfo, int[] ints, List<int[]> intLis, int index) {
        if (ints == null)
            ints = new int[whereInfo.money.length];


        for (int i = 0; i < whereInfo.maxOneMoey; i++) {
            if (whereInfo.money[index] < 0) {
                ints[index] = i;
            } else {
                ints[index] = whereInfo.money[index];
            }

            if (index != whereInfo.money.length - 1)
                mm(whereInfo, ints, intLis, index + 1);
            else {

                int i1 = 0;
                for (int j = 0; j < ints.length; j++) {
                    i1 += ints[j];
                }
                if (i1 > whereInfo.maxTotalMoey)
                    continue;
                int[] ints1 = new int[ints.length];
                System.arraycopy(ints, 0, ints1, 0, ints1.length);
                intLis.add(ints1);

            }

        }

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

                HelloWorld.Info info = new HelloWorld.Info();
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


    }

    private void prepareInfo(HelloWorld.Info info) {

    }

    public static class Info {
        public float initNum;
        public double ratio;
        public int periods;
        public int location;
        public String detail;
        public double curentIn;
        public double totalIn;
        public double currentWin;
        public double toatlWin;
        public double winPercent;
        public float adding;
    }

}
