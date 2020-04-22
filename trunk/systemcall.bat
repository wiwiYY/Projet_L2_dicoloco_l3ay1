@ECHO OFF

cd %PROGRAMFILES%\MySQL\MySQL Server 8.0\bin
mysql -u root -p --default-character-set=utf8 < %~dp0\dicoloco.sql
cd %~dp0\Front\html
dicoloco_accueil.html
cd %~dp0\Executable
java -jar Dicoloco-0.0.1-SNAPSHOT.jar

PAUSE