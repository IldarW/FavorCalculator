# Custom  mod for Wurm Unlimited (Client)

Requires [Ago's Client Mod Launcher](https://github.com/ago1024/WurmClientModLauncher/releases) to run.  
This mod is free software: you can redistribute it and/or modify it under the terms of the [GNU Lesser General Public License](http://www.gnu.org/licenses/lgpl-3.0.en.html) as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.  

**Description**:  
This mod will estimate the favor you would get for items if you sacrifice them. The estimate is based on a simple formula from [this page](https://www.wurmpedia.com/index.php/Base_Price#Favor)

**Setup instructions**:  
Install mod following [instructions](https://forum.wurmonline.com/index.php?/topic/134945-released-client-mod-loader) of Ago's Client Mod Launcher

**How to use**:  
First thing you need to do is to choose a convenient key and bind the command "favor" for that key
>_bind z "favor"_  

Now select any number of items in inventory, hover the mouse over the inventory and press the binded key. 
The mod will show you the favor you will get for that items.  
To set the item constant value type the following:  
>_favor setfactor value(float number)_  

**Notes**:  
* There is a certain constant for each item in the game that directly affects the amount of favor you will get for sacrificing it. 
For example the cordage rope have the constant value equal to the 1 and the door lock have the value 0.5. 
So door locks will give you twice less favor than cordage ropes of same quality. 
* By default mod calculates the favor, assuming that the value constant is 1
* You can configure that value this way  
>_favor setfactor 0.5_  

and next favor estimates will be divided by 2
* The spindles have the value equal to the 0.5, but Vynorians have double favor for wood items so you don't need to change the default value in this situation
* You can calculate favor not only in your inventory but in other containers too
