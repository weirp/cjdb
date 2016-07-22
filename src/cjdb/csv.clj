(ns cjdb.csv
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [semantic-csv.core :as sc :refer :all]
            [clojure.string :as string]
            [clojure.data.csv :as cd]))

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
                        (let [[h m s] (map #(Integer/parseInt %) (string/split (:time m) #":"))
                              millis (* (+ (* (+ (* h 60) m) 60) s) 1000)]
                          millis)))
         raw-data)))

(defn do-freq-analysis [filename linger]
  (reduce
   (fn [m x]
     (update-in m [x]
                (fn [curr]
                  (if (nil? curr)
                    1
                    (inc curr))))) {}
   (for [[x y] (map (juxt :time_millis :time-taken)
                    (prep-iis-log filename) )
         i (range x (+ x -1 (Integer/parseInt y) linger))]
     i)))

(defn output-iis-freq-data [filename outfile linger]
  (let [freq-analysis (do-freq-analysis filename linger)]
    (with-open [out-file (io/writer outfile)]
      (cd/write-csv out-file freq-analysis :delimiter \tab))))

;;(prep-iis-log "resources/u_ex160719.log")
;;(first (load-data "resources/u_ex160719.log" \tab))
;;(first (load-data "resources/u_ex160719.log" \space))

;;(assoc-in (first (load-data "resources/u_ex160719.log" \space)) [:time_millis] "xxx")

;;(map (fn [m] (assoc-in m [:time_millis] (Integer/parseInt (string/join (string/split (:time m) #":"))))) (take 2 (load-data "resources/u_ex160719.log" \space)) )

;;(reduce (fn [m x]
;;          (let [from (:time-millis x)
;;                to (+ from (:time-taken x))]
;;            (update-in m ))))
;;

;;(range 10 78 4)

;;(def m {})
;;(update-in m [1] (fn [x] ((fnil inc 0) x)))

;;(map (fn [a] (update-in m [a] (fn [x] ((fnil inc 0) x)))) [1 2 4 5 1 6 1])
;;(fnil m 0)


;;(sort (for [[x y] [[12 5] [15 12] [20 5]]
;;            i (range x (+ x y))]
;;        i))

;;(sort (for [[x y] (map (juxt :time_millis :time-taken) (take 20 (prep-iis-log "resources/u_ex160719.log") ))
;;i (range x (+ x (Integer/parseInt y)))
;;            ]
;;        i))


;;(map (juxt :time_millis :time-taken) (take 2 (prep-iis-log "resources/u_ex160719.log") ))


;;(reduce
;; (fn [m x]
;;   (update-in m [x]
;;              (fn [curr]
 ;;               (if (nil? curr)
;;                  1
;;                  (inc curr))))) {} [1 2 3 3 3 4 4 5 6])


;;(reduce
;; (fn [m x]
;;   (update-in m [x]
;;              (fn [curr]
;;                (if (nil? curr)
;;                  1
;;                  (inc curr))))) {}
;; (for [[x y] (map (juxt :time_millis :time-taken)
;;                  (take 2000 (prep-iis-log "resources/u_ex160719.log") ))
;;       i (range x (+ x (Integer/parseInt y)))]
;;   i))


;;(let [[h m s] (map #(Integer/parseInt %) (string/split "11:01:01" #":"))]
;;  (* (+ (* (+ (* h 60) m) 60) s) 1000))
