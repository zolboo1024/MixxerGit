# Web View in Android with Safeguards
A simple implementation of WebView that doesn't create infinite loops of errors when there's an exception
This repository is for anyone who are looking to create an Android that contains a simple browser 
with no ugly and unnecessary buttons on the top. I've noticed that the best way to create that browser is to use 
WebView. And here's an example of WebView in action.

GO to Browser.java to see how Android WebView component is implemented. It also contains safeguards 
against when things go wrong like if the device has no internet connection or if the URL doesn't 
exist, etc. 

GO to MainActivity.java to see an example of how to call an activity that contains the WebView component(Browser.java)
in this case and pass the URL programmatically.

Hope you find something useful here! Cheers!
