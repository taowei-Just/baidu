package baidu.Ecai.domain;

import baidu.Ecai.DoPour_1_1;
import baidu.Ecai.Main;
import baidu.Ecai.iml.IDoPour;
import baidu.Ecai.iml.IDooRun;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.bean.WinInfo;
import baidu.utils.Elementutil;
import baidu.utils.LogUtils;

import baidu.utils.Out;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.SimpleDateFormat;
import java.util.*;

public class Do01_1Runn implements IDooRun {

    private LogUtils logUtils;
    private Elementutil elementutil;
    RemoteWebDriver webDriver;
    TicketInfo info;
    List<String> windowIds = new ArrayList<>();
    DoPour_1_1 doPour;


    public Do01_1Runn(RemoteWebDriver webDriver, TicketInfo info) {
        this(webDriver, info, new ArrayList<>());
    }

    public Do01_1Runn(RemoteWebDriver webDriver, TicketInfo info, List<String> windowIds) {
        this.webDriver = webDriver;
        this.info = info;
        this.windowIds = windowIds;
        this.elementutil = new Elementutil(webDriver);
        logUtils = new LogUtils("e:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\" + getClass().getSimpleName() + "" + info.tag + ".txt");
        doPour = new DoPour_1_1(webDriver, info, windowIds);
   
    }


    @Override
    public void start() {
        try {
            doPour.waitDialog();
            String principal = doPour.readPrincipal();
            message("账号 [ " + info.account + " ] 投注：[ " + info.indede + " ] 本金：[ " + principal + " ]");
            
            int data = doPour.matchData(doPour.getHistories());
            WinInfo currentWininfo = doPour.waitingIssue();
            message("当前正在投注的是第[ " + currentWininfo.currentIssue + " ]期");
            
            List<Long> longs = doPour.mathPriods(currentWininfo.currentIssue, data);
            Map<Long, PourInfo> pourInfoMap = doPour.matchBetAmount02(longs);
             currentWininfo = doPour.waitingIssue();
            if (Long.valueOf(currentWininfo.currentIssue) > Long.valueOf(longs.get(0))) {
                start();
                return;
            }
            //开始执行
            doOperate(longs,pourInfoMap);
        } catch ( Exception e) {
            e.printStackTrace();
           restart();
        }
    }

    private void doOperate(List<Long> longs, Map<Long, PourInfo> pourInfoMap) {
        
        for (int i = 0; i < longs.size(); i++) {
            Long aLong = longs.get(i);
            PourInfo pourInfo = pourInfoMap.get(aLong);
            logUtils.saveLog2File("第[" + (i+1)+"]期 " + pourInfo.toString());

            if (pourInfo.win < -10) {
                logUtils.saveLog2File(" 已经投注" + i + "期 达到熔断机制 执行熔断");
                message("账号 ["+ info.account+"]_达到投注熔断已停止投注，请及时关注！" + " 剩余本金 "+  doPour.readPrincipal());
                restart();
                return;
            }

            WinInfo winInfo = doPour.waitCurrentWinInfo(aLong);
            Out.e(winInfo.toString());
            if (winInfo.wiState == 1){
                // 已中奖 终止投注
                if (doPour.wins(winInfo)){
                    message("第 [" + winInfo.issue+"] 期已中奖  [" +(winInfo.toString()) + "] 剩余本金：" +  doPour.readPrincipal());
                }else {
                    logUtils.saveLog2File("第 [" + winInfo.issue+"] 期已中奖   但未投注 损失 " +pourInfo.total*IDoPour.dubS[info.dub]+"元");
                }
                doPour.cancellations();
                if (doPour.matchExit(winInfo))
                    return;
                if (i==0 && winInfo.currentIssue.equals(""+ aLong)) {
                    ddpour(i, aLong, pourInfo);
                    loopWin(aLong);
                }  else {
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    restart();
                    break;
                }
            }else {
                //未中奖继续投注
                if ( winInfo.currentIssue.equals(""+ aLong))
                     ddpour(i, aLong, pourInfo);
            }
        }
    }

    private void loopWin(Long aLong) {

        WinInfo winInfo = doPour.waitCurrentWinInfo(aLong);
        while (winInfo.equals("" + aLong)) {
            Out.e(winInfo.toString());
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void ddpour(int i, Long aLong, PourInfo pourInfo) {
        doPour.pourCount =0 ;
        doPour.pour(i,aLong ,pourInfo.moey);
    }


    private void restart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
        
    }


    private void message(String s) {
        logUtils.saveLog2File(s);
        Main.pushAllMessage(s);
    }
}
