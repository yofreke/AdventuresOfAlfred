package com.yofreke.alfred.level.tile;

import com.yofreke.alfred.level.Level;
import com.yofreke.alfred.tileEntity.TileEntity;

public interface ITileEntityPair {

	public TileEntity getEntity(Level level, int worldX, int worldY);
}
