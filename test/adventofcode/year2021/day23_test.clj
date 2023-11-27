(ns adventofcode.year2021.day23-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day23 :as day]
            [adventofcode.support.test :as test]))

(def example-input
  (day/parse-input "#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########"))

(deftest path
  (is (= (day/path example-input 11 0) [2 1 0]))
  (is (= (day/path example-input 11 10) [2 3 4 5 6 7 8 9 10]))
  (is (= (day/path example-input 15 0) nil))
  (is (= (day/path [\A \. \. \. \. \. \. \. \. \. \.
                    \A \. \. \.
                    \. \. \. \.] 0 11) nil))
  (is (= (day/path [\A \. \. \. \. \. \. \. \. \. \.
                    \A \. \. \.
                    \. \. \. \.] 0 15) nil)))

(deftest valid-destinations
  (is (= (day/valid-destinations [\A \. \. \. \. \. \. \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.] 0)
         [[0 23 6]]))
  (is (= (day/valid-destinations [\A \. \. \. \. \. \. \. \. \. \.
                                  \. \. \. \.
                                  \A \. \. \.
                                  \A \. \. \.
                                  \A \. \. \.] 0)
         [[0 11 3]]))
  (is (= (day/valid-destinations [\B \. \. \. \. \. \. \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.] 0)
         [[0 24 80]]))
  (is (= (day/valid-destinations [\. \. \. \. \. \. \. \. \. \. \.
                                  \A \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.] 11)
         [[11 0 3] [11 1 2] [11 3 2] [11 5 4] [11 7 6] [11 9 8] [11 10 9]]))
  (is (= (day/valid-destinations [\. \. \. \B \. \. \. \. \. \. \.
                                  \A \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.] 11)
         [[11 0 3] [11 1 2]]))
  (is (= (day/valid-destinations [\A \. \. \. \. \. \. \. \. \. \.
                                  \. \. \. \.
                                  \A \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.] 0)
         []))
  (is (= (day/valid-destinations [\. \. \. \. \. \. \. \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \A \. \. \.] 23)
         []))
  (is (= (day/valid-destinations [\. \. \. \. \. \. \. \. \. \. \.
                                  \. \. \. \.
                                  \. \. \. \.
                                  \A \. \. \.
                                  \A \. \. \.] 19)
         []))
  (is (= (day/valid-destinations (into example-input [\A \B \C \D \A \B \C \D]) 11)
         [[11 0 30] [11 1 20] [11 3 20] [11 5 40] [11 7 60] [11 9 80] [11 10 90]])))

(deftest valid-steps
  (is (= (day/valid-steps [\A \. \. \. \. \. \. \. \. \. \.
                           \. \. \. \.
                           \. \. \. \.
                           \. \. \. \.
                           \. \. \. \.])
         [[0 23 6]])))

(deftest apply-step
  (is (= (day/apply-step [\A \. \. \. \. \. \. \. \. \. \.
                          \. \. \. \.
                          \. \. \. \.] [0 15 4])
         [\. \. \. \. \. \. \. \. \. \. \.
          \. \. \. \.
          \A \. \. \.])))

(deftest most-efficient-solution
  (is (= (day/most-efficient-solution day/goal) 0) "goal reached")
  (is (= (day/most-efficient-solution (day/apply-step day/goal [11 0])) 3) "only one step"))

(deftest part1-example
  (is (= (day/part1 example-input) 12521)))

(deftest part2-example
  (is (= (day/part2 example-input) 44169)))

(deftest solution
  (test/test-puzzle 2021 23 15109 53751))
