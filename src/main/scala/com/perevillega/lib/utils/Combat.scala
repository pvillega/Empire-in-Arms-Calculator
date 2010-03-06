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
 * Combat.scala
 *
 * Simulates a combat between 2 armies given 2 tactics
 */

package com.perevillega.lib.utils
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import scala.xml._
import Roll._
import Combat._
import Utils._

/**
 * Default combat class, contains the core logic of a combat
 */
abstract class Combat(val attacker: Combatant, val defender: Combatant, options: Options) {
    
    protected val logid : String

    //control of casualties and combat
    protected var att_moral_lost = 0.0
    protected var def_moral_lost = 0.0
    protected var defender_withdraws = false
    protected var cant_outflank = false
    protected var att_guard_lost = false
    protected var def_guard_lost = false

    def attMoral() = {
        roundWithDecimals(attacker.moral_combat - att_moral_lost,1)
    }

    def defMoral() = {
        roundWithDecimals(defender.moral_combat - def_moral_lost,1)
    }

    //check if this combat has ended
    protected def isCombatEnd : Boolean = {
        defender_withdraws || cant_outflank ||
        att_moral_lost >= attacker.moral_combat ||
        def_moral_lost >= defender.moral_combat ||
        att_current_factors <= 0 ||
        def_current_factors <= 0 ||
        (if(options.artillery_bombardment){
                att_current_factors == att_artillery ||
                def_current_factors == def_artillery
            } else {
                false
            }
        ) ||
        (if(options.guard_commitment){
                att_guard_lost ||
                def_guard_lost
            } else {
                false
            }
        )
    }

    //check if there is a winner
    def attackerWins = {
        !defender_withdraws && !cant_outflank && isCombatEnd && att_moral_lost < attacker.moral_combat && att_current_factors > 0 && (if(options.artillery_bombardment){ att_current_factors > att_artillery } else {true}) && (if(options.guard_commitment){ !att_guard_lost } else {true})
    }

    def defenderWins = {
        !defender_withdraws && !cant_outflank && isCombatEnd && def_moral_lost < defender.moral_combat && def_current_factors > 0 && (if(options.artillery_bombardment){ def_current_factors > def_artillery } else {true}) && (if(options.guard_commitment){ !def_guard_lost } else {true})
    }

    //control of factors
    protected var att_current_factors = 0
    protected var att_attack_factors = 0
    protected var att_cavalry = 0
    protected var att_artillery = 0    
    protected var def_current_factors = 0
    protected var def_attack_factors = 0
    protected var def_cavalry = 0
    protected var def_artillery = 0

    protected val att_reinforcements = new scala.collection.mutable.ListBuffer[Army]
    protected val att_reinforcements_names = new scala.collection.mutable.ListBuffer[String]
    protected val def_reinforcements = new scala.collection.mutable.ListBuffer[Army]
    protected val def_reinforcements_names = new scala.collection.mutable.ListBuffer[String]

    def attUnits() = {
        att_current_factors
    }

    def defUnits() = {
        def_current_factors
    }


    //tactic tables    
    protected var attack_outflank = false
    protected var defend_outflank = false

    protected val attack_main_crc : List[Tuple2[Int,Int]]
    protected val attack_sec_crc : List[Tuple2[Int,Int]]
    protected val defend_main_crc : List[Tuple2[Int,Int]]
    protected val defend_sec_crc : List[Tuple2[Int,Int]]

    protected def crcAttack(round: Int) = {
        if(!attack_outflank){
            attack_main_crc(round-1)
        } else {
            attack_sec_crc(round-1)
        }
    }

    protected def crcDefend(round: Int) = {
        if(!defend_outflank){
            defend_main_crc(round-1)
        } else {
            defend_sec_crc(round-1)
        }
    }

    //commanders selection
    protected var att_commander = attacker.initial_commander
    protected var def_commander = defender.initial_commander
    protected var att_corps = 0
    protected var def_corps = 0
    protected var use_commander_chart = true
    protected var att_echelon_bonus = false
    protected var def_echelon_bonus = false

    //methods to simulate combat
    def start = {
        log("<h2>Combat tactics: <b>" + logid +"</b></h2>")
        //run sequence of combat        
        log("Attacker commander: <u>" + att_commander.toString +"</u>")
        log("Defender commander: <u>" + def_commander.toString +"</u>")
        precombat
        log("Initial forces: Attacker <b>"+att_current_factors+" factors/"+attMoral()+" moral</b> vs. Defender <b>"+
            def_current_factors+" factors/"+defMoral()+" moral</b>")
        if(!isCombatEnd) preR1
        if(!isCombatEnd) R1
        if(!isCombatEnd) preR2
        if(!isCombatEnd) R2
        if(!isCombatEnd) preR3
        if(!isCombatEnd) R3
        log("")
        log("")
    }

    protected def precombat: Any = {
        //factors in combat (usual tactics)
        att_current_factors = attacker.main.factors + attacker.outflank.factors
        att_attack_factors = att_current_factors
        att_cavalry = attacker.main.cavalry + attacker.outflank.cavalry
        att_artillery = attacker.main.artillery + attacker.outflank.artillery
        att_corps = attacker.main.units.size + attacker.outflank.units.size

        def_current_factors = defender.main.factors + defender.outflank.factors
        def_attack_factors = def_current_factors
        def_cavalry = defender.main.cavalry + defender.outflank.cavalry
        def_artillery = defender.main.artillery + defender.outflank.artillery
        def_corps = defender.main.units.size + defender.outflank.units.size

        att_reinforcements.appendAll(attacker.reinforcement)
        def_reinforcements.appendAll(defender.reinforcement)

        verifyFactorsArtillery
    }

    protected def preR1 = {
        //empty by default
    }

    protected def R1 = {
        round(1)
    }

    protected def preR2 = {
        verifyReinforcements
    }

    protected def R2 = {
        round(2)
    }

    protected def preR3 = {
        verifyReinforcements
    }

    protected def R3 = {
        round(3)
    }

    //simulates a round of combat
    protected def round(round: Int) {        
        def getTactical(com: Commander, corps: Int) = {
            def rate(mt: Int, c: Int) : Int = {
                c <= mt match {
                    case true => 0
                    case false => 1 + rate(mt, c-mt)
                }
            }
            
            var rating = 0
            if(options.tactical_leaders){
                log(">> 12.3.7.2 Calculating tactical rating of "+com.name+" according to optional rule")
                rating = 1 max (com.tactical - rate(com.max_tactical, corps))
            } else {
                rating = 1 max (if(corps > 2*com.max_tactical ) com.tactical - 2
                                else if (corps > com.max_tactical) com.tactical - 1
                                else com.tactical)
            }
            log("> Rating of "+com.name+" with tactical "+com.tactical+", max tactical "+com.max_tactical+" and "+corps+" corps is <b>"+rating+"</b>")
            rating
        }

        def modified(n: Int) = {            
            (1 max n) min 5
        }

        def checkDieModifier(base: Tuple2[Int,Int]) = {
            log(">> Commander chart bonuses: Attacker <b>"+base._1+"</b> - Defender <b>"+base._2+"</b>")
            var (att_cav, df_cav) = (0,0)
            if(options.calvary_superiority){
                if((att_cavalry == att_current_factors) && (def_cavalry == def_current_factors)){
                    log(">>12.3.3.1 - Both armies consist of only cavalry, cavalry superiority rule doesn't apply")
                } else {
                    log(">>12.3.3.1 - Checking cavalry superiority. Attacker cavalry: "+att_cavalry+" factors vs Defender cavalry: "+def_cavalry+" factors")
                    if(att_cavalry > 0 && att_cavalry >= 2*def_cavalry){
                        att_cav = 1
                        log(">>+1 bonus to attacker rolls due to rule 12.3.3.1")
                    } else {
                        log(">>No bonus to attacker rolls due to rule 12.3.3.1")
                    }
                    if(def_cavalry > 0 && def_cavalry >= 2*att_cavalry){
                        df_cav = 1
                        log(">>+1 bonus to defender rolls due to rule 12.3.3.1")
                    } else {
                        log(">>No bonus to defender rolls due to rule 12.3.3.1")
                    }
                }
            }
            val att_echelon = if(att_echelon_bonus) 1 else 0
            var att = 1 min (base._1 + att_echelon + att_cav)

            val df_echelon = if(def_echelon_bonus) 1 else 0
            val df = 1 min (base._2 + df_echelon + df_cav)

            (att,df)
        }
       

        log("<b>Round "+round+" >></b>")
        //dice modifiers
        val (att_modifier,def_modifier) = checkDieModifier(getCommanderModifiers(getTactical(att_commander, att_corps),getTactical(def_commander, def_corps),use_commander_chart))

        val (guard_att, guard_def) = guardCommitment(round)
        if(!att_guard_lost && !def_guard_lost) {
            artilleryBombardment(att_modifier,def_modifier)

            val rollatt = D6(att_modifier)
            val att_crc_cas = modified(crcAttack(round)._1+attacker.main.terrain.att_mod_casualties)
            val att_crc_moral = modified(crcAttack(round)._2+attacker.main.terrain.att_mod_moral+guard_att)
            val att_res = getCRCResult(att_crc_cas,att_crc_moral,rollatt)
            val df_lost_u = getCasualtiesGenerated(att_res._1,att_attack_factors)
            val df_lost_m = att_res._2
            log("> Attacker  on table <b>("+att_crc_cas+","+att_crc_moral+")</b>(Original: "+crcAttack(round)._1+","+crcAttack(round)._2+", terrain modifiers: "+attacker.main.terrain.att_mod_casualties+","+attacker.main.terrain.att_mod_moral+", guard commitment: "+guard_att+") rolls with modifier "+att_modifier+" a <b>"+ rollatt+"</b> and causes <u>-"+df_lost_u+" units/-"+df_lost_m+" moral</u>")
            val rolldf = D6(def_modifier)
            val df_crc_cas = modified(crcDefend(round)._1+defender.main.terrain.def_mod_casualties)
            val df_crc_moral = modified(crcDefend(round)._2+defender.main.terrain.def_mod_moral+guard_def)
            val df_res = getCRCResult(df_crc_cas,df_crc_moral,rolldf)
            val att_lost_u = getCasualtiesGenerated(df_res._1,def_attack_factors)
            val att_lost_m = df_res._2
            log("> Defender on table <b>("+df_crc_cas+","+df_crc_moral+")</b>(Original: "+crcDefend(round)._1+","+crcDefend(round)._2+", terrain modifiers: "+defender.main.terrain.def_mod_casualties+","+defender.main.terrain.def_mod_moral+", guard commitment: "+guard_def+") rolls with modifier "+def_modifier+" a <b>"+rolldf+"</b> and causes <u>-"+att_lost_u+" units/-"+att_lost_m+" moral</u>")
            att_current_factors -= att_lost_u
            att_attack_factors -= att_lost_u
            att_moral_lost += att_lost_m
            def_current_factors -= df_lost_u
            def_attack_factors -= df_lost_u
            def_moral_lost += df_lost_m

        }
        log("> Round "+round+" ends: Attacker <b>"+att_current_factors+" factors/"+attMoral()+" moral</b> -- Defender <b>"+
            def_current_factors+" factors/"+defMoral()+" moral</b>")
        
        verifyFactorsArtillery
        if(guard_att > 0 && !attackerWins) {
            log(">> Attacker commited the guard and did not win the combat. <b>Attacker breaks</b> (12.3.4.6)")
            att_guard_lost = true
        }
        if(guard_def > 0 && !defenderWins) {
            log(">> Defender commited the guard and did not win the combat. <b>Defender breaks</b> (12.3.4.6)")
            def_guard_lost = true
        }
    }

    protected def guardCommitment(round: Int) = {
        var (com_att,com_def) = (0,0)
        if(options.guard_commitment) {
            if(options.guard_attacker_round == round) {
                log(">> Attacker commits the <b>guard in round: "+round+" </b>")
                //find the first guard corp in main
                val list = if(!attack_outflank){ attacker.main.units } else { attacker.main.units ++ attacker.outflank.units }
                val corp = list.find(c => c.guard > 0) match {
                    case Some(c) => c
                    case None => log(">> Couldn't find a corp with guard units"); new Corp("", new England(), 0,0,0,0,0,0,0,0,0)
                }
                if(corp.guard > 0){
                    if (corp.country == England() || corp.country == EnglandNoDominant() || corp.country == Spain() || corp.country == SpainDominant()) {
                        log(">> The country "+corp.country+" can't use the guard (12.3.4.3)")
                    } else if((corp.country == Prussia() || corp.country == PrussiaDominant() || corp.country == Austria() || corp.country == AustriaDominant()) && options.guard_attacker_shift > 1) {
                        log(">> The country "+corp.country+" can't shift the guard +"+options.guard_attacker_shift+" moral (12.3.4.3)")
                    } else if(options.guard_attacker_shift > corp.guard) {
                        log(">> Can't proceed with commitment at "+options.guard_attacker_shift+", only "+corp.guard+" guards left")
                    } else {
                        log(">> Corp "+corp.name+" of "+corp.country+" commits at <b>"+options.guard_attacker_shift+"</b>")
                        val roll = D6()
                        val casualties = guardCommitmentTable(options.guard_attacker_shift, roll)                        
                        log(">> Commitment roll is "+roll+" and "+casualties+" guard die")
                        if(casualties >= corp.guard) {
                            log(">> All guard corps have died, attacker has lost the combat")
                            att_current_factors -= corp.guard
                            att_attack_factors -= corp.guard
                            att_guard_lost = true
                        } else {
                            att_current_factors -= casualties
                            att_attack_factors -= casualties
                            com_att = options.guard_attacker_shift
                        }
                    }
                }
            }
            if(options.guard_defender_round == round) {
                log(">> Defender commits the <b>guard in round: "+round+" </b>")
                //find the first guard corp in main
                val list = if(!defend_outflank){ defender.main.units } else { defender.main.units ++ defender.outflank.units }
                val corp = list.find(c => c.guard > 0) match {
                    case Some(c) => c
                    case None => log(">> Couldn't find a corp with guard units"); new Corp("", new England(), 0,0,0,0,0,0,0,0,0)
                }
                if(corp.guard > 0){
                    if (corp.country == England() || corp.country == EnglandNoDominant() || corp.country == Spain() || corp.country == SpainDominant()) {
                        log(">> The country "+corp.country+" can't use the guard (12.3.4.3)")
                    } else if((corp.country == Prussia() || corp.country == PrussiaDominant() || corp.country == Austria() || corp.country == AustriaDominant()) && options.guard_attacker_shift > 1) {
                        log(">> The country "+corp.country+" can't shift the guard +"+options.guard_attacker_shift+" moral (12.3.4.3)")
                    } else if(options.guard_attacker_shift > corp.guard) {
                        log(">> Can't proceed with commitment at "+options.guard_attacker_shift+", only "+corp.guard+" guards left")
                    } else {
                        log(">> Corp "+corp.name+" of "+corp.country+" commits at <b>"+options.guard_attacker_shift+"</b>")
                        val roll = D6()
                        val casualties = guardCommitmentTable(options.guard_attacker_shift, roll)
                        log(">> Commitment roll is "+roll+" and "+casualties+" guard of "+corp.guard+" die")
                        if(casualties >= corp.guard) {
                            log(">> All guard corps have died, defender has lost the combat")
                            def_current_factors -= corp.guard
                            def_attack_factors -= corp.guard
                            def_guard_lost = true
                        } else {
                            def_current_factors -= casualties
                            def_attack_factors -= casualties
                            com_def = options.guard_defender_shift
                        }
                    }
                }
            }
        }
        (com_att,com_def)
    }

    protected def verifyReinforcements = {
        if(att_reinforcements.size > 0) {
            for(a <- att_reinforcements) {
                //reinforce with a commander
                if(!a.commander.isCorp) { 
                    val roll = D6(a.terrain.reinforce_modifier)
                    log(">> Attacker's army commanded by "+a.commander.name+" tries to reinforce, rolls with modifier "+a.terrain.reinforce_modifier+" a "+roll+" vs strategic rating of "+a.commander.strategic)
                    if(roll <= a.commander.strategic + a.terrain.reinforce_modifier) {
                        log(">> Army reinforces with "+a.factors+" factors ("+a.cavalry+" cavalry, "+a.artillery+" artillery, "+a.guards+" guards)")
                        att_current_factors += a.factors
                        att_attack_factors += a.factors
                        att_cavalry += a.cavalry
                        att_artillery += a.artillery
                        att_corps += a.units.size
                        att_reinforcements -= a

                        if(a.commander.isCorp || (a.commander.country == att_commander.country && a.commander.seniority < att_commander.seniority)){
                            log(">> Reinforcing commander "+a.commander.name+" becomes the new commander of the attacker")
                            att_commander = a.commander
                        }
                    } else {
                        log(">> Army fails to reinforce")
                    }
                } else {
                    //each corp tries individually                    
                    for(c <- a.units) {
                        //we check the corp has not reinforced already
                        if(!att_reinforcements_names.contains(c.name)){
                            val roll = D6()
                            log(">> Attacker's army corp "+c.name+" tries to reinforce, rolls with modifier "+a.terrain.reinforce_modifier+" a "+roll+" vs strategic rating of "+c.strategic)
                            if(roll <= c.strategic + a.terrain.reinforce_modifier) {
                                log(">> Corp reinforces with "+c.factors+" factors ("+c.cavalry+" cavalry, "+c.artillery+" artillery, "+c.guard+" guards)")
                                att_current_factors += c.factors
                                att_attack_factors += c.factors
                                att_cavalry += c.cavalry
                                att_artillery += c.artillery
                                att_corps += 1
                                att_reinforcements_names += c.name
                            } else {
                                log(">> Corp fails to reinforce")
                            }
                        }
                    }
                }
            }
        }
        if(def_reinforcements.size > 0) {
            for(a <- def_reinforcements) {
                //reinforce with a commander
                if(!a.commander.isCorp) {
                    val roll = D6(a.terrain.reinforce_modifier)
                    log(">> Defender's army commanded by "+a.commander.name+" tries to reinforce, rolls with modifier "+a.terrain.reinforce_modifier+" a "+roll+" vs strategic rating of "+a.commander.strategic)
                    if(roll <= a.commander.strategic + a.terrain.reinforce_modifier) {
                        log(">> Army reinforces with "+a.factors+" factors ("+a.cavalry+" cavalry, "+a.artillery+" artillery, "+a.guards+" guards)")
                        def_current_factors += a.factors
                        def_attack_factors += a.factors
                        def_cavalry += a.cavalry
                        def_artillery += a.artillery
                        def_corps += a.units.size
                        def_reinforcements -= a

                        if(a.commander.isCorp || (a.commander.country == def_commander.country && a.commander.seniority < def_commander.seniority)){
                            log(">> Reinforcing commander "+a.commander.name+" becomes the new commander of the defender")
                            def_commander = a.commander
                        }
                    } else {
                        log(">> Army fails to reinforce")
                    }
                } else {
                    //each corp tries individually                    
                    for(c <- a.units) {
                        //we check the corp has not reinforced already
                        if(!def_reinforcements_names.contains(c.name)){
                            val roll = D6()
                            log(">> Defender's army corp "+c.name+" tries to reinforce, rolls with modifier "+a.terrain.reinforce_modifier+" a "+roll+" vs strategic rating of "+c.strategic)
                            if(roll <= c.strategic + a.terrain.reinforce_modifier) {
                                log(">> Corp reinforces with "+c.factors+" factors ("+c.cavalry+" cavalry, "+c.artillery+" artillery, "+c.guard+" guards)")
                                def_current_factors += c.factors
                                def_attack_factors += c.factors
                                def_cavalry += c.cavalry
                                def_artillery += c.artillery
                                def_corps += 1
                                def_reinforcements_names += c.name
                            } else {
                                log(">> Corp fails to reinforce")
                            }
                        }
                    }
                }
            }
        }

    }

    protected def verifyFactorsArtillery = {
        if(options.artillery_bombardment){
            if(att_current_factors == att_artillery){
                log(">>12.3.5 - All remaining factors in combat for attacker are artillery. Attacker has lost the combat")
            }
            if(def_current_factors == def_artillery){
                log(">>12.3.5 - All remaining factors in combat for defender are artillery. Defender has lost the combat")
            }
        }
    }

    protected def artilleryBombardment(att_modifier: Int, def_modifier: Int) = {
        if(options.artillery_bombardment){
            if(att_artillery == 0 && def_artillery == 0){
                log(">>12.3.5 - No artillery present, there is no bombardment")
            } else if (attacker.main.terrain == Marsh()){
                log(">>12.3.5 - Terrain is marsh, there is no bombardment")
            } else {
                log(">>12.3.5 - Bombardment. Artillery attacker "+att_artillery+" factors vs artillery defender: "+def_artillery+" factors")
                var (att_lost,def_lost) = (0,0)
                if(att_artillery > 0){
                    val roll = D6(att_modifier)
                    def_lost = getCasualtiesGenerated(getCRCResult(5,5,roll)._1,att_artillery)
                    log(">>Attacker bombardment (table 5-5) rolls with modifier "+att_modifier+" a "+roll+" and causes "+def_lost+" casualties")
                }
                if(def_artillery > 0){
                    val roll = D6(def_modifier)
                    att_lost = getCasualtiesGenerated(getCRCResult(5,5,roll)._1, def_artillery)
                    log(">>Attacker bombardment (table 5-5) rolls with modifier "+def_modifier+" a "+roll+" and causes "+att_lost+" casualties")
                }
                att_current_factors -= att_lost
                att_attack_factors -= att_lost
                def_current_factors -= def_lost
                def_attack_factors -= def_lost
            }
        }
    }

    protected def tryWithdraw(modifier: Int) : Any = {
        var all_cavalry_bonus = 0
        if(options.calvary_withdrawal){
            if((att_cavalry == att_current_factors) && (def_cavalry == def_current_factors)){
                log(">>12.3.3.2 - Both armies consist of only cavalry, cavalry withdrawal rule doesn't apply")
            } else {
                log(">>12.3.3.2 - Checking cavalry withdrawal rule. Cavalry factors: "+def_cavalry+" vs Total factors: "+def_current_factors+" factors")
                if(def_cavalry == def_current_factors){
                    all_cavalry_bonus = 1
                    log(">>+1 bonus to strategic rating on withdrawal due to rule 12.3.3.2")
                } else {
                    log(">>No bonus to strategic rating on withdrawal due to rule 12.3.3.2")
                }
            }
        }

        if(!def_commander.isCorp) {
            val roll = D6()
            log("Defender tries to <b>withdraw</b>: rolls <b>"+roll+"</b> vs strategic "+def_commander.strategic+" with modifier "+ modifier+" due to tactics and "+all_cavalry_bonus+" due to optional rules")
            if(roll <= def_commander.strategic + modifier + all_cavalry_bonus){
                defender_withdraws = true
                log("<b>Defender withdraws</b>")
            }
        } else {
            log("No commander, each corp tries to withdraw by itself")
            for(c <- (defender.main.units ++ defender.outflank.units)){
                val roll = D6()
                log(c.name+" tries to <b>withdraw</b>: rolls <b>"+roll+"</b> vs strategic strategic "+c.strategic+" with modifier " + (modifier+all_cavalry_bonus))
                if(roll <= c.strategic + modifier + all_cavalry_bonus){
                    log("<b>"+c.name+" withdraws</b>")
                    def_current_factors -= c.factors
                    def_attack_factors -= c.factors
                    def_cavalry -= c.total_cavalry
                    def_artillery -= c.artillery
                    def_corps -= 1
                } else {
                    log(c.name+" fails withdraw")
                }
            }
            if(def_corps <= 0){
                defender_withdraws = true
                log("All defending units have withdrawn successfully")
            }
        }
    }

    protected def verifyOutflank(combatant: Combatant, name: String) = {
        val has_corps = combatant.main.units.size >= 1 && combatant.outflank.units.size >= 1 //we check there are enough corps
        val main_only_art = combatant.main.units.foldLeft(true)((x,c) => x && c.isArtillery) //we check main force has some corp besides artillery
        val bad_main = combatant.main.units.foldLeft(false)((x,c) => x || c.isArtillery || c.isCossack)
        val bad_outflank = combatant.outflank.units.foldLeft(false)((x,c) => x || c.isArtillery || c.isGuerrilla)
        if(att_commander.isCorp || !has_corps || main_only_art || bad_main || bad_outflank) {
            cant_outflank = true
            log("<b>Outflank tactic can't be chosen by "+name+"</b> as it doesn't follow the requirements: presence of a leader, at least 2 non-artillery corps and at least 1 non-artillery corp plus all artillery and guerrillas in the main force. All cossacks/freikcorps must be on outflank forces.")
        }
    }

    protected def outflankAttacker(modifier: Int) = {
        if(!attack_outflank) {
            val roll = D6()
            log("Attacker checks if <b>outflank</b> forces arrive: rolls <b>"+roll+"</b> vs strategic "+att_commander.strategic+" modified "+attacker.outflank.terrain.outflank_modifier+" by terrain and "+modifier+" by round")
            if(roll <= att_commander.strategic + attacker.outflank.terrain.outflank_modifier+modifier){
                att_current_factors += attacker.outflank.factors
                att_attack_factors += 2*attacker.outflank.factors
                att_cavalry += attacker.outflank.cavalry
                att_artillery += attacker.outflank.artillery
                att_corps += attacker.outflank.units.size
                attack_outflank = true
                defend_outflank = true
                log("Attacker <b>outflank corps arrive</b>")
            } else {
                log("Attacker outflank corps don't arrive")
            }
        }
    }

    protected def outflankDefender(modifier: Int) = {
        if(!defend_outflank) {
            val roll = D6()
            log("Defender checks if <b>outflank</b> forces arrive: rolls <b>"+roll+"</b> vs strategic "+def_commander.strategic+" modified "+defender.outflank.terrain.outflank_modifier+" by terrain and "+modifier+" by round")
            if(roll <= def_commander.strategic + defender.outflank.terrain.outflank_modifier+modifier){
                def_current_factors += defender.outflank.factors
                def_attack_factors += 2*defender.outflank.factors
                def_cavalry += defender.outflank.cavalry
                def_artillery += defender.outflank.artillery
                def_corps += defender.outflank.units.size
                attack_outflank = true
                defend_outflank = true
                log("Defender <b>outflank corps arrive</b>")
            } else {
                log("Defender outflank corps don't arrive")
            }
        }
    }

    //methods for log of actions
    protected val logfile = new scala.collection.mutable.ListBuffer[String]
    protected def log(s: String) = {
        logfile += s
    }
    def getLog  = {
        logfile.mkString("<br/>")
    }
}

/**
 * From here down there are concrete combat pairings of tactics, attacker vs defender, with the specific modifiers that apply to each one
 */

class OutflankOutflank(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    protected val logid = "Outflank-Outflank"
    protected val attack_main_crc = List((1,2),(1,4),(2,4))
    protected val attack_sec_crc = List((0,0))
    protected val defend_main_crc = List((1,2),(1,4),(2,4))
    protected val defend_sec_crc = List((0,0))

    override protected def precombat = {
        super.precombat
        log("Due to tactics, there is <b>no outflank</b>")
    }
}

class OutflankCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    protected val logid = "Outflank-Counterattack"
    protected val attack_main_crc = List((2,1),(2,1),(2,2))
    protected val attack_sec_crc = List((0,0),(4,4),(4,4))
    protected val defend_main_crc = List((2,3),(3,3),(3,3))
    protected val defend_sec_crc = List((0,0),(3,1),(3,1))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        att_current_factors = attacker.main.factors
        att_cavalry = attacker.main.cavalry
        att_artillery = attacker.main.artillery
        verifyOutflank(attacker, "attacker")
    }

    override protected def preR2 = {
        super.preR2
        outflankAttacker(+0)
    }

    override protected def preR3 = {
        super.preR3
        outflankAttacker(+2)
    }
}

class OutflankEscalatedCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    protected val logid = "Outflank-EscalatedCounterattack"
    protected val attack_main_crc = List((3,1),(3,1),(3,2))
    protected val attack_sec_crc = List((0,0),(5,4),(5,4))
    protected val defend_main_crc = List((3,3),(4,3),(4,3))
    protected val defend_sec_crc = List((0,0),(4,1),(4,1))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        att_current_factors = attacker.main.factors
        att_cavalry = attacker.main.cavalry
        att_artillery = attacker.main.artillery
        verifyOutflank(attacker, "attacker")
    }

    override protected def preR2 = {
        super.preR2
        outflankAttacker(+0)
    }

    override protected def preR3 = {
        super.preR3
        outflankAttacker(+2)
    }
}

class OutflankCordon(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    protected val logid = "Outflank-Cordon"
    protected val attack_main_crc = List((2,1),(3,1),(2,1))
    protected val attack_sec_crc = List((0,0))
    protected val defend_main_crc = List((3,2),(4,2),(4,3))
    protected val defend_sec_crc = List((0,0))

    override protected def precombat = {
        super.precombat
        log("Due to tactics, there is <b>no outflank</b>")
    }

}

class OutflankCordonRiver(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    protected val logid = "Outflank-Cordon(river)"
    protected val attack_main_crc = List((2,1),(3,1),(2,1))
    protected val attack_sec_crc = List((0,0))
    protected val defend_main_crc = List((3,2),(4,2),(4,3))
    protected val defend_sec_crc = List((0,0))

    override protected def precombat = {
        super.precombat
        log("Due to tactics, there is <b>no outflank</b>")
    }

}

class OutflankWithdraw(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    protected val logid = "Outflank-Withdraw"
    protected val attack_main_crc = List((2,2),(0,0),(0,0))
    protected val attack_sec_crc = List((0,0),(3,4),(3,4))
    protected val defend_main_crc = List((1,1),(0,0),(0,0))
    protected val defend_sec_crc = List((0,0),(1,1),(1,1))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        att_current_factors = attacker.main.factors
        att_cavalry = attacker.main.cavalry
        att_artillery = attacker.main.artillery
        
        verifyOutflank(attacker, "attacker")
        if(!cant_outflank) tryWithdraw(+1)
    }

    override protected def preR2 = {
        super.preR2
        outflankAttacker(+0)
        if(!attack_outflank) {
            defender_withdraws = true
            log("<b>Defender withdraws due to outflank force not arriving</b>")
        }
    }

    override protected def preR3 = {
        super.preR3
        outflankAttacker(+2)
    }
}


class OutflankDefend(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    protected val logid = "Outflank-Defend"
    protected val attack_main_crc = List((2,1),(3,1),(3,1))
    protected val attack_sec_crc = List((0,0),(2,4),(4,4))
    protected val defend_main_crc = List((3,1),(4,1),(4,2))
    protected val defend_sec_crc = List((0,0),(1,1),(1,1))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        att_current_factors = attacker.main.factors
        att_cavalry = attacker.main.cavalry
        att_artillery = attacker.main.artillery

        verifyOutflank(attacker, "attacker")
    }

    override protected def preR2 = {
        super.preR2
        log("Due to tactics attacking commander has <b>+1 to strategic rating</b>")
        outflankAttacker(+1)
    }

    override protected def preR3 = {
        super.preR3
        log("Due to tactics attacking commander has <b>+1 to strategic rating</b>")
        outflankAttacker(+3)
    }
}

class AssaultOutflank(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Assault-Outflank"
    val attack_main_crc = List((2,3),(3,3),(3,3))
    val attack_sec_crc = List((0,0),(3,1),(3,1))
    val defend_main_crc = List((2,1),(2,1),(2,2))
    val defend_sec_crc = List((0,0),(4,4),(4,4))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        def_current_factors = defender.main.factors
        def_cavalry = defender.main.cavalry
        def_artillery = defender.main.artillery
        def_corps = defender.main.units.size

        verifyOutflank(defender, "defender")
    }

    override protected def preR2 = {
        super.preR2
        outflankDefender(+0)
    }

    override protected def preR3 = {
        super.preR3
        outflankDefender(+2)
    }
}

class AssaultCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Assault-Counterattack"
    val attack_main_crc = List((3,1),(4,2),(3,2))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,1),(4,2),(3,2))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        //a sides morale level is increased by +1 if the commander is Turkish
        if(att_commander.country == Turkey() || att_commander.country == TurkeyDominant()){
            log("Due to tactics and the attacker commander being <b>Turkish</b>, attacker gets <b>+1 moral</b>")
            att_moral_lost -= 1
        }
        if(def_commander.country == Turkey() || def_commander.country == TurkeyDominant()){
            log("Due to tactics and the defender commander being <b>Turkish</b>, defender gets <b>+1 moral</b>")
            def_moral_lost -= 1
        }
    }

}

class AssaultEscalatedCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Assault-EscalatedCounterattack"
    val attack_main_crc = List((4,1),(5,2),(4,2))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((4,1),(5,2),(4,2))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        //a sides morale level is increased by +1 if the commander is Turkish
        if(att_commander.country == Turkey() || att_commander.country == TurkeyDominant()){
            log("Due to tactics and the attacker commander being <b>Turkish</b>, attacker gets <b>+1 moral</b>")
            att_moral_lost -= 1
        }
        if(def_commander.country == Turkey() || def_commander.country == TurkeyDominant()){
            log("Due to tactics and the defender commander being <b>Turkish</b>, defender gets <b>+1 moral</b>")
            def_moral_lost -= 1
        }
    }

}

class AssaultCordon(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Assault-Cordon"
    val attack_main_crc = List((4,1),(4,3),(4,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((2,1),(2,1),(3,1))
    val defend_sec_crc = List((0,0))

}

class AssaultCordonRiver(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Assault-Cordon(river)"
    val attack_main_crc = List((4,1),(4,2),(4,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((2,1),(3,1),(4,1))
    val defend_sec_crc = List((0,0))

}

class AssaultWithdraw(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Assault-Withdraw"
    val attack_main_crc = List((4,2),(4,2),(4,3))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((1,1),(1,1),(1,1))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        tryWithdraw(+0)
    }
}

class AssaultDefend(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Assault-Defend"
    val attack_main_crc = List((3,1),(3,1),(2,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((4,1),(4,2),(4,3))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        log("Due to tactics, the <b>commander chart is not used</b>, no tactical ratings modifier to dice applies")
        use_commander_chart = false
        //defender morale level is increased by +1 if the commander is Russian
        if(def_commander.country == Russia() || def_commander.country == RussiaDominant()){
            log("Due to tactics and the defender commander being <b>Russian</b>, defender gets <b>+1 moral</b>")
            def_moral_lost -= 1
        }
    }
}

class EscalatedAssaultOutflank(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "EscalatedAssault-Outflank"
    val attack_main_crc = List((3,3),(4,3),(4,3))
    val attack_sec_crc = List((0,0),(4,1),(4,1))
    val defend_main_crc = List((3,1),(3,1),(3,2))
    val defend_sec_crc = List((0,0),(5,4),(5,4))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        def_current_factors = defender.main.factors
        def_cavalry = defender.main.cavalry
        def_artillery = defender.main.artillery
        def_corps = defender.main.units.size

        verifyOutflank(defender, "defender")
    }

    override protected def preR2 = {
        super.preR2
        outflankDefender(+0)
    }

    override protected def preR3 = {
        super.preR3
        outflankDefender(+2)
    }
}

class EscalatedAssaultCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "EscalatedAssault-Counterattack"
    val attack_main_crc = List((4,1),(5,2),(4,2))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((4,1),(5,2),(4,2))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        //a sides morale level is increased by +1 if the commander is Turkish
        if(att_commander.country == Turkey() || att_commander.country == TurkeyDominant()){
            log("Due to tactics and the attacker commander being <b>Turkish</b>, attacker gets <b>+1 moral</b>")
            att_moral_lost -= 1
        }
        if(def_commander.country == Turkey() || def_commander.country == TurkeyDominant()){
            log("Due to tactics and the defender commander being <b>Turkish</b>, defender gets <b>+1 moral</b>")
            def_moral_lost -= 1
        }
    }

}

class EscalatedAssaultEscalatedCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "EscalatedAssault-EscalatedCounterattack"
    val attack_main_crc = List((4,1),(5,2),(4,2))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((4,1),(5,2),(4,2))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        //a sides morale level is increased by +1 if the commander is Turkish
        if(att_commander.country == Turkey() || att_commander.country == TurkeyDominant()){
            log("Due to tactics and the attacker commander being <b>Turkish</b>, attacker gets <b>+1 moral</b>")
            att_moral_lost -= 1
        }
        if(def_commander.country == Turkey() || def_commander.country == TurkeyDominant()){
            log("Due to tactics and the defender commander being <b>Turkish</b>, defender gets <b>+1 moral</b>")
            def_moral_lost -= 1
        }
    }

}

class EscalatedAssaultCordon(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "EscalatedAssault-Cordon"
    val attack_main_crc = List((5,1),(5,3),(5,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,1),(3,1),(4,1))
    val defend_sec_crc = List((0,0))

}

class EscalatedAssaultCordonRiver(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "EscalatedAssault-Cordon(river)"
    val attack_main_crc = List((5,1),(5,2),(5,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,1),(4,1),(5,1))
    val defend_sec_crc = List((0,0))

}

class EscalatedAssaultWithdraw(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    val logid = "EscalatedAssault-Withdraw"
    val attack_main_crc = List((5,2),(5,2),(5,3))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((2,1),(2,1),(2,1))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        tryWithdraw(+0)
    }
}

class EscalatedAssaultDefend(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "EscalatedAssault-Defend"
    val attack_main_crc = List((3,1),(3,1),(2,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((5,1),(5,2),(5,3))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        log("Due to tactics, the <b>commander chart is not used</b>, no tactical ratings modifier to dice applies")
        use_commander_chart = false
        //defender morale level is increased by +1 if the commander is Russian
        if(def_commander.country == Russia() || def_commander.country == RussiaDominant()){
            log("Due to tactics and the defender commander being <b>Russian</b>, defender gets <b>+1 moral</b>")
            def_moral_lost -= 1
        }
    }
}

class EchelonOutflank(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Echelon-Outflank"
    val attack_main_crc = List((2,1),(3,1),(3,2))
    val attack_sec_crc = List((0,0),(2,1),(1,1))
    val defend_main_crc = List((2,1),(2,1),(2,1))
    val defend_sec_crc = List((0,0),(3,3),(3,4))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        def_current_factors = defender.main.factors
        def_cavalry = defender.main.cavalry
        def_artillery = defender.main.artillery
        def_corps = defender.main.units.size

        verifyOutflank(defender, "defender")
    }

    override protected def preR2 = {
        super.preR2
        outflankDefender(+0)
    }

    override protected def preR3 = {
        super.preR3
        outflankDefender(+2)
    }
}

class EchelonCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Echelon-Counterattack"
    val attack_main_crc = List((1,2),(3,4),(2,4))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,1),(4,1), (2,1))
    val defend_sec_crc = List((0,0))

}

class EchelonEscalatedCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Echelon-EscalatedCounterattack"
    val attack_main_crc = List((2,2),(4,4),(3,4))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,1),(4,1), (2,1))
    val defend_sec_crc = List((0,0))
}

class EchelonCordon(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Echelon-Cordon"
    val attack_main_crc = List((2,1),(3,1),(4,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,1),(4,2), (4,3))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        //crc rolls is increased by +1 if the commander is from Austria
        if(att_commander.country == Austria() || att_commander.country == AustriaDominant()){
            log("Due to tactics and the attacker commander being from <b>Austria</b>, attacker gets <b>+1 to rolls</b>")
            att_echelon_bonus = true
        }
        if(def_commander.country == Austria() || def_commander.country == AustriaDominant()){
            log("Due to tactics and the defender commander being from <b>Austria</b>, defender gets <b>+1 to rolls</b>")
            def_echelon_bonus = true
        }
    }

}

class EchelonCordonRiver(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Echelon-Cordon(river)"
    val attack_main_crc = List((2,1),(3,1),(3,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((4,1),(4,2), (4,3))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        //crc rolls is increased by +1 if the commander is from Austria
        if(att_commander.country == Austria() || att_commander.country == AustriaDominant()){
            log("Due to tactics and the attacker commander being from <b>Austria</b>, attacker gets <b>+1 to rolls</b>")
            att_echelon_bonus = true
        }
        if(def_commander.country == Austria() || def_commander.country == AustriaDominant()){
            log("Due to tactics and the defender commander being from <b>Austria</b>, defender gets <b>+1 to rolls</b>")
            def_echelon_bonus = true
        }
    }
}


class EchelonWithdraw(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {
    val logid = "Echelon-Withdraw"
    val attack_main_crc = List((3,1),(3,2),(3,3))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((1,1),(1,1),(2,1))
    val defend_sec_crc = List((0,0))

    override protected def precombat : Any = {
        super.precombat
        tryWithdraw(+1)
    }
}

class EchelonDefend(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Echelon-Defend"
    val attack_main_crc = List((1,3),(2,3),(2,4))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((4,1),(3,1), (2,1))
    val defend_sec_crc = List((0,0))
}


class ProbeOutflank(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Probe-Outflank"
    val attack_main_crc = List((2,1),(4,2),(4,3))
    val attack_sec_crc = List((0,0),(4,2),(4,2))
    val defend_main_crc = List((2,1),(2,1),(1,1))
    val defend_sec_crc = List((0,0),(2,3),(2,2))

    override protected def precombat = {
        super.precombat
        //factors in combat for outflank
        def_current_factors = defender.main.factors
        def_cavalry = defender.main.cavalry
        def_artillery = defender.main.artillery
        def_corps = defender.main.units.size

        verifyOutflank(defender, "defender")
    }

    override protected def preR2 = {
        super.preR2
        outflankDefender(+0)
    }

    override protected def preR3 = {
        super.preR3
        outflankDefender(+2)
    }
}

class ProbeCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Probe-Counterattack"
    val attack_main_crc = List((1,1),(1,3),(2,2))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,2),(3,2), (4,2))
    val defend_sec_crc = List((0,0))
}


class ProbeEscalatedCounterattack(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Probe-EscalatedCounterattack"
    val attack_main_crc = List((2,1),(2,3),(3,2))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((4,2),(4,2),(5,2))
    val defend_sec_crc = List((0,0))
}

class ProbeCordon(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Probe-Cordon"
    val attack_main_crc = List((1,1),(4,2),(4,2))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((1,2),(2,1),(3,1))
    val defend_sec_crc = List((0,0))
}

class ProbeCordonRiver(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Probe-Cordon(river)"
    val attack_main_crc = List((1,1),(4,1),(4,1))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((1,2),(3,1),(3,1))
    val defend_sec_crc = List((0,0))
}

class ProbeWithdraw(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Probe-Withdraw"
    val attack_main_crc = List((0,0))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((0,0))
    val defend_sec_crc = List((0,0))

    override protected def precombat: Any = {
        super.precombat
        //withdraw
        defender_withdraws = true
        log("<b>The defender withdraws automatically due to tactics</b>")
    }
}

class ProbeDefend(attacker: Combatant, defender: Combatant, options: Options) extends Combat(attacker, defender, options) {

    val logid = "Probe-Defend"
    val attack_main_crc = List((1,1),(3,1),(3,3))
    val attack_sec_crc = List((0,0))
    val defend_main_crc = List((3,2),(3,1),(2,1))
    val defend_sec_crc = List((0,0))


    override protected def precombat : Any = {
        super.precombat
        //defender morale level is increased by +1 if the commander is Russian
        if(def_commander.country == Russia() || def_commander.country == RussiaDominant()){
            log("Due to tactics and the defender commander being <b>Russian</b>, defender gets <b>+1 moral</b>")
            def_moral_lost -= 1
        }
    }
}

/**
 * Helper object with methods used in combat, mostly methods to return values from charts
 */
object Combat {

    //commander modifiers table
    val commmander_ratings = new Array[Array[Tuple2[Int, Int]]](6,6)
    commmander_ratings(0) = Array( (-1,-1), (0,-1), (0,-1), (1,-1), (1,-1), (1,-1) )
    commmander_ratings(1) = Array( (-1,0), (-1,-1), (0,-1), (0,-1), (1,-1), (1,-1) )
    commmander_ratings(2) = Array( (-1,0), (-1,0), (0,0), (0,-1), (1,0), (1,-1) )
    commmander_ratings(3) = Array( (-1,1), (-1,0), (-1,0), (0,0), (1,0), (1,0) )
    commmander_ratings(4) = Array( (-1,1), (-1,1), (0,1), (0,1), (1,1), (1,0) )
    commmander_ratings(5) = Array( (-1,1), (-1,1), (-1,1), (0,1), (0,1), (1,1) )

    //returns tactic modifiers according to tactical rating. Pos 1: attacker, 2: defender
    def getCommanderModifiers(attacker: Int, defender: Int, use_commander_chart: Boolean) : Tuple2[Int, Int] = {
        if(use_commander_chart) {
            commmander_ratings(defender)(attacker)
        } else {
            (0,0)
        }
    }


    //casualties table
    val map_casualty_table = Map(5 -> 0,10 -> 1,15 -> 2,20 -> 3,25 -> 4,30 -> 5,45 -> 6,60 -> 7,75 -> 8,90 -> 9,120 -> 10)
    val casualty_table = new Array[Array[Int]](11,20)
    casualty_table(0) = Array( 0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1 )
    casualty_table(1) = Array( 0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2 )
    casualty_table(2) = Array( 0,0,0,1,1,1,1,1,1,2,2,2,2,2,2,2,3,3,3,3 )
    casualty_table(3) = Array( 0,0,1,1,1,1,1,2,2,2,2,2,3,3,3,3,3,4,4,4 )
    casualty_table(4) = Array( 0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5 )
    casualty_table(5) = Array( 0,1,1,1,2,2,2,2,3,3,3,4,4,4,5,5,5,5,6,6 )
    casualty_table(6) = Array( 1,1,1,2,2,3,3,4,4,5,5,5,6,6,7,7,8,8,9,9 )
    casualty_table(7) = Array( 1,1,2,2,3,4,4,5,5,6,7,7,8,8,9,10,10,11,11,12 )
    casualty_table(8) = Array( 1,2,2,3,4,5,5,6,7,8,8,9,10,11,11,12,13,14,14,15 )
    casualty_table(9) = Array( 1,2,3,4,5,5,6,7,8,9,10,11,12,13,14,14,15,16,17,18 )
    casualty_table(10) = Array( 1,2,4,5,6,7,8,10,11,12,13,14,16,17,18,19,20,22,23,24 )

    def getCasualtiesGenerated(percentage: Int, units: Int ) : Int = {
        percentage match {
            case 0 => 0
            case _ =>  units <= 20 match {
                    case false => casualty_table(map_casualty_table(percentage))(20-1) + getCasualtiesGenerated(percentage, units - 20)
                    case true => units <= 0 match {
                            case false =>  casualty_table(map_casualty_table(percentage))(units-1)
                            case true => 0
                        }
                }
        }
    }

    //CRC table
    val table = new Array[Array[Array[Tuple2[Int, Double]]]](5,5,8)
    //Table 1-1
    table(0)(0) = Array((0,0), (0,0), (0,0), (0,0), (0, .2), (5, .4), (5, .6), (5, .8))
    //Table 2-1
    table(1)(0) = Array( (0,0), (0,0), (0,0), (5, .2), (5, .5), (5, .8), (10, 1.1), (10, 1.4) )
    //Table 3-1
    table(2)(0) = Array( (0,0), (0,0), (5, .2), (5, .5), (10, .8), (10, 1.1), (10, 1.5), (15, 1.9) )
    //Table 4-1
    table(3)(0) = Array( (0,0), (5, .2), (5, .4), (10, .7), (10, 1), (15, 1.4), (15, 1.9), (15, 2.4) )
    //Table 5-1
    table(4)(0) = Array( (5, .1), (5, .3), (10, .6), (10, 1), (15, 1.4), (15, 1.8), (15, 2.2), (20, 2.6) )

    //Table 1-2
    table(0)(1) = Array( (0,0), (0,0), (0, .2), (0, .5), (5, .8), (5, 1.1), (5, 1.5), (10, 1.9) )
    //Table 2-2
    table(1)(1) = Array( (0,0), (0, .2), (5, .4), (5, .7), (5, 1), (10, 1.4), (10, 1.9), (10, 2.4) )
    //Table 3-2
    table(2)(1) = Array( (0, .1), (5, .3), (5, .6), (5, 1), (10, 1.4), (10, 1.8), (15, 2.2), (15, 2.6) )
    //Table 4-2
    table(3)(1) = Array( (0, .4), (5, .7), (5, 1), (10, 1.3), (15, 1.6), (15, 1.9), (15, 2.3), (20, 2.8) )
    //Table 5-2
    table(4)(1) = Array( (5, .5), (10, .8), (10, 1.1), (10, 1.4), (15, 1.8), (15, 2.3), (20, 2.8), (20, 3.3) )

    //Table 1-3
    table(0)(2) = Array( (0,0), (0, .3), (0, .6), (5, .9), (5, 1.3), (5, 1.8), (10, 2.3), (10, 2.8) )
    //Table 2-3
    table(1)(2) = Array( (0, .3), (0, .5), (5, .8), (5, 1.1), (10, 1.5), (10, 2), (10, 2.6), (15, 3.2) )
    //Table 3-3
    table(2)(2) = Array( (0, .5), (5, .8), (5, 1.1), (10, 1.4), (10, 1.8), (15, 2.3), (15, 2.8), (15, 3.3) )
    //Table 4-3
    table(3)(2) = Array( (5, .8), (5, 1), (10, 1.3), (10, 1.6), (15, 2), (15, 2.5), (15, 3.1), (20, 3.7) )
    //Table 5-3
    table(4)(2) = Array( (5, .9), (10, 1.1), (10, 1.4), (15, 1.8), (15, 2.3), (20, 2.9), (20, 3.5), (20, 4.1) )

    //Table 1-4
    table(0)(3) = Array( (0, .4), (0, .6), (5, .9), (5, 1.3), (5, 1.8), (10, 2.4), (10, 3), (10, 3.6) )
    //Table 2-4
    table(1)(3) = Array( (0, .6), (5, .9), (5, 1.2), (5, 1.6), (10, 2.1), (10, 2.6), (15, 3.2), (15, 3.8) )
    //Table 3-4
    table(2)(3) = Array( (5, .9), (5, 1.1), (5, 1.4), (10, 1.8), (10, 2.3), (15, 2.9), (15, 3.5), (20, 4.1) )
    //Table 4-4
    table(3)(3) = Array( (5, 1.1), (10, 1.4), (10, 1.7), (10, 2.1), (15, 2.6), (15, 3.1), (20, 3.7), (20, 4.3) )
    //Table 5-4
    table(4)(3) = Array( (5, 1.3), (10, 1.6), (15, 2), (15, 2.4), (15, 2.9), (20, 3.4), (20, 3.9), (25, 4.5) )

    //Table 1-5
    table(0)(4) = Array( (0, .9), (5, 1.1), (5, 1.4), (5, 1.8), (5, 2.3), (10, 2.9), (10, 3.5), (15, 4.1) )
    //Table 2-5
    table(1)(4) = Array( (5, 1.1), (5, 1.4), (5, 1.7), (10, 2.1), (10, 2.6), (10, 3.1), (15, 3.7), (15, 4.3) )
    //Table 3-5
    table(2)(4) = Array( (5, 1.4), (5, 1.6), (10, 1.9), (10, 2.3), (15, 2.8), (15, 3.4), (15, 4), (20, 4.6) )
    //Table 4-5
    table(3)(4) = Array( (5, 1.6), (10, 1.9), (10, 2.2), (15, 2.6), (15, 3.1), (20, 3.6), (20, 4.2), (20, 4.8) )
    //Table 5-5
    table(4)(4) = Array( (5, 1.8), (10, 2.1), (15, 2.5), (15, 2.9), (20, 3.4), (20, 3.9), (25, 4.4), (25, 5) )


    //returns the casualties (unit %, moral )
    def getCRCResult(casualty: Int, moral: Int, roll: Int): Tuple2[Int, Double] = {
        table(casualty-1)(moral-1)(roll)
    }

    //guard commitment
    val guard_table = new Array[Array[Int]](3,6)
    guard_table(0) = Array(0,0,0,0,0,0)
    guard_table(1) = Array(0,0,1,1,2,2)
    guard_table(2) = Array(1,1,2,2,3,4)

    def guardCommitmentTable(commitment: Int, roll: Int) : Int = {
        guard_table(commitment)(roll)
    }
}