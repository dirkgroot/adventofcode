(ns adventofcode.year2021.day04
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-int [str]
  (Integer/parseInt str))

(defn rows [numbers]
  (partition 5 numbers))

(defn cols [numbers]
  (->> (rows numbers)
       (apply interleave)
       (partition 5)))

(defn parse-board [input]
  (let [cells (->> (str/trim input)
                   (#(str/split % #"[ \n]+"))
                   (map parse-int))]
    {:rows   (map set (rows cells))
     :cols   (map set (cols cells))
     :marked #{}}))

(defn parse-boards [input]
  (seq (map #(parse-board %) input)))

(defn parse-random-numbers [input]
  (->> (str/split input #",")
       (map parse-int)
       (seq)))

(defn parse-input [input]
  (let [chunks (str/split input #"\n\n")
        random-numbers-string (first chunks)
        board-strings (rest chunks)]
    [(parse-random-numbers random-numbers-string) (parse-boards board-strings)]))

(defn calculate-score [{rows :rows marked :marked last-marked :last-marked}]
  (* last-marked (reduce + (set/difference (reduce set/union rows) marked))))

(defn is-winner? [{rows :rows cols :cols marked :marked}]
  (or (some #(set/subset? % marked) rows)
      (some #(set/subset? % marked) cols)))

(defn play [number board]
  (when (not (:win board))
    (let [updated (update board :marked #(conj % number))]
      (assoc updated :win (is-winner? updated)
                     :last-marked number))))

(defn play-number-on [boards number]
  (map #(play number %) boards))

(defn winner-score [select-fn numbers boards]
  (->> (reductions play-number-on boards numbers)
       (flatten)
       (filter #(:win %))
       (select-fn)
       (calculate-score)))

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
