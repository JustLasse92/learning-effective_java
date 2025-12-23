package chapter_2_creating_and_destroying_objects.static_factory_methods.item_6;

import java.util.regex.Pattern;

public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})"
                    + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }


    static boolean isRomanNumeralSlow(String s) {
        // Performance kann stark verbessert werden. Der String ist zwar Immutable und wird von der JVM gecached,
        // aber matches muss jedes Mal den Pattern-Compiler durchlaufen, wofür ein neues Pattern-Objekt erstellt wird.
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
                + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }
}

