(ns adventofcode.year2021.day23
  (:require [clojure.data.priority-map :refer [priority-map]]))

(defn parse-input [input]
  (as-> (re-seq #"A|B|C|D" input) $
        (map {"A" \A "B" \B "C" \C "D" \D} $)
        (let [[a1 a2 a3 a4 a5 a6 a7 a8] $]
          [\. \. \. \. \. \. \. \. \. \. \. a1 a2 a3 a4 a5 a6 a7 a8])))

(def all-neighbors
  {0  [1]
   1  [0 2]
   2  [1 3 11]
   3  [2 4]
   4  [3 5 12]
   5  [4 6]
   6  [5 7 13]
   7  [6 8]
   8  [7 9 14]
   9  [8 10]
   10 [9]
   11 [2 15]
   12 [4 16]
   13 [6 17]
   14 [8 18]
   15 [11 19]
   16 [12 20]
   17 [13 21]
   18 [14 22]
   19 [15 23]
   20 [16 24]
   21 [17 25]
   22 [18 26]
   23 [19]
   24 [20]
   25 [21]
   26 [22]})

(def goal
  [\. \. \. \. \. \. \. \. \. \. \.
   \A \B \C \D
   \A \B \C \D
   \A \B \C \D
   \A \B \C \D])

(def energy-per-step {\A 1 \B 10 \C 100 \D 1000})

(defn free-neighbors [state pos]
  (filter #(= (get state %) \.) (all-neighbors pos)))

(defn path
  ([state from to] (path state from to []))
  ([state from to acc]
   (if (= from to)
     acc
     (let [neighbors (filter #(not-any? (fn [p] (= p %)) acc) (free-neighbors state from))]
       (when (not (empty? neighbors))
         (some identity (map #(path state % to (conj acc %)) neighbors)))))))

(defn valid-destinations [state pos]
  (let [amphipod-type-at-pos (nth state pos)
        is-in-hallway?       (<= 0 pos 10)
        home-positions       (case amphipod-type-at-pos \A [11 15 19 23] \B [12 16 20 24] \C [13 17 21 25] \D [14 18 22 26])
        home-occupation      (map state home-positions)
        is-home-empty?       (= home-occupation [\. \. \. \.])
        is-home-free?        (or (= (map first (partition-by identity home-occupation)) [\. amphipod-type-at-pos])
                                 is-home-empty?)
        is-home-full?        (= (map first (partition-by identity home-occupation)) [amphipod-type-at-pos])
        is-at-destination?   (and (some (partial = pos) home-positions)
                                  (or is-home-free? is-home-full?))]
    (->> (cond is-at-destination? []
               (and is-in-hallway? is-home-free?) [(last (filter #(= (get state %) \.) home-positions))]
               (not is-in-hallway?) [0 1 3 5 7 9 10]
               :else [])
         (map #(vec [% (path state pos %)]))
         (filter (fn [[_ path]] (not-empty path)))
         (map (fn [[dest path]] [pos dest (* (energy-per-step amphipod-type-at-pos) (count path))])))))

(defn valid-steps [state]
  (->> (range 27)
       (filter #(not= (get state %) \.))
       (reduce #(into %1 (valid-destinations state %2)) [])))

(defn apply-step [state [from to]]
  (-> (assoc state from \.)
      (assoc to (get state from))))

(def infinity (Integer/MAX_VALUE))

(defn evaluate-steps [[state energy-state] dist prev queue]
  (loop [[[_ _ v-energy :as v] & rest-v] (valid-steps state)
         dist  dist
         prev  prev
         queue queue]
    (if (nil? v)
      [dist prev queue]
      (let [alt       (+ energy-state v-energy)
            new-state (apply-step state v)]
        (if (< alt (get dist new-state infinity))
          (recur rest-v (assoc dist new-state alt) (assoc prev new-state state) (assoc queue new-state alt))
          (recur rest-v dist prev queue))))))

(defn most-efficient-solution [state]
  (loop [dist  {state 0}
         prev  {}
         queue (priority-map state 0)]
    (let [[pos energy :as min-energy] (peek queue)
          queue (pop queue)]
      (if (= pos goal)
        energy
        (let [[dist prev queue] (evaluate-steps min-energy dist prev queue)]
          (recur dist prev queue))))))

(defn part1 [state]
  (most-efficient-solution (into state [\A \B \C \D \A \B \C \D])))

(defn part2 [state]
  (most-efficient-solution (into [] (concat (take 15 state) [\D \C \B \A \D \B \A \C] (drop 15 state)))))
