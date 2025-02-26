# Place
This plugin was inspired by a conversation on [legundo's](https://twitch.tv/legundo) subathon february 2025

You are free to use and modify this plugin to your liking if you want.

<procedure title="setup" id="setup-plugin">
    <step>Install this plugin and its dependencies worldguard and worldedit</step>
    <step>Make a flat area you want to be the painting</step>
    <step>Make a 1 block high worldguard region on top of the newly created area, default region is "painting". (the regions name can be configured in the config or <a href="Commands.md" anchor="painting-set-region">`/painting set_region region`</a>)</step>
    <step>make sure everyone that should be able to set blocks can with flags</step>
    <p>
        And that should be all that's needed. <br/>
        you might want to restrict the usage of some blocks like fences and other non-full blocks. refer to <a href="Commands.md" anchor="palette-and-blacklist"></a> 
    </p>
</procedure>

## Features {id=features}
### This plugins config can be fully modified and changed though the command line {collapsible="true"}

look at the [](Commands.md) section of the docs
### A palette configured via the config 
### A blacklist for when you just have more blocks to block than add (both work at the same time)