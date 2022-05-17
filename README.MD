Go to your home directory

	sudo nano .bashrc	
	
	-- Add
	
	PATH="$PATH:/home/manager/kafka_2.13-3.1.1/bin"

Change "listeners" to the following in server.properties

	manager@CDR2105:~/kafka_2.13-3.1.1/config$ sudo nano server.properties

	############################# Socket Server Settings #############################

	# The address the socket server listens on. It will get the value returned from
	# java.net.InetAddress.getCanonicalHostName() if not configured.
	#   FORMAT:
	#     listeners = listener_name://host_name:port
	#   EXAMPLE:
	#     listeners = PLAINTEXT://your.host.name:9092
	listeners=PLAINTEXT://127.0.0.1:9092


Open new Ubuntu console
	
	zookeeper-server-start.sh ~/kafka_2.13-3.1.1/config/zookeeper.properties

Open new Ubuntu console

	zookeeper-server-start.sh ~/kafka_2.13-3.1.1/config/zookeeper.properties
	
Open new Ubuntu console


public static void main(String[] args) {
		SpringApplication.run(KafkatestApplication.class, args);

		LOGGER.info("KafkaTestApplication started");

		Properties properties = new Properties();

		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer kafkaProducer = new KafkaProducer(properties);

		kafkaProducer.send(new ProducerRecord<>("quickstart-events", "Hello Greg!"));

		kafkaProducer.close();

		LOGGER.info("KafkaTestApplication stopped");
	}

	
public class ProducerConfig {
    private ProducerConfig() {}
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";   
    public static final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
}

package com.nelson.greg.kafkatest;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkatestApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkatestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkatestApplication.class, args);

		LOGGER.info("KafkaTestApplication started");

		String bootstrapServer = "127.0.0.1:9092";
		String groupId = "my-group";
		String topic = "quickstart-events";

		Properties consumerProperties = new Properties();
		consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);

		final Thread consumerThread = Thread.currentThread();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				consumer.wakeup();

				try {
					consumerThread.join();
				} catch (InterruptedException e) {
					LOGGER.error("Interrupted while waiting for consumer thread to exit", e);
				}
			}
		});

		try {

			consumer.subscribe(Arrays.asList(topic));

			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

				for (ConsumerRecord<String, String> record : records) {
					LOGGER.info("Received message: (" + record.key() + ", " + record.value() + ") at offset "
							+ record.offset());
				}
			}

		} catch (WakeupException e) {
			LOGGER.error("Consumer thread interrupted", e);
		} catch (Exception e) {
			LOGGER.error("Error while consuming", e);
		} finally {
			consumer.close();
		}
	}
}

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.6.7</version>
		<relativePath /> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.nelson.greg</groupId>
	<artifactId>kafkatest</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>kafkatest</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>11</java.version>
	</properties>
	<dependencies>
		<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-simple</artifactId>
			<version>1.7.36</version>
			<scope>test</scope>
		</dependency>
		
		<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-api</artifactId>
			<version>1.7.36</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.kafka</groupId>
			<artifactId>spring-kafka</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.kafka</groupId>
			<artifactId>spring-kafka-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
