;; This source code is dual-licensed under the Apache License, version
;; 2.0, and the Eclipse Public License, version 1.0.
;;
;; The APL v2.0:
;;
;; ----------------------------------------------------------------------------------
;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
;; ----------------------------------------------------------------------------------
;;
;; The EPL v1.0:
;;
;; ----------------------------------------------------------------------------------
;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team.
;; All rights reserved.
;;
;; This program and the accompanying materials are made available under the terms of
;; the Eclipse Public License Version 1.0,
;; which accompanies this distribution and is available at
;; http://www.eclipse.org/legal/epl-v10.html.
;; ----------------------------------------------------------------------------------

(ns clojurewerkz.serialism.core-test
  (:require [clojurewerkz.serialism.core :as s]
            [clojure.test :refer :all]
            [cheshire.core :as json]))


(deftest test-serialization
  (testing "bytes to bytes"
    (let [value (.getBytes "a string")]
      (is (= value (s/serialize value :bytes)))))
  (testing "string to bytes"
    (let [value "a string"]
      (is (= (vec (.getBytes value))
             (vec (s/serialize value :bytes))))))

  (testing "bytes to text"
    (let [value (.getBytes "строка")]
      (is (= value (s/serialize value :text)))))
  (testing "string to text"
    (let [value "an über string brø"]
      (is (= (vec (.getBytes value))
             (vec (s/serialize value :text))))))

  (testing "Clojure to JSON"
    (let [value {:language "Clojure" :library "serialism" :authors ["Michael"]}]
      (is (= value
             (json/parse-string (s/serialize value s/json-content-type) true)))))

  (testing "Clojure to reader forms"
    (let [value {:language "Clojure" :library "serialism" :authors ["Michael"]}]
      (is (= value
             (read-string (s/serialize value s/clojure-content-type))))))

  (testing "Clojure to SMILE"
    (let [value {:language "Clojure" :library "serialism" :authors ["Michael"]}]
      (is (instance? (Class/forName "[B") (s/serialize value s/smile-content-type))))))


(deftest test-deserialization
  (testing "bytes to string"
    (let [value (.getBytes "строка")]
      (is (= "строка" (s/deserialize value :text)))))
  (testing "JSON to Clojure"
    (let [json  "{\"language\":\"Clojure\",\"library\":\"serialism\",\"authors\":[\"Michael\"]}"
          value {:language "Clojure" :library "serialism" :authors ["Michael"]}]
      (is (= value
             (s/deserialize json s/json-content-type)))))
  (testing "reader forms to Clojure"
    (let [forms "#=(clojure.lang.PersistentArrayMap/create {:language \"Clojure\", :library \"serialism\", :authors [\"Michael\"]})"
          value {:language "Clojure" :library "serialism" :authors ["Michael"]}]
      (is (= value
             (s/deserialize forms s/clojure-content-type))))))
