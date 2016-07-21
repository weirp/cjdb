(ns cjdb.core
  (:require [clojure.string :as string]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.cli :refer [parse-opts]]
            [cjdb.db :as db]
            [cjdb.config :as config]
            [cjdb.edn :as edn]
            [cjdb.csv :as csv])
  (:gen-class))

(def cli-options
  [
   ["-p" "--profile PROFILE" "Profile"
    :default :dev]
   ["-f" "--file FILE" "Filename"
    :default "out.edn"
    :parse-fn #(str %)]
   ["-c" "--catalog CAT" "Catalog"
    :default nil
    :parse-fn #(str %)]
   ["-s" "--schema SCH" "Schema"
    :default nil
    :parse-fn #(str %)]
   ["-t" "--table TBL" "Table"
    :default nil
    :parse-fn #(str %)]
   ["-x" "--createTarget CAT.SCH.TBL" "New path for table"
    :default nil
    :parse-fn #(str %)]
   ["-i" "--iisLog FILE" "iis log file"
    :default nil
    :parse-fn #(str %)]


   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["description"
        ""
        "Usage: prog [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "   extract     extract schema info from a database"
        "   create      create table"
        "   printTables print tables from file"
        "   iislog      process IIS logs"
        ]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))
<
(defn -main
  ""
  [& args]
  (let [{:keys [options arguments errors summary] :as allopts} (parse-opts args cli-options)]
    (pprint allopts)
    (pprint (config/config (keyword (:profile options))))
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors)))
    (case (first arguments)
      "blah" (do (pprint "blah")
                 (exit 0 "done."))
      "extract" (do (pprint (str "extract to " (:file options)))
                    (pprint (str "source db = " (:source-database (config/config))))
                    (db/extractSchemaInfo (:source-database (config/config))
                                          (:catalog options)
                                          (:schema options)
                                          (:table options)
                                          (:file options))
                    (exit 0 "done.")
                    )
      "create" (do (pprint (str "create from " (:file options) " to db "
                                (:target-database options)))
                   (exit 0 "done."))
      "printTables" (do (let [data (edn/read-edn-data (:file options))]
                          (pprint (type (into [] data)))
                          (pprint (map (juxt :table_type :table_cat :table_schem :table_name)
                                       (vals data)))
                          ;;(pprint (vals data))
                          )
                        (exit 0 "done."))
      "iislog" (do (let [data (csv/output-iis-freq-data (:iisLog options) "out.csv")])
                   (exit 0 "done."))
      (exit 1 (usage summary)))))

;;(def db-spec (:source-database (config/config {:profile :dev})))
;;(def data (edn/read-edn-data "out.edn"))
;;(keys  (nth (vals data) 190))

;;(def t (db/getTables db-spec nil "musicbrainz" nil))
;;(def c (db/getColumns db-spec nil "musicbrainz" nil))
;;(count (keys c))
;;(count (keys t))

;;(filter #(contains? t (key %) ) c)

;;(filter #(contains? [1 2 3] %) [2 4 5 1 6 3 2])
