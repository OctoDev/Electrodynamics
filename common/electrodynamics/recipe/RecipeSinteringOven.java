package electrodynamics.recipe;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.util.InventoryUtil;

import net.minecraft.item.ItemStack;

public class RecipeSinteringOven {

	public List<ItemStack> itemInputs;
	public List<ItemStack> itemOutputs;
	
	public int processingTime;

	public float experience = 0.0f;
	
	public RecipeSinteringOven(List<ItemStack> itemInput, List<ItemStack> itemOutputs, int processingTime) {
		this.itemInputs = itemInput;
		this.itemOutputs = itemOutputs;
		this.processingTime = processingTime;
	}
	
	public RecipeSinteringOven(int processingTime) {
		this.processingTime = processingTime;
		this.itemInputs = new ArrayList<ItemStack>();
		this.itemOutputs = new ArrayList<ItemStack>();
		
		setInput(this.itemInputs);
		setOutput(this.itemOutputs);
	}

	public void setInput(List<ItemStack> inputs) {
		
	}
	
	public void setOutput(List<ItemStack> outputs) {

	}

	public void setExperience(float experience) {
		this.experience = experience;
	}

	public float getExperience() {
		return experience;
	}

	public boolean isInput(List<ItemStack> input) {
		for (ItemStack stack : input) {
			if (stack != null) {
				if (InventoryUtil.contains((ItemStack[]) this.itemInputs.toArray(), stack)) {
					return true;
				}
			}
		}
		
		return false;
	}

}
