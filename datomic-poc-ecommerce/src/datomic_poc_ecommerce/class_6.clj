(ns datomic-poc-ecommerce.class-6
  (:require [datomic-poc-ecommerce.database :as ecommerce.db]
          [datomic-poc-ecommerce.model :as ecommerce.model]
          [datomic.api :as d])
  (:use clojure.pprint))

(def ^:private db-uri "datomic:dev://localhost:4334/query-test")
;;(ecommerce.db/delete-database db-uri)
(def ^:private conn (ecommerce.db/open-connection db-uri))

(ecommerce.db/create-schema conn)
(let [computer
      (ecommerce.model/new-product "new computer", "/new_computer", (bigdec 3500.00))
      phone
      (ecommerce.model/new-product "samsung galaxy S20", "/sms_galaxy_s20", 8000.00M)
      phone-2
      (ecommerce.model/new-product "samsung J1" "/samsung-j1" 850.00M)
      calculator
      {:product/name "Scientific calculator"}]
  (ecommerce.db/create-schema conn)
  (pprint @(d/transact conn [computer, phone, phone-2, calculator])))

;; Ordenar as restrições do where da mais restritiva para a menos restritiva,
;; para otimizar as queries.

(pprint (count (ecommerce.db/all-products-prices (d/db conn) 1000)))
(pprint (count (ecommerce.db/all-products-prices (d/db conn) 5000)))

(d/transact conn [[:db/add 17592186045419 :product/keyword "desktop"]
                  [:db/add 17592186045419 :product/keyword "computer"]])

(pprint (ecommerce.db/all-products (d/db conn)))

(d/transact conn [[:db/retract 17592186045419 :product/keyword "computer"]])

(d/transact conn [[:db/add 17592186045419 :product/keyword "black-white-monitor"]])
(d/transact conn [[:db/add 17592186045420 :product/keyword "cellphone"]])


(pprint (ecommerce.db/all-products (d/db conn)))

(pprint (ecommerce.db/all-products-by-keyword (d/db conn) "cellphone"))