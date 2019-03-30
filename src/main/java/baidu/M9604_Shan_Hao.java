package baidu;

import baidu.Ecai.Main;
import baidu.bean.TicketInfo;

public class M9604_Shan_Hao {

    public static void main(String[] args) {

        TicketInfo info2 = new TicketInfo();
        info2.account = "wtw9604";
        info2.password = "953934cap";
        info2.indede = 7;
        info2.ticketKind = 1;
        info2.tag = "_sanhao_9604_";
        info2.dub = 4;
        info2.mulripe = 1;
        info2.precent=0.5 ;
        info2.minIss = 30;
        info2.expectW =10 ;
        new Main().start(info2);
    }
}
