/*
by Anthony Stump
Created: 18 Feb 2018

for tables:
    Core.Cooking
 */

package asWebRest.model;

public class Cooking {
    
    private int cookTemp;
    private int cookTime;
    private String description;
    private String ingredients;
    private String instructions;
    
    public int getCookTemp() { return cookTemp; }
    public int getCookTime() { return cookTime; }
    public String getDescription() { return description; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    
    public void setCookTemp(int cookTemp) { this.cookTemp = cookTemp; }
    public void setCookTime(int cookTime) { this.cookTime = cookTime; }
    public void setDescription(String description) { this.description = description; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
}
