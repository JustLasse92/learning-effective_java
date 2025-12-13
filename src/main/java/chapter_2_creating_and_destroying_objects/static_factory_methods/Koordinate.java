package chapter_2_creating_and_destroying_objects.static_factory_methods;

import java.util.HashMap;
import java.util.Map;

public class Koordinate {
    private static final Map<Double, Map<Double, Koordinate>> koordinaten = new HashMap<>();
    private final double x;
    private final double y;


    private Koordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private static Koordinate insertIntoCache(Koordinate koordinate) {
        koordinaten.computeIfAbsent(koordinate.x, k -> new HashMap<>())
                .put(koordinate.y, koordinate);
        return koordinate;
    }

    private static Koordinate createOrGetFromCache(double x, double y) {
        Map<Double, Koordinate> KoordinatenY = koordinaten.get(x);
        if (KoordinatenY != null) {
            return KoordinatenY.get(y);
        }
        return insertIntoCache(new Koordinate(x, y));
    }

    public static Koordinate fromCartesian(double x, double y) {
        return createOrGetFromCache(x, y);
    }

    public static Koordinate fromPolar(double r, double theta) {
        return fromCartesian(r * Math.cos(theta), r * Math.sin(theta));
    }


    @Override
    public String toString() {
        return "Koordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
