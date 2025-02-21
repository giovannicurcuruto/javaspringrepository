package br.com.devdreams.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdreams.events.dto.ErrorMessage;
import br.com.devdreams.events.dto.SubscriptionResponse;
import br.com.devdreams.events.exception.EventNotFoundException;
import br.com.devdreams.events.exception.SubscriptonConflictException;
import br.com.devdreams.events.exception.UserIndicatorNotFoundException;
import br.com.devdreams.events.model.Event;
import br.com.devdreams.events.model.User;
import br.com.devdreams.events.service.SubscriptionService;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    @PostMapping({ "/subscription/{prettyName}", "/subscription/{prettyName}/{user_indication_id}" })
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName, @RequestBody User subscriber,
            @PathVariable(required = false) Integer user_indication_id) {
        try {
            SubscriptionResponse res = service.createNewSubscription(prettyName, subscriber, user_indication_id);
            if (res != null) {
                return ResponseEntity.ok().body(res);
            }

        } catch (EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        } catch (SubscriptonConflictException e) {
            return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));
        } catch (UserIndicatorNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
        return ResponseEntity.badRequest().build();

    }

    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> generateRackingByEvent(@PathVariable String prettyName) {
        try {
            return ResponseEntity.ok().body(service.getCompleteRanking(prettyName).subList(0, 3));
        } catch (EventNotFoundException e) {
            return ResponseEntity.status((404)).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String prettyName,
            @PathVariable Integer userId) {

        try {
            return ResponseEntity.ok(service.getRankingByUser(prettyName, userId));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }


    }
}
