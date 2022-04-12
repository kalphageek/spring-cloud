1. confluent-community install
```sh
$ cd ~/Downloads
$ wget http://packages.confluent.io/archive/7.0/confluent-community-7.0.1.tar.gz
$ tar xvf confluent-community-7.0.1.tar.gz
$ mv ./confluent-7.0.7 ~/workspace/
$ cd
$ vi .bashrc
export CONFLUENT_HOME=/home/jjd/workspace/confluent-7.0.1
export PATH=$PATH:$JAVA_HOME/bin:$CONFLUENT_HOME/bin

alias cloud='cd ~/workspace/spring-learning/spring-cloud/'
alias zookeeper-start='zookeeper-server-start $CONFLUENT-HOME/etc/kafka/zookeeper.properties'
alias kafka-start='kafka-server-start $CONFLUENT-HOME/etc/kafka/server.properties'
alias kafka-stop='kafka-server-stop'
alias topic-create='kafka-topics --create --bootstrap-server localhost:9092 --partitions 1 --topic'
alias topic-list='kafka-topics --list --bootstrap-server localhost:9092'
alias topic-describe='kafka-topics --describe --bootstrap-server localhost:9092 --topic'
alias producer='kafka-console-producer --bootstrap-server localhost:9092 --topic'
alias consumer='kafka-console-consumer --bootstrap-server localhost:9092 --topic'
alias connect-start='connect-distributed $CONFLUENT_HOME/etc/kafka/connect-distributed.properties'
:wq

$ zookeeper-start # 2181
$ kafka-start # 9092
$ connect-start  # 8082 (connect), 8083 (rest-proxy)
```
2. jdbc connector setup
> https://docs.confluent.io/kafka-connect-jdbc/current/ download
```sh
$ cp ./confluentinc-kafka-connect-jdbc-10.3.3.zip ~/workspace/connector/
$ unzip ./confluentinc-kafka-connect-jdbc-10.3.3.zip
$ vi $CONFLUENT_HOME/etc/kafka/connect-distributed.properties
plugin.path=~/workspace/connector/confluentinc-kafka-connect-jdbc-10.3.3/lib
:wq
```
3. mariadb jdbc driver setup
```sh
cd ~/workspace/.m2/repository/org/mariadb/jdbc/mariadb-java-client/2.7.1
cp ./mariadb-java-client-2.7.1.jar ~/workspace/confluent-7.0.1/share/java/kafka/
```