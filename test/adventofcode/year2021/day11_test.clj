(ns adventofcode.year2021.day11-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day11 :as day]))

(def example-input
  (day/parse-input "5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"))

(deftest parse-input
  (is (= example-input [5 4 8 3 1 4 3 2 2 3
                        2 7 4 5 8 5 4 7 1 1
                        5 2 6 4 5 5 6 1 7 3
                        6 1 4 1 3 3 6 1 4 6
                        6 3 5 7 3 8 5 4 7 8
                        4 1 6 7 5 2 4 6 4 5
                        2 1 7 6 8 4 1 7 2 1
                        6 8 8 2 8 8 1 1 3 4
                        4 8 4 6 8 4 8 5 5 4
                        5 2 8 3 7 5 1 5 2 6])))

(deftest blaat
  (day/part1 (day/parse-input "0000000000
0000000000
0000000000
0000000000
0000000000
0000900000
0000000000
0000000000
0000000000
0000000000
")))

(deftest part1-example
  (is (= (day/part1 example-input) 1656)))

(deftest part2-example
  (is (= (day/part2 example-input) 195)))
