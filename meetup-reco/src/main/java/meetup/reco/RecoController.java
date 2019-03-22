package meetup.reco;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import org.neo4j.driver.v1.*;
import reactor.core.publisher.*;
import javax.inject.Inject;
import io.reactivex.*;
import io.micronaut.tracing.annotation.*;

@Controller("/reco")
public class RecoController {

    @Inject Driver driver;
    @Inject UserClient users;

	private final static String RECO_STATEMENT = 
		"MATCH (m:Member {id:$id})-[:ATTENDS]->()<-[:ATTENDS]-(m2:Member) "+
		"WITH m, m2, count(*) as sameEvent ORDER BY sameEvent DESC LIMIT 20 "+
		"MATCH (m2)-[:ATTENDS]->(e:Event) "+
		"WHERE e.time > timestamp() AND NOT (m)-[:ATTENDS]->(e) "+
		"RETURN e, count(*) as eventFreq "+
		"ORDER BY eventFreq DESC LIMIT 10; ";
	
    @Get("/{user}")
    @NewSpan("meetup.reco")
    public Flowable<Event> recommend(@SpanTag("user.id") Integer user) {
	    return
	    users.member(user).flatMapPublisher(m -> 
	      Flowable.fromIterable(driver.session().run(RECO_STATEMENT, Values.parameters("id",user)).list()).map(this::event).map(e -> e.withMember(m)));
    }

    @Get("/events")
    public Flowable<Event> events() {
	    String query = "MATCH (e:Event) RETURN e LIMIT $limit";
        return Flowable.fromIterable(driver.session().run(query, Values.parameters("limit",100)).list()).map(this::event);
    }

    private Event event(Record r) {
	    Value e = r.get("e");
		return new Event(e.get("id").asString(), e.get("name").asString(), e.get("time").asLong());
    }
}