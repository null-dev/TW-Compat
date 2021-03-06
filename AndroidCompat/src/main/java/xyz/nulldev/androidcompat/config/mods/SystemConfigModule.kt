package xyz.nulldev.androidcompat.config.mods

import com.typesafe.config.Config
import xyz.nulldev.androidcompat.config.ConfigModule

class SystemConfigModule(val config: Config) : ConfigModule(config) {
    val isDebuggable = config.getBoolean("isDebuggable")

    val propertyPrefix = "properties."

    fun getStringProperty(property: String) = config.getString("$propertyPrefix$property")!!
    fun getIntProperty(property: String) = config.getInt("$propertyPrefix$property")
    fun getLongProperty(property: String) = config.getLong("$propertyPrefix$property")
    fun getBooleanProperty(property: String) = config.getBoolean("$propertyPrefix$property")
    fun hasProperty(property: String) = config.hasPath("$propertyPrefix$property")
}
