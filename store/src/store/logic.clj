(ns store.logic)

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

(defn report-by-user [orders]
  (->> orders
       (group-by :user)
       (map user-order-report)))

(defn orders-by-minimum-value [financial-value orders]
  (let [report (report-by-user orders)]
    (filter #(> (:total-financial-value %) financial-value) report)))

(defn user-has-orders? [user-id orders]
  (let [report (report-by-user orders)]
    (some #(= (:user %) user-id) report)))