package meetup.rsvp;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface RsvpSource {

    @Topic("rsvps")
    void send(@KafkaKey String id, Rsvp rsvp);
}
