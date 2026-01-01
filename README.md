# Item 1: Statische Fabrikmethoden gegenüber Konstruktoren (Kapitel 2 - Koordinate.java)

Vorteile von statischen Fabrikmethoden gegenüber Konstruktoren:

1. Erhöhte Lesbarkeit: Sie haben Namen, die den Zweck der Methode klar machen (z.B. fromCartesian, fromPolar).
2. Eine Klasse kann nur einen Konstruktor mit einem bestimmten Satz von Parametern haben. Es wäre eine schlechte Idee
   die Reihenfolge der Parameter zu ändern, um verschiedene Konstruktoren zu unterstützen. Eine statische Fabrikmethode
   kann jedoch identische Parameterlisten haben.
3. Objekte können im Cache gespeichert werden, um die Leistung zu verbessern. Dies ermöglicht es den Klassen die
   Zustände seiner Instanzen zu verwalten. Dies wird als <STRONG>instance-controlled</STRONG> bezeichnet. Immutable
   Klassen können damit garantieren, dass nicht zwei verschiedene Instanzen den gleichen Zustand haben. a.equals(b) ist
   nur true wenn a == b.
4. Statische Fabrikmethoden können Subtypen zurückgeben. Dadurch können Interfaces und abstrakte Klassen Instanzen ihrer
   Implementierungen zurückgeben, ohne die Implementierung offenzulegen. Ausserdem kann anhand der Parameter entschieden
   werden, welche Implementierung zurückgegeben wird. Bspw. {@link java.util.EnumSet} erzeugt basierend auf der Anzahl
   der Elemente verschiedene Implementierungen. Zudem kann die Rückgabe bei einem Release geändert werden, ohne die API
   zu verändern.

Nachteile von statischen Fabrikmethoden gegenüber Konstruktoren:

1. Wenn keine öffentlichen Konstruktoren vorhanden sind, können keine Subklassen erstellt werden, was die
   Erweiterbarkeit einschränkt. Dies kann jedoch auch gewünscht sein, da es für Immutable Klassen notwendig ist.
   Ausserdem können die Klassen als Komposition gestaltet werden, um Erweiterbarkeit zu ermöglichen. (Komposition:
   Eine Assoziation zwischen zwei Klassen, bei der die eine Instanz nicht ohne die andere existieren kann. zB
   Artikel und Buch).
2. Sie sind nicht sofort als solche erkennbar. Konstruktoren werden mit dem new-Operator aufgerufen, während
   statische Fabrikmethoden wie normale Methoden aufgerufen werden. Dies kann durch eine gute Benennung der Methoden
   abgemildert werden. (from, of, valueOf, getInstance, newInstance, getType, newType, create).

# Item 2: Builder Muster gegenüber Konstruktoren mit vielen Parametern (Kapitel 2 - NutritionFacts.java)

Konstruktoren und statische Fabrikmethoden sind unübersichtlich, wenn sie viele Parameter haben. Insbesondere bei
optionalen Parametern und identischen Typen.

Ohne Builder Muster gäbe es die folgenden Ansätze:

1. Telescoping Constructor Pattern: Beginnend mit einem Konstruktor, der nur die erforderlichen Parameter hat, werden
   weitere Konstruktoren hinzugefügt, die jeweils einen weiteren optionalen Parameter hinzufügen.
2. JavaBeans Pattern: Ein Objekt wird mit einem parameterlosen Konstruktor erstellt und die
   weiteren Parameter werden über Setter-Methoden gesetzt. Zwar ist dies lesbarer als das Teleskop-Muster, aber es
   hat ein **schwerwiegendes** Problem: Das Objekt ist während der Erstellung in einem inkonsistenten Zustand. Die
   Parameter können bei der Erstellung nicht validiert werden. Zudem ist dieses Muster für Immutable Klassen nicht
   geeignet. Threadsicherheit ist ebenfalls ein Problem.

Idealerweise erhält der Builder die required Parameter im Konstruktor und die optionalen Parameter über
Setter-Methoden. Lohnen tut sich das Builder-Muster vor allem bei vielen Parametern (ab 4). Kommen in Zukunft jedoch
weitere Parameter hinzu, bleiben die Konstruktoren und statischen Fabrikmethoden idR vorhanden, um die
Abwärtskompatibilität zu
gewährleisten.

# Item 3: Singleton Muster (Kapitel 2 - Elvis.java)

Konstruktoren sind privat, um die Erstellung von Instanzen zu verhindern. Die Singleton-Instanz wird als
finales, statisches Attribut (`INSTANCE`) gehalten. Diese ist entweder öffentlich oder wird über eine statische
Fabrikmethode zurückgegeben. Als Alternative kann ein Singleton auch durch ein **Enum** implementiert werden.

Will man sicherstellen, dass über Reflection keine weiteren Instanzen erstellt werden können, so kann der Konstruktor
eine Exception werfen, wenn bereits eine Instanz existiert.

Fragen:

1. Welchen Vorteil und Nachteile hat ein Singleton-Muster mit einer statischen Fabrikmethode gegenüber ein
   öffentliches Klassenattribut?
   Vorteile Klassenvariable:
    - Durch die API sofort klar, dass es sich um ein Singleton handelt
    - Leichter umzusetzen, da die Klassenvariable eh Voraussetzung ist Vorteile Fabrikmethode
    - Im Nachhinein kann die Implementierung ändern kann. Bsp. einen Subtyp zurückgeben oder eine Instanz pro Thread
      zu erzeugen.
    - Lazy Initialization ist möglich
    - Die Fabrikmethode kann als Supplier fungieren, ohne dass die Klasse selbst Supplier implementieren zu müssen.
      `Supplier<Elvis> s = Elvis::getInstance;`

Trotz der Vorteile der Fabrikmethoden ist das Klassenattribut die gängigere Variante, da sie simpler ist.

# Item 4: Nicht-Instanziierbarkeit durch private Konstruktoren

Manchmal möchte man eine Klasse schreiben, die nur statische Methoden hat. Überwiegend haben diese Klassen ein
schlechtes Image, da sie nicht objektorientiert sind. Doch es gibt sinnvolle Utilklassen, wenn die Basisklasse nicht
erweiterbar ist. Beispielsweise wenn es ein primitiver Datentyp oder eine Klasse ist, die man nicht erweitern kann.
Beispiele sind 'java.lang.Math' und 'link java.util.Collections'.

Der private Konstruktor kann ein AssertionError werfen, um zu verhindern, dass die Klasse per Reflection
instanziiert wird.

# Item 5: Bevorzuge Dependency Injection gegenüber hart kodierten Abhängigkeiten

Man sollte kein Singleton oder eine statische Utility-Klasse verwenden, wenn diese Abhängigkeiten zu anderen Klassen
hat, die ihr Verhalten beeinflussen. Dies macht die unflexibel und nicht testbar, da die Ressource nicht geändert
werden kann. Die Ressourcen oder entsprechende Factory-Methoden injizieren werden (über den Konstruktor oder einem
Dependency Injection Framework wie Spring).

# Item 6: Wiederverwendung von Instanzen gegenüber dem Erzeugen neuer Instanzen

Die Erzeugung einer Instanz ist oft teuer und wenn möglich, sollte eine Instanz wiederverwendet werden. Manchmal ist
dies nicht leicht zu erkennen, wenn zB. bei Methodenaufrufen neue Instanzen erzeugt werden.

Ebenso muss bei Autoboxing aufgepasst werden, da dies in den meisten Fällen neue Instanzen erzeugt. Primitive
Datentypen sollten gegenüber ihren Wrapper-Klassen bevorzugt werden, um unnötige Instanziierungen zu vermeiden.

Jedoch muss hier auch der Nutzen abgewogen werden. Eine Instanz zu erzeugen, bei dem der Konstruktor simpel ist,
Bedarf nur wenig Ressourcen, insbesondere bei modernen JVM Implementierungen. Wenn durch zusätzliche Objekte das
Programm übersichtlicher, simpler und wartbarer wird, so sollte dies bevorzugt werden. Zudem kann die Verwaltung
eines eigenen Objektpools zu einer schlechteren Performance führen, als wenn die JVM dies eigenständig optimieren
kann. Eine klassische Ausnahme sind beispielsweise Datenbankverbindungen, da die Erstellung dieser sehr teuer ist.

Der Leitsatz "Erzeuge kein neues Objekt, wenn du ein bereits vorhandenes Objekt verwenden solltest" steht der
Leitsatz (Item 50) gegenüber: "Verwende kein vorhandenes Objekt, wenn ein neues Objekt besser geeignet ist". Dies
kommt aus dem Grundsatz vom Defensive Copying.

# Item 7 Vermeide Memory Leaks durch nicht verwendete Objekte

Ein Memory Leak entsteht, wenn Objekte im Speicher gehalten werden, obwohl sie nicht mehr benötigt werden.
Typischerweise entsteht dies, wenn eine Klasse ihren eigenen Speicher verwaltet und Objekte nicht entfernt, die
nicht mehr benötigt werden.

## Cache

Ein weiterer potenzieller Memory Leak entsteht durch einen Cache. Oft wird dieser durch eine Map realisiert, doch
selten werden nicht mehr benötigte Objekte daraus entfernt. Eine Möglichkeit ist es eine 'WeakHashMap' zu verwenden,
die automatisch Einträge entfernt, deren Schlüssel nicht mehr referenziert werden.

## Strong, Soft und Weak References

Solange eine starke Referenz auf ein Objekt existiert, kann der Garbage Collector das Objekt nicht entfernen.

```
Integer i = 1;
```

Bei einer **soft Reference** kann der Garbage Collector das referenzierte Objekt entfernen, wenn der Speicher knapp
wird.

```
Integer prime = 1;  
SoftReference<Integer> soft = new SoftReference<Integer>(prime);
prime = null;
```

Objekte die nur über eine **weak Reference** referenziert werden, werden vom Garbage Collector im nächsten Zyklus
entfernt.

```
Integer prime = 1;  
WeakReference<Integer> soft = new WeakReference<Integer>(prime);
prime = null;
```

# Item 8: Vermeide Finalizer und Cleaner

Um Ressourcen freizugeben, sollten stattdessen **try-with-resources** oder explizite 'close()' Methoden von einem '
AutoCloseable' verwendet werden. Von der Verwendung von Finalizern und Cleanern wird abgeraten. Sie eignen sich
jedoch als Sicherheitsnetz, falls der Anwender vergisst die close() Methode aufzurufen. Der Nutzen muss dabei mit
den Kosten abgewogen werden.

## Finalizer

Finalizer ist eine 'java.Objekt' Methode, die vom Garbage Collector (in einem finalizer-Thread) aufgerufen wird, bevor
das Objekt entfernt wird. Früher wurden die verwendet, um weitere Ressourcen bei der Entfernung eines Objekts
freizugeben. Seit Java 9 sind diese jedoch depricated, da sie viele Probleme mit sich bringen:

- Unvorhersehbare Ausführungszeit: Der Zeitpunkt der Ausführung ist unbestimmt.
- Keine Garantie der Ausführung: Es gibt keine Garantie, dass der Finalizer jemals ausgeführt wird.
- Performance-Overhead: Finalizer können die Garbage Collection verlangsamen.
- Sicherheitsrisiken: Finalizer können Sicherheitslücken öffnen (finalizer attack), wenn sie nicht richtig implementiert

## Cleaner

Abgesehen vom Finalizer gibt es seit Java 9 die 'Cleaner' Klasse, die eine Alternative darstellt. Diese stellt eine
statische Klasse dar, bei der sich die Objekte registrieren können, um bei der Entfernung eine Cleanup-Aktion
auszuführen. Gibt einem mehr Kontrolle über den Thread, doch Probleme bestehen weiterhin.
