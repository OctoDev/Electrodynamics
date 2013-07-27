package electrodynamics.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import electrodynamics.core.CreativeTabED;
import electrodynamics.core.EDLogger;
import electrodynamics.interfaces.IClientDisplay;
import electrodynamics.lib.block.EnergyProduction;
import electrodynamics.lib.block.Machine;
import electrodynamics.tileentity.TileEntityEDRoot;
import electrodynamics.tileentity.machine.TileEntityMachine;
import electrodynamics.tileentity.structure.TileEntityStructure;
import electrodynamics.util.PlayerUtil;

public class BlockEnergy extends BlockContainer {
	
	public Icon[] textures;

	public BlockEnergy(int id) {
		super(id, Material.iron);
		setHardness(1F);
		setCreativeTab(CreativeTabED.block);
	}

	public int damageDropped(int damage) {
		return damage;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if (tile != null && tile instanceof TileEntityEDRoot) {
			((TileEntityEDRoot)tile).onNeighborUpdate();
		}
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		try {
			return EnergyProduction.values()[meta].tileEntity.newInstance();
		} catch (Exception ex) {
			EDLogger.warn("Failed to create TileEntity for machine " + Machine.values()[meta].unlocalizedName);
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list) {
		for (EnergyProduction machine : EnergyProduction.values()) {
			list.add(new ItemStack(id, 1, machine.ordinal()));
		}
	}
	
	/* IGNORE */
	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public void registerIcons(IconRegister registry) {
		textures = new Icon[EnergyProduction.values().length];
		for (int i = 0; i < EnergyProduction.values().length; i++) {
			textures[i] = registry.registerIcon(EnergyProduction.get(i).getTextureFile());
		}
	}
	
	@Override
	public Icon getIcon(int id, int meta) {
		return textures[meta];
	}
	
	public int idPicked(World world, int x, int y, int z) {
		return this.blockID;
	}

	public int getDamageValue(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
	
}
