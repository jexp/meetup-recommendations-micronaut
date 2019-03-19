package meetup.reco;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import reactor.core.publisher.*;
import javax.inject.Inject;
import org.neo4j.driver.v1.*;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class RsvpListener {
	
    @Inject Driver driver;

	private static String STATEMENT = 
		"MERGE (e:Event {id:$rsvp.event_id}) ON CREATE SET e.time = $rsvp.time, e.name = $rsvp.event_name "+
		"MERGE (g:Group {id:$rsvp.group_id}) "+
		"MERGE (m:Member  {id:$rsvp.member_id}) "+
		"MERGE (m)-[:MEMBER_OF]->(g) "+
		"MERGE (m)-[:ATTENDS]->(e); ";
	
    @Topic("rsvps")
    public Mono<Integer> receiveRsvp(@KafkaKey String id, Mono<Rsvp> rsvp) {
	    Session s = driver.session();
	
        return rsvp.map(r -> s.writeTransaction(tx -> tx.run(STATEMENT, Values.parameters("rsvp",params(r))).consume().counters().nodesCreated()));
    }
    private Value params(Rsvp r) {
	  return Values.parameters("event_id",r.event.event_id,"event_name",r.event.event_name,"time",r.event.time,"group_id",r.group.group_id,"member_id",r.member.member_id);
    }
}