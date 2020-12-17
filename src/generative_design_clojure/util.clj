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

(defn re-map
  " Re-maps a number from one range to another.
  This works like Processing's map() function."
  [value start1 stop1 start2 stop2]
  (+ start2
     (* (- stop2 start2)
        (/ value (- stop1 start1)))))
