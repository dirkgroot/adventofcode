(ns adventofcode.year2021.day01
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (vec (map #(Integer/parseInt %) (str/split-lines input))))

(defn count-increases [numbers]
  (count (filter #(apply < %)
                 (partition 2 1 numbers))))

(defn part1 [input]
  (count-increases input))

(defn part2 [input]
  (count-increases (map #(apply + %)
                        (partition 3 1 input))))
