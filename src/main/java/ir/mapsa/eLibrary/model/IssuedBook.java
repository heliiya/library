package ir.mapsa.eLibrary.model;

import org.bson.Document;

import java.util.Date;

public class IssuedBook {

    private String callno;
    private Date issuedDate;
    private boolean returnStatus;

    public IssuedBook(String callno) {
        this.callno = callno;
        this.issuedDate = new Date();
        this.returnStatus = false;
    }

    public IssuedBook(Document document) {
        this.callno = document.getString("callno");
        this.issuedDate = document.getDate("issuedDate");
        this.returnStatus = document.getBoolean("returnStatus");
    }

    public static String getValidationErrorTxt(String callno) {
        if(callno == null || callno.trim().isEmpty() || callno.length()>20){
            return "Invalid callno";
        }
        return null;
    }

    public String getCallno() {
        return callno;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public boolean isReturnStatus() {
        return returnStatus;
    }

    public Document generateDocument(){
        Document doc = new Document();
        doc.append("callno", callno);
        doc.append("issuedDate", issuedDate);
        doc.append("returnStatus", returnStatus);
        return doc;
    }
}