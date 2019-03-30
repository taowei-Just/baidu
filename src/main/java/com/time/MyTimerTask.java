package com.time;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    long startTime ;
    long intervalTime ;
    TimeCAll cAll;

    public MyTimerTask(long startTime, long intervalTime, TimeCAll cAll) {
        this.startTime = startTime;
        this.intervalTime = intervalTime;
        this.cAll = cAll;
    }

    @Override
    public void run() {
  
        if (System.currentTimeMillis() < startTime){
            cAll.onTime(0 , startTime  , System.currentTimeMillis());
        }else {
            if ( System.currentTimeMillis()-startTime >24*60*60*1000){
                startTime += 24*60*60*1000 * ((System.currentTimeMillis()-startTime)/24*60*60*1000);
            }

            long sl = System.currentTimeMillis()-startTime ;

            int l = (int) (sl / intervalTime+1);
            long l1 = sl % intervalTime;
            
            cAll.onTime(l,System.currentTimeMillis() + (l1 >0 ? intervalTime-l1:sl ), System.currentTimeMillis());
        }
        
    }

   public interface TimeCAll {
        
        // 第几期  当前时间 
        void onTime(int postion, long nexTime, long time);
        
        
    }

}
