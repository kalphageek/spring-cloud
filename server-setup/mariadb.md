1. install on Ubuntu
```shell
$ sudo apt update
$ sudo apt install mariadb-server
$ sudo service mariadb status
$ mysql -u root
Access denied for root
$ sudo mysql -u root 
```
2. change password of root
```sql
> use mysql;
> select user, host, plugin from mysql.user;
> set password for 'root'@'localhost'=password('test1234');
> flush privileges
> exit
$ mysql -u root -p
Enter password: test1234
```
3. create tables users
```sql
> create database mydb;
> use mydb;
> show tables;
> create tables users (
id long auto_increment primary key,
user_id varchar(20),
name varchar(20),
pwd varchar(20),
created_at datetime default now(),
);
```
4. insert sample data;
```sql
insert into users (user_id, name, pwd) values ('maru@naver.com', 'maru', 'pwd1');
insert into users (user_id, name, pwd) values ('gildong@naver.com', 'gildong', 'pwd1');
insert into users (user_id, name, pwd) values ('cheonum@naver.com', 'cheonum', 'pwd1');
```

