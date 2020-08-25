(ns hospital.classes.class6
  (:use clojure.pprint)
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(defn simulate []
  (let [hospital {:waiting-line (ref h.model/empty-line)
                  :lab-1 (ref h.model/empty-line)
                  :lab-2 (ref h.model/empty-line)
                  :lab-3 (ref h.model/empty-line)}]
    (pprint hospital)))

(simulate)