(ns classes.class3
  (:require [store.db :as s.db])
  #_(:require [clojure.pprint :as pprint]))


#_(pprint/pprint (group-by :user (s.db/all-orders)))

(defn my-group-fn
  [element]
  (println "Element" element)
  (:user element))


#_(println (group-by my-group-fn (s.db/all-orders)))

(defn item-total
  [[_ details]]
  (* (get details :quantity 0) (get details :price 0)))

(defn order-total
  [order]
  (->> order
     (map item-total)
     (reduce +)))

(defn total-financial-value
  [orders]
  (->> orders
       (map :items)
       (map order-total)
       (reduce +)))

(defn user-order-report
  [[user orders]]
  {:user user
   :order-count (count orders)
   :total-financial-value (total-financial-value orders)})

(->> (s.db/all-orders)
     (group-by :user)
     (map user-order-report)
     println)