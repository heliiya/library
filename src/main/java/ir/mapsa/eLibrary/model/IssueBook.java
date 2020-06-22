package ir.mapsa.eLibrary.model;

import org.bson.Document;

import java.util.Date;

public class IssueBook {

    private String callno;
    private Date issuedDate;
    private boolean returnStatus;

    public IssueBook(String callno, Date issuedDate, boolean returnStatus) {
        this.callno = callno;
        this.issuedDate = issuedDate;
        this.returnStatus = returnStatus;
    }

    public IssueBook(String callno) {
        this.callno = callno;
        this.issuedDate = new Date();
        this.returnStatus = false;
    }

    public IssueBook(Document issuedBookDoc) {
        this.callno = issuedBookDoc.getString("callno");
        this.issuedDate = issuedBookDoc.getDate("issuedDate");
        this.returnStatus = issuedBookDoc.getBoolean("returnStatus");
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