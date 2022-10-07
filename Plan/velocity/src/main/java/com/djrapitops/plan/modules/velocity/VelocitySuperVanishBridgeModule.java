/*
 *  This file is part of Player Analytics (Plan).
 *
 *  Plan is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License v3 as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Plan. If not, see <https://www.gnu.org/licenses/>.
 */
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
