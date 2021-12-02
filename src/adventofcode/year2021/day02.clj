(ns adventofcode.year2021.day02
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (vec (map (fn [[cmd units]] [cmd (Integer/parseInt units)])
            (partition 2 (str/split input #"\s")))))

(defn do-command-part1 [[hpos depth] [cmd units]]
  (case cmd
    "forward" [(+ hpos units) depth]
    "down" [hpos (+ depth units)]
    "up" [hpos (- depth units)]))

(defn part1 [input]
  (apply * (reduce do-command-part1 [0 0] input)))

(defn do-command-part2 [[hpos depth aim] [cmd units]]
  (case cmd
    "forward" [(+ hpos units) (+ depth (* aim units)) aim]
    "down" [hpos depth (+ aim units)]
    "up" [hpos depth (- aim units)]))

(defn part2 [input]
  (apply * (take 2 (reduce do-command-part2 [0 0 0] input))))

(def puzzle
  {:year        2021
   :day         2
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     1250395
   :answer2     1451210346})
