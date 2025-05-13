package com.migros.tracking.common.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        if (com.migros.tracking.common.util.NumberUtils.isConvertable(value)) {
            long timestamp = Long.parseLong(value);
            return Instant.ofEpochMilli(timestamp);
        }
        try {
            return Instant.parse(value);
        } catch (Exception e) {
            throw new IOException("Value cannot be parsed as a numeric timestamp or ISO 8601 Instant: " + value, e);
        }
    }
}
