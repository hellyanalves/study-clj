(ns datomic-poc-ecommerce.class_1
  (:require [datomic-poc-ecommerce.database :as ecommerce.db]
            [datomic-poc-ecommerce.model :as ecommerce.model]
            [datomic.api :as d])
  (:use clojure.pprint))

(comment "Datomic works as one big table, containing registers of an entity id and
its properties values. A namespace such as product can be used to avoid ambiguity between
properties. A namespace would be the equivalent to something like a table.
Each line is called a datom.
Example of database entries (datoms):
id attribute         value          id_transaction  put (insert true/delete false)
15 product/name      new computer   1111            true
15 product/slug      computer_new   1112            true
")

(def ^:private db-uri "datomic:dev://localhost:4334/query-test")
(ecommerce.db/delete-database db-uri)
(def ^:private conn (ecommerce.db/open-connection db-uri))

(let [computer
      (ecommerce.model/new-product "new computer", "/new_computer", (bigdec 3500.00))
      phone
      (ecommerce.model/new-product "samsung galaxy S10", "/sms_galaxy_s10", 3000.00M)]
  (ecommerce.db/create-schema conn)
  (ecommerce.db/insert-to-database conn [computer])
  (ecommerce.db/insert-to-database conn [phone]))

(comment "Query database
? denotes a variable, a question to the database")
(let [conn (d/db (ecommerce.db/open-connection db-uri))]
  (pprint (d/q '[:find ?entity
                 :where [?entity :product/name]] conn)))

(comment "Não é possível transacionar nulo para um campo, mas é possível omitir campos ao transacionar")
#_(pprint (let [calculator {:product/name "Scientific calculator"}] :product/slug nil
            (d/transact conn [calculator])))

