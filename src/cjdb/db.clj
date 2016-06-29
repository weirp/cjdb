(ns cjdb.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [cjdb.edn :as edn]))

(defn loadFromTable [spec query]
  "simply loads data from a single table"
  (jdbc/query spec [query]))

(defn loadTableToMap [spec query id]
  "loads data from single tbl into map keys by id function"
  (into {} (map (juxt id identity)
                (jdbc/query spec [query]))))

(defn loadTableToMapAlt [spec query id]
  "loads data from single tbl into map keys by id function"
  (into {}
        (jdbc/query spec [query]
                    {:row-fn (juxt id identity)})))

(defn loadTableToMapAsVector [spec query id pth]
"load data in a map. key is id function.
push data into pth.
so, if id -> :id and pth > :child_data
then we have keys like:
[1 :child_data][2 :child_data]

then, assuming this is a child tbl, we can merge this map into map of
parent data, and the vector of child elements will be @ :child_data coordinates."
  (into {}
        (jdbc/query spec
                    [query]
                    {:result-set-fn
                     (fn [rs]
                       (reduce
                        (fn [m this_data]
                          (update-in m
                                     (apply conj [(id this_data)] pth)
                                     (fn [already_collected_data]
                                       (into [] (conj already_collected_data this_data)))))
                              {}
                              rs))})))

;;(ns cjdb.db (:require [cjdb.config :as config]))
;;(def db-spec (:database-spec (config/config)))
;;(loadFromTable db-spec "select * from contract_managementx")
;;(loadTableToMap db-spec "select * from contract_managementx" :contract_id)
;;(loadTableToMapAlt db-spec "select * from contract_managementx" :contract_id)
;;(loadTableToMapAsVector db-spec "contract_managementx" :checked [:ccc :ss])
;;(get-in (loadTableToMapAsVector db-spec "select * from contract_managementx" :checked nil) [true])
;;(get-in (loadTableToMapAsVector db-spec "select * from contract_managementx" :checked nil) [false])

;;(apply conj [1] nil)
;;(apply conj [1] [2 3])



;; TODO:
;; pass in array of queries, along with path for each
;; could do 'simple' assoc into first query.
;; anything more complex; just do individually...... like when we get to 3
;; queries, it could be 2 & 3 both assoc in the first; or 2 into 1, and 3 into 2.


(defn getTables [db-spec catalog schema tbl]
  (into {} (map (juxt :table_name identity)
                (jdbc/with-db-metadata [md db-spec]
                  (jdbc/metadata-result
                   (.getTables md catalog schema tbl (into-array ["TABLE" ])))))))

(defn getColumns [db-spec catalog schema tbl]
  (into {}
        (reduce (fn [m x]
                  (update-in m [(:table_name x) :columns]
                             (fn [cols]
                               (into [] (conj cols x)))))
                {}
                (into [] (jdbc/with-db-metadata [md db-spec]
                           (jdbc/metadata-result
                            (.getColumns md catalog schema tbl nil)))))))


(defn buildMetadata [db-spec]
  (let [tbls (getTables db-spec nil nil nil)
        cols (getColumns db-spec nil nil nil)]
    (merge-with conj tbls cols)))

;;(map :column_name (get-in (buildMetadata db-spec) ["contract_term"]))

(defn buildTable [md tblName]
  (let [tblData (get-in md [tblName])
        colDataStr (map (fn [x] (str (:column_name x) " " (:type_name x))) (:columns tblData))
        colData (into [] (map (fn [x] [ (:column_name x) (:type_name x)]) (:columns tblData)))]
    colData))

;;(jdbc/create-table-ddl :cmx (buildTable (buildMetadata db-spec) "contract_managementx"))

(defn build-copy-of-tbl-example [db-spec]
  (jdbc/db-do-commands db-spec
                       (jdbc/create-table-ddl
                        :cmx
                        (buildTable (buildMetadata db-spec) "contract_managementx"))))



(defn extractSchemaInfo [spec filename]
  (let [sch (buildMetadata spec)]
    (edn/write-edn-data sch filename)))

;;(edn/write-edn-data sch "sch.edn")


;; music collection might be good example
;; artist
;; album
;; track
;;
;; there is a standard dataset: mbrainz?
;;

;; name these:
;;       merge-into
;;       collect-by
;; etc
