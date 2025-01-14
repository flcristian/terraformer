# Terraformer - Minecraft World Editing Plugin

- _Join the Discord server, to see the current progress on the mod!_ [Discord Server Link]([https://discord.gg/FPPJsk3QJN](https://discord.gg/FPPJsk3QJN))

- **I will come up with videos on how to abuse this plugin as much as you can, the logic each brush has, tutorials and such once I have a version I'm happy with.**

- _I was annoyed of how the current mods work, so I wanted to create an all in one terraforming tool for minecraft builders.
My focus is on making this tool as easy to use as possible, as well as extremely powerfull and with no limitations.
I wish to implement a wide range of new features, including customizable brushes, procedural generation and advanced material application tools, so stay updated!_

## Contact

- Questions - Discord: ruskizeu
            - Or join the Discord server! [Discord Server Link]([https://discord.gg/FPPJsk3QJN](https://discord.gg/FPPJsk3QJN))
- Issues - [GitHub](https://github.com/flcristian/terraformer/issues)

## Commands

- `/terraformer start` or `/tf start` - Start terraforming mode
- `/terraformer stop` or `/tf stop` - Stop terraforming mode
- `/terraformer undo` or `/tf undo` - Undo last modification
- `/terraformer redo` or `/tf redo` - Redo last modification
- `/terraformer brush <type>` or `/tf b <type>` - Set brush type
- `/terraformer size <1-9>` or `/tf s <1-9>` - Set brush size
- `/terraformer depth <1-20>` or `/tf d <1-20>` - Set brush depth (for Paint/Rise/Dig brushes)
- `/terraformer materials <materials>` or `/tf m <materials>` - Set brush materials
- `/terraformer materialmode <mode>` or `/tf mm <mode>` - Set material mode
- `/terraformer materialmodes` - Show all material modes
- `/terraformer brushes` - Show all brush types

## Brushes

### Basic Brushes
- **Ball** - Creates a spherical shape
- **Smooth** - Smooths terrain by averaging nearby blocks
- **Erode** - Erodes terrain by removing protruding blocks
- **Extrude** - Extrudes terrain outward based on surrounding blocks
- **Rise** - Raises terrain by specified depth
- **Dig** - Removes blocks down to specified depth

### Paint Brushes
- **Paint Top** - Paints blocks from top down
- **Paint Surface** - Paints only surface blocks
- **Paint Wall** - Paints vertical surfaces
- **Paint Bottom** - Paints blocks from bottom up

## Material Modes

### Random Mode
- Randomly selects materials based on specified percentages
- Format: `/tf m 70%stone,30%dirt`
- Materials must add up to 100%

### Layer Mode
- Places materials in layers
- Order of materials determines layer order from bottom to top
- Format: `/tf m stone,dirt,grass_block`

### Gradient Mode
- Creates smooth transitions between materials
- Position in list determines gradient position (0% to 100%)
- Format: `/tf m white_concrete,gray_concrete,black_concrete`
- Best used with color-compatible blocks

## Usage Examples

### 1. Basic Sphere Brush
```
/tf start
/tf b ball
/tf s 5
/tf m stone
```

### 2. Gradient Wall
```
/tf b paintwall
/tf mm gradient
/tf d 10
/tf m white_concrete,light_blue_concrete,blue_concrete
```

### 3. Random Terrain
```
/tf b ball
/tf mm random
/tf m 50%stone,30%dirt,20%grass_block
```

## Brush Settings Menu

Access the brush settings menu by right-clicking with the brush tool. The menu allows you to:

- Change brush size (1-9)
- Change brush depth (1-20) for applicable brushes
- Select brush type
- Change material mode
- Add/remove materials
- Adjust material percentages

## Permissions

- **terraformer.use** - Access to basic commands (default: true)
- **terraformer.mode** - Permission to enter terraform mode (default: op)

## License

This mod is released under the GNU AFFERO GENERAL PUBLIC LICENSE Version 3.