(ns generative-design-clojure.01-P.P-1-2-1-01.P-1-2-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def tile-count-x 10)
(def tile-count-y 10)

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke)
  {:colors-left          (for [_ (range tile-count-y)]
                           (q/color (q/random 60)
                                    (q/random 100)
                                    100))
   :colors-right         (for [_ (range tile-count-y)]
                           (q/color (q/random 160 190)
                                    100
                                    (q/random 100)))
   :interpolate-shortest true})

(defn draw [{:keys [colors-left colors-right interpolate-shortest]}]
  (let [tile-width  (/ (q/width) tile-count-x)
        tile-height (/ (q/height) tile-count-y)]
    (loop [grid-y 0]
      (when (< grid-y tile-count-y)
        (let [color-1 (nth colors-left grid-y)
              color-2 (nth colors-right grid-y)]
          (loop [grid-x 0]
            (when (< grid-x tile-count-x)
              (let [amount (q/map-range grid-x 0 (- tile-count-x 1) 0 1)]
                (if interpolate-shortest
                  (do
                    (q/color-mode :rgb 255 255 255 255)
                    (q/fill (q/lerp-color color-1 color-2 amount))
                    (q/color-mode :hsb 360 100 100 100))
                  (q/fill (q/lerp-color color-1 color-2 amount)))
                (q/rect (* tile-width grid-x)
                        (* tile-height grid-y)
                        tile-width
                        tile-height))
              (recur (inc grid-x)))))
        (recur (inc grid-y))))))

(defn toggle-interpolate-shortest [state event]
  (case (:key event)
    :1 (assoc state :interpolate-shortest true)
    :2 (assoc state :interpolate-shortest false)
    state))

(q/defsketch P_1_2_1_01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released toggle-interpolate-shortest)
