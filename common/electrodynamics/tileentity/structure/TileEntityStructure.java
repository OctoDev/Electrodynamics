package electrodynamics.tileentity.structure;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electrodynamics.interfaces.IRedstoneUser;
import electrodynamics.mbs.MBSManager;
import electrodynamics.mbs.MultiBlockStructure;
import electrodynamics.network.PacketUtils;
import electrodynamics.network.packet.PacketClientData;
import electrodynamics.tileentity.TileEntityGeneric;

public abstract class TileEntityStructure extends TileEntityGeneric {

	// The coordinates of the central TE.
	protected int targetX, targetY, targetZ;

	// Whether if this TE is part of the structure.
	protected boolean isValidStructure = false;

	// The orientation of the MBS as a whole (relative to the MBS's pattern design).
	protected int rotation;

	// stores the ID of the current MBS this TE is a part of
	protected String mbsID = "";

	public void validateStructure(MultiBlockStructure multiBlockStructure, int rotation, int x, int y, int z) {
		this.rotation = rotation;
		this.targetX = x;
		this.targetY = y;
		this.targetZ = z;
		this.isValidStructure = true;
		this.mbsID = multiBlockStructure.getUID();
	}

	public void invalidateStructure() {
		isValidStructure = false;
		mbsID = "";
	}

	public boolean isValidStructure() {
		return isValidStructure;
	}

	public boolean isCentralTileEntity() {
		return isValidStructure() && xCoord == targetX && yCoord == targetY && zCoord == targetZ;
	}

	public TileEntityStructure getCentralTileEntity() {
		if( isValidStructure ) {
			if( this.isCentralTileEntity() ) {
				return this;
			}
			return (TileEntityStructure) worldObj.getBlockTileEntity( targetX, targetY, targetZ );
		}
		return null;
	}

	public MultiBlockStructure getMBS() {
		if( !mbsID.equals( "" ) ) {
			return MBSManager.getMultiBlockStructure( mbsID );
		}
		return null;
	}

	public int getRotation() {
		return rotation;
	}

	public abstract boolean onBlockActivatedBy(EntityPlayer player, int side, float xOff, float yOff, float zOff);

	public void onBlockUpdate() {}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT( nbt );
		NBTTagCompound tag = nbt.getCompoundTag( "structure" );
		isValidStructure = tag.getBoolean( "isPart" );
		targetX = tag.getInteger( "targetX" );
		targetY = tag.getInteger( "targetY" );
		targetZ = tag.getInteger( "targetZ" );
		rotation = tag.getInteger( "rotation" );
		mbsID = tag.getString( "mbsID" );
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT( nbt );
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean( "isPart", isValidStructure );
		tag.setInteger( "targetX", targetX );
		tag.setInteger( "targetY", targetY );
		tag.setInteger( "targetZ", targetZ );
		tag.setInteger( "rotation", rotation );
		tag.setString( "mbsID", mbsID );
		nbt.setTag( "structure", tag );
	}

	@SuppressWarnings("static-access")
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return this.INFINITE_EXTENT_AABB;
    }
	
	public void sendDataToClient(NBTTagCompound nbt) {
		PacketClientData packet = new PacketClientData(xCoord, yCoord, zCoord, nbt);
		PacketUtils.sendToPlayers(packet.makePacket(), this);
	}
	
	public static TileEntityStructure createNewPlaceHolderTE() {
		return new TileStructurePlaceHolder();
	}

	public static class TileStructurePlaceHolder extends TileEntityStructure {

		public NBTTagCompound clientNBT;
		
		public void readClientData(NBTTagCompound nbt) {
			this.clientNBT = nbt;
		}
		
		@Override
		public boolean onBlockActivatedBy(EntityPlayer player, int side, float xOff, float yOff, float zOff) {
			return false;
		}

	}

}
