package baidu.bean;

public class PourInfo {
    public int id ;
    public String issue ;
    public double moey ;
    public double win ;
    public double total ;

    @Override
    public String toString() {
        return "PourInfo{" +
                "issue='" + issue + '\'' +
                ", moey='" + moey + '\'' +
                ", win='" + win + '\'' +
                '}';
    }
}