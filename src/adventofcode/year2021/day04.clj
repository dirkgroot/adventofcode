(ns adventofcode.year2021.day04
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-int [str]
  (Integer/parseInt str))

(defn parse-random-numbers [input]
  (map parse-int (str/split input #",")))

(defn parse-boards [input]
  (map #(hash-map :numbers (vec (map parse-int (str/split (str/trim %) #"[ \n]+")))
                  :marked #{})
       input))

(defn parse-input [input]
  (let [chunks (str/split input #"\n\n")
        random-numbers-string (first chunks)
        board-strings (rest chunks)]
    [(parse-random-numbers random-numbers-string) (parse-boards board-strings)]))

(defn mark-board [board number]
  (update-in board [:marked]
             #(if (some (partial = number) (board :numbers))
                (conj % number)
                %)))

(defn rows [numbers]
  (partition 5 numbers))

(defn cols [numbers]
  (partition 5 (apply interleave (rows numbers))))

(defn is-winner? [drawn board]
  (let [any? (complement not-any?)
        rows (map set (rows board))
        cols (map set (cols board))]
    (or (any? #(set/subset? % drawn) rows)
        (any? #(set/subset? % drawn) cols))))

(defn find-winners [boards]
  (filter #(is-winner? (:marked %) (:numbers %)) boards))

(defn find-winner [boards]
  (first (find-winners boards)))

(defn sum-of-unmarked [{numbers :numbers marked :marked}]
  (reduce + (set/difference (set numbers) marked)))

(defn part1 [[numbers boards]]
  (loop [boards boards
         [n & ns] numbers]
    (let [updated-boards (map #(mark-board % n) boards)
          winner (find-winner updated-boards)]
      (if (nil? winner)
        (recur updated-boards ns)
        (* n (sum-of-unmarked winner))))))

(defn part2 [[numbers boards]]
  (loop [boards boards
         [n & ns] numbers
         last-winner nil
         winning-move nil]
    (if (nil? n)
      (* winning-move (sum-of-unmarked last-winner))
      (let [updated-boards (map #(mark-board % n) boards)
            winners (find-winners updated-boards)]
        (recur (remove #(some (partial = %) winners) updated-boards) ns
               (if (not (empty? winners)) (first winners) last-winner)
               (if (not (empty? winners)) n winning-move))))))

(def puzzle
  {:year        2021
   :day         4
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     12796
   :answer2     18063})
