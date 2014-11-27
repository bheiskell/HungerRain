HungerRain
==========

Reimplementation of the original (abandoned) [HungerRain](http://dev.bukkit.org/bukkit-plugins/hunger-rain/)
Bukkit MineCraft plugin.

## Features

A player's food level will be depleted at a configured rate in the following circumstances:
- When the player is swimming
- When the player stands in the rain
- When the player stands in the snow and they're under a specific light level (lights keep the player warm)

## Configuration

Configuration of the plugin can be found in the configuration file created upon loading of the plugin.

## Permissions

There are currently no permissions for the plugin.

## Found a bug?

### TL;DR

* Weather rules are defined in the Minecraft Client. If HungerRain is misbehaving, please include the client version.
* This plugin assumes FancyGraphics is on (see Quirks for details). If off, the client may receive rain damage up to
  five blocks above where snowing begins.

## Quirks

This plugin requires heavily upon the implementation details of the Minecraft
client, because most logic regarding rain and snow is client side. The
Minecraft Client was reverse engineered to determine the rules.

This means that if the client changes, this plugin may not behave correctly.

See the [research](RESEARCH.md) documentation for additional information.
