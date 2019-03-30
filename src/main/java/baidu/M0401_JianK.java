package baidu;

import baidu.Ecai.Main;
import baidu.bean.TicketInfo;

public class M0401_JianK {

    public static void main(String[] args) {

        TicketInfo info2 = new TicketInfo();
        info2.account = "wtw960401";
        info2.password = "953934cap";
        info2.ticketKind = -1;
        info2.tag = "_jian_kong_";
        info2.indede = 4;
        info2.dub = 4;
        info2.mulripe = 1;
        info2.precent=0.1 ;
        info2.minIss = 30;
        new Main().start(info2);
 
    }
}
