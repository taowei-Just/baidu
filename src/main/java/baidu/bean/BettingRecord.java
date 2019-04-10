package baidu.bean;

public class BettingRecord {
    public int kind;
    public String detail;
    public String type;
    public String priods;
    public String desc;
    public String money;
    public String win;
    public String statue;
    public String cancellations;

    @Override
    public String toString() {
        return "BettingRecord{" +
                "kind=" + kind +
                ", detail='" + detail + '\'' +
                ", type='" + type + '\'' +
                ", priods='" + priods + '\'' +
                ", desc='" + desc + '\'' +
                ", money='" + money + '\'' +
                ", win='" + win + '\'' +
                ", statue='" + statue + '\'' +
                ", cancellations='" + cancellations + '\'' +
                '}';
    }
}
