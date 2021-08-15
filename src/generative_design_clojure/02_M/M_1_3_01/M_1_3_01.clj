(ns generative-design-clojure.02-M.M-1-3-01.M-1-3-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn draw [_]
  (q/background 255)
  (q/stroke 0 130 164)
  (q/stroke-weight 1)
  (q/stroke-join :round)
  (q/no-fill)
  (let [noise-range (/ (q/mouse-x) 10)]
    (q/begin-shape)
    (doseq [x (range 0 (q/width) 10)]
      (let [noise-x (q/map-range x 0 (q/width) 0 noise-range)
            y       (* (q/noise noise-x) (q/height))]
        (q/vertex x y)))
    (q/end-shape)))

(defn mouse-released [_ _]
  (q/noise-seed (q/random 100000)))

(defn key-released [state event]
  (case (:key event)
    :s (util/save-frame))
  state)

(q/defsketch M-1-3-01
  :middleware [m/fun-mode]
  :size [1024 256]
  :draw draw
  :mouse-released mouse-released
  :key-released key-released
  :settings q/smooth)
