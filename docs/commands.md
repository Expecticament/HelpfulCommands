## 🗳️▪ Parameters
Commands have parameters (arguments) that you may need to specify for them to work.

* `[param]`: optional parameter
* `<param>`: required parameter

## 📜▪ Command List
Browse all the commands by categories:material-information:{ title="Categories were introduced in Helpful Commands v. 3.0.0" }. See information such as description, parameters, and permissions:material-information:{ title="Fabric Permissions API support was introduced in Helpful Commands v. 3.0.0" } required to use the command.

### Abilities
#### /fly
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/2.0.0">2.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.abilities.fly</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Toggle flying for target(s)
    
    ``` { .yaml .no-copy }
    /fly [target(s)] [state]
    ```

    ??? abstract "Parameters"
        `target(s)`: player(s)
        
        `state`: boolean(true/false). If not specified, flying will be toggled for each target (can fly -> can't fly; can't fly -> can fly)

#### /god
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/2.0.0">2.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.abilities.god</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Toggle invulnerability for target(s)

    ``` { .yaml .no-copy }
    /god [target(s)] [state]
    ```

    ??? abstract "Parameters"
        `target(s)`: player(s)
        
        `state`: boolean(true/false). If not specified, invulnerability will be toggled for each target (invulnerable -> vulnerable; vulnerable -> invulnerable)

### Entities and Players
#### /extinguish
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.entities.extinguish</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Extinguish target(s)

    ``` { .yaml .no-copy }
    /extinguish [target(s)]
    ```

    ??? abstract "Parameters"
        `target(s)`: entity(-ies)

#### /feed
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.entities.feed</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Feed and saturate target(s)

    ``` { .yaml .no-copy }
    /feed [target(s)]
    ```

    ??? abstract "Parameters"
        `target(s)`: player(s)

#### /gm
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.entities.gm</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Change game mode for target(s)

    ``` { .yaml .no-copy }
    /gm <gameMode> [target(s)]
    ```

    ??? abstract "Parameters"
        `gameMode`:
        
        * `a`: adventure
        * `c`: creative
        * `s`: survival
        * `sp`: spectator

        `target(s)`: player(s)

#### /heal
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.entities.heal</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Restore health for target(s)

    ``` { .yaml .no-copy }
    /heal [target(s)]
    ```

    ??? abstract "Parameters"
        `target(s)`: player(s)

#### /ignite
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.entities.ignite</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Set target(s) on fire for a specified number of seconds

    ``` { .yaml .no-copy }
    /ignite <target(s)> <duration>
    ```

    ??? abstract "Parameters"
        `target(s)`: entity(-ies)
        
        `duration`: time to burn (in seconds)

### Teleportation
#### /back
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.teleportation.back</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Get or teleport to your last death position
    
    ``` { .yaml .no-copy }
    /back [action]
    ```

    ??? abstract "Parameters"
        `action`:

        * `get`: print the coordinates to chat
        * `tp`: teleport to the position

#### /dimension
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.teleportation.dimension</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Get or switch dimension for target(s)
    
    ``` { .yaml .no-copy }
    /dimension get [target(s)]
    /dimension set [newDimension] [target(s)]
    ```

    ??? abstract "Parameters"
        `newDimension`: dimension to teleport to
        
        `target(s)`: entity(-ies)

#### /home
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.teleportation.home</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Set, get, or teleport to your Home position

    ``` { .yaml .no-copy }
    /home [action]
    ```

    ??? abstract "Parameters"
        `action`:
        
        - `set`: update the home position
        - `get`: print the coordinates to chat
        - `tp`: teleport to the position

#### /jump
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.teleportation.jump</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Teleport to the block you are looking at, or in the direction of your cursor at a given distance

    ``` { .yaml .no-copy }
    /jump [distance] [checkForBlocks]
    ```

    ??? abstract "Parameters"
        `distance`: how far you will be teleported. If not specified, you will be teleported to the block you are looking at.

        `checkForBlocks`: boolean(true/false). If `true` and `distance` is given, it will check for blocks in the way; if there are, it will teleport you to the block, even if the given `distance` is greater than the final teleport distance. The default is `false`, which means it will just teleport you forward at the specified distance.

#### /spawn
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.teleportation.spawn</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Teleport to your or the world's spawn point

    ``` { .yaml .no-copy }
    /spawn <type> [action]
    ```

    ??? abstract "Parameters"
        `type`:

        * `player`: your own spawn point
        * `world`: world spawn

        `action`:
        
        * `get`: print the coordinates to chat
        * `tp`: teleport to the spawn

### Time
#### /day
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.time.day</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Set the time to Day

    ``` { .yaml .no-copy }
    /day
    ```

#### /night
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.time.night</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Set the time to Night

    ``` { .yaml .no-copy }
    /night
    ```

### Utility
#### /rename
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/3.0.0">3.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.utility.rename</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Rename an item in your main hand

    ``` { .yaml .no-copy }
    /rename [newName]
    ```

    ??? abstract "Parameters"
        `newName`: new name for the item. If not specified, a set custom name will be removed.

#### /repair
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/3.0.0">3.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.utility.repair</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Repair an item in the main hand of the target(s)

    ``` { .yaml .no-copy }
    /repair [target(s)]
    ```

    ??? abstract "Parameters"
        `target(s)`: player(s)

### World
#### /explosion
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.world.explosion</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Create an explosion at the block you are looking at, or in the direction of your cursor at a given distance with a given power

    ``` { .yaml .no-copy }
    /explosion <power> [distance]
    ```

    ??? abstract "Parameters"
        `power`: how powerful the explosion will be

        `distance`: how far away from you the explosion will be created. If not specified, it will be created near the block you are looking at.

#### /killitems
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.world.killitems</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Kill all the items lying on the ground nearby

    ``` { .yaml .no-copy }
    /killitems
    ```
#### /lightning
<div class="hc-mic-holder">
    <div class="hc-mic-entry hc-mic-version">
        <div class="hc-mic-logo"><span class="twemoji" title="Mod version"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M5.5 7A1.5 1.5 0 0 1 4 5.5 1.5 1.5 0 0 1 5.5 4 1.5 1.5 0 0 1 7 5.5 1.5 1.5 0 0 1 5.5 7m15.91 4.58-9-9C12.05 2.22 11.55 2 11 2H4c-1.11 0-2 .89-2 2v7c0 .55.22 1.05.59 1.41l8.99 9c.37.36.87.59 1.42.59.55 0 1.05-.23 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.56-.23-1.06-.59-1.42Z"></path></svg></span></div><p class="hc-mic-text"><a href="https://github.com/Expecticament/HelpfulCommands/releases/tag/1.0.1">1.0.0</a></p>
    </div>
    <div class="hc-mic-entry hc-mic-permission">
        <div class="hc-mic-logo"><span class="twemoji" title="Permission ID"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M22 4h-8v3h-4V4H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2M8 9a2 2 0 0 1 2 2 2 2 0 0 1-2 2 2 2 0 0 1-2-2 2 2 0 0 1 2-2m4 8H4v-1c0-1.33 2.67-2 4-2s4 .67 4 2v1m8 1h-6v-2h6v2m0-4h-6v-2h6v2m0-4h-6V8h6v2m-7-4h-2V2h2v4Z"></path></svg></span></div><p class="hc-mic-text">helpfulcommands.command.world.lightning</p>
    </div>
</div>
???+ hc-command "Command Info & Usage"
    > Strike a lightning bolt at the block you are looking at, or in the direction of your cursor at a given distance

    ``` { .yaml .no-copy }
    /lightning [distance]
    ```

    ??? abstract "Parameters"
        `distance`: how far away from you the lightning will spawn. If not specified, it will spawn near the block you are looking at.