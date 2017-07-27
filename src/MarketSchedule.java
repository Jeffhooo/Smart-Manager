/**
 * Created by Jeff on 2017/7/24.
 */
public class MarketSchedule {
    private String planName;
    private String productName;
    private String promoteMethod;
    private Date beginDate;
    private Date endDate;
    private String promoteArea;

    public MarketSchedule(String planName, String productName, String promoteMethod, Date beginDate, Date endDate, String promoteArea) {
        this.planName = planName;
        this.productName = productName;
        this.promoteMethod = promoteMethod;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.promoteArea = promoteArea;
    }

    public String getProductName() {
        return productName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPromoteMethod() {
        return promoteMethod;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getPromoteArea() {
        return promoteArea;
    }
}
