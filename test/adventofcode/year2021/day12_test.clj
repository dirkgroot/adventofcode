(ns adventofcode.year2021.day12-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day12 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "start-A
start-b
A-c
A-b
b-d
A-end
b-end"))

(deftest parse-input
  (is (= example-input {"start" {:name "start" :visited 0 :connected-to #{"A" "b"} :small? true}
                        "c"     {:name "c" :visited 0 :connected-to #{"A"} :small? true}
                        "A"     {:name "A" :visited 0 :connected-to #{"c" "start" "b" "end"} :small? false}
                        "b"     {:name "b" :visited 0 :connected-to #{"start" "A" "d" "end"} :small? true}
                        "d"     {:name "d" :visited 0 :connected-to #{"b"} :small? true}
                        "end"   {:name "end" :visited 0 :connected-to #{"A" "b"} :small? true}})))

(deftest part1-example
  (is (= (day/part1 example-input) 10)))

(deftest part2-example
  (is (= (day/part2 example-input) 36)))

(deftest solution
  (test/test-puzzle 2021 12 5576 152837))
