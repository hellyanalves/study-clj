(ns hospital.classes.class6
  (:use clojure.pprint)
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(defn patient-fits-queue? [department]
  (< (count department) 5))


(defn arrive-at [department person]
  (if (patient-fits-queue? department)
    (conj department person)
    (throw (ex-info "Queue already full" {:trying-to-add person}))))

(defn arrive-at! [hospital person]
  "Resets ref value.
  Retries if ref value is altered during transaction"
  (let [line (get hospital :waiting-line)]
    (ref-set line (arrive-at @line person))))

(defn arrive-at! [hospital person]
  "Alter works like swap for refs. Retries if ref value is altered
  during transaction"
  (let [line (get hospital :waiting-line)]
    (alter line arrive-at person)))

(defn simulate []
  (let [hospital {:waiting-line (ref h.model/empty-line)
                  :lab-1 (ref h.model/empty-line)
                  :lab-2 (ref h.model/empty-line)
                  :lab-3 (ref h.model/empty-line)}]
    (dosync
      (arrive-at! hospital "Hellyan")
      (arrive-at! hospital "Melissa")
      (arrive-at! hospital "Edna")
      (arrive-at! hospital "Maria")
      (arrive-at! hospital "Helio"))
    (pprint hospital)))

;; check ensure: https://clojuredocs.org/clojure.core/ensure

(defn async-arrive-at! [hospital person]
  (future
    (Thread/sleep (rand 5000))
    (dosync
      (println "Trying sync to " person)
      (arrive-at! hospital person))))

(defn simulate-async []
  (let [hospital {:waiting-line (ref h.model/empty-line)
                  :lab-1        (ref h.model/empty-line)
                  :lab-2        (ref h.model/empty-line)
                  :lab-3        (ref h.model/empty-line)}]
    (def futures (mapv
                   #(async-arrive-at! hospital %)
                   (range 10)))
    (future
      (dotimes [_ 4]
        (Thread/sleep 2000)
        (pprint hospital)
        (pprint futures)))))

(simulate-async)
