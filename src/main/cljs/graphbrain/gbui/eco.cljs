(ns graphbrain.gbui.eco
  (:require [jayq.core :as jq])
  (:use [jayq.core :only [$]]))

(defn init-eco!
  []
  (.attr ($ "#top-input-field") "action" "/eco")
  (.attr ($ "#top-input-field") "method" "POST"))
