package Test;

import baidu.Ecai.MatchWin;
import baidu.bean.MatchWinInfo;
import baidu.bean.TicketInfo;
import baidu.utils.LogUtils;
import baidu.utils.Out;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 1.跟据计算得出每一次下注的最佳值
 * 2.根据历史数据调整下注的盈利比
 * 3.根据总金额调整盈利比计算出最小损失
 * <p>
 * 盈利最大化
 * 亏损最小化
 */

public class Match_Test01 {

    private LogUtils logUtils;
    private double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};

    public Match_Test01() {
        this.logUtils = new LogUtils("e:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\" + getClass().getSimpleName() + "" + ".txt");

    }

    public static void main(String[] args) {

        Match_Test01 match_test01 = new Match_Test01();
        match_test01.start();
    }

    private void start() {
        Out.d("start");
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setFirst(1)
                .setDub(3)
                .setMoney(400)
                .setPrecent(0.6)
                .setMulripe(1)
                .setIndede(4);


        /**
         *
         * 熔断值  
         * 熔断百分比
         * 最低接收回本
         * 盈利百分比
         *
         */

//                    matchWin(new MatchWinInfo());  

        MatchWinInfo matchliangduiWinInfo = new MatchWinInfo().
                setInde(5)
                .setPresent(0.1)
                .setDecreasP(0.1)
                .setZhiSunP(0.3);
//            matchWin(matchliangduiWinInfo);  

        MatchWinInfo danduiWinInfo = new MatchWinInfo()
                .setInde(6)
                .setAssign(1)
                .setPresent(0.2)
                .setDecreasP(0.3)
                .setZhiSunP(0.3);
//            matchWin(danduiWinInfo);  

        MatchWinInfo shanhaoWinInfo = new MatchWinInfo()
                .setInde(7)
                .setAssign(1)
                .setPresent(0.3)
                .setDecreasP(0.2);
//        matchWin(shanhaoWinInfo);


        TicketInfo info01 = new TicketInfo();
        info01.account = "wtw960424";
        info01.password = "953934cap";
        info01.tag = "_San_Tiao_";
        info01.minIss = 60;
        info01.mulripe = 1;
        info01.dub = 3;
        info01.precent = 0.9;
        info01.ticketKind = 1;
        info01.money = 10000;
        info01.stopLoss = 0.1;
        info01.first = 1;
        info01.expectW = 10;
        info01.indede = 6;

        MatchWinInfo matchWinInfo = MatchWin.creatMatchInfo(info01);
        matchWinInfo.setRongduaPre(2);
        System.out.println(matchWinInfo.toString());
        logUtils.saveLog2File(matchWinInfo.toString());
        new MatchWin(mults).matchWin(matchWinInfo, 200, true);
    }

    public List<Double> matchWin(MatchWinInfo matchWinInfo) {
        return matchWin(matchWinInfo, false);
    }

    public List<Double> matchWin(MatchWinInfo matchWinInfo, boolean useRongd) {
        List<Double> doubles = new ArrayList<>();

        double present = matchWinInfo.present;
        double assign = matchWinInfo.assign;
        boolean zhisunmode = false;
        double mult = mults[matchWinInfo.inde];
        double total = 0;
        for (int i = 0; i < 200; i++) {
            double v = matchAssign(mult, total, assign, present, matchWinInfo.maxMoney, zhisunmode, matchWinInfo.zhisunLoseP);
            total += v;
//            if ((total - maxMoney) / maxMoney > rongduaPre)
//                return -1;
//
            if (useRongd) {

                if (total / matchWinInfo.maxMoney >= matchWinInfo.rongduaPre || matchWinInfo.rongduanMoney > 0 ? total >= matchWinInfo.rongduanMoney : false) {

                    Out.d(" 触发熔断 " + (i + 1) + "熔断值：" + matchWinInfo.rongduanMoney + ":" + matchWinInfo.rongduaPre * matchWinInfo.maxMoney + " _ 预计亏损：" + (total - v));
                    return doubles;
                }
            }

            double pv = ((v * mult) - total) / total;
            double v1 = total / matchWinInfo.maxMoney;

            if (v1 > matchWinInfo.zhiSunP || (matchWinInfo.zhisunMoney > 0 ? total > matchWinInfo.zhisunMoney : false)) {
                Out.d(" 到达减损 " + i + " _ " + v1);

                zhisunmode = true;
                present -= matchWinInfo.decreasP;
                present = present < 0 ? 0 : present;
                assign -= assign * matchWinInfo.decreasP;
            }
            doubles.add(v);
            Out.d("计算第：" + (i + 1) + "项 值为：" + v + " total " + total + " present " + pv * 100 + "%");
            Out.d(" 验证 " + ((v * mult) - total));

        }
        return doubles;
    }

    private double matchAssign(double mult, double total, double assign, double present, double maxMoney, boolean zhisunMode, double zhisunLoseP) {
        for (int i = 0; i < maxMoney; i++) {
//            if (zhisunMode) {
//                assign -= maxMoney * zhisunLoseP;
//            }
//            
            double v = getV(mult, total, assign + i);

            if ((v * mult) - total - v >= assign && (v * mult) - total - v >= present * (total + v)) {
                return v;
            }
        }
        return -1;
    }

    private boolean zhisunMode() {
        // 允许最大亏损值

        return false;
    }


    private double getV(double mult, double total, double assign) {
        double v1 = total / (mult - 1) + assign / mult;
        v1 += v1 % 1 > 0 ? 1 : 0;
        return (int) (v1 > 1 ? v1 : 1);
    }

}
