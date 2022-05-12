zookeeper-server-start.sh ~/kafka/kafka_2.13-3.1.0/config/zookeeper.properties

kafka-server-start.sh ~/kafka/kafka_2.13-3.1.0/config/server.properties

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic kafka-demo --partitions 3 --replication-factor 1