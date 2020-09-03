(ns hospital-test.logic)

(defn patient-fits-queue? [hospital department]
  "Evaluates if the given department from the given hospital waiting line fits more patients.
  Limit is defined as 5 people.
  Using some threading to ensure department exists"
    (some->
      hospital
      department
      count
      (< 5)))


(defn arrive-at! [hospital department person]
  (if-let [new-hospital (try-arrive-at! hospital department person)]
    {:hospital new-hospital :result :success}
    {:hospital hospital :result :unable-to-insert}))

(defn arrive-at-ex-info! [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital."
  (if (patient-fits-queue? hospital department)
    (update hospital department conj person)
    (throw (ex-info "Queue full. Patient does not fit" {:patient person, :type :unable-to-insert}))))

(defn assess-patient [hospital department]
  "Removes patient from queue, simulating a medical assessment"
  (update hospital department pop))

(defn next-patient [hospital department]
  "Returns the next patient in line"
  (-> hospital
      (get department)
      peek))

(defn transfer-patient [hospital origin-department destination-department]
  "Transfers patient from origin department to destination department"
  (let [person (next-patient hospital origin-department)]
    (-> hospital
        (assess-patient origin-department)
        (arrive-at-ex-info! destination-department person))))


(defn patient-fits-queue-old? [hospital department]
  "Evaluates if the given department from the given hospital waiting line fits more patients.
  Limit is defined as 5 people.
  Using when-let to ensure department exists"
  (when-let [queue (get hospital department)]
    (->
      queue
      count
      (< 5))))

(defn arrive-at-ex-info! [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital."
  (if (patient-fits-queue? hospital department)
    (update hospital department conj person)
    (throw (ex-info "Queue full. Patient does not fit" {:patient person, :type :unable-to-insert}))))

(defn arrive-at-illegal-state! [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital."
  (if (patient-fits-queue? hospital department)
    (update hospital department conj person)
    (throw (IllegalStateException.))))

(defn arrive-at-nil! [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital. Returns nil if the queue is full"
  (when (patient-fits-queue? hospital department)
    (update hospital department conj person)))


(comment "Returning a map we can get an actual picture of the result of the operation.
defn- defines a private fn.
The result of the arrive-at! fn has to be treated if an atom swap is being used.")
(defn- try-arrive-at! [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital. Returns nil if the queue is full"
  (when (patient-fits-queue? hospital department)
    (update hospital department conj person)))