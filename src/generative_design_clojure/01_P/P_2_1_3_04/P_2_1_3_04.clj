(ns generative-design-clojure.01-P.P-2-1-3-04.P-2-1-3-04
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  {:draw-mode    1
   :tile-count-x 6
   :tile-count-y 6})

(defn draw [{:keys [draw-mode
                    tile-count-x
                    tile-count-y]}]
  (q/color-mode :hsb 360 100 100)
  (q/rect-mode :center)
  (q/stroke 0)
  (q/no-fill)
  (q/background 360)
  (let [loop-count  (+ (/ (q/mouse-x) 10) 10)
        para        (/ (q/mouse-y) (q/height))
        tile-width  (/ (q/width) tile-count-x)
        tile-height (/ (q/height) tile-count-y)]
    (doseq [grid-y (range tile-count-y)]
      (doseq [grid-x (range tile-count-x)]
        (let [pos-x (+ (* tile-width grid-x)
                       (/ tile-width 2))
              pos-y (+ (* tile-height grid-y)
                       (/ tile-height 2))]
          (q/with-translation [pos-x pos-y]
            (case draw-mode
              1 (doseq [_ (range loop-count)]
                  (q/rect 0 0 tile-width tile-height)
                  (q/scale (- 1 (/ 3 loop-count)))
                  (q/rotate (* para 0.1)))
              2 (doseq [i (range loop-count)]
                  (let [gradient (q/lerp-color
                                   (q/color 0 0 0 0)
                                   (q/color 52 100 71 200)
                                   (/ i loop-count))]
                    (q/fill
                      (q/hue gradient)
                      (q/saturation gradient)
                      (q/brightness gradient)
                      (* 200 (/ i loop-count))))
                  (q/no-stroke)
                  (q/rotate (/ (q/radians 180) 4))
                  (q/rect 0 0 tile-width tile-height)
                  (q/scale (- 1 (/ 3 loop-count)))
                  (q/rotate (* para 1.5)))
              3 (do (q/color-mode :rgb 255)
                    (q/no-stroke)
                    (doseq [i (range loop-count)]
                      (let [gradient (q/lerp-color
                                       (q/color 0 130 164)
                                       (q/color 255)
                                       (/ i loop-count))]
                        (q/fill (q/red gradient)
                                (q/green gradient)
                                (q/blue gradient)
                                170))
                      (q/with-translation [(* 4 i) 0]
                        (q/ellipse 0 0 (/ tile-width 4) (/ tile-height 4)))
                      (q/with-translation [(* -4 i) 0]
                        (q/ellipse 0 0 (/ tile-width 4) (/ tile-height 4)))
                      (q/scale (- 1 (/ 1.5 loop-count)))
                      (q/rotate (* para 1.5)))))))))))

(defn key-released [state event]
  (case (:key event)
    :1     (assoc state :draw-mode 1)
    :2     (assoc state :draw-mode 2)
    :3     (assoc state :draw-mode 3)
    :down  (update state :tile-count-y #(max (dec %) 1))
    :up    (update state :tile-count-y inc)
    :left  (update state :tile-count-x #(max (dec %) 1))
    :right (update state :tile-count-x inc)
    state))

(q/defsketch P-2-1-3-04
  :middleware [m/fun-mode]
  :size [550 550]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
