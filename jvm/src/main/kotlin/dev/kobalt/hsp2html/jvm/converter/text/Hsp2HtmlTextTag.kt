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
import dev.kobalt.hsp2html.jvm.extension.encodeToBase64String
import dev.kobalt.hsp2html.jvm.extension.requireIsLocatedIn
import kotlinx.html.HtmlBlockTag
import kotlinx.html.HtmlTagMarker
import kotlinx.html.div
import kotlinx.html.img
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max

@HtmlTagMarker
fun HtmlBlockTag.hsp2HtmlText(resourcePath: String, pageObject: HspTextEntity) {
    div {
        val text = pageObject.text.replace("/n", "\n")
            .replace("/N", "\n")
            .replace("/t", "\t")
            .replace("/T", "\t")
        img(text, test(resourcePath, pageObject).let { "data:image/gif;base64,$it" }, "hspText${pageObject.uid}")
        /*
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
         */
    }
}

/** Supposed to replace text with rendered image.*/
fun test(resourcePath: String, pageObject: HspTextEntity): String {
    val fontPath = "$resourcePath/image/fonts"
    val fontFile =
        File("$fontPath/${pageObject.font}${pageObject.style}.png").requireIsLocatedIn(File("$resourcePath/image/fonts"))
    val fontImage = fontFile.takeIf { it.exists() }?.let { ImageIO.read(it) }!!
    val fontColumns = 8
    val fontRows = 12
    val fontImageCharWidth = fontImage.width / fontColumns
    val fontImageCharHeight = fontImage.height / fontRows
    val text = pageObject.text.replace("/n", "\n")
        .replace("/N", "\n")
        .replace("/t", "\t")
        .replace("/T", "\t")
    val textWidth = text.length * fontImageCharWidth
    val textHeight = fontImageCharHeight

    // Process of rendering an image file from font.

    var newLineCounter = 0
    val charMap = pageObject.text.mapIndexed { index, char ->
        var charX = (fontImageCharWidth * index) - (250 * newLineCounter)
        if (charX > 250) newLineCounter += 1
        val charY = fontImageCharHeight * newLineCounter
        Pair(charX % 250, charY)
    }


    val textImageWidth = pageObject.width * 3
    val textImageHeight = max(fontImageCharHeight, fontImageCharHeight * newLineCounter)

    val textImage = BufferedImage(textImageWidth, textImageHeight, BufferedImage.TYPE_INT_ARGB)
    textImage.createGraphics().apply {
        pageObject.text.forEachIndexed { index, char ->
            // If x and y are set to 0, it's always A.
            val fontImageCharX = max(0, (char - 'A') % fontColumns) * fontImageCharWidth
            val fontImageCharY = max(0, (char - 'A') / fontColumns) * fontImageCharHeight
            val fontImageChar =
                fontImage.getSubimage(fontImageCharX, fontImageCharX, fontImageCharWidth, fontImageCharHeight)
            drawImage(fontImageChar, charMap[index].first, charMap[index].second, null)
        }
        dispose()
    }
    return ByteArrayOutputStream().use {
        ImageIO.write(textImage, "png", it); it.toByteArray()
    }.encodeToBase64String()
}
