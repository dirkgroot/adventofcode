(ns adventofcode.support.test
  (:require [clojure.test :refer :all]
            [adventofcode.support.utils :as utils]
            ))

(defn test-puzzle
  ([puzzle]
   (let [{year             :year
          day              :day
          parse-input      :parse-input
          part1            :part1
          expected-answer1 :answer1
          part2            :part2
          expected-answer2 :answer2} puzzle
         [input time-io] (utils/measure-time (utils/read-input year day))
         [parsed-input time-parse] (utils/measure-time (parse-input input))
         [answer1 time-part1] (utils/measure-time (part1 parsed-input))
         [answer2 time-part2] (utils/measure-time (part2 parsed-input))]

     (if-not (= expected-answer1 nil) (is (= expected-answer1 answer1)))
     (if-not (= expected-answer2 nil) (is (= expected-answer2 answer2)))

     (println (format "Puzzle %d-%02d" year day)
              (format "[I/O: %.3f ms | Parse: %.3f ms | Part 1: %.3f ms | Part 2: %.3f ms]"
                      time-io time-parse time-part1 time-part2))

     (println (str "  Answer 1: " answer1))
     (println (str "  Answer 2: " answer2)))))
