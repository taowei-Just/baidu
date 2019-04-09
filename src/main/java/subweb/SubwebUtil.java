package subweb;

import com.PattenUtil;
import com.ejin.quickhttp.QuickClient;
import com.ejin.quickhttp.StringCallback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import subweb.subRun.SubWebInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubwebUtil {

    private static String  url = "https://numbers.videoracing.com/analy_11_1.aspx?SortType=desc&DType=Analy100";

    public static void main(String[] args) throws IOException {
        final String[] str = {""};
//                new OkHttpClient().newCall(new Request.Builder().get().url(url).build()).execute().body().string();
        
        String body = Jsoup.connect(url).get().outerHtml();
        str[0]=body;


        SubWebInfo[] webInfoS = new SubWebInfo[1];
      
        webInfoS[0] = new SubWebInfo();
        webInfoS[0].startD = "Con_BonusCode=\"";
        webInfoS[0].endD = "\";";
        webInfoS[0].coner = false;

        System.err.println(subWeb(str[0], webInfoS).toString());
//        new QuickClient.Builder().connectTimeout(20*1000).build().get(url, new StringCallback() {
//            @Override
//            public void onSuccess(String s) {
//                str[0] =s ;
//
//                SubWebInfo[] webInfoS = new SubWebInfo[3];
//                webInfoS[0] = new SubWebInfo();
//                webInfoS[0].startD = "=";
//                webInfoS[0].endD = "</ul>";
//
//                webInfoS[1] = new SubWebInfo();
//                webInfoS[1].startD = "\"indicator2\">";
//                webInfoS[1].endD = "</a></li></ul>";
//
//                webInfoS[2] = new SubWebInfo();
//                webInfoS[2].startD = "<ahref=\"open_14_1.aspx\">";
//                webInfoS[2].endD = "</a></li><li>";
//                webInfoS[2].coner = false;
//
//
//                System.err.println(subWeb(str[0], webInfoS).toString());
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                System.err.println(error);
//            }
//        });

    }

    public static List<List>  subWeb(String str, SubWebInfo[] webInfoS) {
        return subWeb(str, webInfoS, null);
    }

    public static List<List> subWeb(String str, SubWebInfo[] webInfoS, List<List> results) {

        if (results == null)
            results = new ArrayList<>();

        str = PattenUtil.replaceBlank(str).replace(" ", "");
        System.err.println("subWeb_"+str+"\n ");
 
            SubWebInfo webInfo = webInfoS[0];
            System.err.println(webInfo.toString());
            List<String> subUtil = PattenUtil.getSubUtil(str, webInfo.startD + "(.*?)" + webInfo.endD, webInfo.coner);
        if (subUtil==null)
            return results;
        
//            System.err.println(subUtil.toString());
            
            SubWebInfo[] subS = new SubWebInfo[webInfoS.length-1];
            try {
                System.arraycopy(webInfoS, 1, subS, 0, subS.length);
            } catch (Exception e) {
                subS=new SubWebInfo[0];
            }
       
            
            if (subUtil.size() > 0 && subS.length>0) {
                for (String s : subUtil) {
                   subWeb(s, subS, results); 
                }
            }else {
                results.add(subUtil);
            }
        return results;
    }

 
}
