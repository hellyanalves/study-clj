(ns hospital.classes.class3
  (:use clojure.pprint)
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))


; Atoms and Swap

(comment "As shown in the examples from class 2 when a global variable (root binding) is defined, any thread can access
and redefine its value, causing conflits and inconsistency.")

#_(let [name "Hellyan"]
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

;; (test-atom)

(comment "Swap retries if the atom state changed since the function execution beggined.
Therefore its preferable to use pure functions (no side effects) when working with swap.")
(defn arrive-at-waiting! [hospital person]
  (swap! hospital h.logic/arrive-at-with-waiting-with-logs :waiting-line person)
  (println "inserted " person))

(defn arrive-at! [hospital person]
  (swap! hospital h.logic/arrive-at-with-logs :waiting-line person)
  (println "inserted " person))

(defn simulation-parallel-thread []
  (let [hospital (atom (h.model/new-hospital))]
    (.start (Thread. (fn [])))
    (.start (Thread. (fn [] (arrive-at! hospital "111"))))
    (.start (Thread. (fn [] (arrive-at! hospital "222"))))
    (.start (Thread. (fn [] (arrive-at! hospital "333"))))
    (.start (Thread. (fn [] (arrive-at! hospital "444"))))
    (.start (Thread. (fn [] (arrive-at! hospital "555"))))
    (.start (Thread. (fn [] (arrive-at! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 10000)
                       (pprint @hospital))))))

(defn arrive-at-locking! [hospital person]
  (swap! hospital h.logic/arrive-at-lock :waiting-line person))

(comment "Locking locks the resource (hospital) until processing is done.")
(defn simulation-parallel-thread-locking []
  (let [hospital (atom (h.model/new-hospital))]
    (.start (Thread. (fn [])))
    (.start (Thread. (fn [] (arrive-at-locking! hospital "111"))))
    (.start (Thread. (fn [] (arrive-at-locking! hospital "222"))))
    (.start (Thread. (fn [] (arrive-at-locking! hospital "333"))))
    (.start (Thread. (fn [] (arrive-at-locking! hospital "444"))))
    (.start (Thread. (fn [] (arrive-at-locking! hospital "555"))))
    ;; (.start (Thread. (fn [] (arrive-at-locking! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 10000)
                       (pprint @hospital))))))


(simulation-parallel-thread-locking)