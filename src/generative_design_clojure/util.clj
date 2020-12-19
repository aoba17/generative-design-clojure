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
    :s (save-frame)
    nil)
  state)

(defn sort-colors [colors & [method]]
  (case method
    :red        (->> colors (sort-by q/red) reverse)
    :green      (->> colors (sort-by q/green) reverse)
    :blue       (->> colors (sort-by q/blue) reverse)
    :hue        (->> colors (sort-by q/hue) reverse)
    :saturation (->> colors (sort-by q/saturation) reverse)
    :brightness (->> colors (sort-by q/brightness) reverse)
    :alpha      (->> colors (sort-by q/alpha) reverse)
    :grayscale  (->> colors
                     (sort-by
                       (fn [color]
                         (q/color (* (q/red color) 0.3)
                                  (* (q/green color) 0.59)
                                  (* (q/blue color) 0.11))))
                     reverse)
    colors))
