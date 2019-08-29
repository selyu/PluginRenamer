package xyz.selyu.renamer

import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import xyz.selyu.larry.Larry
import xyz.selyu.larry.util.CommandUtil
import xyz.selyu.renamer.commands.RenamerCommand
import java.lang.reflect.Field

class Renamer : JavaPlugin() {

    private val renamed: MutableMap<String, Plugin> = mutableMapOf()
    private lateinit var larry: Larry

    override fun onEnable() {
        larry = Larry(this)
        saveDefaultConfig()

        CommandUtil.registerBaseTypeAdapters(larry)

        larry.registerCommand(RenamerCommand(this))

        server.scheduler.scheduleSyncDelayedTask(this, {
            renamePlugins()
        }, 60L)
    }

    fun renamePlugins() {
        config.getConfigurationSection("plugins")?.getKeys(false)?.forEach { pl ->
            val plugin = if(renamed.containsKey(pl)) {
                renamed[pl]
            } else {
                server.pluginManager.getPlugin(pl)
            }

            if(plugin == null) {
                logger.warning("Plugin $pl could not be found and therefore could not be renamed!")
                return
            }

            val sec = config.getConfigurationSection("plugins.$pl") ?: throw NullPointerException("The configuration section doesn't exist!")
            val desc = plugin.description

            sec.getKeys(false).forEach { key ->
                sec[key]?.let { setField(desc, key, it) }
            }

            renamed[pl] = plugin
        }
    }

    private fun setField(targetObject: Any, fieldName: String, fieldValue: Any): Boolean {
        var field: Field? = try {
            targetObject.javaClass.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            null
        }

        var superClass: Class<*>? = targetObject.javaClass.superclass

        while (field == null && superClass != null) {
            try {
                field = superClass.getDeclaredField(fieldName)
            } catch (e: NoSuchFieldException) {
                superClass = superClass.superclass
            }
        }

        if (field == null) {
            return false
        }

        field.isAccessible = true

        return try {
            field.set(targetObject, fieldValue)
            true
        } catch (e: IllegalAccessException) {
            false
        }
    }
}