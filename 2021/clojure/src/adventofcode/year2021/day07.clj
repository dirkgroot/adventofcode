(ns adventofcode.year2021.day07
  (:require [adventofcode.util.math :refer :all]
            [clojure.string :as str]))

(defn parse-input [input]
  (doall (map #(Integer/parseInt %) (str/split (str/trim input) #","))))

(defn randomize [input]
  (shuffle input))

(defn min-total-cost [cost-fn]
  (->> (range 0 2000)
       (map #(cost-fn %))
       (apply min)))

(defn part1 [input]
  (let [cost-fn (fn [dest] (reduce + (map #(abs (- dest %)) input)))]
    (min-total-cost cost-fn)))

(defn sum-of-range [from to]
  (* (/ (- (inc to) from) 2) (+ from to)))

(defn part2 [input]
  (let [cost-fn (fn [dest] (reduce + (map #(sum-of-range 1 (abs (- dest %))) input)))]
    (min-total-cost cost-fn)))
