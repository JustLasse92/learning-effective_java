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

