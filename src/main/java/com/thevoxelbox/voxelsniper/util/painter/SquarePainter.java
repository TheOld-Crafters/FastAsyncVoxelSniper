package com.thevoxelbox.voxelsniper.util.painter;

import com.sk89q.worldedit.math.BlockVector3;
import com.thevoxelbox.voxelsniper.util.Vectors;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class SquarePainter implements Painter {

    private BlockVector3 center;
    private int radius;
    private BlockSetter blockSetter;

    public SquarePainter center(Block block) {
        BlockVector3 center = Vectors.of(block);
        return center(center);
    }

    public SquarePainter center(Location location) {
        BlockVector3 center = Vectors.of(location);
        return center(center);
    }

    public SquarePainter center(BlockVector3 center) {
        this.center = center;
        return this;
    }

    public SquarePainter radius(int radius) {
        this.radius = radius;
        return this;
    }

    public SquarePainter blockSetter(BlockSetter blockSetter) {
        this.blockSetter = blockSetter;
        return this;
    }

    @Override
    public void paint() {
        if (this.center == null) {
            throw new RuntimeException("Center must be specified");
        }
        if (this.blockSetter == null) {
            throw new RuntimeException("Block setter must be specified");
        }
        paintCube();
    }

    private void paintCube() {
        Painters.block(this)
                .at(0, 0, 0)
                .paint();
        for (int first = 1; first <= this.radius; first++) {
            Painters.block(this)
                    .at(first, 0, 0)
                    .at(-first, 0, 0)
                    .at(0, 0, first)
                    .at(0, 0, -first)
                    .paint();
            for (int second = 1; second <= this.radius; second++) {
                Painters.block(this)
                        .at(first, 0, second)
                        .at(first, 0, -second)
                        .at(-first, 0, second)
                        .at(-first, 0, -second)
                        .paint();
            }
        }
    }

    @Override
    public BlockVector3 getCenter() {
        return this.center;
    }

    public int getRadius() {
        return this.radius;
    }

    @Override
    public BlockSetter getBlockSetter() {
        return this.blockSetter;
    }

}
