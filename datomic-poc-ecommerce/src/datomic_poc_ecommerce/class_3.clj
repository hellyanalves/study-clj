(ns datomic-poc-ecommerce.class-3
  (:require [datomic-poc-ecommerce.database :as ecommerce.db]
          [datomic-poc-ecommerce.model :as ecommerce.model]
          [datomic.api :as d])
  (:use clojure.pprint))

(def ^:private db-uri "datomic:dev://localhost:4334/query-test")
(ecommerce.db/delete-database db-uri)
(def ^:private conn (ecommerce.db/open-connection db-uri))
(ecommerce.db/create-schema conn)

(let [computer
      (ecommerce.model/new-product "new computer", "/new_computer", (bigdec 3500.00))
      phone
      (ecommerce.model/new-product "samsung galaxy S10", "/sms_galaxy_s10", 3000.00M)
      phone-2
      (ecommerce.model/new-product "samsung J1" "/samsung-j1" 850.00M)
      calculator
      {:product/name "Scientific calculator"}]
  (ecommerce.db/create-schema conn)
  (ecommerce.db/insert-to-database conn [computer, phone, phone-2, calculator]))

(comment "Datomic ensures atomicity, which means if one of the products had any invalid values, none of
the four would have been transactioned. https://docs.datomic.com/on-prem/acid.html")

(pprint (ecommerce.db/all-products (d/db conn)))
(pprint (ecommerce.db/products-by-slug (d/db conn) "/new_computer"))

(pprint (ecommerce.db/all-products-slugs (d/db conn)))
(pprint (ecommerce.db/all-products-prices (d/db conn)))
(pprint (ecommerce.db/all-products-prices-keys (d/db conn)))
(pprint (ecommerce.db/all-products (d/db conn)))
(pprint (ecommerce.db/all-products-all-fields (d/db conn)))

