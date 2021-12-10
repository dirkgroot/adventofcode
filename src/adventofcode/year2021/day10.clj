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
   (lazy-seq (cond (and (nil? c) (empty? stack)) nil
                   (contains? #{\( \[ \{ \<} c) (open-close-pairs cs (cons c stack))
                   (contains? #{\) \] \} \>} c) (cons {:first (first stack) :second c}
                                                      (open-close-pairs cs (rest stack)))
                   :else (cons {:first (first stack) :second (matching-brace (first stack)) :added true}
                               (open-close-pairs nil (rest stack)))))))

(defn syntax-error-score [c]
  (case c
    \) 3
    \] 57
    \} 1197
    \> 25137))

(def valid-pairs #{[\( \)] [\[ \]] [\{ \}] [\< \>]})

(defn is-valid? [{first :first second :second}]
  (contains? valid-pairs [first second]))

(defn first-invalid-pair [pairs]
  (first (filter #(not (is-valid? %)) pairs)))

(defn part1 [input]
  (->> (map open-close-pairs input)
       (map first-invalid-pair)
       (filter (complement nil?))
       (map :second)
       (map syntax-error-score)
       (reduce +)))

(defn autocomplete-score [c]
  (case c
    \) 1
    \] 2
    \} 3
    \> 4))

(defn calculate-autocomplete-score [pairs]
  (->> (filter #(contains? % :added) pairs)
       (map :second)
       (map autocomplete-score)
       (reduce #(+ (* %1 5) %2))))

(defn only-valid-lines [pairs]
  (every? is-valid? pairs))

(defn part2 [input]
  (->> (map open-close-pairs input)
       (filter only-valid-lines)
       (map calculate-autocomplete-score)
       (median)))

(def puzzle
  {:year        2021
   :day         10
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     311949
   :answer2     3042730309})
