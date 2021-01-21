(ns generative-design-clojure.01-P.P-2-2-1-02.P-2-2-1-02
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
  (q/color-mode :hsb 360 100 100 100)
  (q/background 360)
  (q/no-stroke)
  {:pos-x     (atom (/ (q/width) 2))
   :pos-y     (atom (/ (q/height) 2))
   :draw-mode 1
   :step-size 1
   :diameter  1})

(defn draw [{:keys [pos-x pos-y draw-mode step-size diameter]}]
  (doseq [i (range (q/mouse-x))]
    (case (get direction-map
               (if (= draw-mode 2)
                 (q/floor (q/random 3))
                 (q/floor (q/random 7))))
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
    (when (= draw-mode 3)
      (when (= (mod i 100) 0)
        (q/fill 192 100 64 80)
        (q/ellipse (+ @pos-x (/ step-size 2))
                   (+ @pos-y (/ step-size 2))
                   (+ diameter 7)
                   (+ diameter 7))))
    (q/fill 0 40)
    (q/ellipse (+ @pos-x (/ step-size 2))
               (+ @pos-y (/ step-size 2))
               diameter
               diameter)))

(defn key-released [state {:keys [key key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 360))
  (case key
    :1 (assoc state
              :draw-mode 1
              :step-size 1
              :diameter 1)
    :2 (assoc state
              :draw-mode 2
              :step-size 1
              :diameter 1)
    :3 (assoc state
              :draw-mode 3
              :step-size 10
              :diameter 5)
    state))

(q/defsketch P-2-2-1-02
  :middleware [m/fun-mode]
  :size [550 550]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
