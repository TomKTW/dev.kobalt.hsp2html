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

data class HspImageEntity(
    override val type: String,
    override val uid: String,
    override val name: String,
    val positionX: Int,
    val positionY: Int,
    val colorHue: Int?,
    val colorSaturation: Int?,
    val colorLuminosity: Int?,
    val caseTag: String,
    val imageFilename: String,
    val scale: String,
    val rotation: String,
    val mirror: String,
    val flip: String,
    val linkScript: String,
    val lawBroken: String,
    val animFlipX: String,
    val animFlipY: String,
    val animFade: String,
    val animTurn: String,
    val animTurnSpeed: String,
    val fps: String,
    val offset: String,
    val sync: String,
    val animMoveOver: String
) : HspObjectEntity(type, uid, name)