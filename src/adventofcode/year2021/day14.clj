(ns adventofcode.year2021.day14
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (let [[pairs rules] (str/split input #"\n\n")]
    [(frequencies (partition 2 1 "-" pairs))
     (reduce (fn [acc [_ pair insert]] (assoc acc (vec pair) (first insert))) {} (re-seq #"(.{2}) -> (.)" rules))]))

(defn apply-rules [pairs rules]
  (reduce (fn [acc [[a b :as pair] count]]
            (let [insert (rules pair)]
              (-> acc
                  (update [a insert] #(if (nil? %) count (+ % count)))
                  (update [insert b] #(if (nil? %) count (+ % count))))))
          {} pairs))

(defn steps [pairs rules]
  (lazy-seq (let [applied (apply-rules pairs rules)]
              (cons applied (steps applied rules)))))

(defn solve [pairs rules step-count]
  (let [pair-freqs (last (take step-count (steps pairs rules)))
        char-freqs (reduce (fn [acc [[a _] count]] (update acc a #(if (nil? %) count (+ % count)))) {} pair-freqs)
        freqs      (vals char-freqs)]
    (- (apply max freqs) (apply min freqs))))

(defn part1 [[pairs rules]] (solve pairs rules 10))
(defn part2 [[pairs rules]] (solve pairs rules 40))
