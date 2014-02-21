# Serialism, a Multiformat Clojure Serialization Library

Serialism is A tiny Clojure library that serializes and deserializes values
into popular formats based on provided content type.

Supported formats:

 * JSON
 * SMILE (binary JSON)
 * Text
 * Clojure reader forms

It is possible to extend the libray to any other format thanks to [Clojure multimethods](http://clojure-doc.org/articles/language/polymorphism.html).

Serialism is heavily influenced by how automatic value serialization is implemented
in [Welle, a Clojure Riak client](http://clojureriak.info).


## Project Goals

Serialism tries to provide an extensible serialization library that supports
multiple formats behind a single API.


## Community

[Serialism has a mailing list](https://groups.google.com/forum/#!forum/clojure-serialism). Feel free to join it and ask any questions you may have.

To subscribe for announcements of releases, important changes and so on, please follow [@ClojureWerkz](https://twitter.com/#!/clojurewerkz) on Twitter.


## Project Maturity

Serialism is a young project but ideas behind it have proven itself in [Welle](http://clojureriak.info).



## Artifacts

Serialism artifacts are [released to Clojars](https://clojars.org/clojurewerkz/serialism). If you are using Maven, add the following repository
definition to your `pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```

### The Most Recent Release

With Leiningen:

    [clojurewerkz/serialism "1.0.1"]


With Maven:

    <dependency>
      <groupId>clojurewerkz</groupId>
      <artifactId>serialism</artifactId>
      <version>1.0.1</version>
    </dependency>



## Documentation & Examples

Serialism is a tiny library. All key functions are in the `clojurewerkz.serialism.core` namespace:

 * `clojurewerkz.serialism.core/serialize`
 * `clojurewerkz.serialism.core/deserialize`

``` clojure
(ns megacorp.myservice
  (:require [clojurewerkz.serialism.core :as s]))

;;
;; Serialization
;;

;; serialize a string to a byte array
(s/serialize "some data" :bytes)
(s/serialize "some data" s/octet-stream-content-type)

;; serialize a Clojure data structure to JSON
(s/serialize {:library "Serialism"} :json)
(s/serialize {:library "Serialism"} s/json-content-type)
(s/serialize {:library "Serialism"} s/json-utf8-content-type)

;; serialize a Clojure data structure to SMILE (binary JSON)
(s/serialize {:library "Serialism"} :smile)
(s/serialize {:library "Serialism"} s/smile-content-type)

;; serialize a Clojure data structure to Clojure reader forms
(s/serialize {:library "Serialism"} :clojure)
(s/serialize {:library "Serialism"} s/clojure-content-type)


;;
;; Deserialization
;;

;; deserialize a string to a text
(s/deserialize "some data" :bytes)
(s/deserialize "some data" s/octet-stream-content-type)

;; deserialize a byte array to a text
(s/deserialize (.getBytes "some data" "UTF-8") :text)
(s/deserialize (.getBytes "some data" "UTF-8") s/text-content-type)

;; serialize a Clojure data structure to JSON
(s/deserialize "{\"language\":\"Clojure\",\"library\":\"serialism\",\"authors\":[\"Michael\"]}"
               :json)
(s/deserialize "{\"language\":\"Clojure\",\"library\":\"serialism\",\"authors\":[\"Michael\"]}"
               s/json-content-type)
(s/deserialize "{\"language\":\"Clojure\",\"library\":\"serialism\",\"authors\":[\"Michael\"]}"
               s/json-utf8-content-type)

;; serialize a Clojure data structure to SMILE (binary JSON)
(s/deserialize bytes-in-the-smile-format :smile)
(s/deserialize bytes-in-the-smile-format s/smile-content-type)

;; serialize a Clojure data structure to Clojure reader forms
(s/deserialize "#=(clojure.lang.PersistentArrayMap/create {:language \"Clojure\", :library \"serialism\", :authors [\"Michael\"]})"
               :clojure)
(s/deserialize "#=(clojure.lang.PersistentArrayMap/create {:language \"Clojure\", :library \"serialism\", :authors [\"Michael\"]})"
               s/clojure-content-type)
```

Both of these functions are [multimethods that can be extended](http://clojure-doc.org/articles/language/polymorphism.html) to support other
formats (e.g. MessagePack, Kryo or custom formats built with Gloss).


## Supported Clojure versions

Serialism requires Clojure 1.4+.


## Continuous Integration Status

[![Continuous Integration status](https://secure.travis-ci.org/clojurewerkz/serialism.png)](http://travis-ci.org/clojurewerkz/serialism)



## Serialism Is a ClojureWerkz Project

Serialism is part of the [group of Clojure libraries known as ClojureWerkz](http://clojurewerkz.org), together with
[Monger](http://clojuremongodb.info), [Welle](http://clojureriak.info), [Langohr](https://github.com/michaelklishin/langohr), [Elastisch](https://github.com/clojurewerkz/elastisch), [Neocons](http://clojureneo4j.info), [Quartzite](https://github.com/michaelklishin/quartzite), and several others.


## Development

serialism uses [Leiningen 2](https://github.com/technomancy/leiningen/blob/master/doc/TUTORIAL.md). Make sure you have it installed and then run tests against
supported Clojure versions using

    lein2 all test

Then create a branch and make your changes on it. Once you are done with your changes and all tests pass, submit a pull request
on Github.



## License

Copyright (C) 2012-2014 Michael S. Klishin, Alex Petrov.

Double licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) (the same as Clojure) or the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
