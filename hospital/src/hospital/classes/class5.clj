(ns hospital.classes.class5
  (:use clojure.pprint)
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(defn arrive-at! [hospital person]
  (swap! hospital h.logic/arrive-at :waiting-line person))

(defn transfer-patient! [hospital origin-department destination-department]
  (swap! hospital h.logic/transfers origin-department destination-department))

(defn simulate []
  (let [hospital (atom (h.model/new-hospital))]
    (arrive-at! hospital "Melissa")
    (arrive-at! hospital "Hellyan")
    (arrive-at! hospital "Maria")
    (arrive-at! hospital "Lucas")
    (transfer-patient! hospital :waiting-line :lab-1)
    (transfer-patient! hospital :waiting-line :lab-2)
    (transfer-patient! hospital :waiting-line :lab-2)
    (transfer-patient! hospital :lab-2 :lab-3)
    (pprint hospital)))

(simulate)