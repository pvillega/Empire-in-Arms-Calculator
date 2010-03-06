/*
 *
 * The MIT License
 * Copyright (c) 2010 Pere Villega Montenegro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 * Terrain.scala
 *
 * Contains the terrains used in the combats
 */

package com.perevillega.lib.utils

abstract class Terrain {
    val att_mod_casualties = 0
    val att_mod_moral = 0
    val def_mod_casualties = 0
    val def_mod_moral = 0
    val outflank_modifier = 0
    val reinforce_modifier = 0
}

case class Normal() extends Terrain {
    override def toString() = {
        "Normal"
    }
}
case class Forest() extends Terrain {
    override val att_mod_casualties = -1
    override val def_mod_casualties = -1
    override val outflank_modifier = -1
    override val reinforce_modifier = -1

    override def toString() = {
        "Forest"
    }
}
case class Mountain() extends Terrain {
    override val att_mod_casualties = -1
    override val outflank_modifier = -1
    override val reinforce_modifier = -1

    override def toString() = {
        "Mountain"
    }
}
case class Desert() extends Terrain {
    override val att_mod_moral = 1
    override val def_mod_moral = 1

    override def toString() = {
        "Desert"
    }
}
case class Marsh() extends Terrain {
    override val att_mod_casualties = -1
    override val def_mod_casualties = -1
    override val att_mod_moral = 1
    override val def_mod_moral = 1
    override val outflank_modifier = -1
    override val reinforce_modifier = -1

    override def toString() = {
        "Marsh"
    }
}

object Terrain{
    val getSeq = ("Normal","Normal") :: ("Forest","Forest") :: ("Mountain","Mountain") :: ("Desert","Desert") :: ("Marsh","Marsh") :: Nil

    implicit def str2terrain(str: String) : Terrain = {
        str.trim match {
            case "Normal" => new Normal
            case "Forest" => new Forest
            case "Mountain" => new Mountain
            case "Desert" => new Desert
            case "Marsh" => new Marsh
            case _ => throw new UndefinedTerrainException("Unknown terrain, must be one of Normal, Forest, Mountain, Desert or Marsh")
        }
    }
}

//Defines a custom exception for the terrain
class UndefinedTerrainException(msg: String) extends Exception(msg)
