(ns adventofcode.year2021.day13
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]))

(defn parse-dots [dots]
  (doall (->> (re-seq #"(\d+),(\d+)" dots)
              (map (fn [[_ x y]] [(Integer/parseInt x) (Integer/parseInt y)])))))

(defn parse-folds [folds]
  (doall (->> (re-seq #"fold along (x|y)=(\d+)" folds)
              (map (fn [[_ xy pos]] [(if (= xy "x") :horizontal :vertical) (Integer/parseInt pos)])))))

(defn parse-input [input]
  (let [[dots folds] (str/split input #"\n\n")]
    {:dots (set (parse-dots dots)) :folds (parse-folds folds)}))

(defn fold [n pos]
  (if (> n pos) (- n (* (- n pos) 2)) n))

(defn create-folds [dots [[direction pos] & fs]]
  (when direction
    (let [folded (set (case direction
                        :horizontal (map (fn [[x y]] [(fold x pos) y]) dots)
                        :vertical (map (fn [[x y]] [x (fold y pos)]) dots)))]
      (cons folded (create-folds folded fs)))))

(defn part1 [{dots :dots folds :folds}]
  (count (first (create-folds dots folds))))

(defn visualize [dots]
  (let [max-x (apply max (map first dots)) max-y (apply max (map second dots))]
    (for [y (range 0 (inc max-y))]
      (for [x (range 0 (inc max-x))]
        (if (contains? dots [x y]) "#" ".")))))

(defn part2 [{dots :dots folds :folds}]
  (let [final-dots (last (create-folds dots folds))]
    (str/join "\n" (map #(apply str %) (visualize final-dots)))))
