package br.com.devdreams.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.devdreams.events.model.Event;
import br.com.devdreams.events.model.Subscription;
import br.com.devdreams.events.model.User;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    
    public Subscription findByEventAndSubscriber(Event ev, User us);
    //subscriber
}
