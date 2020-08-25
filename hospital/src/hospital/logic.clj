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

(defn arrive-at-with-logs [hospital department person]
  (println "Trying to add " person)
  (if (patient-fits-queue? hospital department)
    (do
      (println "Updated " person)
      (update hospital department conj person))
    (throw (ex-info "Queue already full" {:trying-to-add person}))))

(defn arrive-at-with-waiting [hospital department person]
  (if (patient-fits-queue? hospital department)
    (do (Thread/sleep 1000)
        (update hospital department conj person))
    (throw (ex-info "Queue already full" {:trying-to-add person}))))

(defn arrive-at-with-waiting-with-logs [hospital department person]
  (println "Trying to add " person)
  (Thread/sleep 1000)
  (if (patient-fits-queue? hospital department)
    (do ;(Thread/sleep 1000)
      (println "Updated " person)
      (update hospital department conj person))
    (throw (ex-info "Queue already full" {:trying-to-add person}))))

(defn arrive-at-lock [hospital department person]
  "Using locking"
  (locking hospital
    (println "Trying to add " person)
    (if (patient-fits-queue? hospital department)
      (do
        (println "Updated " person)
        (update hospital department conj person))
      (throw (ex-info "Queue already full" {:trying-to-add person})))))

(defn assess-patient [hospital department]
  (update hospital department pop))

(defn assess-and-return-patient [hospital department]
    {:patient  (update hospital department peek)
     :hospital (update hospital department pop)})

(defn assess-and-return-patient-juxt [hospital department]
  (let [queue (get hospital department)
        peek-pop (juxt peek pop)
        [person new-queue] (peek-pop queue)
        new-hospital (update hospital assoc department new-queue)]
    {:patient  person
     :hospital new-hospital}))

(defn next-person [hospital department]
  "Returns next patient waiting in line for the given department"
  (-> hospital
      department
      peek))

(defn transfers [hospital origin-department destination-department]
  "Transfers patient from origin to destination department"
  (let [person (next-person hospital origin-department)]
    (-> hospital
        (assess-patient origin-department)
        (arrive-at destination-department person))))
