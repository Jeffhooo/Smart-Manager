import java.util.Comparator;

/**
 * Created by Jeff on 2017/7/24.
 */
public class ProductWeekSalesComparator implements Comparator<Product> {
    public int compare(Product a, Product b) {
        int aWeekSales = a.getWeekSales();
        int bWeekSales = b.getWeekSales();
        if(aWeekSales < bWeekSales)
            return 1;
        else if(aWeekSales == bWeekSales)
            return 0;
        else
            return -1;
    }
}
