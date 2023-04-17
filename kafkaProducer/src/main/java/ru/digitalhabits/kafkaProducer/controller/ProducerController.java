package ru.digitalhabits.kafkaProducer.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.digitalhabits.kafkaProducer.model.Message;
import ru.digitalhabits.kafkaProducer.service.ProducerService;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProducerController {

    ProducerService producerService;

    @PostMapping("/send")
    public ResponseEntity<String> SendMessage(@RequestParam(value = "topic") String topicName,
                                              @RequestParam(value = "partition", required = false) Integer partition,
                                              @RequestBody Message message) {

        if (producerService.send(message.getMessageText(),topicName, partition)) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error work kafka");
        }

    }

}
