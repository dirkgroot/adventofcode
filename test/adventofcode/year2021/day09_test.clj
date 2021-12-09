(ns adventofcode.year2021.day09-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day09 :as day]))

(def example-input
  (day/parse-input "2199943210
3987894921
9856789892
8767896789
9899965678"))

(deftest part1-example
  (is (= (day/part1 example-input) 15)))

(deftest part2-example
  (is (= (day/part2 example-input) 1134)))
