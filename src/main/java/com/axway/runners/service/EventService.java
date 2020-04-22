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

    public Event saveEvent(Event event){
        return eventRepository.save(event);
    }



    public Event findById(String id){
        return eventRepository.findById(id).get();
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

    public void deleteAll(){
        eventRepository.deleteAll();
    }

    public Event findByFeedsUsingCustomQuery(String activityId){
        return eventRepository.findByFeedsUsingCustomQuery(activityId);
    }
}
