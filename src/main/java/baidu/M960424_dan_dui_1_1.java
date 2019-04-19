package baidu;

import baidu.Ecai.Main;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.utils.Out;
import com.TestMysql;
import com.runn.DataTask;
import matchore.MatchCore;
import sun.rmi.runtime.Log;

import java.util.List;

import static matchore.MatchCore.creatTicketInfo;
import static matchore.MatchCore.getDanduiPourInfos;

public class M960424_dan_dui_1_1 {

    private final TestMysql sql;

    public M960424_dan_dui_1_1() {
        sql = new TestMysql("ticket_data_vr_1_1");
    }

    public static void main(String[] args) {
//        TicketInfo info01 = new TicketInfo();
//        info01.account = "wtw960424";
//        info01.password = "953934cap";
//        info01.tag = "_dan_dui_";
//        info01.minIss = 40;
//        info01.mulripe = 1;
//        info01.dub = 3;
//        info01.precent = 0.3;
//        info01.ticketKind = 3;
//        info01.money = 10000;
//        info01.stopLoss = 0.4;
//        info01.zhisunMoney = 200;
//        info01.first = 1;
//        info01.expectW = 5;
//        info01.indede = 6;
//        info01.useDefaultPresent = false;
//        info01.decreasP = 0.05;


//        List<PourInfo> pourInfos = getDanduiPourInfos(info01);


        TicketInfo info = creatTicketInfo(6, 5, 0.30, 8000, 4, 3);
//        new Main().start(info);

        new M960424_dan_dui_1_1().start(info);

    }

    private void start(TicketInfo info) {

        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date >'0'  ORDER BY date asc ,  periods asc";
        List<DataTask.Info> infos = MatchCore.querYInfoList(sql, str);
        List<PourInfo> pourInfos = MatchCore.getDanduiPourInfos(info);
        for (PourInfo pourInfo : pourInfos) {
            Out.e(pourInfo.toString());
        }

        double totalWin = 0;
        double totalMoney = 0;
        for (int i = 0; i < infos.size() - pourInfos.size(); i++) {
            if (infos.get(i).location < 4) {
                int offet = 0;
                boolean win = false;
                int jump = 0;
                int last5Jump = -1;
                int last4Jump = -1;

                for (int i1 = 0; i1 < pourInfos.size() + offet; i1++) {
                    int i2 = i1 + i;

                    if (i1 - offet > pourInfos.size() - 1)
                        break;
//                    if (i > 6) {
//
//                        boolean count5 = true;
//
//                        for (int j = i2; j > i2 - 5; j--) {
//                            DataTask.Info info1 = infos.get(i2);
//                            if (info1.location != 7) {
//                                count5 = false;
//                                break;
//                            }
//
//
//                        }
//                        if (count5)
//                            jump=1;
//                    }

                    DataTask.Info info1 = infos.get(i2);
                    PourInfo pourInfo = pourInfos.get(i1 - offet);

                    if (info1.location == 6) {
                        if (jump <= 0) {
                            totalWin += pourInfo.win;
                            totalMoney += pourInfo.total;
                            win = true;
                            i += i1;
                            break;
                        } else {
                            Out.e(" 你跳过了一个机会 " + info1.toString());
                            jump = 0;
                            continue;
                        }
                    } else {
                        if (jump > 0) {
                            Out.e("  跳过  " + info1.toString());
                            jump = 0;
                            continue;
                        }
                    }
                    
                    boolean sT = false;
                    
                    
                    // 之前有连续出现三次散号跳过一期
                    if (i2 > 1) {
                        DataTask.Info info3 = infos.get(i2 - 1);
                        DataTask.Info info4 = infos.get(i2 - 2);
                        if (info1.location == 7 && info3.location == 7 && info4.location == 7) {
                            sT = true;
                        }

                    }
                    
                    // 之前有出现过三次两对跳过一期
                    if (i2 > 1 && i2 - last5Jump > 2) {
                        int count = 0;
                        for (int j = i2; j > i2 - i1; j--) {
                            DataTask.Info info2 = infos.get(j);
                            if (info2.location == 5)
                                count++;
                        }
                        if (count > 2) {
                            sT = true;
                            last5Jump = i2;
                        }
                    }    
                    if (i2 > 1 && i2 - last4Jump > 1) {
                        int count = 0;
                        for (int j = i2; j > i2 - i1; j--) {
                            DataTask.Info info2 = infos.get(j);
                            if (info2.location == 4)
                                count++;
                        }
                        if (count > 1) {
                            sT = true;
                            last4Jump = i2;
                        }
                    }


                    if ( sT) {
                        jump = 1;
                    } else {
                        jump = 0;
                    }
//                    if (info1.location == 4 || sT) {
//                        jump = 1;
//                    } else {
//                        jump = 0;
//                    }
                    offet += jump;
                }
                if (!win) {
                    totalWin -= pourInfos.get(pourInfos.size() - 1).total;
                    Out.e("\n                                 亏损 " + pourInfos.get(pourInfos.size() - 1).total + " " + infos.get(i).toString());
                    totalMoney +=  pourInfos.get(pourInfos.size() - 1).total;
                }
                i += pourInfos.size() + offet;
            }
        }
        Out.e(" 总盈利 ：" + totalWin);
        Out.e(" 流水 ：" + totalMoney);

    }
}
