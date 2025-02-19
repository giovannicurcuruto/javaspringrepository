package br.com.devdreams.events.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.devdreams.events.model.Event;
import br.com.devdreams.events.repository.EventRepository;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;

    public Event addNewEvent(Event event) {
        //gerando o prettyName

        event.setPrettyName(event.getTitle().toLowerCase().replace(" ", "-"));
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents(){
        return (List<Event>) eventRepository.findAll();
    }

    public Event getPrettyName(String prettyName) {
        return eventRepository.findByPrettyName(prettyName);
    }

    public Event getEventByEventId(Integer eventId) {
        return eventRepository.findByEventId(eventId);
    }

}
