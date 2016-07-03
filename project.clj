(defproject cjdb "0.1.0-SNAPSHOT"
  :description "A set of database utilities. Who knows what will end up in here."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.microsoft.sqlserver/sqljdbc4 "4.0"]
                 [postgresql "9.3-1102.jdbc41"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/java.jdbc "0.6.0-rc1"]
                 [com.rpl/specter "0.10.0"]
                 [aero "1.0.0-beta5"]
                 [org.clojure/tools.cli "0.3.5"]
                 [clj-time "0.8.0"]
                 [semantic-csv "0.1.0"]]
  :main ^:skip-aot cjdb.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
