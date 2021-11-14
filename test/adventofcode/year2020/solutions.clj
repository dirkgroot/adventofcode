(ns adventofcode.year2020.solutions
  (:require [clojure.test :refer :all]
            [adventofcode.support.test :as test]
            [adventofcode.year2020.day06 :as day06]
            [adventofcode.year2020.day25 :as day25]))

(deftest day06 (test/test-puzzle day06/puzzle))
(deftest day25 (test/test-puzzle day25/puzzle))
