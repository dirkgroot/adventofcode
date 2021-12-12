(ns adventofcode.support.test
  (:require [clojure.test :refer :all]
            [adventofcode.support.utils :as utils]))

(defn test-puzzle
  ([year day] (test-puzzle year day nil nil))
  ([year day expected-answer1] (test-puzzle year day expected-answer1 nil))
  ([year day expected-answer1 expected-answer2]
   (let [namespace   (symbol (str "adventofcode.year" year ".day" (format "%02d" day)))
         _           (require namespace)
         parse-input (ns-resolve namespace 'parse-input)
         part1       (ns-resolve namespace 'part1)
         part2       (ns-resolve namespace 'part2)
         [input time-io] (utils/measure-time (utils/read-input year day))
         [parsed-input time-parse] (utils/measure-time (parse-input input))
         [answer1 time-part1] (utils/measure-time (part1 parsed-input))
         [answer2 time-part2] (utils/measure-time (part2 parsed-input))]

     (if-not (= expected-answer1 nil) (is (= expected-answer1 answer1) "Part 1"))
     (if-not (= expected-answer2 nil) (is (= expected-answer2 answer2) "Part 2"))

     (println (format "Puzzle %d-%02d" year day)
              (format "[I/O: %.3f ms | Parse: %.3f ms | Part 1: %.3f ms | Part 2: %.3f ms]"
                      time-io time-parse time-part1 time-part2))

     (println (str "  Answer 1: " answer1))
     (println (str "  Answer 2: " answer2)))))
