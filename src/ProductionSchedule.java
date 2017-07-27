/**
 * Created by Jeff on 2017/7/24.
 */
public class ProductionSchedule {
    private String ingredientName;
    private int produceNumber;
    private Date beginDate;
    private Date endDate;

    public ProductionSchedule(String ingredientName, int produceNumber, Date beginDate, Date endDate) {
        this.ingredientName = ingredientName;
        this.produceNumber = produceNumber;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getProduceNumber() {
        return produceNumber;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

}
