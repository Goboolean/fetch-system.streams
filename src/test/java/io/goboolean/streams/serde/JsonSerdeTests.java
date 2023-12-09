package io.goboolean.streams.serde;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import io.goboolean.streams.serde.Model.Trade;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class JsonSerdeTests {

    @Test
    public void testJsonSerde() {

        Trade trade = Trade.newBuilder()
                .setPrice(1000.05)
                .setSize(3)
                .setTimestamp(System.currentTimeMillis())
                .build();

        JsonSerde<Trade> serde = new JsonSerde<Trade>(
                new JsonSerde.JsonSerializer<>(),
                new JsonSerde.JsonDeserializer<>()
        );

        Serializer<Trade> serializer = serde.serializer();

        byte[] bytes = serializer.serialize("topic", trade);
        String JsonString = new String(bytes);

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(JsonString).getAsJsonObject();

        assertEquals(trade.getPrice(), json.get("price").getAsDouble());
        assertEquals(trade.getSize(), json.get("size").getAsInt());
        assertEquals(trade.getTimestamp(), json.get("timestamp").getAsLong());
    }
}
