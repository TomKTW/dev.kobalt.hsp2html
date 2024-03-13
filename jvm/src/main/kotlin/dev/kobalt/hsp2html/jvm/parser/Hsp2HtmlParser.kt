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

package dev.kobalt.hsp2html.jvm.parser

import dev.kobalt.hsp2html.jvm.entity.HspImageEntity
import dev.kobalt.hsp2html.jvm.entity.HspTextEntity
import dev.kobalt.hsp2html.jvm.entity.HspWebpageEntity
import dev.kobalt.lib.hsp2html.entity.HspObjectEntity
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class Hsp2HtmlParser {

    private fun Int.colorToRgb(): String {
        val c = this
        val b = c / 65536
        val g = (c - (b * 65536)) / 256
        val r = (c - (b * 65536)) - (g * 256)
        val rString = r.toString(16).padStart(2, '0')
        val gString = g.toString(16).padStart(2, '0')
        val bString = b.toString(16).padStart(2, '0')
        return "$rString$gString$bString".padStart(6, '0')
    }

    fun parse(item: JsonElement): HspObjectEntity {
        return when (val type = item.jsonArray[0].jsonArray[0].jsonPrimitive.content) {
            "Webpage" -> HspWebpageEntity(
                type = item.jsonArray[0].jsonArray[0].jsonPrimitive.content,
                uid = item.jsonArray[0].jsonArray[1].jsonPrimitive.content,
                name = item.jsonArray[0].jsonArray[2].jsonPrimitive.content,
                title = item.jsonArray[1].jsonArray[1].jsonPrimitive.content,
                username = item.jsonArray[1].jsonArray[2].jsonPrimitive.content,
                height = item.jsonArray[1].jsonArray[3].jsonPrimitive.int,
                music = item.jsonArray[1].jsonArray[4].jsonPrimitive.content.replace("\\", "/"),
                backgroundImage = item.jsonArray[1].jsonArray[5].jsonPrimitive.content.replace("\\", "/"),
                mouseFx = item.jsonArray[1].jsonArray[6].jsonPrimitive.int,
                backgroundColor = item.jsonArray[1].jsonArray[7].jsonPrimitive.int.toString().padStart(6, '0'),
                description = item.jsonArray[1].jsonArray[8].jsonPrimitive.content,
                pageStyle = item.jsonArray[1].jsonArray[9].jsonPrimitive.content,
                isUserHome = item.jsonArray[1].jsonArray[10].jsonPrimitive.content,
                onLoadScript = item.jsonArray[1].jsonArray[11].jsonPrimitive.content
            )

            "Text" -> HspTextEntity(
                type = item.jsonArray[0].jsonArray[0].jsonPrimitive.content,
                uid = item.jsonArray[0].jsonArray[1].jsonPrimitive.content,
                name = item.jsonArray[0].jsonArray[2].jsonPrimitive.content,
                positionXFromCenter = item.jsonArray[1].jsonArray[1].jsonPrimitive.int,
                positionY = item.jsonArray[1].jsonArray[2].jsonPrimitive.int,
                width = item.jsonArray[1].jsonArray[3].jsonPrimitive.int,
                caseTag = item.jsonArray[1].jsonArray[4].jsonPrimitive.content,
                text = item.jsonArray[1].jsonArray[5].jsonPrimitive.content,
                color = item.jsonArray[1].jsonArray[6].jsonPrimitive.int.colorToRgb(),
                font = item.jsonArray[1].jsonArray[7].jsonPrimitive.content,
                style = item.jsonArray[1].jsonArray[8].jsonPrimitive.content,
                align = item.jsonArray[1].jsonArray[9].jsonPrimitive.content,
                linkScript = item.jsonArray[1].jsonArray[10].jsonPrimitive.content,
                lawBroken = item.jsonArray[1].jsonArray[11].jsonPrimitive.content,
                animation = item.jsonArray[1].jsonArray[12].jsonPrimitive.int,
                animationSpeed = item.jsonArray[1].jsonArray[13].jsonPrimitive.content,
                colorFadeTo = item.jsonArray[1].jsonArray[14].jsonPrimitive.int.colorToRgb(),
                colorFadeToSpeed = item.jsonArray[1].jsonArray[15].jsonPrimitive.content,
                noContent = item.jsonArray[1].jsonArray[16].jsonPrimitive.content
            )

            "Gif" -> HspImageEntity(
                type = item.jsonArray[0].jsonArray[0].jsonPrimitive.content,
                uid = item.jsonArray[0].jsonArray[1].jsonPrimitive.content,
                name = item.jsonArray[0].jsonArray[2].jsonPrimitive.content,
                positionX = item.jsonArray[1].jsonArray[1].jsonPrimitive.int,
                positionY = item.jsonArray[1].jsonArray[2].jsonPrimitive.int,
                colorHue = item.jsonArray[1].jsonArray[3].jsonPrimitive.content.takeIf { it != "-1" }?.split(",")
                    ?.getOrNull(0)?.toInt(),
                colorSaturation = item.jsonArray[1].jsonArray[3].jsonPrimitive.content.takeIf { it != "-1" }
                    ?.split(",")?.getOrNull(1)?.toInt(),
                colorLuminosity = item.jsonArray[1].jsonArray[3].jsonPrimitive.content.takeIf { it != "-1" }
                    ?.split(",")?.getOrNull(2)?.toInt(),
                caseTag = item.jsonArray[1].jsonArray[4].jsonPrimitive.content,
                imageFilename = item.jsonArray[1].jsonArray[5].jsonPrimitive.content.lowercase()
                    .replace("\\", "/"),
                scale = item.jsonArray[1].jsonArray[6].jsonPrimitive.content,
                rotation = item.jsonArray[1].jsonArray[7].jsonPrimitive.content,
                mirror = item.jsonArray[1].jsonArray[8].jsonPrimitive.content,
                flip = item.jsonArray[1].jsonArray[9].jsonPrimitive.content,
                linkScript = item.jsonArray[1].jsonArray[10].jsonPrimitive.content,
                lawBroken = item.jsonArray[1].jsonArray[11].jsonPrimitive.content,
                animFlipX = item.jsonArray[1].jsonArray[12].jsonPrimitive.content,
                animFlipY = item.jsonArray[1].jsonArray[13].jsonPrimitive.content,
                animFade = item.jsonArray[1].jsonArray[14].jsonPrimitive.content,
                animTurn = item.jsonArray[1].jsonArray[15].jsonPrimitive.content,
                animTurnSpeed = item.jsonArray[1].jsonArray[16].jsonPrimitive.content,
                fps = item.jsonArray[1].jsonArray[17].jsonPrimitive.content,
                offset = item.jsonArray[1].jsonArray[18].jsonPrimitive.content,
                sync = item.jsonArray[1].jsonArray[19].jsonPrimitive.content,
                animMoveOver = item.jsonArray[1].jsonArray[20].jsonPrimitive.content
            )

            else -> throw Exception("Unknown type: $type")
        }
    }

}