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

(ns clojurewerkz.serialism.core
  (:require [cheshire.custom   :as json]
            [cheshire.core     :as json2]
            [clojure.set       :as cs]
            [clojure.java.io   :as io])
  (:import [java.io ByteArrayOutputStream PrintWriter InputStreamReader ByteArrayInputStream]
           [java.util.zip GZIPOutputStream GZIPInputStream]))


;; clojure.java.io has these as private, so we had to copy them. MK.
(def ^{:doc "Type object for a Java primitive byte array."}
  byte-array-type (class (make-array Byte/TYPE 0)))


(defprotocol BytesConversion
  (^bytes to-bytes [input] "Converts input to a byte array value that can be stored in a bucket"))

(extend-protocol BytesConversion
  String
  (to-bytes [^String input]
    (.getBytes input)))

(defprotocol StringConversion
  (^String to-string [input] "Converts the input to a string using UTF-8 for encoding"))

(extend-protocol StringConversion
  String
  (to-string [^String input]
    input))

(defprotocol ByteArrayInputStreamConversion
  (^ByteArrayInputStream to-byte-array-stream [input] "Converts the input to a string using UTF-8 for encoding"))

(extend-protocol ByteArrayInputStreamConversion
  ByteArrayInputStream
  (to-byte-array-string [^ByteArrayInputStream input]
    input)

  String
  (to-byte-array-stream [^String input]
    (ByteArrayInputStream. (.getBytes input))))


(extend byte-array-type
  BytesConversion
  {:to-bytes (fn [^bytes input]
               input) }

  StringConversion
  {:to-string (fn [^bytes input]
                (String. input "UTF-8")) }

  ByteArrayInputStreamConversion
  {:to-byte-array-stream (fn [^bytes input]
                           (ByteArrayInputStream. input)) })


;;
;; API
;;

(def ^{:const true}
  json-content-type "application/json")

(def ^{:const true}
  json-utf8-content-type "application/json; charset=UTF-8")

(def ^{:const true}
  json-gzip-content-type "application/json+gzip")

(def ^{:const true}
  text-content-type "text/plain")

(def ^{:const true}
  text-utf8-content-type "text/plain; charset=UTF-8")

(def ^{:const true}
  octet-stream-content-type "application/octet-stream")

(def ^{:const true}
  smile-content-type "application/smile")

(def ^{:const true}
  clojure-content-type "application/clojure")



(defmulti serialize (fn [_ content-type]
                      content-type))

;; byte streams, strings
(defmethod serialize octet-stream-content-type
  [value _]
  (to-bytes value))
(defmethod serialize :octet-stream
  [value _]
  (to-bytes value))
(defmethod serialize :bytes
  [value _]
  (to-bytes value))
(defmethod serialize text-content-type
  [value _]
  (to-bytes value))
(defmethod serialize :text
  [value _]
  (to-bytes value))
(defmethod serialize text-utf8-content-type
  [value _]
  (to-bytes value))


;; JSON
(defmethod serialize json-content-type
  [value _]
  (json/encode value))
(defmethod serialize :json
  [value _]
  (json/encode value))
(defmethod serialize json-utf8-content-type
  [value _]
  (json/encode value))
;; a way to support GZip content encoding for both HTTP and PB interfaces.
(defmethod serialize json-gzip-content-type
  [value _]
  (with-open [out    (ByteArrayOutputStream.)
              gzip   (GZIPOutputStream. out)
              writer (PrintWriter. gzip)]
    (json2/generate-stream value writer)
    (.flush writer)
    (.finish gzip)
    (.toByteArray out)))

;; SMILE
(defmethod serialize "application/jackson-smile"
  [value _]
  (json/generate-smile value))

(defmethod serialize smile-content-type
  [value _]
  (json/generate-smile value))

(defmethod serialize :smile
  [value _]
  (json/generate-smile value))

;; Clojure
(defmethod serialize :clojure
  [value _]
  (binding [*print-dup* true]
    (pr-str value)))

(defmethod serialize clojure-content-type
  [value _]
  (binding [*print-dup* true]
    (pr-str value)))



(defmulti deserialize (fn [_ content-type]
                        content-type))
(defmethod deserialize octet-stream-content-type
  [value _]
  value)
(defmethod deserialize :bytes
  [value _]
  value)
(defmethod deserialize text-content-type
  [value _]
  (String. ^bytes value))
(defmethod deserialize :text
  [value _]
  (String. ^bytes value))
(defmethod deserialize text-utf8-content-type
  [value _]
  (String. ^bytes value "UTF-8"))

;; JSON
(defmethod deserialize json-content-type
  [value _]
  (json/parse-string (to-string value) true))

(defmethod deserialize :json
  [value _]
  (json/parse-string (to-string value) true))

(defmethod deserialize json-utf8-content-type
  [value _]
  (json/decode (to-string value) true))
(defmethod deserialize "application/json;charset=UTF-8"
  [value _]
  (json/decode (to-string value) true))
(defmethod deserialize json-gzip-content-type
  [value _]
  (with-open [in (GZIPInputStream. (to-byte-array-stream value))]
    (json/decode-stream (InputStreamReader. in "UTF-8") true)))

;; SMILE (binary JSON)
(defmethod deserialize "application/jackson-smile"
  [value _]
  (json/decode-smile value true))

(defmethod deserialize smile-content-type
  [value _]
  (json/decode-smile value true))

;; Clojure
(defmethod deserialize clojure-content-type
  [value _]
  (binding [*print-dup* true]
    (read-string (to-string value))))


(defmethod deserialize :default
  [value content-type]
  (throw (UnsupportedOperationException. (str "Deserializer for content type " content-type " is not defined"))))
