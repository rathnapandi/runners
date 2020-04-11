package com.axway.runners;

import com.axway.runners.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {


    @Autowired
    private EventService eventService;

    private Logger logger = LoggerFactory.getLogger(EventController.class);

    //@Autowired
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/{id}", produces = "application/json")
    public Event getEvents(@PathVariable String id,  OAuth2AuthenticationToken authToken) {
        return eventService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping (value = "/{id}/participant", produces = "application/json")
    public Event addParticipant(@PathVariable String id, @RequestBody Participant participant, OAuth2AuthenticationToken authToken) {

        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        Event event = eventService.findById(id);
        event.setVersion(System.currentTimeMillis());
        event.addParticipant(participant);
        return eventService.saveEvent(event);
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
