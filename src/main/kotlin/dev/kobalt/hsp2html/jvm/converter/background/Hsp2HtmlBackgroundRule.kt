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

package dev.kobalt.hsp2html.jvm.converter.background

import dev.kobalt.hsp2html.jvm.extension.encodeToBase64String
import dev.kobalt.hsp2html.jvm.extension.requireIsLocatedIn
import kotlinx.css.CssBuilder
import java.io.File

fun CssBuilder.hsp2HtmlBackground(resourcePath: String, color: String, image: String) {
    rule("body") {
        put("overflow-x", "hidden")
        put("background-color", "#$color")
        val bgPath = "$resourcePath/image/bgs"
        val bgFile = File("$bgPath/$image").requireIsLocatedIn(File("$resourcePath/image/bgs"))
        bgFile.takeIf { it.exists() }?.inputStream()?.use { it.readBytes().encodeToBase64String() }
            ?.let { "data:image/gif;base64,$it" }?.let {
                put("background-image", "url($it)")
                put("background-size", "600px")
                put("background-position", "center top")
            }
    }
}
