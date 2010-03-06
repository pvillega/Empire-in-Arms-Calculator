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
 * Country.scala
 *
 * Countries the troops can be from
 */

package com.perevillega.lib.utils

//Countries in the game
sealed class Country {
    val moral_infantery = 3.0    
    val moral_cavalry = 4.0
    val moral_militia = 2.0
    val moral_guard = 5.0
    val moral_cossack = 1.0
    val moral_guerrilla = 1.0
    val moral_feudal_infantery = 2.0
    val moral_feudal_cavalry = 2.0
    //var as implicit strategic and tactical ratings of minor countries depends on major power
    var strategic = 1
    var tactical = 1
}

case class England() extends Country{
    override val moral_infantery = 4.5
    strategic = 2
    tactical = 2
}

case class France() extends Country{
    override val moral_infantery = 4.0    
    strategic = 2
    tactical = 2
}

case class Spain() extends Country{
    override val moral_cavalry = 3.0
}

case class Turkey() extends Country{
    override val moral_infantery = 2.0    
    override val moral_cavalry = 3.0
}

case class Prussia() extends Country
case class Russia() extends Country {
    tactical = 2
}
case class Austria() extends Country

case class SpainDominant() extends Spain{
    override val moral_infantery = 4.0
    override val moral_cavalry = 4.0
}

case class EnglandNoDominant() extends England{
    override val moral_infantery = 3.5
    strategic = 2
    tactical = 2
}

case class FranceNoDominant() extends France{
    override val moral_infantery = 3.0
    strategic = 2
    tactical = 2
}

case class TurkeyDominant() extends Turkey{
    override val moral_infantery = 3.0    
    override val moral_cavalry = 4.0
    override val moral_feudal_infantery = 2.5
    override val moral_feudal_cavalry = 2.5
}

case class PrussiaDominant() extends Prussia {
    override val moral_infantery = 4.0
}

case class RussiaDominant() extends Russia {
    override val moral_infantery = 4.0
    tactical = 2
}

case class AustriaDominant() extends Austria {
    override val moral_infantery = 4.0
}

case class Prussia1810() extends Prussia {
    strategic = 2
}

case class Prussia1810Dominant() extends Prussia {
    strategic = 2
    override val moral_infantery = 4.0
}

case class Algeria() extends Country{
    override val moral_infantery = 1.0
    override val moral_cavalry = 2.0
}

case class Baden() extends Country{
    override val moral_infantery = 3.0
    override val moral_cavalry = 4.0
}

case class Bavaria() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 3.0
}

case class Cyrenaica() extends Country{
    override val moral_infantery = 1.0
    override val moral_cavalry = 2.0
}

case class Denmark() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 3.0
}

case class Egypt() extends Country{
    override val moral_infantery = 1.0
    override val moral_cavalry = 3.0
}

case class Hanover() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 2.0
}

case class HanoverGB() extends Hanover{
    override val moral_infantery = 4.0
    override val moral_cavalry = 4.0
}

case class Hesse() extends Country{
    override val moral_infantery = 3.0
    override val moral_cavalry = 4.0
}

case class Holland() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 3.0
}

case class Lombardy() extends Country{
    override val moral_infantery = 3.0
    override val moral_cavalry = 3.0
}

case class Morrocco() extends Country{
    override val moral_infantery = 1.0
    override val moral_cavalry = 2.0
}

case class Naples() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 2.0
}

case class Piedmont() extends Country{
    override val moral_infantery = 3.0
    override val moral_cavalry = 3.0
}

case class Poland() extends Country{    
    override val moral_cavalry = 4.0
}

case class Portugal() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 2.0
}

case class PortugalGB() extends Portugal{
    override val moral_infantery = 4.0
    override val moral_cavalry = 4.0
}

case class Saxony() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 4.0
}

case class Sweden() extends Country{
    override val moral_infantery = 3.0
    override val moral_cavalry = 4.0
}

case class Syria() extends Country{
    override val moral_infantery = 1.0
    override val moral_cavalry = 2.0
}

case class Tripolitania() extends Country{
    override val moral_infantery = 1.0
    override val moral_cavalry = 2.0
}

case class Tunisia() extends Country{
    override val moral_infantery = 1.0
    override val moral_cavalry = 2.0
}

case class Venetia() extends Country{
    override val moral_infantery = 3.0
    override val moral_cavalry = 3.0
}

case class Wurttemburg() extends Country{
    override val moral_infantery = 3.0
    override val moral_cavalry = 3.0
}

case class TurkeyOttoman() extends Country{
    override val moral_infantery = 2.0
    override val moral_cavalry = 3.0
}

object Country{
    def getSeq = (("England","England") ::
    ("France","France") ::
    ("Spain","Spain") ::
    ("Turkey","Turkey") ::
    ("Prussia","Prussia") ::
    ("Prussia1810","Prussia(1810)") ::
    ("Russia","Russia") ::
    ("Austria","Austria") ::
    ("EnglandND","England No Dominant") ::
    ("FranceND","France No Dominant") ::
    ("SpainD","Spain Dominant") ::
    ("TurkeyD","Turkey Dominant") ::
    ("PrussiaD","Prussia Dominant") ::
    ("Prussia1810D","Prussia(1810) Dominant") ::
    ("RussiaD","Russia Dominant") ::
    ("AustriaD","Austria Dominant") ::
    Nil) :::
    (("Algeria-England","Algeria (England)") ::
    ("Algeria-France","Algeria (France)") ::
    ("Algeria-Spain","Algeria (Spain)") ::
    ("Algeria-Turkey","Algeria (Turkey)") ::
    ("Algeria-Prussia","Algeria (Prussia)") ::
    ("Algeria-Prussia1810","Algeria (Prussia 1810)") ::
    ("Algeria-Russia","Algeria (Russia)") ::
    ("Algeria-Austria","Algeria (Austria)") ::
    ("Algeria-EnglandND","Algeria (England ND)") ::
    ("Algeria-FranceND","Algeria (France ND)") ::
    ("Algeria-SpainD","Algeria (Spain D)") ::
    ("Algeria-TurkeyD","Algeria (Turkey D)") ::
    ("Algeria-PrussiaD","Algeria (Prussia D)") ::
    ("Algeria-Prussia1810D","Algeria (Prussia 1810 D)") ::
    ("Algeria-RussiaD","Algeria (Russia D)") ::
    ("Algeria-AustriaD","Algeria (Austria D)") ::
    ("Algeria-TurkeyOttoman","Algeria (Ottoman Empire)") ::
    Nil) :::
    (("Baden-England","Baden (England)") ::
    ("Baden-France","Baden (France)") ::
    ("Baden-Spain","Baden (Spain)") ::
    ("Baden-Turkey","Baden (Turkey)") ::
    ("Baden-Prussia","Baden (Prussia)") ::
    ("Baden-Prussia1810","Baden (Prussia 1810)") ::
    ("Baden-Russia","Baden (Russia)") ::
    ("Baden-Austria","Baden (Austria)") ::
    ("Baden-EnglandND","Baden (England ND)") ::
    ("Baden-FranceND","Baden (France ND)") ::
    ("Baden-SpainD","Baden (Spain D)") ::
    ("Baden-TurkeyD","Baden (Turkey D)") ::
    ("Baden-PrussiaD","Baden (Prussia D)") ::
    ("Baden-Prussia1810D","Baden (Prussia 1810 D)") ::
    ("Baden-RussiaD","Baden (Russia D)") ::
    ("Baden-AustriaD","Baden (Austria D)") ::
    Nil) :::
    (("Bavaria-England","Bavaria (England)") ::
    ("Bavaria-France","Bavaria (France)") ::
    ("Bavaria-Spain","Bavaria (Spain)") ::
    ("Bavaria-Turkey","Bavaria (Turkey)") ::
    ("Bavaria-Prussia","Bavaria (Prussia)") ::
    ("Bavaria-Prussia1810","Bavaria (Prussia 1810)") ::
    ("Bavaria-Russia","Bavaria (Russia)") ::
    ("Bavaria-Austria","Bavaria (Austria)") ::
    ("Bavaria-EnglandND","Bavaria (England ND)") ::
    ("Bavaria-FranceND","Bavaria (France ND)") ::
    ("Bavaria-SpainD","Bavaria (Spain D)") ::
    ("Bavaria-TurkeyD","Bavaria (Turkey D)") ::
    ("Bavaria-PrussiaD","Bavaria (Prussia D)") ::
    ("Bavaria-Prussia1810D","Bavaria (Prussia 1810 D)") ::
    ("Bavaria-RussiaD","Bavaria (Russia D)") ::
    ("Bavaria-AustriaD","Bavaria (Austria D)") ::
    Nil) :::
    (("Cyrenaica-England","Cyrenaica (England)") ::
    ("Cyrenaica-France","Cyrenaica (France)") ::
    ("Cyrenaica-Spain","Cyrenaica (Spain)") ::
    ("Cyrenaica-Turkey","Cyrenaica (Turkey)") ::
    ("Cyrenaica-Prussia","Cyrenaica (Prussia)") ::
    ("Cyrenaica-Prussia1810","Cyrenaica (Prussia 1810)") ::
    ("Cyrenaica-Russia","Cyrenaica (Russia)") ::
    ("Cyrenaica-Austria","Cyrenaica (Austria)") ::
    ("Cyrenaica-EnglandND","Cyrenaica (England ND)") ::
    ("Cyrenaica-FranceND","Cyrenaica (France ND)") ::
    ("Cyrenaica-SpainD","Cyrenaica (Spain D)") ::
    ("Cyrenaica-TurkeyD","Cyrenaica (Turkey D)") ::
    ("Cyrenaica-PrussiaD","Cyrenaica (Prussia D)") ::
    ("Cyrenaica-Prussia1810D","Cyrenaica (Prussia 1810 D)") ::
    ("Cyrenaica-RussiaD","Cyrenaica (Russia D)") ::
    ("Cyrenaica-AustriaD","Cyrenaica (Austria D)") ::
    ("Cyrenaica-TurkeyOttoman","Cyrenaica (Ottoman Empire)") ::
    Nil) :::
    (("Denmark-England","Denmark (England)") ::
    ("Denmark-France","Denmark (France)") ::
    ("Denmark-Spain","Denmark (Spain)") ::
    ("Denmark-Turkey","Denmark (Turkey)") ::
    ("Denmark-Prussia","Denmark (Prussia)") ::
    ("Denmark-Prussia1810","Denmark (Prussia 1810)") ::
    ("Denmark-Russia","Denmark (Russia)") ::
    ("Denmark-Austria","Denmark (Austria)") ::
    ("Denmark-EnglandND","Denmark (England ND)") ::
    ("Denmark-FranceND","Denmark (France ND)") ::
    ("Denmark-SpainD","Denmark (Spain D)") ::
    ("Denmark-TurkeyD","Denmark (Turkey D)") ::
    ("Denmark-PrussiaD","Denmark (Prussia D)") ::
    ("Denmark-Prussia1810D","Denmark (Prussia 1810 D)") ::
    ("Denmark-RussiaD","Denmark (Russia D)") ::
    ("Denmark-AustriaD","Denmark (Austria D)") ::
    Nil) :::
    (("Egypt-England","Egypt (England)") ::
    ("Egypt-France","Egypt (France)") ::
    ("Egypt-Spain","Egypt (Spain)") ::
    ("Egypt-Turkey","Egypt (Turkey)") ::
    ("Egypt-Prussia","Egypt (Prussia)") ::
    ("Egypt-Prussia1810","Egypt (Prussia 1810)") ::
    ("Egypt-Russia","Egypt (Russia)") ::
    ("Egypt-Austria","Egypt (Austria)") ::
    ("Egypt-EnglandND","Egypt (England ND)") ::
    ("Egypt-FranceND","Egypt (France ND)") ::
    ("Egypt-SpainD","Egypt (Spain D)") ::
    ("Egypt-TurkeyD","Egypt (Turkey D)") ::
    ("Egypt-PrussiaD","Egypt (Prussia D)") ::
    ("Egypt-Prussia1810D","Egypt (Prussia 1810 D)") ::
    ("Egypt-RussiaD","Egypt (Russia D)") ::
    ("Egypt-AustriaD","Egypt (Austria D)") ::
    ("Egypt-TurkeyOttoman","Egypt (Ottoman Empire)") ::
    Nil) :::
    (("Hanover-England","Hanover (England)") ::
    ("Hanover-France","Hanover (France)") ::
    ("Hanover-Spain","Hanover (Spain)") ::
    ("Hanover-Turkey","Hanover (Turkey)") ::
    ("Hanover-Prussia","Hanover (Prussia)") ::
    ("Hanover-Prussia1810","Hanover (Prussia 1810)") ::
    ("Hanover-Russia","Hanover (Russia)") ::
    ("Hanover-Austria","Hanover (Austria)") ::
    ("Hanover-EnglandND","Hanover (England ND)") ::
    ("Hanover-FranceND","Hanover (France ND)") ::
    ("Hanover-SpainD","Hanover (Spain D)") ::
    ("Hanover-TurkeyD","Hanover (Turkey D)") ::
    ("Hanover-PrussiaD","Hanover (Prussia D)") ::
    ("Hanover-Prussia1810D","Hanover (Prussia 1810 D)") ::
    ("Hanover-RussiaD","Hanover (Russia D)") ::
    ("Hanover-AustriaD","Hanover (Austria D)") ::
    Nil) :::
   (("HanoverGB-England","Hanover (England training)") ::
    Nil) :::
    (("Hesse-England","Hesse (England)") ::
    ("Hesse-France","Hesse (France)") ::
    ("Hesse-Spain","Hesse (Spain)") ::
    ("Hesse-Turkey","Hesse (Turkey)") ::
    ("Hesse-Prussia","Hesse (Prussia)") ::
    ("Hesse-Prussia1810","Hesse (Prussia 1810)") ::
    ("Hesse-Russia","Hesse (Russia)") ::
    ("Hesse-Austria","Hesse (Austria)") ::
    ("Hesse-EnglandND","Hesse (England ND)") ::
    ("Hesse-FranceND","Hesse (France ND)") ::
    ("Hesse-SpainD","Hesse (Spain D)") ::
    ("Hesse-TurkeyD","Hesse (Turkey D)") ::
    ("Hesse-PrussiaD","Hesse (Prussia D)") ::
    ("Hesse-Prussia1810D","Hesse (Prussia 1810 D)") ::
    ("Hesse-RussiaD","Hesse (Russia D)") ::
    ("Hesse-AustriaD","Hesse (Austria D)") ::
    Nil) :::
    (("Holland-England","Holland (England)") ::
    ("Holland-France","Holland (France)") ::
    ("Holland-Spain","Holland (Spain)") ::
    ("Holland-Turkey","Holland (Turkey)") ::
    ("Holland-Prussia","Holland (Prussia)") ::
    ("Holland-Prussia1810","Holland (Prussia 1810)") ::
    ("Holland-Russia","Holland (Russia)") ::
    ("Holland-Austria","Holland (Austria)") ::
    ("Holland-EnglandND","Holland (England ND)") ::
    ("Holland-FranceND","Holland (France ND)") ::
    ("Holland-SpainD","Holland (Spain D)") ::
    ("Holland-TurkeyD","Holland (Turkey D)") ::
    ("Holland-PrussiaD","Holland (Prussia D)") ::
    ("Holland-Prussia1810D","Holland (Prussia 1810 D)") ::
    ("Holland-RussiaD","Holland (Russia D)") ::
    ("Holland-AustriaD","Holland (Austria D)") ::
    Nil) :::
    (("Lombardy-England","Lombardy (England)") ::
    ("Lombardy-France","Lombardy (France)") ::
    ("Lombardy-Spain","Lombardy (Spain)") ::
    ("Lombardy-Turkey","Lombardy (Turkey)") ::
    ("Lombardy-Prussia","Lombardy (Prussia)") ::
    ("Lombardy-Prussia1810","Lombardy (Prussia 1810)") ::
    ("Lombardy-Russia","Lombardy (Russia)") ::
    ("Lombardy-Austria","Lombardy (Austria)") ::
    ("Lombardy-EnglandND","Lombardy (England ND)") ::
    ("Lombardy-FranceND","Lombardy (France ND)") ::
    ("Lombardy-SpainD","Lombardy (Spain D)") ::
    ("Lombardy-TurkeyD","Lombardy (Turkey D)") ::
    ("Lombardy-PrussiaD","Lombardy (Prussia D)") ::
    ("Lombardy-Prussia1810D","Lombardy (Prussia 1810 D)") ::
    ("Lombardy-RussiaD","Lombardy (Russia D)") ::
    ("Lombardy-AustriaD","Lombardy (Austria D)") ::
    Nil) :::
    (("Morrocco-England","Morrocco (England)") ::
    ("Morrocco-France","Morrocco (France)") ::
    ("Morrocco-Spain","Morrocco (Spain)") ::
    ("Morrocco-Turkey","Morrocco (Turkey)") ::
    ("Morrocco-Prussia","Morrocco (Prussia)") ::
    ("Morrocco-Prussia1810","Morrocco (Prussia 1810)") ::
    ("Morrocco-Russia","Morrocco (Russia)") ::
    ("Morrocco-Austria","Morrocco (Austria)") ::
    ("Morrocco-EnglandND","Morrocco (England ND)") ::
    ("Morrocco-FranceND","Morrocco (France ND)") ::
    ("Morrocco-SpainD","Morrocco (Spain D)") ::
    ("Morrocco-TurkeyD","Morrocco (Turkey D)") ::
    ("Morrocco-PrussiaD","Morrocco (Prussia D)") ::
    ("Morrocco-Prussia1810D","Morrocco (Prussia 1810 D)") ::
    ("Morrocco-RussiaD","Morrocco (Russia D)") ::
    ("Morrocco-AustriaD","Morrocco (Austria D)") ::
    ("Morrocco-TurkeyOttoman","Morrocco (Ottoman Empire)") ::
    Nil) :::
    (("Naples-England","Naples (England)") ::
    ("Naples-France","Naples (France)") ::
    ("Naples-Spain","Naples (Spain)") ::
    ("Naples-Turkey","Naples (Turkey)") ::
    ("Naples-Prussia","Naples (Prussia)") ::
    ("Naples-Prussia1810","Naples (Prussia 1810)") ::
    ("Naples-Russia","Naples (Russia)") ::
    ("Naples-Austria","Naples (Austria)") ::
    ("Naples-EnglandND","Naples (England ND)") ::
    ("Naples-FranceND","Naples (France ND)") ::
    ("Naples-SpainD","Naples (Spain D)") ::
    ("Naples-TurkeyD","Naples (Turkey D)") ::
    ("Naples-PrussiaD","Naples (Prussia D)") ::
    ("Naples-Prussia1810D","Naples (Prussia 1810 D)") ::
    ("Naples-RussiaD","Naples (Russia D)") ::
    ("Naples-AustriaD","Naples (Austria D)") ::
    Nil) :::
    (("Piedmont-England","Piedmont (England)") ::
    ("Piedmont-France","Piedmont (France)") ::
    ("Piedmont-Spain","Piedmont (Spain)") ::
    ("Piedmont-Turkey","Piedmont (Turkey)") ::
    ("Piedmont-Prussia","Piedmont (Prussia)") ::
    ("Piedmont-Prussia1810","Piedmont (Prussia 1810)") ::
    ("Piedmont-Russia","Piedmont (Russia)") ::
    ("Piedmont-Austria","Piedmont (Austria)") ::
    ("Piedmont-EnglandND","Piedmont (England ND)") ::
    ("Piedmont-FranceND","Piedmont (France ND)") ::
    ("Piedmont-SpainD","Piedmont (Spain D)") ::
    ("Piedmont-TurkeyD","Piedmont (Turkey D)") ::
    ("Piedmont-PrussiaD","Piedmont (Prussia D)") ::
    ("Piedmont-Prussia1810D","Piedmont (Prussia 1810 D)") ::
    ("Piedmont-RussiaD","Piedmont (Russia D)") ::
    ("Piedmont-AustriaD","Piedmont (Austria D)") ::
    Nil) :::
    (("Poland-England","Poland (England)") ::
    ("Poland-France","Poland (France)") ::
    ("Poland-Spain","Poland (Spain)") ::
    ("Poland-Turkey","Poland (Turkey)") ::
    ("Poland-Prussia","Poland (Prussia)") ::
    ("Poland-Prussia1810","Poland (Prussia 1810)") ::
    ("Poland-Russia","Poland (Russia)") ::
    ("Poland-Austria","Poland (Austria)") ::
    ("Poland-EnglandND","Poland (England ND)") ::
    ("Poland-FranceND","Poland (France ND)") ::
    ("Poland-SpainD","Poland (Spain D)") ::
    ("Poland-TurkeyD","Poland (Turkey D)") ::
    ("Poland-PrussiaD","Poland (Prussia D)") ::
    ("Poland-Prussia1810D","Poland (Prussia 1810 D)") ::
    ("Poland-RussiaD","Poland (Russia D)") ::
    ("Poland-AustriaD","Poland (Austria D)") ::
    Nil) :::
    (("Portugal-England","Portugal (England)") ::
    ("Portugal-France","Portugal (France)") ::
    ("Portugal-Spain","Portugal (Spain)") ::
    ("Portugal-Turkey","Portugal (Turkey)") ::
    ("Portugal-Prussia","Portugal (Prussia)") ::
    ("Portugal-Prussia1810","Portugal (Prussia 1810)") ::
    ("Portugal-Russia","Portugal (Russia)") ::
    ("Portugal-Austria","Portugal (Austria)") ::
    ("Portugal-EnglandND","Portugal (England ND)") ::
    ("Portugal-FranceND","Portugal (France ND)") ::
    ("Portugal-SpainD","Portugal (Spain D)") ::
    ("Portugal-TurkeyD","Portugal (Turkey D)") ::
    ("Portugal-PrussiaD","Portugal (Prussia D)") ::
    ("Portugal-Prussia1810D","Portugal (Prussia 1810 D)") ::
    ("Portugal-RussiaD","Portugal (Russia D)") ::
    ("Portugal-AustriaD","Portugal (Austria D)") ::
    Nil) :::
   (("PortugalGB-England","Portugal (England training)") ::
    Nil) :::
    (("Saxony-England","Saxony (England)") ::
    ("Saxony-France","Saxony (France)") ::
    ("Saxony-Spain","Saxony (Spain)") ::
    ("Saxony-Turkey","Saxony (Turkey)") ::
    ("Saxony-Prussia","Saxony (Prussia)") ::
    ("Saxony-Prussia1810","Saxony (Prussia 1810)") ::
    ("Saxony-Russia","Saxony (Russia)") ::
    ("Saxony-Austria","Saxony (Austria)") ::
    ("Saxony-EnglandND","Saxony (England ND)") ::
    ("Saxony-FranceND","Saxony (France ND)") ::
    ("Saxony-SpainD","Saxony (Spain D)") ::
    ("Saxony-TurkeyD","Saxony (Turkey D)") ::
    ("Saxony-PrussiaD","Saxony (Prussia D)") ::
    ("Saxony-Prussia1810D","Saxony (Prussia 1810 D)") ::
    ("Saxony-RussiaD","Saxony (Russia D)") ::
    ("Saxony-AustriaD","Saxony (Austria D)") ::
    Nil) :::
    (("Sweden-England","Sweden (England)") ::
    ("Sweden-France","Sweden (France)") ::
    ("Sweden-Spain","Sweden (Spain)") ::
    ("Sweden-Turkey","Sweden (Turkey)") ::
    ("Sweden-Prussia","Sweden (Prussia)") ::
    ("Sweden-Prussia1810","Sweden (Prussia 1810)") ::
    ("Sweden-Russia","Sweden (Russia)") ::
    ("Sweden-Austria","Sweden (Austria)") ::
    ("Sweden-EnglandND","Sweden (England ND)") ::
    ("Sweden-FranceND","Sweden (France ND)") ::
    ("Sweden-SpainD","Sweden (Spain D)") ::
    ("Sweden-TurkeyD","Sweden (Turkey D)") ::
    ("Sweden-PrussiaD","Sweden (Prussia D)") ::
    ("Sweden-Prussia1810D","Sweden (Prussia 1810 D)") ::
    ("Sweden-RussiaD","Sweden (Russia D)") ::
    ("Sweden-AustriaD","Sweden (Austria D)") ::
    Nil) :::
    (("Syria-England","Syria (England)") ::
    ("Syria-France","Syria (France)") ::
    ("Syria-Spain","Syria (Spain)") ::
    ("Syria-Turkey","Syria (Turkey)") ::
    ("Syria-Prussia","Syria (Prussia)") ::
    ("Syria-Prussia1810","Syria (Prussia 1810)") ::
    ("Syria-Russia","Syria (Russia)") ::
    ("Syria-Austria","Syria (Austria)") ::
    ("Syria-EnglandND","Syria (England ND)") ::
    ("Syria-FranceND","Syria (France ND)") ::
    ("Syria-SpainD","Syria (Spain D)") ::
    ("Syria-TurkeyD","Syria (Turkey D)") ::
    ("Syria-PrussiaD","Syria (Prussia D)") ::
    ("Syria-Prussia1810D","Syria (Prussia 1810 D)") ::
    ("Syria-RussiaD","Syria (Russia D)") ::
    ("Syria-AustriaD","Syria (Austria D)") ::
    ("Syria-TurkeyOttoman","Syria (Ottoman Empire)") ::
    Nil) :::
    (("Tripolitania-England","Tripolitania (England)") ::
    ("Tripolitania-France","Tripolitania (France)") ::
    ("Tripolitania-Spain","Tripolitania (Spain)") ::
    ("Tripolitania-Turkey","Tripolitania (Turkey)") ::
    ("Tripolitania-Prussia","Tripolitania (Prussia)") ::
    ("Tripolitania-Prussia1810","Tripolitania (Prussia 1810)") ::
    ("Tripolitania-Russia","Tripolitania (Russia)") ::
    ("Tripolitania-Austria","Tripolitania (Austria)") ::
    ("Tripolitania-EnglandND","Tripolitania (England ND)") ::
    ("Tripolitania-FranceND","Tripolitania (France ND)") ::
    ("Tripolitania-SpainD","Tripolitania (Spain D)") ::
    ("Tripolitania-TurkeyD","Tripolitania (Turkey D)") ::
    ("Tripolitania-PrussiaD","Tripolitania (Prussia D)") ::
    ("Tripolitania-Prussia1810D","Tripolitania (Prussia 1810 D)") ::
    ("Tripolitania-RussiaD","Tripolitania (Russia D)") ::
    ("Tripolitania-AustriaD","Tripolitania (Austria D)") ::
    ("Tripolitania-TurkeyOttoman","Tripolitania (Ottoman Empire)") ::
    Nil) :::
    (("Tunisia-England","Tunisia (England)") ::
    ("Tunisia-France","Tunisia (France)") ::
    ("Tunisia-Spain","Tunisia (Spain)") ::
    ("Tunisia-Turkey","Tunisia (Turkey)") ::
    ("Tunisia-Prussia","Tunisia (Prussia)") ::
    ("Tunisia-Prussia1810","Tunisia (Prussia 1810)") ::
    ("Tunisia-Russia","Tunisia (Russia)") ::
    ("Tunisia-Austria","Tunisia (Austria)") ::
    ("Tunisia-EnglandND","Tunisia (England ND)") ::
    ("Tunisia-FranceND","Tunisia (France ND)") ::
    ("Tunisia-SpainD","Tunisia (Spain D)") ::
    ("Tunisia-TurkeyD","Tunisia (Turkey D)") ::
    ("Tunisia-PrussiaD","Tunisia (Prussia D)") ::
    ("Tunisia-Prussia1810D","Tunisia (Prussia 1810 D)") ::
    ("Tunisia-RussiaD","Tunisia (Russia D)") ::
    ("Tunisia-AustriaD","Tunisia (Austria D)") ::
    ("Tunisia-TurkeyOttoman","Tunisia (Ottoman Empire)") ::
    Nil) :::
    (("Venetia-England","Venetia (England)") ::
    ("Venetia-France","Venetia (France)") ::
    ("Venetia-Spain","Venetia (Spain)") ::
    ("Venetia-Turkey","Venetia (Turkey)") ::
    ("Venetia-Prussia","Venetia (Prussia)") ::
    ("Venetia-Prussia1810","Venetia (Prussia 1810)") ::
    ("Venetia-Russia","Venetia (Russia)") ::
    ("Venetia-Austria","Venetia (Austria)") ::
    ("Venetia-EnglandND","Venetia (England ND)") ::
    ("Venetia-FranceND","Venetia (France ND)") ::
    ("Venetia-SpainD","Venetia (Spain D)") ::
    ("Venetia-TurkeyD","Venetia (Turkey D)") ::
    ("Venetia-PrussiaD","Venetia (Prussia D)") ::
    ("Venetia-Prussia1810D","Venetia (Prussia 1810 D)") ::
    ("Venetia-RussiaD","Venetia (Russia D)") ::
    ("Venetia-AustriaD","Venetia (Austria D)") ::
    Nil) :::
    (("Wurttemburg-England","Wurttemburg (England)") ::
    ("Wurttemburg-France","Wurttemburg (France)") ::
    ("Wurttemburg-Spain","Wurttemburg (Spain)") ::
    ("Wurttemburg-Turkey","Wurttemburg (Turkey)") ::
    ("Wurttemburg-Prussia","Wurttemburg (Prussia)") ::
    ("Wurttemburg-Prussia1810","Wurttemburg (Prussia 1810)") ::
    ("Wurttemburg-Russia","Wurttemburg (Russia)") ::
    ("Wurttemburg-Austria","Wurttemburg (Austria)") ::
    ("Wurttemburg-EnglandND","Wurttemburg (England ND)") ::
    ("Wurttemburg-FranceND","Wurttemburg (France ND)") ::
    ("Wurttemburg-SpainD","Wurttemburg (Spain D)") ::
    ("Wurttemburg-TurkeyD","Wurttemburg (Turkey D)") ::
    ("Wurttemburg-PrussiaD","Wurttemburg (Prussia D)") ::
    ("Wurttemburg-Prussia1810D","Wurttemburg (Prussia 1810 D)") ::
    ("Wurttemburg-RussiaD","Wurttemburg (Russia D)") ::
    ("Wurttemburg-AustriaD","Wurttemburg (Austria D)") ::
    Nil) :::
    Nil   

    implicit def str2country(str: String) : Country = {
        def getCountry(str: String) = str.trim match {
            case "England" => new England
            case "France" => new France
            case "Spain" => new Spain
            case "Turkey" => new Turkey
            case "Prussia" => new Prussia
            case "Russia" => new Russia
            case "Austria" => new Austria
            case "EnglandND" => new EnglandNoDominant
            case "FranceND" => new FranceNoDominant
            case "SpainD" => new SpainDominant
            case "TurkeyD" => new TurkeyDominant
            case "PrussiaD" => new PrussiaDominant
            case "RussiaD" => new RussiaDominant
            case "AustriaD" => new AustriaDominant
            case "Prussia1810" => new Prussia1810
            case "Prussia1810D" => new Prussia1810Dominant
            case "Algeria" => new Algeria
            case "Baden" => new Baden
            case "Bavaria" => new Bavaria
            case "Cyrenaica" => new Cyrenaica
            case "Denmark" => new Denmark
            case "Egypt" => new Egypt
            case "Hanover" => new Hanover
            case "HanoverGB" => new HanoverGB
            case "Hesse" => new Hesse
            case "Holland" => new Holland
            case "Lombardy" => new Lombardy
            case "Morrocco" => new Morrocco
            case "Naples" => new Naples
            case "Piedmont" => new Piedmont
            case "Poland" => new Poland
            case "Portugal" => new Portugal
            case "PortugalGB" => new PortugalGB
            case "Saxony" => new Saxony
            case "Sweden" => new Sweden
            case "Syria" => new Syria
            case "Tripolitania" => new Tripolitania
            case "Tunisia" => new Tunisia
            case "Venetia" => new Venetia
            case "Wurttemburg" => new Wurttemburg
            case "TurkeyOttoman" => new TurkeyOttoman
            case _ => throw new UndefinedCountryException("Unknown country, must be one of England, France, Spain, Turkey, Prussia, Russia, Austria, EnglandND, FranceND, SpainD, TurkeyD, PrussiaD, RussiaD, AustriaD, Prussia1810 or a minor country in the format Minor-Major (ex.: Turkey-Algeria). Minor countries are Algeria, Baden, Bavaria, Cyrenaica, Denmark, Egypt, Hanover, Hesse, Holland, Lombardy, Morrocco, Naples, Piedmont, Poland, Portugal, Saxony, Sweden, Syria, Tripolitania, Tunisia, Venetia, Wurttemburg, TurkeyOttoman (for troops of Ottoman Empire controlled by Turkey)")
        }

        val countries = str.split("-")
        val base = getCountry(countries(0))
        countries.length > 1 match {
            case false => base
            case true => {
                    val owner = getCountry(countries(1))
                    base.strategic = owner.strategic
                    base.tactical = owner.tactical
                    base
                }
        }
    }
}

//Defines a custom exception for the country
class UndefinedCountryException(msg: String) extends Exception(msg)