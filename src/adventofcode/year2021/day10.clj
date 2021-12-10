(ns adventofcode.year2021.day10
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]
            [adventofcode.util.stats :refer :all]))

(defn parse-input [input]
  (str/split-lines input))

(def matching-brace
  {\( \)
   \[ \]
   \{ \}
   \< \>})

(defn open-close-pairs
  ([line] (open-close-pairs line nil))
  ([[c & cs] stack]
   (lazy-seq
     (cond
       (and (nil? c) (empty? stack)) nil
       (nil? c) (cons {:first (first stack) :second (matching-brace (first stack)) :added true} (open-close-pairs nil (rest stack)))
       (contains? #{\( \[ \{ \<} c) (open-close-pairs cs (cons c stack))
       (contains? #{\) \] \} \>} c) (cons {:first (first stack) :second c} (open-close-pairs cs (rest stack)))))))

(defn part1-score [c]
  (case c
    \) 3
    \] 57
    \} 1197
    \> 25137))

(def valid-pairs #{[\( \)] [\[ \]] [\{ \}] [\< \>]})

(defn is-valid? [{first :first second :second}]
  (contains? valid-pairs [first second]))

(defn part1 [input]
  (->> (map open-close-pairs input)
       (map (fn [pairs] (first (filter #(not (is-valid? %)) pairs))))
       (filter (complement nil?))
       (map :second)
       (map part1-score)
       (reduce +)))

(defn part2-score [c]
  (case c
    \) 1
    \] 2
    \} 3
    \> 4))

(defn part2 [input]
  (->> (map open-close-pairs input)
       (filter (fn [pairs] (every? #(is-valid? %) pairs)))
       (map (fn [pairs] (filter #(contains? % :added) pairs)))
       (map (fn [pairs] (map :second pairs)))
       (map (fn [chars] (reduce #(+ (* %1 5) %2) (map part2-score chars))))
       (median)))

(def puzzle
  {:year        2021
   :day         10
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     311949
   :answer2     3042730309})
