(ns adventofcode.year2021.day15
  (:require [clojure.string :as str]
            [clojure.pprint :refer :all]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map #(re-seq #"\d" %))
       (map (fn [line] (map #(Integer/parseInt %) line)))
       (map vec)
       (vec)))

(defn neighbors [graph [x y]]
  (->> [[(dec x) y] [(inc x) y] [x (dec y)] [x (inc y)]]
       (filter (fn [[nx ny]] (and (<= 0 nx (dec (count (first graph))))
                                  (<= 0 ny (dec (count graph))))))))

(defn risk [graph [x y]]
  (get (get graph y) x))

(def infinity 1000000)

(defn shortest-path [graph start goal]
  (letfn [(evaluate-neighbors [current open-set came-from g-score f-score]
            (loop [[neighbor & rest] (neighbors graph current)
                   open-set  (disj open-set current)
                   came-from came-from
                   g-score   g-score
                   f-score   f-score]
              (if (nil? neighbor)
                [open-set came-from g-score f-score]
                (let [tentative-score (+ (get g-score current infinity) (risk graph neighbor))]
                  (if (< tentative-score (get g-score neighbor infinity))
                    (recur rest
                           (conj open-set neighbor)
                           (assoc came-from neighbor current)
                           (assoc g-score neighbor tentative-score)
                           (assoc f-score neighbor (+ tentative-score (risk graph neighbor))))
                    (recur rest open-set came-from g-score f-score))))))
          (reconstruct-path [came-from current]
            (loop [path              nil
                   came-from         came-from
                   current           current
                   current-came-from (came-from current)]
              (if current-came-from
                (recur (cons current path) came-from current-came-from (came-from current-came-from))
                path)))]
    (loop [open-set  #{start}
           came-from {}
           g-score   {start 0}
           f-score   {start (risk graph start)}]
      (let [current (apply min-key #(get f-score % infinity) open-set)]
        (if (= current goal)
          (reconstruct-path came-from current)
          (let [[open-set came-from g-score f-score] (evaluate-neighbors current open-set came-from g-score f-score)]
            (recur open-set came-from g-score f-score)))))))

(defn lowest-total-risk [grid]
  (let [start [0 0]
        goal  [(dec (count (first grid))) (dec (count grid))]]
    (reduce #(+ %1 (risk grid %2)) 0 (shortest-path grid start goal))))

(defn part1 [input]
  (lowest-total-risk input))

(defn enlarge-grid [input]
  (let [input-height (count input)
        input-width  (count (first input))
        height       (* input-height 5)
        width        (* input-width 5)]
    (vec (for [y (range 0 height)]
           (vec (map (fn [x] (let [source-x (mod x input-width)
                                   source-y (mod y input-height)
                                   score    (+ (risk input [source-x source-y]) (quot x input-width) (quot y input-height))]
                               (if (> score 9) (- score 9) score)))
                     (range 0 width)))))))

(defn part2 [input]
  (lowest-total-risk (enlarge-grid input)))
