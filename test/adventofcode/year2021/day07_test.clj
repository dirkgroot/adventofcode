(ns adventofcode.year2021.day07-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day07 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "16,1,2,0,4,2,7,1,2,14"))

(deftest part1-example
  (is (= (day/part1 example-input) 37)))

(deftest part2-example
  (is (= (day/part2 example-input) 168)))

(deftest solution
  (test/test-puzzle 2021 7 352331 99266250))
