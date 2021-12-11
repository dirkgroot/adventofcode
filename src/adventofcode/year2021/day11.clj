(ns adventofcode.year2021.day11
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map #(str/split % #""))
       (map #(map (fn [n] (Integer/parseInt n)) %))
       (flatten)
       (vec)))

(def neighbors [-11 -10 -9 -1 1 9 10 11])
(def neighbors-left-edge [-10 -9 1 10 11])
(def neighbors-right-edge [-11 -10 -1 9 10])

(defn get-neighbors [index]
  (->> (cond (= (mod index 10) 0) neighbors-left-edge
             (= (mod index 10) 9) neighbors-right-edge
             :else neighbors)
       (map #(+ index %))
       (filter #(<= 0 % 99))))

(defn simulate-flashes [grid]
  (let [flashed          (filter #(> (get grid %) 9) (range 0 100))
        neighbors-counts (frequencies (flatten (map get-neighbors flashed)))]
    (if (empty? flashed)
      grid
      (simulate-flashes (vec (map-indexed (fn [idx energy] (cond (> energy 9) 0
                                                                 (zero? energy) 0
                                                                 :else (+ energy (get neighbors-counts idx 0))))
                                          grid))))))

(defn step [grid]
  (->> (vec (map inc grid))
       (simulate-flashes)))

(defn simulation [grid]
  (lazy-seq
    (let [after-step (step grid)]
      (cons after-step (simulation after-step)))))

(defn part1 [input]
  (->> (simulation input)
       (take 100)
       (map #(count (filter zero? %)))
       (reduce +)))

(defn part2 [input]
  (->> (simulation input)
       (take-while #(not-every? zero? %))
       (count)
       (inc)))

(def puzzle
  {:year        2021
   :day         11
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     1749
   :answer2     285})
