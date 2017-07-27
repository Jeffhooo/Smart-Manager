import java.util.ArrayList;

/**
 * Created by Jeff on 2017/7/24.
 */
public class Product {
    private String name;
    private int id;
    private int weekSales;
    private ArrayList<Integer> eachDaySales;
    private ArrayList<String> feature;

    public Product(String name) {
        this.name = name;
    }

    public void setWeekSales(int weekSales) {
        this.weekSales = weekSales;
    }

    public void setEachDaySales(ArrayList<Integer> eachDaySales) {
        this.eachDaySales = eachDaySales;
    }

    public void setFeature(ArrayList<String> feature) {
        this.feature = feature;
    }

    public int getWeekSales() {
        return this.weekSales;
    }

    public String getName() {
        return this.name;
    }
}
