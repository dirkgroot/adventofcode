(ns adventofcode.year2021.day12
  (:require [clojure.pprint :refer :all]))

(defn parse-input [input]
  (->> (partition 2 (flatten (map (fn [[_ start end]] [start end end start])
                                  (re-seq #"(.*)-(.*)" input))))
       (reduce (fn [caves [start end]]
                 (if (contains? caves start)
                   (update caves start (fn [cave] (update cave :connected-to #(conj % end))))
                   (assoc caves start {:name         start
                                       :connected-to #{end}
                                       :visited      0
                                       :small?       (nil? (re-matches #"[A-Z]+" start))})))
               {})))

(defn count-foutes
  ([caves small-visits-limit] (count-foutes caves small-visits-limit 0 (caves "start")))
  ([caves small-visits-limit max-small-visits {name :name connected-to :connected-to visited :visited small? :small?}]
   (cond (= name "end") 1
         (and (= name "start") (= visited 1)) 0
         (and small? (= visited 1) (= max-small-visits small-visits-limit)) 0
         (and small? (= visited small-visits-limit)) 0
         :else (let [updated-visited               (inc visited)
                     updated-max-small-cave-visits (if small? (max max-small-visits updated-visited) max-small-visits)
                     updated-caves                 (update caves name #(assoc % :visited updated-visited))]
                 (reduce + (map #(count-foutes updated-caves small-visits-limit updated-max-small-cave-visits (caves %))
                                connected-to))))))

(defn part1 [input]
  (count-foutes input 1))

(defn part2 [input]
  (count-foutes input 2))

(def puzzle
  {:year        2021
   :day         12
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     5576
   :answer2     152837})
