(ns clojurewerkz.serialism.core-test
  (:require [clojurewerkz.serialism.core :as s])
  (:use clojure.test))


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
      (is (= "{\"language\":\"Clojure\",\"library\":\"serialism\",\"authors\":[\"Michael\"]}"
             (s/serialize value s/json-content-type)))))

  (testing "Clojure to reader forms"
    (let [value {:language "Clojure" :library "serialism" :authors ["Michael"]}]
      (is (= "#=(clojure.lang.PersistentArrayMap/create {:language \"Clojure\", :library \"serialism\", :authors [\"Michael\"]})"
             (s/serialize value s/clojure-content-type)))))

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
