package xyz.nulldev.androidcompat.io

import com.github.salomonbrys.kodein.conf.KodeinGlobalAware
import com.github.salomonbrys.kodein.instance
import xyz.nulldev.androidcompat.config.ConfigManager
import xyz.nulldev.androidcompat.config.mods.FilesConfigModule
import java.io.File

/**
 * Android file constants.
 */
class AndroidFiles : KodeinGlobalAware {
    val configManager: ConfigManager = kodein.instance()

    val filesConfig: FilesConfigModule
        get() = configManager.module(FilesConfigModule::class.java)

    val dataDir: File get() = registerFile(filesConfig.dataDir)
    val filesDir: File get() = registerFile(filesConfig.filesDir)
    val noBackupFilesDir: File get() = registerFile(filesConfig.noBackupFilesDir)
    val externalFilesDirs: List<File> get() = filesConfig.externalFilesDirs.map { registerFile(it) }
    val obbDirs: List<File> get() = filesConfig.obbDirs.map { registerFile(it) }
    val cacheDir: File get() = registerFile(filesConfig.cacheDir)
    val codeCacheDir: File get() = registerFile(filesConfig.codeCacheDir)
    val externalCacheDirs: List<File> get() = filesConfig.externalCacheDirs.map { registerFile(it) }
    val externalMediaDirs: List<File> get() = filesConfig.externalMediaDirs.map { registerFile(it) }
    val rootDir: File get() = registerFile(filesConfig.rootDir)
    val externalStorageDir: File get() = registerFile(filesConfig.externalStorageDir)
    val downloadCacheDir: File get() = registerFile(filesConfig.downloadCacheDir)
    val databasesDir: File get() = registerFile(filesConfig.databasesDir)

    val prefsDir: File get() = registerFile(filesConfig.prefsDir)

    fun registerFile(file: String): File {
        return File(file).apply {
            mkdirs()
        }
    }
}
