package com.nelson.greg;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Hello World!");

        KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.getProperties());

        ProducerRecord<String, String> record = new ProducerRecord<>("first-topic", "key", "value");

        // send the data - aschronously
        producer.send(record);

        // flush the data - synchronously
        producer.flush();

        producer.close();
    }
}
