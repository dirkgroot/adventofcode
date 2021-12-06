(ns adventofcode.year2021.day06
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map #(Integer/parseInt %) (str/split (str/trim input) #",")))

(defn randomize [input]
  (shuffle input))

(defn simulate-day [population]
  {8 (population 0 0)
   7 (population 8 0)
   6 (+ (population 7 0) (population 0 0))
   5 (population 6 0)
   4 (population 5 0)
   3 (population 4 0)
   2 (population 3 0)
   1 (population 2 0)
   0 (population 1 0)})

(defn simulations [initial-population]
  (lazy-seq (cons initial-population
                  (simulations (simulate-day initial-population)))))

(defn number-of-lanternfish-after [days initial-population]
  (reduce #(+ %1 (second %2))
          0
          (nth (simulations (frequencies initial-population)) days)))

(defn part1 [input]
  (number-of-lanternfish-after 80 input))

(defn part2 [input]
  (number-of-lanternfish-after 256 input))

(def puzzle
  {:year        2021
   :day         6
   :parse-input parse-input
   :randomize   randomize
   :part1       part1
   :part2       part2
   :answer1     359999
   :answer2     1631647919273})
