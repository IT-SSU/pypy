package com.cookandroid.kotlinapp

import java.util.regex.Pattern

object test {

    @JvmStatic
    fun main(args: Array<String>) {
        val txt = "20190824"

        val re1 =
            "((?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))(?:[0]?[1-9]|[1][012])(?:(?:[0-2]?\\d{1})|(?:[3][01]{1})))(?![\\d])"    // YYYYMMDD 1

        val p = Pattern.compile(re1, Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        val m = p.matcher(txt)
        if (m.find()) {
            val yyyymmdd1 = m.group(1)
            print("($yyyymmdd1)\n")
        }
    }
}
