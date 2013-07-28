package electrodynamics.block.item;

import electrodynamics.lib.block.Gas;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockGas extends ItemBlock {

	public ItemBlockGas(int i) {
		super(i);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return Gas.get(itemstack.getItemDamage()).unlocalizedName;
	}
	
}
