package bg.sofia.uni.fmi.mjt.splitwise.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSingleton {
    private static final Gson GSON = new GsonBuilder().create();

    private GsonSingleton() {
    }

    public static Gson getInstance() {
        return GSON;
    }
}
