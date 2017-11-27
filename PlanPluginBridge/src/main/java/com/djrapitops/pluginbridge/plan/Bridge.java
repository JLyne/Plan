package com.djrapitops.pluginbridge.plan;

import com.djrapitops.plugin.api.utility.log.Log;
import com.djrapitops.pluginbridge.plan.advancedachievements.AdvancedAchievementsHook;
import com.djrapitops.pluginbridge.plan.askyblock.ASkyBlockHook;
import com.djrapitops.pluginbridge.plan.banmanager.BanManagerHook;
import com.djrapitops.pluginbridge.plan.essentials.EssentialsHook;
import com.djrapitops.pluginbridge.plan.factions.FactionsHook;
import com.djrapitops.pluginbridge.plan.griefprevention.GriefPreventionHook;
import com.djrapitops.pluginbridge.plan.jobs.JobsHook;
import com.djrapitops.pluginbridge.plan.litebans.LiteBansHook;
import com.djrapitops.pluginbridge.plan.mcmmo.McmmoHook;
import com.djrapitops.pluginbridge.plan.protocolsupport.ProtocolSupportHook;
import com.djrapitops.pluginbridge.plan.redprotect.RedProtectHook;
import com.djrapitops.pluginbridge.plan.superbvote.SuperbVoteHook;
import com.djrapitops.pluginbridge.plan.towny.TownyHook;
import com.djrapitops.pluginbridge.plan.vault.VaultHook;
import com.djrapitops.pluginbridge.plan.viaversion.ViaVersionHook;
import main.java.com.djrapitops.plan.Settings;
import main.java.com.djrapitops.plan.data.additional.HookHandler;

/**
 * Manages connection to other plugins.
 *
 * @author Rsl1122
 * @see AdvancedAchievementsHook
 * @see ASkyBlockHook
 * @see BanManagerHook
 * @see EssentialsHook
 * @see FactionsHook
 * @see GriefPreventionHook
 * @see JobsHook
 * @see LiteBansHook
 * @see McmmoHook
 * @see SuperbVoteHook
 * @see ProtocolSupportHook
 * @see RedProtectHook
 * @see TownyHook
 * @see VaultHook
 * @see ViaVersionHook
 */
@SuppressWarnings("WeakerAccess")
public class Bridge {

    private Bridge() {
        throw new IllegalStateException("Utility class");
    }

    public static void hook(HookHandler h) {
        Hook[] hooks = new Hook[]{
                new AdvancedAchievementsHook(h),
                new ASkyBlockHook(h),
                new BanManagerHook(h),
                new EssentialsHook(h),
                new FactionsHook(h),
                new GriefPreventionHook(h),
                new JobsHook(h),
                new LiteBansHook(h),
                new McmmoHook(h),
                new SuperbVoteHook(h),
                new ProtocolSupportHook(h),
                new RedProtectHook(h),
                new TownyHook(h),
                new VaultHook(h),
                new ViaVersionHook(h)
        };
        for (Hook hook : hooks) {
            try {
                hook.hook();
            } catch (Exception | NoClassDefFoundError e) {
                if (Settings.DEV_MODE.isTrue()) {
                    Log.toLog("PluginBridge", e);
                }
            }
        }
    }
}
