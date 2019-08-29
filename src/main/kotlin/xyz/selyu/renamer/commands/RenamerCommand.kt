package xyz.selyu.renamer.commands

import org.bukkit.command.CommandSender
import xyz.selyu.larry.annotation.CommandInfo
import xyz.selyu.larry.annotation.Default
import xyz.selyu.renamer.Renamer

@CommandInfo(name = "renamer", permission = "selyu.renamer")
class RenamerCommand(private val plugin: Renamer) {

    @Default
    fun onDefault(commandSender: CommandSender) {
        plugin.reloadConfig()
        plugin.renamePlugins()

        commandSender.sendMessage("§aRenamer has been reloaded!")
    }
}