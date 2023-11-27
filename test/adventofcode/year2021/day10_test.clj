(ns adventofcode.year2021.day10-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer :all]
            [adventofcode.year2021.day10 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input
    "[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]"))

(deftest part1-example
  (is (= (day/part1 example-input) 26397)))

(deftest part2-example
  (is (= (day/part2 example-input) 288957)))

(deftest solution
  (test/test-puzzle 2021 10 311949 3042730309))
