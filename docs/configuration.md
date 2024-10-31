Helpful Commands is very flexible in terms of configuration. Everything can be done directly in the game and there is no need to manually tinker with the config file.

## 🔰▪ Basics
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.config</p>
    </div>
    <div style="margin: 0 6px 0 6px">or</div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.config.*</p>
    </div>
</div>
All configuration is done with the `/hc config` command. If you don't have a `config` parameter, it means you don't have permissions to configure the mod.

### What can you do and configure
- Manage commands: toggle their active and public states
- View a list of all configuration fields, query and update their values

### Who can configure the mod
#### On dedicated server
- Anyone with OP level of 4
- Anyone who has permissions to do so, which can be set using any permissions management tool (e.g. [LuckPerms](https://luckperms.net/))

#### On integrated server
For security reasons, only the host can configure the mod. Other players will get an error regardless of their OP level and permissions.

### Config file location
If you need the config file, you can find it here: `(world root directory)/config/(filename).json`

`filename`:

- Since 3.0.0: `helpfulcommands3.json`
- Since 1.0.0: `hcConfig.json`

## 📃▪ Command management
<div class="hc-mic-entry hc-mic-permission" style="margin-right: 10px">
    <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.config.manageCommand</p>
</div>

### Toggle command's active state
This command allows you to enable or disable commands: <br>
`/hc config manageCommand <command name> toggleEnabled [state]`

- If `state` parameter is empty, the command will be toggled (enabled -> disabled; disabled -> enabled)

Disabled commands can't be used by anyone, and they don't appear when you try to type them (no autocomplete)

!!! tip
    You can toggle commands more easily in the Command List (`/hc commandList`): just click on the command name to toggle its active state. Each command in the list is also color-coded: green - enabled, red - disabled. This doesn't apply to server consoles.

### Toggle command's public state
This command allows you to toggle the public state of the command: <br>
`/hc config manageCommand <command name> togglePublic [state]`

- If `state` parameter is empty, the public state will be toggled (public -> restricted; restricted -> public)

!!! tip
    You can toggle the public state more easily in the Command List (`/hc commandList`): just click on the special letter to the left of the command name. Public commands will be highlighted in the list. This doesn't apply to server consoles.

## 🔧▪ Config field management
<div class="hc-mic-entry hc-mic-permission" style="margin-right: 10px">
    <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.config.manageField</p>
</div>

### Query current value
Use this command to see the current value of the config field:<br>
`/hc config manageField <field name> query`


### Update value
Use this command to set the new value of the field:<br>
`/hc config manageField <field name> set <new value>`

!!! tip
    You can see all available fields, their current values and descriptions in one place in the game by simply typing `/hc config` without any other parameters. You can also click on the current value to edit it.

### List of fields
#### explosionPowerLimit
> Default value: `15`

Maximum power limit for /explosion command. Be careful: strong explosions are destructive and can cause lag.

#### jumpDistanceLimit
> Default value: `0`

Maximum distance for the /jump command. Players will not be able to teleport further than this value. If set to 0, there is no limit for the 'distance' argument, and the default formula will be used if the 'distance' argument is empty.

#### killitemsRangeLimit
> Default value: `250`

Maximum range limit for /killitems command.