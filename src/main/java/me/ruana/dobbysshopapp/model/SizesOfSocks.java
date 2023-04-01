package me.ruana.dobbysshopapp.model;


public enum SizesOfSocks {
    XS(32, 35),
    S( 35, 37),
    M( 37, 39),
    L(39, 42),
    XL( 42, 44),
    XXXL( 44, 46);

    private final Integer minSize;
    private final Integer maxSize;

    SizesOfSocks( Integer minSize, Integer maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    // ===== ГЕТТЕРЫ: ========
    public Integer getMinSize() {
        return minSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    // ТУСТРИНГ ДЛЯ ВЫВОДА ЗНАЧЕНИЯ ПЕРЕЧИСЛЕНИЯ:
//    @Override
//    public String toString() {
//        return name() + " (" + minSize + "-" + maxSize + ")";
//    }
}



