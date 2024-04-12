package test.connect.geoexploreapp.model;

public class ReportedUser {
    private Long id;
    private Long reportedUserId;
    private User reportedUser;
    private Boolean Harassment;

    private Boolean Misinformation;
    private Boolean Spamming;
    private Boolean InappropriateContent;
    private int numReports;

    public ReportedUser(Long userId, Boolean harass, Boolean misInfo, Boolean spam, Boolean inappropriate){

        this.reportedUserId = userId;
        this.Harassment = harass;
        this.Misinformation = misInfo;
        this.Spamming = spam;
        this.InappropriateContent = inappropriate;

    }

    public ReportedUser(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public Boolean getHarassment() {
        return Harassment;
    }

    public void setHarassment(Boolean harassment) {
        Harassment = harassment;
    }

    public Boolean getMisinformation() {
        return Misinformation;
    }

    public void setMisinformation(Boolean misinformation) {
        Misinformation = misinformation;
    }

    public Boolean getSpamming() {
        return Spamming;
    }

    public void setSpamming(Boolean spamming) {
        Spamming = spamming;
    }

    public Boolean getInappropriateContent() {
        return InappropriateContent;
    }

    public void setInappropriateContent(Boolean inappropriateContent) {
        InappropriateContent = inappropriateContent;
    }

    public int getNumReports() {
        return numReports;
    }

    public void setNumReports(int numReports) {
        this.numReports = numReports;
    }
}
