package com.cookandroid.kotlinapp

import java.util.regex.Pattern

object test {

    @JvmStatic
    fun main(args: Array<String>) {
        println("세종대왕가나다라마바사아자차카타파하")
        val txt = "imjgsddsa!@"

        //val regex = Regex(pattern = """"^(?=.*[A-Za-z])(?=.*\d)(?=.*[${'$'}@${'$'}!%*#?&])[A-Za-z\d${'$'}@${'$'}!%*#?&]{8,}${'$'}"""")
        //val matched = regex.containsMatchIn(input = txt)


        val re1 =
            """^(?=.*[A-Za-z])(?=.*\d)(?=.*[${'$'}@${'$'}!%*#?&])[A-Za-z\d${'$'}@${'$'}!%*#?&]{8,}${'$'}"""

        val p = Pattern.compile(re1, Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        val m = p.matcher(txt)
        if (m.matches()) {
            println(m.matches())
            //val yyyymmdd1 = m.group(1)
            //print("($yyyymmdd1)\n")
        }

        println(m.matches())
        //println(matched)
    }
}
