(ns adventofcode.year2021.day09-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day09 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "2199943210
3987894921
9856789892
8767896789
9899965678"))

(deftest parse-input
  (is (= example-input
         {:heights [[9 9 9 9 9 9 9 9 9 9 9 9]
                    [9 2 1 9 9 9 4 3 2 1 0 9]
                    [9 3 9 8 7 8 9 4 9 2 1 9]
                    [9 9 8 5 6 7 8 9 8 9 2 9]
                    [9 8 7 6 7 8 9 6 7 8 9 9]
                    [9 9 8 9 9 9 6 5 6 7 8 9]
                    [9 9 9 9 9 9 9 9 9 9 9 9]]
          :nx      10
          :ny      5}))
  (is (vector? (example-input :heights)))
  (is (every? #(vector? %) (example-input :heights))))

(deftest part1-example
  (is (= (day/part1 example-input) 15)))

(deftest part2-example
  (is (= (day/part2 example-input) 1134)))

(deftest solution
  (test/test-puzzle 2021 9 530 1019494))
