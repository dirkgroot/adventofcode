(ns adventofcode.year2021.day22
  (:require [adventofcode.util.math :refer :all]))

(defn parse-input [input]
  (->> (re-seq #"(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)" input)
       (map-indexed (fn [index [_ on-off x1 x2 y1 y2 z1 z2]]
                      {:index index
                       :on?   (= on-off "on")
                       :x1    (Integer/parseInt x1) :x2 (inc (Integer/parseInt x2))
                       :y1    (Integer/parseInt y1) :y2 (inc (Integer/parseInt y2))
                       :z1    (Integer/parseInt z1) :z2 (inc (Integer/parseInt z2))}))))

(defn xs [cuboids] (distinct (sort (flatten (map #(map % [:x1 :x2]) cuboids)))))
(defn ys [cuboids] (distinct (sort (flatten (map #(map % [:y1 :y2]) cuboids)))))
(defn zs [cuboids] (distinct (sort (flatten (map #(map % [:z1 :z2]) cuboids)))))

(defn is-on? [cuboids y1 y2]
  (let [cuboids-at-y1-y2 (filter #(or (<= (:y1 %) y1 (dec (:y2 %)))
                                      (<= (:y1 %) (dec y2) (dec (:y2 %)))
                                      (<= y1 (:y1 %) (dec (:y2 %)) (dec y2)))
                                 cuboids)]
    (:on? (if (empty? cuboids-at-y1-y2) false (apply max-key :index cuboids-at-y1-y2)))))

(defn get-y-pairs-at-x [cuboids x min-y max-y]
  (let [cuboids-at-x (filter #(<= (:x1 %) x (dec (:x2 %))) cuboids)]
    (->> (filter #(<= min-y % max-y) (ys cuboids-at-x))
         (partition 2 1)
         (map (fn [[y1 y2]] {:on? (is-on? cuboids-at-x y1 y2) :y1 y1 :y2 y2})))))

(defn get-squares-at-z [cuboids z min-x max-x min-y max-y]
  (let [cuboids-at-z (filter #(and (<= (:z1 %) z (dec (:z2 %)))) cuboids)]
    (->> (partition 2 1 (filter #(<= min-x % max-x) (xs cuboids-at-z)))
         (map (fn [[x1 x2]] (map #(assoc % :x1 x1 :x2 x2) (get-y-pairs-at-x cuboids-at-z x1 min-y max-y))))
         (flatten))))

(defn get-cuboids-within-bounds [cuboids min-x max-x min-y max-y min-z max-z]
  (let [zs (filter #(<= min-z % max-z) (zs cuboids))]
    (flatten (for [[z1 z2] (partition 2 1 (sort (distinct zs)))]
               (map #(assoc % :z1 z1 :z2 z2) (get-squares-at-z cuboids z1 min-x max-x min-y max-y))))))

(defn count-cubes-on [cuboids min-x max-x min-y max-y min-z max-z]
  (let [new-cuboids (get-cuboids-within-bounds cuboids min-x max-x min-y max-y min-z max-z)]
    (->> (filter :on? new-cuboids)
         (map (fn [{:keys [x1 x2 y1 y2 z1 z2]}]
                (* (abs (- (min max-x x2) (max min-x x1)))
                   (abs (- (min max-y y2) (max min-y y1)))
                   (abs (- (min max-z z2) (max min-z z1))))))
         (reduce +))))

(defn part1 [cuboids]
  (count-cubes-on cuboids -50 51 -50 51 -50 51))

(defn part2 [cuboids]
  (let [xs (flatten (map #(map % [:x1 :x2]) cuboids))
        ys (flatten (map #(map % [:y1 :y2]) cuboids))
        zs (flatten (map #(map % [:z1 :z2]) cuboids))]
    (count-cubes-on cuboids (apply min xs) (apply max xs) (apply min ys) (apply max ys) (apply min zs) (apply max zs))))
