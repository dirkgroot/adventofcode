(ns adventofcode.year2021.day15
  (:require [clojure.string :as str]
            [clojure.pprint :refer :all]
            [clojure.data.priority-map :refer [priority-map]]))

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

(def infinity (Integer/MAX_VALUE))

(defn reconstruct-path
  ([came-from goal] (reconstruct-path came-from goal (came-from goal)))
  ([came-from current current-came-from]
   (lazy-seq (if current-came-from
               (cons current (reconstruct-path came-from current-came-from (came-from current-came-from)))
               nil))))

(defn evaluate-neighbors [grid [u dist-u] dist prev queue]
  (loop [[v & rest-v] (neighbors grid u)
         dist  dist
         prev  prev
         queue queue]
    (if (nil? v)
      [dist prev queue]
      (let [alt (+ dist-u (risk grid v))]
        (if (< alt (get dist v infinity))
          (recur rest-v (assoc dist v alt) (assoc prev v u) (assoc queue v alt))
          (recur rest-v dist prev queue))))))

(defn dijkstra [grid start goal]
  (loop [dist  {start 0}
         prev  {}
         queue (priority-map start 0)]
    (let [u     (peek queue)
          queue (pop queue)]
      (if (= (first u) goal)
        (reconstruct-path prev goal)
        (let [[dist prev queue] (evaluate-neighbors grid u dist prev queue)]
          (recur dist prev queue))))))

(defn lowest-total-risk [grid]
  (let [start [0 0]
        goal  [(dec (count (first grid))) (dec (count grid))]
        path  (dijkstra grid start goal)]
    (reduce #(+ %1 (risk grid %2)) 0 path)))

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
