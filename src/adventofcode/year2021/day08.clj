(ns adventofcode.year2021.day08
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-input [input]
  (doall
    (->> (str/split-lines input)
         (map #(str/split % #"\s+\|\s+"))
         (map (fn [[patterns-str output-str]] {:patterns (map set (str/split patterns-str #" "))
                                               :output   (map set (str/split output-str #" "))})))))

(defn count-1478 [input]
  (->> (input :output)
       (filter #(contains? #{2 4 3 7} (count %)))
       (count)))

(defn part1 [input]
  (reduce + (map #(count-1478 %) input)))

(defn deduce [patterns]
  (loop [[test & rest] [1 7 4 8 9 6 2 3 5 0]
         acc {}]
    (if (nil? test)
      acc
      (recur rest
             (assoc acc
               test
               (case test
                 1 (first (filter #(= (count %) 2) patterns))
                 7 (first (filter #(= (count %) 3) patterns))
                 4 (first (filter #(= (count %) 4) patterns))
                 8 (first (filter #(= (count %) 7) patterns))
                 9 (first (filter #(and (= (count %) 6) (= 1 (count (set/difference % (set/union (acc 4) (acc 7)))))) patterns))
                 6 (first (filter #(and (= (count %) 6) (not= % (acc 9)) (not (set/subset? (acc 7) %))) patterns))
                 2 (first (filter #(and (= (count %) 5) (not (set/subset? % (acc 9)))) patterns))
                 5 (first (filter #(and (= (count %) 5) (set/subset? % (acc 6))) patterns))
                 3 (first (filter #(and (= (count %) 5) (not= % (acc 5)) (set/subset? % (acc 9)) (not (set/subset? % (acc 6)))) patterns))
                 0 (first (filter #(not (some (fn [[_ v]] (= % v)) acc)) patterns))))))))

(defn part2 [input]
  (->> (map #(assoc % :deduction (set/map-invert (deduce (% :patterns)))) input)
       (map #(map (% :deduction) (% :output)))
       (map #(reduce (fn [acc n] (+ (* acc 10) n)) %))
       (reduce +)))

(def puzzle
  {:year        2021
   :day         8
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     352
   :answer2     936117})
