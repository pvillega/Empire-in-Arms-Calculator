/*
 * The MIT License
 * Copyright (c) 2010 Pere Villega Montenegro

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
 * Boot.scala
 *
 * Configuration class for Lift
 */
package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, StandardDBVendor}
import _root_.java.sql.{Connection, DriverManager}
import _root_.net.liftweb._
import Helpers._

import actor._
import scala.xml._
import java.text.NumberFormat


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
    def boot {       

        // where to search snippet and DB entities
        LiftRules.addToPackages("com.perevillega")
        
        // Build SiteMap
        LiftRules.setSiteMap(SiteMap(MenuInfo.menu: _*))

        /*
         * Show the spinny image when an Ajax call starts
         */
        LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

        /*
         * Make the spinny image go away when it ends
         */
        LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

        LiftRules.early.append(makeUtf8)               
    }

    /**
     * Force the request to be UTF-8
     */
    private def makeUtf8(req: HTTPRequest) {
        req.setCharacterEncoding("UTF-8")
    }

}


object MenuInfo {
    import Loc._

    def menu: List[Menu] =
    Menu(Loc("Home", List("index"), "Home")) ::
    Menu(Loc("Tools", Link("tools" :: "" :: Nil, true, "/tools/simulator"), "Tools"),
    Menu(Loc("Simulator", "tools" :: "simulator" :: Nil, "Simulator"))) ::
    Nil
}


