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
package com.djrapitops.plan.gathering.listeners.velocity;

import com.djrapitops.plan.gathering.domain.VelocityPlayerData;
import com.djrapitops.plan.gathering.domain.event.PlayerJoin;
import com.djrapitops.plan.gathering.domain.event.PlayerLeave;
import com.djrapitops.plan.gathering.events.PlayerJoinEventConsumer;
import com.djrapitops.plan.gathering.events.PlayerLeaveEventConsumer;
import com.djrapitops.plan.identification.Server;
import com.djrapitops.plan.identification.ServerInfo;
import com.djrapitops.plan.utilities.logging.ErrorContext;
import com.djrapitops.plan.utilities.logging.ErrorLogger;
import com.velocitypowered.api.event.Subscribe;
import uk.co.notnull.supervanishbridge.api.VanishStateChangeEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SuperVanishBridgeListener {
	PlayerJoinEventConsumer joinEventConsumer;
	PlayerLeaveEventConsumer leaveEventConsumer;
	ServerInfo serverInfo;
	ErrorLogger errorLogger;

	@Inject
	public SuperVanishBridgeListener(
			PlayerJoinEventConsumer joinEventConsumer,
            PlayerLeaveEventConsumer leaveEventConsumer,
            ServerInfo serverInfo,
            ErrorLogger errorLogger
    ) {
        this.joinEventConsumer = joinEventConsumer;
        this.leaveEventConsumer = leaveEventConsumer;
        this.serverInfo = serverInfo;
        this.errorLogger = errorLogger;
	}

	@Subscribe
	public void onVanishStateChange(VanishStateChangeEvent event) {
		Server server = serverInfo.getServer();
		VelocityPlayerData player = new VelocityPlayerData(event.getPlayer());
		long time = System.currentTimeMillis();

		try {
			if(event.isVanishing()) {
				leaveEventConsumer.onLeaveProxyServer(
						PlayerLeave.builder()
								.server(server)
								.player(player)
								.time(time)
								.build());
			} else {
				joinEventConsumer.onJoinProxyServer(
						PlayerJoin.builder()
								.server(server)
								.player(player)
								.time(time)
								.build());
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorLogger.error(e, ErrorContext.builder().related(event).build());
		}
	}
}
