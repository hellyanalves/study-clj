(ns hospital.logic)

(defn patient-fits-queue? [hospital department]
  (-> hospital
      (get department)
      count
      (< 5)))

(defn arrive-at [hospital department person]
    (if (patient-fits-queue? hospital department)
      (update hospital department conj person)
      (throw (ex-info "Queue already full" {:trying-to-add person}))))

(defn arrive-at-with-waiting [hospital department person]
  (if (patient-fits-queue? hospital department)
    (do (Thread/sleep 1000)
        (update hospital department conj person))
    (throw (ex-info "Queue already full" {:trying-to-add person}))))

(defn assess-patient [hospital department]
  (update hospital department pop))
