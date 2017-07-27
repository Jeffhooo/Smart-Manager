import java.util.Comparator;

/**
 * Created by Jeff on 2017/7/24.
 */
public class FeatureWeekScoreComparator implements Comparator<Feature> {
    public int compare(Feature a, Feature b) {
        int aScore = a.getScore();
        int bScore = b.getScore();
        if(aScore < bScore)
            return 1;
        else if(aScore == bScore)
            return 0;
        else
            return -1;
    }
}
