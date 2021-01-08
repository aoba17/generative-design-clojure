(ns generative-design-clojure.01-P.P-2-1-2-01.P-2-1-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/no-fill)
  {:tile-count      20
   :circle-color    (q/color 0)
   :circle-alpha    180
   :act-random-seed 0})

(defn draw [{:keys [tile-count
                    circle-color
                    circle-alpha
                    act-random-seed]}]
  (q/translate (/ (q/width) tile-count 2)
               (/ (q/height) tile-count 2))
  (q/background 255)
  (q/random-seed act-random-seed)
  (q/stroke (q/red circle-color)
            (q/green circle-color)
            (q/blue circle-color)
            circle-alpha)
  (q/stroke-weight (/ (q/mouse-y) 60))

  (loop [grid-y 0]
    (when (< grid-y tile-count)
      (loop [grid-x 0]
        (when (< grid-x tile-count)
          (let [pos-x   (* (/ (q/width) tile-count) grid-x)
                pos-y   (* (/ (q/height) tile-count) grid-y)
                shift-x (/ (q/random (- (q/mouse-x)) (q/mouse-x)) 20)
                shift-y (/ (q/random (- (q/mouse-x)) (q/mouse-x)) 20)]
            (q/ellipse (+ pos-x shift-x)
                       (+ pos-y shift-y)
                       (/ (q/mouse-y) 15)
                       (/ (q/mouse-y) 15)))
          (recur (inc grid-x))))
      (recur (inc grid-y)))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(q/defsketch P-2-1-2-01
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-pressed mouse-pressed
  :settings q/smooth)
