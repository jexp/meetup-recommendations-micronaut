package meetup.reco;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.reactivex.*;

@Client("meetup-users")
public interface UserClient {

    @Get("/user/{id}")
    public Single<Member> member(Integer id);
}