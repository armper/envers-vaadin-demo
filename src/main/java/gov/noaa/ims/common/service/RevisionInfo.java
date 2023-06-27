package gov.noaa.ims.common.service;

import java.util.Date;

public class RevisionInfo {

    private int revisionNumber;
    private Date revisionDate;
    private long revisionTimestamp;

    public RevisionInfo(int revisionNumber, Date revisionDate, long revisionTimestamp) {
        this.revisionNumber = revisionNumber;
        this.revisionDate = revisionDate;
        this.revisionTimestamp = revisionTimestamp;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public long getRevisionTimestamp() {
        return revisionTimestamp;
    }

    public void setRevisionTimestamp(long revisionTimestamp) {
        this.revisionTimestamp = revisionTimestamp;
    }

}
