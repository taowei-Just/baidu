package baidu.bean;

import com.sun.org.apache.regexp.internal.RE;

public class TicketInfo {

    // 下注类目
    public int indede = 4;
    // 单注价格单位
    public int dub = 3;
    //最小盈利与投入百分比
    public double precent = 0.6;
    //第一期投入金额
    public double first = -1;
    //账号
    public String account;
    //密码
    public String password;
    //标签
    public String tag;
    //与关盘最后一次盈利
    public int minIss = 50;
    // 下注倍数
    public int mulripe = 1;
    //彩票种类
    public int ticketKind = 1; // vr1.5   快速
    // 止损百分比
    public double stopLoss = 0.1;
    // 账户初始金额
    public double money = 4000;
    public String openMarket = "9";
    public String closeMarket = "6";
    public  boolean autoStop = true;
    public  boolean autoPrecent = true;
    public  boolean autoStopLoss = true;
    public  boolean  useDefaultPresent =true ;
    // 下注重试次数
    public int reInCount =3 ;
    //期望最小盈利
    public int expectW =10 ;
    public double rongDuanPre = 0.9;

    String logPath ;

    public void setReInCount(int reInCount) {
        this.reInCount = reInCount;
    }

    public void setAutoStopLoss(boolean autoStopLoss) {
        this.autoStopLoss = autoStopLoss;
    }

    public void setAutoStop(boolean autoStop) {
        this.autoStop = autoStop;
    }

    public void setAutoPrecent(boolean autoPrecent) {
        this.autoPrecent = autoPrecent;
    }

    public TicketInfo setIndede(int indede) {
        this.indede = indede;
        return this;
    }

    public TicketInfo setDub(int dub) {
        this.dub = dub;
        return this;
    }

    public TicketInfo setPrecent(double precent) {
        this.precent = precent;
        return this;
    }

    public TicketInfo setFirst(double first) {
        this.first = first;
        return this;
    }

    public TicketInfo setAccount(String account) {
        this.account = account;
        return this;
    }

    public TicketInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public TicketInfo setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public TicketInfo setMinIss(int minIss) {
        this.minIss = minIss;
        return this;
    }

    public TicketInfo setMulripe(int mulripe) {
        this.mulripe = mulripe;
        return this;
    }

    public TicketInfo setTicketKind(int ticketKind) {
        this.ticketKind = ticketKind;
        return this;
    }

    public TicketInfo setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
        return this;
    }

    public TicketInfo setMoney(double money) {
        this.money = money;
        return this;
    }

    public TicketInfo setOpenMarket(String openMarket) {
        this.openMarket = openMarket;
        return this;
    }

    public TicketInfo setCloseMarket(String closeMarket) {
        this.closeMarket = closeMarket;
        return this;
    }


}
