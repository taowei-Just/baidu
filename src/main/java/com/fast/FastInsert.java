package com.fast;

import com.Iinsert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FastInsert  implements Iinsert<FastAllColor.Content> {

    private Statement queryStatement;

    FastMysql mysql;
    private PreparedStatement preparedStatement;

    public FastInsert(FastMysql mysql) {
        this.mysql = mysql;
        try {
            preparedStatement = mysql.prepareInsert();
            queryStatement = mysql.prepareQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void insertData( FastAllColor.Content content) throws SQLException {

        if (queryData(content)){
//            updata(info);
        }else {
            preparedStatement.setString(1, content.resultInfo);
            preparedStatement.setString(2, content.sh);
            preparedStatement.setString(3, content.nn);
            preparedStatement.setString(4, content.issue);
            preparedStatement.setString(5, content.openingTime);
            preparedStatement.setInt(6, content.location);
            preparedStatement.setString(7, content.alie);
 
            boolean execute = preparedStatement.execute();
            mysql.commit();
        }

    }

    private boolean queryData( FastAllColor.Content info) throws SQLException {
        ResultSet resultSet =  queryStatement.executeQuery("select * from " + mysql.getTableName() + " where issue = '" + info.issue + "'");
        return resultSet ==null? false : resultSet.next();
    }

    @Override
    public void updata(FastAllColor.Content  info) throws SQLException {
        PreparedStatement   updataStatement = mysql.prepareUpdata(info);
        boolean execute = updataStatement.execute();
        mysql.commit();
        System.err.println( execute);
    }

  
   

    @Override
    public String queryMaxPro() throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("select max(periods)from " + mysql.getTableName() + " ");
        if (resultSet.next())
            return  resultSet.getString(1);
        return "";

    }

    public static void main(String [] s){
//        FastInsert ticket_data = new FastInsert(new FastMysql("ticket_data"));
//        try {
//         
// 
//
////            ResultSet resultSet = ticket_data.queryStatement.executeQuery("select * from " + ticket_data.mysql.getTableName() + " where periods = '" + info.periods + "'");
//            if (  resultSet.next()) {
//                String string = resultSet.getString(5);
//                System.err.println(string);
////                ticket_data.updata(info);
//            }else {
//                System.err.println(" null ");
////                ticket_data.insertData(info);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }
}
