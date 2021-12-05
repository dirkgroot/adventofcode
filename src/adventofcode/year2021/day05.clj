(ns adventofcode.year2021.day05
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map #(str/replace % " -> " ","))
       (map #(str/split % #","))
       (map #(map (fn [s] (Integer/parseInt s)) %))
       (doall)))

(defn randomize-line-order [input]
  (shuffle input))

(defn step [a b]
  (let [d (- b a)]
    (cond
      (= d 0) 0
      (< d 0) -1
      :else 1)))

(defn line-covered-points [[x1 y1 x2 y2]]
  (let [step-x (step x1 x2)
        step-y (step y1 y2)]
    (loop [acc []
           x1 x1
           y1 y1
           x2 x2
           y2 y2]
      (if (and (= x1 x2) (= y1 y2))
        (conj acc [x1 y1])
        (recur (conj acc [x1 y1]) (+ x1 step-x) (+ y1 step-y) x2 y2)))))

(defn all-covered-points [lines]
  (reduce concat (map line-covered-points lines)))

(defn is-horizontal-line? [[x1 y1 x2 y2]]
  (or (= x1 x2) (= y1 y2)))

(defn part1 [input]
  (->> (filter is-horizontal-line? input)
       (all-covered-points)
       (frequencies)
       (filter (fn [[_ v]] (> v 1)))
       (count)))

(defn part2 [input]
  (->> (all-covered-points input)
       (frequencies)
       (filter (fn [[_ v]] (> v 1)))
       (count)))

(def puzzle
  {:year        2021
   :day         5
   :parse-input parse-input
   :randomize   randomize-line-order
   :part1       part1
   :part2       part2
   :answer1     5774
   :answer2     18423})
