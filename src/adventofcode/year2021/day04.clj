(ns adventofcode.year2021.day04
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-int [str]
  (Integer/parseInt str))

(defn parse-random-numbers [input]
  (vec (map parse-int (str/split input #","))))

(defn parse-boards [input]
  (vec (map #(map parse-int (str/split (str/trim %) #"[ \n]+"))
            input)))

(defn parse-input [input]
  (let [chunks (str/split input #"\n\n")
        random-numbers-string (first chunks)
        board-strings (rest chunks)]
    [(parse-random-numbers random-numbers-string) (parse-boards board-strings)]))

(defn rows [numbers]
  (partition 5 numbers))

(defn cols [numbers]
  (partition 5 (apply interleave (rows numbers))))

(defn is-winner? [drawn board]
  (let [any? (complement not-any?)
        rows (map set (rows board))
        cols (map set (cols board))]
    (or (any? #(set/subset? % (set drawn)) rows)
        (any? #(set/subset? % (set drawn)) cols))))

(defn map-turns-to-winners
  ([numbers boards] (filter #(not-empty (second %)) (map-turns-to-winners numbers boards 1)))
  ([numbers boards turn]
   (lazy-seq
     (if (> turn (count numbers))
       '()
       (let [drawn (take turn numbers)
             {win true rest false} (group-by #(is-winner? drawn %) boards)]
         (cons [drawn (first win)] (map-turns-to-winners numbers rest (inc turn))))))))

(defn calculate-score [drawn board]
  (* (last drawn)
     (reduce + (set/difference (set board) (set drawn)))))

(defn part1 [[numbers boards]]
  (let [turn-winners (map-turns-to-winners numbers boards)
        [drawn board] (first turn-winners)]
    (calculate-score drawn board)))

(defn part2 [[numbers boards]]
  (let [turn-winners (map-turns-to-winners numbers boards)
        [drawn board] (last turn-winners)]
    (calculate-score drawn board)))

(def puzzle
  {:year        2021
   :day         4
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     12796
   :answer2     18063})
