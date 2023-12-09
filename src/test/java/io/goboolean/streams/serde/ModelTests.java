package io.goboolean.streams.serde;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.goboolean.streams.serde.Model.Aggregate;
import io.goboolean.streams.serde.Model.Trade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.checkerframework.checker.units.qual.m;

@SpringBootTest
public class ModelTests {

    @Test
    public void testTradeSerde() throws Exception {

        double price = 1000.05;
        long size = 3;
        long time = System.currentTimeMillis();

        Trade trade = Trade.newBuilder()
                .setPrice(price)
                .setSize(size)
                .setTimestamp(time)
                .build();

        byte[] payload = trade.toByteArray();

        Trade newTrade;
        try {
            newTrade = Trade.parseFrom(payload);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            e.printStackTrace();
            throw new Exception("Failed to parse protobuf data", e);
        }

        // Assertions to verify the data is the same
        assertEquals(price, newTrade.getPrice());
        assertEquals(size, newTrade.getSize());
        assertEquals(time, newTrade.getTimestamp());
    }

    @Test
    public void testAggregateSerde() throws Exception {

        double minPrice = 1000.05;
        double maxPrice = 2000.35;
        long size = 3;
        long time = System.currentTimeMillis();

        Aggregate aggregate = Aggregate.newBuilder()
                .setOpen(time)
                .setClosed(time)
                .setMin(minPrice)
                .setMax(maxPrice)
                .setVolume(size)
                .setTimestamp(time)
                .build();

        byte[] payload = aggregate.toByteArray();

        Aggregate newAggregate;
        try {
            newAggregate = Aggregate.parseFrom(payload);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            e.printStackTrace();
            throw new Exception("Failed to parse protobuf data", e);
        }

        assertEquals(minPrice, newAggregate.getMin());
        assertEquals(maxPrice, newAggregate.getMax());
        assertEquals(size, newAggregate.getVolume());
        assertEquals(time, newAggregate.getOpen());
        assertEquals(time, newAggregate.getClosed());
        assertEquals(time, newAggregate.getTimestamp());
    }
}
