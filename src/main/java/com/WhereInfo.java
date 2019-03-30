package com;

import java.util.Arrays;

public class WhereInfo {

    /**
     *
     * 输入查询条件 得到结果
     *
     *
     */


    //组合
    public int[] ints ;
    //金额
    public  int money[] ;
    //最大单注金额
    public   int maxOneMoey ;
    //最大总金额
    public   int maxTotalMoey ;
    //期望 中奖
    public  int [] expect ;
    //最小yinli
    public double minWin = 5 ;



    @Override
    public String toString() {
        return "WhereInfo{" +
                "ints=" + Arrays.toString(ints) +
                ", money=" + Arrays.toString(money) +
                ", maxOneMoey=" + maxOneMoey +
                ", maxTotalMoey=" + maxTotalMoey +
                ", expect=" + Arrays.toString(expect) +
                '}';
    }
}
