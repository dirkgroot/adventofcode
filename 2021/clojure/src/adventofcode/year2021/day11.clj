(ns adventofcode.year2021.day11
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map #(str/split % #""))
       (map #(map (fn [n] (Integer/parseInt n)) %))
       (flatten)
       (vec)))

(def randomize identity)

(defn get-neighbors [index]
  (->> (case (mod index 10) 0 [-10 -9 1 10 11]
                            9 [-11 -10 -1 9 10]
                            [-11 -10 -9 -1 1 9 10 11])
       (map #(+ index %))
       (filter #(<= 0 % 99))))

(defn apply-flashes [grid flashed]
  (let [flashed-neighbor-counts (frequencies (flatten (map get-neighbors flashed)))
        update-energy           (fn [index energy] (+ energy (get flashed-neighbor-counts index 0)))]
    (->> grid
         (map-indexed #(if (<= 1 %2 9) (update-energy %1 %2) 0))
         (vec))))

(defn simulate-flashes [grid]
  (let [flashed (filter #(> (get grid %) 9) (range 0 100))]
    (if (empty? flashed) grid
                         (simulate-flashes (apply-flashes grid flashed)))))

(defn simulation [grid]
  (lazy-seq
    (let [step-result (->> (vec (map inc grid))
                           (simulate-flashes))]
      (cons step-result (simulation step-result)))))

(defn part1 [input]
  (->> (simulation input)
       (take 100)
       (map #(count (filter zero? %)))
       (reduce +)))

(defn part2 [input]
  (->> (simulation input)
       (take-while #(not-every? zero? %))
       (count)
       (inc)))
