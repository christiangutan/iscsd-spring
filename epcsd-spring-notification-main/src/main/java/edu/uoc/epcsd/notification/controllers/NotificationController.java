package edu.uoc.epcsd.notification.controllers;

import edu.uoc.epcsd.notification.pojos.Show;
import edu.uoc.epcsd.notification.services.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Log4j2
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/{id}")
    public ResponseEntity sendShowCreated(@PathVariable Long id) {
        log.trace("Send Show Created");

        Show show = new RestTemplate().getForObject("http://localhost:18081/show/{id}", Show.class, id);

        if (show == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            notificationService.notifyShowCreation(show);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
