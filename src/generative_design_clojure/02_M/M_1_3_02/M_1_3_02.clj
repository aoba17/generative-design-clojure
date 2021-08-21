(ns generative-design-clojure.02-M.M-1-3-02.M-1-3-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  {:act-random-seed 0})

(defn draw [{:keys [act-random-seed]}]
  (q/background 0)
  (q/random-seed act-random-seed)
  (dotimes [x (q/width)]
    (dotimes [y (q/height)]
      (q/set-pixel x y (q/color (q/random 255))))))

(defn mouse-released [state _]
  (assoc state :act-random-seed (q/random 100000)))

(defn key-released [state event]
  (case (:key event)
    :s (util/save-frame))
  state)

(q/defsketch M-1-3-02
  :middleware [m/fun-mode]
  :size [512 512]
  :setup setup
  :draw draw
  :mouse-released mouse-released
  :key-released key-released
  :settings q/smooth)
