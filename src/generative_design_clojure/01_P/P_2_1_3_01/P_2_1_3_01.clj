(ns generative-design-clojure.01-P.P-2-1-3-01.P-2-1-3-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def tile-count-x 10)
(def tile-count-y 10)

(defn setup []
  {:act-random-seed 0})

(defn draw [{:keys [act-random-seed]}]
  (q/no-fill)
  (q/stroke 0 128)
  (q/background 255)
  (q/random-seed act-random-seed)
  (let [tile-width   (/ (q/width) tile-count-x)
        tile-height  (/ (q/height) tile-count-y)
        circle-count (inc (/ (q/mouse-x) 30))
        end-size     (q/map-range (q/mouse-x)
                                  0 (q/width)
                                  (/ tile-width 2) 0)
        end-offset   (q/map-range (q/mouse-y)
                                  0 (q/height)
                                  0 (/ (- tile-width end-size) 2))]
    (q/scale 1 (/ tile-height tile-width))
    (q/translate (/ tile-width 2)
                 (/ tile-height 2))
    (doseq [grid-y (range tile-count-y)]
      (doseq [grid-x (range tile-count-x)]
        (q/with-translation [(* tile-width grid-x)
                             (* tile-height grid-y)]
          (case (q/floor (q/random 4))
            0 (q/rotate (q/radians -90))
            1 (q/rotate 0)
            2 (q/rotate (q/radians 90))
            3 (q/rotate (q/radians 180)))
          (doseq [i (range circle-count)]
            (let [diameter (q/map-range i
                                        0 (dec circle-count)
                                        tile-width end-size)
                  offset   (q/map-range i
                                        0 (dec circle-count)
                                        0 end-offset)]
              (q/ellipse offset 0 diameter diameter))))))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(q/defsketch P-2-1-3-01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-pressed mouse-pressed
  :settings q/smooth)
