(ns hospital-test.logic
  (:require [schema.core :as s]
            [hospital-test.model :as model]))

(defn patient-fits-queue? [hospital department]
  "Evaluates if the given department from the given hospital waiting line fits more patients.
  Limit is defined as 5 people.
  Using some threading to ensure department exists"
    (some->
      hospital
      department
      count
      (< 5)))

(comment "Returning a map we can get an actual picture of the result of the operation.
defn- defines a private fn.
The result of the arrive-at! fn has to be treated if an atom swap is being used.")
(defn- try-arrive-at! [hospital department person]
  "Simulates a person arriving to the waiting line at some
  department of the hospital. Returns nil if the queue is full"
  (when (patient-fits-queue? hospital department)
    (update hospital department conj person)))

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

(s/defn assess-patient
  [hospital :- model/Hospital
   department :- s/Keyword] :- model/Hospital
  "Removes patient from queue, simulating a medical assessment"
  (update hospital department pop))

(s/defn next-patient :- model/PatientId
  [hospital :- model/Hospital
   department :- s/Keyword]
  "Returns the next patient in line"
  (-> hospital
      (get department)
      peek))


(defn same-size? [hospital new-hospital origin-department destination-department]
  (= (+ (count (get new-hospital origin-department))
        (count (get new-hospital destination-department)))
     (+ (count (get hospital origin-department))
        (count (get hospital destination-department)))))

;; Tests could be added to ensure AssertionErrors are being thrown when pre and post conditions
;; are not being fulfilled.
(s/defn transfer-patient :- model/Hospital
  [hospital :- model/Hospital
   origin-department :- s/Keyword
   destination-department :- s/Keyword]
  {:pre [(contains? hospital origin-department)
         (contains? hospital destination-department)]
   :post [(same-size? hospital % origin-department destination-department)]}

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
