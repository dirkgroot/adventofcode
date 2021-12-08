(ns adventofcode.year2021.day08
  (:require [clojure.pprint :refer :all]
            [clojure.string :as str]))

(defn parse-input [input]
  (doall
    (->> (str/split-lines input)
         (map #(str/split % #"\s+\|\s+"))
         (map (fn [[patterns-str output-str]] {:patterns (map set (str/split patterns-str #" "))
                                               :output   (map set (str/split output-str #" "))})))))

(defn count-1478 [input]
  (->> (input :output)
       (filter #(contains? #{2 4 3 7} (count %)))
       (count)))

(defn part1 [input]
  (reduce + (map #(count-1478 %) input)))

(def translation-table {"4677889" 8
                        "467789"  6
                        "467889"  0
                        "47788"   2
                        "677889"  9
                        "67789"   5
                        "6789"    4
                        "77889"   3
                        "889"     7
                        "89"      1})

(defn translate-output [{output :output map-to-frequency-strings :map-to-frequency-strings}]
  (->> output
       (map map-to-frequency-strings)
       (map translation-table)
       (reduce (fn [acc n] (+ (* acc 10) n)))))

(defn create-map-to-frequency-strings [patterns]
  (let [freqs (frequencies (reduce str patterns))
        create-frequency-string #(reduce str (sort (map freqs %)))]
    (reduce (fn [acc pattern] (assoc acc pattern (create-frequency-string pattern)))
            {}
            patterns)))

(defn part2 [input]
  (->> input
       (map #(assoc % :map-to-frequency-strings (create-map-to-frequency-strings (% :patterns))))
       (map translate-output)
       (reduce +)))

(def puzzle
  {:year        2021
   :day         8
   :parse-input parse-input
   :part1       part1
   :part2       part2
   :answer1     352
   :answer2     936117})
