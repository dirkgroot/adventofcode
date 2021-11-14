(ns adventofcode.year2020.day06
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-input [input]
  (map (fn [person] (map set (str/split person #"\n")))
       (str/split input #"\n\n")))

(defn count-answers [reducer groups]
  (reduce + (map count (map (partial reduce reducer) groups))))

(defn part1 [groups] (count-answers set/union groups))
(defn part2 [groups] (count-answers set/intersection groups))

(def puzzle
  {:year        2020
   :day         6
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     6170
   :answer2     2947})
