package spring.movieclinic.omdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

import static java.util.Optional.empty;

@Component
@RequiredArgsConstructor
public class OmdbConverter {
    private final ObjectMapper mapper;

    Optional<String> toBase64(Object object) {
        try {
            return Optional.of(encode(mapper.writeValueAsString(object)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return empty();
    }

    <T> Optional<T> fromBase64(String base64, Class<T> clazz) {
        try {
            return Optional.of(mapper.readValue(decode(base64), clazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return empty();
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    private String decode(String base64) {
        return new String(Base64.getDecoder().decode(base64));
    }

}

