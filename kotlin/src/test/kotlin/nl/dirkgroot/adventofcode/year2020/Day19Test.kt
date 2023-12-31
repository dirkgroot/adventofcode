package nl.dirkgroot.adventofcode.year2020

import nl.dirkgroot.adventofcode.util.StringInput
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {
    private val example1 =
        """
            0: 4 1 5
            1: 2 3 | 3 2
            2: 4 4 | 5 5
            3: 4 5 | 5 4
            4: "a"
            5: "b"

            ababbb
        """.trimIndent()

    private val example2 =
        """
            0: 8 11
            1: "a"
            2: 1 24 | 14 4
            3: 5 14 | 16 1
            4: 1 1
            5: 1 14 | 15 1
            6: 14 14 | 1 14
            7: 14 5 | 1 21
            8: 42
            9: 14 27 | 1 26
            10: 23 14 | 28 1
            11: 42 31
            12: 24 14 | 19 1
            13: 14 3 | 1 12
            14: "b"
            15: 1 | 14
            16: 15 1 | 14 14
            17: 14 2 | 1 7
            18: 15 15
            19: 14 1 | 14 14
            20: 14 14 | 1 15
            21: 14 1 | 1 14
            22: 14 14
            23: 25 1 | 22 14
            24: 14 1
            25: 1 1 | 1 14
            26: 14 22 | 1 20
            27: 1 6 | 14 18
            28: 16 1
            31: 14 17 | 1 13
            42: 9 14 | 10 1

            abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
            bbabbbbaabaabba
            babbbbaabbbbbabbbbbbaabaaabaaa
            aaabbbbbbaaaabaababaabababbabaaabbababababaaa
            bbbbbbbaaaabbbbaaabbabaaa
            bbbababbbbaaaaaaaabbababaaababaabab
            ababaaaaaabaaab
            ababaaaaabbbaba
            baabbaaaabbaaaababbaababb
            abbbbabbbbaaaababbbbbbaaaababb
            aaaaabbaabaaaaababaa
            aaaabbaaaabbaaa
            aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
            babaaabbbaaabaababbaabababaaab
            aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
        """.trimIndent()

    @Test
    fun part1() {
        assertEquals(1, Day19(StringInput(example1)).part1())
        assertEquals(3, Day19(StringInput(example2)).part1())
    }

    @Test
    fun part2() {
        val input = """
            0: 8 11
            8: 42 | 42 8
            11: 42 31 | 42 11 31
            42: 100 101
            31: 101
            100: "a"
            101: "b"
            
            abababbb
        """.trimIndent()
        assertEquals(1, Day19(StringInput(input)).part2())
        assertEquals(12, Day19(StringInput(example2)).part2())
    }
}