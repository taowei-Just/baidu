package com;

import com.runn.DataTask;

import java.sql.*;


public class TestMysql {
    private Connection con;
    private Statement statement;
    private PreparedStatement prepareStatement;
    private Statement statement1;

    public Connection getCon() {
        return con;
    }

    String tableName = "";

    public TestMysql(String tableName) {
        this.tableName = tableName;
        prepareMysql();
    }

    public String getTableName() {
        return tableName;
    }

    public static void main(String[] s) {
        new TestMysql("").prepareMysql();
    }

    public void prepareMysql() {
        //驱动程序名
        String driver = "com.mysql.cj.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "root";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            con.setAutoCommit(false);
            //2.创建statement类对象，用来执行SQL语句！！
            statement = con.createStatement();
//
//            prepareStatement = con.prepareStatement("insert into "+tableName+" ( " +
//                    "touzhu_wutiao , " +
//                    " touzhu_zadan ," +
//                    "touzhu_hulu ," +
//                    "touzhu_shunzi , " +
//                    " touzhu_santiao ," +
//                    "touzhu_liangdui ," +
//                    "touzhu_dandui ," +
//                    "touzhu_sanhao ," +
//                    "wutiao_zhongjiang_yingli ," +
//                    "wutiao_zhongjiang_kuisun, " +
//                    "zadan_zhongjiang_yingli, " +
//                    "zadan_zhongjiang_kuisun, " +
//                    "hulu_zhongjiang_yingli, " +
//                    "hulu_zhongjiang_kuisun, " +
//                    "shunzi_zhongjiang_yignli, " +
//                    "shunzi_zhongjiang_kuisun, " +
//                    "santiao_zhongjiang_yingli, " +
//                    "santiao_zhongjiang_kuisun, " +
//                    "liangdui_zhongjiang_yingli," +
//                    " liangdui_zhongjiang_kuisun," +
//                    " dandui_zhongjiang_yingli," +
//                    " dandui_zhongjiang_kuisun," +
//                    " sanhao_zhongjiang_yingli, " +
//                    "sanhao_zhongjiang_kuisun)"
//                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

//            for (int i = 1; i <= 8; i++) {
//                prepareStatement.setInt(i, 1);
//            }
//            for (int i = 9; i <= 24; i++) {
//                prepareStatement.setDouble(i, 1);
//            }
//
//            prepareStatement.execute();
//            prepareStatement.clearParameters();
//            insertData(new TicletTab(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    int count = 0;

    public synchronized void insertData(TicletTab ticletTab, boolean b) throws SQLException {
        count++;

        if (ticletTab != null) {

            //        "touzhu_wutiao , " +
//                " touzhu_zadan ," +
//                "touzhu_hulu ," +
//                "touzhu_shunzi , " +
//                " touzhu_santiao ," +
//                "touzhu_liangdui ," +
//                "touzhu_dandui ," +
//                "touzhu_sanhao ," +

            prepareStatement.setInt(1, ticletTab.getTouzhu_wutiao());
            prepareStatement.setInt(2, ticletTab.getTouzhu_zadan());
            prepareStatement.setInt(3, ticletTab.getTouzhu_hulu());
            prepareStatement.setInt(4, ticletTab.getTouzhu_shunzi());
            prepareStatement.setInt(5, ticletTab.getTouzhu_santiao());
            prepareStatement.setInt(6, ticletTab.getTouzhu_liangdui());
            prepareStatement.setInt(7, ticletTab.getTouzhu_dandui());
            prepareStatement.setInt(8, ticletTab.getTouzhu_sanhao());

            //                "wutiao_zhongjiang_yingli ," +
//                "wutiao_zhongjiang_kuisun, " +
//                "zadan_zhongjiang_yingli, " +
//                "zadan_zhongjiang_kuisun, " +
//                "hulu_zhongjiang_yingli, " +
//                "hulu_zhongjiang_kuisun, " +
//                "shunzi_zhongjiang_yignli, " +
//                "shunzi_zhongjiang_kuisun, " +
//                "santiao_zhongjiang_yingli, " +
//                "santiao_zhongjiang_kuisun, " +
//                "liangdui_zhongjiang_yingli," +
//                " liangdui_zhongjiang_kuisun," +
//                " dandui_zhongjiang_yingli," +
//                " dandui_zhongjiang_kuisun," +
//                " sanhao_zhongjiang_yingli, " +
//                "sanhao_zhongjiang_kuisun)"

            prepareStatement.setDouble(9, ticletTab.getWutiao_zhongjiang_yingli());
            prepareStatement.setDouble(10, ticletTab.getWutiao_zhongjiang_kuisun());

            prepareStatement.setDouble(11, ticletTab.getZadan_zhongjiang_yingli());
            prepareStatement.setDouble(12, ticletTab.getZadan_zhongjiang_kuisun());

            prepareStatement.setDouble(13, ticletTab.getHulu_zhongjiang_yingli());
            prepareStatement.setDouble(14, ticletTab.getHulu_zhongjiang_kuisun());

            prepareStatement.setDouble(15, ticletTab.getShunzi_zhongjiang_yignli());
            prepareStatement.setDouble(16, ticletTab.getShunzi_zhongjiang_kuisun());

            prepareStatement.setDouble(17, ticletTab.getSantiao_zhongjiang_yingli());
            prepareStatement.setDouble(18, ticletTab.getSantiao_zhongjiang_kuisun());

            prepareStatement.setDouble(19, ticletTab.getLiangdui_zhongjiang_yingli());
            prepareStatement.setDouble(20, ticletTab.getLiangdui_zhongjiang_kuisun());

            prepareStatement.setDouble(21, ticletTab.getDandui_zhongjiang_yingli());
            prepareStatement.setDouble(22, ticletTab.getDandui_zhongjiang_kuisun());

            prepareStatement.setDouble(23, ticletTab.getSanhao_zhongjiang_yingli());
            prepareStatement.setDouble(24, ticletTab.getSanhao_zhongjiang_kuisun());

            prepareStatement.addBatch();
        }
        if (count >= 2 * 1000 || b) {
            prepareStatement.executeBatch();
            prepareStatement.clearParameters();
            con.commit();
            count = 0;
        }


    }

    public ResultSet queryData(String query) {

        try {
            return statement.executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertData(Iinsert iinsert, Object info) throws SQLException {
        iinsert.insertData(info);
    }


    public PreparedStatement prepareInsert() throws SQLException {
        return con.prepareStatement("insert into " + tableName + " (  ordern,number,periods,location,detail,niuniu,alie,date )values(?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement prepareInsert(String sql) throws SQLException {

        return con.prepareStatement(sql);
    }

    public void commit() throws SQLException {
        con.commit();
    }


    public Statement prepareQuery() throws SQLException {
        if (statement1 ==null || statement1.isClosed()  )
         statement1 = con.createStatement();
        return statement1;
    }

    public PreparedStatement prepareUpdata(DataTask.Info info) throws SQLException {
        String sql = "update   " + tableName + " set   ordern ='" + info.order + "' ,  number='" + info.number + "' , periods='" + info.periods
                + "' , location=" + info.location + " , detail ='" + info.detail + "', date ='" + info.date + "',niuniu='" +info.niuniu+"'  where periods='" 
                + info.periods +"'";
        System.err.println(sql);
        return con.prepareStatement(sql);
    } 
    
    public PreparedStatement prepareUpdata(String sql) throws SQLException {
        return con.prepareStatement(sql);
    }

    public void close() {
        try {
            con.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
