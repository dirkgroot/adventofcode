(ns adventofcode.year2021.day04
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-int [str]
  (Integer/parseInt str))

(defn parse-random-numbers [input]
  (->> input
       (#(str/split % #","))
       (map parse-int)
       (vec)))

(defn rows [numbers]
  (partition 5 numbers))

(defn cols [numbers]
  (->> numbers
       (partition 5)
       (apply interleave)
       (partition 5)))

(defn parse-boards [input]
  (map
    #(let [cells (->> %
                      (str/trim)
                      ((fn [s] (str/split s #"[ \n]+")))
                      (map parse-int))]
       {:rows (map set (rows cells))
        :cols (map set (cols cells))})
    input))

(defn parse-input [input]
  (let [chunks (str/split input #"\n\n")
        random-numbers-string (first chunks)
        board-strings (rest chunks)]
    [(parse-random-numbers random-numbers-string) (parse-boards board-strings)]))

(defn is-winner? [drawn {rows :rows cols :cols}]
  (let [any? (complement not-any?)
        drawn (set drawn)]
    (or (any? #(set/subset? % drawn) rows)
        (any? #(set/subset? % drawn) cols))))

(defn map-turns-to-winners
  ([numbers boards] (filter #(not-empty (second %)) (map-turns-to-winners numbers boards 1)))
  ([numbers boards turn]
   (lazy-seq
     (if (> turn (count numbers))
       '()
       (let [drawn (take turn numbers)
             {win true rest false} (group-by #(is-winner? drawn %) boards)]
         (cons [drawn (first win)] (map-turns-to-winners numbers rest (inc turn))))))))

(defn winner-score [fn numbers boards]
  (let [turn-winners (map-turns-to-winners numbers boards)
        [drawn {rows :rows}] (fn turn-winners)
        non-marked (set/difference (apply set/union rows) (set drawn))]
    (* (last drawn) (reduce + non-marked))))

(defn part1 [[numbers boards]]
  (winner-score first numbers boards))

(defn part2 [[numbers boards]]
  (winner-score last numbers boards))

(def puzzle
  {:year        2021
   :day         4
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     12796
   :answer2     18063})
