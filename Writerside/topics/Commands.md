# Commands
there are 2 commands for modifying the config

### `/painting` {collapsible="true"}
general commands for configuring the plugin
#### `/painting config` {collapsible="true"}
##### `/painting config save`
saves the config

##### `/painting config revert`
Discards the current config and loads from the config.yml file

#### `/painting set_region` {collapsible="true"}
set the region that people can place blocks in to paint
```yaml
Parameters:
  Region: required
```
    
#### `/painting get_region` {collapsible="true"}
prints the current region
#### `/painting reset_cooldown` {collapsible="true"}
resets the cooldown for the executor if a player isn't specified
```yaml
Parameters:
  Player: optional
```
#### `/painting reset_all_cooldowns` {collapsible="true"}
resets all players cooldowns

### `/palette` and `/blacklist` {collapsible="true"}
Restrict the blocks available to just a few block or to a whole list
if you only want to restrict the use of a few blocks use the `/blacklist` command 
The blacklist command is almost a 100% copy of the palette just operating opposite the palette

#### `/palette add` {collapsible="true"}
add a block to the palette.
```yaml
Parameters:
  Block: required
```

#### `/palette add_pattern` {collapsible="true"}
add multiple blocks with regex
```yaml
Parameters:
   Regex: required
```

#### `/palette remove` {collapsible="true"}
remove a block from the palette
```yaml
Parameters:
  Block: required
```

#### `/palette list` {collapsible="true"}
Sends the current palette to the user
<note>This might change into an inventory gui at some point</note>

#### `/palette clear` {collapsible="true"}
clears the palette
<warning>THIS WILL CLEAR THE PALETTE, it will not be autosaved to the config but any other changes to the palette will be removed
if you need to revert your changed use <a href="Commands.md" anchor="painting-config-revert">`/painting config revert`</a></warning>

#### `/palette toggle` {collapsible="true"}
toggles the palette on and off
if its enabled, it will restrict the use of blocks to the ones configured previously 
<tip>This is the only command that is not available on the `/blacklist`command</tip>