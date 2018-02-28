/*
by Anthony Stump
Created: 15 Feb 2018

Applies to tables:
    Core.WebUIsers

 */

package asWebRest.model;

public class WebUIsers {
    
    private String hash;
    private int id;
    private String password;
    private String sha256;
    private String username;
    
    public String getHash() { return hash; }
    public int getId() { return id; }
    public String getPassword() { return password; }
    public String getSha256() { return sha256; }
    public String getUsername() { return username; }
    
    public void setHash(String hash) { this.hash = hash; }
    public void setId(int id) { this.id = id; }
    public void setPassword(String password) { this.password = password; }
    public void setSha256(String sha256) { this.sha256 = sha256; }
    public void setUsername(String username) { this.username = username; }
    
}
