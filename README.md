# Hex Overpowered

[![Curseforge](https://badges.moddingx.org/curseforge/versions/1173074) ![CurseForge](https://badges.moddingx.org/curseforge/downloads/1173074)](https://www.curseforge.com/minecraft/mc-mods/hexoverpowered)  
[![Modrinth](https://badges.moddingx.org/modrinth/versions/PkhtCPsD) ![Modrinth](https://badges.moddingx.org/modrinth/downloads/PkhtCPsD)](https://modrinth.com/mod/hexoverpowered)

as the name reveals, this mod contains some OP stuff for Hex Casting

[Online HexBook](https://yukkuric.github.io/HexOverpowered)

### WARNING: All features enabled by default, turn off those unwanted before playing, or create feature request issues [HERE](https://github.com/YukkuriC/HexOverpowered/issues) if not configurable

## General

_note: all features that currently can't be turned off are marked with `☆`_

-   displays spell inside casting items (ItemPackagedHex)
-   disables item dropping of certain mishaps
-   re-enable TP for various limited entities
-   Player entity iota (true name) keeps valid even if the player not inside current dimension
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
        -   a simple example using these events in `KubeJS`: [HERE](https://github.com/YukkuriC/hex_playground/blob/main/server_scripts/HexOP_TrainingPersonalMedia.js)
-   (1.20 exclusive) amethyst budding crystal growth accelerator circle
    -   configurable cost, required base media & max accelerate power

---

-   OP spells:

    -   schedule code to be called in certain ticks later

-   OP great spells:
    -   `YJSP's Charge Media`: recharges casting item / circle / wisp / personal media pool to 114514 dust
        -   (id: `hexoverpowered:yjsp_media`, could be set forbidden in base Hex's config)
    -   `Factor Cut`: a pattern to factorize enemy's health as integer with input number, with higher efficiency using prime factors
        -   Usage 1: `living -> int` reads a living entity's health as an integer to be factorized and divided
            -   with no cost `:3`
        -   Usage 2: `living, int -> int` divides it's health with given factor and returns the divided result, with various edge cases
            -   specially destroys target with `<= 1` health for free
            -   if not divisible, attack 1 like a brainsweep mishap with cost without program flow break
            -   prime divisor has constant cost while non-prime has linear scaled cost

---

-   Hexcellular interop
    -   executable PropertyIota
-   Hexal interop
    -   `☆` item handler for mote nexus
        -   supports interaction with hoppers, AE/RS, etc.
        -   optional GUI to display (first 6x9) mote entries

## Forge Exclusive

-   Mekanism interop
    -   `☆` use MekaSuit's battery as media provider
