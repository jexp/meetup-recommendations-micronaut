package meetup.users;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.reactiverse.reactivex.pgclient.*;
import javax.inject.Inject;
import io.reactivex.*;
import java.util.stream.*;
import java.util.*;
import reactor.core.publisher.*;

@Controller("/user")
public class UserController {

    @Inject PgPool client;

    @Get("/{id}")
    public Single<Member> member(Integer id) {
        return client.rxBegin().flatMap(tx ->
                tx.rxPreparedQuery("SELECT * FROM members WHERE member_id = $1 LIMIT 1", Tuple.of(id)).map(
                        result -> {
                            PgIterator it = result.iterator();
                            if (!it.hasNext()) return null;
                            Row row = it.next();
                            return new Member(row.getLong("member_id"), row.getString("member_name"), row.getString("photo"));
                        }));
    }

/*
return pool.rxBegin()
     .flatMapPublisher(tx -> tx.rxPrepare(sql)
       .flatMapPublisher(preparedQuery -> {
         // Fetch 50 rows at a time
         PgStream<io.reactiverse.reactivex.pgclient.Row> stream = preparedQuery.createStream(50, Tuple.tuple());
         return stream.toFlowable();
       })
       .doAfterTerminate(tx::commit));
 }
*/

   @Post("/{id}/{name}")
   public Single<Integer> insert(Integer id, String name) {
	   return client.rxBegin()
	     .flatMap(tx -> 
	      tx.rxPreparedQuery("INSERT INTO members VALUES ($1,$2,$3)",Tuple.of(id, name, null)).map(PgResult::rowCount)
		  .doAfterTerminate(tx::commit)
		);
   }
}
/*

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