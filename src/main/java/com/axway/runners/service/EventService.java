package com.axway.runners.service;

import com.axway.runners.model.Event;
import com.axway.runners.repo.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public Event saveEvent(Event event){
        return eventRepository.save(event);
    }



    public Event findById(String id){
        Optional<Event> event = eventRepository.findById(id);
        return event.orElse(null);
    }

    public Iterable<Event> findAll(){
        return eventRepository.findAll();
    }

    public void deleteAll(){
        eventRepository.deleteAll();
    }

    public Event findByFeedsUsingCustomQuery(String activityId){
        return eventRepository.findByFeedsUsingCustomQuery(activityId);
    }
}
