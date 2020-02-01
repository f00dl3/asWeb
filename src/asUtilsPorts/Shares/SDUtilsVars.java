/*
by Anthony Stump
Created 18 Dec 2017
Updated 1 Feb 2020
*/

package asUtilsPorts.Shares;

public class SDUtilsVars {

    JunkyBeans junkyBeans = new JunkyBeans();
        
	private String build;
	final private String cachePath = junkyBeans.getUserHome().toString()+"/.cache";
    final private String codexPath = junkyBeans.getUserHome().toString()+"/src/codex";
	private String updated;
    final private String usbBackupPath = junkyBeans.getUserHome().toString()+"/USB-Back";

	public String getBuild() { return build; }
	public String getCachePath() { return cachePath; }
    public String getCodexPath() { return codexPath; }
	public String getUpdated() { return updated; }
    public String getUsbBackupPath() { return usbBackupPath; }

	public void setBuild(String newBuild) { build = newBuild; }
	public void setUpdated(String newUpdated) { updated = newUpdated; }

}
