package electrodynamics.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electrodynamics.api.tool.ToolType;
import electrodynamics.core.CreativeTabED;
import electrodynamics.interfaces.IAcceptsTool;
import electrodynamics.lib.core.Strings;
import electrodynamics.tileentity.TileEntityTable;

public class BlockTable extends BlockContainer implements IAcceptsTool {

	public static String[] subNames = new String[] {Strings.BASIC_TABLE, Strings.SMASH_TABLE};
	
	public Random random = new Random();
	
	public BlockTable(int id) {
		super(id, Material.wood);
		setHardness(1F);
		setCreativeTab(CreativeTabED.block);
		setBlockBounds(0, 0, 0, 1, 0.87F, 1);
	}

	public int damageDropped(int damage) {
		return damage;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntityTable table = (TileEntityTable) world.getBlockTileEntity(x, y, z);

		if (table != null) {
			ItemStack itemstack = table.displayedItem;

			if (itemstack != null) {
				if (itemstack.getItem() instanceof ItemBlock) {
					return Block.lightValue[itemstack.itemID];
				}
			}
		}
		
		return super.getLightValue(world, x, y, z);
	}
	
	public void breakBlock(World world, int x, int y, int z, int i1, int i2) {
		TileEntityTable table = (TileEntityTable) world.getBlockTileEntity(x, y, z);

		if (table != null) {
			table.onBlockBreak();
		}

		super.breakBlock(world, x, y, z, i1, i2);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hX, float hY, float hZ) {
		TileEntityTable table = (TileEntityTable) world.getBlockTileEntity(x, y, z);
		if(table != null) {
			table.onBlockActivated(player);
		}
		
		return true;
	}
	
	@Override
	public Icon getIcon(int side, int meta) {
		return Block.planks.getIcon(0, 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list) {
		for (int i = 0; i < subNames.length; i++) {
			list.add(new ItemStack(id, 1, i));
		}
	}

	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTable();
	}

	public int idPicked(World world, int x, int y, int z) {
		return this.blockID;
	}

	public int getDamageValue(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public boolean accepts(ToolType tool) {
		return tool == ToolType.HAMMER;
	}

	@Override
	public boolean onToolUse(World world, int x, int y, int z, EntityPlayer player, ItemStack stack) {
		TileEntityTable table = (TileEntityTable) world.getBlockTileEntity(x, y, z);
		
		if (table.hasRecipe(stack)) {
			table.handleSmash(player, stack);
			return true;
		}
		
		return false;
	}
	
}
