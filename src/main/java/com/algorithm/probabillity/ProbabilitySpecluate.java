package com.algorithm.probabillity;

import com.TestMysql;
import com.TicletTab;
import com.runn.DataTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProbabilitySpecluate {

    private final TestMysql testMysql;

    public static void main(String[] args) {
        ProbabilitySpecluate specluate = new ProbabilitySpecluate();
        specluate.start();
    }


    public ProbabilitySpecluate() {
        testMysql = new TestMysql("ticket_data_vr_1_1");
        
    }


    private void start() {
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date ='" +"'  ORDER BY date DESC ,  periods DESC";
        ResultSet resultSet = testMysql.queryData(str);
        List<DataTask.Info> infoList = new ArrayList<>();
        try {
    
            while (resultSet.next()) {
                DataTask.Info info = new DataTask.Info();
                info.periods = resultSet.getString(1);
                info.date = resultSet.getString(2);
                info.number = resultSet.getString(3);
                info.order = resultSet.getString(4);
                info.location = resultSet.getInt(5);
                info.detail = resultSet.getString(6);
                infoList.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        match01(infoList);

    }

    /**
     * 
     * @param infoList
     * 
     * 通过历史数据计算概率
     * 计算出在一定范围内出现某一值的概率
     * 
     * 
     * 
     */

    private void match01(List<DataTask.Info> infoList) {
        
        
        
        
        
        
    }


}
