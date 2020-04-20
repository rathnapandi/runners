package com.axway.runners.ical;

import com.axway.runners.Participant;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.GregorianCalendar;

@Service
public class MSCalendarEvent {
    private TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();

    public String createEvent(Participant participant){

        TimeZone timezone = registry.getTimeZone("America/Mexico_City");
        VTimeZone tz = timezone.getVTimeZone();

        // Start Date is on: April 1, 2008, 9:00 am
        java.util.Calendar startDate = new GregorianCalendar();
        startDate.setTimeZone(timezone);
        startDate.setTimeInMillis(Long.parseLong(participant.getStartTime()));


        // End Date is on: April 1, 2008, 13:00
        java.util.Calendar endDate = new GregorianCalendar();
        endDate.setTimeZone(timezone);
        endDate.setTimeInMillis(Long.parseLong(participant.getEndTime()));


// Create the event
        String eventName = participant.getEventName();
        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent meeting = new VEvent(start, end, eventName);

// add timezone info..
        meeting.getProperties().add(tz.getTimeZoneId());

// generate unique identifier..
        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();
        meeting.getProperties().add(uid);

// add attendees..
        Attendee dev1 = new Attendee(URI.create(participant.getEmail()));
        dev1.getParameters().add(Role.REQ_PARTICIPANT);
        dev1.getParameters().add(new Cn("Participant 1"));
        meeting.getProperties().add(dev1);



// Create a calendar
        net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(CalScale.GREGORIAN);


// Add the event and print
        icsCalendar.getComponents().add(meeting);
        return icsCalendar.toString();
    }
}
