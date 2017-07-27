/**
 * Created by Jeff on 2017/7/25.
 */
public class DistributionSchedule {
    private String ingredientName;
    private int storeNumber;
    private Date shipInDate;
    private Date shipOutDate;
    private String destination;

    public DistributionSchedule(String ingredientName, int storeNumber, Date shipInDate, Date shipOutDate, String destination) {
        this.ingredientName = ingredientName;
        this.storeNumber = storeNumber;
        this.shipInDate = shipInDate;
        this.shipOutDate = shipOutDate;
        this.destination = destination;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public Date getShipInDate() {
        return shipInDate;
    }

    public Date getShipOutDate() {
        return shipOutDate;
    }

    public String getDestination() {
        return destination;
    }
}
