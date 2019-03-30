package baidu;

import baidu.Ecai.Main;
import baidu.bean.TicketInfo;

public class M04_Dan_Dui {
    public static void main(String[] args) {
        new Main().start(new TicketInfo()
                .setAccount("wtw960424")
                .setPassword("953934cap")
                .setPrecent(0.1)
                .setMinIss(30)
                .setDub(4)
                .setIndede(6)
                .setStopLoss(0.1)
                .setMoney(100)
                .setTag("_dan_dui_") );
    }

}
