package net.bl4st.elytradrag;

import net.bl4st.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ElytraDrag implements ModInitializer {

	public static final String MOD_ID = "elytradrag";

	@Override
	public void onInitialize() {
		ModConfig.LoadConfig();
		ServerTickEvents.END_SERVER_TICK.register(this::ElytraDragTick);
	}
	private void ElytraDragTick(MinecraftServer server) {
		var playerList  = server.getPlayerManager().getPlayerList();
		if(playerList.isEmpty())
			return;

		for (ServerPlayerEntity spe : playerList)
		{
			if(spe.isGliding() && spe.isSneaking())
			{
				spe.getJumpBoostVelocityModifier();
				var playerVelocity = spe.getVelocity();
				var playerSpeed = playerVelocity.length() * 20.0f;
				if(playerSpeed > ModConfig.MINIMUM_SPEED)
				{
					var newVelocity = playerVelocity.multiply(1.0f - 0.05f * ModConfig.ELYTRA_DRAG);
					spe.setVelocity(newVelocity);
					spe.velocityModified = true;
				}
				LimitFallDistance(spe);
			}
		}
	}

	/**
	 * Caps the FallDistance when drag is applied and
	 * makes the vertical speed requirement for
	 * fallDistance reset easier to reach
	 */
	private void LimitFallDistance(ServerPlayerEntity player)
	{
		if (player.getVelocity().getY() > -1.0f && player.fallDistance > 1.0f)
			player.fallDistance = 1.0f;
		else if(player.fallDistance > ModConfig.MAXIMUM_FALLDISTANCE)
		{
			player.fallDistance = ModConfig.MAXIMUM_FALLDISTANCE;
		}
	}
}