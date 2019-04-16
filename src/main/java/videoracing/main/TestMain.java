package videoracing.main;

import baidu.utils.Out;
import org.jsoup.Jsoup;
import subweb.SubwebUtil;
import subweb.subRun.SubWebInfo;
import videoracing.bean.HrefInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static String videoracingUrl = "https://numbers.videoracing.com/";

    public static void main(String[] args) {
        try {
            new TestMain().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException {
        String s = Jsoup.connect(videoracingUrl).get().outerHtml();
        List<HrefInfo> noticeHrefS = matchNoticeHref(s);
        List<HrefInfo> sisHrefS = matchAnalySisHref(s);
        
        
        sisHrefS.addAll(noticeHrefS);
        for (HrefInfo hrefInfo : sisHrefS) {
            Out.d(hrefInfo.toString());
        }

    }

    private List<HrefInfo> matchAnalySisHref(String s) {
        SubWebInfo[] subWebInfos = new SubWebInfo[2];
        subWebInfos[0] = new SubWebInfo().setStartD("<liclass=\"li_menu_4\">").setEndD("</li><liclass=\"li_menu");
        subWebInfos[1] = new SubWebInfo().setStartD("<ahref=\"").setEndD("</a></li>");

        List<HrefInfo> hrefInfos = new ArrayList<>();
        List<List> lists = SubwebUtil.subWeb(s, subWebInfos);
        for (List list : lists) {
            for (Object o : list) {
//                Out.d(o.toString());
                try {
                    List<List> lists1 = SubwebUtil.subWeb((String) o, new SubWebInfo[]{new SubWebInfo().setStartD("<ahref=\"").setEndD("\">").setConer(true)});


                    try {

                        if (((String) lists1.get(0).get(1)).contains("<ahref=\""))
                            lists1 = SubwebUtil.subWeb((String) lists1.get(0).get(1), new SubWebInfo[]{new SubWebInfo().setStartD("<ahref=\"").setEndD("\">").setConer(true)});
                    } catch (Exception e) {

                    }


                    lists1 = SubwebUtil.subWeb((String) lists1.get(0).get(0), new SubWebInfo[]{new SubWebInfo().setStartD("<ahref=\"").setEndD("\">").setConer(false)});


                    List<List> lists2 = SubwebUtil.subWeb((String) o, new SubWebInfo[]{new SubWebInfo().setStartD("aspx\">").setEndD("</a></li>").setConer(false)});

                    HrefInfo hrefInfo = new HrefInfo().setHrefName((String) lists2.get(0).get(0)).setCurrentHref((String) lists1.get(0).get(0));
                    hrefInfos.add(hrefInfo);
                } catch (Exception e) {
                }

            }

        }
        return hrefInfos;

    }

    public static List<HrefInfo> matchNoticeHref(String s) {
        SubWebInfo[] subWebInfos = new SubWebInfo[2];
        subWebInfos[0] = new SubWebInfo().setStartD("<liclass=\"li_menu_3\">").setEndD("</li><liclass=\"li_menu");
        subWebInfos[1] = new SubWebInfo().setStartD("<ahref").setEndD("</a></li></ul></li><li>");
//        subWebInfos[2] = new SubWebInfo().setStartD("<ahref=").setEndD("><");

        List<HrefInfo> hrefInfos = new ArrayList<>();
        List<List> lists = SubwebUtil.subWeb(s, subWebInfos);
        for (List list : lists) {
            for (Object o : list) {
//                Out.d(o.toString());
                try {

                    // 名称
                    List<List> subWeb = SubwebUtil.subWeb((String) o, new SubWebInfo[]{new SubWebInfo().setStartD(">").setEndD("<emclass=").setConer(true)});

                    try {
                        String s1 = (String) subWeb.get(0).get(1);
//                        Out.d(s1  );
                        if (s1.contains("\">"))
                            subWeb = SubwebUtil.subWeb(s1, new SubWebInfo[]{new SubWebInfo().setStartD("\">").setEndD("<emclass=").setConer(true)});

                    } catch (Exception e) {

                    }
                    subWeb = SubwebUtil.subWeb((String) subWeb.get(0).get(0), new SubWebInfo[]{new SubWebInfo().setStartD(">").setEndD("<emclass=").setConer(false)});


                    // 当期  </a><ul><li><ahref="open_1_1.aspx">Currentdrawingnumbe
                    List<List> subWeb1 = SubwebUtil.subWeb((String) o, new SubWebInfo[]{new SubWebInfo().setStartD("<emclass=\"indicator2\"></em></a><ul><li><ahref=\"").setEndD("\">Currentdrawingnumbe").setConer(true)});

                    //</a><ul><li><ahref="javascript:void(0)">VRVenus1.5Lottery<emclass="indicator2"></em></a><ul><li><ahref="open_3_1.aspx">Currentdrawingnumbe

//                    Out.d( " 1 "+subWeb1.toString());

                    try {
                        if (((String) subWeb1.get(0).get(0)).contains("</em></a><ul><li><ahref=\""))
                            subWeb1 = SubwebUtil.subWeb(((String) subWeb1.get(0).get(0)), new SubWebInfo[]{new SubWebInfo().setStartD("</a><ul><li><ahref=\"").setEndD("\">Currentdrawingnumbe").setConer(true)});

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    subWeb1 = SubwebUtil.subWeb((String) subWeb1.get(0).get(0), new SubWebInfo[]{new SubWebInfo().setStartD("<ahref=\"").setEndD("\">").setConer(false)});
//                    Out.d("  2 "+subWeb1.toString());

                    //历史连接
                    List<List> subWeb2 = SubwebUtil.subWeb((String) o, new SubWebInfo[]{new SubWebInfo().setStartD("Currentdrawingnumber</a></li><li><ahref=\"").setEndD("\">Historicaldrawingnumber").setConer(false)});


                    HrefInfo hrefInfo = new HrefInfo().setHrefName((String) subWeb.get(0).get(0)).setCurrentHref((String) subWeb1.get(0).get(0)).setHistoryHref((String) subWeb2.get(0).get(0));

                    hrefInfos.add(hrefInfo);
                } catch (Exception e) {
                }

            }

        }
        return hrefInfos;

    }

}
