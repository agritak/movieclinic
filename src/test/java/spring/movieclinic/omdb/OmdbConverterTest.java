package spring.movieclinic.omdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmdbConverterTest {
    @Mock
    ObjectMapper mapper;
    @InjectMocks
    OmdbConverter omdbConverter;

    @Test
    public void toBase64Movie() throws JsonProcessingException {
        String json = "json string";
        OmdbOption option = new OmdbOption();
        when(mapper.writeValueAsString(option)).thenReturn(json);

        omdbConverter.toBase64Movie(option);

        assertThat(option.getBase64Movie()).isNotEmpty();

        verify(mapper).writeValueAsString(option);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void toBase64Movie_throwsException() throws JsonProcessingException {

    }

}
