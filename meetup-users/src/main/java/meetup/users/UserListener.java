package meetup.users;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.reactiverse.reactivex.pgclient.*;
import javax.inject.Inject;
import io.reactivex.*;
import java.util.stream.*;
import java.util.*;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class UserListener {
	
    @Inject PgPool client;
	
    @Topic("rsvps")
    public Single<Integer> receiveRsvp(@KafkaKey String id, Single<Rsvp> event) {
	   return event.map(rsvp -> rsvp.member).flatMap( m ->
         client.rxBegin()
	     .flatMap(tx -> 
	      tx.rxPreparedQuery("INSERT INTO members VALUES ($1,$2,$3)",Tuple.of((int)m.member_id, m.member_name, m.photo)).map(r -> r.rowCount())
	      .doAfterTerminate(tx::commit)
		));
    }
}