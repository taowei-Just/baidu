package baidu.Ecai;

import baidu.bean.MatchWinInfo;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.utils.Out;

import java.util.ArrayList;
import java.util.List;

public class MatchWin {
     public static double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};

    private static MatchWinInfo matchliangduiWinInfo;
    private static MatchWinInfo danduiWinInfo;
    private static MatchWinInfo shanhaoWinInfo;


    public static MatchWinInfo[] matchInfoS = new MatchWinInfo[]{null, null, null, null,
            new MatchWinInfo(),
            new MatchWinInfo().
                    setInde(5)
                    .setPresent(0.1)
                    .setDecreasP(0.1)
                    .setZhiSunP(0.3),
            new MatchWinInfo()
                    .setInde(6)
                    .setAssign(1)
                    .setPresent(0.2)
                    .setDecreasP(0.3)
                    .setZhiSunP(0.3),
            new MatchWinInfo()
                    .setInde(7)
                    .setAssign(1)
                    .setPresent(0.3)
                    .setDecreasP(0.2)};

    static {
        matchliangduiWinInfo = new MatchWinInfo().
                setInde(5)
                .setPresent(0.1)
                .setDecreasP(0.1)
                .setZhiSunP(0.3);
        danduiWinInfo = new MatchWinInfo()
                .setInde(6)
                .setAssign(1)
                .setPresent(0.2)
                .setDecreasP(0.3)
                .setZhiSunP(0.3);
        shanhaoWinInfo = new MatchWinInfo()
                .setInde(7)
                .setAssign(1)
                .setPresent(0.3)
                .setDecreasP(0.2);
    }

    public MatchWin(double[] mults) {
        this.mults = mults;
    }

    public static MatchWinInfo creatMatchInfo(TicketInfo info) {
        MatchWinInfo matchInfo = null;
        for (int i = 0; i < matchInfoS.length; i++) {
            matchInfo = matchInfoS[i];
            if (matchInfo == null)
                continue;
            if (matchInfo.inde == info.indede) {
                if (!info.useDefaultPresent)
                    matchInfo.setPresent(info.precent);
                    matchInfo.setMaxMoney(info.money)
                        .setZhiSunP(info.stopLoss)
                            .setFirstM(info.first)
                        .setAssign(info.expectW)
                    .setRongduaPre(info.rongDuanPre)
                    .setDecreasP(info.decreasP)
            .setZhisunMoney(info.zhisunMoney);
                break;
            }
        }

        return matchInfo;
    }

    public List<PourInfo> matchWin(MatchWinInfo matchWinInfo) {
        return matchWin(matchWinInfo, 200, false);
    }

    public List<PourInfo> matchWin(MatchWinInfo matchWinInfo, int count) {
        return matchWin(matchWinInfo, count, false);
    }

    public List<PourInfo> matchWin(MatchWinInfo matchWinInfo, int count, boolean useRongd) {
        List<PourInfo> doubles = new ArrayList<>();

        double present = matchWinInfo.present;
        double assign = matchWinInfo.assign;
        boolean zhisunmode = false;
        double mult = mults[matchWinInfo.inde];
        double total = 0;
        for (int i = 0; i < count; i++) {
            double v ;
            if(i==0 && matchWinInfo.firstM >0){
                v= matchWinInfo.firstM;
            }else {
                v = matchAssign(mult, total, assign, present, matchWinInfo.maxMoney, zhisunmode, matchWinInfo.zhisunLoseP);
                total += v;
            }
//            if ((total - maxMoney) / maxMoney > rongduaPre)
//                return -1;
//}
            if (useRongd) {
                if ((total / matchWinInfo.maxMoney >= matchWinInfo.rongduaPre || matchWinInfo.rongduanMoney > 0 ? total >= matchWinInfo.rongduanMoney : false ) ) {
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

            PourInfo pourInfo = new PourInfo();
            pourInfo.moey = v;
            pourInfo.id = i;
            pourInfo.win = (v * mult) - total;
            pourInfo.total = total;
            doubles.add(pourInfo);
            Out.d("计算第：" + (i + 1) + "项 值为：" + v + " total " + total + " present " + pv * 100 + "%");
            Out.d(" 验证 " + ((v * mult) - total));

        }
        return doubles;
    }
//    public List<PourInfo> matchWin(MatchWinInfo matchWinInfo, int count, boolean useRongd) {
//        List<PourInfo> doubles = new ArrayList<>();
//
//        double present = matchWinInfo.present;
//        double assign = matchWinInfo.assign;
//        boolean zhisunmode = false;
//        double mult = mults[matchWinInfo.inde];
//        double total = 0;
//        for (int i = 0; i < count; i++) {
//            double v = matchAssign(mult, total, assign, present, matchWinInfo.maxMoney, zhisunmode, matchWinInfo.zhisunLoseP);
//            total += v;
////            if ((total - maxMoney) / maxMoney > rongduaPre)
////                return -1;
////
//            if (useRongd)
//                if (total / matchWinInfo.maxMoney >= matchWinInfo.rongduaPre || matchWinInfo.rongduanMoney > 0 ? total >= matchWinInfo.rongduanMoney : false)
//                    return doubles;
//
//            double pv = ((v * mult) - total) / total;
//            double v1 = (matchWinInfo.maxMoney - total) / matchWinInfo.maxMoney;
//            if (v1 < matchWinInfo.zhiSunP || ( matchWinInfo.zhisunMoney > 0 ? total > matchWinInfo.zhisunMoney : false)) {
//                zhisunmode = true;
//                present -= matchWinInfo.decreasP;
//                present = present < 0 ? 0 : present;
//                assign -= assign * matchWinInfo.decreasP;
//            }
//            PourInfo pourInfo = new PourInfo();
//            pourInfo.moey = v;
//            pourInfo.id = i;
//            pourInfo.win = (v * mult) - total;
//            pourInfo.total = total;
//            doubles.add(pourInfo);
//            Out.d("计算第：" + (i + 1) + "项 值为：" + v + " total " + total + " present " + pv * 100 + "%");
//            Out.d(" 验证 " + ((v * mult) - total));
//
//        }
//        return doubles;
//    }

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
