// curl -s http://localhost:8081/rsvp/ | jq .group

package meetup.rsvp;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;

@Controller("/rsvp")
public class RsvpController {

    private final RsvpClient client;

    public RsvpController(RsvpClient client) {
        this.client = client;
    }

    @Get(value = "/", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flux<Rsvp> index() {
        return Flux.from(client.index()).filter(r -> r.response.equalsIgnoreCase("yes"));
    }
}