/*
by Anthony Stump
Created: 10 Feb 2018
Updated: 28 Dec 2019
*/

package asUtilsPorts;

import asUtils.Shares.JunkyBeans;
import java.io.File;

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
