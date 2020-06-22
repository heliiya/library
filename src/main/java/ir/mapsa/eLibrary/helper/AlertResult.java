package ir.mapsa.eLibrary.helper;

public class AlertResult {
    private AlertEnum alertEnum;
    private String alertTxt;

    public AlertResult(AlertEnum alertEnum, String alertTxt) {
        this.alertEnum = alertEnum;
        this.alertTxt = alertTxt;
    }

    public AlertEnum getAlertEnum() {
        return alertEnum;
    }

    public String getAlertTxt() {
        return alertTxt;
    }
}
