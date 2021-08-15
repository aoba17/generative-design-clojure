(ns generative-design-clojure.02-M.M-1-2-01.M-1-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/cursor :cross)
  {:act-random-seed 0
   :count           150})

(defn draw [{:keys [act-random-seed
                    count]}]
  (q/background 255)
  (q/no-stroke)
  (let [fader-x (/ (q/mouse-x) (q/width))
        angle   (q/radians (/ 360 count))]
    (q/random-seed act-random-seed)
    (dotimes [i count]
      (let [random-x (q/random 0 (q/width))
            random-y (q/random 0 (q/height))
            circle-x (+ (/ (q/width) 2)
                        (* (q/cos (* angle i))
                           300))
            circle-y (+ (/ (q/height) 2)
                        (* (q/sin (* angle i))
                           300))
            x        (q/lerp random-x circle-x fader-x)
            y        (q/lerp random-y circle-y fader-x)]
        (q/fill 0 130 164)
        (q/ellipse y x 11 11)))))

(defn mouse-released [state _]
  (assoc state :act-random-seed (q/random 10000)))

(defn key-released [state event]
  (case (:key event)
    :s (util/save-frame))
  state)

(q/defsketch M-1-2-01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :mouse-released mouse-released
  :key-released key-released
  :settings q/smooth)
