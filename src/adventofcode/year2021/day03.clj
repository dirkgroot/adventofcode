(ns adventofcode.year2021.day03
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (vec (map (fn [n]
              (map #(Character/digit ^char ^int % 2) n))
            (str/split-lines input))))

(defn bin-to-int [bin]
  (reduce #(+ (* %1 2) %2) bin))

(defn part1 [input]
  (let [half-number-count (quot (count input) 2)
        number-of-ones (apply map + input)
        gamma (bin-to-int (map #(if (> % half-number-count) 1 0) number-of-ones))
        epsilon (bin-to-int (map #(if (< % half-number-count) 1 0) number-of-ones))]
    (* gamma epsilon)))

(defn calculate-rating [input keep-requirement]
  (loop [remaining input
         bit 0]
    (if (= (count remaining) 1)
      (bin-to-int (first remaining))
      (let [number-count (count remaining)
            half-number-count (int (Math/ceil (/ number-count 2)))
            number-of-ones (apply map + remaining)
            keep-value (if (keep-requirement (nth number-of-ones bit) half-number-count) 1 0)]
        (recur (filter #(= (nth % bit) keep-value) remaining)
               (inc bit))))))

(defn part2 [input]
  (let [oxygen-generator-rating (calculate-rating input >=)
        co2-scrubber-rating (calculate-rating input <)]
    (* oxygen-generator-rating co2-scrubber-rating)))

(def puzzle
  {:year        2021
   :day         3
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     1307354
   :answer2     482500})
