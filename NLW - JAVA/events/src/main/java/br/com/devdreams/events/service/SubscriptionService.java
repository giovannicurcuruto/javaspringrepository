package br.com.devdreams.events.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.devdreams.events.dto.SubscriptionRankingByUser;
import br.com.devdreams.events.dto.SubscriptionRankingItem;
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
        User indicador = null;
        if (user_id != null) {
            indicador = userRepository.findById(user_id).orElse(null);
            if (indicador == null) {
                throw new UserIndicatorNotFoundException("Usuário " + user_id + " indicador não encontrado");
            }
        }

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

    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
        Event event = eventRepository.findByPrettyName(prettyName);
        if (event == null) {
            throw new EventNotFoundException("Ranking do Evento " + prettyName + " não encontrado");
        }

        return subscriptionRepository.generateRanking(event.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);

        SubscriptionRankingItem item = ranking.stream().filter(i -> i.userId().equals(userId)).findFirst().orElse(null);

        if (item == null) {
            throw new UserIndicatorNotFoundException("Inscrição do usuário " + userId + " não encontrado");
        }
        Integer position = IntStream.range(0, ranking.size())
                .filter(pos -> ranking.get(pos).userId().equals(userId))
                .findFirst().getAsInt();
        return new SubscriptionRankingByUser(item, position+1);
    }

}
