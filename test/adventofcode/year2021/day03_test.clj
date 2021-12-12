(ns adventofcode.year2021.day03-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day03 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input
    "00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010"))

(deftest part1-example
  (is (= (day/part1 example-input) 198)))

(deftest part2-example
  (is (= (day/part2 example-input) 230)))

(deftest solution
  (test/test-puzzle 2021 3 1307354 482500))
