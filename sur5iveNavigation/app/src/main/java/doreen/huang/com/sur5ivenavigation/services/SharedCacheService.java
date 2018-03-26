package doreen.huang.com.sur5ivenavigation.services;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import doreen.huang.com.sur5ivenavigation.MessageFragment;
import doreen.huang.com.sur5ivenavigation.R;

public class SharedCacheService {

    private static SharedCacheService instance = new SharedCacheService();

    private Map<String, Object> cache = new HashMap<>();

    public static SharedCacheService getInstance() {
        return instance;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    private SharedCacheService () {

    }

}
