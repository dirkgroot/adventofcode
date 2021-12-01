(ns adventofcode.year2021.day01
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map #(Integer/parseInt %) (str/split-lines input)))

(defn count-increases [numbers]
  (count (filter #(apply < %)
                 (partition 2 1 numbers))))

(defn part1 [input]
  (count-increases input))

(defn part2 [input]
  (count-increases (map #(apply + %)
                        (partition 3 1 input))))

(def puzzle
  {:year        2021
   :day         1
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     1711
   :answer2     1743})
