package matchore;

import baidu.utils.MapUtil;
import baidu.utils.Out;
import com.CoreMath;
import com.TestMysql;
import com.runn.DataTask;

import java.util.*;

public class MatchBig {

    private TestMysql maySql;

    public MatchBig() {
        maySql = new TestMysql("");
    }

    public static void main(String[] args) {
        MatchBig matchBig = new MatchBig();
        matchBig.start();
    }

    // 得出所有组合的最小出现间隔和该间隔的数量
    private void start() {
        String str = "SELECT distinct(periods), date,number,ordern,location,detail FROM ticket_data_vr_1_1  where date ='20190413'  ORDER BY date asc ,  periods asc";
        List<DataTask.Info> infos = MatchCore.querYInfoList(maySql, str);


        for (int i = 0; i < MatchCore.indedeS.length; i++) {
            Out.e(" \n  》》》》》》》》》》》》》》》》》》》》》 " + MatchCore.detailS[i]);
            matchGroupS(infos, i);
        }

    }

    /**
     * 1.得出各个组合的连续次数 预计对应的数量
     * 2. 得出连续出现n次后 其他组合出现的次数 及数量
      * @param infos
     * @param inde
     */    
    private void matchGroupS(List<DataTask.Info> infos, int inde) {
        List<GroupInfo> groupInfos = new ArrayList<>();
        for (int i = 0; i < infos.size() - 1; i++) {
            DataTask.Info info = infos.get(i);
            if (info.location != inde)
                continue;
                 groupS(infos, groupInfos, i);
            for (int j = i + 1; j < infos.size(); j++) {
                DataTask.Info info1 = infos.get(j);
                if (info1.location == inde) {
                    i = j;
                    break;
                }
            }

        }

        for (GroupInfo groupInfo : groupInfos) {
            Out.e(groupInfo.toString());
        }
    }

    public static void groupS(List<DataTask.Info> infos, List<GroupInfo> groupInfos, int i) {
        for (int j = 1; j < MatchCore.indedeS.length; j++) {
            GroupInfo group = null;
            for (GroupInfo groupInfo : groupInfos) {
                if (groupInfo.location == j) {
                    group = groupInfo;
                    break;
                }
            }
            if (group == null) {
                group = new GroupInfo();
                group.location = j;
                groupInfos.add(group);
            }

            groupMap(infos, i, j, group);

        }
    }

    public static void groupMap(List<DataTask.Info> infos, int i, int j, GroupInfo group) {
        // 组合往后最近间隔
        Map<Integer, Integer> mps = matchGroup(infos, i, j);
        GroupInfo finalGroup = group;
        MapUtil.iteratorMap(mps, new MapUtil.IteratorCall<Integer>() {
            @Override
            public MapUtil.IteratorType onIterator(Object key, Integer value) {

                if (!finalGroup.intS.contains(value)) {
                    finalGroup.intS.add(value);
                }
                if (finalGroup.countMap.containsKey(value)) {
                    finalGroup.countMap.put(value, finalGroup.countMap.get(value) + 1);

                } else {
                    finalGroup.countMap.put(value, 1);
                }

                return MapUtil.IteratorType.next;
            }
        });
    }

    public static Map<Integer, Integer> matchGroup(List<DataTask.Info> infos, int i, int j) {

        Map<Integer, Integer> mps = new HashMap<>();
        for (int k = i + 1; k < infos.size(); k++) {
            DataTask.Info info = infos.get(k);
            if (info.location != j)
                continue;
            mps.put(j, k - i);
            return mps;

        }
        return mps;

    }


    public static class GroupInfo {

        public int location;

        // 最近一次出现的间隔对应的数量
        public Map<Integer, Integer> countMap = new HashMap<>();

        // 最近一次出现的间隔
        public List<Integer> intS = new ArrayList<>();

        @Override
        public String toString() {
            Collections.sort(intS);
            return "GroupInfo{" +
                    "location=" + location +
                    ", countMap=" + countMap +
                    MatchCore.detailS[location] + " 间隔最长为：" + (intS.size() > 0 ? intS.get(intS.size() - 1) : "1") +
                    '}';
        }
    }
}
