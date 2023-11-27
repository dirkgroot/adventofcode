(ns adventofcode.year2021.day14-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day14 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C
"))

(deftest part1-example
  (is (= (day/part1 example-input) 1588)))

(deftest part2-example
  (is (= (day/part2 example-input) 2188189693529)))

(deftest solution
  (test/test-puzzle 2021 14 2170 2422444761283))
