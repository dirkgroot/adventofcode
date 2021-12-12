(ns adventofcode.year2021.day12
  (:require [clojure.pprint :refer :all]))

(defn parse-input [input]
  (->> (re-seq #"(.*)-(.*)" input)
       (map (fn [[_ start end]] [start end end start]))
       (flatten)
       (partition 2)
       (reduce (fn [caves [start end]]
                 (if (contains? caves start)
                   (update caves start (fn [cave] (update cave :connected-to #(conj % end))))
                   (assoc caves start {:name         start
                                       :connected-to #{end}
                                       :visited      0
                                       :small?       (nil? (re-matches #"[A-Z]+" start))})))
               {})))

(defn find-foutes
  ([caves max-visits-small] (find-foutes caves max-visits-small [] (caves "start")))
  ([caves max-visits-small route {name :name connected-to :connected-to visited :visited small? :small? :as cave}]
   (cond (= name "end") [(conj route cave)]
         (and (= visited 1) (= name "start")) nil
         (and (= visited 1) small? (some #(and (% :small?) (= max-visits-small (% :visited))) route)) nil
         (and (= visited max-visits-small) small?) nil
         :else (let [updated-cave  (update cave :visited inc)
                     updated-caves (assoc caves name updated-cave)]
                 (apply concat (map #(find-foutes updated-caves max-visits-small (conj route updated-cave) (caves %))
                                    connected-to))))))

(defn part1 [input]
  (count (find-foutes input 1)))

(defn part2 [input]
  (count (find-foutes input 2)))

(def puzzle
  {:year        2021
   :day         12
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     5576
   :answer2     152837})
