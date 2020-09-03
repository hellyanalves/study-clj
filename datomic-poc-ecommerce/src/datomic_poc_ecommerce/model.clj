(ns datomic-poc-ecommerce.model)

(defn new-product [name slug price]
  {:product/name name
   :product/slug slug
   :product/price price})


