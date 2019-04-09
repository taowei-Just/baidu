package baidu.Ecai;


import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.utils.CoreMath;
import baidu.utils.Elementutil;
import baidu.utils.LogUtils;
import baidu.utils.Out;
import com.PattenUtil;
import com.runn.DataTask;
import niuniu.NiuNIuMatch;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 保障本金不亏
 * 保障精准下注
 * 保障精准识别盈亏
 * 盈亏记录
 * 异常日志
 */


public class DoPour2 {

    // 描述
    private String[] detailS = new String[]{"五条", "炸弹", "葫芦", "顺子", "三条", "两对", "单对", "散号"};
    //倍率
    private double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};
    //最优投注间隔期数
    private int[] indedeS = new int[]{200, 100, 50, 30, 15, 13, 4, 6};
    // 最优间隔期间投注次数
    private int[] countS = new int[]{60, 30, 17, 7, 5, 3, 1, 2};

    // 间隔提醒阈值
    private double[] thresholdS = new double[]{150, 60, 50, 20, 20, 13, 5, 10};
    // 上衣次数据发送次数
    private double[] messAgeSends = new double[]{0, 0, 0, 0, 0, 0, 0, 0}; 
    
    // 推送消息频次
    private double[] messSendF = new double[]{0, 0, 20, 10, 4, 2, 1, 1};


    private List<String> lisStrS = new ArrayList<>();
    private RemoteWebDriver webDriver;
    private Elementutil elementutil;
    private int maxIss = 840;
    private TicketInfo info;
    private LogUtils logUtils;
    int pourCount = 0;

    public static void main(String[] args) {

        DoPour2 doPour = new DoPour2(null, new Main.Info());
        doPour.info.tag = "test_match";


    }


    public DoPour2(RemoteWebDriver webDriver, TicketInfo info) {
        this.webDriver = webDriver;
        this.info = info;
        elementutil = new Elementutil(webDriver);
        logUtils = new LogUtils("e:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\" + getClass().getSimpleName() + "" + info.tag + ".txt");
    }

    Map<Integer, List<DataTask.Info>> hisMap = new HashMap<>();

    public void start(int index) {
        hisMap.clear();
        /**
         *
         * 查看历史数据
         *
         */

        Main.pushAllMessage("启动[" + info.account + "_" + "]监控！");
        lisStrS.clear();
        waitDialog();
        String principal = readPrincipal();
        logUtils.saveLog2File("本金：" + principal);
        Main.pushAllMessage("本金：" + principal);
        List<History> histories = getHistories();
        Out.e(" histories " + histories.toString());
        matchInfo(histories);
        loopSerch();
    }

    private void matchInfo(List<History> histories) {
        for (History his : histories)
            matchInfo(his.number, his.issue);
    }

    private void matchInfo(String number, String issue) {
        DataTask.Info info = new DataTask.Info();
        info.number = number;
        info.niuniu = NiuNIuMatch.matchNiu(info.number) + "";
        info.periods = issue;
        info.location = CoreMath.mth(number) - 1;
        info.detail = detailS[info.location];
        info.order = info.periods.substring(info.periods.length() - 3, info.periods.length());
        info.date = issue.substring(0, 8);
        saveHisMap(info);
    }

    private void saveHisMap(DataTask.Info info) {
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

    private void matchInfo(String number) {
        matchInfo(number, null);
    }

    String lastIssue = "";

    private void loopSerch() {
        Out.e(outInfo().toString());

        while (true) {
            WinInfo winInfo = waitingIssue();
            if (winInfo == null)
                continue;
            if (winInfo.currentIssue.equals(lastIssue))
                continue;

            matchInfo(winInfo.Numner, winInfo.issue);
            //得出每个梭哈的当前间隔
            Map<Integer, DataTask.Info> infoMap = outInfo();
            Out.e("" + infoMap.toString());

            Set<Integer> keySet = infoMap.keySet();
            for (Integer integer : keySet) {
                DataTask.Info info = infoMap.get(integer);
                long l = Long.valueOf(winInfo.currentIssue) - Long.valueOf(info.periods);
                Out.e("[" + detailS[integer] + "]最近一次出现：" + info.periods + " 距离当前投注期数：" + l);
                if (l -1>= thresholdS[integer] && messAgeSends[integer] != l) {
                    sendNotifyMessage(integer, l, winInfo.currentIssue, info.periods);
                }
                messAgeSends[integer] = l;
            }
            bpttomPour(0 , 4, 1);
            Elementutil.wait_(3);
            cancellations();
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void cancellations() {
        List<BettingRecord> bettingRecordList = getBettingRecords();
        for (int i = 0; i < lisStrS.size(); i++) {
            String s = lisStrS.get(i);
            for (int j = 0; j < bettingRecordList.size(); j++) {
                if (s.equals(bettingRecordList.get(j).priods) && bettingRecordList.get(j).statue.equals("未开奖")) {
                    Out.e("撤销这一期：" + bettingRecordList.get(j).toString());
                    logUtils.saveLog2File("撤销当前期数：" + bettingRecordList.get(j).toString());
                    String path = "/html/body/div[2]/div/div[2]/div[1]/div[2]/div/div[4]/div[1]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[" + (j + 1) + "]/td[8]/button";
                    elementutil.clickPath(path);
                }
            }
        }
    }
    private void sendNotifyMessage(Integer integer, long l, String currentIssue, String periods) {
        // [监控提醒] 当前 [三条] 已经有[20]期未出现请留意，当前销售期数为[210051]上一次出现期数为 [20120212]
        String msg = "账号 " + info.account + " [监控提醒]当前 [" + detailS[integer] + "] 已经有[" + (l-1) + "]期未出现了，敬请留意，当前销售期数为[" + currentIssue + "]上一次出现期数为 [" + periods + "]";
        Main.pushAllMessage(msg);
    }

    private Map<Integer, DataTask.Info> outInfo() {
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

    private DataTask.Info getMaxInfo(List<DataTask.Info> infos) {
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

    @NotNull
    private List<History> getHistories() {
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

    private String readPrincipal() {

        WebElement walletAmount = webDriver.findElement(By.className("walletAmount"));
        if (!walletAmount.isDisplayed()) {
            elementutil.wait_(1);
            elementutil.clickId("showWalletAmount");
            elementutil.wait_(1);

        }
        return elementutil.readTextByClass("walletAmount");
    }

    private void restart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                start(0);
            }
        }).start();
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

    private void closeDialog() {
        elementutil.wait_(2);
        WebElement path = webDriver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/p"));
        String content = path.getText();
        if (path.isDisplayed())
            Out.e(info.tag, " text " + content);
        elementutil.clickClass("modal-footer");
        elementutil.clickPath("/html/body/div[3]/div/div/div[3]/button");
//        elementutil.clickCssS("#warning-dialog > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > button:nth-child(1)");
    }

    @NotNull
    private List<BettingRecord> getBettingRecords() {
        elementutil.clickClass("font-lavander");
        elementutil.wait_(1);
        String innerHTML = webDriver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/div[4]/div[1]/div")).getAttribute("innerHTML");
        innerHTML = PattenUtil.replaceBlank(innerHTML).replace(" ", "");
        String regex = "class=\"odd\">(.*?)</tr>";
        List<String> stringList = PattenUtil.getSubUtil(innerHTML, regex, true);
        List<BettingRecord> bettingRecordList = new ArrayList<>();
        for (String st : stringList) {
            try {
                BettingRecord bettingRecord = matchBetting(st);
                bettingRecordList.add(bettingRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bettingRecordList;
    }

    private BettingRecord matchBetting(String st) throws Exception {
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
            Out.d(e.toString());
        }
        Out.e(bettingRecord.toString());
        return bettingRecord;
    }

    private WinInfo waitingIssue() {

        while (true) {
            try {
                WinInfo win = win();
                if (win.Numner == null)
                    continue;
                WebElement curissue = webDriver.findElement(By.id("curissue"));
                String text = curissue.getText();
                text = text.replace(" ", "");
                text = text.substring(1, text.length() - 1);
                win.currentIssue = text;

                if (win != null)
                    return win;

                Thread.sleep(1 * 1000);
            } catch (Exception e) {
//                Out.e(info.tag,""+e.toString());
            }

            return null;
        }
    }

    static class BettingRecord {
        public int kind;
        public String detail;
        public String type;
        public String priods;
        public String desc;
        public String money;
        public String win;
        public String statue;
        public String cancellations;

        @Override
        public String toString() {
            return "BettingRecord{" +
                    "kind=" + kind +
                    ", detail='" + detail + '\'' +
                    ", type='" + type + '\'' +
                    ", priods='" + priods + '\'' +
                    ", desc='" + desc + '\'' +
                    ", money='" + money + '\'' +
                    ", win='" + win + '\'' +
                    ", statue='" + statue + '\'' +
                    ", cancellations='" + cancellations + '\'' +
                    '}';
        }
    }

    static class WinInfo {
        public String Numner;
        public String issue;
        public int wiState;
        public int winInde;
        public String currentIssue;

        @Override
        public String toString() {
            return "WinInfo{" +
                    "Numner='" + Numner + '\'' +
                    ", issue='" + issue + '\'' +
                    ", wiState=" + wiState +
                    ", winInde=" + winInde +
                    ", currentIssue='" + currentIssue + '\'' +
                    '}';
        }
    }

    String lastlastissue = "";

    private WinInfo win() {
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
            Out.d(info.tag, "第 " + lastissue + " 期 开奖号码为 " + s + "   [" + detailS[mth] + "]");
            if (mth == info.indede) {
                winInfo.wiState = 1;
            } else {
                winInfo.wiState = 0;
            }
        } catch (Exception e) {
        }

        return winInfo;
    }

    public static class History {
        public String number = "10";
        public String issue;
        public String overTime;

        @Override
        public String toString() {
            return "History{" +
                    "number='" + number + '\'' +
                    ", issue='" + issue + '\'' +
                    ", overTime='" + overTime + '\'' +
                    '}';
        }
    }

    private void waitDialog() {

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

    private void bpttomPour(int i, int i1, int num) {

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
        Elementutil.wait_(2);
        elementutil.clickPath("/html/body/div[4]/div[2]/div/div[3]/button[2]");

    }

    private void select2F() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/button");
        //2分
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/ul/li[5]/a");
    }

    private void select1J() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/button");
        //1角
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/ul/li[4]/a");
    }

    private void select2J() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/button");
        //2角
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/ul/li[3]/a");
    }

    private void select1Y() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/button");
        //1元
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/ul/li[2]/a");
    }

    private void select2Y() {
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/button");
        //2元
        elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[5]/div[1]/div[1]/div/ul/li[1]/a");
    }
    private void clickzhadan() {
        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[2]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickhulu() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[3]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickshunz() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[4]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clicksantiao() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[5]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickliangdui() {


        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[6]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickdandui() {
        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[7]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clicksanhao() {
        try {
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[8]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void click5tiao() {
        try {
            //5tiao
            elementutil.clickPath("/html/body/div[2]/div/div[2]/div[1]/div[1]/div[3]/div[2]/div[2]/div[27]/div[2]/button[1]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
