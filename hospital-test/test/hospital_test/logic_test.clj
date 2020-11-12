(ns hospital-test.logic-test
  (:require [clojure.test :refer [deftest is testing]]
            [hospital-test.logic :as h.logic]
            [hospital-test.model :as h.model]
            [schema.core :as s])
  (:use clojure.pprint)
  (:import (clojure.lang ExceptionInfo)))

(s/set-fn-validation! true)
(comment "To get the benefits of TDD - Test Driven Development
or Test Driven Design requires the developer to know what to test.")

(comment " *Boundary Tests*
One way to achieve that test checklist is by using border/edge
cases. It's a good practice to create an actual checklist.
Symbols that may be null and cause a wrong behaviour should also be considered
as edge cases (such as department in this case)")

(comment " *Integration Tests*
Ensure the individual functions work does not ensure that they work
together the way that is expected. To do that, integration tests are important.")

(deftest patient-fits-queue?-test
  (testing "Patient fits queue"
    ; lower limit
    (is (h.logic/patient-fits-queue?
          {:waiting-line []}
          :waiting-line))
    ; one off lower limit
    (is (h.logic/patient-fits-queue?
          {:waiting-line [1]}
          :waiting-line))
    ;one off upper limit
    (is (h.logic/patient-fits-queue?
          {:waiting-line (range 4)}
          :waiting-line)))
  (testing "Patient does not fit queue"
    ; upper limit
    (is (not (h.logic/patient-fits-queue?
               {:waiting-line (range 5)}
               :waiting-line)))
    ; one off upper limit
    (is (not (h.logic/patient-fits-queue?
               {:waiting-line (range 6)}
               :waiting-line))))
  (testing "Non existing department (should return false)"
    (is (not (h.logic/patient-fits-queue?
               {:waiting-line (range 6)}
               :x-ray-line)))))

(deftest transfer-patient-test
  (testing "Accepts patient if they fit in the queue"
    (let [original-hospital {:waiting-line (conj h.model/empty-line "5"), :x-ray-line h.model/empty-line}]
      (is (= {:waiting-line h.model/empty-line
              :x-ray-line (conj h.model/empty-line "5")}
             (h.logic/transfer-patient original-hospital
                                       :waiting-line
                                       :x-ray-line))))
    (let [original-hospital {:waiting-line (conj h.model/empty-line "5" "6"),
                             :x-ray-line (conj h.model/empty-line "8")}]
      (is (= {:waiting-line (conj h.model/empty-line "6") :x-ray-line (conj h.model/empty-line "8" "5")}
             (h.logic/transfer-patient original-hospital
                                       :waiting-line
                                       :x-ray-line)))))
  (testing "Refuses patient if they do not fit in the queue"
    (let [full-hospital {:waiting-line (conj h.model/empty-line "5"),
                         :x-ray-line (conj h.model/empty-line "6" "9" "3" "7" "4")}]
      (is (thrown? ExceptionInfo
                   (h.logic/transfer-patient full-hospital
                                             :waiting-line
                                             :x-ray-line)))))
  ;; Guarantees that the schema is being validated (the schema validation can be altered)
  (testing "Should not be able to perform transfer without an hospital"
    (is (thrown? ExceptionInfo
                 (h.logic/transfer-patient nil :waiting-line :x-ray-line))))
  (testing "Required conditions"
    (let [original-hospital {:waiting-line (conj h.model/empty-line "5"), :x-ray-line h.model/empty-line}]
      (is (thrown? AssertionError
                   (h.logic/transfer-patient original-hospital
                                             :non-existing-department
                                             :x-ray-line)))
      (is (thrown? AssertionError
                   (h.logic/transfer-patient original-hospital
                                             :x-ray-line
                                             :non-existing-department))))))


(deftest arrive-at!-test
  (let [full-hospital {:waiting-line [5 1 3 9 8]}]
    (testing "Accepts people while person fits queue"
      ; lower limit
      (is (=
            {:hospital {:waiting-line [5]} :result :success}
            (h.logic/arrive-at! {:waiting-line []} :waiting-line 5))))
    (testing "Refuses person when waiting line is full."
      (is (=
            {:hospital full-hospital :result :unable-to-insert}
            (h.logic/arrive-at! full-hospital :waiting-line 7))))))


(comment "Avoid using sequential values")
(deftest arrive-at!-test-for-learning
  (let [full-hospital {:waiting-line [5 1 3 9 8]}
        empty-hospital {:waiting-line []}]
    (testing "Accepts people while person fits queue"
      ; lower limit
      (is (=
            {:waiting-line [5]}
            (h.logic/arrive-at-ex-info! empty-hospital :waiting-line 5)))
      ; upper limit
      (is (=
            {:waiting-line [1 4 3 6 5]}
            (h.logic/arrive-at-ex-info! {:waiting-line [1 4 3 6]} :waiting-line 5))))
    (testing "Accepts people while person fits. Middle cases"
      (is (=
            {:waiting-line [10 1 5]}
            (h.logic/arrive-at-ex-info! {:waiting-line [10 1]} :waiting-line 5))))
    (testing "Refuses person when waiting line is full. Check if exception is thrown"
      (comment "Checking if the generic exceptions ExceptionInfo or IllegalStateException
     were thrown may not provide the security needed to assure the code is working exactly as it should,
    because the exception could have been thrown on another part of the code.
    Checking the ExceptionInfo message could also not be a good approach, because of how
    easily a string can be edited on some other part of the code, thus breaking the test.")
      (is (thrown-with-msg? ExceptionInfo #"Queue full. Patient does not fit"
                            (h.logic/arrive-at-ex-info! full-hospital :waiting-line 2)))
      (is (thrown? IllegalStateException
                   (h.logic/arrive-at-illegal-state! full-hospital :waiting-line 2)))
      ;; not the best alternative as we may use the return of this function to continue our processing
      (is (nil? (h.logic/arrive-at-nil! full-hospital :waiting-line 2)))
      (comment "Another way to test with exception info, less sensible to changes in the
    string is to check exception data.")
      (is (try
            (h.logic/arrive-at-ex-info! full-hospital :waiting-line 2)
            false
            (catch ExceptionInfo e
              (= :unable-to-insert (:type (ex-data e)))))))))




