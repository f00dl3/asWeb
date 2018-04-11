/* 
by Anthony Stump
Created: 10 Apr 2018

This will be a file that is a stub for new way of rendering Leaflet w/ the new API version using REST.

Specifications:
    - Determine the Leaflet "Map View" in the window, max/min LAT/LON coords.
    - run Database query based on stations in the Map View - return only stations via REST call that fit the view
    - render the station data per old method but only for those stations
    - set on mouse move listener to detect new viewport and re-run query pulling stations in that view.
 */


