/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thevoxelbox.voxelsniper.brush.performer.type;

import com.thevoxelbox.voxelsniper.Messages;
import com.thevoxelbox.voxelsniper.sniper.SnipeData;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class InkComboPerformer extends AbstractPerformer {

	private BlockData blockData;
	private BlockData replaceBlockData;

	public InkComboPerformer() {
		super("Ink-Combo");
	}

	@Override
	public void init(SnipeData snipeData) {
		this.world = snipeData.getWorld();
		this.blockData = snipeData.getBlockData();
		this.replaceBlockData = snipeData.getReplaceBlockData();
	}

	@Override
	public void info(Messages messages) {
		messages.performerName(this.getName());
		messages.replaceBlockDataType();
		messages.blockData();
		messages.replaceBlockData();
	}

	@Override
	public void perform(Block block) {
		BlockData blockData = block.getBlockData();
		if (blockData.equals(this.replaceBlockData)) {
			this.undo.put(block);
			block.setBlockData(this.blockData);
		}
	}

	@Override
	public boolean isUsingReplaceMaterial() {
		return true;
	}
}
