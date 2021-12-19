(ns adventofcode.year2021.day18-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day18 :as day]
            [adventofcode.support.test :as test]
            [clojure.pprint :refer :all]))

(def example-input
  (day/parse-input "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"))

; [[[[[9 8] 1] 2] 3] 4]
(def tree-left [{:depth 1 :leaf? false}
                {:depth 2 :leaf? false}
                {:depth 3 :leaf? false}
                {:depth 4 :leaf? false}
                {:depth 5 :leaf? false}
                {:depth 5 :leaf? true :value 9}
                {:depth 5 :leaf? true :value 8}
                {:depth 4 :leaf? true :value 1}
                {:depth 3 :leaf? true :value 2}
                {:depth 2 :leaf? true :value 3}
                {:depth 1 :leaf? true :value 4}])

; [7,[6,[5,[4,[3,2]]]]]
(def tree-right [{:depth 1 :leaf? false}
                 {:depth 1 :leaf? true :value 7}
                 {:depth 2 :leaf? false}
                 {:depth 2 :leaf? true :value 6}
                 {:depth 3 :leaf? false}
                 {:depth 3 :leaf? true :value 5}
                 {:depth 4 :leaf? false}
                 {:depth 4 :leaf? true :value 4}
                 {:depth 5 :leaf? false}
                 {:depth 5 :leaf? true :value 3}
                 {:depth 5 :leaf? true :value 2}])

; [[6,[5,[4,[3,2]]]],1]
(def tree-middle [{:depth 1 :leaf? false}
                  {:depth 2 :leaf? false}
                  {:depth 2 :leaf? true :value 6}
                  {:depth 3 :leaf? false}
                  {:depth 3 :leaf? true :value 5}
                  {:depth 4 :leaf? false}
                  {:depth 4 :leaf? true :value 4}
                  {:depth 5 :leaf? false}
                  {:depth 5 :leaf? true :value 3}
                  {:depth 5 :leaf? true :value 2}
                  {:depth 1 :leaf? true :value 1}])

(defn to-vector [number]
  (loop [[n & ns] (reverse number)
         stack nil]
    (cond (nil? n) (peek stack)
          (:leaf? n) (recur ns (conj stack (:value n)))
          :else (let [left  (peek stack)
                      right (peek (pop stack))
                      stack (pop (pop stack))]
                  (recur ns (conj stack [left right]))))))

(defn vector-to-string [[left right]]
  (str "["
       (if (number? left) left (vector-to-string left))
       ","
       (if (number? right) right (vector-to-string right))
       "]"))

(defn to-string [number]
  (let [as-vector (to-vector number)]
    (vector-to-string as-vector)))

(deftest parsing
  (is (= (day/parse "[[[[[9,8],1],2],3],4]") tree-left))
  (is (= (day/parse "[7,[6,[5,[4,[3,2]]]]]") tree-right))
  (is (= (day/parse "[[6,[5,[4,[3,2]]]],1]") tree-middle)))

(deftest explode
  (is (= (day/explode (day/parse "[[[[[9,8],1],2],3],4]")) (day/parse "[[[[0,9],2],3],4]")))
  (is (= (day/explode (day/parse "[7,[6,[5,[4,[3,2]]]]]")) (day/parse "[7,[6,[5,[7,0]]]]")))
  (is (= (day/explode (day/parse "[[6,[5,[4,[3,2]]]],1]")) (day/parse "[[6,[5,[7,0]]],3]")))
  (is (= (day/explode (day/parse "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")) (day/parse "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")))
  (is (= (day/explode (day/parse "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")) (day/parse "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")))
  (is (= (day/explode (day/parse "[[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]],[[[5,[2,8]],4],[5,[[9,9],0]]]]"))
         (day/parse "[[[[5,0],[[9,7],[9,6]]],[[4,[1,2]],[[1,4],2]]],[[[5,[2,8]],4],[5,[[9,9],0]]]]"))))

(deftest split
  (is (= (day/split (day/parse "[[[[0,7],4],[15,[0,13]]],[1,1]]")) (day/parse "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")))
  (is (= (day/split (day/parse "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")) (day/parse "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"))))

(deftest add
  (is (= (day/add (day/parse "[1,2]") (day/parse "[3,4]")) (day/parse "[[1,2],[3,4]]")))
  (is (= (type (day/add (day/parse "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]") (day/parse "[[[5,[2,8]],4],[5,[[9,9],0]]]")))
         (type (day/parse "[[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]],[[[5,[2,8]],4],[5,[[9,9],0]]]]")))))

(deftest magnitude
  (is (= (day/magnitude (day/parse "[9,1]")) 29))
  (is (= (day/magnitude (day/parse "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")) 3488)))

(deftest reduction
  (is (= (day/reduce-number (day/parse "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")) (day/parse "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"))))

(deftest to-string-test
  (is (= (to-string (day/parse "[1,2]")) "[1,2]"))
  (is (= (to-string (day/parse "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")) "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")))

(deftest part1-example
  (is (= (day/part1 example-input) 4140)))

(deftest part2-example
  (is (= (day/part2 example-input) 3993)))

(deftest solution
  (test/test-puzzle 2021 18))
