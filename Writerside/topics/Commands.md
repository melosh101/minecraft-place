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
parameters:
  Region: required
```
    
#### `/painting get_region` {collapsible="true"}
prints the current region
#### `/painting reset_cooldown` {collapsible="true"}
resets the cooldown for the executor if a player isn't specified
```yaml
parameters:
  Player: Optional
```
#### `/painting reset_all_cooldowns` {collapsible="true"}
resets all players cooldowns

### `/palette` {collapsible="true"}
#### `/palette add` {collapsible="true"}
add a block to the palette
```yaml
Parameters:
  Block: required
```
#### `/palette add_pattern`
add blocks via regex
```yaml
Parameters:
   
```
#### `/palette remove`
remove a block from the palette
```yaml
Parameters:
  Block: required
```

