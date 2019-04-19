package baidu;

import baidu.Ecai.Main;
import baidu.bean.PourInfo;
import baidu.bean.TicketInfo;
import baidu.utils.Out;
import com.TestMysql;
import com.runn.DataTask;
import matchore.MatchCore;

import java.util.List;

import static matchore.MatchCore.creatTicketInfo;

public class M960424_dan_dui_1_1_02 {

    private final TestMysql sql;

    public M960424_dan_dui_1_1_02() {
        sql = new TestMysql("ticket_data_vr_1_1");
    }

    public static void main(String[] args) {
 


        TicketInfo info = creatTicketInfo(6, 5, 0.30, 10000, 4, 2);
        info.ticketKind =4 ;
        new Main().start(info);

//        new M960424_dan_dui_1_1_02().start(info);

    }
 
}
