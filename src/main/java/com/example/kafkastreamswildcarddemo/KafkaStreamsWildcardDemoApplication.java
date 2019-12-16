package com.example.kafkastreamswildcarddemo;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@SpringBootApplication
public class KafkaStreamsWildcardDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsWildcardDemoApplication.class, args);
	}

	@Bean
    public Consumer<KStream<String, String>> consumeMessage(MessageConsumer consumer) {
	    return input -> input.peek((key, value) -> consumer.consume(value));
    }

    @Component
    static class MessageConsumer {

	    public void consume(String message) {
	        // something can happen here
        }

    }

}
