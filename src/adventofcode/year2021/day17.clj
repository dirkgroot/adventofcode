(ns adventofcode.year2021.day17)

(defn parse-input [input]
  (let [[_ min-x max-x min-y max-y] (re-matches #"target area: x=(\d+)..(\d+), y=(-?\d+)..(-?\d+)\n?" input)]
    [(Integer/parseInt min-x) (Integer/parseInt max-x) (Integer/parseInt min-y) (Integer/parseInt max-y)]))

(defn xy-velocities [min-x max-x min-y max-y]
  (for [vx (range 1 (inc max-x)) vy (range min-y (- min-y))]
    (loop [x 0 y 0 highest-y 0 vx vx vy vy]
      (cond (and (<= min-x x max-x) (<= min-y y max-y)) {:hit true :highest-y highest-y}
            (or (> x max-x) (< y min-y)) {:hit false}
            :else (recur (+ x vx) (+ y vy) (max y highest-y) (max (dec vx) 0) (dec vy))))))

(defn part1 [[min-x max-x min-y max-y]]
  (let [hits (filter :hit (xy-velocities min-x max-x min-y max-y))]
    (:highest-y (apply max-key :highest-y hits))))

(defn part2 [[min-x max-x min-y max-y]]
  (count (filter :hit (xy-velocities min-x max-x min-y max-y))))
