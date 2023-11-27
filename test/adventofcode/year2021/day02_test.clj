(ns adventofcode.year2021.day02-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day02 :as day02]
            [adventofcode.support.test :as test]))

(def example-input
  (day02/parse-input
    "forward 5
down 5
forward 8
up 3
down 8
forward 2"))

(deftest part1-example
  (is (= (day02/part1 example-input) 150)))

(deftest part2-example
  (is (= (day02/part2 example-input) 900)))

(deftest solution
  (test/test-puzzle 2021 2 1250395 1451210346))
