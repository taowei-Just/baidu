package niuniu;

import com.TestMysql;
import com.TicletTab;
import com.runn.DataTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NiuNIuMatch {

    private static TestMysql testMysql;

    public NiuNIuMatch() {
        testMysql = new TestMysql("ticket_data_vr");
    }

    public static void main(String[] args) {
        NiuNIuMatch niuNIuMatch = new NiuNIuMatch();
        niuNIuMatch.start();
    }
    private void start() {
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr  where date >'" + "20190328" + "'  ORDER BY    periods DESC";
        ResultSet resultSet = testMysql.queryData(str);
        List<DataTask.Info> infoList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                DataTask.Info info = new DataTask.Info();
                TicletTab ticletTab = new TicletTab();
//
//                ticletTab.setTouzhu_wutiao(resultSet.getInt(10));
//                ticletTab.setTouzhu_zadan(resultSet.getInt(11));
//                ticletTab.setTouzhu_hulu(resultSet.getInt(12));
//                ticletTab.setTouzhu_shunzi(resultSet.getInt(13));
//                ticletTab.setTouzhu_santiao(resultSet.getInt(14));
//                ticletTab.setTouzhu_liangdui(resultSet.getInt(14));
//                ticletTab.setTouzhu_dandui(resultSet.getInt(16));
//                ticletTab.setTouzhu_sanhao(resultSet.getInt(17));
//                ticletTab.setWutiao_zhongjiang_yingli(resultSet.getDouble(2));
//                ticletTab.setWutiao_zhongjiang_kuisun(resultSet.getDouble(18));
//                ticletTab.setZadan_zhongjiang_yingli(resultSet.getDouble(3));
//                ticletTab.setZadan_zhongjiang_kuisun(resultSet.getDouble(19));
//                ticletTab.setHulu_zhongjiang_yingli(resultSet.getDouble(4));
//                ticletTab.setHulu_zhongjiang_kuisun(resultSet.getDouble(20));
//                ticletTab.setShunzi_zhongjiang_yignli(resultSet.getDouble(5));
//                ticletTab.setShunzi_zhongjiang_kuisun(resultSet.getDouble(21));
//                ticletTab.setSantiao_zhongjiang_yingli(resultSet.getDouble(6));
//                ticletTab.setSantiao_zhongjiang_kuisun(resultSet.getDouble(22));
//                ticletTab.setLiangdui_zhongjiang_yingli(resultSet.getDouble(7));
//                ticletTab.setLiangdui_zhongjiang_kuisun(resultSet.getDouble(23));
//                ticletTab.setDandui_zhongjiang_yingli(resultSet.getDouble(8));
//                ticletTab.setDandui_zhongjiang_kuisun(resultSet.getDouble(24));
//                ticletTab.setSanhao_zhongjiang_yingli(resultSet.getDouble(9));
//                ticletTab.setSanhao_zhongjiang_kuisun(resultSet.getDouble(25));
//                ticletTabList.add(ticletTab);
                info.periods = resultSet.getString(1);
                info.date = resultSet.getString(2);
                info.number = resultSet.getString(3);
                info.order = resultSet.getString(4);
                info.location = resultSet.getInt(5);
                info.detail = resultSet.getString(6);
//                System.err.println("" + info.toString());
                infoList.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        matchNiuNiu(infoList);
    }

    public static String niuniuString( String number){
        int str = matchNiu( number);
        return  (str >-1? str==0? "牛牛" : "牛" + str : "无牛");
    }
    private void matchNiuNiu(List<DataTask.Info> infoList) {
        for (int i = 0; i < infoList.size(); i++) {
            DataTask.Info info = infoList.get(i);
            int str = matchNiu(info.number);
            System.err.println(info.number + (str >-1? str==0? "牛牛" : "牛" + str : "无牛"));
            info.niuniu = (str >-1? str==0? "牛牛" : "牛" + str : "无牛");
            try {
                testMysql.prepareUpdata(info).execute();
                testMysql.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//
//        try {
//            testMysql.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

   public static int[] niuS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public  static  int matchNiu(String number) {
        char[] chars = number.toCharArray();
        int[] numbers = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            numbers[i] = Integer.parseInt(chars[i]+"");
        }
        for (int i = 0; i < numbers.length - 2; i++) {
            for (int j = i + 1; j < numbers.length - 1; j++) {
                for (int k = j + 1; k < numbers.length; k++) {
                    int i1 = numbers[i] + numbers[j] + numbers[k];
                    if (i1 % 10 == 0) {
                        int num = 0;
                        for (int l = 0; l < numbers.length; l++) {
                            if (l != i && l != j && l != k) {
                                num += numbers[l];
                            }
                        }
                        System.err.println(""+num);
                        return niuS[num % 10];
                    }
                }
            }
        }

        return -1;
    }
}
