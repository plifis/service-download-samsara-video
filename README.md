Пример запуска из командной строки:  
java -DAUTH_KEY="secret" -DENV_LOCATION="c:\ent.txt"  -DVIDEO_LOCATION="d:\temp" -jar .\samsara_grabber-0.0.1-SNAPSHOT.jar -vs

java -DAUTH_KEY="samsara_api_Lm1qDkW8DtjN4bsZ877CA3cs0FDwcX" -DSTART_TIME=2023-02-04T12:30:00Z -DEND_TIME=2023-02-04T13:30:00Z -jar samsara_grabber-0.0.1.jar -se
параметры для запуска приложения:
DAUTH_KEY - токен для подключения к Samsara
DSTART_TIME и DEND_TIME - указывается временной промежуток за который необходимо получить видео  с endpoint SafetyEvents.
DENV_LOCATION - файл в котором указаны критерии поиска "поездок и видео файлов". 
В формате:
"vehicleId:123123,9999;/n
start:2022-12-20 12:30:00;/n
end:2022-12-21 12:31:00;/n"

ключи для запуска приложения:
AUTH_KEY=***, где *** - токен для подключения сгенерированный в Samsara
-vs загрузить все машины
-se загрузить видео с endpoint SafetyEvents

[//]: # (-tr для поездок: -vehicleId=*** -startTrip="2022-12-20 12:20:30" -end="2022-12-21 12:20:30" )

[//]: # (-vo для видео)
