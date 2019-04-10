package baidu.bean;

public class WinInfo {
    public String Numner;
    public String issue;
    public int wiState;
    public int winInde;
    public String currentIssue;

    @Override
    public String toString() {
        return "WinInfo{" +
                "Numner='" + Numner + '\'' +
                ", issue='" + issue + '\'' +
                ", wiState=" + wiState +
                ", winInde=" + winInde +
                ", currentIssue='" + currentIssue + '\'' +
                '}';
    }
}
