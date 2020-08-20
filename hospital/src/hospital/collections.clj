(ns hospital.collections
  (:use [clojure.pprint]))

(pprint "Due to immutability, conj, remove and pop return a different version for each print,
adding or removing only the element given on that line")

(defn vector-test []
  (println "\n\n VECTOR TEST \n")
  (let [waiting-line [111 222]]
       (pprint waiting-line)
       (pprint (conj waiting-line 333))
       (pprint (conj waiting-line 444))
       (pprint (remove #(= 111 %) waiting-line))
       (pprint "pop removes from end of vector, working differently from what you would expect from a waiting line")
       (pprint (pop waiting-line))))

(defn list-test []
  (println "\n\n LIST TEST \n")
  (let [waiting-line '(111 222)]
    (pprint waiting-line)
    (pprint "conj adds element at the beginning, working differently from what you would expect from a waiting line")
    (pprint (conj waiting-line 333))
    (pprint (conj waiting-line 444))
    (pprint (remove #(= 111 %) waiting-line))
    (pprint "Removes from beginning")
    (pprint (pop waiting-line))))


(defn set-test []
  (println "\n\n SET TEST \n")
  (let [waiting-line #{111 222}]
    (pprint waiting-line)
    (pprint "Ignores duplicate element")
    (pprint (conj waiting-line 111))
    (pprint (conj waiting-line 333))
    (pprint (conj waiting-line 444))
    (pprint (remove #(= 111 %) waiting-line))))
    ; pop cannot be executed on a set, because it is not a stack
    ;(pprint (pop waiting-line))))

(defn print-queue [value]
  (-> value
      seq
      pprint))

(defn queue-test []
  (println "\n\n QUEUE TEST \n")
  (let [waiting-line (conj clojure.lang.PersistentQueue/EMPTY 111 222)]
    (print-queue waiting-line)
    (print-queue (conj waiting-line 333))
    (print-queue (pop waiting-line))
    (pprint (peek waiting-line))))

(vector-test)
(list-test)
(set-test)
(queue-test)