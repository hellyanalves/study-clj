(ns datomic-poc-ecommerce.model)

(defn new-product
  ([name slug price]
   {:product/name            name
    :product/slug            slug
    :product/price           price})
  ([name slug price available-units]
   {:product/name            name
    :product/slug            slug
    :product/price           price
    :product/available-units available-units}))

(defn available-unit [{:keys [id expiry-date]}]
  (merge
    {:unit/id id}
    (when expiry-date {:unit/expiry-date expiry-date})))

