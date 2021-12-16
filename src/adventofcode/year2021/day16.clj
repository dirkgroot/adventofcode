(ns adventofcode.year2021.day16)

(def hex-to-bin {\0 [0 0 0 0] \4 [0 1 0 0] \8 [1 0 0 0] \C [1 1 0 0]
                 \1 [0 0 0 1] \5 [0 1 0 1] \9 [1 0 0 1] \D [1 1 0 1]
                 \2 [0 0 1 0] \6 [0 1 1 0] \A [1 0 1 0] \E [1 1 1 0]
                 \3 [0 0 1 1] \7 [0 1 1 1] \B [1 0 1 1] \F [1 1 1 1]})

(defn parse-input [input]
  (->> (map hex-to-bin input)
       (flatten)))

(declare parse-packet)

(defn parse-int [bits]
  (reduce #(+ (* %1 2) %2) 0 bits))

(defn parse-literal-value [bits]
  (loop [acc    0
         length 0
         [b1 b2 b3 b4 b5 & rest] bits]
    (let [new-acc (+ (bit-shift-left acc 4) (parse-int [b2 b3 b4 b5]))]
      (if (zero? b1)
        [new-acc (+ length 5)]
        (recur new-acc (+ length 5) rest)))))

(defn parse-subpackets-type0 [bit-count bits]
  (loop [acc           []
         bits-to-parse bits
         bits-parsed   0]
    (if (>= bits-parsed bit-count)
      [acc bits-parsed]
      (let [{length :length :as packet} (parse-packet bits-to-parse)]
        (recur (conj acc packet) (drop length bits-to-parse) (+ bits-parsed length))))))

(defn parse-subpackets-type1 [packet-count bits]
  (loop [acc            []
         bits-to-parse  bits
         bits-parsed    0
         packets-parsed 0]
    (if (>= packets-parsed packet-count)
      [acc bits-parsed]
      (let [{length :length :as packet} (parse-packet bits-to-parse)]
        (recur (conj acc packet) (drop length bits-to-parse) (+ bits-parsed length) (inc packets-parsed))))))

(defn parse-operator-packet [bits]
  (let [[length-type-id & rest] bits
        [value length] (case length-type-id
                         0 (parse-subpackets-type0 (parse-int (take 15 rest)) (drop 15 rest))
                         1 (parse-subpackets-type1 (parse-int (take 11 rest)) (drop 11 rest)))]
    [value (+ length 1 (if (= length-type-id 0) 15 11))]))

(defn parse-packet [bits]
  (let [[v1 v2 v3 t1 t2 t3 & rest] bits
        version (parse-int [v1 v2 v3])
        type-id (parse-int [t1 t2 t3])
        [value length] (case type-id
                         4 (parse-literal-value rest)
                         (parse-operator-packet rest))]
    {:version version
     :type-id type-id
     :value   value
     :length  (+ length 6)}))

(defn add-versions [{version :version type-id :type-id value :value}]
  (if (= type-id 4)
    version
    (+ version (->> (map #(add-versions %) value)
                    (reduce +)))))

(defn part1 [input]
  (add-versions (parse-packet input)))

(defn calculate-value [{type-id :type-id value :value}]
  (case type-id
    0 (reduce + (map calculate-value value))
    1 (reduce * (map calculate-value value))
    2 (apply min (map calculate-value value))
    3 (apply max (map calculate-value value))
    4 value
    5 (if (> (calculate-value (first value)) (calculate-value (second value))) 1 0)
    6 (if (< (calculate-value (first value)) (calculate-value (second value))) 1 0)
    7 (if (= (calculate-value (first value)) (calculate-value (second value))) 1 0)))

(defn part2 [input]
  (calculate-value (parse-packet input)))
