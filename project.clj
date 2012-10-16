(defproject clojurewerkz/serialism "1.0.1-SNAPSHOT"
  :description "A tiny Clojure library that serializes and deserializes values into popular formats based on provided content type"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clojurewerkz/support "0.7.0"]
                 [cheshire             "4.0.3"]]
  :profiles {:1.3 {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.0-master-SNAPSHOT"]]}
             :dev {:resource-paths ["test/resources"]
                   :plugins [[codox "0.6.1"]]
                   :codox {:sources ["src/clojure"]
                           :output-dir "doc/api"}}}
  :aliases {"all" ["with-profile" "dev:dev,1.3:dev,1.5"]}
  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail}}
                 "sonatype-snapshots" {:url "http://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :jvm-opts           ["-Dfile.encoding=utf-8"]
  :source-paths       ["src/clojure"])
