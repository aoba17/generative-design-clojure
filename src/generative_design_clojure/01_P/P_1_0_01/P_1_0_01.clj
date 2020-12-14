(ns generative-design-clojure.01-P.P-1-0-01.P-1-0-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/no-cursor)
  (q/color-mode :hsb 360 100 100)
  (q/rect-mode :center)
  (q/no-stroke))

(defn draw [_]
  (q/background (/ (q/mouse-y) 2) 100 100)
  (q/fill (- 360 (/ (q/mouse-y) 2)) 100 100)
  (q/rect 360 360 (+ 1 (q/mouse-x)) (+ 1 (q/mouse-x))))

(q/defsketch P-1-0-01
  :middleware [m/fun-mode]
  :size [720 720]
  :setup setup
  :draw draw
  :key-pressed util/key-controller)
