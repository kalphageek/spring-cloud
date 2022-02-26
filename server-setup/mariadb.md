* setup on Ubuntu
```shell
$ sudo apt update
$ sudo apt install mariadb-server
$ mysql -u root
Access denied for root
$ sudo mysql -u root 

mysql> use mysql;
mysql> select user, host, plugin from mysql.user;
mysql> set password for 'root'@'localhost'=password('test1234');
mysql> flush privileges
mysql> exit
$ sudo service mariadb status

$ mysql -u root -p
Enter password: test1234
mysql> create database mydb;
mysql> use mydb;
mysql> show tables;
```