package baidu;

import baidu.Ecai.Main;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import matchore.MatchCore;

import java.util.List;

import static matchore.MatchCore.creatTicketInfo;
import static matchore.MatchCore.getDanduiPourInfos;

public class M960424_San_Tiao_1_1 {

    public static void main(String[] args) {
        TicketInfo info01 = new TicketInfo();
        info01.account = "wtw960424";
        info01.password = "953934cap";
        info01.tag = "_dan_dui_";
        info01.minIss = 40;
        info01.mulripe = 1;
        info01.dub = 4;
        info01.precent = 0.3;
        info01.ticketKind = 3;
        info01.money = 10000;
        info01.stopLoss = 0.4;
        info01.zhisunMoney = 200;
        info01.first = 1;
        info01.expectW = 5;
        info01.indede = 6;
        info01.useDefaultPresent = false;
        info01.decreasP = 0.05;


//        List<PourInfo> pourInfos = getDanduiPourInfos(info01);

        TicketInfo info = creatTicketInfo(4, 5, 0.35, 8000, 4, 1);
        
        new Main().start(info);
    }
}
