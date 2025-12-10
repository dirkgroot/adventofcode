(defproject adventofcode-2020-clojure "1.0.0-SNAPSHOT"
  :description "Advent of Code 2020 solutions in Clojure"
  :url "https://github.com/dirkgroot/adventofcode"
  :license {:name "MIT"
            :url  "https://github.com/dirkgroot/adventofcode-clojure/blob/main/LICENSE"}
  :dependencies [[org.clojure/clojure "1.12.4"]
                 [org.clojure/data.priority-map "1.2.0"]]
  :repl-options {:init-ns adventofcode-clojure.core}
  :test-selectors {:2020 [(fn [n & _]
                            (.startsWith (str n) "adventofcode.year2020"))
                          (fn [_] true)]})
