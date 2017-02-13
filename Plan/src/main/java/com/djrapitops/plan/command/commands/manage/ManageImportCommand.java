package main.java.com.djrapitops.plan.command.commands.manage;

import com.djrapitops.plan.Phrase;
import com.djrapitops.plan.Plan;
import com.djrapitops.plan.command.CommandType;
import com.djrapitops.plan.command.SubCommand;
import com.djrapitops.plan.data.UserData;
import com.djrapitops.plan.data.cache.DataCacheHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import main.java.com.djrapitops.plan.data.importing.Importer;
import main.java.com.djrapitops.plan.data.importing.OnTimeImporter;
import org.bukkit.Bukkit;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getOfflinePlayer;

/**
 *
 * @author Rsl1122
 */
public class ManageImportCommand extends SubCommand {

    private final Plan plugin;

    /**
     * Class Constructor.
     *
     * @param plugin Current instance of Plan
     */
    public ManageImportCommand(Plan plugin) {
        super("import", "plan.manage", Phrase.CMD_USG_MANAGE_IMPORT+"", CommandType.CONSOLE, Phrase.ARG_IMPORT+"");
        this.plugin = plugin;
    }

    /**
     * Subcommand inspect.
     *
     * Adds player's data from DataCache/DB to the InspectCache for amount of
     * time specified in the config, and clears the data from Cache with a timer
     * task.
     *
     * @param sender
     * @param cmd
     * @param commandLabel
     * @param args Player's name or nothing - if empty sender's name is used.
     * @return true in all cases.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Phrase.COMMAND_REQUIRES_ARGUMENTS_ONE.toString() + " "+Phrase.USE_IMPORT);
            return true;
        }

        String importFromPlugin = args[0].toLowerCase();
        List<String> supportedImports = Arrays.asList(new String[]{"ontime"});
        if (!supportedImports.contains(importFromPlugin)) {
            sender.sendMessage(Phrase.MANAGE_ERROR_INCORRECT_PLUGIN + importFromPlugin);
            return true;
        }
        HashMap<String, Importer> importPlugins = new HashMap<>();
        importPlugins.put("ontime", new OnTimeImporter(plugin));

        if (!importPlugins.get(importFromPlugin).isEnabled()) {
            sender.sendMessage(Phrase.MANAGE_ERROR_PLUGIN_NOT_ENABLED + importFromPlugin);
            return true;
        }

        if (!Arrays.asList(args).contains("-a")) {
            sender.sendMessage(Phrase.COMMAND_ADD_CONFIRMATION_ARGUMENT.parse(Phrase.WARN_OVERWRITE_SOME.parse(plugin.getDB().getConfigName())));
            return true;
        }

        // Header
        sender.sendMessage(Phrase.MANAGE_IMPORTING+"");
        Set<UUID> uuids = new HashSet<>();
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            uuids.add(p.getUniqueId());
        }
        HashMap<UUID, Long> numbericData = importPlugins.get(importFromPlugin).grabNumericData(uuids);
        DataCacheHandler handler = plugin.getHandler();
        if (importFromPlugin.equals("ontime")) {
            importOnTime(numbericData, handler);
        }
        handler.saveCachedUserData();

        sender.sendMessage(Phrase.MANAGE_SUCCESS+"");

        return true;
    }

    private void importOnTime(HashMap<UUID, Long> onTimeData, DataCacheHandler handler) {
        for (UUID uuid : onTimeData.keySet()) {
            OfflinePlayer player = getOfflinePlayer(uuid);
            if (handler.getActivityHandler().isFirstTimeJoin(uuid)) {
                handler.newPlayer(player);
            }
            UserData uData = handler.getCurrentData(uuid);
            Long playTime = onTimeData.get(uuid);
            if (playTime > uData.getPlayTime()) {
                uData.setPlayTime(playTime);
                uData.setLastGamemode(GameMode.SURVIVAL);
                uData.setAllGMTimes(playTime, 0, 0, 0);
                uData.setLastGmSwapTime(playTime);                
            }
        }
    }
}
