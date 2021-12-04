(ns adventofcode.support.benchmark
  (:require [adventofcode.support.stats :as stats]
            [adventofcode.support.utils :as utils]))

(defn iterations [part parsed-input randomizer]
  (repeatedly
    #(let [randomized-input (randomizer parsed-input)
           [_ time] (utils/measure-time (part randomized-input))]
       time)))

(defn benchmark-puzzle [puzzle]
  (let [{year        :year
         day         :day
         parse-input :parse-input
         randomizer  :randomize
         part1       :part1
         part2       :part2} puzzle
        [input time-io] (utils/measure-time (utils/read-input year day))
        [parsed-input time-parse] (utils/measure-time (parse-input input))
        number-of-measurements 50
        measurements-part1 (doall (take number-of-measurements (iterations part1 parsed-input randomizer)))
        measurements-part2 (doall (take number-of-measurements (iterations part2 parsed-input randomizer)))]

    (println (format "Puzzle %d-%02d" year day))
    (println (format "- I/O        : %.3f ms" time-io))
    (println (format "- Parse      : %.3f ms" time-parse))
    (println (format "- Iterations : %d" number-of-measurements))
    (println (format "- Part 1     : [min: %.3f ms | max: %.3f ms | median: %.3f ms | mean: %.3f ms | stddev: %.3f ms]"
                     (apply min measurements-part1)
                     (apply max measurements-part1)
                     (stats/median measurements-part1)
                     (stats/mean measurements-part1)
                     (stats/standard-deviation measurements-part1)))
    (println (format "- Part 2     : [min: %.3f ms | max: %.3f ms | median: %.3f ms | mean: %.3f ms | stddev: %.3f ms]"
                     (apply min measurements-part2)
                     (apply max measurements-part2)
                     (stats/median measurements-part2)
                     (stats/mean measurements-part2)
                     (stats/standard-deviation measurements-part2)))))
