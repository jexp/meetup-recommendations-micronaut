// mn create-job rsvp
package meetup.rsvp;

import javax.inject.Singleton;
import io.micronaut.scheduling.annotation.Scheduled;
import javax.inject.Inject;
import reactor.core.publisher.*;

@Singleton
public class RsvpJob {

    @Inject RsvpClient client;
	@Inject RsvpSource source;
	
    @Scheduled(fixedRate = "5s")
    public void process() {
	   client.index()
	   .subscribe(r -> source.send(r.rsvp_id, r));
    }
}