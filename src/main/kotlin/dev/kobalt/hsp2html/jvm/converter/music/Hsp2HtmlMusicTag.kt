/*
 * dev.kobalt.hsp2html
 * Copyright (C) 2022 Tom.K
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.kobalt.hsp2html.jvm.converter.music

import dev.kobalt.hsp2html.jvm.entity.HspWebpageEntity
import dev.kobalt.hsp2html.jvm.extension.encodeToBase64String
import dev.kobalt.hsp2html.jvm.extension.requireIsLocatedIn
import kotlinx.html.HtmlBlockTag
import kotlinx.html.HtmlTagMarker
import kotlinx.html.audio
import java.io.File

@HtmlTagMarker
fun HtmlBlockTag.hsp2HtmlMusic(resourcePath: String, item: HspWebpageEntity) {
    val musicFile = File("$resourcePath/${item.music}").takeIf { item.music.isNotEmpty() }
        ?.requireIsLocatedIn(File("$resourcePath/audio"))
    musicFile?.takeIf { it.exists() }?.inputStream()?.use { it.readBytes().encodeToBase64String() }
        ?.let { "data:audio/ogg;base64,$it" }?.let {
            audio { autoPlay = true; src = it; loop = true }
        }
}
