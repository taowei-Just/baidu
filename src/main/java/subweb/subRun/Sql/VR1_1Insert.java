package subweb.subRun.Sql;

import com.Iinsert;
import com.TestMysql;
import com.TicketInfoInsert;
import com.runn.DataTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 

public class VR1_1Insert implements Iinsert<DataTask.Info> {

    private Statement queryStatement;
    TestMysql mysql;
    private PreparedStatement preparedStatement;

    public VR1_1Insert(TestMysql mysql) {
        this.mysql = mysql;
        try {
            preparedStatement = mysql.prepareInsert();
            queryStatement = mysql.prepareQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void insertData(DataTask.Info info) throws SQLException {

        if (queryData(info)){
//            updata(info);
        }else {
            preparedStatement.setString(1, info.order);
            preparedStatement.setString(2, info.number);
            preparedStatement.setString(3, info.periods);
            preparedStatement.setInt(4, info.location);
            preparedStatement.setString(5, info.detail);
            preparedStatement.setString(7, info.alie);
            preparedStatement.setString(6, info.niuniu);
            preparedStatement.setString(8, info.date);
            boolean execute = preparedStatement.execute();
            mysql.commit();
        }

    }

    private boolean queryData(DataTask.Info info) throws SQLException {
        ResultSet resultSet =  queryStatement.executeQuery("select * from " + mysql.getTableName() + " where periods ='" + info.periods + "'");
        return resultSet ==null? false : resultSet.next();
    }

    @Override
    public void updata(DataTask.Info info) throws SQLException {
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
        VR1_1Insert ticket_data = new VR1_1Insert(new TestMysql("ticket_data"));
        try {
            DataTask.Info info = new DataTask.Info();
            info.location=1;
            info.detail="人民 警察 55555";
            info.periods="121";
            info.number="121";
            info.order="121";
            info.date="121";
//            ticket_data.insertData(info);
//

            ResultSet resultSet = ticket_data.queryStatement.executeQuery("select * from " + ticket_data.mysql.getTableName() + " where periods = '" + info.periods + "'");
            if (  resultSet.next()) {
                String string = resultSet.getString(5);
                System.err.println(string);
                ticket_data.updata(info);
            }else {
                System.err.println(" null ");
                ticket_data.insertData(info);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
