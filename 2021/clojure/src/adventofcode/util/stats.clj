(ns adventofcode.util.stats)

(defn mean [coll]
  (/ (reduce + coll) (count coll)))

(defn median [coll]
  (nth (sort coll) (quot (count coll) 2)))

(defn standard-deviation [coll]
  (let [mean (mean coll)]
    (Math/sqrt
      (/ (reduce +
                 (map #(Math/pow (- % mean) 2)
                      coll))
         (count coll)))))
