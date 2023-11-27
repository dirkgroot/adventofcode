(ns adventofcode.year2021.day18
  (:require [clojure.string :as str]))

(defn parse [input]
  (let [tokens (re-seq #"\[|\]|\d+" input)]
    (loop [[n & ns] tokens
           depth  0
           result []]
      (if (nil? n)
        result
        (case n
          "[" (recur ns (inc depth) (conj result {:depth (inc depth) :leaf? false}))
          "]" (recur ns (dec depth) result)
          (recur ns depth (conj result {:depth depth :leaf? true :value (Integer/parseInt n)})))))))

(defn parse-input [input]
  (doall (map parse (str/split-lines input))))

(defn should-explode? [triple]
  (let [a      (first triple)
        depths (map :depth triple)
        leafs  (map :leaf? triple)]
    (and (apply = depths) (> (:depth a) 4) (= leafs [false true true]))))

(defn explode [number]
  (loop [[[a b c :as triple] & ls] (partition 3 1 (conj number nil nil))
         skip               0
         index              0
         index-of-last-leaf nil
         exploded-right     0
         has-exploded?      false
         result             []]
    (if (nil? triple)
      (apply conj result (filter identity (drop skip triple)))
      (let [index-of-last-leaf (if (:leaf? a) index index-of-last-leaf)
            index              (inc index)
            explode?           (and (not has-exploded?) (should-explode? triple))
            result             (if (and explode? index-of-last-leaf)
                                 (assoc result index-of-last-leaf (update (get result index-of-last-leaf) :value + (:value b)))
                                 result)]
        (cond
          (> skip 0) (recur ls (dec skip) index index-of-last-leaf exploded-right has-exploded? result)
          explode? (recur ls 2 index index-of-last-leaf (:value c) true (conj result (assoc a :depth (dec (:depth a)) :leaf? true :value 0)))
          (:leaf? a) (recur ls 0 index index-of-last-leaf 0 has-exploded? (conj result (update a :value + exploded-right)))
          :else (recur ls 0 index index-of-last-leaf exploded-right has-exploded? (conj result a)))))))

(defn split [number]
  (loop [[{depth :depth leaf? :leaf? value :value :as n} & ns] number
         has-split? false
         result     []]
    (cond (nil? n) result
          (and (not has-split?) leaf? (> value 9)) (recur ns true
                                                          (apply conj result [{:depth (inc depth) :leaf? false}
                                                                              {:depth (inc depth) :leaf? true :value (quot value 2)}
                                                                              {:depth (inc depth) :leaf? true :value (- value (quot value 2))}]))
          :else (recur ns has-split? (conj result n)))))

(defn reduce-number [number]
  (loop [number number]
    (let [must-explode? (some #(> (:depth %) 4) number)
          must-split?   (and (not must-explode?) (some #(> (get :value % 0)) number))
          result        (cond must-explode? (explode number)
                              must-split? (split number)
                              :else number)]
      (if (= result number)
        number
        (recur result)))))

(defn add [n1 n2]
  (let [value (concat [{:depth 1 :leaf? false}]
                      (map #(update % :depth inc) n1)
                      (map #(update % :depth inc) n2))]
    (reduce-number (vec value))))

(defn magnitude [number]
  (loop [[{leaf? :leaf? value :value :as n} & ns] (reverse number)
         stack nil]
    (cond (nil? n) (peek stack)
          leaf? (recur ns (conj stack value))
          :else (let [left      (peek stack)
                      right     (peek (pop stack))
                      magnitude (+ (* left 3) (* right 2))
                      stack     (pop (pop stack))]
                  (recur ns (conj stack magnitude))))))

(defn part1 [input]
  (magnitude (reduce add input)))

(defn part2 [input]
  (let [numbers (for [n1 input n2 input :when (not= n1 n2)]
                  (magnitude (add n1 n2)))]
    (apply max numbers)))
