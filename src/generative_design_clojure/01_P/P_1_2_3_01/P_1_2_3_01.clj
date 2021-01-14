(ns generative-design-clojure.01-P.P-1-2-3-01.P-1-2-3-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def tile-count-x 50)
(def tile-count-y 10)
(def tile-count-x-seq (range tile-count-x))

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke)
  {:hue-values        (for [_ tile-count-x-seq]
                        (q/random 360))
   :saturation-values (for [_ tile-count-x-seq]
                        (q/random 100))
   :brightness-values (for [_ tile-count-x-seq]
                        (q/random 100))})

(defn draw [{:keys [hue-values saturation-values brightness-values]}]
  (q/background 0 0 100)
  (let [counter              (atom 0)
        current-tile-count-x (q/map-range (q/mouse-x)
                                          0 (q/width)
                                          1 tile-count-x)
        current-tile-count-y (q/map-range (q/mouse-y)
                                          0 (q/height)
                                          1 tile-count-y)
        tile-width           (/ (q/width) current-tile-count-x)
        tile-height          (/ (q/height) current-tile-count-y)]
    (doseq [grid-y (range tile-count-y)]
      (doseq [grid-x (range tile-count-x)]
        (let [index (mod @counter current-tile-count-x)]
          (q/fill (nth hue-values index)
                  (nth saturation-values index)
                  (nth brightness-values index))
          (q/rect (* grid-x tile-width)
                  (* grid-y tile-height)
                  (* tile-width (+ (q/random 1) 1))
                  tile-height)
          (swap! counter inc))))))

(defn key-released [state event]
  (case (:key event)
    :1 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   (q/random 360))
              :saturation-values (for [_ tile-count-x-seq]
                                   (q/random 100))
              :brightness-values (for [_ tile-count-x-seq]
                                   (q/random 100)))
    :2 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   (q/random 360))
              :saturation-values (for [_ tile-count-x-seq]
                                   (q/random 100))
              :brightness-values (for [_ tile-count-x-seq]
                                   100))
    :3 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   (q/random 360))
              :saturation-values (for [_ tile-count-x-seq]
                                   100)
              :brightness-values (for [_ tile-count-x-seq]
                                   (q/random 100)))
    :4 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   0)
              :saturation-values (for [_ tile-count-x-seq]
                                   0)
              :brightness-values (for [_ tile-count-x-seq]
                                   (q/random 100)))
    :5 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   195)
              :saturation-values (for [_ tile-count-x-seq]
                                   100)
              :brightness-values (for [_ tile-count-x-seq]
                                   (q/random 100)))
    :6 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   195)
              :saturation-values (for [_ tile-count-x-seq]
                                   (q/random 100))
              :brightness-values (for [_ tile-count-x-seq]
                                   100))
    :7 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   (q/random 180))
              :saturation-values (for [_ tile-count-x-seq]
                                   (q/random 80 100))
              :brightness-values (for [_ tile-count-x-seq]
                                   (q/random 50 90)))
    :8 (assoc state
              :hue-values        (for [_ tile-count-x-seq]
                                   (q/random 180 360))
              :saturation-values (for [_ tile-count-x-seq]
                                   (q/random 80 100))
              :brightness-values (for [_ tile-count-x-seq]
                                   (q/random 50 90)))
    :9 (assoc state
              :hue-values        (for [i tile-count-x-seq]
                                   (if (= (mod i 2) 0)
                                     (q/random 360)
                                     195))
              :saturation-values (for [i tile-count-x-seq]
                                   (if (= (mod i 2) 0)
                                     100
                                     (q/random 100)))
              :brightness-values (for [i tile-count-x-seq]
                                   (if (= (mod i 2) 0)
                                     (q/random 100)
                                     100)))
    :0 (assoc state
              :hue-values        (for [i tile-count-x-seq]
                                   (if (= (mod i 2) 0)
                                     192
                                     273))
              :saturation-values (for [_ tile-count-x-seq]
                                   (q/random 100))
              :brightness-values (for [i tile-count-x-seq]
                                   (if (= (mod i 2) 0)
                                     (q/random 10 100)
                                     (q/random 10 90))))
    state))

(q/defsketch P_1_2_3_01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released)
