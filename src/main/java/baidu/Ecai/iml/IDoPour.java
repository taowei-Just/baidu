package baidu.Ecai.iml;

import baidu.bean.BettingRecord;
import baidu.bean.PourInfo;
import baidu.bean.WinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IDoPour {
     

    // 描述
      String[] detailS = new String[]{"五条", "炸弹", "葫芦", "顺子", "三条", "两对", "单对", "散号"};
    //倍率
      double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};
 

    int maxIss = 1260;
    List<String> lisStrS = new ArrayList<>();
   
     void start( );
    List<History> getHistories();
    String readPrincipal();
    void restart();
    void loopIngQueue(String issue, int inde) throws Exception;
    Map<Long, Double> matchBetAmount(List<Long> issueLong);
    Map<Long, PourInfo> matchBetAmount02(List<Long> issueLong);
    /**
     * @param currentIssue 最新一期数据
     * @param inde         目标出现的距离
     * @return
     */
    List<Long> mathPriods(String currentIssue, int inde);
    /**
     * @param currentIssue 最新一期数据
     * @param inde         目标出现的距离
     * @return
     */
    List<Long> mathPriods02(String currentIssue, int inde);
    void operate(List<Long> issueLong, Map<Long, PourInfo> doubleMap);
    void pour(int i, Long aLong, Double aDouble);
    boolean confirmPour(int i, Long aLong, Double aDouble, List<BettingRecord> bettingRecords);
    void waitMarketOpen();
    boolean closeDialog();
    void cancellations();
    List<BettingRecord> getBettingRecords();
    BettingRecord matchBetting(String st) throws Exception;
    WinInfo waitingIssue();
    WinInfo waitCurrentWinInfo(Long aLong);

    WinInfo win();
    void bpttomPour(int i, int i1, int num);

    int matchData(List<History> histories);


    void select2F();

    void select1J();

    void select2J();

    void select1Y();

    void select2Y();

    void clickzhadan();

    void clickhulu();

    void clickshunz();

    void clicksantiao();

    void clickliangdui();

    void clickdandui();

    void clicksanhao();

    void click5tiao();

    /**
     * @param mult      倍率
     * @param last      上一次投注
     * @param lastnum   往期总投注
     * @param expectwin 期望盈利1
     * @param v         期望盈利2
     * @param lastTMult 上一期盈利
     * @return
     */
    double matchWin(double mult, double last, double lastnum, double expectwin, double v, double lastTMult);

    void waitDialog();

    void init();

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


}
