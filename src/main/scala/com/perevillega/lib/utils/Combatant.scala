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
 * Combatant.scala
 *
 * Defines armies and its members
 */

package com.perevillega.lib.utils
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import Utils._

/**
 * Defines a corp of EiA
 */
class Corp(val name: String, val country: Country, val guard: Int, val artillery: Int, val infantery: Int, val militia: Int, val cavalry: Int, val cossack: Int, val guerrilla: Int, val feudal_inf: Int, val feudal_cav: Int){
    val factors = guard + artillery + infantery + militia + cavalry + cossack + guerrilla + feudal_inf + feudal_cav
    //moral of corp is just the addition of moral of its units, we don't consider the units
    val moral = guard*country.moral_guard + artillery*country.moral_infantery + infantery*country.moral_infantery + militia*country.moral_militia + cavalry*country.moral_cavalry + cossack*country.moral_cossack + guerrilla*country.moral_guerrilla + feudal_inf*country.moral_feudal_infantery + feudal_cav*country.moral_feudal_cavalry
    val total_cavalry = cavalry + cossack + feudal_cav
    def isGuerrilla = guerrilla == factors
    def isCossack = cossack == factors
    def isArtillery = artillery == factors
 
    //cossacks, freikcorps and guerrillas have intrinsic ratings of 0
    val strategic = if(!isCossack && !isGuerrilla) country.strategic else 0
    val tactical = if(!isCossack && !isGuerrilla) country.tactical else 0
}

/**
 * Defines a commander of EiA
 */
class Commander(val name: String, val country: Country,  val strategic: Int, val tactical: Int, val max_tactical: Int, val seniority : String, val isCorp: Boolean) {
    def this( name: String,  country: Country,   strategic: Int,  tactical: Int,  max_tactical: Int,  seniority : String) = this(name, country, strategic, tactical, max_tactical, seniority, false)

    override def toString() = {
        if(!isCorp) {
            name+" ("+country+","+strategic+","+tactical+","+max_tactical+","+seniority+")"
        } else {
            "No commander, corp selected ("+strategic+","+tactical+","+max_tactical+","+seniority+")"
        }
    }
}

/**
 * Defines an army, a set of corps and commanders
 */
class Army(val terrain: Terrain, val units: List[Corp], val commanders: Box[List[Commander]] ){
    val moral : Double = units.foldLeft(0.0)((t,c)=> t + c.moral)
    val factors = units.foldLeft(0)((t,c)=> t + c.factors)
    val cavalry = units.foldLeft(0)((t,c)=> t + c.total_cavalry)
    val artillery = units.foldLeft(0)((t,c)=> t + c.artillery)
    val guards = units.foldLeft(0)((t,c)=> t + c.guard)

    lazy val commander = selectCommander

    //selects the commander of the army according to the Commander selection rules
    private def selectCommander = {
        var comm : Commander = null
        val candidates : List[Commander] = (commanders openOr Nil)

        if(candidates.size > 0){
            //we use the first commander by default
            comm = candidates(0)
            var count = 0
            for(c <- candidates) {
                val troops = units.count(_.country == c.country)
                //more troops of this commander, replace
                if(troops > count) {
                    count = troops
                    comm = c
                } else if(c.country == comm.country) {
                    //same country, check seniority
                    //seniority is a string and the lower the better (A < B < C < D)
                    if(c.seniority < comm.seniority){
                        comm = c
                    }
                }
            }
        } else {
            val tactical = units.foldLeft(0)((x,c) => x max c.tactical)
            val strategic = units.foldLeft(0)((x,c) => x max c.strategic)
            comm = new Commander("Corp", new Country, tactical, strategic, 1, "D", true)
        }
        comm
    }
}

/**
 * Defines one of the sides in combat, attacker or defender
 */
class Combatant(val main: Army, val outflank: Army, val reinforcement: List[Army]){
    val moral_combat = roundWithDecimals((main.moral + outflank.moral)/(main.factors + outflank.factors),1)
    lazy val initial_commander = main.commander
}

/**
 * Stores the options that have been chosen by the user for the simulation
 */
class Options(val iterations: Int, val calvary_superiority: Boolean, val calvary_withdrawal: Boolean, val guard_commitment: Boolean, val guard_attacker_round: Int, val guard_attacker_shift: Int, val guard_defender_round: Int, val guard_defender_shift: Int, val artillery_bombardment: Boolean, val tactical_leaders: Boolean)

/**
 * Object that extracts the values from the request and builds the combatants
 */
object Combatant{
    private def addMap(k: String, corp: Corp, map: scala.collection.mutable.HashMap[String, scala.collection.mutable.ListBuffer[Corp]]){
        val s = map.getOrElseUpdate(k, new scala.collection.mutable.ListBuffer[Corp]())
        s += corp
        map.put(k, s)
    }

    private def addMap(k: String, com: Commander, map: scala.collection.mutable.HashMap[String, scala.collection.mutable.ListBuffer[Commander]]){
        val s = map.getOrElseUpdate(k, new scala.collection.mutable.ListBuffer[Commander]())
        s += com
        map.put(k, s)
    }

    def buildCombatants(data: Map[String, String]) = {        

        //recover number of corps/commanders submitted
        val att_corpnum = data("att_corpnum").toInt
        val att_comnum = data("att_corpnum").toInt
        val df_corpnum = data("att_corpnum").toInt
        val df_comnum = data("att_corpnum").toInt

        //rules
        val iterations = data("iterations").toInt
        val cavsup = data.getOrElse("orcavsup","off")
        val cavwith = data.getOrElse("orcavwithdraw","off")
        val guard = data.getOrElse("orguard","off")
        val guard_att = data.getOrElse("guard_attack","0").toInt
        val guard_att_shift = data.getOrElse("guard_attack_shift","0").toInt
        val guard_def = data.getOrElse("guard_defend","0").toInt
        val guard_def_shift = data.getOrElse("guard_defend_shift","0").toInt
        val art = data.getOrElse("orartillery","off")
        val tactical = data.getOrElse("ortactical","off")
        val options = new Options(iterations, "on" == cavsup, "on" == cavwith, "on" == guard, guard_att, guard_att_shift, guard_def, guard_def_shift, "on" == art, "on" == tactical)

        //common options of combat
        val terrains = new scala.collection.mutable.HashMap[String, Terrain]
        terrains.put("main", data("main_terrain"))
        terrains.put("a_reinforce_1", data("ar1_terrain"))
        terrains.put("a_reinforce_2", data("ar2_terrain"))
        terrains.put("a_reinforce_3", data("ar3_terrain"))
        terrains.put("a_reinforce_4", data("ar4_terrain"))
        terrains.put("a_reinforce_5", data("ar5_terrain"))
        terrains.put("d_reinforce_1", data("dr1_terrain"))
        terrains.put("d_reinforce_2", data("dr2_terrain"))
        terrains.put("d_reinforce_3", data("dr3_terrain"))
        terrains.put("d_reinforce_4", data("dr4_terrain"))
        terrains.put("d_reinforce_5", data("dr5_terrain"))

        //attacker
        val att_com_main = new scala.collection.mutable.ListBuffer[Commander]
        val att_com_rein = new scala.collection.mutable.HashMap[String, scala.collection.mutable.ListBuffer[Commander]]
        for(i <- 1 to att_comnum) {            
            
            try {
                val com = new Commander(data("att_com_name_"+i), data("att_com_country_"+i), data("att_com_s_"+i).toInt, data("att_com_t_"+i).toInt, data("att_com_mt_"+i).toInt, data("att_com_v_"+i))
                data("att_com_section_"+i) match {
                    case "main" => att_com_main += com
                    case "outflank" => att_com_main += com
                    case k @ "reinforce_1" => addMap(k, com, att_com_rein)
                    case k @ "reinforce_2" => addMap(k, com, att_com_rein)
                    case k @ "reinforce_3" => addMap(k, com, att_com_rein)
                    case k @ "reinforce_4" => addMap(k, com, att_com_rein)
                    case k @ "reinforce_5" => addMap(k, com, att_com_rein)
                }
            } catch {
                case e: java.util.NoSuchElementException =>   //ignore missing elements, they've been removed'
                case e @ _ => S.error(e.getMessage); Log.error(e.getMessage, e);
            }
        }

        val att_corp_main = new scala.collection.mutable.ListBuffer[Corp]
        val att_corp_out = new scala.collection.mutable.ListBuffer[Corp]
        val att_corp_rein = new scala.collection.mutable.HashMap[String, scala.collection.mutable.ListBuffer[Corp]]
        for(i <- 1 to att_corpnum) {
            
            try {
                val corp = new Corp(data("att_name_"+i), data("att_country_"+i), data("att_g_"+i).toInt, data("att_a_"+i).toInt, data("att_i_"+i).toInt, data("att_m_"+i).toInt,
                                    data("att_c_"+i).toInt, data("att_ck_"+i).toInt, data("att_gr_"+i).toInt, data("att_fi_"+i).toInt, data("att_fc_"+i).toInt)
                data("att_section_"+i) match {
                    case "main" => att_corp_main += corp
                    case "outflank" => att_corp_out += corp
                    case k @ "reinforce_1" => addMap(k, corp, att_corp_rein)
                    case k @ "reinforce_2" => addMap(k, corp, att_corp_rein)
                    case k @ "reinforce_3" => addMap(k, corp, att_corp_rein)
                    case k @ "reinforce_4" => addMap(k, corp, att_corp_rein)
                    case k @ "reinforce_5" => addMap(k, corp, att_corp_rein)
                }
            } catch {
                case e: java.util.NoSuchElementException =>   //ignore missing elements, they've been removed'
                case e @ _ => S.error(e.getMessage); Log.error(e.getMessage, e);
            }
        }

        //defender
        val df_com_main = new scala.collection.mutable.ListBuffer[Commander]
        val df_com_rein = new scala.collection.mutable.HashMap[String, scala.collection.mutable.ListBuffer[Commander]]
        for(i <- 1 to df_comnum) {
            
            try {
                val com = new Commander(data("df_com_name_"+i), data("df_com_country_"+i), data("df_com_s_"+i).toInt, data("df_com_t_"+i).toInt, data("df_com_mt_"+i).toInt, data("df_com_v_"+i))
                data("df_com_section_"+i) match {
                    case "main" => df_com_main += com
                    case "outflank" => df_com_main += com
                    case k @ "reinforce_1" => addMap(k, com, df_com_rein)
                    case k @ "reinforce_2" => addMap(k, com, df_com_rein)
                    case k @ "reinforce_3" => addMap(k, com, df_com_rein)
                    case k @ "reinforce_4" => addMap(k, com, df_com_rein)
                    case k @ "reinforce_5" => addMap(k, com, df_com_rein)
                }
            } catch {
                case e: java.util.NoSuchElementException =>   //ignore missing elements, they've been removed'
                case e @ _ => S.error(e.getMessage); Log.error(e.getMessage, e);
            }
        }

        val df_corp_main = new scala.collection.mutable.ListBuffer[Corp]
        val df_corp_out = new scala.collection.mutable.ListBuffer[Corp]
        val df_corp_rein = new scala.collection.mutable.HashMap[String, scala.collection.mutable.ListBuffer[Corp]]
        for(i <- 1 to df_corpnum) {

            try {
                val corp = new Corp(data("df_name_"+i), data("df_country_"+i), data("df_g_"+i).toInt, data("df_a_"+i).toInt, data("df_i_"+i).toInt, data("df_m_"+i).toInt,
                                    data("df_c_"+i).toInt, data("df_ck_"+i).toInt, data("df_gr_"+i).toInt, data("df_fi_"+i).toInt, data("df_fc_"+i).toInt)
                data("df_section_"+i) match {
                    case "main" => df_corp_main += corp
                    case "outflank" => df_corp_out += corp
                    case k @ "reinforce_1" => addMap(k, corp, df_corp_rein)
                    case k @ "reinforce_2" => addMap(k, corp, df_corp_rein)
                    case k @ "reinforce_3" => addMap(k, corp, df_corp_rein)
                    case k @ "reinforce_4" => addMap(k, corp, df_corp_rein)
                    case k @ "reinforce_5" => addMap(k, corp, df_corp_rein)
                }
            } catch {
                case e: java.util.NoSuchElementException =>   //ignore missing elements, they've been removed'
                case e @ _ => S.error(e.getMessage); Log.error(e.getMessage, e);
            }
        }

        //create combatants
        val att_m = new Army(terrains("main"), att_corp_main.toList, Full(att_com_main.toList))
        val att_o = new Army(terrains("main"), att_corp_out.toList, Empty )
        val att_r : List[Army] = for {
            k <- att_corp_rein.keys.toList
            a = new Army(terrains("a_"+k), att_corp_rein(k).toList, Full(att_com_rein.getOrElse(k, new scala.collection.mutable.ListBuffer[Commander]).toList))
        } yield a
        val attacker = new Combatant(att_m, att_o, att_r)

        val df_m = new Army(terrains("main"), df_corp_main.toList, Full(df_com_main.toList))
        val df_o = new Army(terrains("main"), df_corp_out.toList, Empty )
        val df_r : List[Army] = for {
            k <- df_corp_rein.keys.toList
            a = new Army(terrains("d_"+k), df_corp_rein(k).toList, Full(df_com_rein.getOrElse(k, new scala.collection.mutable.ListBuffer[Commander]).toList))
        } yield a
        val defender = new Combatant(df_m, df_o, df_r)

        (attacker, defender, options)
    }
}

