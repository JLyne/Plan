package com.djrapitops.plan.modules.velocity;

import com.djrapitops.plan.gathering.events.PlayerJoinEventConsumer;
import com.djrapitops.plan.gathering.events.PlayerLeaveEventConsumer;
import com.djrapitops.plan.gathering.listeners.velocity.SuperVanishBridgeListener;
import com.djrapitops.plan.identification.ServerInfo;
import com.djrapitops.plan.utilities.logging.ErrorLogger;
import dagger.Module;
import dagger.Provides;
import uk.co.notnull.supervanishbridge.helper.SuperVanishBridgeHelper;

import javax.annotation.Nullable;

@Module
public class VelocitySuperVanishBridgeModule {

	@Provides
	@Nullable
	SuperVanishBridgeListener provideListener(
			PlayerJoinEventConsumer joinEventConsumer,
			PlayerLeaveEventConsumer leaveEventConsumer,
			ServerInfo serverInfo,
			ErrorLogger errorLogger) {
		var ref = new Object() {
			SuperVanishBridgeListener listener = null;
		};

		SuperVanishBridgeHelper instance = SuperVanishBridgeHelper.getInstance();


		if(instance == null) {
			return null;
		}

		instance.ifLoaded(handler -> {
			ref.listener = new SuperVanishBridgeListener(joinEventConsumer, leaveEventConsumer, serverInfo, errorLogger);
			return null;
		});
		
		return ref.listener;
	}
}
