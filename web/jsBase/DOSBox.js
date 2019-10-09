/* 
By Anthony Stump
Created: 24 Aug 2019
 */

function generateApplication() {
	Dos(document.getElementById("jsdos"), { 
	    wdosboxUrl: "/asWeb/DOSBox/wdosbox.js" 
	}).ready((fs, main) => {
	  fs.extract("/asWeb/DOSBox/wf/dagger.zip").then(() => {
	    main(["-c", "CD DOOM2", "-c","DOOM2.EXE"])
	  });
	});
}


function initD() {
    generateApplication();
}

dojo.ready(initD);
