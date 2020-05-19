package spring.movieclinic.omdb;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class OmdbConverter {
    ObjectMapper mapper = new ObjectMapper();


    public void toBase64Movie(OmdbOption omdbOption) {
        try {
            String json = mapper.writeValueAsString(omdbOption);
            omdbOption.setBase64Movie(Base64.getEncoder().encodeToString(json.getBytes()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public OmdbOption fromBase64Movie(String base64Movie) {
        OmdbOption option = new OmdbOption();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Movie);
        String decodedString = new String(decodedBytes);
        try {
            option = mapper.readValue(decodedString, OmdbOption.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return option;
    }

}
