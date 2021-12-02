(ns adventofcode.year2021.solutions
  (:require [clojure.test :refer :all]
            [adventofcode.support.test :as test]
            [adventofcode.year2021.day01 :as day01]
            [adventofcode.year2021.day02 :as day02]))

(deftest day01 (test/test-puzzle day01/puzzle))
(deftest day02 (test/test-puzzle day02/puzzle))
