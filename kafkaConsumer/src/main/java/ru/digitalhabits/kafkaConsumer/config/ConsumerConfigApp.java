package ru.digitalhabits.kafkaConsumer.config;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.HashMap;

@EnableKafka
@Configuration
@ConfigurationProperties(prefix = "consumer")
@Getter
@Setter
public class ConsumerConfigApp {
    //@Value("${producer.kafka.server}")
    private String kafkaServer;
    private String kafkaTopicName;
    private String groupId;

//    @Bean
//    public NewTopic createTopic() {
//        return TopicBuilder.name(kafkaTopicName)
//                .replicas(1)
//                .partitions(10)
//                .build();
//    }

    @Bean
    public ConsumerFactory<String, String> producerFactory() {
        HashMap<String, Object> config = new HashMap();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        val factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean(name = "kafkaTopicName")
    public String getKafkaTopicName() {
        return kafkaTopicName;
    }

}
