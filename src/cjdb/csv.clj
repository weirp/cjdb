(ns cjdb.csv
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [semantic-csv.core :as sc :refer :all]
            [clojure.string :as string]))

(defn load-data [filename delim]
  (with-open [in-file (io/reader filename)]
    (doall
     (process {:cast-fns (fn [x] (if (= "NULL" x)
                                   nil
                                   x))}
              (csv/parse-csv in-file :delimiter delim)))))


(defn prep-iis-log [filename]
  "loads iis log. Calculates and injects time_millis entry into each row."
  (let [raw-data (load-data filename \space)]
    (map (fn [m]
           (assoc-in m [:time_millis]
                     (Integer/parseInt (string/join (string/split (:time m) #":")))))
         raw-data)))

;;(prep-iis-log "resources/u_ex160719.log")
;;(first (load-data "resources/u_ex160719.log" \tab))
;;(first (load-data "resources/u_ex160719.log" \space))

;;(assoc-in (first (load-data "resources/u_ex160719.log" \space)) [:time_millis] "xxx")

;;(map (fn [m] (assoc-in m [:time_millis] (Integer/parseInt (string/join (string/split (:time m) #":"))))) (take 2 (load-data "resources/u_ex160719.log" \space)) )

(reduce (fn [m x]
          (let [from (:time-millis x)
                to (+ from (:time-taken x))]
            (update-in m ))))
(take 2 (prep-iis-log "resources/u_ex160719.log"))

(range 10 78 4)

(def m {})
(update-in m [1] (fn [x] ((fnil inc 0) x)))

(map (fn [a] (update-in m [a] (fn [x] ((fnil inc 0) x)))) [1 2 4 5 1 6 1])
(fnil m 0)
