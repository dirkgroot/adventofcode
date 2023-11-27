(ns adventofcode.support.utils
  (:require [clojure.test :refer :all]))

(defmacro measure-time
  [expr]
  `(let [start# (. System (nanoTime))
         ret# ~expr
         time# (/ (double (- (. System (nanoTime)) start#)) 1000000.0)]
     [ret# time#]))

(defn read-input [year day]
  (slurp (format "input/%d/%02d.txt" year day)))
