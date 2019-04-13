package matchore;

import baidu.Ecai.Main;
import baidu.Ecai.MatchWin;
import baidu.bean.MatchWinInfo;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.bean.WinInfo;
import baidu.utils.CoreMath;
import baidu.utils.Out;
import com.TestMysql;
import com.algorithm.probabillity.ProbabilitySpecluate;
import com.runn.DataTask;
import niuniu.NiuNIuMatch;
import org.jetbrains.annotations.NotNull;
import subweb.subRun.SubWeb_VR_1_1;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MatchCore {
    public static String[] detailS = new String[]{"五条", "炸弹", "葫芦", "顺子", "三条", "两对", "单对", "散号"};

    //    public static double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};
    public static double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};
    public static int[] indedeS = new int[]{200, 100, 50, 20, 22, 20, 4, 4};
    public static int[] countS = new int[]{60, 30, 17, 7, -1, -1, -1, -1};
    public static double[] dubS = new double[]{2.0, 1, 0.2, 0.1, 0.02};
    public static double[] messAgeSends = new double[]{0, 0, 0, 0, 0, 0, 0, 0};

    // 间隔提醒阈值
    public static double[] thresholdS = new double[]{150, 60, 50, 20, 20, 13, 5, 10};

    private final TestMysql testMysql;
    private static MatchCore matchWin;

    public MatchCore() {
        testMysql = new TestMysql("ticket_data_vr_1_1");
    }

    public static void main(String[] args) {
        matchWin = new MatchCore();
        matchWin.test();
    }

    public static List<DataTask.Info> querYInfoNet(TestMysql testMysql, WinInfo currentWininfo) {
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date ='" + currentWininfo.currentIssue.substring(0,8) + "'  ORDER BY date asc ,  periods asc";

        List<DataTask.Info> infos = querYInfoList(testMysql, str);
        Collections.sort(infos);
        DataTask.Info info = infos.get(infos.size() - 1);
        Out.e(info.toString() + " " + currentWininfo.toString()) ;
        if (info.equals(currentWininfo.issue)){
            Out.e("数据库最近一跳数据为最新数据直接使用");
            return infos ;
        }else {
            Out.e("从网络获取最新数据 ");
            try {
                List<DataTask.Info> infos1 = SubWeb_VR_1_1.doSubVR_1_1();
                for (int i = 0; i < infos1.size(); i++) {
                    DataTask.Info info1 = infos1.get(i);
                    boolean have =false ;
                    for (int j = 0; j < infos.size(); j++) {
                        DataTask.Info info2 = infos.get(j);
                        if (info1.periods .equals( info2.periods)){
                            have =true ;
                            break;
                        } 
                    }
                    if (!have)
                        infos.add(info1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Out.e(" 得到最近数据 " +infos.toString());
        return infos ;
    }

    public static void matchInfo(Map<Integer, List<DataTask.Info>> hisMap, List<DataTask.Info> infos) {
            for (DataTask.Info his : infos)
                MatchCore. matchInfo( hisMap,his.number, his.periods);
    }

    private void test() {
        TicketInfo info = creatTicketInfo(4, 5, 0.35, 8000, 4, 1);
        
        List<PourInfo> pourInfos = getDanduiPourInfos(info);
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
            List<DataTask.Info> infos = start(new SimpleDateFormat("yyyyMMdd").format(instance.getTime()));
            matchWinMoney(testMysql, infos, pourInfos, info.indede);
        }


    }

    /**
     * @param location    下注组合
     * @param expect      期望最小盈利
     * @param precent     期望盈利百分比
     * @param zhisunMoney 止损金额
     * @param dub         下注单位
     * @param mulripe     下注倍数
     * @return
     */
    public static TicketInfo creatTicketInfo(int location, int expect, double precent, double zhisunMoney, int dub, int mulripe) {

        TicketInfo info = new TicketInfo();
        info.account = "wtw960424";
        info.password = "953934cap";
        info.tag = "_dan_dui_";
        info.minIss = 40;
        info.ticketKind = 3;
        info.money = 8000;
        info.stopLoss = 1;
        info.first = 1;
        info.useDefaultPresent = false;
        info.decreasP = 0.05;
        info.mulripe = mulripe;
        info.dub = dub;
        info.precent = precent;
        info.zhisunMoney = zhisunMoney;
        info.expectW = expect;
        info.indede = location;
        return info;
    }

    public static List<PourInfo> getDanduiPourInfos(TicketInfo info01) {
        MatchWinInfo matchWinInfo = MatchWin.creatMatchInfo(info01);
        matchWinInfo.setRongduaPre(1);
        System.out.println(matchWinInfo.toString());
        return matchWin(matchWinInfo);
    }

    public static void matchWinMoney(TestMysql testMysql, List<DataTask.Info> infos, List<PourInfo> pourInfos, int indede) {
        double money = 0;
        double max = 0;
        double maxJ = 0;
        int j = 0;
        Random random = new Random();
        List<Integer> subs = null;
        Map<Integer, Integer> toatalMax = toatalMax(testMysql, indede);
        for (int i = 0; i < infos.size(); i++) {

            if (j == 0) {
                subs = new ArrayList<>();
                for (int k = 0; k < countS[indede]; k++) {
                    int o = random.nextInt(indedeS[indede]);
                    if (subs.contains(o)) {
                        k--;
                        continue;
                    } else
                        subs.add(o);
                }
                Collections.sort(subs);
                List<DataTask.Info>  inS = infos.subList(0,i);
                
                dynamicIndes( pourInfos, inS, toatalMax,indede);
            }

            if (indede == infos.get(i).location) {
                maxJ = j > maxJ ? j : maxJ;

                if (j < indedeS[indede]) {
//                    if (subs.contains(j)) {
//                        money += pourInfos.get(j).win;
//
//                    }else {
//
//                        int ind=0;
//                        for (int k = 0; k <subs.size(); k++) {
//
//                            Integer integer = subs.get(k);
//                            if (integer<j)
//                                ind =integer ;
//                        }

//                        Out.e(" 投注：" +  subs.toString());
//                        PourInfo pourInfo = pourInfos.get(ind);

//                        Out.e(" 损失1：" + pourInfo.total);
//                        Out.e("损失1 间隔 " + (j) + "期");
//                        money-= pourInfo.total ;
//                        max = pourInfo.total >max? pourInfo.total:max;
//                    
//                    }
                    j = 0;
                    continue;
                }

                if (j >= pourInfos.size() + indedeS[indede]) {
                    PourInfo pourInfo = pourInfos.get(pourInfos.size() - 1);
                    Out.e(" 损失：" + pourInfo.total);
                    Out.e("损失 间隔 " + (j) + "期");
                    money -= pourInfo.total;
                    max = pourInfo.total > max ? pourInfo.total : max;
                } else {
                    PourInfo pourInfo = pourInfos.get(j - indedeS[indede]);
                    Out.e(" 盈利：" + pourInfo.win + " " +j );
                    money += pourInfo.win;
                    max = pourInfo.total > max ? pourInfo.total : max;

                }
                j = 0;
                continue;
            }
            j++;
        }
        Out.e("总盈利：" + money);
        Out.e("最大投注金额：" + max);
        Out.e("最大间隔：" + maxJ);
    }

    public static void dynamicIndes(  List<PourInfo> pourInfos, List<DataTask.Info> infos,  Map<Integer, Integer> totalMap, int indede) {

        // 获取历史最大间隔 以及出现次数 动态设置 头部跳过长度  
        // 最大长度为 历史最大值减去生成的跟注数量 +1 
        // 大于最大值的 50% 间隔数量出现的越多 后面头部跳过的长度越短 
    
        Set<Integer> totalSet = totalMap.keySet();
        
        List<DataTask.Info> infoList = new ArrayList<>();
        List<Integer> sortL = new ArrayList<>();
   
        for (Integer integer : totalSet) {
            sortL.add(integer);
        }
        Collections.sort(sortL);

        Integer total = sortL.get(sortL.size() - 1);
//        Out.e("历史最大间隔为 :" + total);

        for (DataTask.Info info : infos) {
                infoList.add(info);
        }
        
//        infoList = infoList.subList(0,infoList.indexOf(infos.get(i)));
        
        Map<Integer, Integer> inteMap = ProbabilitySpecluate.match06(infoList, indede);
        Set<Integer> integers = inteMap.keySet();
        int count = 0;
        sortL.clear();
        for (Integer integer : integers) {
            sortL.add(integer);
            if(integer > total/2){
                count+= inteMap.get(integer);
            }
        }
        
        Collections.sort(sortL);
        Integer dataTotal =0;
        if (sortL.size() > 0)
            dataTotal = sortL.get(sortL.size() - 1);
        
        
        int maxJump = total - pourInfos.size() + 1;
        int [] juMpS =new int[]{ maxJump,maxJump,(maxJump/2),(maxJump/2)/2,0};
        
        if (count < juMpS.length)
            indedeS[indede] = juMpS[count];
        else 
            indedeS[indede]=0;

        Out.e(" 当前最大间隔为 :" + dataTotal + " 超过最大间隔一半的数量：" +count +  " 跟注期数：" +pourInfos.size());
        Out.e(" 当前最跳过长度为 :" + indedeS[indede] );
     
//        0 ,1, 2 ,3 ,4 ,5 ,6 ,7 ,8 ;
//        17 , 17 ,8,2,1,0
        
        
        /**
         *  56-8
         *  85-1
         *  49-7
         *  65-2
         *  54-4
         *  55-9
         *  68-7
         *  50-8
         */
         
        
        
        
        
        
        
    }

    public static Map<Integer, Integer> toatalMax(TestMysql sql, int indede) {
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date >'0'  ORDER BY date asc ,  periods asc";
        List<DataTask.Info> historyInfoS = querYInfoList(sql, str);
        return ProbabilitySpecluate.match06(historyInfoS, indede);
    }

    /**
     * @param matchWinInfo 计算参数
     * @return
     */

    public static List<PourInfo> matchWin(MatchWinInfo matchWinInfo) {
        List<PourInfo> doubles = new ArrayList<>();
        //期望收益百分比
        double present = matchWinInfo.present;
        //期望最小收益
        double assign = matchWinInfo.assign;
        double total = 0;
        double bet;
        for (int i = 0; i < matchWinInfo.count; i++) {
            //投注金额
            if (i == 0 && matchWinInfo.firstM > 0) {
                bet = matchWinInfo.firstM;
            } else {
                bet = matchAssign(mults[matchWinInfo.inde], total, assign, present, matchWinInfo.maxMoney);
            }
            total += bet;

            if (matchWinInfo.useRongd) {
                if ((total / matchWinInfo.maxMoney >= matchWinInfo.rongduaPre || matchWinInfo.rongduanMoney > 0 ? total >= matchWinInfo.rongduanMoney : false)) {
                    Out.d(" 触发熔断 " + (i + 1) + "熔断值：" + matchWinInfo.rongduanMoney + ":" + matchWinInfo.rongduaPre * matchWinInfo.maxMoney + " _ 预计亏损：" + (total - bet));
                    return doubles;
                }
            }
            if (bet < 0)
                return doubles;

            double pv = ((bet * mults[matchWinInfo.inde]) - total) / total;
            double v1 = total / matchWinInfo.maxMoney;

            if (v1 > matchWinInfo.zhiSunP || (matchWinInfo.zhisunMoney > 0 ? total > matchWinInfo.zhisunMoney : false)) {
                Out.d(" 到达减损 " + i + " _ " + v1);

                present -= matchWinInfo.decreasP;
                present = present < 0 ? 0 : present;
                assign -= assign * matchWinInfo.decreasP;
            }

            PourInfo pourInfo = new PourInfo();
            pourInfo.moey = bet;
            pourInfo.id = i;
            pourInfo.win = (bet * mults[matchWinInfo.inde]) - total;
            pourInfo.total = total;
            doubles.add(pourInfo);
            Out.d("计算第：" + (i + 1) + "项 值为：" + bet + " total " + total + " present " + pv * 100 + "%");
            Out.d(" 验证 " + ((bet * mults[matchWinInfo.inde]) - total));

        }
        return doubles;
    }

    /**
     * @param mult     倍率
     * @param total    已经投入总金额
     * @param assign   期望盈利
     * @param present  期望盈利百分比
     * @param maxMoney 最大投入资金
     * @return 计算出匹配期望盈利的最小投入
     */

    private static double matchAssign(double mult, double total, double assign, double present, double maxMoney) {
        for (int i = 0; i < maxMoney * 10; i++) {
            double v = getV(mult, total, total * present > assign + i ? total * present : assign + i);
            //验证
            double v1 = (v * mult - total - v) / (total + v);
            if ((v * mult) - total - v >= assign && v1 >= present) {
                return v;
            }
        }
        return -1;
    }

    /**
     * @param mult   倍率
     * @param total  总投注金额
     * @param assign 期望盈利
     * @return
     */
    private static double getV(double mult, double total, double assign) {
        double v1 = total / (mult - 1) + assign / mult;
        v1 += v1 % 1 > 0 ? 1 : 0;
        return (int) (v1 > 1 ? v1 : 1);
    }

    private List<DataTask.Info> start(String strd) {
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date ='" + strd + "'  ORDER BY date asc ,  periods asc";
        List<DataTask.Info> infoList = querYInfoList(testMysql, str);
        return infoList;

    }

    @NotNull
    public static List<DataTask.Info> querYInfoList(TestMysql mysql, String str) {
        ResultSet resultSet = mysql.queryData(str);
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
        return infoList;
    }

    public static void matchInfo( Map<Integer, List<DataTask.Info>> hisMap ,String number, String issue) {
        DataTask.Info info = new DataTask.Info();
        info.number = number;
        info.niuniu = NiuNIuMatch.matchNiu(info.number) + "";
        info.periods = issue;
        info.location = CoreMath.mth(number) - 1;
        info.detail = MatchCore.detailS[info.location];
        info.order = info.periods.substring(info.periods.length() - 3, info.periods.length());
        info.date = issue.substring(0, 8);
        saveHisMap(hisMap,info);
    }

    public static void saveHisMap( Map<Integer, List<DataTask.Info>> hisMap  ,DataTask.Info info) {
        if (hisMap.containsKey(info.location)) {
            List<DataTask.Info> infos = hisMap.get(info.location);
            boolean hava = false;
            for (DataTask.Info datain : infos) {
                if (datain.periods == info.periods) {
                    hava = true;
                    break;
                }
            }
            if (!hava)
                infos.add(info);
        } else {
            ArrayList<DataTask.Info> value = new ArrayList<>();
            value.add(info);
            hisMap.put(info.location, value);
        }
    }

    public static Map<Integer, DataTask.Info> outInfo( Map<Integer, List<DataTask.Info>> hisMap ) {
        Map<Integer, DataTask.Info> maxInfoMap = new HashMap<>();
        Iterator<Integer> iterator = hisMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            List<DataTask.Info> infos = hisMap.get(next);
            DataTask.Info info = getMaxInfo(infos);
            maxInfoMap.put(next, info);
        }

        return maxInfoMap;
    }

    private static DataTask.Info getMaxInfo(List<DataTask.Info> infos) {
        DataTask.Info info = null;
        for (int i = 0; i < infos.size(); i++) {
            DataTask.Info info1 = infos.get(i);
            if (info == null)
                info = info1;
            else {
                if (Long.valueOf(info1.periods) > Long.valueOf(info.periods))
                    info = info1;
            }
        }
        return info;
    }
    public static void sendNotifyMessage( TicketInfo info ,Integer integer, long l, String currentIssue, String periods) {
        // [监控提醒] 当前 [三条] 已经有[20]期未出现请留意，当前销售期数为[210051]上一次出现期数为 [20120212]
        String msg = "账号 " + info.account + " [监控提醒]当前 [" +  MatchCore.detailS[integer] + "] 已经有[" + (l-1) + "]期未出现了，敬请留意，当前销售期数为[" + currentIssue + "]上一次出现期数为 [" + periods + "]";
        Main.pushAllMessage(msg);
    }
    public static void outMessage(TicketInfo ticketInfo, WinInfo winInfo, Map<Integer, DataTask.Info> infoMap) {
        Set<Integer> keySet = infoMap.keySet();
        for (Integer integer : keySet) {
            DataTask.Info info = infoMap.get(integer);
            long l = Long.valueOf(winInfo.currentIssue) - Long.valueOf(info.periods);
            Out.e("[" + MatchCore.detailS[integer] + "]最近一次出现：" + info.periods + " 距离当前投注期数：" + l);
            if (l - 1 >= MatchCore.thresholdS[integer] && MatchCore.messAgeSends[integer] != l) {
                MatchCore.sendNotifyMessage(ticketInfo, integer, l, winInfo.currentIssue, info.periods);
            }
            MatchCore.messAgeSends[integer] = l;
        }
    }

    public static  void waitMarketOpen(TicketInfo info , WaitTimeCall call) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Out.e(info.tag, "关闭本次跟注 等待开盘");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Out.d(calendar.getTime().toString());
                calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(info.openMarket));
                calendar.set(Calendar.MILLISECOND ,0);
                Out.d(calendar.getTime().toString());
                while (true) {
                    if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                        call.onTimeOpen(System.currentTimeMillis());
                        return;
                    }
                  
                    call.onNoTime(calendar.getTimeInMillis());
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}
