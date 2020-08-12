(ns store.db)

(def order-1 {:user  15
              :items {
                      :backpack {:id :backpack :quantity 2 :price 10}
                      :shirt {:id :shirt :quantity 3 :price 40}
                      :sneakers {:id :sneakers :quantity 1}}})

(def order-2 {:user  1
              :items {
                      :backpack {:id :backpack :quantity 2 :price 10}
                      :shirt {:id :shirt :quantity 3 :price 40}
                      :sneakers {:id :sneakers :quantity 1}}})


(def order-3 {:user  15
              :items {
                      :backpack {:id :backpack :quantity 2 :price 10}
                      :shirt {:id :shirt :quantity 3 :price 40}
                      :sneakers {:id :sneakers :quantity 1}}})


(def order-4 {:user  15
              :items {
                      :backpack {:id :backpack :quantity 2 :price 10}
                      :shirt {:id :shirt :quantity 3 :price 40}
                      :sneakers {:id :sneakers :quantity 1}}})


(def order-5 {:user  10
              :items {
                      :backpack {:id :backpack :quantity 2 :price 10}
                      :shirt {:id :shirt :quantity 3 :price 40}
                      :sneakers {:id :sneakers :quantity 1}}})


(def order-6 {:user  10
              :items {
                      :backpack {:id :backpack :quantity 20 :price 10}
                      :shirt {:id :shirt :quantity 10 :price 40}
                      :sneakers {:id :sneakers :quantity 1}}})


(defn all-orders []
  [order-1 order-2 order-3 order-4 order-5 order-6])
