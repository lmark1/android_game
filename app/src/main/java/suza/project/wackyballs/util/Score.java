package suza.project.wackyballs.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Score helper class.
 *
 * Created by lmark on 20/09/2017.
 */

public class Score {

    /**
     * Save score constant.
     */
    public static final String SAVE_SCORE_REQUEST = "save";

    /**
     * Save player name and score locally in Shared preferences
     *
     * @param name Player name.
     * @param score Player score.
     * @param sharedPreferences Application shared preferences.
     * @return True if score is saved, otherwise false.
     */
    public static boolean saveScoreLocally(String name, int score, SharedPreferences sharedPreferences) {
        Object getScore = sharedPreferences.getAll().get(name);

        // Don't save if current score is less than new score
        if (getScore != null && (Integer)getScore > score) {
            return false;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, score);
        editor.commit();
        return true;
    }


    /**
     * @return Returns sorted map from all elements found in shared preferences.
     *
     * @param sharedPreferences Application shared preferences.
     * @return Returns unmodifiable ordered map of names and score.
     */
    public static Map<String, Integer> getSortedScoreMap(SharedPreferences sharedPreferences) {

        // Get current score map)
        final Map<String, Integer> scoreMap = (Map<String, Integer>) sharedPreferences.getAll();

        // Initialize sorted map
        Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int i1 = scoreMap.get(o1);
                int i2 = scoreMap.get(o2);

                if (i1 < i2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        sortedMap.putAll(scoreMap);

        return Collections.unmodifiableMap(sortedMap);
    }

    /**
     * Runs a score request.
     *
     * @param context Activity context.
     * @param response Task response, executed when task is finished.
     */
    public static void getSortedScoreOnline(
            Context context,
            DatabaseRequest.AsyncResponse response) {

        DatabaseRequest request = new DatabaseRequest(context, response);
        request.execute(DatabaseRequest.REQUEST_SELECT_ALL);
    }

    /**
     * Get current player score.
     *
     * @param context Activity context.
     * @param name Player name.
     * @return String message. If null request passed without issue, oterwise not.
     */
    public static void saveScoreOnline(
            Context context,
            String name,
            Integer score,
            DatabaseRequest.AsyncResponse response) {

        DatabaseRequest request = new DatabaseRequest(context, response);
        request.execute(DatabaseRequest.REQUEST_INSERT_UPDATE, name, String.valueOf(score));
    }

}

