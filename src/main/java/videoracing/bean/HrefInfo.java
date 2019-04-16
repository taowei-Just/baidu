package videoracing.bean;

public class HrefInfo {

    public  String hrefName ;
    public String currentHref ;
    public String historyHref ;

    public HrefInfo setHrefName(String hrefName) {
        this.hrefName = hrefName;
        return this;
    }

    public HrefInfo setCurrentHref(String currentHref) {
        this.currentHref = currentHref;
        return this;
    }

    public HrefInfo setHistoryHref(String historyHref) {
        this.historyHref = historyHref;
        return this;
    }

    @Override
    public String toString() {
        return "HrefInfo{" +
                "hrefName='" + hrefName + '\'' +
                ", currentHref='" + currentHref + '\'' +
                ", historyHref='" + historyHref + '\'' +
                '}';
    }
}
