package baidu.bean;

import org.jetbrains.annotations.NotNull;

public class PourInfo implements Comparable<PourInfo> {
    public int id;
    public String issue;
    public double moey;
    public double win;
    public double total;


    @Override
    public String toString() {
        return "PourInfo{" +
                "id=" + id +
                ", issue='" + issue + '\'' +
                ", moey=" + moey +
                ", win=" + win +
                ", total=" + total +
                '}';
    }

    @Override
    public int compareTo(@NotNull PourInfo o) {


        return id - o.id;
    }
}
