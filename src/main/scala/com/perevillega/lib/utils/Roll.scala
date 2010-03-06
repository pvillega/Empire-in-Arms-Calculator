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
 * Roll.scala
 *
 * Simulates dice rolls
 */

package com.perevillega.lib.utils

import java.util.Random


/**
 * Simulates dice rolls
 */
object Roll {

    val rnd = new Random(System.currentTimeMillis)

    private def getRoll(min: Int, max: Int): Int = {
        rnd.nextInt(max-min) + min
    }

    def D6(): Int = {
        getRoll(1,6)
    }

    def D6x2(): Int = {
        D6 + D6
    }

    def D6(modifier: Int): Int ={
        D6 + modifier        
    }
}
