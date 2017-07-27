/**
 * Created by Jeff on 2017/7/25.
 */
public class Ingredient {
    private String ingredientName;
    private int store_number;

    public Ingredient(String ingredientName, int store_number) {
        this.ingredientName = ingredientName;
        this.store_number = store_number;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getStore_number() {
        return store_number;
    }
}
