package org.example;

import chapter_2_creating_and_destroying_objects.static_factory_methods.item_2.Elvis;

import java.util.function.Supplier;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int a = 3 << 1;
        System.out.println(a);
        Supplier<Elvis> supplier = Elvis::getInstance;
    }
}