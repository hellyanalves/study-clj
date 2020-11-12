(ns datomic-poc-ecommerce.class-5
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
      result @(d/transact conn [computer, phone])]
  (pprint result))

(def snapshot (d/db conn))
(def ^:private inst (-> (java.time.Instant/now)
                        (java.util.Date/from)))

(let [phone-2
      (ecommerce.model/new-product "samsung J1" "/samsung-j1" 850.00M)
      calculator
      {:product/name "Scientific calculator"}
      result @(d/transact conn [phone-2, calculator])]
  (pprint result))

;; Snapshot em um banco filtrado com dados do passado
(pprint inst)
(pprint (count (ecommerce.db/all-products snapshot)))
(pprint (count (ecommerce.db/all-products (d/as-of (d/db conn) inst))))
