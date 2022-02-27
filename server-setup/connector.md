1. Jdbc source connector create
> [POST] http://localhost:8083/connectors
* create topic automatically at first time produce
```json
{
  "name" : "my-source-connect",
  "config" : {
    "connector.class" : "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url" : "jdbc:mysql://localhost:3306/mydb",
    "connection.user" : "root",
    "connection.password" : "test1234",
    "mode" : "incrementing",
    "incrementing.column.name" : "id",
    "table.whitelist" : "users",
    "topic.prefix" : "my_topic_",
    "tasks.max" : "1"
  }
}
```

2. Connect list
> [GET] http://localhost:8083/connectors

3. Connect describe
> [GET] http://localhost:8083/connectors/my-source-connect/status

4. Jdbc sing connector create
> [POST] http://localhost:8083/connectors
* create same table with topic name automatically and insert topic data 
```json
{
  "name" : "my-sink-connect",
  "config" : {
    "connector.class" : "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url" : "jdbc:mysql://localhost:3306/mydb",
    "connection.user" : "root",
    "connection.password" : "test1234",
    "auto.create" : "true",
    "auto.evolve" : "true",
    "delete.enabled" : "false",
    "topics" : "my_topic_users",
    "tasks.max" : "1"
  }
}
```