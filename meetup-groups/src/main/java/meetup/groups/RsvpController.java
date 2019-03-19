package meetup.groups;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.reactivex.*;
import java.util.stream.*;
import java.util.*;


@Controller("/rsvp")
public class RsvpController {

	    private final MongoClient mongoClient;

	    public RsvpController(MongoClient mongoClient) {
	        this.mongoClient = mongoClient;
	    }

	    @Post("/groups")
	    public Single<Group> save(long id, String name, List<String> topics) {
	        Group group = new Group(id,name);
            group.group_topics = topics.stream().map(GroupTopic::new).collect(Collectors.toList());
	        return Single
	                .fromPublisher(getCollection().insertOne(group))
	                .map(success -> group);
	    }

	    @Get("/groups")
	    public Flowable<Group> list() {
	        return Flowable.fromPublisher(getCollection().find());
	    }

	    private MongoCollection<Group> getCollection() {
	        return mongoClient
	                .getDatabase("micronaut")
	                .getCollection("groups", Group.class);
	    }
	}
