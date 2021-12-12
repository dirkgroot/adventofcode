(ns adventofcode.support.benchmark
  (:require [adventofcode.util.stats :as stats]
            [adventofcode.support.utils :as utils]))

(defn iterations [part parsed-input randomizer]
  (repeatedly
    #(let [randomized-input (randomizer parsed-input)
           [_ time] (utils/measure-time (part randomized-input))]
       time)))

(defn benchmark-puzzle [year day]
  (let [namespace              (symbol (str "adventofcode.year" year ".day" (format "%02d" day)))
        _                      (require namespace)
        parse-input            (ns-resolve namespace 'parse-input)
        randomizer             (ns-resolve namespace 'randomize)
        part1                  (ns-resolve namespace 'part1)
        part2                  (ns-resolve namespace 'part2)
        [input time-io] (utils/measure-time (utils/read-input year day))
        [parsed-input time-parse] (utils/measure-time (parse-input input))
        ; Warmup
        _                      (doall (take 10 (iterations part1 parsed-input randomizer)))
        _                      (doall (take 10 (iterations part2 parsed-input randomizer)))
        number-of-measurements 50
        measurements-part1     (doall (take number-of-measurements (iterations part1 parsed-input randomizer)))
        measurements-part2     (doall (take number-of-measurements (iterations part2 parsed-input randomizer)))]

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
