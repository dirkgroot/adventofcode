(ns adventofcode.year2021.day21-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day21 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "Player 1 starting position: 4
Player 2 starting position: 8"))

(deftest parse
  (is (= example-input [3 7])))

(deftest part1-example
  (is (= (day/part1 example-input) 739785)))

(deftest part2-example
  (is (= (day/part2 example-input) 444356092776315)))

(deftest solution
  (test/test-puzzle 2021 21 908595 91559198282731))
