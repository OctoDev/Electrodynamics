package electrodynamics.network;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import electrodynamics.network.packet.PacketED;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		PacketED packetED = PacketTypeHandler.buildPacket(packet.data);
		packetED.execute(manager, player, FMLCommonHandler.instance().getEffectiveSide());
	}

}
