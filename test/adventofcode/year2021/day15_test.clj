(ns adventofcode.year2021.day15-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day15 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581"))

(deftest part1-example
  (is (= (day/part1 example-input) 40)))

(deftest part2-example
  (is (= (day/part2 example-input) 315)))

(deftest solution
  (test/test-puzzle 2021 15 595 2914))
