package baidu.Ecai.domain;

import baidu.Ecai.DoPour_1_1;
import baidu.Ecai.Main;
import baidu.Ecai.iml.IDoPour;
import baidu.Ecai.iml.IDooRun;
import baidu.M960424_San_Tiao_1_1;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.bean.WinInfo;
import baidu.utils.Elementutil;
import baidu.utils.LogUtils;

import baidu.utils.Out;
import com.TestMysql;
import com.runn.DataTask;
import matchore.MatchCore;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.SimpleDateFormat;
import java.util.*;

public class Do01_1Runn implements IDooRun {

    private static LogUtils logUtils;
    private Elementutil elementutil;
    RemoteWebDriver webDriver;
    TicketInfo info;
    List<String> windowIds = new ArrayList<>();
    DoPour_1_1 doPour;
    private TestMysql testMysql;
    Map<Integer, List<DataTask.Info>> hisMap = new HashMap<>();

    public Do01_1Runn(RemoteWebDriver webDriver, TicketInfo info) {
        this(webDriver, info, new ArrayList<>());
    }

    public Do01_1Runn(RemoteWebDriver webDriver, TicketInfo info, List<String> windowIds) {
        this.webDriver = webDriver;
        this.info = info;
        this.windowIds = windowIds;
        this.elementutil = new Elementutil(webDriver);
        logUtils = new LogUtils("e:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\" + getClass().getSimpleName() + "" + info.tag + ".txt");
        try {
            doPour = new DoPour_1_1(webDriver, info, windowIds);
            testMysql = new TestMysql("ticket_data_vr_1_1");
        } catch (Exception e) {
            e.printStackTrace();
         new Main().start(info);   
        }
    }


    public void start1() {

        while (true) {

            Out.e("测试正常投注");
            long l = Long.parseLong(doPour.findCurrent());
            ddpour(0, l, 1d);
            Elementutil.wait_(2);
            Out.e("撤单。。");
            doPour.cancellations();
            Elementutil.wait_(15);
        }
    }

    @Override
    public void start() {
        if (doPour ==null ||testMysql ==null )
            return;
        hisMap.clear();
        try {
            Out.e("1等待警示弹窗");
            doPour.waitDialog();
            Out.e("2获取当前本金");

            String principal = doPour.readPrincipal();
            message("账号 [ " + info.account + " ] 投注：[ " + info.indede + " ] 本金：[ " + principal + " ]");
            Out.e("3获取当前历史列表");
            int data = 0;
//            = doPour.matchData(doPour.getHistories());

            Out.e("4获取当前投注信息");
            WinInfo currentWininfo = doPour.waitingIssue();
            message("当前正在投注的是第[ " + currentWininfo.currentIssue + " ]期 ");

            Out.e("5获取当日历史数据 ");
            List<DataTask.Info> infos = MatchCore.querYInfoNet(testMysql, currentWininfo);

            MatchCore.matchInfo(hisMap, infos);
            
            data = doPour.matchData(infos, 0);
            Out.e("6获取当前历史 距离  " + data);
            Out.e("7 匹配下注金额列表");

            List<PourInfo> pourInfos = MatchCore.getDanduiPourInfos(info);

//            MatchCore.matchWinMoney(testMysql, infos, pourInfos, info.indede);
            Out.e("8 获取历史数据中最大间隔数");

            Map<Integer, Integer> toatalMax = MatchCore.toatalMax(testMysql, info.indede);
            Out.e("9 设置当前投注跳过间隔");
            MatchCore.dynamicIndes(pourInfos, infos, toatalMax, info.indede);
            Out.e("11   得到下注期号列表");
            List<Long> longs = doPour.mathPriods(currentWininfo.currentIssue, data);
            Out.e("12   期号列表与下注金额匹配");
            Map<Long, PourInfo> pourInfoMap = doPour.matchBetAmount03(longs, pourInfos);
            Out.e("13 等待当前出奖号码");
            currentWininfo = doPour.waitingIssue();
            Out.e("14  判断当前号码是否与下注号码匹配");

            if (Long.valueOf(currentWininfo.currentIssue) > Long.valueOf(longs.get(0))) {
                restart();
                return;
            }

            Out.e("15  开始执行下注程序 ");

            //开始执行
            doOperate(longs, pourInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
            restart();
        }
    }

    private void doOperate(List<Long> longs, Map<Long, PourInfo> pourInfoMap) {

        Out.e(longs.toString());
        Out.e(pourInfoMap.toString());
        for (int i = 0; i < longs.size(); i++) {
            Long aLong = longs.get(i);
            PourInfo pourInfo = pourInfoMap.get(aLong);
            logUtils.saveLog2File("第[" + (i + 1) + "]期 " + pourInfo.toString());

            if (pourInfo.win < -10) {
                logUtils.saveLog2File(" 已经投注" + i + "期 达到熔断机制 执行熔断");
                message("账号 [" + info.account + "]_达到投注熔断已停止投注，请及时关注！" + " 剩余本金 " + doPour.readPrincipal());
                restart();
                return;
            }

            Out.e(" 等待出奖号码 ");
            WinInfo winInfo = doPour.waitCurrentWinInfo(aLong);
            Out.e("等待出奖号码 为" + winInfo.toString());
            MatchCore.matchInfo(hisMap, winInfo.Numner, winInfo.issue);
            MatchCore.outMessage(info, winInfo, MatchCore.outInfo(hisMap));
            
            if (winInfo.winInde <4){
             
                message("大奖["+ MatchCore.detailS[winInfo.winInde]+"] 出现了！历史出现最大间隔["+ MatchCore.maxTotal(testMysql, info.indede)+"]敬请关注！");
            }
            if (winInfo.wiState == 1) {
                // 已中奖 终止投注
                if (doPour.wins(winInfo)) {
                    message("第 [" + winInfo.issue + "] 期已中奖  [" + (winInfo.toString()) + "] 剩余本金：" + doPour.readPrincipal());
                } else {
                    logUtils.saveLog2File("第 [" + winInfo.issue + "] 期已中奖   但未投注 损失 " + pourInfo.total * MatchCore.dubS[info.dub] + "元");
                }
                doPour.cancellations();
                if (doPour.matchExit(winInfo))
                    return;
                if (i == 0 && winInfo.currentIssue.equals("" + aLong)) {
                    ddpour(i, aLong, pourInfo.moey);
                    loopWin(aLong);
                } else {
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    restart();
                    break;
                }
            } else {
                //未中奖继续投注
                if (winInfo.currentIssue.equals("" + aLong)) {
                    Out.e(" 是目标期数 " + aLong + "  进行下注 ");
                    ddpour(i, aLong, pourInfo.moey);
                } else
                    Out.e(" 不是是目标期数 " + aLong + "  不进行下注 ");

                doPour.waitDialog();
                String principal = doPour.readPrincipal();
                double aDouble = Double.parseDouble(principal);
                Out.d("当前余额为：" +aDouble);

                double moneyS = 0;
                for (int j  = i+1; j <(5>longs.size()? 5 :longs.size()-j) ; j++) {
                    moneyS+= pourInfoMap.get(aLong).moey;
                    if (aDouble <moneyS){
                        message(" 余额预警： 当前余额不足后："+(j-i)+"期投注 \n当前已投注 ["+(i+1)+"]期 剩余投注长度：["+ (pourInfoMap.size()-i-1)+"] 敬请留意！");
                    }
                        break;
                }
            }
            
        }
    }

    private void loopWin(Long aLong) {

        WinInfo winInfo = doPour.waitCurrentWinInfo(aLong);
        while (winInfo.equals("" + aLong)) {
            Out.e(winInfo.toString());
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void ddpour(int i, Long aLong, Double moey) {
        doPour.pourCount = 0;
        doPour.pour(i, aLong, moey);
    }


    private void restart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();

    }


    public static void message(String s) {
        if (logUtils!=null)
        logUtils.saveLog2File(s);
        s =" \n时间："+new SimpleDateFormat("yyyyMMdd HH:mm:ss.S").format(new Date(System.currentTimeMillis()))+" \n"+s;
        Main.pushAllMessage(s);
    }
}
