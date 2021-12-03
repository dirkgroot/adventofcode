(ns adventofcode.year2021.day03
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (vec (map (fn [n]
              (map #(Character/digit ^char ^int % 2) n))
            (str/split-lines input))))

(defn bin-to-int [bin]
  (reduce #(+ (* %1 2) %2) bin))

(defn part1 [input]
  (let [number-count (count input)
        half-number-count (quot number-count 2)
        number-of-ones (apply map + input)
        gamma (bin-to-int (map #(if (> % half-number-count) 1 0) number-of-ones))
        epsilon (bin-to-int (map #(if (< % half-number-count) 1 0) number-of-ones))]
    (* gamma epsilon)))

(defn part2 [input]
  (let [oxygen-generator-rating (loop [remaining input
                                       bit 0]
                                  (if (= (count remaining) 1)
                                    (bin-to-int (first remaining))
                                    (let [number-count (count remaining)
                                          half-number-count (int (Math/ceil (/ number-count 2)))
                                          number-of-ones (apply map + remaining)
                                          keep-value (if (>= (nth number-of-ones bit) half-number-count) 1 0)]
                                      (recur (filter #(= (nth % bit) keep-value) remaining)
                                             (inc bit)))))
        co2-scrubber-rating (loop [remaining input
                                   bit 0]
                              (if (= (count remaining) 1)
                                (bin-to-int (first remaining))
                                (let [number-count (count remaining)
                                      half-number-count (int (Math/ceil (/ number-count 2)))
                                      number-of-ones (apply map + remaining)
                                      keep-value (if (< (nth number-of-ones bit) half-number-count) 1 0)]
                                  (recur (filter #(= (nth % bit) keep-value) remaining)
                                         (inc bit)))))]
    (* oxygen-generator-rating co2-scrubber-rating)))

(def puzzle
  {:year        2021
   :day         3
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     1307354
   :answer2     482500})
