package ru.digitalhabits.kafkaProducer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Message implements Serializable {
    private String messageText;
}
