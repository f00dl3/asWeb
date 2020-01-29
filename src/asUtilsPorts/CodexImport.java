/*
by Anthony Stump
Created: 10 Feb 2018
Updated: 29 Jan 2020
*/

package asUtilsPorts;

import java.io.File;

import asUtilsPorts.Shares.JunkyBeans;

public class CodexImport {
    
	public static void wrapper(String fileName) {
		String wrapperArgs[] = { fileName };
		main(wrapperArgs);
	}
	
    public static void main(String[] args) {
        
        if(args.length == 0) { System.out.println("You must enter the file name, relative to codex, as an argument!"); System.exit(0); }
        
        JunkyBeans junkyBeans = new JunkyBeans();
        
        final File file2Read = new File(junkyBeans.getUserHome().toString()+"/codex/"+args[0]);
        System.out.println("DEBUG: Last Modified: "+file2Read.lastModified());
        
        
    }
    
}
