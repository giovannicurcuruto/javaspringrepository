package br.com.devdreams.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.devdreams.events.dto.SubscriptionResponse;
import br.com.devdreams.events.exception.EventNotFoundException;
import br.com.devdreams.events.exception.SubscriptonConflictException;
import br.com.devdreams.events.exception.UserIndicatorNotFoundException;
import br.com.devdreams.events.model.Event;
import br.com.devdreams.events.model.Subscription;
import br.com.devdreams.events.model.User;
import br.com.devdreams.events.repository.EventRepository;
import br.com.devdreams.events.repository.SubscriptionRepository;
import br.com.devdreams.events.repository.UserRepository;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer user_id) {

        Subscription subs = new Subscription();

        Event event = eventRepository.findByPrettyName(eventName);

        if (event == null) {
            throw new EventNotFoundException("Evento " + eventName + " não encontrado");
        }

        User userRec = userRepository.findByEmail(user.getEmail());

        if (userRec == null) {
            userRec = userRepository.save(user);
        }

        User indicador = userRepository.findById(user_id).orElse(null);
        if( indicador == null){
            throw new UserIndicatorNotFoundException("Usuário "+user_id+" indicador não encontrado");
        }

        // user = userRepository.save(user);

        subs.setEvent(event);
        subs.setSubscriber(userRec);
        subs.setIndication(indicador);

        Subscription tmpSub = subscriptionRepository.findByEventAndSubscriber(event, userRec);

        if (tmpSub != null) {
            throw new SubscriptonConflictException(
                    userRec.getName() + " já está inscrito no evento: " + event.getTitle());
        }

        Subscription res = subscriptionRepository.save(subs);

        return new SubscriptionResponse(res.getSubscriptionNumber(),
                "http://codeCraft.com/inscricao/" + res.getEvent().getPrettyName() + '/' + res.getSubscriber().getId());

    }

}
