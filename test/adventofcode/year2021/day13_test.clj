(ns adventofcode.year2021.day13-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day13 :as day]
            [adventofcode.support.test :as test]
            [clojure.string :as str]))

(def example-input
  (day/parse-input "6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5"))

(deftest part1-example
  (is (= (day/part1 example-input) 17)))

(deftest part2-example
  (is (= (day/part2 example-input)
         (str/join "\n" ["#####"
                         "#...#"
                         "#...#"
                         "#...#"
                         "#####"]))))

(def part2-solution
  (str/join "\n" ["..##.###..####..##..#..#..##..#..#.###."
                  "...#.#..#....#.#..#.#..#.#..#.#..#.#..#"
                  "...#.#..#...#..#....#..#.#..#.#..#.#..#"
                  "...#.###...#...#....#..#.####.#..#.###."
                  "#..#.#....#....#..#.#..#.#..#.#..#.#.#."
                  ".##..#....####..##...##..#..#..##..#..#"]))

(deftest solution
  (test/test-puzzle 2021 13 655 part2-solution))
