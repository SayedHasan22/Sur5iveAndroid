package com.ja.sur5ive.services;

import java.util.HashMap;
import java.util.Map;

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
