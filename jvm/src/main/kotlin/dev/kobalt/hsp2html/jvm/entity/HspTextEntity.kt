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

package dev.kobalt.hsp2html.jvm.entity

import dev.kobalt.lib.hsp2html.entity.HspObjectEntity

data class HspTextEntity(
    override val type: String,
    override val uid: String,
    override val name: String,
    val positionXFromCenter: Int,
    val positionY: Int,
    val width: Int,
    val caseTag: String,
    val text: String,
    val color: String,
    val font: String,
    val style: String,
    val align: String,
    val linkScript: String,
    val lawBroken: String,
    val animation: Int,
    val animationSpeed: String,
    val colorFadeTo: String,
    val colorFadeToSpeed: String,
    val noContent: String
) : HspObjectEntity(type, uid, name)