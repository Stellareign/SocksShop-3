package me.ruana.dobbysshopapp.model;

public enum ColoursOfSocks {
    GREY("серый"),
    DARKGREY("тёмно-серый"),
    BLACK("чёрный"),
    WHITE("белый"),
    DENIM("джинсовый"),
    DARKBLUE("тёмно-синий"),
    BEIGE("бежевый"),
    COLORFUL("разноцветный");

    private final String colour;

    ColoursOfSocks(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

// ТУСТРИНГ ДЛЯ ВЫВОДА ЗНАЧЕНИЯ ПЕРЕЧИСЛЕНИЯ:
//    @Override
//    public String toString() {
//        return name() + " (" + colour + ")";
//    }
}
