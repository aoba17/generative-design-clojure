(ns generative-design-clojure.02-M.M-1-3-03.M-1-3-03
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/cursor :cross)
  (q/frame-rate 1)
  {:octaves    4
   :falloff    0.5
   :noise-mode 1})

(defn draw [{:keys [octaves
                    falloff
                    noise-mode]}]
  (q/background 0)
  (q/noise-detail octaves falloff)
  (let [noise-range-x (/ (q/mouse-x) 10)
        noise-range-y (/ (q/mouse-y) 10)]
    (dotimes [x (q/width)]
      (dotimes [y (q/height)]
        (let [noise-x     (q/map-range x 0 (q/width) 0 noise-range-x)
              noise-y     (q/map-range y 0 (q/height) 0 noise-range-y)
              noise-value (case noise-mode
                            1 (* (q/noise noise-x noise-y) 255)
                            2 (let [n (* (q/noise noise-x noise-y) 24)]
                                (* (- n (q/floor n)) 255)))]
          (q/set-pixel x y (q/color noise-value)))))))

(defn key-released [state event]
  (case (:key event)
    :s     (do (util/save-frame)
               state)
    :space (do (q/noise-seed (q/random 100000))
               state)
    :1     (assoc state :noise-mode 1)
    :2     (assoc state :noise-mode 2)
    state))

(defn key-pressed [state event]
  (case (:key event)
    :up    (update state :falloff #(min 1.0 (+ % 0.05)))
    :down  (update state :falloff #(max 0.0 (- % 0.05)))
    :left  (update state :octaves #(max 0 (dec %)))
    :right (update state :octaves inc)
    state))

(q/defsketch M-1-3-03
  :middleware [m/fun-mode]
  :size [512 512]
  :setup setup
  :draw draw
  :key-released key-released
  :key-pressed key-pressed
  :settings q/smooth)
