(ns hospital.core
  (:use clojure.pprint)
  (:require [hospital.model :as h.model]))

; General waiting line

; Lab 1 (waiting line)
; Lab 2 (waiting line)
; Lab 3 (waiting line)


(let [nina-simone-hospital (h.model/new-hospital)]
  (pprint nina-simone-hospital))
