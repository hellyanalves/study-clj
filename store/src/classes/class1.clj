(ns classes.class1)

;; Build my own map function


(map println ["Melissa", "Lucas", "Edna", "Gabriela"])
(println (first ["Melissa", "Lucas", "Edna", "Gabriela"])) ; Gets first element of sequence
(println (rest ["Melissa", "Lucas", "Edna", "Gabriela"]))  ; Gets all elements of sequence but the first
(println (next ["Melissa", "Lucas", "Edna", "Gabriela"]))  ; Gets all elements of sequence but the first
(println (rest [])) ;; Returns empty
(println (next [])) ;; Returns nil, can be used to check if the collection is over
(println (seq []))
(println (seq (range 5)))

(println "\n\n**Using my custom map**\n\n")

(defn my-custom-map
  [function sequence]
  (let [first-element (first sequence)]
    (when-not (nil? first-element)
      (function first-element)
      (my-custom-map function (next sequence)))))

(my-custom-map println ["Melissa", false, "Lucas", "Edna", "Gabriela"])

; Using recursion this way could cause a stack overflow error for bigger collections, such as
#_(my-custom-map println (range 10000))


;; Tail Recursion

(defn my-custom-map-tail-recur
  "Custom map using tail recursion (recur) that converts recursion to  loop
  during execution time, preventing stack overflow"
  [function sequence]
  (let [first-element (first sequence)]
    (when-not (nil? first-element)
      (function first-element)
      (recur function (next sequence)))))

#_(my-custom-map-tail-recur println (range 10000))