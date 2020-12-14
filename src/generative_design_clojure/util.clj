(ns generative-design-clojure.util
  (:require [quil.core :as q]))

(defn time-stamp []
  (format
    "%04d%02d%02d_%02d%02d%02d"
    (q/year) (q/month) (q/day)
    (q/hour) (q/minute) (q/seconds)))

(defn save-frame []
  (q/save-frame (str (time-stamp) "_##.png")))

(defn key-controller [state {:keys [key]}]
  (case key
    :s (save-frame))
  state)
