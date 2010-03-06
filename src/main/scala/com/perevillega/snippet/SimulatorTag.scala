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
 * SimulatorTag.scala
 *
 * Retrieves the values of the Ajax/Json form and launches the simulation
 */

package com.perevillega.snippet


import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._

import Helpers._
import SHtml._
import S._
import js._
import JsCmds._

import com.perevillega.lib.utils._
import Utils._

class SimulatorTag {
        
    //json form header
    def jhead (xhtml : NodeSeq) : NodeSeq =  (<head>{Script(jsonwebform.jsCmd)}</head>)

    //json form body
    def jform (xhtml : NodeSeq) : NodeSeq = {
        jsonForm(jsonwebform, xhtml)
    }
    
    object jsonwebform extends JsonHandler {        

        def apply(in: Any): JsCmd ={
            in match {
                case j @ JsonCmd("processForm", _, p: Map[String, String], _) => {
                        //add empty notice to clean old messages from buffer (in case we don't show any new messages this iteration)
                        S.notice("")
                        runCombats(p)
                    }
                case x => S.error("Couldn't handle the data"); Noop;
            }                      
        }

    //launches the simulation
    private def runCombats(p: Map[String, String]): JsCmd = {

            def getTactic(s: Char) = {
                s match {
                    case 'o' => "Outflank"
                    case 'c' => "Counterattack"
                    case 'e' => "Escalated Counterattack"
                    case 'n' => "Cordon"
                    case 'r' => "Cordon"
                    case 'w' => "Withdraw"
                    case 'd' => "Defend"
                    case 'a' => "Assault"
                    case 's' => "Escalated Assault"
                    case 'l' => "Echelon"
                    case 'p' => "Probe"
                }
            }

            val combatslog = new scala.collection.mutable.ListBuffer[String]

            val (attacker, defender, options) = Combatant.buildCombatants(p)
            //display info on sidebars of tables
            val side_att = SetHtml("att_side", {scala.xml.Unparsed(getSideData(attacker))})
            val side_df = SetHtml("df_side", {scala.xml.Unparsed(getSideData(defender))})

            //run combats
            var table : JsCmd = JsCmds.Noop
            var (att_tactic, att_pct, def_tactic, def_pct) = ("",0.0,"",0.0)
            val tactics = List("oo","oc","oe","on","or","ow","od","ao","ac","ae","an","ar","aw","ad","so","sc","se","sn","sr","sw","sd","lo","lc","le","ln","lr","lw","ld","po","pc","pe","pn","pr","pw","pd")
            for(t <- tactics) {
                val (updt,apct,dpct) = simulate(t,attacker, defender, combatslog, options)

                //store results and select best tactics for each side
                table = table & updt
                if(apct > att_pct){
                    att_pct = apct
                    att_tactic = getTactic(t(0))+" ("+att_pct+"%)"
                } else if(apct == att_pct && !(att_tactic.indexOf(getTactic(t(0))) >= 0)){
                    att_tactic = att_tactic+ (if(!"".equals(att_tactic)){" and "}else{""}) +getTactic(t(0))+" ("+att_pct+"%)"
                }

                if(dpct > def_pct){
                    def_pct = dpct
                    def_tactic = getTactic(t(1))+" ("+def_pct+"%)"
                } else if(dpct == def_pct && !(def_tactic.indexOf(getTactic(t(1))) >= 0) ){
                    def_tactic = def_tactic+ (if(!"".equals(def_tactic)){" and "}else{""}) + getTactic(t(1))+" ("+def_pct+"%)"
                }
            }
            table = SetHtml("win_att",  <u>{scala.xml.Unparsed(att_tactic)}</u> ) & SetHtml("win_def",  <u>{scala.xml.Unparsed(def_tactic)}</u> ) & table

            //update logs
            val logs = SetHtml("logs",  {scala.xml.Unparsed(combatslog.mkString(" "))} )

            //return results
            side_att & side_df & logs & table
        }

        //generates a list of relevant data from each combatant, now displayed under the forces on submit
        private def getSideData(combatant: Combatant) = {
            val data = new scala.collection.mutable.ListBuffer[String]
            data += "<h2>Relevant data</h2>"
            data += ""
            data += "Commander: <b>"+combatant.initial_commander.name+"</b>"
            data += "Factors: <b>"+(combatant.main.factors+combatant.outflank.factors)+"</b>"
            data += "Moral: <b>"+ combatant.moral_combat+"</b>"
            data += "Terrain: <b>"+ combatant.main.terrain+"</b>"
            data += ""
            data += "<u>Pinning</u>  force factors: "+ combatant.main.factors
            data += "<u>Outflank</u> force factors: "+ combatant.outflank.factors
            data += ""
            data += "<u>Cavalry</u> force factors: "+ (combatant.main.cavalry + combatant.outflank.cavalry) +" (P: "+combatant.main.cavalry +", O:"+ combatant.outflank.cavalry+")"
            data += "<u>Artillery</u> force factors: "+ (combatant.main.artillery + combatant.outflank.artillery) +" (P: "+combatant.main.artillery +", O:"+ combatant.outflank.artillery+")"
            data += "<u>Guards</u>  force factors: "+ (combatant.main.guards + combatant.outflank.guards) +" (P: "+combatant.main.guards +", O:"+ combatant.outflank.guards+")"            
            data += ""
            data += "Terrain modifiers: <ul><li><b>"+ combatant.main.terrain.outflank_modifier+"</b> to outflank</li><li><b>"+ combatant.main.terrain.att_mod_casualties+"</b> to casualties in CRC for attacker</li><li><b>"+ combatant.main.terrain.att_mod_moral+"</b> to moral in CRC for attacker</li><li><b>"+ combatant.main.terrain.def_mod_casualties+"</b> to casualties in CRC for defenderer</li><li><b>"+ combatant.main.terrain.def_mod_moral+"</b> to moral in CRC for defenderer</li></ul>"
            data.mkString("<br/>")
        }

        //launches the simulation of a combat
        private def simulate(chits: String, attacker: Combatant, defender: Combatant, combatslog: scala.collection.mutable.ListBuffer[String], options: Options) = {
            combatslog += "<div id='logs_"+chits+"' name='"+chits+"'>"
            var (att,df,attu, attm, dfu, dfm, actions) = (0,0,0,0.0,0,0.0,0)
            for(i <- 1 to options.iterations) {
                val combat = chits match { /* TODO: try to simplify as each change on one of this affects many areas of the html */
                    case "oo" => new OutflankOutflank(attacker, defender, options)
                    case "oc" => new OutflankCounterattack(attacker, defender, options)
                    case "oe" => new OutflankEscalatedCounterattack(attacker, defender, options)
                    case "on" => new OutflankCordon(attacker, defender, options)
                    case "or" => new OutflankCordonRiver(attacker, defender, options)
                    case "ow" => new OutflankWithdraw(attacker, defender, options)
                    case "od" => new OutflankDefend(attacker, defender, options)
                    case "ao" => new AssaultOutflank(attacker, defender, options)
                    case "ac" => new AssaultCounterattack(attacker, defender, options)
                    case "ae" => new AssaultEscalatedCounterattack(attacker, defender, options)
                    case "an" => new AssaultCordon(attacker, defender, options)
                    case "ar" => new AssaultCordonRiver(attacker, defender, options)
                    case "aw" => new AssaultWithdraw(attacker, defender, options)
                    case "ad" => new AssaultDefend(attacker, defender, options)
                    case "so" => new EscalatedAssaultOutflank(attacker, defender, options)
                    case "sc" => new EscalatedAssaultCounterattack(attacker, defender, options)
                    case "se" => new EscalatedAssaultEscalatedCounterattack(attacker, defender, options)
                    case "sn" => new EscalatedAssaultCordon(attacker, defender, options)
                    case "sr" => new EscalatedAssaultCordonRiver(attacker, defender, options)
                    case "sw" => new EscalatedAssaultWithdraw(attacker, defender, options)
                    case "sd" => new EscalatedAssaultDefend(attacker, defender, options)
                    case "lo" => new EchelonOutflank(attacker, defender, options)
                    case "lc" => new EchelonCounterattack(attacker, defender, options)
                    case "le" => new EchelonEscalatedCounterattack(attacker, defender, options)
                    case "ln" => new EchelonCordon(attacker, defender, options)
                    case "lr" => new EchelonCordonRiver(attacker, defender, options)
                    case "lw" => new EchelonWithdraw(attacker, defender, options)
                    case "ld" => new EchelonDefend(attacker, defender, options)
                    case "po" => new ProbeOutflank(attacker, defender, options)
                    case "pc" => new ProbeCounterattack(attacker, defender, options)
                    case "pe" => new ProbeEscalatedCounterattack(attacker, defender, options)
                    case "pn" => new ProbeCordon(attacker, defender, options)
                    case "pr" => new ProbeCordonRiver(attacker, defender, options)
                    case "pw" => new ProbeWithdraw(attacker, defender, options)
                    case "pd" => new ProbeDefend(attacker, defender, options)
                }                
                              
                combat.start

                //select 10 samples for logs, every 10% of the runs or (if less than 10 runs remaining) all of them
                if(actions < 10 && ((options.iterations - i) <= (10 - actions) || i % ((actions+1)*options.iterations / 10) == 0 )){
                    combatslog += i+") "+combat.getLog+"<br/>"
                    actions += 1
                }

                if(combat.attackerWins) att += 1
                if(combat.defenderWins) df += 1
                attu += combat.attUnits
                attm += combat.attMoral
                dfu += combat.defUnits
                dfm += combat.defMoral
            }
            //create table results info
            combatslog += "</div>"
            val attpct = roundWithDecimals((att/options.iterations.toDouble)*100,2)
            val dfpct = roundWithDecimals((df/options.iterations.toDouble)*100,2)
            val log = SetHtml(chits, {scala.xml.Unparsed("Att <b>"+attpct.toString +"%  - "+dfpct.toString +"%</b> Def <br/>"+
                                                         "--Avg Att: "+roundWithDecimals((attu/options.iterations.toDouble),0).toString +" units / "+ roundWithDecimals((attm/options.iterations.toDouble),1).toString  +" moral remaining<br/>"+
                                                         "--Avg Def: "+roundWithDecimals((dfu/options.iterations.toDouble),0).toString +" units / "+ roundWithDecimals((dfm/options.iterations.toDouble),1).toString  +" moral remaining"
                    )} )
            
            (log, attpct, dfpct)
        }
    }
}
