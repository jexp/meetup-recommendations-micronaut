package meetup.groups;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import com.mongodb.reactivestreams.client.*;
import reactor.core.publisher.*;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class GroupListener {
	
    private final MongoClient mongoClient;

    public GroupListener(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Topic("rsvps")
    public Mono<Success> receiveRsvp(@KafkaKey String id, Mono<Rsvp> rsvp) {
        return rsvp.flatMap(r -> Flux.from(getCollection().insertOne(r.group)).next());
    }

    private MongoCollection<Group> getCollection() {
        return mongoClient
                .getDatabase("micronaut")
                .getCollection("groups", Group.class);
    }
}