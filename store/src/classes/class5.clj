(ns classes.class5
  (:require [store.db :as s.db])
  (:require [store.logic :as s.logic]))

;; LAZY vs EAGER

(defn big-order? [user-info]
  (> (:total-financial-value user-info) 500))

;; Keep -> Map + filter (stores fn return, removes values that return nil)
(let [orders (s.db/all-orders)
      report(s.logic/report-by-user orders)]
  (println "Testing Keep" (keep big-order? report))
  (println "Testing Filter" (filter big-order? report)))

;; Execution takes about the same amount of time, because range is generated in a lazy form
(println (time (take 2 (range 1000000000))))
(println (time (take 2 (range 5))))


(println "\n\nDemonstrating map is executed in chunks\n\n")

(defn filter-fn [value]
  (println "filter-fn" value)
  value)

(defn other-filter-fn [value]
  (println "other filter-fn" value)
  value)

(println (map other-filter-fn (map filter-fn (range 10))))

(->> (range 50)
     (map filter-fn)
     (map other-filter-fn)
     println)

(println "\n\nmapv forces map to execute in an eager form, returning a vector\n\n")

(->> (range 50)
     (mapv filter-fn)
     (mapv other-filter-fn)
     println)


(println "\nlinked list does not use chunks, executing in a completely lazy form\n\n")
(->> '(0 3 6 9 12 15 18 21 24 27 30 33 36 39 42 45 48 51 54 57 60 63 66 69 72 75 78 81 84 87 90 93 96 99)
     (map filter-fn)
     (map other-filter-fn)
     println)

;; Avoid mixing Lazy and eager on executions with collateral effects (logging, saving to database) to avoid confusion