(ns cjdb.config
  (:require [aero.core :as aero]))

(defn config
  ([] (let [profileFromEnv (:profileFromEnv (aero/read-config "config.edn"))]
        (config profileFromEnv)))
  ([profile] (aero/read-config "config.edn" {:profile profile})))

;;(config)

(aero/read-config "config.edn")
