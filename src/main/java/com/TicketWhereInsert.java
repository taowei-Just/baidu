package com;
 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class TicketWhereInsert implements Iinsert<HelloWorld.Info> {
    TestMysql mysql;
    private Statement queryStatement;
    private PreparedStatement insertStatement;

    public TicketWhereInsert(TestMysql mysql) {
        this.mysql = mysql;
        try {
            queryStatement = mysql.prepareQuery();
            insertStatement = mysql.prepareInsert("insert into " + mysql.getTableName() + " " +
                    "(  totalMoney,tou_wutiao,tou_zadan,tou_hulu,tou_shunzi,tou_santiao,tou_liangdui," +
                    "tou_dandui,tou_sanhao,wutiao_yingli,zadan_yingli,hulu_yingli,shunzi_yignli," +
                    "santiao_yingli,liangdui_yingli,dandui_yingli,sanhao_yingli,wutiao_winRatio,zadan_winRatio," +
                    "hulu_winRatio,shunzi_winRatio,santiao_winRatio,liangdui_winRatio,dandui_winRatio," +
                    "sanhao_winRatio,congrats )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void insertData(HelloWorld.Info object) throws SQLException {

        NewTicketTable ticketTable = info2Table(object);

        String sql = "select *from " + mysql.getTableName() +
                " where congrats='" + ticketTable.congrats + "' and tou_wutiao =" + ticketTable.tou_wutiao + " and tou_zadan = " + ticketTable.tou_zadan
                + " and tou_hulu= " + ticketTable.tou_hulu + " and tou_shunzi =" + ticketTable.tou_shunzi + " and tou_santiao ="
                + ticketTable.tou_santiao + " and tou_liangdui = " + ticketTable.tou_liangdui + " and tou_dandui = "
                + ticketTable.tou_dandui + " and tou_sanhao = " + ticketTable.tou_sanhao;

//        System.err.println("ticketTable  = " +ticketTable.toString());
//        System.err.println("query baidu.sql  = " +baidu.sql);

        ResultSet resultSet = queryStatement.executeQuery(sql);

        if (resultSet != null && resultSet.next()) {



            updata(object);
        } else {
//            System.err.println("insertData ");

            insertStatement.setInt(1, ticketTable.totalMoney);
            insertStatement.setInt(2, ticketTable.tou_wutiao);
            insertStatement.setInt(3, ticketTable.tou_zadan);
            insertStatement.setInt(4, ticketTable.tou_hulu);
            insertStatement.setInt(5, ticketTable.tou_shunzi);
            insertStatement.setInt(6, ticketTable.tou_santiao);
            insertStatement.setInt(7, ticketTable.tou_liangdui);
            insertStatement.setInt(8, ticketTable.tou_dandui);
            insertStatement.setInt(9, ticketTable.tou_sanhao);


            insertStatement.setDouble(10, ticketTable.wutiao_yingli);
            insertStatement.setDouble(11, ticketTable.zadan_yingli);
            insertStatement.setDouble(12, ticketTable.hulu_yingli);
            insertStatement.setDouble(13, ticketTable.shunzi_yignli);
            insertStatement.setDouble(14, ticketTable.santiao_yingli);
            insertStatement.setDouble(15, ticketTable.liangdui_yingli);
            insertStatement.setDouble(16, ticketTable.dandui_yingli);
            insertStatement.setDouble(17, ticketTable.sanhao_yingli);

//            public  double wutiao_winRatio;
//            public   double zadan_winRatio;
//            public   double hulu_winRatio;
//            public  double shunzi_winRatio;
//            public double santiao_winRatio;
//            public double liangdui_winRatio;
//            public double dandui_winRatio;
//            public double sanhao_winRatio;
//            public  String congrats ;


            insertStatement.setDouble(18, ticketTable.wutiao_winRatio);
            insertStatement.setDouble(19, ticketTable.zadan_winRatio);
            insertStatement.setDouble(20, ticketTable.hulu_winRatio);
            insertStatement.setDouble(21, ticketTable.shunzi_winRatio);
            insertStatement.setDouble(22, ticketTable.santiao_winRatio);
            insertStatement.setDouble(23, ticketTable.liangdui_winRatio);
            insertStatement.setDouble(24, ticketTable.dandui_winRatio);
            insertStatement.setDouble(25, ticketTable.sanhao_winRatio);
            insertStatement.setString(26, ticketTable.congrats);

            commit(insertStatement);
            insertStatement.clearParameters();
        }

    }

    private void commit(PreparedStatement insertStatement) throws SQLException {
        insertStatement.execute();
        mysql.commit();
    }

    private NewTicketTable info2Table(HelloWorld.Info object) {
        NewTicketTable ticketTable = new NewTicketTable();
        ticketTable=  preparetouzhu(ticketTable, object);
        ticketTable.totalMoney = object.totalmoney;
        ticketTable.congrats = Arrays.toString(object.number);
        return ticketTable;
    }


    private NewTicketTable preparetouzhu(NewTicketTable ticletTab, HelloWorld.Info object) {

        for (int i = 0; i < object.number.length; i++) {
            switch (object.number[i]) {
                case 0:
                    ticletTab.tou_wutiao = object.money[i];
                    ticletTab.wutiao_winRatio = object.winRatio[i];
                    ticletTab.wutiao_yingli = object.result[i];

                    break;
                case 1:
                    ticletTab.tou_zadan = object.money[i];
                    ticletTab.zadan_winRatio = object.winRatio[i];
                    ticletTab.zadan_yingli = object.result[i];
                    break;
                case 2:
                    ticletTab.tou_hulu = object.money[i];
                    ticletTab.hulu_winRatio = object.winRatio[i];
                    ticletTab.hulu_yingli = object.result[i];
                    break;
                case 3:
                    ticletTab.tou_shunzi = object.money[i];
                    ticletTab.shunzi_winRatio = object.winRatio[i];
                    ticletTab.shunzi_yignli = object.result[i];
                    break;

                case 4:
                    ticletTab.tou_santiao = object.money[i];
                    ticletTab.santiao_winRatio = object.winRatio[i];
                    ticletTab.santiao_yingli = object.result[i];
                    break;
                case 5:
                    ticletTab.tou_liangdui = object.money[i];
                    ticletTab.liangdui_winRatio = object.winRatio[i];
                    ticletTab.liangdui_yingli = object.result[i];


                    break;

                case 6:
                    ticletTab.tou_dandui = object.money[i];
                    ticletTab.dandui_winRatio = object.winRatio[i];
                    ticletTab.dandui_yingli = object.result[i];


                    break;
                case 7:
                    ticletTab.tou_sanhao = object.money[i];
                    ticletTab.sanhao_winRatio = object.winRatio[i];
                    ticletTab.sanhao_yingli = object.result[i];

                    break;


            }
        }


        return ticletTab;
    }

    @Override
    public void updata(HelloWorld.Info info) throws SQLException {
        NewTicketTable ticketTable = info2Table(info);
        String sql = "update   " + mysql.getTableName() + " set   " +
                "  totalMoney =" + ticketTable.totalMoney + ",tou_wutiao = " + ticketTable.tou_wutiao + " ,tou_zadan = " + ticketTable.tou_zadan + " ,tou_hulu= " + ticketTable.tou_hulu + " ,tou_shunzi= " + ticketTable.tou_shunzi + " ,tou_santiao= " + ticketTable.tou_santiao + " ,tou_liangdui= " + ticketTable.tou_liangdui + " ," +
                "tou_dandui= " + ticketTable.tou_dandui + " ,tou_sanhao= " + ticketTable.tou_sanhao + " ,wutiao_yingli= " + ticketTable.wutiao_yingli + " ,zadan_yingli= " + ticketTable.zadan_yingli + " ,hulu_yingli= " + ticketTable.hulu_yingli + " ,shunzi_yignli= " + ticketTable.shunzi_yignli + " " +
                ",santiao_yingli= " + ticketTable.santiao_yingli + " ,liangdui_yingli= " + ticketTable.liangdui_yingli + "  ,dandui_yingli= " + ticketTable.dandui_yingli + " , sanhao_yingli= " + ticketTable.sanhao_yingli + " ,wutiao_winRatio= " + ticketTable.wutiao_winRatio + "  ,zadan_winRatio= " + ticketTable.zadan_winRatio + " " +
                ",hulu_winRatio= " + ticketTable.hulu_winRatio + " ,shunzi_winRatio= " + ticketTable.shunzi_winRatio + " , santiao_winRatio= " + ticketTable.santiao_winRatio + "  ,liangdui_winRatio= " + ticketTable.liangdui_winRatio + " , dandui_winRatio= " + ticketTable.dandui_winRatio + " " +
                ",sanhao_winRatio= " + ticketTable.sanhao_winRatio + " ,congrats = '" + ticketTable.congrats + "'  "+" where congrats='" + ticketTable.congrats + "' and tou_wutiao =" + ticketTable.tou_wutiao+" and tou_zadan = "+ticketTable.tou_zadan
                + " and tou_hulu= " +ticketTable.tou_hulu+ " and tou_shunzi =" +ticketTable.tou_shunzi+ " and tou_santiao ="
                +ticketTable.tou_santiao+ " and tou_liangdui = "  +ticketTable.tou_liangdui+ " and tou_dandui = "
                +ticketTable.tou_dandui+ " and tou_sanhao = "+ticketTable.tou_sanhao ;
        System.err.println("updata baidu.sql " +sql);
        PreparedStatement preparedStatement = mysql.prepareUpdata(sql);
        commit(preparedStatement);
    }

    @Override
    public String queryMaxPro() throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("select max(periods)from " + mysql.getTableName() + " ");
        if (resultSet.next())
            return resultSet.getString(1);
        return "";
    }

    public void insertData(List<HelloWorld.Info> infos) throws SQLException {
        for (HelloWorld.Info info : infos
                ) {
            if (info == null)
                continue;
            insertData(info);

        }

    }
}
