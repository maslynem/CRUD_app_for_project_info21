# CRUD_app_for_project_info21
Создание web-приложения для учебного проекта SQL на языке Java. <br>
Программа реализована с использованием MVC-фреймворка (Spring) <br>
Рендеринг страниц осуществляется на стороне сервера (использование технологии **Server-Side Rendering**) <br>
### Содержание
- Главная страница содержит:
    - Навигационное меню, обеспечивающее переход к основным разделам приложения: *«Данные»* и *«Операции»*
    - Поле *«О проекте»*, содержащее основную информацию о проекте

- Графическая оболочка страниц *«Данные»* и *«Операции»* содержит следующие разделы:
    - Шапка, по нажатию на которую можно осуществить переход на главную страницу
    - Навигационное меню, позволяющее осуществить переход по основным разделам
      
- Раздел *«Данные»* содержит подразделы, которые позволяют через GUI поддерживать следующий функционал:
    - Совершать CRUD-операции по всем таблицам
    - При любой модификации таблиц (create, update, delete) приложение запрашивает у пользователя подтверждение на осуществление операции
    - После любого вида модификации таблиц пользователю выводится измененная таблица
    - Импорт и экспорт данных для каждой таблицы из файлов/в файлы с расширением *.csv*

- Раздел *«Операции»* содержит компоненты:
    - Блок, содержащий все возможные для вызова хранимых процедур из БД, наименование/краткое описание сути запроса
    - Блок с возможностью самостоятельного ввода пользователем SQL-запроса для работы с данными в базе

Реализовано логирование всех действий пользователя (файлы логов записывать в папку logs). <br>
Подготовлен файл docker-compose.yml обеспечивающий развертывыние приложения.
