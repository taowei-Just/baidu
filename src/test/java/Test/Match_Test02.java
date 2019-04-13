package Test;

import baidu.bean.TicketInfo;
import baidu.utils.LogUtils;
import baidu.utils.Out;
import matchore.MatchCore;
import matchore.WaitTimeCall;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1.跟据计算得出每一次下注的最佳值
 * 2.根据历史数据调整下注的盈利比
 * 3.根据总金额调整盈利比计算出最小损失
 * <p>
 * 盈利最大化
 * 亏损最小化
 */

public class Match_Test02 {

    private LogUtils logUtils;
    private double[] mults = new double[]{9850, 218, 109, 0, 13.68, 9.12, 1.95, 3.391};

    public Match_Test02() {
        this.logUtils = new LogUtils("e:\\Temp\\log\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "\\" + getClass().getSimpleName() + "" + ".txt");

    }

    public static void main(String[] args) {

//        Match_Test02 match_test01 = new Match_Test02();
//        match_test01.start();
        TicketInfo info = new TicketInfo();
        info.openMarket  = "9" ; 
        info.closeMarket  = "10" ; 
        MatchCore.waitMarketOpen(info, new WaitTimeCall() {
            @Override
            public void onTimeOpen(long timeInMillis) {
                Out.e("开盘了 》》");
            }

            @Override
            public void onNoTime(long timeInMillis) {
                Out.e("》》等待开盘 " + new Date(timeInMillis).toString());
            }
        });
        
    }
 
 
}
