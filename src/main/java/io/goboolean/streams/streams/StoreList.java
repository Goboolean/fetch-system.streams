package io.goboolean.streams.streams;

import org.apache.kafka.streams.state.KeyValueStore;

import java.util.ArrayList;
import java.util.List;

public class StoreList<T> extends ArrayList<T> implements List<T> {

    private KeyValueStore<Integer, T> stateStore;

    public StoreList(KeyValueStore<Integer, T> stateStore) {
        super();
        this.stateStore = stateStore;
    }

    public boolean add(T data) {
        stateStore.put(data.hashCode(), data);
        return super.add(data);
    }

    public void clear() {
        stateStore.all().forEachRemaining(kv -> stateStore.delete(kv.key));
        super.clear();
    }
}