/*
by Anthony Stump
Created: 18 Feb 2018

Applies to multiple tables:
    Core.FB_CFCKxx
    Core.FB_CFSVxx
    Core.FB_DICCxx
    Core.FB_ONCCxx

 */

package asWebRest.model;

public class Accounts {
    
    private String bank;
    private double credit;
    private int ctid;
    private String date;
    private double debit;
    private String description;
    private String dueDate;
    private double extra;
    private double planned;
    private String referenceNo;
    private int stid;
    
    public String getBank() { return bank; }
    public double getCredit() { return credit; }
    public int getCtid() { return ctid; }
    public String getDate() { return date; }
    public double getDebit() { return debit; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public double getExtra() { return extra; }
    public double getPlanned() { return planned; }
    public String getReferenceNo() { return referenceNo; }
    public int getStid() { return stid; }
    
    public void setBank(String bank) { this.bank = bank; }
    public void setCredit(double credit) { this.credit = credit; }
    public void setCtid(int ctid) { this.ctid = ctid; }
    public void setDate(String date) { this.date = date; }
    public void setDebit(double debit) { this.debit = debit; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setExtra(double extra) { this.extra = extra; }
    public void setPlanned(double planned) { this.planned = planned; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }
    public void setStid(int stid) { this.stid = stid; }
    
    
}
