package com.axway.runners;

import com.axway.runners.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManagementController {

    @Autowired
    private EventService eventService;

    @PostMapping("/management/events")
    public ResponseEntity<?> createEvent(@RequestHeader("Host") String host , @RequestBody Event event){

            if(host.contains("localhost")){
                eventService.saveEvent(event);
                return new ResponseEntity<Event>(HttpStatus.CREATED);
            }


        return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);

    }
    @GetMapping("/management/events")
    public Iterable<Event> listEvents(OAuth2AuthenticationToken authToken) {
//        //  ..logger.info(authToken.toString());
//        logger.info(authToken.getAuthorities().toString());
//        logger.info(authToken.getPrincipal().getAttributes().toString());

//        Event event = new Event();
//        event.setName("Axway Run");
//        event.setDescription("Axway run");
        // eventService.saveEvent(event);

        Iterable<Event> events = eventService.findAll();
        return events;
        // logger.info();
        // return "Hello " + authToken.getPrincipal().getName() + "Your email id is : " + authToken.getPrincipal().getAttributes().get("unique_name") + "and accessing from " + authToken.getPrincipal().getAttributes().get("ctry");
    }

    @PutMapping ("/management/events/{id}")
    public ResponseEntity<?> updateEvent(@RequestHeader("Host") String host , @RequestBody Event event, @PathVariable String id){

        if(host.contains("localhost")){

            Event existingEvent = eventService.findById(id);
            existingEvent.setVersion(System.currentTimeMillis());
            existingEvent.setStartDate(event.getStartDate());
            existingEvent.setEndDate(event.getEndDate());
            Event updatedEvent = eventService.saveEvent(existingEvent);
            return new ResponseEntity<Event>(updatedEvent, HttpStatus.OK);
        }

        return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);

    }

    @DeleteMapping  ("/management/events")
    public ResponseEntity<?> deleteEvent(@RequestHeader("Host") String host ){

        if(host.contains("localhost")){

            eventService.deleteAll();
            return new ResponseEntity<Event>( HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);

    }
}
