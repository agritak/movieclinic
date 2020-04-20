package spring.movieclinic.omdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//TODO
// 1. Loģiku, kas veic pašu HTTP pieprasījumu un izmanto RestTemplate (ieskaitot visu URL izveidi)
//    vajadzētu atdalīt atsevišķā klasē. Ja nu jūs nākotnē gribēsiet kaut kur citur izmantot Omdb filmu meklēšanu,
//    vari arī pieprasīt datus par konkrētu filmu? Šo loģiku nevarēs tā vienkārši reusot, būs jāmaina
//    kods šajā klasē (vai jāraksta tas pats citur). Bet ja mēs šo loģiku iznestu atsevišķā klasē/komponentā, \
//    tad nākotnē jebkur citur var varēs viegli izmantot šo komponentu.
//    Piemēram, varat izveidot OmdbGateway klasi un tur būs restTemplate kā dependency. Šai klasei būs
//    divas metodes:
//           List<OMDbMovie> searchBy(String title);
//           OMDbMovie getBy(String id);
//    šajā klasē vajadzētu būt tikai loģikai, kas saistīta ar datu iegūšanu no Omdb (taisam URL, veicak request un viss).
//    Ja pēc tam vajag kaut ko manipulāt vai apstrādāt šos datus, tad to data servisā.
// 2. Iedomāsimies sekojošo situāciju:
//         1) lietotājs-1 atrod filmu x, saraksts tiek iztīrīts un filma x tiek saglabāta found sarakstā
//         2) lietotājs-2 atrod filmu y, saraksts tiek iztīrīts un filma y tiek saglabāta found sarakstā
//         3) lietotājs-1 spiež save, lai saglaātu filmu x, bet datubāzē tiek saglabāta filma y
//             (jo sarakstā "found" ir filma y)
//       Kā risināt? Vienkāršības pēc pieņemsim, ka filmu saturs OMDB ir nemainīgs.
//       Kad lietotājs meklē filmu, vienkārši atgriežam datus (tai skaitā arī ID) uz frontendu.
//       Pēc tam, kad lietotājs spiež save, tad redzu divu variantus:
//              1) vai nu sūtam sarakstu tikai ar ID un atkal meklējam filmas Omdb un tad saglabājam
//              2) Var sūtīt visus vajadzīgos datus, lai nevajag atkal iet uz Omdb. Šis variants
//                 nodrošinās arī to, ka lietotājs patiešām saglabās to, ko bija domājis saglabāt
//                 (potenciāli var gadīties, ka dati mainās pa to brīdi, kamēr lietotājs skatījās savu
//                 meklēšānas rezultātu un spieda save)
@Service
@AllArgsConstructor
//TODO klases nosaukumam vajadzētu būt OmdbService (vairāki lielie burti pēc kārtas nevajadzētu būt)
// tas pats attiecās uz visām klasēm
public class OMDbService {
    private static final String SEARCH_URL = "http://www.omdbapi.com/?s=TITLE&type=movie&apikey=APIKEY";
    private static final String SEARCH_BY_IMDB_URL = "http://www.omdbapi.com/?i=IMDB&apikey=APIKEY";
    private static final String API_KEY = "5ca97d27";
    private final RestTemplate restTemplate;
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;
    private final List<OMDbMovie> found = new ArrayList<>();

    List<OMDbMovie> findMovies(String title) {
        found.clear();

        //TODO lūdzu izmantot UriComponentsBuilder klasi, lai pareizi veidotu URL
        String requestURL = SEARCH_URL
                //TODO nekur kodā nevajadzētu būt literāļiem. Šādas lietas vienmēr būtu jāiznes
                // kā konstantes (private static final String...). Un tad ja kādreiz vajag rename,
                // var pamainīt vienā vietā, nevis meklēt visur pa kodu. :)
                .replaceAll("TITLE", title)
                .replaceAll("APIKEY", API_KEY);
        OMDbMoviesList response = restTemplate.getForObject(requestURL, OMDbMoviesList.class);

        if (response != null) {
            List<OMDbMovie> movies = response.getOMDbMovies();
            for (OMDbMovie movie : movies) {
                //TODO lūdzu izmantot UriComponentsBuilder klasi, lai pareizi veidotu URL.
                requestURL = SEARCH_BY_IMDB_URL
                        .replaceAll("IMDB", movie.getId())
                        .replaceAll("APIKEY", API_KEY);
                ; //TODO lieks semikolons?
                found.add(restTemplate.getForObject(requestURL, OMDbMovie.class));
            }
        }

        return found;
    }

    List<OMDbMovie> saveMovie(String id) {
        for (OMDbMovie movie : found) {
            if (id.equals(movie.getId())) {
                Movie m = new Movie();
                m.setName(movie.getName());
                m.setDescription(movie.getDescription());
                m.setYear(movie.getYear());
                m.setPictureURL(movie.getPictureURL());

                String categories = movie.getCategories();
                String[] genres = categories.split(", ");
                for (String genre : genres) {
                    Optional<Category> optional = categoryRepository.findByName(genre);
                    optional.ifPresent(m::addCategory);
                }

                movieRepository.save(m);
                found.remove(movie);
                break;
            }
        }
        return found;
    }
}
