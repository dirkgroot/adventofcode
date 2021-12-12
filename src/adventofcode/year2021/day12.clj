(ns adventofcode.year2021.day12
  (:require [clojure.pprint :refer :all]))

(defn parse-input [input]
  (->> (group-by first (partition 2 (flatten (map (fn [[_ start end]] [start end end start])
                                                  (re-seq #"(.*)-(.*)" input)))))
       (reduce (fn [acc [start connections]] (assoc acc start {:name         start
                                                               :connected-to (set (map second connections))
                                                               :visited      0
                                                               :small?       (nil? (re-matches #"[A-Z]+" start))})) {})))

(defn count-routes
  ([caves small-visits-limit] (count-routes caves small-visits-limit 0 (caves "start")))
  ([caves small-visits-limit max-small-visits {name :name connected-to :connected-to visited :visited small? :small?}]
   (cond (= name "end") 1
         (and (= name "start") (= visited 1)) 0
         (and small? (= visited 1) (= max-small-visits small-visits-limit)) 0
         (and small? (= visited small-visits-limit)) 0
         :else (let [updated-visited          (inc visited)
                     updated-max-small-visits (if small? (max max-small-visits updated-visited) max-small-visits)
                     updated-caves            (update caves name #(assoc % :visited updated-visited))]
                 (reduce + (map #(count-routes updated-caves small-visits-limit updated-max-small-visits (caves %))
                                connected-to))))))

(defn part1 [input]
  (count-routes input 1))

(defn part2 [input]
  (count-routes input 2))
