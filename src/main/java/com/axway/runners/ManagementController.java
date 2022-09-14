package com.axway.runners;

import com.axway.runners.model.Event;
import com.axway.runners.model.UnMatchedEventFeed;
import com.axway.runners.service.EventService;
import com.axway.runners.service.ParticipantService;
import com.axway.runners.service.UnMatchedEventFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManagementController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private UnMatchedEventFeedService unMatchedEventFeedService;

    @PostMapping("/management/events")
    public ResponseEntity<?> createEvent(@RequestHeader("Host") String host, @RequestBody Event event) {

        if (host.contains("localhost")) {
            eventService.saveEvent(event);
            return new ResponseEntity<Event>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
    }

    @GetMapping("/management/events")
    public Iterable<Event> listEvents() {
        Iterable<Event> events = eventService.findAll();
        return events;
    }

    @GetMapping("/management/events/participant")
    public Iterable<Event> getParticipant() {
        Iterable<Event> events = eventService.findAll();
        return events;
    }

    @PutMapping("/management/events/{id}")
    public ResponseEntity<?> updateEvent(@RequestHeader("Host") String host, @RequestBody Event event, @PathVariable String id) {

        if (host.contains("localhost")) {
            Event existingEvent = eventService.findById(id);
            existingEvent.setVersion(System.currentTimeMillis());
            existingEvent.setStartDate(event.getStartDate());
            existingEvent.setEndDate(event.getEndDate());
            existingEvent.setName(event.getName());
            existingEvent.setImage(event.getImage());
            existingEvent.setDescription(event.getDescription());
            Event updatedEvent = eventService.saveEvent(existingEvent);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        }
        return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
    }

    @DeleteMapping("/management/participants")
    public ResponseEntity<?> deleteParticipants(@RequestHeader("Host") String host) {

        if (host.contains("localhost")) {
            participantService.deleteAll();
            return new ResponseEntity<Event>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
    }

    @DeleteMapping("/management/events")
    public ResponseEntity<?> deleteEvent(@RequestHeader("Host") String host) {
        if (host.contains("localhost")) {
            eventService.deleteAll();
            return new ResponseEntity<Event>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
    }

    @DeleteMapping("/management/feeds/{id}")
    public ResponseEntity<?> deleteFeed(@RequestHeader("Host") String host, @PathVariable String id) {
        if (host.contains("localhost")) {
            unMatchedEventFeedService.deleteById(id);
            return new ResponseEntity<UnMatchedEventFeed>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<UnMatchedEventFeed>(HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("/management/feeds")
    public ResponseEntity<?> addFeed(@RequestHeader("Host") String host, @RequestBody  UnMatchedEventFeed unMatchedEventFeed) {
        if (host.contains("localhost")) {
            unMatchedEventFeedService.save(unMatchedEventFeed);
            return new ResponseEntity<UnMatchedEventFeed>(HttpStatus.CREATED);
        }
        return new ResponseEntity<UnMatchedEventFeed>(HttpStatus.EXPECTATION_FAILED);
    }
}
