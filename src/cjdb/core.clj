(ns cjdb.core
  (:require [clojure.string :as string]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.cli :refer [parse-opts]]
            [cjdb.db :as db]
            [cjdb.config :as config])
  (:gen-class))

(def cli-options
  [
   ["-p" "--profile" "Profile"
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
        "   create      create table"]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  ""
  [& args]
  (let [{:keys [options arguments errors summary] :as allopts} (parse-opts args cli-options)]
    (pprint allopts)
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors)))
    (case (first arguments)
      "blah" (do (pprint "blah")
                 (exit 0 "done."))
      "extract" (do (pprint (str "extract to " (:file options)))
                    (pprint (str "db spec = " (:database-spec (config/config))))
                    (db/extractSchemaInfo (:database-spec (config/config))
                                          (:catalog options)
                                          (:schema options)
                                          (:table options)
                                          (:file options))
                    (exit 0 "done.")
                    )
      "create" (do (pprint (str "create from " (:file options)))
                   (exit 0 "done."))
      (exit 1 (usage summary)))))

;;(:database-spec (config/config))
