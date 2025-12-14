package chapter_2_creating_and_destroying_objects.static_factory_methods.item_2;

public class Elvis {
    private static final Elvis INSTANCE = new Elvis();

    private Elvis() {
    }

    public static Elvis getInstance() {
        return INSTANCE;
    }

    public void leaveTheBuilding() {
        System.out.println("Elvis has left the building.");
    }
}
