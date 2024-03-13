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

package dev.kobalt.hsp2html.jvm.converter

import dev.kobalt.hsp2html.jvm.converter.background.hsp2HtmlBackground
import dev.kobalt.hsp2html.jvm.converter.font.hsp2HtmlFontFace
import dev.kobalt.hsp2html.jvm.converter.font.hsp2HtmlFontSmooth
import dev.kobalt.hsp2html.jvm.converter.image.*
import dev.kobalt.hsp2html.jvm.converter.music.hsp2HtmlMusic
import dev.kobalt.hsp2html.jvm.converter.page.hsp2HtmlPage
import dev.kobalt.hsp2html.jvm.converter.text.hsp2HtmlText
import dev.kobalt.hsp2html.jvm.converter.text.hsp2HtmlTextColorFadeAnimation
import dev.kobalt.hsp2html.jvm.converter.text.hsp2HtmlTextFloatAnimation
import dev.kobalt.hsp2html.jvm.converter.text.hsp2HtmlTextMarqueeAnimation
import dev.kobalt.hsp2html.jvm.entity.HspImageEntity
import dev.kobalt.hsp2html.jvm.entity.HspPageEntity
import dev.kobalt.hsp2html.jvm.entity.HspTextEntity
import dev.kobalt.hsp2html.jvm.entity.HspWebpageEntity
import dev.kobalt.hsp2html.jvm.extension.fromJsonElement
import dev.kobalt.hsp2html.jvm.gif.GifConverter
import dev.kobalt.hsp2html.jvm.parser.Hsp2HtmlParser
import kotlinx.css.CssBuilder
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class Hsp2HtmlConverter(
    private val resourcePath: String,
    private val fontPath: String,
    private val parser: Hsp2HtmlParser
) {

    private val gifConverter = GifConverter()

    fun convert(data: String): String {
        val jsonData = data.fromJsonElement().jsonObject["data"]!!.jsonArray
        val page = HspPageEntity(jsonData.mapNotNull { runCatching { parser.parse(it) }.getOrNull() })
        val webpage = page.objects.find { it is HspWebpageEntity } as HspWebpageEntity
        val pageWidth = 300.0f
        val pageLeftMarginOffset = (pageWidth / 2) * -1
        val objects = page.objects.reversed()
        return StringBuilder().append("<!doctype html>\n").appendHTML().html {
            head {
                title(webpage.title)
                meta { httpEquiv = "Content-Type"; content = "text/html;charset=UTF-8" }
                meta { name = "description"; content = webpage.description }
                style {
                    unsafe {
                        raw(CssBuilder().apply {
                            hsp2HtmlFontFace(fontPath)
                            hsp2HtmlFontSmooth()
                            hsp2HtmlBackground(resourcePath, webpage.backgroundColor, webpage.backgroundImage)
                            hsp2HtmlPage(pageWidth, pageLeftMarginOffset)
                            objects.forEachIndexed { index, item ->
                                when (item) {
                                    is HspTextEntity -> {
                                        hsp2HtmlText(item, index, pageWidth)
                                        if (item.animation == 2) hsp2HtmlTextFloatAnimation(item)
                                        if (item.animation == 3) hsp2HtmlTextMarqueeAnimation(item)
                                        if (item.colorFadeTo != "-1") hsp2HtmlTextColorFadeAnimation(item)
                                    }

                                    is HspImageEntity -> {
                                        hsp2HtmlImage(item, index)
                                        if (item.animFade != "-1") hsp2HtmlImageFadeAnimation(item)
                                        if (item.animTurn == "1") hsp2HtmlImageSwingAnimation(item)
                                        if (item.animTurn == "2") hsp2HtmlImageSpinAnimation(item)
                                    }
                                }
                            }
                        }.toString())
                    }
                    hsp2HtmlImageRendering()
                }
            }
            body {
                div("page") {
                    hsp2HtmlMusic(resourcePath, webpage)
                    objects.forEach { item ->
                        when (item) {
                            is HspTextEntity -> hsp2HtmlText(resourcePath, item)
                            is HspImageEntity -> hsp2HtmlImage(resourcePath, item, gifConverter)
                        }
                    }
                }
            }
        }.toString()
    }

}
