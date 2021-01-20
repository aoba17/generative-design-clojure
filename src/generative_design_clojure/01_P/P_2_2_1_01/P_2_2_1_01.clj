(ns generative-design-clojure.01-P.P-2-2-1-01.P-2-2-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def direction-map
  {0 :north
   1 :north-east
   2 :east
   3 :south-east
   4 :south
   5 :south-west
   6 :west
   7 :north-west})

(defn setup []
  (q/background 255)
  (q/no-stroke)
  {:pos-x     (atom (/ (q/width) 2))
   :pos-y     (atom (/ (q/height) 2))
   :step-size 1
   :diameter  1})

(defn draw [{:keys [pos-x pos-y step-size diameter]}]
  (q/fill 0 40)
  (doseq [_ (range (q/mouse-x))]
    (case (get direction-map
               (q/floor (q/random 8)))
      :north      (swap! pos-y - step-size)
      :north-east (do (swap! pos-x + step-size)
                      (swap! pos-y - step-size))
      :east       (swap! pos-x + step-size)
      :south-east (do (swap! pos-x + step-size)
                      (swap! pos-y + step-size))
      :south      (swap! pos-y + step-size)
      :south-west (do (swap! pos-x - step-size)
                      (swap! pos-y + step-size))
      :west       (swap! pos-x - step-size)
      :north-west (do (swap! pos-x - step-size)
                      (swap! pos-y - step-size)))
    (cond
      (> @pos-x (q/width))  (reset! pos-x 0)
      (< @pos-x 0)          (reset! pos-x (q/width))
      (< @pos-y 0)          (reset! pos-y (q/height))
      (> @pos-y (q/height)) (reset! pos-y 0))
    (q/ellipse (+ @pos-x (/ step-size 2))
               (+ @pos-y (/ step-size 2))
               diameter
               diameter)))

(defn key-released [state {:keys [key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 255))
  state)

(q/defsketch P-2-2-1-01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
