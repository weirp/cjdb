(ns cjdb.csv
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [semantic-csv.core :as sc :refer :all]))

(defn load-data [filename delim]
  (with-open [in-file (io/reader filename)]
    (doall
     (process {:cast-fns (fn [x] (if (= "NULL" x)
                                   nil
                                   x))}
              (csv/parse-csv in-file :delimiter delim)))))


(defn prep-iis-log [filename]
  (let [raw-data (load-data filename \tab)]
    raw-data))

;;(prep-iis-log "resources/u_ex160719.log")
;;(first (load-data "resources/u_ex160719.log" \tab))
