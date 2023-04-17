package ru.digitalhabits.kafkaConsumer.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ConsumerService.class);
    //@Value("${consumer.kafkaTopicName}")
    //private final String kafkaTopicName;

    @KafkaListener(topics = "${consumer.kafkaTopicName}")
    public void consume(String message) {
        log.info("Receive new message: {} ",message);
    }
    @KafkaListener(topics = {"${consumer.kafkaTopicName}"+".message","${consumer.kafkaTopicName}"+".booking"}
                  ,topicPartitions = @TopicPartition(topic = "${consumer.kafkaTopicName}"+".message", partitions = {"0","1", "3"}))
    public void consumeMeesageBooking(@Payload String message,
                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String receivedTopic,
                                      @Header(KafkaHeaders.RECEIVED_PARTITION) String receivedPartition,
                                      @Header(KafkaHeaders.OFFSET) String receivedOffset
                                      ) {
        log.info("Receive from topic {} by partition {} with offset {} new message: {} ",receivedTopic,receivedPartition, receivedOffset,message);
    }

}
