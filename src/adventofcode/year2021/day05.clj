(ns adventofcode.year2021.day05
  (:require [clojure.pprint :refer :all]))

(defn parse-input [input]
  (->> (re-seq #"(\d+),(\d+) \-> (\d+),(\d+)" input)
       (map (fn [[_ x1 y1 x2 y2]] {:x1 (Integer/parseInt x1) :y1 (Integer/parseInt y1) :x2 (Integer/parseInt x2) :y2 (Integer/parseInt y2)}))
       (doall)))

(defn randomize [input]
  (shuffle input))

(defn step [a b]
  (let [d (- b a)]
    (cond
      (= d 0) 0
      (< d 0) -1
      :else 1)))

(defn all-covered-points
  ([[{x1 :x1 y1 :y1 x2 :x2 y2 :y2 dx :dx dy :dy} & rest]]
   (all-covered-points x1 y1 x2 y2 dx dy rest))
  ([x1 y1 x2 y2 dx dy rest]
   (lazy-seq (if (and (= x1 x2) (= y1 y2))
               (if (nil? rest)
                 '()
                 (cons [x1 y1] (all-covered-points rest)))
               (cons [x1 y1] (all-covered-points (+ x1 dx) (+ y1 dy) x2 y2 dx dy rest))))))

(defn is-horizontal-line? [{dx :dx dy :dy}]
  (or (= dx 0) (= dy 0)))

(defn line-direction [line]
  (let [{x1 :x1 y1 :y1 x2 :x2 y2 :y2} line]
    (assoc line :dx (step x1 x2)
                :dy (step y1 y2))))

(defn set-line-directions [lines]
  (map line-direction lines))

(defn count-points-with-overlapping-lines [input filter-fn]
  (->> (set-line-directions input)
       (filter filter-fn)
       (all-covered-points)
       (frequencies)
       (filter (fn [[_ v]] (> v 1)))
       (count)))

(defn part1 [input]
  (count-points-with-overlapping-lines input is-horizontal-line?))

(defn part2 [input]
  (count-points-with-overlapping-lines input any?))
