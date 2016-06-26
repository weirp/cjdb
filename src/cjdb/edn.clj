(ns cjdb.edn
  (:require [clojure.java.io :refer [writer]]))

;; helper functions for writing and reading EDN data
(defn write-edn-data
  [data file-path]
  (with-open [w (clojure.java.io/writer file-path)]
    (binding [*out* w]
      (pr data))))

(defn read-edn-data
  [file-path]
  (with-open [r (java.io.PushbackReader. (clojure.java.io/reader file-path))]
    (binding [*read-eval* false]
      (read r))))
