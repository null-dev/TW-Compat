/*
 * Copyright 2016 Andy Bao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.nulldev.ts.api.http.manga

import eu.kanade.tachiyomi.data.database.DatabaseHelper
import eu.kanade.tachiyomi.source.Source
import eu.kanade.tachiyomi.source.SourceManager
import spark.Request
import spark.Response
import xyz.nulldev.ts.api.http.TachiWebRoute
import xyz.nulldev.ts.ext.kInstanceLazy
import xyz.nulldev.ts.library.LibraryUpdater

/**
 * Project: TachiServer
 * Author: nulldev
 * Creation Date: 30/09/16
 */
class UpdateRoute : TachiWebRoute() {

    private val libraryUpdater: LibraryUpdater by kInstanceLazy()
    private val db: DatabaseHelper by kInstanceLazy()
    private val sourceManager: SourceManager by kInstanceLazy()

    override fun handleReq(request: Request, response: Response): Any {
        val updateType: UpdateType
        try {
            updateType = UpdateType.valueOf((request.params(":updateType") ?: "").toUpperCase())
        } catch (e: IllegalArgumentException) {
            return error("Invalid/no update type specified!")
        }

        val mangaId = request.params(":mangaId")?.toLong()
                ?: return error("MangaID must be specified!")
        val manga = db.getManga(mangaId).executeAsBlocking()
                ?: return error("The specified manga does not exist!")
        val source: Source?
        try {
            source = sourceManager.get(manga.source)
            if (source == null) {
                throw IllegalArgumentException()
            }
        } catch (e: Exception) {
            return error("This manga's source is not loaded!")
        }

        if (updateType == UpdateType.INFO) {
            //Update manga info
            try {
                libraryUpdater.updateMangaInfo(manga, source)
            } catch (e: Exception) {
                return error("Error updating manga!")
            }

        } else if (updateType == UpdateType.CHAPTERS) {
            //Update manga chapters
            try {
                val results = libraryUpdater.updateChapters(manga, source)
                //Return the results in JSON
                val toReturn = TachiWebRoute.success()
                toReturn.put(KEY_ADDED, results.first)
                toReturn.put(KEY_REMOVED, results.second)
                return toReturn.toString()
            } catch (e: Exception) {
                return error("Error updating chapters!")
            }

        } else {
            return error("Null/unimplemented update type!")
        }
        return success()
    }

    /**
     * The type of update to perform
     */
    enum class UpdateType {
        INFO,
        CHAPTERS
    }

    companion object {
        private val KEY_ADDED = "added"
        private val KEY_REMOVED = "removed"
    }
}