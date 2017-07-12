package etheric.common.network;

import java.util.UUID;

import etheric.common.world.stability.StabilityHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageStabilityRequest implements IMessage {
	
	// block coordinates, not chunk coordinates
	int x = 0, z = 0;
	UUID id = null;
	
	
	public MessageStabilityRequest() {
		
	}
	
	public MessageStabilityRequest(int x, int z, UUID id) {
		this.x = x;
		this.z = z;
		this.id = id;
	}
	
	public MessageStabilityRequest(BlockPos pos, UUID id) {
		this(pos.getX(), pos.getZ(), id);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		z = buf.readInt();
		id = new UUID(buf.readLong(), buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(z);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}
	
	public static class MessageHolder implements IMessageHandler<MessageStabilityRequest, IMessage> {

		@Override
		public IMessage onMessage(MessageStabilityRequest message, MessageContext ctx) {
			EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.id);
			float stability = StabilityHandler.getChunkData(player.dimension, new ChunkPos(new BlockPos(message.x, 0, message.z)));
			
			return new MessageStabilityData(stability);
		}
		
	}

}
