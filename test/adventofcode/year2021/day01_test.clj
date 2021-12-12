(ns adventofcode.year2021.day01-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day01 :as day01]
            [adventofcode.support.test :as test]))

(def example-input
  (day01/parse-input
    "199
200
208
210
200
207
240
269
260
263"))

(deftest part1-example
  (is (= 7 (day01/part1 example-input))))

(deftest part2-example
  (is (= 5 (day01/part2 example-input))))

(deftest solution
  (test/test-puzzle 2021 1 1711 1743))
