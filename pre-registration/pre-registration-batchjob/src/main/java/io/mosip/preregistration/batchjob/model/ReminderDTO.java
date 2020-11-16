package io.mosip.preregistration.batchjob.model;

/**
 * @author CONDEIS
 */

public class ReminderDTO {

    private String preregId;
    private String appointementDate;
    private String slotFrom;
    private String toSlot;
    private String applicantfirstName;
    private String appliantLastName;
    private String applicantGender;
    private String applicantMobNum;
    private String applicantEmail;
    private String centerID;
    private String centerDetails;

    public String getPreRegId() {
        return preregId;
    }

    public String getAppointementDate() {
        return appointementDate;
    }

    public String getSlotFrom() {
        return slotFrom;
    }

    public String getToSlot() {
        return toSlot;
    }

    public String getApplicantName() {
        return applicantfirstName;
    }

    public String getApplicantMobNum() {
        return applicantMobNum;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getCenterID() {
        return centerID;
    }

    public String getCenterDetails() {
        return centerDetails;
    }

    /**
     * Remind constructor
     *
     * @param preRegId
     * @param appointementDate
     * @param slotFrom
     * @param toSlot
     * @param applicantfirstName
     * @param appliantLastName
     * @param applicantGender
     * @param applicantMobNum
     * @param applicantEmail
     * @param centerID
     */
    public ReminderDTO(String preRegId, String appointementDate, String slotFrom, String toSlot,
                       String applicantfirstName, String appliantLastName, String applicantGender, String applicantMobNum,
                       String applicantEmail, String centerID) {
        super();
        this.preregId = preRegId;
        this.appointementDate = appointementDate;
        this.slotFrom = slotFrom;
        this.toSlot = toSlot;
        this.applicantfirstName = applicantfirstName;
        this.appliantLastName = appliantLastName;
        this.applicantGender = applicantGender;
        this.applicantMobNum = applicantMobNum;
        this.applicantEmail = applicantEmail;
        this.centerID = centerID;
    }

    public String getApplicantfirstName() {
        return applicantfirstName;
    }

    public String getAppliantLastName() {
        return appliantLastName;
    }

    public String getApplicantGender() {
        return applicantGender;
    }

    @Override
    public String toString() {
        return "ReminderDTO [preregId=" + preregId + ", appointementDate=" + appointementDate + ", slotFrom=" + slotFrom
                + ", toSlot=" + toSlot + ", applicantfirstName=" + applicantfirstName + ", appliantLastName="
                + appliantLastName + ", applicantGender=" + applicantGender + ", applicantMobNum=" + applicantMobNum
                + ", applicantEmail=" + applicantEmail + ", centerID=" + centerID + ", centerDetails=" + centerDetails
                + "]";
    }
}
