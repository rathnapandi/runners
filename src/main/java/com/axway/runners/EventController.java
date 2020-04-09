package com.axway.runners;

import com.axway.runners.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {


    @Autowired
    private EventService eventService;




    private Logger logger = LoggerFactory.getLogger(EventController.class);

    //@Autowired
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/api/events/{id}")
    public String getEvents(@PathVariable String id,  OAuth2AuthenticationToken authToken) {
        //  ..logger.info(authToken.toString());
        logger.info(authToken.getAuthorities().toString());
        logger.info(authToken.getPrincipal().getAttributes().toString());
        // logger.info();
        return "Hello " + authToken.getPrincipal().getName() + "Your email id is : " + authToken.getPrincipal().getAttributes().get("unique_name") + "and accessing from " + authToken.getPrincipal().getAttributes().get("ctry");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/api/events")
    public Iterable<Event> listEvents(OAuth2AuthenticationToken authToken) {
        //  ..logger.info(authToken.toString());
        logger.info(authToken.getAuthorities().toString());
        logger.info(authToken.getPrincipal().getAttributes().toString());

        Event event = new Event();
        event.setName("Axway Run");
        event.setDescription("Axway run");
       // eventService.saveEvent(event);

        Iterable<Event> events = eventService.findAll();
        return events;
        // logger.info();
       // return "Hello " + authToken.getPrincipal().getName() + "Your email id is : " + authToken.getPrincipal().getAttributes().get("unique_name") + "and accessing from " + authToken.getPrincipal().getAttributes().get("ctry");
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping("/api/events")
//    public String addEvent(OAuth2AuthenticationToken authToken) {
//        //  ..logger.info(authToken.toString());
//        logger.info(authToken.getAuthorities().toString());
//        logger.info(authToken.getPrincipal().getAttributes().toString());
//        // logger.info();
//        return "Hello " + authToken.getPrincipal().getName() + "Your email id is : " + authToken.getPrincipal().getAttributes().get("unique_name") + "and accessing from " + authToken.getPrincipal().getAttributes().get("ctry");
//    }
}
