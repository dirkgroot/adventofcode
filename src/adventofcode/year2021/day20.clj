(ns adventofcode.year2021.day20
  (:require [clojure.string :as str]
            [clojure.pprint :refer :all]))

(defn parse-input [input]
  (let [[algorithm input-image] (str/split input #"\n\n")
        algorithm (vec (map #(if (= % \.) 0 1) algorithm))
        image     (->> (str/split-lines input-image)
                       (map (fn [line] (vec (map #(if (= % \.) 0 1) line))))
                       (vec))]
    {:algorithm algorithm
     :flip?     (= (get algorithm 0) 1)
     :default   0
     :image     image
     :height    (count image)
     :width     (count (get image 0))}))

(defn to-int [bits]
  (reduce #(+ (* %1 2) %2) bits))

(defn get-pixel [{:keys [default image height width]} x y]
  (if (or (< x 0) (< y 0) (>= x width) (>= y height))
    default
    (get (get image y) x)))

(defn get-algorithm-index [input start-x start-y]
  (let [index (for [y (range 3) x (range 3)]
                (get-pixel input (+ start-x x) (+ start-y y)))]
    (to-int index)))

(defn enhance [{:keys [algorithm height width flip? default] :as input}]
  (assoc input
    :default (if flip? (if (zero? default) 1 0) default)
    :image (->> (vec (for [y (range height)]
                       (vec (for [x (range width)]
                              (->> (get-algorithm-index input (dec x) (dec y))
                                   (get algorithm)))))))))

(defn expand [{:keys [height width] :as input} count]
  (let [new-height (+ height (* count 2))
        new-width  (+ width (* count 2))
        new-image  (vec (for [y (range new-height)]
                          (vec (for [x (range new-width)]
                                 (get-pixel input (- x count) (- y count))))))]
    (assoc input
      :height new-height
      :width new-width
      :image new-image)))

(defn enhance-times [input count]
  (let [input (expand input (+ count 1))]
    (last (take count (iterate enhance (enhance input))))))

(defn part1 [input]
  (let [{image :image} (enhance-times input 2)]
    (->> (map #(reduce + %) image)
         (reduce +))))

(defn part2 [input]
  (let [{image :image} (enhance-times input 50)]
    (->> (map #(reduce + %) image)
         (reduce +))))
