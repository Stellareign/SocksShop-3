package me.ruana.dobbysshopapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Socks {
    @SerializedName("Размер")
    @JsonProperty("Размер")
    private SizesOfSocks sizesOfSocks;
    @SerializedName("Цвет")
    @JsonProperty("Цвет")
    private ColoursOfSocks colourOfSocks;

    @SerializedName("Содержание хлопка")
    @JsonProperty("Содержание хлопка")
    private int cottonContent;



    @Override
    public String toString() {
        return
                "размер: " + sizesOfSocks.toString() +
                ", цвет: " + colourOfSocks +
                ", содержание хлопка: " + cottonContent +
                ", наличие на складе ";
    }
}
