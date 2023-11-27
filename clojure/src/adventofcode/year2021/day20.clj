(ns adventofcode.year2021.day20
  (:require [clojure.string :as str]))

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

(defn get-pixel [{:keys [default image]} x y]
  (get (get image y default) x default))

(defn get-algorithm-index [input start-x start-y]
  (->> (for [y (range 3) x (range 3)] (get-pixel input (+ start-x x) (+ start-y y)))
       (to-int)))

(defn expand [{:keys [height width] :as input}]
  (let [new-height (+ height 2)
        new-width  (+ width 2)
        new-image  (vec (for [y (range new-height)]
                          (vec (for [x (range new-width)]
                                 (get-pixel input (dec x) (dec y))))))]
    (assoc input :height new-height :width new-width :image new-image)))

(defn enhance [input]
  (let [{:keys [algorithm height width flip? default] :as input} (expand input)]
    (assoc input
      :default (if flip? (if (zero? default) 1 0) default)
      :image (->> (vec (for [y (range height)]
                         (vec (for [x (range width)]
                                (->> (get-algorithm-index input (dec x) (dec y))
                                     (get algorithm))))))))))

(defn enhance-times [input count]
  (->> (iterate enhance input) (drop count) (first)))

(defn count-lit-pixels [{image :image}]
  (->> (map #(reduce + %) image)
       (reduce +)))

(defn part1 [input] (count-lit-pixels (enhance-times input 2)))
(defn part2 [input] (count-lit-pixels (enhance-times input 50)))
