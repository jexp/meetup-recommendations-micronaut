package test;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.reactivex.Single;

@Controller("/hello")
public class HelloController {

	@Get ("/echo/{text}")
	public Single<String> echo(String text) {
	    return Single.just(">" + text);
	}
}