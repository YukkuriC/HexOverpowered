# Hex Overpowered

[![Curseforge](https://badges.moddingx.org/curseforge/versions/1173074) ![CurseForge](https://badges.moddingx.org/curseforge/downloads/1173074)](https://www.curseforge.com/minecraft/mc-mods/hexoverpowered)  
[![Modrinth](https://badges.moddingx.org/modrinth/versions/PkhtCPsD) ![Modrinth](https://badges.moddingx.org/modrinth/downloads/PkhtCPsD)](https://modrinth.com/mod/hexoverpowered)

as the name reveals, this mod contains some OP stuff for Hex Casting

### WARNING: All features enabled by default, turn off those unwanted before playing, or create feature request issues [HERE](https://github.com/YukkuriC/HexOverpowered/issues) if not configurable

## General

_note: all features that currently can't be turned off are marked with `☆`_

-   displays spell inside casting items (ItemPackagedHex)
-   disables item dropping of certain mishaps
-   re-enable TP for various limited entities
-   a set of spells manipulating player's staff environment (mind stack)
    -   pushing/poping iota between current context and mind stack
    -   getting the size of mind stack
    -   getting all patterns drawn inside the staff canvas
-   extra player mana(media) bar
    -   `☆` has max priority
    -   `☆` a pair of patterns to query player's current/max personal media amount
    -   configurable max value & regeneration
    -   attribute driven, can be adjusted by modifiers, commands, etc.
    -   active after player is enlightened, or any time, depends on config
    -   fake players don't regenerate media by default; can be turned on
    -   triggers events when consuming/regenerating personal media pool
        -   event handlers inside class `io.yukkuric.hexop.personal_mana.PersonalManaEvents`
        -   also registered as global access `PersonalManaEvents` & `PersonalMediaEvents` inside startup & server script scopes if `KubeJS` installed
        -   `TODO` kjs example script links
-   (1.20 exclusive) amethyst budding crystal growth accelerator circle
    -   configurable cost, required base media & max accelerate power

---

-   OP great spells:
    -   `YJSP's Charge Media`: recharges casting item / circle / wisp / personal media pool to 114514 dust
        -   (id: `hexoverpowered:yjsp_media`, could be set forbidden in base Hex's config)

---

-   Hexal interop
    -   `☆` item handler for mote nexus
        -   `☆` supports interaction with hoppers, AE/RS, etc.
        -   optional GUI to display (first 6x9) mote entries; can be disabled in config

## Forge Exclusive

-   Mekanism interop
    -   `☆` use MekaSuit's battery as media provider
