package meetup.groups;

import java.util.List;

public class Rsvp {
   public Venue venue;
   public String visibility;
   public String response;
   public int guests;
   public String rsvp_id;
   public Long mtime;
   public Event event;
   public Member member;
   public Group group;
}
 class Venue {
  public String venue_name;
  public Double lon;
  public Double lat;
  public long venue_id;
}
 class Member {
   public long member_id;
   public String photo;
   public String member_name;
}
 class Event {
   public String event_name;
   public String event_id;
   public Long time;
   public String event_url;
}
class Group {
List<GroupTopic> group_topics;
public String group_city;
public String group_country;
public String group_name;
public long group_id;
public Double group_lon;
public Double group_lat;
public String group_urlname;
public String group_state;
}
class GroupTopic {
   public String topic_name;
   public String urlkey;
}
