(defproject clojurewerkz/serialism "1.3.0-SNAPSHOT"
  :description "A tiny Clojure library that serializes and deserializes values into popular formats based on provided content type"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clojurewerkz/support "0.20.0"]
                 [cheshire             "5.3.1"]]
  :profiles {:1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :master {:dependencies [[org.clojure/clojure "1.7.0-master-SNAPSHOT"]]}
             :dev {:resource-paths ["test/resources"]
                   :plugins [[codox "0.6.7"]]
                   :codox {:sources ["src/clojure"]
                           :output-dir "doc/api"}}}
  :aliases {"all" ["with-profile" "dev:dev,1.4:dev,1.5:dev,master"]}
  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail}}
                 "sonatype-snapshots" {:url "http://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :jvm-opts           ["-Dfile.encoding=utf-8"]
  :source-paths       ["src/clojure"])
