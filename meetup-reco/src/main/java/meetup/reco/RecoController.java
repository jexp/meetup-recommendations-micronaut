package meetup.reco;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import org.neo4j.driver.v1.*;
import reactor.core.publisher.*;
import javax.inject.Inject;

@Controller("/reco")
public class RecoController {

    @Inject Driver driver;

	private final static String RECO_STATEMENT = 
		"MATCH (m:Member {id:$id})-[:ATTENDS]->()<-[:ATTENDS]-(m2:Member) "+
		"WITH m, m2, count(*) as sameEvent ORDER BY sameEvent DESC LIMIT 20 "+
		"MATCH (m2)-[:ATTENDS]->(e:Event) "+
		"WHERE e.time > timestamp() AND NOT (m)-[:ATTENDS]->(e) "+
		"RETURN e, count(*) as eventFreq "+
		"ORDER BY eventFreq DESC LIMIT 10; ";
	
    @Get("/{user}")
    public Flux<Event> recommend(Integer user) {
        return Flux.fromStream(driver.session().run(RECO_STATEMENT, Values.parameters("id",user)).list().stream().map(this::event));
    }
    @Get("/events")
    public Flux<Event> events() {
	    String query = "MATCH (e:Event) RETURN e LIMIT $limit";
        return Flux.fromStream(driver.session().run(query, Values.parameters("limit",100)).list().stream().map(this::event));
    }

    private Event event(Record r) {
	    Value e = r.get("e");
		return new Event(e.get("id").asString(), e.get("name").asString(), e.get("time").asLong());
    }
}