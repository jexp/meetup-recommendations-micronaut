package meetup.users;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.reactiverse.reactivex.pgclient.*;
import javax.inject.Inject;
import io.reactivex.*;
import java.util.stream.*;
import java.util.*;

@Controller("/user")
public class UserController {

    @Inject PgPool client;

// TODO make this all async
    @Get("/")
    public List<String> index() {
        return client.rxQuery("SELECT * FROM members").map(pgRowSet -> {
		    List<String> names = new ArrayList();
		    PgIterator iterator = pgRowSet.iterator();
		    while (iterator.hasNext()) {
		        Row row = iterator.next();
		        System.out.println(row.getClass().getName());
		        names.add(row.getString("member_name"));
		    }
		    return names;
		}).blockingGet();
    }
}
/*
client.preparedQuery("INSERT INTO members VALUES ($1,$2,$3)", Tuple.of(user.member_id, user.member_name, user.photo), ar -> {
  if (ar.succeeded()) {
    PgRowSet rows = ar.result();
    System.out.println(rows.rowCount());
  } else {
    System.out.println("Failure: " + ar.cause().getMessage());
  }
});

create table members (
   member_id integer,
   member_name varchar,
   photo varchar
);

create table member_groups (
   member_id integer,
   group_id integer
);

select * from members;

*/