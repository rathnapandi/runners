package com.axway.runners;

import com.axway.runners.service.EventService;
import com.axway.runners.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {


    @Autowired
    private EventService eventService;

    @Autowired
    private ParticipantService participantService;

    private Logger logger = LoggerFactory.getLogger(EventController.class);

    //@Autowired
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/{id}", produces = "application/json")
    public Event getEvents(@PathVariable String id,  OAuth2AuthenticationToken authToken) {
        return eventService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping (value = "/{id}/participant", produces = "application/json")
    public Participant addParticipant(@PathVariable String id, @RequestBody Participant participant, OAuth2AuthenticationToken authToken) {
        return participantService.saveParticipant(participant);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping (value = "/{id}/participant/{participantId}", produces = "application/json")
    public ResponseEntity<?> updateParticipant(@PathVariable String id, @PathVariable String participantId, @RequestBody Participant participant, OAuth2AuthenticationToken authToken) {

       Participant existingParticipant =  participantService.findById(participantId);
       if( existingParticipant == null){
           return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
       }
       existingParticipant.setEndTime(participant.getEndTime());
       existingParticipant.setStartTime(participant.getStartTime());
       existingParticipant.setVersion(System.currentTimeMillis());
       return new ResponseEntity<Participant>(participantService.saveParticipant(participant), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping (value = "/{id}/participant/{participantId}", produces = "application/json")
    public ResponseEntity<?> deleteParticipant(@PathVariable String id, @PathVariable String participantId,  OAuth2AuthenticationToken authToken) {

        Participant existingParticipant =  participantService.findById(participantId);
        if( existingParticipant == null){
            return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
        }
        participantService.deleteParticipant(existingParticipant);
        return new ResponseEntity<Participant>( HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping (value = "/{id}/feed", produces = "application/json")
    public Event addFeed(@PathVariable String id, @RequestBody Feed feed, OAuth2AuthenticationToken authToken) {
        Event event = eventService.findById(id);
        event.addFeed(feed);
        return eventService.saveEvent(event);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Iterable<Event> listEvents(OAuth2AuthenticationToken authToken) {

        Iterable<Event> events = eventService.findAll();
        return events;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping()
    public Event addEvent(@RequestBody Event event, OAuth2AuthenticationToken authToken) {
        return eventService.saveEvent(event);
    }
}
