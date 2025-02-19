package br.com.devdreams.events.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdreams.events.model.Event;
import br.com.devdreams.events.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class EventController {
    
    @Autowired
    private EventService service;

    @PostMapping("/events")
    public Event addNewEvent(@RequestBody Event newEvent) {
        return service.addNewEvent(newEvent);
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return service.getAllEvents();

    }

    @GetMapping("/events/id/{id}")
    public Event getEventById(@PathVariable Integer id) {
        return service.getEventByEventId(id);
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName) {
        Event evt = service.getPrettyName(prettyName);

        if(evt != null) {
            return ResponseEntity.ok().body(evt);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    


}
