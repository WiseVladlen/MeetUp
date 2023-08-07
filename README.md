# Meet Up

## Краткое описание

Приложение для работы с календарем. Позволяет создавать события. Имеется интеграция с сервисом Яндекс.Календарь.

## Системные требования
- Min SDK - 26
- Java Version 1.8

## Пример сборки и запуска
- Клонировать проект и запустить с помощью IDE (Android Studio)
- Настроить версию Gradle (по необходимости)
- Запуститить на устройстве, учитывая минимально допустимую версию Android API

## Прочее
Синхронизация событий осуществляется в одностороннем режиме, где приоритетом является мобильное приложение. Для ее осуществления необходимо стабильное подключение к сети Интернет.

Для авторизации в системе необходимо воспользоваться данными, которые можно найти [здесь](https://github.com/WiseVladlen/MeetUp/blob/master/app/src/main/java/com/example/meet_up/data/local/PreliminaryData.kt)

## Версионность
Приложение имеет 2 версии, которые распределены по веткам `master` и `refactor`

- Первоначальная версия находится на ветке `master` и ее apk можно найти [здесь](https://github.com/WiseVladlen/MeetUp/blob/master/meet_up.apk)
- Усовершенствованная (т.е. с исправлениями) версия находится на ветке `refactor` и ее apk можно найти [здесь](https://github.com/WiseVladlen/MeetUp/blob/master/meet_up_version_2.apk)
