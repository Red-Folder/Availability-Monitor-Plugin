# Availability Monitor Plugin

## Quick summary
Plugin which provides availability monitoring of resource (such as a website) via a background service.  Results are stored to a DB and can be retrieve by your HTML/ JavaScript application at any point.

## Getting started
The plugin can be installed using the following commands (this assumes you are familiar with the [Cordova Android Getting Started] (http://docs.phonegap.com/en/3.3.0/guide_platforms_android_index.md.html#Android%20Platform%20Guide)):

* cordova create hello com.example.hello "HelloWorld"
* cd hello
* cordova platform add android
* cordova plugin add https://github.com/Red-Folder/Availability-Monitor-Plugin.git
* Amend the hello\www\config.xml, replace any existing content node with: `<content src="availabilityMonitor.html" />` (add the content node if not already present)
* cordova build

You should be able to run this example.  To use the example:

* Create the monitor, for example:
** Name: "Test"
** Frequency: 1 (this is in minutes)
** Type: HTTP
** Notify When Down: Unchecked
** Notify When Up: Unchecked
** Unavailable Threshold: 10
** URL: "http://www.google.com"
** Click Update
* Start the Monitor Service by clicking on the Toggle

The background service will start monitoring the availability of www.google.com.  This data will be saved into a database, with the results available for later use.

Further documentation can be found at https://github.com/Red-Folder/Availability-Monitor-Plugin/wiki

## Development
This is a very early version of the plugin and is under active development.

Please see the [Enhancements List] (https://github.com/Red-Folder/Availability-Monitor-Plugin/issues?labels=enhancement&page=1&state=open) for the planned changes. 

## Questions and Support
If you have problems, then please log an issue: https://github.com/Red-Folder/Availability-Monitor-Plugin/issues?state=open

As the plugin is updated (for new features or fixes) I will tweet via @FolderRed and blog http://red-folder.blogspot.co.uk/

## Spread the love

If you find the Background Service Plugin useful then spread the love.

All the work I do on the Plugin is done in my spare time - time I would otherwise be spending taking my wife out for a nice meal or helping my lad find vinyl records (he's currently very much into The Smiths, Fleetwood Mac and Kate Bush).

The Plugin is free and will always remain free. I will continue to develop, maintain and distribute the Plugin under the MIT License.

https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=E64TCFQ3NLHZ8

## Licence
Copyright 2013 Red Folder Consultancy Ltd
    
Licensed under the Apache License, Version 2.0 (the "License");   
you may not use this file except in compliance with the License.   
You may obtain a copy of the License at       
  
http://www.apache.org/licenses/LICENSE-2.0   
 
Unless required by applicable law or agreed to in writing, software   
distributed under the License is distributed on an "AS IS" BASIS,   
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
See the License for the specific language governing permissions and   
limitations under the License.