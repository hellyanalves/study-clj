(ns hospital-test.logic)

(defn patient-fits-queue-old? [hospital department]
  "Evaluates if the given department from the given hospital waiting line fits more patients.
  Limit is defined as 5 people.
  Using when-let to ensure department exists"
  (when-let [queue (get hospital department)]
    (->
      queue
      count
      (< 5))))

(defn patient-fits-queue? [hospital department]
  "Evaluates if the given department from the given hospital waiting line fits more patients.
  Limit is defined as 5 people.
  Using some threading to ensure department exists"
    (some->
      hospital
      department
      count
      (< 5)))

(defn arrive-at-1? [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital."
  (if (patient-fits-queue? hospital department)
    (update hospital department conj person)
    (throw (ex-info "Queue full. Patient does not fit" {:patient person}))))

(defn arrive-at-2? [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital."
  (if (patient-fits-queue? hospital department)
    (update hospital department conj person)
    (throw (IllegalStateException.))))

(defn arrive-at-3? [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital."
  (when (patient-fits-queue? hospital department)
    (update hospital department conj person)))