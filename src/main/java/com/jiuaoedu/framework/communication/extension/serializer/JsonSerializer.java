package com.jiuaoedu.framework.communication.extension.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.api.message.serializer.MessageSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonSerializer implements MessageSerializer {
    private final Gson gson;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public JsonSerializer() {
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(MessageType.class, new MessageTypeAdapter())
            .create();
    }

    @Override
    public String serialize(IMessage message) {
        return gson.toJson(message);
    }

    @Override
    public IMessage deserialize(String serializedMessage) {
        return gson.fromJson(serializedMessage, IMessage.class);
    }

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(value.format(FORMATTER));
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return LocalDateTime.parse(in.nextString(), FORMATTER);
        }
    }

    private static class MessageTypeAdapter extends TypeAdapter<MessageType> {
        @Override
        public void write(JsonWriter out, MessageType value) throws IOException {
            out.value(value.name());
        }

        @Override
        public MessageType read(JsonReader in) throws IOException {
            return MessageType.valueOf(in.nextString());
        }
    }
}