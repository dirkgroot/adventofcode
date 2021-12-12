(ns adventofcode.year2021.day10
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]
            [adventofcode.util.stats :refer :all]))

(defn parse-input [input]
  (str/split-lines input))

(defn open-close-pairs
  ([line] (open-close-pairs line nil))
  ([[c & cs] stack]
   (lazy-seq (cond (and (nil? c) (empty? stack)) nil
                   (contains? #{\( \[ \{ \<} c) (open-close-pairs cs (cons c stack))
                   (contains? #{\) \] \} \>} c) (cons {:first (first stack) :second c} (open-close-pairs cs (rest stack)))
                   :else (cons {:first (first stack) :open true} (open-close-pairs nil (rest stack)))))))

(defn is-valid? [{first :first second :second open :open}]
  (or open (contains? #{[\( \)] [\[ \]] [\{ \}] [\< \>]}
                      [first second])))

(defn first-invalid-pair [pairs]
  (first (filter #(not (is-valid? %)) pairs)))

(defn part1 [input]
  (->> (map open-close-pairs input)
       (map first-invalid-pair)
       (filter (complement nil?))
       (map :second)
       (map {\) 3 \] 57 \} 1197 \> 25137})
       (reduce +)))

(defn calculate-autocomplete-score [pairs]
  (->> (filter #(contains? % :open) pairs)
       (map :first)
       (map {\( 1 \[ 2 \{ 3 \< 4})
       (reduce #(+ (* %1 5) %2))))

(defn part2 [input]
  (->> (map open-close-pairs input)
       (filter #(every? is-valid? %))
       (map calculate-autocomplete-score)
       (median)))
