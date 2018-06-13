dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.form.Button");

var ifrs = new dojo.data.ItemFileReadStore({url:"wxSample.json"});

dojo.ready(function() {
    
   function getStations() {
       function clearOldList(size, request) {
           var list = dojo.byId("list2");
           if(list) {
               while(list.firstChild) { list.removeChild(list.firstChild); }
           }
       }
   }
   
   function gotStations(items, request) {
       var list = dojo.byId("list2");
       if(list) {
           var i;
           for (i=0; i<items.length; i++) {
               var item = items[i];
               list.appendChild(document.createTextNode(ifrs.getValue(item, "Temperature")));
               list.appendChild(document.createElement("br"));
           }
       }
   }
   
   function fetchFailed(error, request) {
       alert("lookup failed");
       alert(error);
   }
   
   ifrs.fetch({query: { station: "KOJC" }, onBegin: getStations, onComplete: gotStations, onError: fetchFailed, queryOptions: { deep: true }});
   
    dojo.connect(button2, "onClick", getStations);
    
});