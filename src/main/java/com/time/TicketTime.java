package com.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class TicketTime {

    public static void main(String[] args) throws ParseException {
        new TicketTime().test(new MyTimerTask.TimeCAll() {
            @Override
            public void onTime(int postion, long next,long time) {
                System.err.println(" 当前期数:" + postion +  " 下一期时间 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(next))+" 当前时间 time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)) + " 倒计时：" + (next-System.currentTimeMillis())/1000 + "s");
            }
        });
    }


    public void test(MyTimerTask.TimeCAll cAll) throws ParseException {
        long offeset = 73*1000 ;
        String startTime = "09:00:00";
        long timeInterval = (long) (1.5 * 60 * 1000);
        Calendar instance = Calendar.getInstance();
        System.err.println("instance:"+instance.getTime());
        
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        instance.setTimeInMillis(System.currentTimeMillis());
        instance.set(instance.get(Calendar.YEAR),instance.get(Calendar.MONTH),instance.get(Calendar.DAY_OF_MONTH) ,format.parse(startTime).getHours(),0);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(instance.getTimeInMillis() -offeset,timeInterval ,cAll) , 1*1000 ,1*1000 );
    }


}
