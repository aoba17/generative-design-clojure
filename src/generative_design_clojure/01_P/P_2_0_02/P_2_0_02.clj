(ns generative-design-clojure.01-P.P-2-0-02.P-2-0-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/smooth)
  (q/no-fill)
  (q/background 255)
  (q/stroke-weight 2)
  (q/stroke 0 25))

(defn draw [_]
  (when (q/mouse-pressed?)
    (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
      (let [circle-resolution (q/floor (q/map-range
                                         (+ (q/mouse-y) 100)
                                         0 (q/height)
                                         2 10))
            radius            (+ (- (q/mouse-x)
                                    (/ (q/width) 2))
                                 0.5)
            angle             (q/radians (/ 360 circle-resolution))]
        (q/begin-shape)
        (loop [i 0]
          (when (<= i circle-resolution)
            (let [x (* radius (q/cos (* angle i)))
                  y (* radius (q/sin (* angle i)))]
              (q/vertex x y))
            (recur (inc i))))
        (q/end-shape)))))

(defn key-released [state {:keys [key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 255))
  state)

(q/defsketch P-2-0-01
  :middleware [m/fun-mode]
  :size [720 720]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released)
