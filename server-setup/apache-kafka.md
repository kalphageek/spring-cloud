* Setup
```shell
$ cd ~/workspace
$ wget https://www.apache.org/dyn/closer.cgi?path=/kafka/3.1.0/kafa_2.13-3.1.0.tgz
$ tar xvf kafa_2.13-3.1.0.tgz
$ cd
$ vi .bashrc
export KAFKA_HOME=/home/jjd/workspace/kafka_2.13-3.1.0
export PATH=$PATH:$KAFKA_HOME/bin
alias zookeeper-start='zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties'
alias kafka-start='kafka-server-start.sh $KAFKA_HOME/config/server.properties'
alias kafka-stop='kafka-server-stop.sh'
alias topic-create='kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 1 --topic'
alias topic-list='kafka-topics.sh --list --bootstrap-server localhost:9092'
alias topic-describe='kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic'
alias producer='kafka-console-producer.sh --bootstrap-server localhost:9092 --topic'
alias consumer='kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic'
:wq
$ zookeeper-start
$ kafka-start
$ topic-list
```