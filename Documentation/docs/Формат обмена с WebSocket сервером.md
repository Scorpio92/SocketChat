Перед началом обмена (при открытии socket-канала), клиент должен предоставить токен доступа. Пока клиент клиент не предоставит токен, все сообщения от него сервер должен отбрасывать.

При инициализации соединения, клиент помещает в http пакет кастомный заголовок с именем **authToken**. Сервер проверяет наличие этого заголовка и если заголовок присутствует, отправляет на сервер авторизации запрос с типом [CHECK_TOKEN](https://github.com/mpgp/Documentation/wiki/%D0%A4%D0%BE%D1%80%D0%BC%D0%B0%D1%82-%D0%BE%D0%B1%D0%BC%D0%B5%D0%BD%D0%B0-%D1%81-%D1%81%D0%B5%D1%80%D0%B2%D0%B5%D1%80%D0%BE%D0%BC-%D0%B0%D0%B2%D1%82%D0%BE%D1%80%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8-(%D0%B1%D0%B5%D0%B7-%D0%BA%D1%80%D0%B8%D0%BF%D1%82%D0%BE%D0%B3%D1%80%D0%B0%D1%84%D0%B8%D0%B8)#%D0%9F%D1%80%D0%BE%D0%B2%D0%B5%D1%80%D0%BA%D0%B0-%D1%82%D0%BE%D0%BA%D0%B5%D0%BD%D0%B0)

## AuthMessage
- server -> client
```json
//если токен принят
{
	type: "AUTH_MESSAGE",
	status: "SUCCESS",
	serverData: {
		userList: [{
			nickname: "String",
			userId: "String",
			avatar: "String",
                        language: "String",
                        status: "String",
                        lastOnline: "String",
                        registerDate: "String"
		},
		...]
	}
}
```
```
serverData - строка шифрованная ключом сервера
userList - массив пользователей, которые в данный момент находятся онлайн
nickname - никнейм пользователя
userId - id пользователя
avatar - ссылка на аватарку пользователя
language - язык пользователя
status - статус пользователя (как в соц. сетях)
lastOnline - время последнего пребывания в сети
registerDate - дата регистрации
```

```json
//если что-то пошло не так
{
        type: "AUTH_MESSAGE",
	status: "ERROR",
        error: {
           errorCode: "String",
           message: "String"
        }
}
```
**Возможные коды ошибок:**

_"1" – клиент уже подключен_

_"2" – токен не найден или он невалидный_

## ChatMessage

Broadcast-сообщение которое посылается всем клиентам в чате

- client -> server
```json
{
	type: "CHAT_MESSAGE",
	serverData: {
		message: "String"
	}
}
```
```
serverData - строка шифрованная ключом сервера
```
- server -> client
```json
{
	type: "CHAT_MESSAGE",
	serverData: {
		message: "String",
                sender: "String",
		sendTime: "String"
	}
}

serverData - строка шифрованная ключом сервера
sender - userId отправителя
sendTime - время отправки сообщения (timestamp)
```

## DialogMessage

Личное сообщение которое должно маршрутизироваться от клиента к клиенту. Сервер гарантирует доставку данного сообщения посредством отправки клиенту контрольного сообщения DeliveryMessage.

- client -> server
```json
{
	type: "DIALOG_MESSAGE",
	clientData: {
		message: "String"
	},
	serverData: {
		receiver: "String"
	}
}

clientData - строка шифрованная ключом клиента
serverData - строка шифрованная ключом сервера
receiver - userId получателя
```
- server -> client
```json
{
	type: "DIALOG_MESSAGE",
	clientData: {
		message: "String"
	},
	serverData: {
                messageId: "String",
		sender: "String",
		sendTime: "String"
	}
}

clientData - строка шифрованная ключом клиента
serverData - строка шифрованная ключом сервера
messageId - id сообщения пользователя
```

## DeliveryMessage
Служебное сообщение об успешности доставки DialogMessage.

- server -> client

```json
//личное сообщение(DIALOG_MESSAGE) успешно доставлено
{
	type: "DELIVERY_MESSAGE",
	status: "SUCCESS",
        serverData: {
              messageId: "String"
        }
}
```
```json
//личное сообщение(DIALOG_MESSAGE) не доставлено
{
	type: "DELIVERY_MESSAGE",
	status: "ERROR",
        serverData: {
              messageId: "String"
        }
}
```

## UserConnectionMessage

Broadcast-сообщение которое отсылается всем при подключении/отключении клиента.

- server -> client
```json
{
	type: "USER_CONNECTION_MESSAGE",
	serverData: {
                connectionStatus: "String"
		userId: "String",
                nickname: "String",
		avatar: "String",
		language: "String",
		status: "String",
                lastOnline: "String",
                registerDate: "String"
	}
}

connectionStatus: {CONNECT, DISCONNECT}

При DISCONNECT поля nickname, avatar, language, status, lastOnline, registerDate не отсылаются.
```

## SaveAvatar

- client -> server
```json
{
	type: "SAVE_AVATAR",
	serverData: {
		imageBase64: "String"
	}
}
```
- server -> client
```json
//новая аватарка успешно сохранена
{
	type: "SAVE_AVATAR",
	status: "SUCCESS"
}
```
```json
//новая аватарка не сохранена
{
	type: "SAVE_AVATAR",
	status: "ERROR",
        error: {
           errorCode: "String",
           message: "String"
        }
}
```
**Возможные коды ошибок:**

_"1" – размер картинки слишком большой_
