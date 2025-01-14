package com.thevoxelbox.voxelsniper.brush.type.blend;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.thevoxelbox.voxelsniper.sniper.snipe.Snipe;
import com.thevoxelbox.voxelsniper.sniper.snipe.message.SnipeMessenger;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import com.thevoxelbox.voxelsniper.util.math.MathHelper;
import com.thevoxelbox.voxelsniper.util.painter.Painters;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlendVoxelDiscBrush extends AbstractBlendBrush {

    @Override
    public void handleCommand(String[] parameters, Snipe snipe) {
        SnipeMessenger messenger = snipe.createMessenger();
        String firstParameter = parameters[0];

        if (firstParameter.equalsIgnoreCase("info")) {
            messenger.sendMessage(ChatColor.GOLD + "Blend Voxel Disc Brush Parameters:");
            messenger.sendMessage(ChatColor.AQUA + "/b bvd water -- Toggles include or exclude (default) water.");
            return;
        }
        super.handleCommand(parameters, snipe);
    }

    @Override
    public void blend(Snipe snipe) {
        ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
        int brushSize = toolkitProperties.getBrushSize();
        int squareEdge = 2 * brushSize + 1;
        BlockVector3 targetBlock = getTargetBlock();
        int smallSquareArea = MathHelper.square(squareEdge);
        Set<BlockVector3> smallSquare = new HashSet<>(smallSquareArea);
        Map<BlockVector3, BlockType> smallSquareBlockTypes = new HashMap<>(smallSquareArea);
        Painters.square()
                .center(targetBlock)
                .radius(brushSize)
                .blockSetter(position -> {
                    BlockType type = getBlockType(position);
                    smallSquare.add(position);
                    smallSquareBlockTypes.put(position, type);
                })
                .paint();
        for (BlockVector3 smallSquareBlock : smallSquare) {
            Map<BlockType, Integer> blockTypesFrequencies = new HashMap<>();
            Painters.square()
                    .center(smallSquareBlock)
                    .radius(1)
                    .blockSetter(position -> {
                        if (position.equals(smallSquareBlock)) {
                            return;
                        }
                        BlockType type = getBlockType(position);
                        blockTypesFrequencies.merge(type, 1, Integer::sum);
                    })
                    .paint();
            CommonMaterial commonMaterial = findCommonMaterial(blockTypesFrequencies);
            BlockType type = commonMaterial.getBlockType();
            if (type != null) {
                smallSquareBlockTypes.put(smallSquareBlock, type);
            }
        }
        setBlocks(smallSquareBlockTypes);
    }

}
