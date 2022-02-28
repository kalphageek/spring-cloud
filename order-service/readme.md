## Kafka sink connector for the orders table
```json
{
  "name" : "orders-sink-connect",
  "config" : {
    "connector.class" : "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url" : "jdbc:mysql://localhost:3306/mydb",
    "connection.user" : "root",
    "connection.password" : "test1234",
    "auto.create" : "true",
    "auto.evolve" : "true",
    "delete.enabled" : "false",
    "topics" : "orders",
    "tasks.max" : "1"
  }
}
```