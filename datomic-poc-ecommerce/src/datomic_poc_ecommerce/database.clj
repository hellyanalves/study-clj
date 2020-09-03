(ns datomic-poc-ecommerce.database
  (:require [datomic.api :as d])
  (:use clojure.pprint))

(comment "To specify the values a datom property can take in this database a schema
must be defined.")
(def ^:private db-schema [{:db/ident       :product/name
                           :db/valueType   :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc         "Product name"}
                          {:db/ident       :product/slug
                           :db/valueType   :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc         "Product http path"}
                          {:db/ident       :product/price
                           :db/valueType   :db.type/bigdec
                           :db/cardinality :db.cardinality/one
                           :db/doc         "Product price with monetary precision"}])

(defn open-connection [db-uri]
  (d/create-database db-uri)
  (d/connect db-uri))

(defn create-schema [conn]
  (d/transact conn db-schema))

(defn insert-to-database [conn data]
  (clojure.pprint/pprint (d/transact conn data)))

(defn delete-database [db-uri]
  (d/delete-database db-uri))

(defn all-products [db]
  "List all products"
  (d/q '[:find ?entity
         :where [?entity :product/name]] db))

(defn products-by-slug [db search-string]
  "List all products fixed slug. If there is no :in key, the database is an implicit argument."
  (d/q '[:find ?entity
         :where [?entity :product/slug "/new_computer"]] db))


; $ denotes the database (standard symbol)
(def find-products-by-slug
  "List all products by slug query"
  '[:find ?entity
    :in $ ?search-query-slug
    :where [?entity :product/slug ?search-query-slug]])

(defn products-by-slug [db slug-search-string]
  "List all products"
  (d/q find-products-by-slug db slug-search-string))

; if the entity is not being used, the value can be replaced by an underscore _
(defn all-products-slugs [db]
  "List all products"
  (d/q '[:find ?any-value
         :where [_ :product/slug ?any-value]] db))

(defn all-products-prices [db]
  "List all products. Returns name, price tuple"
  (d/q '[:find ?name, ?price
         :keys name, price
         :where [?product :product/price ?price]
                [?product :product/name ?name]] db))

(defn all-products-prices-keys [db]
  "List all products. Returns a map containing the keys name and price"
  (d/q '[:find ?name, ?price
         :keys product/name, product/price
         :where [?product :product/price ?price]
         [?product :product/name ?name]] db))

(defn all-products [db]
  "List all products. Using pull to get all specified fields from entity ?product"
  (d/q '[:find (pull ?product [:product/name :product/price :product/slug])
         :where [?product :product/name]] db))

(defn all-products-all-fields [db]
  "List all products. Using pull to get all fields from entity ?product"
  (d/q '[:find (pull ?product [*])
         :where [?product :product/name]] db))