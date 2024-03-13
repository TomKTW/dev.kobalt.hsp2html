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
import kotlinx.css.CssBuilder


fun CssBuilder.hsp2HtmlText(item: HspTextEntity, index: Int, pageWidth: Float) {
    rule(".hspText${item.uid}") {
        val width = item.width * 3
        val x = item.positionXFromCenter * 3 + (pageWidth / 2) - width / 2
        val alignment = when (item.align) {
            "0" -> "left"
            "2" -> "right"
            else -> "center"
        }
        put("color", "#${item.color}")
        put("position", "absolute")
        put("left", "${x}px")
        put("top", "${item.positionY - 6}px")
        put("width", "${width}px")
        put("text-align", alignment)
        put("z-index", "$index")
        put("line-break", "anywhere")
        put("white-space", "pre-wrap")
        put("font-family", "HypnoFont")
        if (item.font == "DesignElements") put("background-color", "#${item.color}")
        if (item.style.contains("b")) put("font-weight", "bold")
        when {
            item.style.contains("1") -> put("font-size", "9px")
            item.style.contains("2") -> put("font-size", "12px")
            else -> put("font-size", "7px")
        }
        if (item.colorFadeTo != "-1") {
            put("animation-name", "fade${item.uid}")
            put("animation-duration", "${(100 - item.colorFadeToSpeed.toInt()) * 50}ms")
            put("animation-iteration-count", "infinite")
            put("animation-timing-function", "linear")
        }
        when (item.animation) {
            2 -> {
                put("animation-name", "float${item.uid}")
                put("animation-duration", "${(100 - item.animationSpeed.toInt()) * 10}ms")
                put("animation-iteration-count", "infinite")
                put("animation-timing-function", "ease-in-out")
            }

            3 -> {
                put("animation-name", "marquee${item.uid}")
                put("animation-duration", "${(100 - item.animationSpeed.toInt()) * 100}ms")
                put("animation-iteration-count", "infinite")
                put("animation-timing-function", "linear")
                put("overflow-x", "hidden")
            }
        }
    }
}