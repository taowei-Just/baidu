package baidu.Ecai.domain;

import baidu.Ecai.DoPour_1_1;
import baidu.Ecai.Main;
import baidu.Ecai.iml.IDooRun;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.bean.WinInfo;
import baidu.utils.Elementutil;
import baidu.utils.LogUtils;
import baidu.utils.Out;
import com.TestMysql;
import com.runn.DataTask;
import matchore.MatchCore;
import matchore.WaitTimeCall;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.SimpleDateFormat;
import java.util.*;

public class Do01_1RunnJK implements IDooRun {

    private static LogUtils logUtils;
    private Elementutil elementutil;
    RemoteWebDriver webDriver;
    TicketInfo info;
    List<String> windowIds = new ArrayList<>();
    DoPour_1_1 doPour;
    private TestMysql testMysql;
    Map<Integer, List<DataTask.Info>> hisMap = new HashMap<>();
    int maxIss = 1260;

    public Do01_1RunnJK(RemoteWebDriver webDriver, TicketInfo info) {
        this(webDriver, info, new ArrayList<>());
    }

    public Do01_1RunnJK(RemoteWebDriver webDriver, TicketInfo info, List<String> windowIds) {
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
        if (doPour == null || testMysql == null)
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


            Out.e("15  开始执行下注程序 ");

            //开始执行
            doOperate(longs, pourInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
            restart();
        }
    }

    String lastIssue = "";

    private void doOperate(List<Long> longs, Map<Long, PourInfo> pourInfoMap) {

        while (true) {
            WinInfo winInfo = doPour.waitingIssue();
            if (winInfo == null || winInfo.issue == null || winInfo.issue.equals(lastIssue))
                continue;
            lastIssue = winInfo.issue;
            Out.d(lastIssue);
            if (winInfo.winInde < 5) {
                String str =  "[VR1.1 彩] 大奖[" + MatchCore.detailS[winInfo.winInde] + "] 出现了！期号["+ winInfo.issue+"]历史出现最大间隔[" +( testMysql == null ? "【？？】" : MatchCore.maxTotal(testMysql, winInfo.winInde)) + "]敬请关注！";
                message(str );
            }
            
            ddpour(  1,Long.parseLong(winInfo.currentIssue) , 1d);
            doPour.cancellations();
            if (matchExit(winInfo))
                return;

            try {
                Thread.sleep(3*1000);
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
        if (logUtils != null)
            logUtils.saveLog2File(s);
        s = " \n时间：" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.S").format(new Date(System.currentTimeMillis())) + " \n" + s;
        Main.pushAllMessage(s);
    }


    public boolean matchExit(WinInfo winInfo) {
        String substring = winInfo.issue.substring(8, winInfo.issue.length());
        System.out.println(substring);
        if (maxIss - Long.valueOf(substring) < 50) {
            webDriver.quit();
            MatchCore.waitMarketOpen(info, new WaitTimeCall() {
                @Override
                public void onTimeOpen(long timeInMillis) {
                    Out.e("开盘了 》》");
                    new Main().start(info);
                    Main.pushAllMessage("开盘时间到，启动[ 监控]下注 当前时间" + new Date(System.currentTimeMillis()) + "");
                }

                @Override
                public void onNoTime(long timeInMillis) {
                    Out.e("》》等待开盘 " + new Date(System.currentTimeMillis()).toString());
                }
            });
            return true;
        }
        return false;
    }

}
