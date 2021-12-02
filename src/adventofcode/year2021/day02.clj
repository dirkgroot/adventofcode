(ns adventofcode.year2021.day02
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (vec (map (fn [[cmd units]] [cmd (Integer/parseInt units)])
            (partition 2 (str/split input #"\s")))))

(defn do-command [[hpos depth aim] [cmd units]]
  (case cmd
    "forward" [(+ hpos units) (+ depth (* aim units)) aim]
    "down" [hpos depth (+ aim units)]
    "up" [hpos depth (- aim units)]))

(defn part1 [input]
  (let [[hpos _ aim] (reduce do-command [0 0 0] input)]
    (* hpos aim)))

(defn part2 [input]
  (let [[hpos depth _] (reduce do-command [0 0 0] input)]
    (* hpos depth)))

(def puzzle
  {:year        2021
   :day         2
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     1250395
   :answer2     1451210346})
