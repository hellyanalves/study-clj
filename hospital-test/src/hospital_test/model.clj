(ns hospital-test.model
  (:require [schema.core :as s])
  (:import (clojure.lang PersistentQueue)))

(def empty-line
  PersistentQueue/EMPTY)

(s/defn new-hospital []
  {:waiting-line empty-line
   :lab-1 empty-line
   :lab-2 empty-line
   :lab-3 empty-line})

(s/def PatientId s/Str)
(s/def Department (s/queue PatientId))
(s/def Hospital {s/Keyword Department})


;; (s/validate PatientId "Hellyan")
;; (s/validate PatientId 24) ;; invalid
;; (s/validate Department (conj empty-line "Hellyan" "Lucas"))
;; (s/validate Hospital {:waiting-line ["Hellyan" "Lucas"]})