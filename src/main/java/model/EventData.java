package model;

import java.util.List;

/**
 * Created by inafalcao on 22/02/17.
 */
public class EventData {
    public Event event;
    public List<Session> sessions;
    public String timeZoneId;

    public EventData(Event event, List<Session> sessions, String timezone) {
        this.event = event;
        this.sessions = sessions;
        this.timeZoneId = timezone;
    }
}
