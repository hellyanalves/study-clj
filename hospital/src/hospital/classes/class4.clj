(ns hospital.classes.class4
  (:use clojure.pprint)
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(defn arrive-at! [hospital person]
  (swap! hospital h.logic/arrive-at :waiting-line person))

(defn start-arrival-thread
  ;; with partial this function signature becomes unnecessary
  ([hospital]
   (fn [person] (start-arrival-thread hospital person)))
  ([hospital person]
   (.start (Thread. (fn [] (arrive-at! hospital person))))))

(defn simulation-parallel-thread []
  "Using multiple arity"
  (let [hospital (atom (h.model/new-hospital))
        people ["111", "222", "333", "444", "555"]]
    (mapv (start-arrival-thread hospital) people)
    (.start (Thread. (fn [] (Thread/sleep 5000)
                       (pprint @hospital))))))

(defn simulation-parallel-thread-partial []
  "Using partial"
  (let [hospital (atom (h.model/new-hospital))
        people ["111", "222", "333", "444", "555"]
        partial-arrival (partial start-arrival-thread hospital)]
    (mapv partial-arrival people)
    (.start (Thread. (fn [] (Thread/sleep 5000)
                       (pprint @hospital))))))

(defn simulation-parallel-thread-doseq []
  "Using doseq, which has no return"
  (let [hospital (atom (h.model/new-hospital))
        people ["111", "222", "333", "444", "555"]]
    (doseq [person people]
      (start-arrival-thread hospital person))
    (.start (Thread. (fn [] (Thread/sleep 5000)
                       (pprint @hospital))))))

(defn simulation-parallel-thread-dotimes []
  "Using doseq, which has no return"
  (let [hospital (atom (h.model/new-hospital))]
    (dotimes [person 5]
      (start-arrival-thread hospital person))
    (.start (Thread. (fn [] (Thread/sleep 5000)
                       (pprint @hospital))))))

(simulation-parallel-thread-dotimes)