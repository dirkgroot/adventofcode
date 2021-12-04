(ns adventofcode.year2021.benchmarks
  (:require [clojure.test :refer :all]
            [adventofcode.support.benchmark :as benchmark]
            [adventofcode.year2021.day04 :as day04]))

(deftest day04 (benchmark/benchmark-puzzle day04/puzzle))
