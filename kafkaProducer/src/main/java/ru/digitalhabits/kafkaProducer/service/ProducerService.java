package ru.digitalhabits.kafkaProducer.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.digitalhabits.kafkaProducer.config.ProducerConfigApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProducerService {

    KafkaTemplate<String, String> kafkaTemplate;
    final String kafkaTopicName;

    private static final Random ra = new Random();
    private static final ArrayList<String> KEYS = new ArrayList<>(){{
        add("FIRST");
        add("SECOND");
        add("THIRD");
        add("FOURTH");
        add("FIFTH");
    }};

    public boolean send(String message, String topicName, Integer partition) {

        var key = KEYS.get(ra.nextInt(KEYS.size()));
        //var future = kafkaTemplate.send(kafkaTopicName, 2, key, message);
        var future = kafkaTemplate.send(kafkaTopicName+'.'+topicName, (partition == null?0:partition), key, message);
        try{
            var result = future.get();
            log.info("Send message success! Into topic {} , partition {}, with offset {}, key {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    result.getProducerRecord().key());
            return true;
        }
        catch(Exception e) {
            log.error("Cannot set message to topic {}. Error: {}", kafkaTopicName,e.getMessage());
            return false;
        }

    }
}
