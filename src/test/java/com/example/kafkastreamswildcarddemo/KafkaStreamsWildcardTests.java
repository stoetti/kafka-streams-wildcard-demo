package com.example.kafkastreamswildcarddemo;

import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(
        properties = {
                "spring.cloud.stream.kafka.binder.brokers=${spring.embedded.kafka.brokers}",
                "spring.cloud.stream.bindings.consumeMessage-in-0.destination=input.*",
                "spring.cloud.stream.kafka.streams.bindings.consumeMessage-in-0.consumer.destinationIsPattern=true"}
)
@EmbeddedKafka(topics = {"input.1", "input.2"}, ports = 19092)
@DirtiesContext
class KafkaStreamsWildcardTests {

    @Autowired
    private EmbeddedKafkaBroker kafkaEmbedded;

    @MockBean
    private KafkaStreamsWildcardDemoApplication.MessageConsumer consumerMock;

    @Test
    void sendMessageTest() {
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(kafkaEmbedded);
        senderProps.put("key.serializer", StringSerializer.class);
        senderProps.put("value.serializer", StringSerializer.class);
        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
        KafkaTemplate<String, String> template = new KafkaTemplate<>(pf, true);
        template.setDefaultTopic("input.1");
        template.sendDefault("foo");

        verify(consumerMock, timeout(2000).times(1)).consume(eq("foo"));
    }

}
