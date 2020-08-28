(ns hospital-test.logic-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [hospital-test.logic :as h.logic]))

(comment "To get the benefits of TDD - Test Driven Development
or Test Driven Design requires the developer to know what to test.")

(comment " *Boundary Tests*
One way to achieve that test checklist is by using border/edge
cases. It's a good practice to create an actual checklist.
Symbols that may be null and cause a wrong behaviour should also be considered
as edge cases (such as department in this case)")

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

(comment "Avoid using sequential values")
(deftest arrive-at!-test
  (testing "Accepts people while person fits queue"
    ; lower limit
    (is (=
          {:waiting-line [5]}
          (h.logic/arrive-at? {:waiting-line []} :waiting-line 5)))
    ; upper limit
    (is (=
          {:waiting-line [1 4 3 6 5]}
          (h.logic/arrive-at? {:waiting-line [1 4 3 6]} :waiting-line 5))))
  (testing "Accepts people while person fits. Middle cases"
    (is (=
          {:waiting-line [10 1 5]}
          (h.logic/arrive-at? {:waiting-line [10 1]} :waiting-line 5))))
  (testing "Refuses person when waiting line is full. Check if exception is thrown"
    (comment "Checking if the generic exceptions ExceptionInfo or IllegalStateException
     were thrown may not provide the security needed to assure the code is working exactly as it should,
    because the exception could have been thrown on another part of the code.
    Checking the ExceptionInfo message could also not be a good approach, because of how
    easily a string can be edited on some other part of the code, thus breaking the test.")
    (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Queue full. Patient does not fit"
          (h.logic/arrive-at-1? {:waiting-line [10 1 3 5 6]} :waiting-line 2)))
    (is (thrown? IllegalStateException
                 (h.logic/arrive-at-2? {:waiting-line [10 1 3 5 6]} :waiting-line 2)))
    ;; not the best alternative as we may use the return of this function to continue our processing
    (is (nil? (h.logic/arrive-at-3? {:waiting-line [10 1 3 5 6]} :waiting-line 2)))))
