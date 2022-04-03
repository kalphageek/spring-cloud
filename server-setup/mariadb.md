1. install on Ubuntu
```sh
$ sudo apt update
$ sudo apt install mariadb-server
$ sudo service mariadb status
# 설치위치
$ ls /var/lib/mysql
```
2. 대소문자 구분하지 않기
```sh
$ sudo vi /etc/mysql/my.cnf
[mysqld]
lower_case_table_names = 1
:wq
$ sudo systemctl restart mariadb
```
3. root password 변경
```sh
$ mysql -u root -p
Access denied for root
$ sudo mysql -u root -p
```sql
> use mysql;
> select user, host, plugin from mysql.user;
> set password for 'root'@'localhost'=password('test1234');
> flush privileges
> exit
$ mysql -u root -p
Enter password: test1234
```
4. users 테이블 생성
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
5. insert sample data;
```sql
insert into users (user_id, name, pwd) values ('maru@naver.com', 'maru', 'pwd1');
insert into users (user_id, name, pwd) values ('gildong@naver.com', 'gildong', 'pwd1');
insert into users (user_id, name, pwd) values ('cheonum@naver.com', 'cheonum', 'pwd1');
```

