(ns classes.class4
  (:require [store.db :as s.db])
  (:require [store.logic :as s.logic]))


(let [orders (s.db/all-orders)
      report(s.logic/report-by-user orders)]
  (println "Report " report
   (println "Sorted " (sort-by :total-financial-value report)))
  (println "Reverse sort " (->> report
                               (sort-by :total-financial-value)
                               reverse)))

(defn report-by-user-sorted [orders]
  (->> orders
       s.logic/report-by-user
       (sort-by :total-financial-value)
       reverse))

(let [orders (s.db/all-orders)
      report (report-by-user-sorted orders)]
  (println "\n\n**Report**\n\n")
  (clojure.pprint/pprint report)
  (print "\nTotal Orders ")
  (clojure.pprint/pprint (count report))
  ;; get nth order
  #_(println "nth 1" (nth report 1))
  ;; get fn will return nil because this seq is not a vector
  #_(println "get 1" (get report 1))
  ;; take two first elements
  #_(println "Take 2" (take 2 report)))

(defn orders-by-minimum-value [financial-value]
  (let [orders (s.db/all-orders)
        report (report-by-user-sorted orders)]
    (filter #(> (:total-financial-value %) financial-value) report)))

(defn user-has-orders-filter? [user-id]
  "Using filter"
  (let [orders (s.db/all-orders)
        report (report-by-user-sorted orders)]
    (-> (filter #(= (:user %) user-id) report)
        nil?
        not)))

(defn user-has-orders? [user-id]
  "Using some"
  (let [orders (s.db/all-orders)
        report (report-by-user-sorted orders)]
    (some #(= (:user %) user-id) report)))


(println "Users that spent more than 500" (orders-by-minimum-value 500))
(println "User 15 has made any orders?" (user-has-orders? 15))
(println "User 15 has made any orders?" (user-has-orders-filter? 15))
