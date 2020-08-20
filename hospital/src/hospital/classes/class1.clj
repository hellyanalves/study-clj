(ns hospital.classes.class1
  (:use [clojure.pprint])
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(comment "In this example, in order to update the waiting line we have to update the global symbol
every time a new patient arrives, which is a very bad practice.")
(defn simulation []
  ; root binding
  (def hospital (h.model/new-hospital))
  (def hospital (h.logic/arrive-at hospital :waiting-line "111"))
  (def hospital (h.logic/arrive-at hospital :waiting-line "222"))
  (def hospital  (h.logic/arrive-at hospital :waiting-line "333"))
  (def hospital  (h.logic/arrive-at hospital :waiting-line "666"))
  (def hospital  (h.logic/arrive-at hospital :waiting-line "777"))
  ;; here the code throws an exception, because there are 5 patients already
  #_(def hospital  (h.logic/arrive-at hospital :waiting-line "888"))
  (pprint hospital)
  (def hospital  (h.logic/arrive-at hospital :lab-1 "444"))
  (def hospital  (h.logic/arrive-at hospital :lab-3 "555"))

  (def hospital (pprint (h.logic/assess-patient hospital :lab-1)))
  (def hospital (pprint (h.logic/assess-patient hospital :waiting-line))))

;(simulation)

(comment "Demosntrating the problem of using global variables on multithreading")

(defn wrong-arrive-at [person]
  (def hospital (h.logic/arrive-at-with-waiting hospital :waiting-line person))
  (println "inserted " person))

(defn simulation-parallel-thread []
  (def hospital (h.model/new-hospital))
  (.start (Thread. (fn [])))
  (.start (Thread. (fn [] (wrong-arrive-at "111"))))
  (.start (Thread. (fn [] (wrong-arrive-at "222"))))
  (.start (Thread. (fn [] (wrong-arrive-at "333"))))
  (.start (Thread. (fn [] (wrong-arrive-at "444"))))
  (.start (Thread. (fn [] (wrong-arrive-at "555"))))
  (.start (Thread. (fn [] (wrong-arrive-at "666"))))
  (.start (Thread. (fn [] (wrong-arrive-at "777"))))
  (.start (Thread. (fn [] (wrong-arrive-at "888"))))
  (.start (Thread. (fn [] (wrong-arrive-at "999"))))
  (.start (Thread. (fn [] (Thread/sleep 4000)
                     (pprint hospital)))))

(simulation-parallel-thread)