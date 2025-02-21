package br.com.devdreams.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.devdreams.events.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
    
    public Event findByPrettyName(String prettyName);

    public Event findByEventId(Integer eventId);


}
