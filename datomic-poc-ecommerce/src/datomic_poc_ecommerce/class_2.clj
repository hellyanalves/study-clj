(ns datomic-poc-ecommerce.class-2
  (:require [datomic-poc-ecommerce.database :as ecommerce.db]
          [datomic-poc-ecommerce.model :as ecommerce.model]
          [datomic.api :as d])
  (:use clojure.pprint))

(def ^:private db-uri "datomic:dev://localhost:4334/query-test")
(ecommerce.db/delete-database db-uri)
(def ^:private conn (ecommerce.db/open-connection db-uri))
(ecommerce.db/create-schema conn)

(let [phone (ecommerce.model/new-product "samsung J1" "/samsung-j1" 850.00M)
      result @(d/transact conn [phone])
      entity-id (-> (:tempids result) vals first)]
  (pprint result)
  ;; update item Datomic (:db/add - sets old value as false, inserts new value as true)
  ;; #datom[17592186045422 74 200.00M 13194139534319 true] #datom[17592186045422 74 850.00M 13194139534319 false]]
  (pprint @(d/transact conn [[:db/add entity-id :product/price 200.00M]]))
  ;; "Remove" item Datomic (:db/retract - sets old value as false)
  ;; #datom[17592186045422 73 "/samsung-j1" 13194139534320 false]],
  (pprint @(d/transact conn [[:db/retract entity-id :product/slug "/samsung-j1"]])))