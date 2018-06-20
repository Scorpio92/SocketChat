### Установка и настройка PostgreSQL:

```
sudo apt-get install postgresql
```
Чтобы разрешить соединения по TCP/IP, отредактируйте файл /etc/postgresql/9.5/main/postgresql.conf. Найдите строку
```
#listen_addresses = 'localhost'
```
и замените ее на:
```
listen_addresses = 'localhost'
```
Перезапуск для принятия изменений
```
sudo /etc/init.d/postgresql restart
```
Доступ к командной строке Postgres без переключения аккаунтов
```
sudo -u postgres psql
```

### Создание БД
```
CREATE DATABASE auth_db;
```

Создание пользователя
```
CREATE USER auth_server WITH password 'auth_server';
```

Назначение прав
```
GRANT ALL ON DATABASE auth_db TO auth_server;
```

Аутентификация и начало работы с базой данных
```
psql -h localhost auth_db auth_server
```

### Скрипты создания таблиц

```
CREATE SEQUENCE account_ids;

CREATE TABLE accounts (
  account_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('account_ids'),
  login CHAR(20) NOT NULL,
  password_hash CHAR(40) NOT NULL,
  nickname CHAR(20) NOT NULL,
  email CHAR(45) DEFAULT NULL,
  CONSTRAINT login_idx UNIQUE (login),
  CONSTRAINT nickname_idx UNIQUE (nickname)
);


CREATE TABLE public.auth_info
(
  account_id integer NOT NULL,
  auth_token character(128) NOT NULL,
  token_create_timestamp timestamp without time zone DEFAULT now(),
  CONSTRAINT auth_info_pkey PRIMARY KEY (account_id),
  CONSTRAINT auth_token_idx UNIQUE (auth_token)
);

CREATE OR REPLACE FUNCTION upd_timestamp() RETURNS TRIGGER 
LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.token_create_timestamp = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

CREATE TRIGGER auth_info_upd_token_trigger
  BEFORE UPDATE
  ON auth_info
  FOR EACH ROW
  EXECUTE PROCEDURE upd_timestamp();
```

### Как задеплоить сервер

sudo apt-get install default-jdk

**Создать на сервере папку WebAuthServer и скопировать в нее 3 библиотеки + файл конфигурации:**

*gson-2.8.2.jar*

*postgresql-42.2.2.jar*

*WebAuthServer.jar*

*server.properties*

**Запустить сервер командой:**
```
java -cp WebAuthServer.jar:/home/ubuntu/WebAuthServer/*: ru.scorpio92.authserver.AuthServer

либо в фоне

nohup java -cp WebAuthServer.jar:/home/ubuntu/WebAuthServer/*: ru.scorpio92.authserver.AuthServer &
```
