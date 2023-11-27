(ns adventofcode.year2021.day17-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day17 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "target area: x=20..30, y=-10..-5"))

(deftest part1-example
  (is (= (day/part1 example-input) 45)))

(deftest part2-example
  (is (= (day/part2 example-input) 112)))

(deftest solution
  (test/test-puzzle 2021 17 3570 1919))
