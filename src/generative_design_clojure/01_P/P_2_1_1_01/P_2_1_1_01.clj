(ns generative-design-clojure.01-P.P-2-1-1-01.P-2-1-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def tile-count 20)

(defn setup []
  {:act-stroke-cap  :round
   :act-random-seed 0})

(defn draw [{:keys [act-stroke-cap act-random-seed]}]
  (q/background 255)
  (q/no-fill)
  (q/stroke-cap act-stroke-cap)
  (q/random-seed act-random-seed)

  (doseq [grid-y (range tile-count)]
    (doseq [grid-x (range tile-count)]
      (let [tile-size-x (/ (q/width) tile-count)
            tile-size-y (/ (q/height) tile-count)
            pos-x       (* tile-size-x grid-x)
            pos-y       (* tile-size-y grid-y)
            toggle      (q/floor (q/random 2))]
        (when (= toggle 0)
          (q/stroke-weight (/ (q/mouse-x) 20))
          (q/line pos-x
                  pos-y
                  (+ pos-x tile-size-x)
                  (+ pos-y tile-size-y)))
        (when (= toggle 1)
          (q/stroke-weight (/ (q/mouse-y) 20))
          (q/line pos-x
                  (+ pos-y tile-size-y)
                  (+ pos-x tile-size-x)
                  pos-y))))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/floor (q/random 100000))))

(defn key-released [state event]
  (case (:key event)
    :1 (assoc state :act-stroke-cap :round)
    :2 (assoc state :act-stroke-cap :square)
    :3 (assoc state :act-stroke-cap :project)
    state))

(q/defsketch P-2-1-1-01
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
