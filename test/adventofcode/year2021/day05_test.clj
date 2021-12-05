(ns adventofcode.year2021.day05-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day05 :as day]))

(def example-input
  (day/parse-input
    "0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2"))

(deftest part1-example
  (is (= (day/part1 example-input) 5)))

(deftest part2-example
  (is (= (day/part2 example-input) :todo)))
