package electrodynamics.world.gen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenFlower implements IWorldGenerator {

	public int flowerID;
	
	public List<BiomeGenBase> validBiomes;
	
	public WorldGenFlower(int id, BiomeGenBase ... validBiomes) {
		this.flowerID = id;
		this.validBiomes = Arrays.asList(validBiomes);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int x = chunkX * random.nextInt(16);
		int y = random.nextInt(256);
		int z = chunkZ * random.nextInt(16);
		
		generateFlowers(world, x, y, z, flowerID, random);
	}

	private void generateFlowers(World world, int x, int y, int z, int blockID, Random random) {
		for (int l = 0; l < 64; ++l) {
			int i1 = x + random.nextInt(8) - random.nextInt(8);
			int j1 = y + random.nextInt(4) - random.nextInt(4);
			int k1 = z + random.nextInt(8) - random.nextInt(8);

			if (world.isAirBlock(i1, j1, k1) && (!world.provider.hasNoSky || j1 < 127) && Block.blocksList[blockID].canBlockStay(world, i1, j1, k1)) {
				if (validBiomes.contains(world.getBiomeGenForCoords(i1, k1))) {
					world.setBlock(i1, j1, k1, blockID, 0, 2);
				}
			}
		}
	}
	
}
