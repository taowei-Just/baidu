package baidu.Ecai;

import baidu.Ecai.iml.IDoPour;
import baidu.bean.BettingRecord;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.bean.WinInfo;
import baidu.utils.CoreMath;
import baidu.utils.Elementutil;
import baidu.utils.LogUtils;
import baidu.utils.Out;
import com.PattenUtil;
import com.runn.DataTask;
import matchore.MatchCore;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.SimpleDateFormat;
import java.util.*;

 

public class DoPour_1_1 implements IDoPour {

    public LogUtils logUtils;
    public Elementutil elementutil;
    public WinInfo lastWinInfo = null;
    public RemoteWebDriver webDriver = null;
    public TicketInfo info = null;
    public int pourCount = 0;
    List<String> windowIds;

    public DoPour_1_1(RemoteWebDriver webDriver, TicketInfo info, List<String> windowIds) {
        this.webDriver = webDriver;
        this.info = info;
        this.windowIds = windowIds;
        this.elementutil = new Elementutil(webDriver);
        logUtils = new LogUtils("e:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\" + getClass().getSimpleName() + "" + info.tag + ".txt");
        init();
    }

    public static void main(String[] args) {

        DoPour_1_1 doPour = new DoPour_1_1(null, new Main.Info());
        doPour.info.tag = "test_match";


    }

    public DoPour_1_1(RemoteWebDriver webDriver, TicketInfo info) {
        this(webDriver, info, new ArrayList<>());
    }


    public void start() {
        /**
         *
         * 查看历史数据
         *
         */
//        webDriver.manage().window().maximize();

        message("启动[", "]投注！");
        lisStrS.clear();
        waitDialog();
//        cancellations();
        String principal = readPrincipal();
        logUtils.saveLog2File("本金：" + principal);
        Main.pushAllMessage("本金：" + principal);
        List<History> histories = getHistories();
        Out.e(info.tag, " histories " + histories.toString());

        // 匹配数据
        int inde = matchData(histories);
        // 生成下注组合
        // 执行下注
        Out.e(info.tag, Thread.currentThread() + "inde " + inde);
        try {
            loopIngQueue(waitingIssue().currentIssue, inde);
        } catch (Exception e) {
            e.printStackTrace();
            restart();
        }
    }

    private void message(String s, String s2) {
        Main.pushAllMessage(s + info.account + "_" + detailS[info.indede] + s2);
    }

    @NotNull
    public List<History> getHistories() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[1]/div[5]/div[1]/a[1]");
        WebElement element = webDriver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[1]/div[5]/div[1]/a[1]/div"));
        String innerHTML = element.getAttribute("innerHTML");
        innerHTML = PattenUtil.replaceBlank(innerHTML).replace(" ", "");
        Out.e(innerHTML);
        String regex = "<divclass=\"history_numberhistory_number_02\">(.*?)</div><div";
        List<String> subUtil = PattenUtil.getSubUtil(innerHTML, regex, true);
        List<History> histories = new ArrayList<>();
        for (String s : subUtil) {
            History history = new History();
            histories.add(history);
            regex = "<lidata-bind=\"text:serialNumber\"class=\"font-deeppurple\">(.*?)</li><!";
            List<String> subUtil1 = PattenUtil.getSubUtil(s, regex, false);
            regex = "class=\"circle_numbercircle_number_03bg-deeppurplefont-white\">(.*?)</li><";
            List<String> subUtil2 = PattenUtil.getSubUtil(s, regex, false);
            String number = "";
            for (String nn : subUtil2) {
                number += nn;
            }
            history.issue = subUtil1.get(0);
            history.number = number;
        }
        return histories;
    }

    public String readPrincipal() {

        WebElement walletAmount = webDriver.findElement(By.className("walletAmount"));
        if (!walletAmount.isDisplayed()) {
            elementutil.wait_(1);
            elementutil.clickId("showWalletAmount");
            elementutil.wait_(1);

        }
        return elementutil.readTextByClass("walletAmount");
    }

    public void restart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }

    public void loopIngQueue(String issue, int inde) throws Exception {
        Out.e(info.tag, " 当前正在投注：" + issue);
        //确定下注期数
        List<Long> issueLong = mathPriods(issue, inde);
        Map<Long, PourInfo> pourMap = matchBetAmount02(issueLong);
        operate(issueLong, pourMap);
    }

    @NotNull
    public Map<Long, Double> matchBetAmount(List<Long> issueLong) {
        Map<Long, Double> doubleMap = new HashMap<>();
        double expectwin = 1;
        double precent = info.precent;
        double lastTMult = 0;
        double lastnum = info.first;
        double last = 1;
        for (int k = 0; k < issueLong.size(); k++) {
            double mult = mults[info.indede];
            double n = matchWin(mult, last, lastnum, lastnum * precent, expectwin, lastTMult);
            last = n;
            lastnum += n;
            lastTMult = (n * mult - lastnum);
            if (lastnum < 0 || (n * mult - lastnum) < -5)
                break;
            doubleMap.put(issueLong.get(k), n);
            Out.e(info.tag, detailS[info.indede] + "第" + (k + 1) + "期 预计当期投入" + n + " 总投入 " + lastnum + " 当期盈利 ：" + n * mult + " 总盈亏状况 ：" + (n * mult - lastnum) + " 盈利百分比  " + (n * mult - lastnum) / lastnum * 100 + "%");
        }
        return doubleMap;
    }

    public Map<Long, PourInfo> matchBetAmount02(List<Long> issueLong) {
        Map<Long, PourInfo> doubleMap = new HashMap<>();
        List<PourInfo> pourInfos = new MatchWin(mults).matchWin(MatchWin.creatMatchInfo(info), issueLong.size());
        doPourMap(issueLong, doubleMap, pourInfos);
        Out.e(doubleMap.toString());
        return doubleMap;
    }

    private void doPourMap(List<Long> issueLong, Map<Long, PourInfo> doubleMap, List<PourInfo> pourInfos) {
        for (int i = 0; i < issueLong.size(); i++) {
            PourInfo info = null;
            if (i >= pourInfos.size())
                return;

            for (int j = 0; j < pourInfos.size(); j++) {
                if (pourInfos.get(i).id == i) {
                    info = pourInfos.get(i);
                    break;
                }
            }
            info.issue = issueLong.get(i) + "";
            Long key = issueLong.get(i);
            Out.e(key + "  " + info.toString());
            doubleMap.put(key, info);
        }
    }

    public Map<Long, PourInfo> matchBetAmount03(List<Long> issueLong, List<PourInfo> pourInfos) {
        Map<Long, PourInfo> doubleMap = new HashMap<>();

        doPourMap(issueLong, doubleMap, pourInfos);
        return doubleMap;
    }

    /**
     * @param currentIssue 最新一期数据
     * @param inde         目标出现的距离
     * @return
     */

    @NotNull
    public List<Long> mathPriods(String currentIssue, int inde) {
        Long aLong = Long.valueOf(currentIssue);
        List<Long> issueLong = new ArrayList<>();
        if (inde > -1 && MatchCore.indedeS[info.indede] - inde > 0) {
            for (int i = 0; i < MatchCore.countS[info.indede]; i++) {
                long e = new Random().nextInt(MatchCore.indedeS[info.indede] - inde);
                Out.e(info.tag, e + "");
                if (!issueLong.contains(aLong + e)) {
                    issueLong.add(aLong + e);
                }
            }
            Out.e(info.tag, issueLong.toString());
        } else {
            inde = MatchCore.indedeS[info.indede];
        }
        for (int i = 0; i < 100; i++) {
            issueLong.add(aLong + i + MatchCore.indedeS[info.indede] - inde);
        }
        for (int i = 0; i < issueLong.size() - 1; i++) {
            for (int j = i + 1; j < issueLong.size(); j++) {
                Long aLong1 = issueLong.get(i);
                Long aLong2 = issueLong.get(j);
                if (aLong2 < aLong1) {
                    issueLong.set(i, aLong2);
                    issueLong.set(j, aLong1);
                }
            }
        }
        return issueLong;
    }


    /**
     * @param currentIssue 最新一期数据
     * @param inde         目标出现的距离
     * @return
     */
    public List<Long> mathPriods02(String currentIssue, int inde) {

        List<Long> issueLong = new ArrayList<>();
        Long iss = new Long(0);
        Long aLong = Long.valueOf(currentIssue);
        int indede = MatchCore.indedeS[inde];

        if (inde == -1) {
            int i = indede - 10;
            if (i < 0) {
                iss = aLong + 1;
                issueLong.add(iss);
            } else {
                for (int j = 0; j < 3; j++) {
                    int i1 = new Random().nextInt(i);
                    if (!issueLong.contains(aLong + i1))
                        issueLong.add(aLong + i1);
                }
            }
        } else {
            int i = indede - inde;
            if (i > 0)
                issueLong.add(aLong + i);
            else
                issueLong.add(aLong + 1);

        }

        for (int i = 0; i < issueLong.size() - 1; i++) {
            for (int j = i + 1; j < issueLong.size(); j++) {
                Long aLong1 = issueLong.get(i);
                Long aLong2 = issueLong.get(j);
                if (aLong2 < aLong1) {
                    issueLong.set(i, aLong2);
                    issueLong.set(j, aLong1);
                }
            }
        }
        Long aLong1;
        if (issueLong.size() > 0)
            aLong1 = issueLong.get(issueLong.size() - 1);
        else
            aLong1 = aLong + 1;

        for (int i = 0; i < 100; i++) {
            issueLong.add(aLong1 + i + 1);
        }

        return issueLong;
    }

    public void operate(List<Long> issueLong, Map<Long, PourInfo> doubleMap) {
        Out.e(info.tag, "准备期数：" + issueLong.toString());
        Out.e(info.tag, "详情：" + doubleMap.toString());

        for (int i = 0; i < issueLong.size(); i++) {
            Long aLong = issueLong.get(i);
            PourInfo pourInfo = doubleMap.get(aLong);

            if (pourInfo.win < 0) {
                logUtils.saveLog2File(" 已经投注" + i + "期 达到熔断机制 执行熔断");
                message("当前账号", "_达到投注熔断已停止投注，请及时关注！");
                restart();
                return;
            }

            double aDouble = pourInfo.moey;
            Out.e(info.tag, " 等待下第" + (i + 1) + "注 期号 [" + aLong + "] 金额 [" + aDouble * MatchCore.dubS[info.dub] * info.mulripe + "] 元");
            logUtils.saveLog2File("准备下第" + (i + 1) + "注 期号 [" + aLong + "] 金额 [" + aDouble * MatchCore.dubS[info.dub] * info.mulripe + "] 元" + " 预计收益 " + pourInfo.win * MatchCore.dubS[info.dub] * info.mulripe + "元" + " 总投入：" + pourInfo.total * MatchCore.dubS[info.dub] * info.mulripe + " 元");
            /**
             *
             *   检查等待当前中奖号码
             *   已中奖  撤销上一期投注 重新判断
             *   未中奖  进行下一注投注
             */

            Out.d(info.tag, " [ 等待出奖号码... ] ");
            WinInfo winInfo;
            winInfo = waitCurrentWinInfo(aLong);

            // 中奖了但是不是投注的期数
            System.err.println("" + winInfo.toString());
            if (winInfo.wiState != 1 || (winInfo.wiState == 1 && !lisStrS.contains(winInfo.issue))) {
                pourCount = 0;
                pour(i, aLong, aDouble);
            } else {
                wins(winInfo);
                logUtils.saveLog2File("[ 中奖重置 ]" + winInfo.toString());
                cancellations();
                if (matchExit(winInfo))
                    return;
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (lastWinInfo == null || !lastWinInfo.issue.equals(winInfo.issue))
                    lastWinInfo = winInfo;

                restart();
                return;
            }
        }
    }

    public boolean matchExit(WinInfo winInfo) {
        String substring = winInfo.issue.substring(8, winInfo.issue.length());
        System.out.println(substring);
        if (maxIss - Long.valueOf(substring) < info.minIss && maxIss !=-1) {
            webDriver.quit();
            waitMarketOpen();
            return true;
        }
        return false;
    }

    public boolean wins(WinInfo winInfo) {
        if (lisStrS.contains(winInfo.issue)) {
            lisStrS.remove(winInfo.issue);
            return true;
        }
        return false;
    }

    @NotNull
    public WinInfo waitCurrentWinInfo(Long aLong) {
        WinInfo winInfo;
        while (true) {
            winInfo = waitingIssue();

            // 没有当前下注期号继续查询
            if (winInfo.currentIssue == null)
                continue;
            // 中奖了  
            if (winInfo.wiState == 1)
                break;
            // 没有中奖且当前投注期数为目标期数且没有投注
            if (winInfo.currentIssue.equals("" + aLong) && !lisStrS.contains("" + aLong))
                break;
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return winInfo;
    }


    public void pour(int i, Long aLong, Double aDouble) {
        waitDialog();
        bpttomPour(info.indede, info.dub, aDouble.intValue() * info.mulripe);
        pourCount++;
        boolean b = closeDialog();
        elementutil.wait_(1);
        if (!b && !confirmPour(i, aLong, aDouble, getBettingRecords())) {
            logUtils.saveLog2File("第" + (i + 1) + "注 期号 [" + aLong + "]金额 [" + aDouble * MatchCore.dubS[info.dub] * info.mulripe + "]元" + "  下注失败 !");
            if (pourCount <= info.reInCount) {
                logUtils.saveLog2File("重试第 " + pourCount + " 次下注  " + aLong);
                pour(i, aLong, aDouble);
            }
        } else {

            if (!lisStrS.contains(aLong + ""))
                lisStrS.add(aLong + "");
        }
    }


    public boolean confirmPour(int i, Long aLong, Double aDouble, List<BettingRecord> bettingRecords) {
        Out.e("查找确认是否下注完成");
        boolean have = false;
        for (int j = 0; j < bettingRecords.size(); j++) {
            BettingRecord bettingRecord = bettingRecords.get(j);
//            Out.e(bettingRecord.toString());
            if ((Long.parseLong(bettingRecord.priods) >= aLong && (bettingRecord.statue.equals("未开奖") || bettingRecord.statue.contains("中奖")))) {
                lisStrS.add(aLong + "");
                Out.e(info.tag, "第" + (i + 1) + "注 期号 [" + aLong + "]金额 [" + aDouble * MatchCore.dubS[info.dub] * info.mulripe + "]元" + "  下注完成 !");
                logUtils.saveLog2File("第" + (i + 1) + "注 期号 [" + aLong + "]金额 [" + aDouble * MatchCore.dubS[info.dub] * info.mulripe + "]元" + "  下注完成 !");
                have = true;
                break;
            }

        }
        return have;
    }

    public void waitMarketOpen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Out.e(info.tag, "关闭本次跟注 等待开盘");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Out.d(calendar.getTime().toString());
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(info.openMarket));
                Out.d(calendar.getTime().toString());
                while (true) {
                    if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                        Out.e("开盘了 》》");
                        new Main().start(info);
                        Main.pushAllMessage("开盘时间到，启动[" + detailS[info.indede] + "]下注 当前时间" + new Date(System.currentTimeMillis()) + "");
                        return;
                    }
                    Out.e("》》等待开盘 " + new Date(System.currentTimeMillis()).toString());
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public boolean closeDialog() {

        try {
            //利润上限提醒按钮

            if (webDriver.findElement(By.id("confirm-dialog")).isDisplayed()) {
                elementutil.clickPath("/html/body/div[4]/div[2]/div/div[3]/button[2]");
            }
            elementutil.wait_(1);
            if (webDriver.findElement(By.id("warning-dialog")).isDisplayed()) {
                WebElement path = webDriver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/p/p[1]/span"));
                String content = path.getText();
                if (path.isDisplayed() && content.contains("成功")) {
                    Out.e(info.tag, " text " + content);
                    elementutil.clickPath("/html/body/div[3]/div/div/div[3]/button");
                    return true;
                }
                elementutil.clickPath("/html/body/div[3]/div/div/div[3]/button");
            }
        } catch (Exception e) {
        }

        return false;
    }

    public void cancellations() {


        List<BettingRecord> bettingRecordList = getBettingRecords();
        for (BettingRecord bettingRecord : bettingRecordList) {
            Out.e(bettingRecord.toString());
        }
        for (int i = 0; i < lisStrS.size(); i++) {
            String s = lisStrS.get(i);
            for (int j = 0; j < bettingRecordList.size(); j++) {
                if (s.equals(bettingRecordList.get(j).priods) && bettingRecordList.get(j).statue.equals("未开奖")) {
                    Out.e("撤销这一期：" + bettingRecordList.get(j).toString());
//                    logUtils.saveLog2File("撤销当前期数：" + bettingRecordList.get(j).toString());
                    String path = "/html/body/div[2]/div/div[2]/div[1]/div[2]/div/div[2]/div/div[2]/div[1]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[" + (j + 1) + "]/td[8]/button/i";
                    elementutil.clickPath(path);
                    Elementutil.wait_(1);
                }
            }
        }
    }

    @NotNull
    public List<BettingRecord> getBettingRecords() {
        List<BettingRecord> bettingRecordList = new ArrayList<>();
        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/ul/li[1]/a");
            elementutil.wait_(2);
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/ul/li[1]/a");
            elementutil.wait_(2);

            String innerHTML = webDriver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/div[2]/div/div[2]/div[1]/div")).getAttribute("innerHTML");
            innerHTML = PattenUtil.replaceBlank(innerHTML).replace(" ", "");
            String regex = "class=\"odd\">(.*?)</tr>";
            List<String> stringList = PattenUtil.getSubUtil(innerHTML, regex, true);

            for (String st : stringList) {
                try {
//                    Out.e(st);
                    BettingRecord bettingRecord = matchBetting(st);
                    bettingRecordList.add(bettingRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bettingRecordList;
    }

    public BettingRecord matchBetting(String st) throws Exception {
        String regex;
        BettingRecord bettingRecord = new BettingRecord();

        regex = "<tdstyle=\"width:12%;\">(.*?)</td>";
        bettingRecord.desc = PattenUtil.getSubUtil(st, regex, false).get(0);
        regex = "<tdstyle=\"width:17%;\">(.*?)</td>";
        bettingRecord.type = PattenUtil.getSubUtil(st, regex, false).get(0);
        regex = "<tdstyle=\"width:15%;\">(.*?)</td>";
        bettingRecord.priods = PattenUtil.getSubUtil(st, regex, false).get(0);
        regex = "<tdstyle=\"width:14%;\">(.*?)</td>";
        bettingRecord.detail = PattenUtil.getSubUtil(st, regex, false).get(0);
        regex = "<tdstyle=\"width:12%;\">(.*?)</td><tdstyle=\"width:12%;\">";
        bettingRecord.money = PattenUtil.getSubUtil(st, regex, false).get(0);
        regex = "</td><tdstyle=\"width:12%;\">(.*?)</td><tdstyle=\"width:12%;\">";
        bettingRecord.win = PattenUtil.getSubUtil(st, regex, false).get(0);
        regex = "title=\"点击查看详情\">(.*?)</button>";
        bettingRecord.statue = PattenUtil.getSubUtil(st, regex, false).get(0);
        try {
            regex = "title=\"(.*?)\"style=";
            bettingRecord.cancellations = PattenUtil.getSubUtil(st, regex, false).get(0);
        } catch (Exception e) {
//            Out.d(e.toString());
        }

        return bettingRecord;
    }

    public WinInfo waitingIssue() {

        while (true) {
            try {
                WinInfo win = win();
                if (win.Numner == null)
                    continue;
                String text = findCurrent();
                win.currentIssue = text;

                if (win != null)
                    return win;

                Thread.sleep(1 * 1000);
            } catch (Exception e) {
//                Out.e(info.tag,""+e.toString());
            }

            return new WinInfo();
        }
    }

    @NotNull
    public String findCurrent() {
        WebElement curissue = webDriver.findElement(By.id("curissue"));
        String text = curissue.getText();
        text = text.replace(" ", "");
        text = text.substring(1, text.length() - 1);
        return text;
    }

    String lastlastissue = "";


    public WinInfo win() {
        WinInfo winInfo = new WinInfo();

        try {
//            Out.e("查询出奖号码");
            String lastissue = webDriver.findElement(By.id("lastissue")).getText();
            WebElement element = webDriver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[1]/div[4]/div"));
            WebElement lastdigit1 = element.findElement(By.id("lastdigit1"));
            WebElement lastdigit2 = element.findElement(By.id("lastdigit2"));
            WebElement lastdigit3 = element.findElement(By.id("lastdigit3"));
            WebElement lastdigit4 = element.findElement(By.id("lastdigit4"));
            WebElement lastdigit5 = element.findElement(By.id("lastdigit5"));

            String s = lastdigit1.getText() + lastdigit2.getText() + lastdigit3.getText() + lastdigit4.getText() + lastdigit5.getText();
            int mth = CoreMath.mth(s) - 1;

            lastlastissue = lastissue;
            winInfo.Numner = s;
            winInfo.issue = lastissue;
            winInfo.winInde = mth;

            if (mth == info.indede) {
                winInfo.wiState = 1;
                Out.d(info.tag, "第 " + lastissue + " 期 开奖号码为 " + s + "   [" + detailS[mth] + "]" + " [ 已中奖！ ]");
            } else {
                if (!lastissue.equals(lastlastissue))
                    Out.d(info.tag, "第 " + lastissue + " 期 开奖号码为 " + s + "   [" + detailS[mth] + "]" + " [ 未中奖！ ]");
                winInfo.wiState = 0;
            }
        } catch (Exception e) {
        }


        return winInfo;
    }


    public void bpttomPour(int i, int i1, int num) {

        switch (i) {
            case 0:
                click5tiao();
                break;
            case 1:
                clickzhadan();
                break;
            case 2:
                clickhulu();
                break;
            case 3:
                clickshunz();
                break;
            case 4:
                clicksantiao();
                break;
            case 5:
                clickliangdui();
                break;
            case 6:
                clickdandui();
                break;
            case 7:
                clicksanhao();
                break;
        }

        switch (i1) {
            case 0:
                select2Y();
                break;
            case 1:
                select1Y();
                break;
            case 2:
                select2J();
                break;
            case 3:
                select1J();
                break;
            case 4:
                select2F();
                break;

        }

        webDriver.findElement(By.id("exampleInputAmount")).clear();
        elementutil.sendTextByid("exampleInputAmount", "" + num);
//        elementutil.clickId("bet-confirmPour-fast");
        elementutil.clickPath("//*[@id=\"bet-confirm-fast\"]");

    }


    public int matchData(List<History> histories) {
        int index = MatchCore.indedeS[info.indede];
        for (int i = 0; i < histories.size(); i++) {
            int mth = CoreMath.mth(histories.get(i).number) - 1;
            if (mth == info.indede) {
                index = i;
                return index;
            }
        }
        return index;
    }


    public void select2F() {

        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/button");

        //2分
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/ul/li[5]/a");
    }

    public void select1J() {

        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/button");

        //1角
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/ul/li[4]/a");
    }

    public void select2J() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/button");

        //2角
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/ul/li[3]/a");
    }

    public void select1Y() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/button");
        //1元
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/ul/li[2]/a");
    }

    public void select2Y() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/button");

        //2元
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[4]/div[1]/div[1]/div/ul/li[1]/a");
    }

    public void clickzhadan() {
        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[2]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickhulu() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[3]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickshunz() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[4]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clicksantiao() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[5]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickliangdui() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[6]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickdandui() {
        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[7]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clicksanhao() {
        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[8]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void click5tiao() {
        try {
            //5tiao
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/div[2]/div[27]/div[2]/button[1]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param mult      倍率
     * @param last      上一次投注
     * @param lastnum   往期总投注
     * @param expectwin 期望盈利1
     * @param v         期望盈利2
     * @param lastTMult 上一期盈利
     * @return
     */
    public double matchWin(double mult, double last, double lastnum, double expectwin, double v, double lastTMult) {
//        matchWin02(mult, last, lastnum, expectwin, v , lastTMult);
        for (double i = last; i < Integer.MAX_VALUE; i++) {
            if (i * (mult - 1) > lastnum + expectwin && (i * (mult - 1) > v + i && i * (mult - 1) > lastnum * 2 || i * (mult - 1) > lastnum + lastTMult)) {
                return i;
            }
        }

        return -1;
    }

    public void waitDialog() {


        WebElement time = webDriver.findElement(By.className("time"));
        String hour = time.findElement(By.id("hour")).getText();
        String minute = time.findElement(By.id("minute")).getText();
        String second = time.findElement(By.id("second")).getText();

        Out.e(info.tag, "截止下注还剩：" + hour + ":" + minute + ":" + second);
        if (minute.equals("01") && Integer.valueOf(second) > 25) {
            for (int i = 0; i < 10; i++) {
                WebElement element = webDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button"));
                if (element.isDisplayed()) {
                    element.click();
                }
                elementutil.wait_(1);
                if (!webDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button")).isDisplayed())
                    return;
            }
        }

        if (minute.equals("00") && Integer.valueOf(second) < 5) {
            for (int i = 0; i < Integer.valueOf(second) + 6; i++) {

                WebElement element = webDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button"));
                if (element.isDisplayed()) {
                    element.click();
                }
                elementutil.wait_(1);
                if (!webDriver.findElement(By.xpath("/html/body/div[5]/div/div/div[3]/button")).isDisplayed())
                    return;
            }

        }


    }

    @Override
    public void init() {
        try {

            WebElement element = webDriver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[1]/div[2]/div/ul/li[3]/a/span"));
            System.err.println(element.getText());
            if (element.getText().contains("VR彩票"))
                element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        webDriver.close();

        Elementutil.wait_(1);
        Set<String> windowHandles = webDriver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (!windowIds.contains(next))
                windowIds.add(next);
        }

        webDriver.switchTo().window(windowIds.get(windowIds.size() - 1));

        System.err.println("当前界面id：" + webDriver.getWindowHandle());
        Elementutil.wait_(1);
        elementutil.waitDialog(webDriver);

        Actions actions = new Actions(webDriver);
        actions.moveToElement(webDriver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[2]/div/div[1]/div/div/div[1]/a[21]")));
        elementutil.clickPath("/html/body/div[2]/div/div[1]/div[2]/div/div[1]/div/div/div[1]/a[21]");

        elementutil.clickId("regularBetType");

        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[4]/div[2]/div[1]/div[1]/ul/li[14]/a");
        elementutil.clickPath("//*[@id=\"NiuNiuStud\"]");

    }


    public int matchData(List<DataTask.Info> infos, int z) {
        int index = MatchCore.indedeS[info.indede];
        for (int i = 0; i < infos.size(); i++) {
            int mth = CoreMath.mth(infos.get(i).number) - 1;
            if (mth == info.indede) {
                index = i;
                return index;
            }
        }
        return index;
    }
}
