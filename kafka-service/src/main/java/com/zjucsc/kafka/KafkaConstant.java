package com.zjucsc.kafka;

public interface KafkaConstant {
    String KAFKA_BROKERS = "10.15.191.100:9092";

    Integer MESSAGE_COUNT=10000;

    String CLIENT_ID="client1";

    String TOPIC_NAME="demo";

    String GROUP_ID_CONFIG="consumerGroup10";

    Integer MAX_NO_MESSAGE_FOUND_COUNT=100;

    String OFFSET_RESET_LATEST="latest";

    String OFFSET_RESET_EARLIER="earliest";

    Integer MAX_POLL_RECORDS=10;
}
