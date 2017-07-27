import java.util.ArrayList;

/**
 * Created by Jeff on 2017/7/24.
 */
public class RDSchedule {
    private String productName;
    private int id;
    private ArrayList<String> features;
    private Date beginDate;
    private Date endDate;

    public RDSchedule(String productName, int id, ArrayList<String> features, Date beginDate, Date endDate) {
        this.productName = productName;
        this.id = id;
        this.features = features;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public String getProductName() {
        return productName;
    }

    public int getId() {
        return id;
    }

    public Date getEndDate() {
        return endDate;
    }
}
