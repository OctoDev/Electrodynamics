package electrodynamics.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import electrodynamics.core.CreativeTabED;
import electrodynamics.item.EDItems;
import electrodynamics.lib.core.ModInfo;
import electrodynamics.lib.item.Component;
import electrodynamics.lib.item.ItemIDs;

public class BlockWormwood extends BlockFlower {

	public static final int NORMAL = 0;
	public static final int DRIED = 1;

	private Icon[] textures;

	public BlockWormwood(int id) {
		super(id);
		setCreativeTab(CreativeTabED.resource);
	}

	@Override
	public Icon getIcon(int side, int meta) {
		return textures[meta];
	}

	@Override
	protected boolean canThisPlantGrowOnThisBlockID(int id) {
		return id == Block.sand.blockID || super.canThisPlantGrowOnThisBlockID(id);
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return ItemIDs.ITEM_COMPONENT_ID;
	}

	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		dropBlockAsItem(world, x, y, z, id, meta);
	}
	
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		if (metadata == 1) {
			ret.add(new ItemStack(EDItems.itemComponent, 1, Component.SAP.ordinal()));
		}
		
		ret.add(new ItemStack(EDItems.itemComponent, MathHelper.getRandomIntegerInRange(new Random(), 0, 3), Component.TWINE.ordinal()));
		ret.add(new ItemStack(EDItems.itemComponent, MathHelper.getRandomIntegerInRange(new Random(), 0, 3), Component.WORMWOOD_LEAF.ordinal()));
		
		return ret;
	}

	public void registerIcons(IconRegister icon) {
		textures = new Icon[2];

		textures[0] = icon.registerIcon(ModInfo.ICON_PREFIX + "world/plant/plantWormseed");
		textures[1] = icon.registerIcon(ModInfo.ICON_PREFIX + "world/plant/plantWormseedDried");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(int id, CreativeTabs tab, List list) {
		for (int i=0; i<2; i++) {
			list.add(new ItemStack(id, 1, i));
		}
	}
	
}