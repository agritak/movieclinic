package spring.movieclinic.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public final class OmdbDraftsList {
    @JsonProperty("Search")
    private List<OmdbDraft> omdbDrafts;
}
