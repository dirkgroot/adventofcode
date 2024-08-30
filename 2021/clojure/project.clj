(defproject adventofcode-2021-clojure "1.0.0-SNAPSHOT"
  :description "Advent of Code 2021 solutions in Clojure"
  :url "https://github.com/dirkgroot/adventofcode"
  :license {:name "MIT"
            :url  "https://github.com/dirkgroot/adventofcode/blob/main/LICENSE"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.priority-map "1.1.0"]]
  :repl-options {:init-ns adventofcode-clojure.core}
  :test-selectors {:2021 [(fn [n & _]
                            (.startsWith (str n) "adventofcode.year2021"))
                          (fn [_] true)]})
