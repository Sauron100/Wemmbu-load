package de.characterloading;

/** The selectable characters and which image file belongs to which menu slot. */
public enum Figure {
    WEMMBU("Wemmbu",
            "wemmbu_1_loading", "wemmbu_2_transition", "wemmbu_3_title", "wemmbu_4_menu_a", "wemmbu_5_menu_b"),
    MANEPEAR("ManePear",
            "manepear_1_loading", "manepear_2_transition", "manepear_3_title", "manepear_4_menu", "manepear_2_transition");

    public final String display;
    private final String[] files;

    Figure(String display, String loading, String transition, String title, String menuA, String menuB) {
        this.display = display;
        this.files = new String[]{loading, transition, title, menuA, menuB};
    }

    public String file(Slot slot) { return files[slot.ordinal()]; }

    public enum Slot { LOADING, TRANSITION, TITLE, MENU_A, MENU_B }
}
