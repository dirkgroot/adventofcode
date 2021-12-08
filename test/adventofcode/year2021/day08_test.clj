(ns adventofcode.year2021.day08-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer :all]
            [adventofcode.year2021.day08 :as day]))

(def example-input
  (day/parse-input
    "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
"))

(deftest deduce-test
  (is (= (day/deduce (map set ["acedgfb" "cdfbe" "gcdfa" "fbcad" "dab" "cefabd" "cdfgeb" "eafb" "cagedb" "ab"]))
         {0 #{\c \a \g \e \d \b}
          1 #{\a \b}
          2 #{\g \c \d \f \a}
          3 #{\f \b \c \a \d}
          4 #{\e \a \f \b}
          5 #{\c \d \f \b \e}
          6 #{\c \d \f \g \e \b}
          7 #{\d \a \b}
          8 #{\a \c \e \d \g \f \b}
          9 #{\c \e \f \a \b \d}})))

(deftest part1-example
  (is (= (day/part1 example-input) 26)))

(deftest part2-example
  (is (= (day/part2 example-input) 61229)))
