(ns hospital.model)

(def empty-line
  clojure.lang.PersistentQueue/EMPTY)

(defn new-hospital []
    {:waiting-line empty-line
     :lab-1 empty-line
     :lab-2 empty-line
     :lab3 empty-line})


