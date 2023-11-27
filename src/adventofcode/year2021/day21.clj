(ns adventofcode.year2021.day21)

(defn parse-input [input]
  (let [[_ one two] (re-matches #"Player 1 starting position: (\d+)\nPlayer 2 starting position: (\d+)\n?" input)]
    [(dec (Integer/parseInt one)) (dec (Integer/parseInt two))]))

(defn deterministic-die [previous] (if (= previous 100) 1 (inc previous)))

(defn turn [{:keys [pos score rolls]} die-roll]
  (let [pos (mod (+ pos die-roll) 10)]
    {:pos   pos
     :score (+ score pos 1)
     :rolls (+ rolls 6)}))

(defn play [start-one start-two]
  (let [die             (partition 2 (map #(apply + %) (partition 3 (iterate deterministic-die 1))))
        turns-one       (reductions (fn [state [roll _]] (turn state roll)) {:pos start-one :score 0 :rolls -3} die)
        turns-two       (reductions (fn [state [_ roll]] (turn state roll)) {:pos start-two :score 0 :rolls 0} die)
        turns           (interleave turns-one turns-two)
        turn-before-win (last (take-while #(< (% :score) 1000) turns))]
    (* (turn-before-win :score) (+ (turn-before-win :rolls) 3))))

(defn part1 [[start-one start-two]]
  (play start-one start-two))

(def roll-multipliers {3 1, 4 3, 5 6, 6 7, 7 6, 8 3, 9 1})
(def possible-rolls [3 4 5 6 7 8 9])

(defn simulate
  ([state] (reduce #(-> (update %1 :one + (:one %2))
                        (update :two + (:two %2)))
                   (map #(simulate state 1 %) possible-rolls)))
  ([{{pos :pos score :score player :player :as turn} :turn other :other} worlds roll]
   (let [updated-pos    (mod (+ pos roll) 10)
         updated-score  (+ score updated-pos 1)
         updated-worlds (*' worlds (roll-multipliers roll))
         updated-state  {:turn other :other (assoc turn :pos updated-pos :score updated-score)}]
     (if (>= updated-score 21)
       {player          updated-worlds
        (:player other) 0}
       (reduce #(-> (update %1 :one + (:one %2))
                    (update :two + (:two %2)))
               (map #(simulate updated-state updated-worlds %) possible-rolls))))))

(defn part2 [[start-one start-two]]
  (let [{one :one two :two} (simulate
                              {:turn  {:pos start-one :score 0 :player :one}
                               :other {:pos start-two :score 0 :player :two}})]
    (max one two)))
