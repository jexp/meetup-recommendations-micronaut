package meetup.rsvp;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.micronaut.retry.annotation.*;
import reactor.core.publisher.*;

@Client("https://stream.meetup.com/2/")
@CircuitBreaker(reset = "30s")
public interface RsvpClient {

    @Get("/rsvps")
    public Flux<Rsvp> index();
}