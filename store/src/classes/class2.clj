(ns classes.class2)

;; Create my own reduce

(defn count-elements
  "Counts elements in a sequence"
  ([elements] (count-elements 0 elements))
  ([total-count elements]
   (if (seq elements)
     (recur (inc total-count) (next elements))
     total-count)))

(defn count-elements-loop
  "Counts elements in a sequence using loop with tail recursion. Disadvantage: complex function."
  ([elements]
   (loop [total-count 0
          remaining-elements elements]
     (if (seq remaining-elements)
       (recur (inc total-count)
              (next remaining-elements))
       total-count))))

(println "Count recursion function"  (count-elements-loop ["Melissa", "Lucas", "Edna", "Gabriela"]))
(println "Count recursion function"  (count-elements-loop []))

(println "Count recursion loop" (count-elements-loop ["Melissa", "Lucas", "Edna", "Gabriela"]))
(println "Count recursion loop"  (count-elements-loop []))

(defn my-own-reduce
  "Apply fn to elements in a sequence"
  ([result given-fn elements]
   (if (seq elements)
     (recur (given-fn result (first elements)) given-fn (next elements))
     result)))

(println "Sum using my own reduce" (my-own-reduce 0 + [1 2 3 4]))
(println "Sum using my own reduce" (my-own-reduce 0 + []))
