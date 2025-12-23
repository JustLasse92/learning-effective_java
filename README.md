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
Beispiele sind {@link java.lang.Math} und {@link java.util.Collections}.

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