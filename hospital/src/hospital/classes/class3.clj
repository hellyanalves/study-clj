(ns hospital.classes.class3
  (:use clojure.pprint)
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))


; Atoms and Swap

(comment "As shown in the examples from class 2 when a global variable (root binding) is defined, any thread can access
and redefine its value, causing conflits and inconsistency.")

(let [name "Hellyan"]
  ; prints Hellyan
  (pprint name)
  (let [name "Lucas"]
    ; prints Lucas - Shadowing
    (pprint name)))


(defn test-atom []
  "Creating atom"
  (let [hospital-oliveira (atom { :waiting-line h.model/empty-line})]
    (println hospital-oliveira)
    (pprint hospital-oliveira)
    ; deref retrieves atom value
    (pprint (deref hospital-oliveira))
    (pprint (assoc @hospital-oliveira :lab-1 h.model/empty-line))
    ; to alter the atom value specific functions such as swap!l must be used
    (pprint @hospital-oliveira)
    (swap! hospital-oliveira assoc :lab-1 h.model/empty-line)
    (swap! hospital-oliveira assoc :lab-2 h.model/empty-line)
    (swap! hospital-oliveira update :lab-1 conj "111")
    (pprint @hospital-oliveira)))

(test-atom)