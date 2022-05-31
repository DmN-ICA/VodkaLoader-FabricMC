package DmN.ICA.vodka.json.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface JsonObjectParser {
    Byte getByte(String name);
    Short getShort(String name);
    Integer getInt(String name);
    Long getLong(String name);
    Float getFloat(String name);
    Double getDouble(String name);
    String getString(String name);
    Iterator<JsonElement> getArrIterator(String name);

    static JsonObjectParser create(JsonObject json) {
        return new JsonObjectParser() {
            @Override
            public Byte getByte(String name) {
                JsonElement element = json.get(name);
                return element == null ? null : element.getAsByte();
            }

            @Override
            public Short getShort(String name) {
                JsonElement element = json.get(name);
                return element == null ? null : element.getAsShort();
            }

            @Override
            public Integer getInt(String name) {
                JsonElement element = json.get(name);
                return element == null ? null : element.getAsInt();
            }

            @Override
            public Long getLong(String name) {
                JsonElement element = json.get(name);
                return element == null ? null : element.getAsLong();
            }

            @Override
            public Float getFloat(String name) {
                JsonElement element = json.get(name);
                return element == null ? null : element.getAsFloat();
            }

            @Override
            public Double getDouble(String name) {
                JsonElement element = json.get(name);
                return element == null ? null : element.getAsDouble();
            }

            @Override
            public String getString(String name) {
                JsonElement element = json.get(name);
                return element == null ? null : element.getAsString();
            }

            @Override
            public Iterator<JsonElement> getArrIterator(String name) {
                JsonArray arr = json.getAsJsonArray(name);
                return arr == null ? Collections.emptyIterator() : arr.iterator();
            }
        };
    }
}
