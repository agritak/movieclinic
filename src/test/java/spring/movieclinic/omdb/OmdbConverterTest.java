package spring.movieclinic.omdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmdbConverterTest {

    @Mock
    ObjectMapper mapper;

    @InjectMocks
    OmdbConverter omdbConverter;

    @Test
    public void toBase64() throws JsonProcessingException {
        Object object = mock(Object.class);
        String json = "json";

        when(mapper.writeValueAsString(object)).thenReturn(json);

        Optional<String> optional = omdbConverter.toBase64(object);

        assertThat(optional.isPresent()).isTrue();

        verify(mapper).writeValueAsString(object);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void toBase64Movie_throwsException() throws JsonProcessingException {
        Object object = mock(Object.class);

        when(mapper.writeValueAsString(object)).thenThrow(new JsonProcessingException("") {
        });

        Optional<String> optional = omdbConverter.toBase64(object);

        assertThat(optional.isPresent()).isFalse();

        verify(mapper).writeValueAsString(object);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void fromBase64() throws JsonProcessingException {
        Object object = new Object();
        String base64 = "anNvbgoK";

        when(mapper.readValue(anyString(), eq(Object.class))).thenReturn(object);

        Optional<Object> optional = omdbConverter.fromBase64(base64, Object.class);

        assertThat(optional.isPresent()).isTrue();

        verify(mapper).readValue(anyString(), eq(Object.class));
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void fromBase64_throwsException() throws JsonProcessingException {
        String base64 = "anNvbgoK";

        when(mapper.readValue(anyString(), eq(Object.class))).thenThrow(new JsonProcessingException("") {
        });

        Optional<Object> optional = omdbConverter.fromBase64(base64, Object.class);

        assertThat(optional.isPresent()).isFalse();

        verify(mapper).readValue(anyString(), eq(Object.class));
        verifyNoMoreInteractions(mapper);
    }

}
