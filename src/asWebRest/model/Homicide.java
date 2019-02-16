/*
by Anthony Stump
Created: 18 Feb 2018

for tables:
    Core.Homicide
 */

package asWebRest.model;

import org.json.JSONArray;

public class Homicide {
    
    private int age;
    private String circumstances;
    private String date;
    private String description;
    private String gender;
    private String homId;
    private JSONArray point;
    private String race;
    private String region;
    private int timeOfDeath;
    private String victim;
    private String whereFound;
    
    public int getAge() { return age; }
    public String getCircumstances() { return circumstances; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getGender() { return gender; }
    public String getHomId() { return homId; }
    public JSONArray getPoint() { return point; }
    public String getRace() { return race; }
    public String getRegion() { return region; }
    public int getTimeOfDeath() { return timeOfDeath; }
    public String getVictim() { return victim; }
    public String getWhereFound() { return whereFound; }
    
    public void setAge(int age) { this.age = age; }
    public void setCircumstances(String circumstances) { this.circumstances = circumstances; }
    public void setDate(String date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setGender(String gender) { this.gender = gender; }
    public void setHomId(String homId) { this.homId = homId; }
    public void setPoint(JSONArray point) { this.point = point; }
    public void setRace(String race) { this.race = race; }
    public void setRegion(String region) { this.region = region; }
    public void setTimeOfDeath(int timeOfDeath) { this.timeOfDeath = timeOfDeath; }
    public void setVictim(String victim) { this.victim = victim; }
    public void setWhereFound(String whereFound) { this.whereFound = whereFound; }
    
}
