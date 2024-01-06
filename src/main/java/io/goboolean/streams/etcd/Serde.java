package io.goboolean.streams.etcd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serde {

    public static List<Map<String, String>> groupByPrefix(Map<String, String> input) throws IllegalArgumentException {
        Map<String, Map<String, String>> mapBased = new HashMap<>();
        String type = null;

        for (String key : input.keySet()) {
            String[] parts = key.split("/");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid key format");
            }

            if (type == null) {
                type = parts[1];
            } else if (!type.equals(parts[1])) {
                throw new IllegalArgumentException("ErrGivenTypeNotMatch");
            }
        }

        String prefix = "/" + type + "/";

        for (Map.Entry<String, String> entry : input.entrySet()) {
            String key = entry.getKey();
            String strippedKey = key.substring(prefix.length());
            String[] parts = strippedKey.split("/");
            if (parts.length < 1) {
                throw new IllegalArgumentException("Invalid key format");
            }

            String groupKey = parts[0];
            mapBased.computeIfAbsent(groupKey, k -> new HashMap<>()).put(key, entry.getValue());
        }

        return new ArrayList<>(mapBased.values());
    }

}
