(ns generative-design-clojure.01-P.P-2-0-03.P-2-0-03
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-fill)
  (q/background 360)
  {:stroke-color (q/color 0 10)})

(defn draw [{:keys [stroke-color]}]
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
        (q/stroke-weight 2)
        (q/stroke stroke-color)
        (q/begin-shape)
        (doseq [i (range (inc circle-resolution))]
          (let [x (* radius (q/cos (* angle i)))
                y (* radius (q/sin (* angle i)))]
            (q/vertex x y)))
        (q/end-shape)))))

(defn key-released [state {:keys [key-code key]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 360))
  (case key
    :1 (assoc state :stroke-color (q/color 0 10))
    :2 (assoc state :stroke-color (q/color 192 100 64 10))
    :3 (assoc state :stroke-color (q/color 52 100 71 10))
    state))

(q/defsketch P-2-0-03
  :middleware [m/fun-mode]
  :size [720 720]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
