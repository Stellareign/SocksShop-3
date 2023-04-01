package me.ruana.dobbysshopapp.model;

public enum CottonContentInSocks {
    SYNTHETICS(0, 5),
    VERY_LITTLE_COTTON(5, 20),
    LITTLE_COTTON(25, 40),
    MEDIUM_COTTON(40, 60),
    NORMAL_COTTON(60, 75),
    LOST_OF_COTTON(45, 97),
    MAX_COTTON(97, 99),
    COTTON_100(100, 100);

    private final Integer minCottonContent;
    private final Integer maxCottonContent;

    CottonContentInSocks(Integer minCottonContent, Integer maxCottonContent) {
        this.minCottonContent = minCottonContent;
        this.maxCottonContent = maxCottonContent;
    }

    public Integer getMinCottonContent() {
        return minCottonContent;
    }
    public Integer getMaxCottonContent() {
        return maxCottonContent;
    }


    // ТУСТРИНГ ДЛЯ ВЫВОДА ЗНАЧЕНИЯ ПЕРЕЧИСЛЕНИЯ:
//    @Override
//    public String toString() {
//        return name() + " (" + minCottonContent + "-" + maxCottonContent + " %" + ")";
//    }
}
