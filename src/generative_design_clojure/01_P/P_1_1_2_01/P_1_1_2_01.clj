(ns generative-design-clojure.01-P.P-1-1-2-01.P-1-1-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/color-mode :hsb 360 (q/width) (q/height) 100)
  (q/no-stroke)
  {:segment-count 360})

(defn draw [{:keys [segment-count]}]
  (q/background 360)
  (let [angle-step (/ 360 segment-count)
        center-x   (/ (q/width) 2)
        center-y   (/ (q/height) 2)
        radius     center-x]
    (q/begin-shape :triangle-fan)
    (q/vertex center-x center-y)
    (loop [angle 0]
      (when (<= angle 360)
        (q/vertex (+ center-x
                     (* (q/cos (q/radians angle))
                        radius))
                  (+ center-y
                     (* (q/sin (q/radians angle))
                        radius)))
        (q/fill angle (q/mouse-x) (q/mouse-y))
        (recur (+ angle angle-step))))
    (q/end-shape)))

(defn key-control [state event]
  (case (:key event)
    :1 (assoc state :segment-count 360)
    :2 (assoc state :segment-count 45)
    :3 (assoc state :segment-count 24)
    :4 (assoc state :segment-count 12)
    :5 (assoc state :segment-count 6)
    state))

(q/defsketch P-1-1-2-01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-control)
