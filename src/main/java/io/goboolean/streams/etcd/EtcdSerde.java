package io.goboolean.streams.etcd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtcdSerde<T extends Model> {

    private final Class<T> type;

    public EtcdSerde(Class<T> type) {
        this.type = type;
    }

    public List<Map<String, String>> groupByPrefix(Map<String, String> input) throws IllegalArgumentException {
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

    public Map<String, String> serialize(T model) throws IllegalAccessException {
        Map<String, String> result = new HashMap<>();
        Class<?> cls = model.getClass();

        String id = null;
        for (Field field : cls.getDeclaredFields()) {
            Etcd annotation = field.getAnnotation(Etcd.class);
            if (annotation != null) {
                field.setAccessible(true);
                String fieldValue = String.valueOf(field.get(model));

                if ("id".equals(annotation.value())) {
                    id = fieldValue;
                    break;
                }
            }
        }

        String prefix = String.format("/%s/%s/", model.getName(), id);
        result.put(String.format("/%s/%s", model.getName(), id), "");

        for (Field field : cls.getDeclaredFields()) {
            Etcd annotation = field.getAnnotation(Etcd.class);

            System.out.println("annotation value: " + annotation.value());
            if (annotation != null && !"id".equals(annotation.value())) {
                field.setAccessible(true);
                String fieldValue = String.valueOf(field.get(model));

                String key = String.format("%s%s", prefix, annotation.value());
                result.put(key, fieldValue);
            }
        }

        return result;
    }

    public Map<String, String> serializeList(List<T> models) throws IllegalAccessException {
        Map<String, String> result = new HashMap<>();
        for (T model : models) {
            result.putAll(serialize(model));
        }
        return result;
    }

    public T deserialize(Map<String, String> input) throws IllegalAccessException {
        T result;

        try {
            result = type.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        Class<?> cls = result.getClass();


        String id = null;
        for (Map.Entry<String, String> entry : input.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("/");
            if (parts.length < 4) continue;

            String modelType = parts[1];
            if (!result.getName().equals(modelType)) {
                throw new IllegalArgumentException("Type mismatch");
            }

            String modelId = parts[2];
            if (id == null) {
                id = modelId;
            } else if (!id.equals(modelId)) {
                throw new IllegalArgumentException("ID mismatch");
            }

            String fieldName = parts[3];
            for (Field field : cls.getDeclaredFields()) {
                Etcd annotation = field.getAnnotation(Etcd.class);

                if (annotation != null && fieldName.equals(annotation.value())) {
                    field.setAccessible(true);
                    field.set(result, entry.getValue());
                }
            }
        }

        for (Field field : cls.getDeclaredFields()) {
            Etcd annotation = field.getAnnotation(Etcd.class);
            if (annotation != null && "id".equals(annotation.value())) {
                field.setAccessible(true);
                field.set(result, id);
            }
        }

        return result;
    }
}
