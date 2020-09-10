(ns datomic-poc-ecommerce.core
  (:require [datomic-poc-ecommerce.database :as ecommerce.db]
          [datomic-poc-ecommerce.model :as ecommerce.model]
          [datomic.api :as d])
  (:use clojure.pprint))

(def ^:private db-uri "datomic:dev://localhost:4334/nested-test")
(def ^:private conn (ecommerce.db/open-connection db-uri))

(let [computer
      (ecommerce.model/new-product "new computer", "/new_computer", (bigdec 3500.00),
                                   (mapv ecommerce.model/available-unit [{:id (bigint 1)}, {:id (bigint 2)}]))
      phone
      (ecommerce.model/new-product "samsung galaxy S10", "/sms_galaxy_s10", 3000.00M,
                                   (mapv ecommerce.model/available-unit [{:id (bigint 1)}, {:id (bigint 2)}]))]
  (pprint (ecommerce.db/create-schema conn))
  #_(pprint computer)
  #_(pprint phone)
  (ecommerce.db/insert-to-database conn [computer])
  (ecommerce.db/insert-to-database conn [phone]))

(ecommerce.db/delete-database db-uri)