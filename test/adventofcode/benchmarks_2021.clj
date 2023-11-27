(ns adventofcode.benchmarks-2021
  (:require [clojure.test :refer :all]
            [adventofcode.support.benchmark :as benchmark]))

(deftest day05 (benchmark/benchmark-puzzle 2021 5))
(deftest day04 (benchmark/benchmark-puzzle 2021 4))
(deftest day06 (benchmark/benchmark-puzzle 2021 6))
(deftest day07 (benchmark/benchmark-puzzle 2021 7))
(deftest day11 (benchmark/benchmark-puzzle 2021 11))
