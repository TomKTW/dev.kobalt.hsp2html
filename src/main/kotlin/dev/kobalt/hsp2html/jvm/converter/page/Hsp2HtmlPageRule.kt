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

package dev.kobalt.hsp2html.jvm.converter.page

import kotlinx.css.CssBuilder

fun CssBuilder.hsp2HtmlPage(width: Float, leftMarginOffset: Float) {
    rule(".page") {
        put("width", "${width}px")
        put("transform", "scale(2.0)")
        put("position", "absolute")
        put("left", "50%")
        put("margin-left", "${leftMarginOffset}px")
    }
}