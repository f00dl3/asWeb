/*
by Anthony Stump
Created: 17 Feb 2018

Applies to tables:
    Core.Fit_Calories

 */
package asWebRest.model;

public class FitCalories {
    
    private int calories;
    private int carbs;
    private int cholest;
    private int fat;
    private String food;
    private double fruitVeggie;
    private String last;
    private String lastE;
    private int protein;
    private String serving;
    private double servingsLast;
    private double servingsLastE;
    private int sodium;
    private int sugar;
    private int water;
    
    public int getCalories() { return calories; }
    public int getCarbs() { return carbs; }
    public int getCholest() { return cholest; }
    public int getFat() { return fat; }
    public String getFood() { return food; }
    public double getFruitVeggie() { return fruitVeggie; }
    public String getLast() { return last; }
    public String getLastE() { return lastE; }
    public int getProtein() { return protein; }
    public String getServing() { return serving; }
    public double getServingsLast() { return servingsLast; }
    public double getServingsLastE() { return servingsLastE; }
    public int getSodium() { return sodium; }
    public int getSugar() { return sugar; }
    public int getWater() { return water; }
    
    public void setCalories(int calories) { this.calories = calories; }
    public void setCarbs(int carbs) { this.carbs = carbs; }
    public void setCholest(int cholest) { this.cholest = cholest; }
    public void setFat(int fat) { this.fat = fat; }
    public void setFood(String food) { this.food = food; }
    public void setFruitVeggie(double fruitVeggie) { this.fruitVeggie = fruitVeggie; }
    public void setLast(String last) { this.last = last; }
    public void setLastE(String lastE) { this.lastE = lastE; }
    public void setProtein(int protein) { this.protein = protein; }
    public void setServing(String serving) { this.serving = serving; }
    public void setServingsLast(double servingsLast) { this.servingsLast = servingsLast; }
    public void setServingsLastE(double servingsLastE) { this.servingsLastE = servingsLastE; }
    public void setSodium(int sodium) { this.sodium = sodium; }
    public void setSugar(int sugar) { this.sugar = sugar; }
    public void setWater(int water) { this.water = water; }
       
}
