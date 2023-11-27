(ns adventofcode.year2021.day22)

(defn parse-input [input]
  (->> (re-seq #"(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)" input)
       (map (fn [[_ on-off x1 x2 y1 y2 z1 z2]]
              {:on? (= on-off "on")
               :x1  (Integer/parseInt x1) :x2 (inc (Integer/parseInt x2))
               :y1  (Integer/parseInt y1) :y2 (inc (Integer/parseInt y2))
               :z1  (Integer/parseInt z1) :z2 (inc (Integer/parseInt z2))}))))

(defn volume [{:keys [on? x1 x2 y1 y2 z1 z2]}]
  (* (if on? 1 -1) (- x2 x1) (- y2 y1) (- z2 z1)))

(defn get-intersection [c1 c2]
  (let [x1 (max (:x1 c1) (:x1 c2)) x2 (min (:x2 c1) (:x2 c2))
        y1 (max (:y1 c1) (:y1 c2)) y2 (min (:y2 c1) (:y2 c2))
        z1 (max (:z1 c1) (:z1 c2)) z2 (min (:z2 c1) (:z2 c2))]
    (when (and (> x2 x1) (> y2 y1) (> z2 z1))
      {:on? (not (:on? c1)) :x1 x1 :x2 x2 :y1 y1 :y2 y2 :z1 z1 :z2 z2})))

(defn find-intersections [cuboid others]
  (filter some? (map #(get-intersection % cuboid) others)))

(defn count-cubes-on [cuboids]
  (->> (reduce #(if (:on? %2) (conj (into %1 (find-intersections %2 %1)) %2)
                              (into %1 (find-intersections %2 %1)))
               [] cuboids)
       (map volume)
       (reduce +)))

(def cuboids-within-init-region
  (partial filter (fn [{:keys [x1 x2 y1 y2 z1 z2]}]
                    (and (<= -50 x1 x2 51) (<= -50 y1 y2 51) (<= -50 z1 z2 51)))))

(defn part1 [cuboids] (count-cubes-on (cuboids-within-init-region cuboids)))
(defn part2 [cuboids] (count-cubes-on cuboids))
