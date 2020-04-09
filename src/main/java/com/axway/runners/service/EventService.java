package com.axway.runners.service;

import com.axway.runners.Event;
import com.axway.runners.repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;

    // @Autowired
    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public void saveEvent(Event event){
        eventRepository.save(event);
    }

    public Iterable<Event> findAll(){
//        Iterable<Event> events = eventRepository.findAll();
//        for(Event event : events){
//            System.out.println(event.getName());
//        }
//        return  null;
        Iterable<Event> events = eventRepository.findAll();
        return events;
    }
}
