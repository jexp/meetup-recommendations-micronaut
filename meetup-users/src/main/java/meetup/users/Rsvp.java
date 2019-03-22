package meetup.users;

import java.util.List;

public class Rsvp {
   public Member member;
}
class Member {
   public long member_id;
   public String photo;
   public String member_name;
   public Member(long member_id,String member_name, String photo) {
	 this.member_id = member_id;
	 this.member_name = member_name;
	 this.photo = photo;
   }
   public Member() {}
}