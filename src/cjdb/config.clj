(ns cjdb.config
  (:require [aero.core :refer [read-config resource-resolver]]))

(defn config
  ([] (let [profileFromEnv (:profileFromEnv (read-config "config.edn" ))]
        (config profileFromEnv)))
  ([profile] (read-config "config.edn" {:profile profile})))

;;(config)

;;(aero/read-config "config.edn")
