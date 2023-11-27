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

(defn neighbors [{width :width height :height} [x y]]
  (->> [[(dec x) y] [(inc x) y] [x (dec y)] [x (inc y)]]
       (filter (fn [[nx ny]] (and (<= 0 nx (dec width))
                                  (<= 0 ny (dec height)))))))

(defn risk [{grid :grid input-width :input-width input-height :input-height} [x y]]
  (let [source-x (mod x input-width)
        source-y (mod y input-height)
        score    (+ (get (get grid source-y) source-x) (quot x input-width) (quot y input-height) -1)]
    (inc (mod score 9))))

(def infinity (Integer/MAX_VALUE))

(defn reconstruct-path
  ([came-from goal] (reconstruct-path came-from goal (came-from goal)))
  ([came-from current current-came-from]
   (lazy-seq (if current-came-from
               (cons current (reconstruct-path came-from current-came-from (came-from current-came-from)))
               nil))))

(defn evaluate-neighbors [cave [pos risk-pos] dist prev queue]
  (loop [[v & rest-v] (neighbors cave pos)
         dist  dist
         prev  prev
         queue queue]
    (if (nil? v)
      [dist prev queue]
      (let [alt (+ risk-pos (risk cave v))]
        (if (< alt (get dist v infinity))
          (recur rest-v (assoc dist v alt) (assoc prev v pos) (assoc queue v alt))
          (recur rest-v dist prev queue))))))

(defn dijkstra [cave start goal]
  (loop [dist  {start 0}
         prev  {}
         queue (priority-map start 0)]
    (let [[pos _ :as min-risk] (peek queue)
          queue (pop queue)]
      (if (= pos goal)
        (reconstruct-path prev goal)
        (let [[dist prev queue] (evaluate-neighbors cave min-risk dist prev queue)]
          (recur dist prev queue))))))

(defn lowest-total-risk [{width :width height :height :as cave}]
  (let [start [0 0]
        path  (dijkstra cave start [(dec width) (dec height)])]
    (reduce #(+ %1 (risk cave %2)) 0 path)))

(defn create-cave [input multiplier]
  {:grid         input
   :input-width  (count (first input))
   :input-height (count input)
   :width        (* (count (first input)) multiplier)
   :height       (* (count input) multiplier)})

(defn part1 [input] (lowest-total-risk (create-cave input 1)))
(defn part2 [input] (lowest-total-risk (create-cave input 5)))
