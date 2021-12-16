(ns adventofcode.year2021.day16-test
  (:require [clojure.test :refer :all]
            [adventofcode.year2021.day16 :as day]
            [clojure.pprint :refer :all]
            [adventofcode.support.test :as test]))

(def example-input-1 (day/parse-input "8A004A801A8002F478"))
(def example-input-2 (day/parse-input "620080001611562C8802118E34"))
(def example-input-3 (day/parse-input "C0015000016115A2E0802F182340"))
(def example-input-4 (day/parse-input "A0016C880162017C3686B18A3D4780"))

(deftest test-literal
  (is (= (day/parse-packet [1 1 0 1 0 0 1 0 1 1 1 1 1 1 1 0 0 0 1 0 1 0 0 0])
         {:version 6
          :type-id 4
          :value   2021
          :length  21})))

(deftest test-operator-0
  (is (= (day/parse-packet [0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 1 1 1 1 0 1 0 0 0 1 0 1 0 0 1 0 1 0 0 1 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0])
         {:length  49
          :type-id 6
          :value   [{:length  11
                     :type-id 4
                     :value   10
                     :version 6}
                    {:length  16
                     :type-id 4
                     :value   20
                     :version 2}]
          :version 1})))

(deftest test-operator-1
  (is (= (day/parse-packet [1 1 1 0 1 1 1 0 0 0 0 0 0 0 0 0 1 1 0 1 0 1 0 0 0 0 0 0 1 1 0 0 1 0 0 0 0 0 1 0 0 0 1 1 0 0 0 0 0 1 1 0 0 0 0 0])
         {:length  51
          :version 7
          :type-id 3
          :value   [{:length  11
                     :type-id 4
                     :value   1
                     :version 2}
                    {:length  11
                     :type-id 4
                     :value   2
                     :version 4}
                    {:length  11
                     :type-id 4
                     :value   3
                     :version 1}]})))

(deftest part1-example
  (is (= (day/part1 example-input-1) 16))
  (is (= (day/part1 example-input-2) 12))
  (is (= (day/part1 example-input-3) 23))
  (is (= (day/part1 example-input-4) 31)))

(deftest part2-example
  (is (= (day/part2 (day/parse-input "C200B40A82")) 3))
  (is (= (day/part2 (day/parse-input "04005AC33890")) 54))
  (is (= (day/part2 (day/parse-input "880086C3E88112")) 7))
  (is (= (day/part2 (day/parse-input "CE00C43D881120")) 9))
  (is (= (day/part2 (day/parse-input "D8005AC2A8F0")) 1))
  (is (= (day/part2 (day/parse-input "F600BC2D8F")) 0))
  (is (= (day/part2 (day/parse-input "9C005AC2F8F0")) 0))
  (is (= (day/part2 (day/parse-input "9C0141080250320F1802104A08")) 1))
  )

(deftest solution
  (test/test-puzzle 2021 16))
