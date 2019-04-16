package subweb.subRun;

import baidu.Ecai.Main;
import baidu.utils.Out;
import com.CoreMath;
import com.TestMysql;
import com.runn.DataTask;
import matchore.MatchCore;
import niuniu.NiuNIuMatch;
import org.jsoup.Jsoup;
import subweb.SubwebUtil;
import subweb.subRun.Sql.VR1_1Insert;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SubWeb_VR_1_1 implements Runnable {

    private static String url = "https://numbers.videoracing.com/analy_11_1.aspx?SortType=desc&DType=Analy100";
//        private static String url = "https://numbers.videoracing.com/analy_11_1.aspx?SortType=desc&DType=Lastest3Day";
    private final TestMysql testMysql;
    private final VR1_1Insert vr1_1Insert;

    private static long lastIssue = 0;

    public SubWeb_VR_1_1() {
        testMysql = new TestMysql("ticket_data_vr_1_1");
        vr1_1Insert = new VR1_1Insert(testMysql);
    }

    public static void main(String[] args) {
        new SubWeb_VR_1_1().run();
    }


    boolean running = false;

    @Override
    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (running)
                    return;
                running = true;
                try {
                    saveDataInfo(doSubVR_1_1(testMysql));
                } catch (IOException e) {

                } finally {
                    running = false;
                }
            }
        }, 1, 5 * 1000);

    }

    public static List<DataTask.Info> doSubVR_1_1() throws IOException {
        return doSubVR_1_1(null);

    }

    public static List<DataTask.Info> doSubVR_1_1(TestMysql testMysql) throws IOException {

        SubWebInfo[] webInfoS = new SubWebInfo[1];

        webInfoS[0] = new SubWebInfo();
        webInfoS[0].startD = "Con_BonusCode=\"";
        webInfoS[0].endD = "\";";
        webInfoS[0].coner = false;

        String body = Jsoup.connect(url).get().outerHtml();
        List<List> lists = SubwebUtil.subWeb(body, webInfoS);
//        Out.e(lists.toString());

        List<DataTask.Info> dataInf0S = new ArrayList<>();
        for (List list : lists) {
            if (list.size() <= 0)
                continue;
            for (int i = 0; i < list.size(); i++) {
                String o = (String) list.get(i);
                if (o == null)
                    continue;
                String[] split = o.split(";");
                for (String s : split) {

                    DataTask.Info info = new DataTask.Info();
                    info.periods = s.split("=")[0];
                    info.number = s.split("=")[1].replace(",", "");
                    info.date = info.periods.substring(0, 8);
                    info.order = info.periods.substring(8, info.periods.length());
                    int mth = CoreMath.mth(info.number);
                    info.location = mth - 1;
                    info.detail = CoreMath.detail(info.location);
                    info.niuniu = NiuNIuMatch.niuniuString(info.number);
                    info.alie = CoreMath.alaie(info.location);
                    dataInf0S.add(info);
                }
            }
        }

        Collections.sort(dataInf0S);

        DataTask.Info info = dataInf0S.get(dataInf0S.size() - 1);
        long parseLong = Long.parseLong(info.periods);
        if (parseLong > lastIssue && info.location <5) {
            String str = " \n时间：" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.S").format(new Date(System.currentTimeMillis())) + 
                    " \n" + "[VR1.1 彩] 大奖[" + MatchCore.detailS[info.location] + "] 出现了！期号["+ info.periods+"]历史出现最大间隔[" +( testMysql == null ? "【？？】" : MatchCore.maxTotal(testMysql, info.location)) + "]敬请关注！";
      
//            Main.pushAllMessage(str);
            lastIssue =parseLong;
        }

        return dataInf0S;

    }

    List<DataTask.Info> lastDataInf0S;

    private void saveDataInfo(List<DataTask.Info> dataInf0S) {
        if (dataInf0S == null)
            return;


        for (DataTask.Info dataInf : dataInf0S) {
            if (dataInf == null)
                continue;

            if (lastDataInf0S == null)
                Out.d(dataInf.toString());
            else {
                boolean have =false ;
                for (int i = 0; i < lastDataInf0S.size(); i++) {
                    DataTask.Info info = lastDataInf0S.get(i);
                    if (dataInf.periods.equals(info.periods)) {
                        
                     have=true ;
                        break;
                    }
                }
                if (!have){
                    lastDataInf0S.add(dataInf);
                    Out.d("__" + dataInf.toString());
                }
                
            }
            try {
                testMysql.insertData(vr1_1Insert, dataInf);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        if (lastDataInf0S == null) {
            lastDataInf0S = dataInf0S;
        }

//        Out.d(" ==============================");
    }
}
