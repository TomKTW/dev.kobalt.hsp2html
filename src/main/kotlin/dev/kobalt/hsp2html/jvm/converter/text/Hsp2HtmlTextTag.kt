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

package dev.kobalt.hsp2html.jvm.converter.text

import dev.kobalt.hsp2html.jvm.entity.HspTextEntity
import dev.kobalt.hsp2html.jvm.resource.ResourceRepository
import kotlinx.html.*

@HtmlTagMarker
fun HtmlBlockTag.hsp2HtmlText(pageObject: HspTextEntity) {
    div {
        pre("hspText${pageObject.uid}") {
            if (pageObject.animation == 1) {
                id = "typewriter${pageObject.uid}"
            }
            text(
                pageObject.text.replace("/n", "\n")
                    .replace("/N", "\n")
                    .replace("/t", "\t")
                    .replace("/T", "\t")
            )
        }
        if (pageObject.animation == 1) script {
            unsafe {
                val text = pageObject.text.replace("\"", "\'")
                    .replace("/n", "\\n")
                    .replace("/N", "\\n")
                    .replace("/t", "\\t")
                    .replace("/T", "\\t")

                +ResourceRepository.getText("typewriter.js")!!.replace("\r\n", "")
                    .replace("\$pageObjectUid\$", pageObject.uid)
                    .replace("\$pageObjectText\$", text)
                    .replace("\$pageObjectSpeed\$", "50")
            }
        }
    }
}