(ns generative-design-clojure.02-M.M-1-1-01.M-1-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  {:act-random-seed 42})

(defn draw [{:keys [act-random-seed]}]
  (q/background 255)

  (q/stroke 0 130 164)
  (q/stroke-weight 1)
  (q/stroke-join :round)
  (q/no-fill)

  (q/random-seed act-random-seed)
  (q/begin-shape)
  (doseq [x (->> (q/width)
                 inc
                 range
                 (filter #(= (mod % 10) 0)))]
    (q/vertex x (q/random 0 (q/height))))
  (q/end-shape)

  (q/no-stroke)
  (q/fill 0)

  (q/random-seed act-random-seed)
  (q/begin-shape)
  (doseq [x (->> (q/width)
                 inc
                 range
                 (filter #(= (mod % 10) 0)))]
    (q/ellipse x (q/random 0 (q/height)) 3 3))
  (q/end-shape))

(defn mouse-released [state _]
  (assoc state :act-random-seed (q/random 100000)))

(defn key-released [state event]
  (case (:key event)
    :s (util/save-frame))
  state)

(q/defsketch M-1-1-01
  :middleware [m/fun-mode]
  :size [1024 256]
  :setup setup
  :draw draw
  :mouse-released mouse-released
  :key-released key-released
  :settings q/smooth)
