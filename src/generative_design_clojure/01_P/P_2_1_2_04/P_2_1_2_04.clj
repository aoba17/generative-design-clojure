(ns generative-design-clojure.01-P.P-2-1-2-04.P-2-1-2-04
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def tile-count 20)
(def rect-size 30)

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  {:act-random-seed 0})

(defn draw [{:keys [act-random-seed]}]
  (q/background 360)
  (q/no-stroke)
  (q/fill 192 100 64 60)
  (q/random-seed act-random-seed)

  (loop [grid-y 0]
    (when (< grid-y tile-count)
      (loop [grid-x 0]
        (when (< grid-x tile-count)
          (let [pos-x    (* (/ (q/width) tile-count)
                            grid-x)
                pos-y    (* (/ (q/height) tile-count)
                            grid-y)
                shift-x1 (* (/ (q/mouse-x) 20) (q/random -1 1))
                shift-y1 (* (/ (q/mouse-y) 20) (q/random -1 1))
                shift-x2 (* (/ (q/mouse-x) 20) (q/random -1 1))
                shift-y2 (* (/ (q/mouse-y) 20) (q/random -1 1))
                shift-x3 (* (/ (q/mouse-x) 20) (q/random -1 1))
                shift-y3 (* (/ (q/mouse-y) 20) (q/random -1 1))
                shift-x4 (* (/ (q/mouse-x) 20) (q/random -1 1))
                shift-y4 (* (/ (q/mouse-y) 20) (q/random -1 1))]
            (q/begin-shape)
            (q/vertex (+ pos-x shift-x1)
                      (+ pos-y shift-y1))
            (q/vertex (+ pos-x shift-x2 rect-size)
                      (+ pos-y shift-y2))
            (q/vertex (+ pos-x shift-x3 rect-size)
                      (+ pos-y shift-y3 rect-size))
            (q/vertex (+ pos-x shift-x4)
                      (+ pos-y shift-y4 rect-size))
            (q/end-shape :close))
          (recur (inc grid-x))))
      (recur (inc grid-y)))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(q/defsketch P-2-1-2-04
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-pressed mouse-pressed
  :settings q/smooth)
