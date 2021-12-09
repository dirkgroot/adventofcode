(ns adventofcode.year2021.day09
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]))

(defn parse-map [input]
  (->> (str/split-lines input)
       (map (fn [line] (map #(Integer/parseInt (str %)) line)))))

(defn parse-input [input]
  (let [height-map (parse-map input)
        nx (count (nth height-map 0))
        ny (count height-map)
        dummy-row (repeat (+ nx 2) 9)]
    {:heights (->> height-map
                   (map #(cons 9 (concat % '(9))))
                   (#(cons dummy-row (concat % [dummy-row]))))
     :nx      nx
     :ny      ny}))

(defn neighbors [x y]
  [[(dec x) y] [(inc x) y] [x (dec y)] [x (inc y)]])

(defn neighbor-heights [heights x y]
  [(nth (nth heights y) (dec x))
   (nth (nth heights y) (inc x))
   (nth (nth heights (dec y)) x)
   (nth (nth heights (inc y)) x)])

(defn is-low-point [{heights :heights} [x y]]
  (let [height (nth (nth heights y) x)
        neighbors (neighbor-heights heights x y)]
    (every? #(< height %) neighbors)))

(defn all-coords [nx ny]
  (apply concat (map (fn [y] (map (fn [x] [x y]) (range 1 (inc nx)))) (range 1 (inc ny)))))

(defn get-height [heights x y]
  (nth (nth heights y) x))

(defn part1 [input]
  (let [heights (input :heights)
        nx (input :nx)
        ny (input :ny)]
    (->> (all-coords nx ny)
         (filter #(is-low-point input %))
         (map (fn [[x y]] (get-height heights x y)))
         (map inc)
         (reduce +))))

(defn get-basin [heights [x y]]
  (lazy-seq
    (distinct
      (let [height (get-height heights x y)
            neighbors (neighbors x y)
            basin-neighbors (filter (fn [[nx ny]] (> 9 (get-height heights nx ny) height)) neighbors)]
        (cons [x y] (apply concat (map #(get-basin heights %) basin-neighbors)))))))

(defn part2 [input]
  (let [heights (input :heights)
        nx (input :nx)
        ny (input :ny)]
    (->> (all-coords nx ny)
         (filter #(is-low-point input %))
         (map #(get-basin heights %))
         (sort-by #(- (count %)))
         (take 3)
         (map #(count %))
         (reduce *))))

(def puzzle
  {:year        2021
   :day         9
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     530
   :answer2     1019494})
