package meetup.rsvp;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.micronaut.retry.annotation.*;
import reactor.core.publisher.*;

@Client("${app.meetup-url}")
@CircuitBreaker(reset = "30s")
public interface RsvpClient {

    @Get("/rsvps")
    public Flux<Rsvp> index();
}
