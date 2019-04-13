package matchore;

public interface WaitTimeCall {
    void  onTimeOpen(long timeInMillis);
    void  onNoTime(long timeInMillis) ;
}
