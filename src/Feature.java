/**
 * Created by Jeff on 2017/7/24.
 */
public class Feature {
    private String name;
    private int score;

    public Feature(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public String getName() {
        return this.name;
    }

}
