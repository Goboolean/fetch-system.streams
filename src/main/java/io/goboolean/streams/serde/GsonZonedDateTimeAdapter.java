package io.goboolean.streams.serde;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.time.ZoneOffset;

public class GsonZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

        @Override
        public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                long time = src.toEpochSecond() * 1_000_000_000L + src.getNano();
                return new JsonPrimitive(time);
        }

        @Override
        public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
                long time = json.getAsLong();
                return ZonedDateTime.ofInstant(Instant.ofEpochSecond(time / 1_000_000_000L, time % 1_000_000_000L), ZoneOffset.UTC);
        }
}