package chapter_2_creating_and_destroying_objects.static_factory_methods.item_5;

import java.util.List;

public class SpellCheckerUtility {
    // Dictionary ist nicht mehr austauschbar
    private static final List<String> dictionary = List.of("example", "words", "for", "the", "dictionary");

    private SpellCheckerUtility() {
    }

    public static boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public static List<String> suggestions(String typo) {
        // Dummy implementation for suggestions
        return List.of("suggestion1", "suggestion2");
    }
}
