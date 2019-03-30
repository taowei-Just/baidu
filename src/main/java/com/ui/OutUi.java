package com.ui;

import com.analyze.ArticleAnalyze;

public class OutUi  {
    
    /*
    *  最大值
    *  宽度
    * 
     */
    int maxCount =1000;
    int wedith  =1;
    String content = "▆";

    public void draw(int i, ArticleAnalyze.MatchInfo matchInfo) {
         
        StringBuilder stringBuilder = new StringBuilder();
        
        for (int j = 0; j <  matchInfo.interval; j++) {
            stringBuilder.append(content);
        }

        for (int j = 0; j <  40- matchInfo.interval; j++) {
            stringBuilder.append("‥");
        }
        
        String s = i + "";
        int length = s.length();
        for (int k= 0 ; k< 4-length ; k++ ){
            s+=" ";
        }
        System.err.println(""+s+" "+matchInfo.info2.periods+" "+stringBuilder + " " + matchInfo.interval);    
    }


    public static void main(String[] args) {
        OutUi outUi = new OutUi();
       
        for (int i = 0; i < 100; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j <= i; j++) {
                stringBuilder.append(outUi.content);
            }
            System.err.println(stringBuilder);
        }
  
    }
}
