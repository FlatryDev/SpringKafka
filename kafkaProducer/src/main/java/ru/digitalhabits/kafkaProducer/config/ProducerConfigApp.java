package ru.digitalhabits.kafkaProducer.config;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
@ConfigurationProperties(prefix = "producer")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProducerConfigApp {

    private String kafkaServer;
    private String kafkaTopicName;
    @Autowired
    private KafkaAdmin kafkaAdmin ;


    //@Bean
    public NewTopic createMessageTopic() {
        return TopicBuilder.name(kafkaTopicName+".message")
                .replicas(1)
                .partitions(10)
                .build();
    }


    //@Bean
    public NewTopic createBookingTopic() {
        return TopicBuilder.name(kafkaTopicName+".booking")
                .replicas(1)
                .partitions(10)
                .build();
    }

    @PostConstruct
    public void createTop() {
        System.out.println("kafkaAdmin = "+ kafkaAdmin.getClass());
        kafkaAdmin.createOrModifyTopics(createBookingTopic(), createMessageTopic());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        HashMap<String, Object> config = new HashMap();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<String, String>(producerFactory);
    }

    @Bean(name = "kafkaTopicName")
    public String getKafkaTopicName() {
        return kafkaTopicName;
    }

}
