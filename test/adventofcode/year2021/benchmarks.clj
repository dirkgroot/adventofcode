(ns adventofcode.year2021.benchmarks
  (:require [clojure.test :refer :all]
            [adventofcode.support.benchmark :as benchmark]
            [adventofcode.year2021.day04 :as day04]
            [adventofcode.year2021.day05 :as day05]
            [adventofcode.year2021.day06 :as day06]
            [adventofcode.year2021.day07 :as day07]
            [adventofcode.year2021.day11 :as day11]))

(deftest day04 (benchmark/benchmark-puzzle day04/puzzle))
(deftest day05 (benchmark/benchmark-puzzle day05/puzzle))
(deftest day06 (benchmark/benchmark-puzzle day06/puzzle))
(deftest day07 (benchmark/benchmark-puzzle day07/puzzle))
(deftest day11 (benchmark/benchmark-puzzle day11/puzzle))
