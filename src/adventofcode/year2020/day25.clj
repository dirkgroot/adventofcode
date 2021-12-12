(ns adventofcode.year2020.day25
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map (fn [str] (Integer/parseInt str)) (str/split input #"\n")))

(def subjectNumber 7)
(defn solve [cardPublicKey doorPublicKey]
  (loop [i 1
         k 1]
    (if (= i cardPublicKey)
      k
      (recur (mod (* i subjectNumber) 20201227) (mod (* k doorPublicKey) 20201227)))))

(defn part1 [[cardPublicKey doorPublicKey]] (solve cardPublicKey doorPublicKey))
(defn part2 [_] "Merry Christmas!")
