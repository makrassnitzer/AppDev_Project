# Event Planner Dokumentation
Diese App wurde in Kotlin geschrieben und dient zur vereinfachten Terminkoordination. <br>
Für das UI-Design wurde Jetpack Compose verwendet.

## Funktionalität
Auf der Startseite gibt es eine Übersicht, welche die zukünftigen Events abbildet sowie eine Übersicht, welche monatliche Statistiken (Anzahl der Events, Ausgaben, verschiedene Standorte) zeigt.
Die Zeitspanne dieser Statistiken kann anhand eines Dropdowns angepasst werden.
Weiters kann man hier auf die anderen Ansichten der App navigieren. <br>
Um ein neues Event erstellen zu können, muss man zumindest einen Titel sowie eine Uhrzeit und das Datum des jeweiligen Events bereitstellen. 
Werden die notwendigen Eingabefelder nicht ausgefüllt erscheint eine Snackbar mit der Fehlermeldung.
Zu empfehlen ist jedoch ohnehin das Ausfüllen der weiteren Eingabefelder, um eine optimale Auswertung der Statisken zu erhalten. 
Man kann, um zu diesem Vorgang zu kommen, einfach in der App hinnavigieren oder durch Schütteln des Handys ebenfalls ein Event erstellen.
Des Weiteren kann man bei der Erstellung eines Events die Email von einem Begleiter bereitstellen, um diesen dann direkt per Mail über das Event zu informieren. <br>
Hat man Events erstellt kann man diese in der Kalenderansicht ansehen und die betroffenen Tage werden farblich im Kalender markiert. Klickt man auf einen betroffenen Tag erhält man die Details zu diesem Tag. Durch Klicken auf die Detailansicht eines Events besteht die Möglichkeit dieses in den Google Kalender zu übernehmen.
Außerdem wird man per Push-Benachrichtigung über ein bevorstehendes Event informiert. <br>
Darüber hinaus kann man noch in einer eigenen Ansicht alle erstellen Events inkl. Details einsehen und mittels Dropdown Menü filtern. <br>
Die Datenspeicherung der Termine erfolgt lokal in einer JSON-Datei.

