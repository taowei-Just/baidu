package baidu.bean;

public class MatchWinInfo {

    //下注指示
    public int inde = 4;
    //期望盈利百分百
    public double present = 0.4;
    //期望最小盈利值
    public double assign = 10;
    // 最大下注金额
    public double maxMoney = 5000;
    //止损百分比
    public double zhiSunP = 0.4;
    //已投注开始止损金额
    public double zhisunMoney = -1;
    //熔断百分比
    public double rongduaPre = 0.9;
    //熔断金额
    public double rongduanMoney = -1;
    //止损
    public double zhisunLoseP = 0.1;
    // 止损盈利递减百分比
    public double decreasP = 0.05;
    //第一期下注金额
    public double firstM =  -1;
    public boolean useRongd =true;
    public  int count = 1000 ;
    public MatchWinInfo setFirstM(double firstM) {
        this.firstM = firstM;
        return this;
    }

    public MatchWinInfo setInde(int inde) {
        this.inde = inde;
        return this;
    }

    public MatchWinInfo setPresent(double present) {
        this.present = present;
        return this;
    }

    public MatchWinInfo setAssign(double assign) {
        this.assign = assign;
        return this;
    }

    public MatchWinInfo setMaxMoney(double maxMoney) {
        this.maxMoney = maxMoney;
        return this;
    }

    public MatchWinInfo setZhiSunP(double zhiSunP) {
        this.zhiSunP = zhiSunP;
        return this;
    }

    public MatchWinInfo setZhisunMoney(double zhisunMoney) {
        this.zhisunMoney = zhisunMoney;
        return this;
    }

    public MatchWinInfo setRongduaPre(double rongduaPre) {
        this.rongduaPre = rongduaPre;
        return this;
    }

    public MatchWinInfo setRongduanMoney(double rongduanMoney) {
        this.rongduanMoney = rongduanMoney;
        return this;
    }

    public MatchWinInfo setZhisunLoseP(double zhisunLoseP) {
        this.zhisunLoseP = zhisunLoseP;
        return this;
    }

    public MatchWinInfo setDecreasP(double decreasP) {
        this.decreasP = decreasP;
        return this;
    }

    @Override
    public String toString() {
        return "MatchWinInfo{" +
                "inde=" + inde +
                ", present=" + present +
                ", assign=" + assign +
                ", maxMoney=" + maxMoney +
                ", zhiSunP=" + zhiSunP +
                ", zhisunMoney=" + zhisunMoney +
                ", rongduaPre=" + rongduaPre +
                ", rongduanMoney=" + rongduanMoney +
                ", zhisunLoseP=" + zhisunLoseP +
                ", decreasP=" + decreasP +
                '}';
    }

}
