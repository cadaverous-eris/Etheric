package etheric.common.network;

import etheric.client.handler.SeeingStoneHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageStabilityData implements IMessage {
	
	float stability = 0F;
	
	public MessageStabilityData() {
		
	}
	
	public MessageStabilityData(float stability) {
		this.stability = stability;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		stability = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(stability);
	}
	
	public static class MessageHolder implements IMessageHandler<MessageStabilityData, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(MessageStabilityData message, MessageContext ctx) {
			SeeingStoneHandler.stabilityDisplayValue = message.stability;
			return null;
		}
		
	}

}
