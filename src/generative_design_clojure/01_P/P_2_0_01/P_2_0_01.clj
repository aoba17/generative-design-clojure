(ns generative-design-clojure.01-P.P-2-0-01.P-2-0-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn draw [_]
  (q/stroke-cap :square)
  (q/no-fill)
  (q/background 255)

  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
    (let [circle-resolution (q/round (* (/ (q/mouse-y) (q/height))
                                        80))
          radius            (+ (- (q/mouse-x)
                                  (/ (q/width) 2))
                               0.5)
          angle             (q/radians (/ 360 circle-resolution))]
      (q/stroke-weight (/ (q/mouse-y) 20))
      (q/begin-shape)
      (loop [i 0]
        (when (<= i circle-resolution)
          (let [x (* radius (q/cos (* angle i)))
                y (* radius (q/sin (* angle i)))]
            (q/line 0 0 x y)
            ;; (q/vertex x y)
            )
          (recur (inc i))))
      (q/end-shape))))

(q/defsketch P-2-0-01
  :middleware [m/fun-mode]
  :size [550 550]
  :draw draw
  :key-pressed util/key-controller
  :settings q/smooth)
