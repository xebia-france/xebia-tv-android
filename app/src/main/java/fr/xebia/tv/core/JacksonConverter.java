package fr.xebia.tv.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * A {@link retrofit.converter.Converter} which uses Jackson for reading and writing entities.
 *
 * @author Kai Waldron (kaiwaldron@gmail.com)
 */
public class JacksonConverter implements Converter {
    private static final String MIME_TYPE = "application/json; charset=UTF-8";

    public static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructType(type);
            return MAPPER.readValue(body.in(), javaType);
        } catch (JsonParseException e) {
            throw new ConversionException(e);
        } catch (JsonMappingException e) {
            throw new ConversionException(e);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override public TypedOutput toBody(Object object) {
        try {
            String json = MAPPER.writeValueAsString(object);
            return new TypedByteArray(MIME_TYPE, json.getBytes("UTF-8"));
        } catch (JsonProcessingException e) {
            throw new AssertionError(e);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
