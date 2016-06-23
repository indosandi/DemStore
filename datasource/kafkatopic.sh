/usr/local/kafka/bin/kafka-topics.sh --describe --zookeeper localhost:2181
./bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic price
/usr/local/kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181 --topic price --partitions 4 --replication-factor 2
/usr/local/kafka/bin/kafka-simple-consumer-shell.sh --broker-list localhost:9092 --topic price_data_part4 --partition 0
