package net.javango.bakingtime.model;

import com.google.gson.annotations.SerializedName;

/**
 * A recipe ingredient.
 */
public class Ingredient {

    @SerializedName("ingredient")
    private String name;
    private String measure;
    private float quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
