package com;
import java.util.List;

public class Ranking {

    /**
     * 按投入金额和 期望结果最大值排序
     * @param whereInfo
     * @param infoList
     */
    public void pour(WhereInfo whereInfo, List<HelloWorld.Info> infoList) {

        for (int i = 0; i < infoList.size(); i++) {
            HelloWorld.Info info = infoList.get(i);
            for (int j = i; j < infoList.size(); j++) {
                HelloWorld.Info info1 = infoList.get(j);
                if (info1.totalmoney < info.totalmoney){
                    infoList.set(i,info1);
                    infoList.set(j,info);
                }
            }
        }

        for (int i = 0; i < infoList.size(); i++) {
            HelloWorld.Info info = infoList.get(i);
            for (int j = i; j < infoList.size(); j++) {
                HelloWorld.Info info1 = infoList.get(j);
                if (info.totalmoney == info1.totalmoney){

//                    double v = maxResult(info.result);
//                    double v1 = maxResult(info1.result);

                    int inde =0 ;
                    for (int k = 0; k < info.number.length; k++) {
                        if (info.number[k] == whereInfo.expect[0]){
                            inde=k;
                        }
                    }

                    double v2 = info.result[ inde];
                    double v3 = info.result[ inde];
                    if(v3>v2){
                        infoList.set(i,info1);
                        infoList.set(j,info);
                    }

                }
            }
        }
    }

    private double maxResult(double[] result) {
        double max  =result[0];
        for (int i = 0; i < result.length; i++) {
            for (int j = i; j < result.length; j++) {
                if (result[j]>result[i]){
                    max =result[j];
                }

            }
        }
        return max ;
    }
}
