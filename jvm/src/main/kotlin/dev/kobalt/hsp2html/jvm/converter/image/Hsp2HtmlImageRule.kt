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

package dev.kobalt.hsp2html.jvm.converter.image

import dev.kobalt.hsp2html.jvm.entity.HspImageEntity
import dev.kobalt.hsp2html.jvm.extension.align
import kotlinx.css.CssBuilder

fun CssBuilder.hsp2HtmlImage(item: HspImageEntity, index: Int) {
    rule(".hspImage${item.uid}") {
        put("transform-origin", "center")
        put(
            "transform",
            "scale(${item.scale}) rotate(${item.rotation}deg) scaleX(${if (item.mirror == "0") 1 else -1}) scaleY(${if (item.flip == "0") 1 else -1})"
        )
        if (item.colorHue != null && item.colorSaturation != null && item.colorLuminosity != null) {
            put(
                "filter",
                "hue-rotate(${
                    item.colorHue.align(-100..100, -360..360)
                }deg) saturate(${item.colorSaturation}%) brightness(${item.colorLuminosity}%)"
            )
        }
        when (item.animTurn) {
            "1" -> {
                put("animation-name", "swing${item.uid}")
                put("animation-duration", "${(100 - item.animTurnSpeed.toInt()) * 50}ms")
                put("animation-iteration-count", "infinite")
                put("animation-timing-function", "ease-in-out")
            }

            "2" -> {
                put("animation-name", "spin${item.uid}")
                put("animation-duration", "${(100 - item.animTurnSpeed.toInt()) * 100}ms")
                put("animation-iteration-count", "infinite")
                put("animation-timing-function", "linear")
            }
        }
        put("z-index", "$index")
    }
    rule(".hspImage${item.uid} img") {
        if (item.animFade != "-1") {
            put("animation-name", "fade${item.uid}")
            put("animation-duration", "${(item.animFade.toInt()) * 50}ms")
            put("animation-iteration-count", "infinite")
            put("animation-timing-function", "steps(8, end)")
        }
    }
}